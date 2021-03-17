package org.geektimes.context;

import org.geektimes.function.ThrowableAction;
import org.geektimes.function.ThrowableFunction;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.naming.*;
import javax.servlet.ServletContext;
import javax.sql.DataSource;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Stream;

/**
 * @author GH
 */
public class ComponentContext {

    private static Logger logger = Logger.getLogger(ComponentContext.class.getName());

    private static final String COMPONENT_ENV_CONTEXT_NAME = "java:comp/env";

    public static final String CONTEXT_NAME = ComponentContext.class.getName();

    private static ServletContext servletContext;

    private ClassLoader classLoader;

    private Context envContext;

    private Map<String, Object> componentsMap = new LinkedHashMap<>();

    /**
     * 获取 ComponentContext
     *
     * @return
     */
    public static ComponentContext getInstance() {
        return (ComponentContext) servletContext.getAttribute(CONTEXT_NAME);
    }

    /**
     * 通过名称进行依赖查找
     *
     * @param name
     * @param <C>
     * @return
     */
    public <C> C getComponent(String name) {
        return (C) componentsMap.get(name);
    }

    public void init(ServletContext servletContext) {
        ComponentContext.servletContext = servletContext;
        servletContext.setAttribute(CONTEXT_NAME, this);

        this.classLoader = servletContext.getClassLoader();
        // 初始化环境信息
        initEnvContext();

        try {
            DataSource lookup = (DataSource) envContext.lookup("jdbc/UserPlatformDB");
        } catch (NamingException e) {
            e.printStackTrace();
        }

        // 实例化
        instantiateComponents();


        // 初始化
        initializeComponents();

    }


    private void initEnvContext() throws RuntimeException {
        if (this.envContext != null) {
            return;
        }
        Context context = null;
        try {
            context = new InitialContext();
            this.envContext = (Context) context.lookup(COMPONENT_ENV_CONTEXT_NAME);
        } catch (NamingException e) {
            throw new RuntimeException(e);
        } finally {
            close(context);
        }
    }

    private static void close(Context context) {
        if (context != null) {
            ThrowableAction.execute(context::close);
        }
    }

    private void instantiateComponents() {
        // 遍历获取所有的组件名称
        List<String> componentNames = listAllComponentNames();
        // 通过依赖查找，实例化对象（ Tomcat BeanFactory setter 方法的执行，仅支持简单类型）
        componentNames.forEach(name -> componentsMap.put(name, lookupComponent(name)));
    }

    private <C> C lookupComponent(String name) {
        return executeInContext(context -> (C) context.lookup(name));
    }

    private List<String> listAllComponentNames() {
        return listComponentNames("/");
    }

    private List<String> listComponentNames(String name) {

        return executeInContext(context -> {
            NamingEnumeration<NameClassPair> namingEnumeration
                    = executeInContext(context, con -> con.list(name), false);

            if (namingEnumeration == null) { // 当前 JNDI 名称下没有子节点
                return Collections.emptyList();
            }

            List<String> fullNames = new LinkedList<>();
            while (namingEnumeration.hasMoreElements()) {
                NameClassPair element = namingEnumeration.next();
                String className = element.getClassName();
                logger.info("className:" + className);

                Class<?> clazz = classLoader.loadClass(className);
                //  判断 clazz 是否为 Context的子类
                if (Context.class.isAssignableFrom(clazz)) {
                    fullNames.addAll(listComponentNames(element.getName()));
                } else {
                    // 否则，当前名称绑定目标类型的话话，添加该名称到集合中
                    String fullName = name.startsWith("/") ?
                            element.getName() : name + "/" + element.getName();
                    fullNames.add(fullName);
                }


            }

            return fullNames;
        });
    }


    protected <R> R executeInContext(ThrowableFunction<Context, R> function) {
        return executeInContext(function, false);
    }

    protected <R> R executeInContext(ThrowableFunction<Context, R> function, boolean ignoredException) {
        return executeInContext(this.envContext, function, ignoredException);
    }

    private <R> R executeInContext(Context context, ThrowableFunction<Context, R> function, boolean ignoredException) {
        R result = null;
        try {
            result = ThrowableFunction.execute(context, function);
        } catch (Throwable e) {
            if (ignoredException) {
                logger.warning(e.getMessage());
            } else {
                throw new RuntimeException(e);
            }
        }

        return result;
    }


    private void initializeComponents() {
        componentsMap.values().forEach(component -> {
            Class<?> componentClass = component.getClass();
            // 注入阶段 - {@link Resource}
            injectComponents(component, componentClass);
            // 初始阶段 - {@link PostConstruct}
            processPostConstruct(component, componentClass);
            // TODO 实现销毁阶段 - {@link PreDestroy}
            processPreDestroy(component, componentClass);
        });
    }


    private void injectComponents(Object component, Class<?> componentClass) {
        Stream.of(componentClass.getDeclaredFields())
                .filter(field -> {
                    int mods = field.getModifiers();
                    return !Modifier.isStatic(mods) &&
                            field.isAnnotationPresent(Resource.class);
                })
                .forEach(field -> {
                    Resource resource = field.getAnnotation(Resource.class);
                    String resourceName = resource.name();
                    Object injectedObject = lookupComponent(resourceName);
                    field.setAccessible(true);
                    try {
                        // 注入目标对象
                        field.set(component, injectedObject);
                    } catch (IllegalAccessException e) {
                    }
                });
    }

    private void processPostConstruct(Object component, Class<?> componentClass) {
        Stream.of(componentClass.getMethods())
                .filter(method ->
                        !Modifier.isStatic(method.getModifiers()) &&      // 非 static
                                method.getParameterCount() == 0 &&        // 没有参数
                                method.isAnnotationPresent(PostConstruct.class) // 标注 @PostConstruct
                ).forEach(method -> {
            // 执行目标方法
            try {
                method.invoke(component);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void processPreDestroy(Object component, Class<?> componentClass) {
        Stream.of(componentClass.getMethods())
                .filter(method -> {
                    return !Modifier.isStatic(method.getModifiers())
                            && method.getParameterCount() == 0
                            && method.isAnnotationPresent(PreDestroy.class);
                })
                .forEach(method -> {
                    try {
                        method.invoke(component);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });
    }

}
