package org.geektimes.web.mvc;

import org.apache.commons.lang.StringUtils;
import org.geektimes.web.mvc.controller.Controller;
import org.geektimes.web.mvc.controller.PageController;
import org.geektimes.web.mvc.controller.RestController;
import org.geektimes.web.mvc.myannotation.MyComponent;
import org.geektimes.web.mvc.myannotation.MyController;
import org.geektimes.web.mvc.myannotation.MyRepository;
import org.geektimes.web.mvc.myannotation.MyService;

import javax.annotation.Resource;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.HttpMethod;
import javax.ws.rs.Path;
import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.*;

import static java.util.Arrays.asList;
import static org.apache.commons.lang.StringUtils.substringAfter;

@MyService
public class FrontControllerServlet extends HttpServlet {

    private List<String> classPathList = new ArrayList<>();

    private Map<String, Object> beanNameMap = new HashMap<>();

    /**
     * 请求路径和 Controller 的映射关系缓存
     */
    private Map<String, Controller> controllersMapping = new HashMap<>();

    /**
     * 请求路径和 {@link HandlerMethodInfo} 映射关系缓存
     */
    private Map<String, HandlerMethodInfo> handleMethodInfoMapping = new HashMap<>();

    /**
     * 初始化 Servlet
     *
     * @param servletConfig
     */
    @Override
    public void init(ServletConfig servletConfig) {
        // 扫描所有的包
        doScanner(servletConfig.getInitParameter("scanPackage"));

        // 所有注解类存放到容器中
        doInstanceBean();

        // 依赖注入
        doAutowired();

        doHandleController();

//        initHandleMethods();
    }

    private void doHandleController() {
        for (Map.Entry<String, Object> entry : beanNameMap.entrySet()) {
            Object object = entry.getValue();
            Class<?> clazz = object.getClass();
            if (!clazz.isAnnotationPresent(MyController.class)) {
                continue;
            }

            Path pathFromClass = clazz.getAnnotation(Path.class);
            String basePath = pathFromClass.value();
            Method[] publicMethods = clazz.getDeclaredMethods();
            // 处理方法支持的 HTTP 方法集合

            for (Method method : publicMethods) {
                Set<String> supportedHttpMethods = findSupportedHttpMethods(method);
                Path pathFromMethod = method.getAnnotation(Path.class);
                String requestPath = "";
                if (pathFromMethod != null) {
                    requestPath = basePath + pathFromMethod.value();
                }
                handleMethodInfoMapping.put(requestPath,
                        new HandlerMethodInfo(requestPath, method, supportedHttpMethods, object));
            }
            controllersMapping.put(basePath, (Controller) object);

        }
    }

