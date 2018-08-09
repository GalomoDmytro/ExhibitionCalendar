package utility;

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
}
