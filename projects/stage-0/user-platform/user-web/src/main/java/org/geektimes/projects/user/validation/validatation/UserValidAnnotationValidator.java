package org.geektimes.projects.user.validation.validatation;

import org.geektimes.projects.user.domain.User;
import org.geektimes.projects.user.validation.annotation.UserValid;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UserValidAnnotationValidator implements ConstraintValidator<UserValid, User> {

    private int idRange;

    @Override
    public void initialize(UserValid annotation) {
        this.idRange = annotation.idRange();
    }

    @Override
    public boolean isValid(User value, ConstraintValidatorContext context) {

        // 获取模板信息
        context.getDefaultConstraintMessageTemplate();


        return false;
    }
}
