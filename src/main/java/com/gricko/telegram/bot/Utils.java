package com.gricko.telegram.bot;

import org.apache.commons.validator.routines.EmailValidator;

public class Utils {
    public static boolean isValidEmail(String email){
        return EmailValidator.getInstance().isValid(email);
    }
}
