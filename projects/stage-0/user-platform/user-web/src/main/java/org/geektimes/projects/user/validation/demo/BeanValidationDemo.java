package org.geektimes.projects.user.validation.demo;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;
import java.util.concurrent.CountDownLatch;

public class BeanValidationDemo {

    public static void main(String[] args) {
//        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
//        // cache the factory somewhere
//        Validator validator = factory.getValidator();
//
//        User user = new User();
//        user.setPassword("***");
//        user.setPhoneNumber("12345");
//
//        // 校验结果
//        Set<ConstraintViolation<User>> violations = validator.validate(user);
//
//        violations.forEach(c -> {
//            System.out.println(c.getPropertyPath() + ":" + c.getMessage());
//        });

        ThreadLocal<String> threadLocal = new ThreadLocal<String>(){
            @Override
            protected String initialValue() {
                return "aa";
            }
        };

        String str = threadLocal.get();
        System.out.println(str);

    }
}
