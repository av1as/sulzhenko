package com.sulzhenko.Util.validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * InputValidator class to check validity of input data
 *
 * @author Artem Sulzhenko
 * @version 1.0
 */
public class InputValidator {
    /**
     * Constants describing requirements for input fields and containing regular expressions.
     */
    public static final String PASSWORD_REGEX = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%]).{8,20}$";
    public static final String PASSWORD_MESSAGE = "Password must contain numeric, lowercase and "
            + "uppercase character, special symbol among @#$% and must consist of 8-20 symbols";
    public static final String PASSWORD_ERROR = "password.requirements";
    public static final String EMAIL_REGEX = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
            + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
    public static final String EMAIL_MESSAGE = "Email can contain numeric values, " +
            "both uppercase and lowercase latin letters, " +
            "dot “.”(not at the start and end of the local part, no consecutive dots), " +
            "underscore “_” and hyphen “-“, local part maximum 64 characters";
    public static final String EMAIL_ERROR = "email.requirements";
    public static final String NAME_REGEX = "^[^- '](?=(?!\\p{Lu}?\\p{Lu}))(?=(?!\\p{Ll}+\\p{Lu}))(?=(?!.*" +
            "\\p{Lu}\\p{Lu}))(?=(?!.*[- '][- '.]))(?=(?!.*[.][-'.]))(\\p{L}|[- '.]){2,24}$";
    public static final String NAME_MESSAGE = "up to 25 characters: letters, dot “.”, hyphen “-“, " +
            "whitespace “ ” and apostrophe “ ' “";
    public static final String NAME_ERROR = "name.requirements";
    public static final String LOGIN_REGEX = "^(?=.*[A-Za-z0-9]$)[A-Za-z][A-Za-z\\d_.-]{3,19}$";
    public static final String LOGIN_MESSAGE = "Login must start with a letter, end with a letter " +
            "or digit and can contain both uppercase and lowercase latin letters, dot “.”, " +
            "underscore “_” and hyphen “-“ (not at the start and end), and must consist of 4-20 symbols";
    public static final String LOGIN_ERROR = "login.requirements";

    private InputValidator() {
    }

    /**
     * Compares input value with regular expression.
     * @param input - The input value to be validated
     * @param regex – The valid regular expression
     * Return true if matches, otherwise false.
     */

    public static boolean isValid(String input, String regex) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);
        return matcher.matches();
    }
}
