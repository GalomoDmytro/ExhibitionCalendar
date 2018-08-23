package utility;

import org.apache.commons.codec.digest.DigestUtils;

public class PasswordHandler {

    public PasswordHandler(){}

    public Integer encryptPassword(String passwrod) {

        return passwrod.hashCode();
    }

    public boolean comparePassword(Integer pas1, Integer pas2) {
        if(pas1.intValue() == pas2.intValue()) return true;
        return false;
    }

    public boolean comparePassword(String pas1, Integer pas2) {
        if(encryptPassword(pas1).intValue() == pas2.intValue()) return true;
        return false;
    }

    // TODO: refactor to use this methods
    public String encrypt(String originalString) {
        return DigestUtils.sha256Hex(originalString);
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
