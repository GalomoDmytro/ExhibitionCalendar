package utility;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.log4j.Logger;

public class PasswordHandler {

    private static final Logger LOGGER = Logger.getLogger(PasswordHandler.class);

    public PasswordHandler(){}

    public String encrypt(String originalString) {
        return DigestUtils.sha256Hex(originalString + "salt java");
    }

    public boolean compare(String pas1, String pas2) {
        if(pas1.equals(pas2)) return true;
        return false;
    }

    public boolean encryptAndCompare(String rawPas, String pas2) {
        String pas1 = encrypt(rawPas);
        return compare(pas1, pas2);
    }
}
