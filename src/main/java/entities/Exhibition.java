package entities;

import java.util.Objects;

public class Exhibition {

    private Integer id;
    private String title;
    private String imgSrc;

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


    public String getImgSrc() {
        return imgSrc;
    }

    public void setImgSrc(String imgSrc) {
        this.imgSrc = imgSrc;
    }

    @Override
    public String toString() {
        return "Exhibition{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", imgSrc='" + imgSrc + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Exhibition that = (Exhibition) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(title, that.title) &&
                Objects.equals(imgSrc, that.imgSrc);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, imgSrc);
    }

    public static class Builder {
        private Exhibition exhibition;

        Builder() {
            exhibition = new Exhibition();
        }

        public Builder setId(Integer id) {
            exhibition.setId(id);
            return this;
        }

        public Builder setTitle(String title) {
            exhibition.setTitle(title);
            return this;
        }

        public Exhibition build() {
            return exhibition;
        }
    }
}