    private void doAutowired() {
        for (Map.Entry<String,Object> entry : beanNameMap.entrySet()) {
            try {
                Object clazz = entry.getValue();
                Field[] declaredFields = clazz.getClass().getDeclaredFields();
                for (Field declaredField : declaredFields) {
                    if (declaredField.isAnnotationPresent(Resource.class)) {
                        String name = declaredField.getName();
                        Object fieldObject = beanNameMap.get(name);

                        declaredField.setAccessible(true);
                        declaredField.set(clazz,fieldObject);
                    }
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }


    }

    private void doInstanceBean() {
        for (String classPath : classPathList) {
            try {
                Class<?> clazz = Class.forName(classPath);
                if (clazz.isAnnotationPresent(MyComponent.class)
                        || clazz.isAnnotationPresent(MyService.class)
                        || clazz.isAnnotationPresent(MyRepository.class)
                        || clazz.isAnnotationPresent(MyController.class)) {
                    Object object = clazz.newInstance();
                    String simpleName = clazz.getSimpleName();
                    char[] charsSimpleName = simpleName.toCharArray();
                    charsSimpleName[0] += 32;
                    beanNameMap.put(String.valueOf(charsSimpleName),object);

                    Class<?>[] interfaces = clazz.getInterfaces();
                    Class<?> clazzInterface = interfaces[0];
                    String simpleNameInterface = clazzInterface.getSimpleName();
                    char[] chars = simpleNameInterface.toCharArray();
                    chars[0] += 32;
                    beanNameMap.put(String.valueOf(chars),object);
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }



    /**
     * 扫描所有符合条件的类
     */
    private void doScanner(String scanPackage) {
        String scanPackageUrl = "/" + scanPackage.replaceAll("\\.", "/");
        URL resource = this.getClass().getClassLoader().getResource(scanPackageUrl);
        File file = new File(resource.getFile());
        File[] files = file.listFiles();
        for (File filePackage : files) {
            String fileName = filePackage.getName();

            // 如果为文件夹
            if (filePackage.isDirectory()) {
                doScanner(scanPackage + "." + fileName);
                continue;
            }

            // 不以class结尾
            if (!fileName.endsWith(".class")) {
                continue;
            }

            classPathList.add(scanPackage + "." + fileName.replace(".class", ""));
        }
    }

    /**
     * 读取所有的 RestController 的注解元信息 @Path
     * 利用 ServiceLoader 技术（Java SPI）
     */
    private void initHandleMethods() {
        for (Controller controller : ServiceLoader.load(Controller.class)) {
            Class<?> controllerClass = controller.getClass();
            Path pathFromClass = controllerClass.getAnnotation(Path.class);
            String basePath = pathFromClass.value();
            Method[] publicMethods = controllerClass.getDeclaredMethods();
            // 处理方法支持的 HTTP 方法集合

            for (Method method : publicMethods) {
                Set<String> supportedHttpMethods = findSupportedHttpMethods(method);
                Path pathFromMethod = method.getAnnotation(Path.class);
                String requestPath = "";
                if (pathFromMethod != null) {
                    requestPath = basePath + pathFromMethod.value();
                }
                handleMethodInfoMapping.put(requestPath,
                        new HandlerMethodInfo(requestPath, method, supportedHttpMethods, controllerClass));
            }
            controllersMapping.put(basePath, controller);
        }
    }

    /**
     * 获取处理方法中标注的 HTTP方法集合
     *
     * @param method 处理方法
     * @return
     */
    private Set<String> findSupportedHttpMethods(Method method) {
        Set<String> supportedHttpMethods = new LinkedHashSet<>();
        for (Annotation annotationFromMethod : method.getAnnotations()) {
            HttpMethod httpMethod = annotationFromMethod.annotationType().getAnnotation(HttpMethod.class);
            if (httpMethod != null) {
                supportedHttpMethods.add(httpMethod.value());
            }
        }

        if (supportedHttpMethods.isEmpty()) {
            supportedHttpMethods.addAll(asList(HttpMethod.GET, HttpMethod.POST,
                    HttpMethod.PUT, HttpMethod.DELETE, HttpMethod.HEAD, HttpMethod.OPTIONS));
        }

        return supportedHttpMethods;
    }

    /**
     * SCWCD
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @Override
    public void service(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // 建立映射关系
        // requestURI = /a/hello/world
        String requestURI = request.getRequestURI();
        // contextPath  = /a or "/" or ""
        String servletContextPath = request.getContextPath();
        String prefixPath = substringAfter(requestURI,
                StringUtils.replace(servletContextPath, "//", "/"));
        // 映射路径（子路径）
        String requestMappingPath = StringUtils.substring(prefixPath, 0, StringUtils.lastIndexOf(prefixPath, "/"));
        // 映射到 Controller
        Controller controller = controllersMapping.get(requestMappingPath);

        if (controller != null) {
            HandlerMethodInfo handlerMethodInfo = handleMethodInfoMapping.get(prefixPath);
            try {
                if (handlerMethodInfo != null) {
                    String httpMethod = request.getMethod();

                    if (!handlerMethodInfo.getSupportedHttpMethods().contains(httpMethod)) {
                        // HTTP 方法不支持
                        response.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
                        return;
                    }
                    if (controller instanceof PageController) {
                        Method handlerMethod = handlerMethodInfo.getHandlerMethod();
                        String viewPath = (String) handlerMethod.invoke(handlerMethodInfo.getObject(), request, response);
//                        PageController pageController = PageController.class.cast(controller);
//                        // 执行方法
//                        String viewPath = pageController.execute(request, response);
                        // 页面请求 forward
                        // request -> RequestDispatcher forward
                        // RequestDispatcher requestDispatcher = request.getRequestDispatcher(viewPath);
                        // ServletContext -> RequestDispatcher forward
                        // ServletContext -> RequestDispatcher 必须以 "/" 开头
                        ServletContext servletContext = request.getServletContext();
                        if (!viewPath.startsWith("/")) {
                            viewPath = "/" + viewPath;
                        }
                        RequestDispatcher requestDispatcher = servletContext.getRequestDispatcher(viewPath);
                        requestDispatcher.forward(request, response);
                        return;
                    } else if (controller instanceof RestController) {
                        Method handlerMethod = handlerMethodInfo.getHandlerMethod();
                        Object result = (Object) handlerMethod.invoke(handlerMethodInfo.getObject(), request, response);
                        response.getWriter().println(result);
                        return;
                    }

                }
            } catch (Throwable throwable) {
                if (throwable.getCause() instanceof IOException) {
                    throw (IOException) throwable.getCause();
                } else {
                    throw new ServletException(throwable.getCause());
                }
            }
        }
    }

//    private void beforeInvoke(Method handleMethod, HttpServletRequest request, HttpServletResponse response) {
//
//        CacheControl cacheControl = handleMethod.getAnnotation(CacheControl.class);
//
//        Map<String, List<String>> headers = new LinkedHashMap<>();
//
//        if (cacheControl != null) {
//            CacheControlHeaderWriter writer = new CacheControlHeaderWriter();
//            writer.write(headers, cacheControl.value());
//        }
//    }
}
