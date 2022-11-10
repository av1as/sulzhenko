package com.sulzhenko.DAO;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class validates input values.
 */
public class InputValidator {
    /**
     * Constants describing requirements for input fields and containing regular expressions.
     */
    public static final String PASSWORD_REGEX = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%]).{8,20}$";
    public static final String PASSWORD_MESSAGE = "Password must contain numeric, lowercase and "
            + "uppercase character, special symbol among @#$% and should consist of 8-20 symbols";
    public static final String EMAIL_REGEX = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
            + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
    public static final String EMAIL_MESSAGE = "Email can contain numeric values, " +
            "both uppercase and lowercase latin letters, " +
            "dot “.”(not at the start and end of the local part, no consecutive dots), " +
            "underscore “_” and hyphen “-“, local part maximum 64 characters";
    public static final String NAME_REGEX = "^[^- '](?=(?!\\p{Lu}?\\p{Lu}))(?=(?!\\p{Ll}+\\p{Lu}))(?=(?!.*" +
            "\\p{Lu}\\p{Lu}))(?=(?!.*[- '][- '.]))(?=(?!.*[.][-'.]))(\\p{L}|[- '.]){2,24}$";
    public static final String NAME_MESSAGE = "up to 25 characters: letters, dot “.”, hyphen “-“, " +
            "whitespace “ ” and apostrophe “ ' “";

    /**
     * Compares input value with regular expression.
     * Params: input - The input value to be validated
     *         regex – The valid regular expression
     * Return true if matches otherwise false.
     */
    public static boolean isValid(String input, String regex) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);
        return matcher.matches();
    }
}
