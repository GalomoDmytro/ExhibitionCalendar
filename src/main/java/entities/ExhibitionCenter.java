package entities;

import dao.mysql.ExhibitionCenterMySql;
import org.apache.log4j.Logger;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class ExhibitionCenter {

    private Integer id;
    private String title;
    private String address;
    private String eMail;
    private String webPage;
    private List<String> phone;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String geteMail() {
        return eMail;
    }

    public void seteMail(String eMail) {
        this.eMail = eMail;
    }

    public String getWebPage() {
        return webPage;
    }

    public void setWebPage(String webPage) {
        this.webPage = webPage;
    }

    public List<String> getPhone() {
        if(phone == null) {
            return Collections.emptyList();
        }
        return phone;
    }

    public void setPhone(List<String> phone) {
        this.phone = phone;
    }

    @Override
    public String toString() {
        return "ExhibitionCenter{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", address='" + address + '\'' +
                ", eMail='" + eMail + '\'' +
                ", webPage='" + webPage + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ExhibitionCenter that = (ExhibitionCenter) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(title, that.title) &&
                Objects.equals(address, that.address) &&
                Objects.equals(eMail, that.eMail) &&
                Objects.equals(webPage, that.webPage) &&
                Objects.equals(phone, that.phone);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, address, eMail, webPage, phone);
    }

    public static class Builder {

        private ExhibitionCenter exhibitionCenter;

        public Builder() {
            exhibitionCenter = new ExhibitionCenter();
        }

        public Builder setId(Integer id) {
            exhibitionCenter.setId(id);
            return this;
        }

        public Builder setTitle(String title) {
            exhibitionCenter.setTitle(title);
            return this;
        }

        public Builder setAddress(String address) {
            exhibitionCenter.setAddress(address);
            return this;
        }

        public Builder seteMail(String eMail) {
            exhibitionCenter.seteMail(eMail);
            return this;
        }

        public Builder setWebPage(String webPage) {
            exhibitionCenter.setWebPage(webPage);
            return this;
        }

        public Builder setPhone(List<String> phone) {
            exhibitionCenter.setPhone(phone);
            return this;
        }

        public ExhibitionCenter build() {
            return exhibitionCenter;
        }

    }
}
