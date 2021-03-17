package org.geektimes.projects.user.validation.validatation;

import org.apache.commons.lang.StringUtils;
import org.geektimes.projects.user.validation.annotation.PhoneNumber;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

/**
 * @author GH
 */
public class PhoneNumberAnnotationValidator implements ConstraintValidator<PhoneNumber, String> {

    private static final Pattern CHINA_PATTERN = Pattern.compile("^((13[0-9])|(14[0,1,4-9])|(15[0-3,5-9])|(16[2,5,6,7])|(17[0-8])|(18[0-9])|(19[0-3,5-9]))\\d{8}$");

    // 初始化操作
    @Override
    public void initialize(PhoneNumber constraintAnnotation) {

    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (StringUtils.isBlank(value)) {
            return false;
        }

        return CHINA_PATTERN.matcher(value).matches();
    }
}
