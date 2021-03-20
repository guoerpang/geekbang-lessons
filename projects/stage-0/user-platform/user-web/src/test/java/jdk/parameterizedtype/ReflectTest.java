package jdk.parameterizedtype;

import jdk.parameterizedtype.user.User;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.stream.Stream;

/**
 * @author GH
 */
public class ReflectTest {
    @Test
    public void testDeclaredFields() {
        Field[] declaredFields = User.class.getDeclaredFields();
        // userName
        // publicUserName
        Stream.of(declaredFields).forEach(field -> System.out.println(field.getName()));

        System.out.println("===========我是分割线=============");

        // publicUserName
        // publicCorpCode
        Field[] fields = User.class.getFields();
        Stream.of(fields).forEach(field -> System.out.println(field.getName()));
    }
}
