package de.snackaholic.rss.model;

/**
 * <image> sub-element of <channel>
 * <image> is an optional sub-element of <channel>, which contains three required and three optional sub-elements.
 *
 * <url> is the URL of a GIF, JPEG or PNG image that represents the channel.
 *
 * <title> describes the image, it's used in the ALT attribute of the HTML <img> tag when the channel is rendered in HTML.
 *
 * <link> is the URL of the site, when the channel is rendered, the image is a link to the site. (Note, in practice the image <title> and <link> should have the same value as the channel's <title> and <link>.
 *
 * Optional elements include <width> and <height>, numbers, indicating the width and height of the image in pixels. <description> contains text that is included in the TITLE attribute of the link formed around the image in the HTML rendering.
 *
 * Maximum value for width is 144, default value is 88.
 *
 * Maximum value for height is 400, default value is 31.
 */
public class Image implements Cloneable {
    // mandatory members
    private String url;
    private String title;
    private String link;
    // optional members
    private String description;
    private int width;
    private int height;

    public Image() {
        super();
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    @Override
    public String toString() {
        return "Image{" +
                "url='" + url + '\'' +
                ", title='" + title + '\'' +
                ", link='" + link + '\'' +
                ", description='" + description + '\'' +
                ", width=" + width +
                ", height=" + height +
                '}';
    }


    @Override
    public Image clone() {
        try {
            Image clone = (Image) super.clone();
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
