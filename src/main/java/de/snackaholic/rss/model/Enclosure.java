package de.snackaholic.rss.model;

/**
 * Documentation source from https://validator.w3.org/feed/docs/rss2.html
 * <enclosure> sub-element of <item>
 * <enclosure> is an optional sub-element of <item>.
 *
 * It has three required attributes. url says where the enclosure is located, length says how big it is in bytes, and type says what its type is, a standard MIME type.
 *
 * The url must be an http url.
 *
 * <enclosure url="http://www.scripting.com/mp3s/weatherReportSuite.mp3" length="12216320" type="audio/mpeg" />
 */
public class Enclosure {

    private String url;
    private String type;
    private double length;

    public Enclosure() {
        super();
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getLength() {
        return length;
    }

    public void setLength(double length) {
        this.length = length;
    }
}
