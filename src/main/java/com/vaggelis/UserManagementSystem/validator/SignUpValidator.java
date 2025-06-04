package com.vaggelis.UserManagementSystem.validator;

import com.vaggelis.UserManagementSystem.dtos.SignUpRequest;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import java.util.regex.Pattern;

@Component
public class SignUpValidator implements Validator {

    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$"
    );

    @Override
    public boolean supports(Class<?> clazz) {
        return SignUpRequest.class == clazz;
    }

    @Override
    public void validate(Object target, Errors errors) {
        SignUpRequest request = (SignUpRequest) target;

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "username", "empty");
        if (request.getUname().length()<3 || request.getUname().length() > 16){
            errors.reject("username", "size");
        }

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "empty");
        if (request.getPassword().length()<3 || request.getPassword().length() > 16){
            errors.reject("password", "size");
        }

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email", "empty");
        if (request.getEmail() != null && !EMAIL_PATTERN.matcher(request.getEmail()).matches()) {
            errors.rejectValue("email", "invalid", "Invalid email format.");
        }
    }
}
