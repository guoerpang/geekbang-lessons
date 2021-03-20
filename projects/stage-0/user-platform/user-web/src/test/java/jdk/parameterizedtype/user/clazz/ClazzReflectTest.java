package jdk.parameterizedtype.user.clazz;

import jdk.parameterizedtype.user.impl.UserServiceImpl;
import org.junit.Test;

import java.lang.reflect.Type;
import java.util.stream.Stream;

/**
 * @author GH
 */
public class ClazzReflectTest {

    @Test
    public void testGenericInterfaces() {
        Type[] genericInterfaces = UserServiceImpl.class.getGenericInterfaces();
        Stream.of(genericInterfaces).forEach(type -> System.out.println("TypeName:" + type.getTypeName()));
    }

}
