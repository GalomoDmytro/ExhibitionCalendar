package utility;

public class Patterns {
    /* PASSWORD
   * start-of-string
   * a digit must occur at least once
   * a lower case letter must occur at least once
   * an upper case letter must occur at least once
   * no whitespace allowed in the entire string
   * anything, at least 6 places though
   * end-of-string
   */
    public static final String PASSWORD = "^(?=.*[0-9])(?=.*[a-zа-я])(?=.*[A-ZА-Я])(?=\\S+$).{6,}$";

    public static final String EMAIL = "^[^@]*@[^@]+.[^@]+$"; // contain one '@' and after, one '.'

    public static final String EMAIL_LENGTH = ".{4,254}";

    public static final String ADDRESS_LENGTH = ".{1,254}";

    public static final String PHONE_LENGTH = ".{,44}";

    public static final String TITLE_EXPO_CENTER = ".{1,254}";

    public static final String TITLE_EXPO = ".{1,254}";

    public static final String NAME = "^[a-zA-Z][a-zA-Z0-9-_\\.]{1,20}$"; // first letter, with 2-20 chars

    public static final String DATE_PATTERN = "^[0-9]{4}-[0-9]{2}-[0-9]{2}$";

    public static final String PRICE_PATTERN = "^[0-9]+[.,]{0,1}[0-9]*$";

    public static final String DATE_SQL_PATTERN = "yyyy-MM-dd";

}
