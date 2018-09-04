package entities;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class User {

    private Integer id;
    private String name;
    private String mail;
    private List<String> phone;
    private String firstName = "";
    private String lastName = "";
    private String password;
    private Role role;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String eMail) {
        this.mail = eMail;
    }

    public List<String> getPhone() {
        if (phone == null) {
            return Collections.emptyList();
        }
        return phone;
    }

    public void setPhone(List<String> phone) {
        this.phone = phone;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public User emptyUser() {
        return new User.Builder()
                .setId(0)
                .setName("empty")
                .setMail("empty")
                .setFirstName("empty")
                .setLastName("empty")
                .setPassword("empty")
                .build();
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", mail='" + mail + '\'' +
//                ", phone='" + phone.toString() + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", role=" + role +
                '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, mail, phone, firstName, lastName, password, role);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) &&
                Objects.equals(name, user.name) &&
                Objects.equals(mail, user.mail) &&
                Objects.equals(phone, user.phone) &&
                Objects.equals(firstName, user.firstName) &&
                Objects.equals(lastName, user.lastName) &&
                Objects.equals(password, user.password) &&
                role == user.role;
    }

    public static class Builder {

        private User user;

        public Builder() {
            user = new User();
        }

        public Builder setId(Integer id) {
            user.setId(id);
            return this;
        }

        public Builder setName(String name) {
            user.setName(name);
            return this;
        }

        public Builder setMail(String eMail) {
            user.setMail(eMail);
            return this;
        }

        public Builder setPhone(List<String> phone) {
            user.setPhone(phone);
            return this;
        }

        public Builder setFirstName(String firstName) {
            user.setFirstName(firstName);
            return this;
        }

        public Builder setLastName(String lastName) {
            user.setLastName(lastName);
            return this;
        }

        public Builder setPassword(String password) {
            user.setPassword(password);
            return this;
        }

        public Builder setRole(Role role) {
            user.setRole(role);
            return this;
        }

        public User build() {
            return user;
        }
    }
}
