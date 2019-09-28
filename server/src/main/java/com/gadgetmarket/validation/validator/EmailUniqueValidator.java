package com.gadgetmarket.validation.validator;

import com.gadgetmarket.repository.UserRepository;
import com.gadgetmarket.validation.annotation.EmailUnique;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class EmailUniqueValidator implements ConstraintValidator<
        EmailUnique, String> {

    private UserRepository userRepository;

    public EmailUniqueValidator(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void initialize(final EmailUnique constraint) {
    }

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        return !userRepository.existsByEmail(email);
    }

}
