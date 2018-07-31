package password;

public class PasswordHelper {

    public Integer encrypt(String pas) {
        return (pas + "salt").hashCode();
    }

    public boolean isEquals(Integer pas1, Integer pas2) {
        return pas1 == pas2;
    }
}
