package org.geektimes.web.mvc.myannotation;

import java.lang.annotation.*;

/**
 * @author GH
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MyService {

    String value() default "";

}
