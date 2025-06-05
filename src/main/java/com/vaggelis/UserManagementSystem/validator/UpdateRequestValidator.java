package com.vaggelis.UserManagementSystem.validator;


import com.vaggelis.UserManagementSystem.dtos.UpdateRequest;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import java.util.regex.Pattern;

public class UpdateRequestValidator implements Validator {

    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$"
    );

    @Override
    public boolean supports(Class<?> clazz) {
        return UpdateRequest.class == clazz;
    }

    @Override
    public void validate(Object target, Errors errors) {
        UpdateRequest request = (UpdateRequest) target;

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "uname", "uname.empty", "Username is required.");
        if (request.getUname() != null && (request.getUname().length() < 3 || request.getUname().length() > 16)) {
            errors.rejectValue("uname", "uname.size", "Username must be between 3 and 16 characters.");
        }

        // --- Password ---
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "password.empty", "Password is required.");
        if (request.getPassword() != null && (request.getPassword().length() < 3 || request.getPassword().length() > 16)) {
            errors.rejectValue("password", "password.size", "Password must be between 3 and 16 characters.");
        }

        // --- Email ---
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email", "email.empty", "Email is required.");
        if (request.getEmail() != null && !EMAIL_PATTERN.matcher(request.getEmail()).matches()) {
            errors.rejectValue("email", "email.invalid", "Invalid email format.");
        }
    }
}
