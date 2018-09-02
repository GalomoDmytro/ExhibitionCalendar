package utility;

import org.junit.Test;

import static org.junit.Assert.*;

public class PasswordHandlerTest {

    private final PasswordHandler passwordHandler = new PasswordHandler();

    @Test
    public void encrypt() {
        String lineToEncrypt1 = "";
        String lineToEncrypt2 = "123";
        String lineToEncrypt3 = "привет мир";

        String cipher1 = passwordHandler.encrypt(lineToEncrypt1);
        String cipher2 = passwordHandler.encrypt(lineToEncrypt2);
        String cipher3 = passwordHandler.encrypt(lineToEncrypt3);

        assertEquals(64, cipher1.length());
        assertEquals(64, cipher2.length());
        assertEquals(64, cipher3.length());
    }

    @Test
    public void compare() {
        String lineToEncrypt1 = "password";
        String lineToEncrypt2 = "password";
        String lineToEncrypt3 = "password wrong";

        String cipher1 = passwordHandler.encrypt(lineToEncrypt1);
        String cipher2 = passwordHandler.encrypt(lineToEncrypt2);
        String cipher3 = passwordHandler.encrypt(lineToEncrypt3);

        assertTrue(passwordHandler.compare(cipher1, cipher2));
        assertFalse(passwordHandler.compare(cipher1, cipher3));
    }

    @Test
    public void encryptAndCompare() {
        String lineToEncrypt1 = "password";
        String lineToEncrypt2 = "password";
        String lineToEncrypt3 = "password wrong";

        String cipher2 = passwordHandler.encrypt(lineToEncrypt2);
        String cipher3 = passwordHandler.encrypt(lineToEncrypt3);

        assertTrue(passwordHandler.encryptAndCompare(lineToEncrypt1, cipher2));
        assertFalse(passwordHandler.encryptAndCompare(lineToEncrypt1, cipher3));
    }
}