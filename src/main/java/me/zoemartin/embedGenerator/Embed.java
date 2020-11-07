package me.zoemartin.embedGenerator;

import java.io.Serializable;
import java.util.List;

public class Embed implements Serializable {
    private final String title;
    private final String type;
    private final String description;
    private final String url;
    private final String timestamp;
    private final Integer color;
    private final Footer footer;
    private final Image image;
    private final Thumbnail thumbnail;
    private final Video video;
    private final Provider provider;
    private final Author author;
    private final List<Field> fields;

    public Embed(String title, String type, String description, String url, String timestamp, Integer color,
                 Footer footer, Image image, Thumbnail thumbnail, Video video, Provider provider,
                 Author author, List<Field> fields) {
        this.title = title;
        this.type = type;
        this.description = description;
        this.url = url;
        this.timestamp = timestamp;
        this.color = color;
        this.footer = footer;
        this.image = image;
        this.thumbnail = thumbnail;
        this.video = video;
        this.provider = provider;
        this.author = author;
        this.fields = fields;
    }

    public String getTitle() {
        return title;
    }

    public String getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }

    public String getUrl() {
        return url;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public Integer getColor() {
        return color;
    }

    public Footer getFooter() {
        return footer;
    }

    public Image getImage() {
        return image;
    }

    public Thumbnail getThumbnail() {
        return thumbnail;
    }

    public Video getVideo() {
        return video;
    }

    public Provider getProvider() {
        return provider;
    }

    public Author getAuthor() {
        return author;
    }

    public List<Field> getFields() {
        return fields;
    }

    public static class Thumbnail implements Serializable {
        private final String url;
        private final String proxy_url;
        private final Integer height;
        private final Integer width;

        public Thumbnail(String url, String proxy_url, Integer height, Integer width) {
            this.url = url;
            this.proxy_url = proxy_url;
            this.height = height;
            this.width = width;
        }

        public String getUrl() {
            return url;
        }

        public String getProxy_url() {
            return proxy_url;
        }

        public Integer getHeight() {
            return height;
        }

        public Integer getWidth() {
            return width;
        }
    }

    public static class Video implements Serializable {
        private final String url;
        private final Integer height;
        private final Integer width;

        public Video(String url, Integer height, Integer width) {
            this.url = url;
            this.height = height;
            this.width = width;
        }

        public String getUrl() {
            return url;
        }

        public Integer getHeight() {
            return height;
        }

        public Integer getWidth() {
            return width;
        }
    }

    public static class Image implements Serializable {
        private final String url;
        private final String proxy_url;
        private final Integer height;
        private final Integer width;

        public Image(String url, String proxy_url, Integer height, Integer width) {
            this.url = url;
            this.proxy_url = proxy_url;
            this.height = height;
            this.width = width;
        }

        public String getUrl() {
            return url;
        }

        public String getProxy_url() {
            return proxy_url;
        }

        public Integer getHeight() {
            return height;
        }

        public Integer getWidth() {
            return width;
        }
    }

    public static class Provider implements Serializable {
        private final String name;
        private final String url;

        public Provider(String name, String url) {
            this.name = name;
            this.url = url;
        }

        public String getName() {
            return name;
        }

        public String getUrl() {
            return url;
        }
    }

    public static class Author implements Serializable {
        private final String name;
        private final String url;
        private final String icon_url;
        private final String proxy_icon_url;

        public Author(String name, String url, String icon_url, String proxy_icon_url) {
            this.name = name;
            this.url = url;
            this.icon_url = icon_url;
            this.proxy_icon_url = proxy_icon_url;
        }

        public String getName() {
            return name;
        }

        public String getUrl() {
            return url;
        }

        public String getIcon_url() {
            return icon_url;
        }

        public String getProxy_icon_url() {
            return proxy_icon_url;
        }
    }

    public static class Footer implements Serializable {
        private final String text;
        private final String icon_url;
        private final String proxy_icon_url;

        public Footer(String text, String icon_url, String proxy_icon_url) {
            this.text = text;
            this.icon_url = icon_url;
            this.proxy_icon_url = proxy_icon_url;
        }

        public String getText() {
            return text;
        }

        public String getIcon_url() {
            return icon_url;
        }

        public String getProxy_icon_url() {
            return proxy_icon_url;
        }
    }

    public static class Field implements Serializable {
        private final String name;
        private final String value;
        private final boolean inline;

        public Field(String name, String value, boolean inline) {
            this.name = name;
            this.value = value;
            this.inline = inline;
        }

        public String getName() {
            return name;
        }

        public String getValue() {
            return value;
        }

        public Boolean getInline() {
            return inline;
        }
    }
}
