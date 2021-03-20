package jdk.parameterizedtype;

import org.junit.Test;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * @author GH
 */
public class ParameterizedTypeTest {

    /**
     * 打印结果：
     * listString[java.util.List<java.lang.String>]：true
     * list[interface java.util.List]：false
     * mapStringLong[java.util.Map<java.lang.String, java.lang.Long>]：true
     * map[interface java.util.Map]：false
     * mapEntry[java.util.Map$Entry<java.lang.Long, java.lang.Short>]：true
     */
    @Test
    public void testGenericType() {
        Field[] fields = ParameterizedBean.class.getDeclaredFields();
        for (Field field : fields) {
            //是否是 ParameterizedType ,带泛型的全部为ture
            // field.getGenericType() 获取泛型，例如：java.util.List<java.lang.String>
            System.out.println(field.getName() + "[" + field.getGenericType() + "]：" + (field.getGenericType() instanceof ParameterizedType));
        }
    }

    /**
     * 打印结果：
     * 变量：java.util.List<java.lang.String>     类型：java.lang.String
     * 变量：java.util.Map<java.lang.String, java.lang.Long>     类型：java.lang.String类型：java.lang.Long
     * 变量：java.util.Map$Entry<java.lang.Long, java.lang.Short>     类型：java.lang.Long类型：java.lang.Short
     */
    @Test
    public void testActualTypeArguments() {
        Field[] fields = ParameterizedBean.class.getDeclaredFields();
        for (Field field : fields) {
            if (field.getGenericType() instanceof ParameterizedType) {
                ParameterizedType type = (ParameterizedType) field.getGenericType();
                System.out.print("变量：" + type.getTypeName() + "     ");
                // 实际类型集合
                Type[] types = type.getActualTypeArguments();
                for (Type t : types) {
                    // 例如：java.lang.Long
                    System.out.print("类型：" + t.getTypeName());
                }
                System.out.println("");
            }
        }

    }

    /**
     * 变量：listString;   RawType：java.util.List
     * 变量：mapStringLong;   RawType：java.util.Map
     * 变量：mapEntry;   RawType：java.util.Map$Entry
     */
    @Test
    public void testRawType() {
        Field[] fields = ParameterizedBean.class.getDeclaredFields();
        for (Field field : fields) {
            if (field.getGenericType() instanceof ParameterizedType) {
                ParameterizedType genericType = (ParameterizedType) field.getGenericType();
                System.out.print("变量：" + field.getName() + ";   ");
                System.out.print("RawType：" + genericType.getRawType().getTypeName());
            }
            System.out.println("");
        }
    }

    /**
     * 打印结果：
     * 变量：listString;  OwnerType:Null
     * 变量：mapStringLong;  OwnerType:Null
     * 变量：mapEntry;  OwnerType：java.util.Map
     */
    @Test
    public void testOwnerType() {
        Field[] fields = ParameterizedBean.class.getDeclaredFields();
        for (Field f : fields) {
            if (f.getGenericType() instanceof ParameterizedType) {
                ParameterizedType parameterizedType = (ParameterizedType) f.getGenericType();
                System.out.print("变量：" + f.getName() + ";  ");
                Type t = parameterizedType.getOwnerType();
                if (t == null) {
                    System.out.print("OwnerType:Null     ");
                } else {
                    System.out.print("OwnerType：" + t.getTypeName());
                }

                System.out.println("");
            }
        }
    }
}
