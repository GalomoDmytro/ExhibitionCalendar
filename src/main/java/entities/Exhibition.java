package entities;

import java.util.Objects;

public class Exhibition {

    private Integer id;
    private String title;
    private String description;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
                ", description='" + description + '\'' +
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
                Objects.equals(description, that.description) &&
                Objects.equals(imgSrc, that.imgSrc);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, description, imgSrc);
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

        public Builder setDescription(String description) {
            exhibition.setDescription(description);
            return this;
        }

        public Builder setImgSrc(String imgSrc) {
            exhibition.setDescription(imgSrc);
            return this;
        }

        public Exhibition build() {
            return exhibition;
        }
    }
}
