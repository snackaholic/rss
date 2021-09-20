package de.snackaholic.rss.model;

/**
 * Representation of an RSS item node <item></item>
 *
 * TODO enclose is own type with multiple children and so on
 * TODO add itunes http://www.itunes.com/dtds/podcast-1.0.dtd
 * TODO add google http://www.google.com/schemas/play-podcasts/1.0
 */
public class Item {

    private String author;
    private String title;
    private String pubDate;
    private String guid;
    private String description;
    private String enclosure;


    public Item() {
        super();
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPubDate() {
        return pubDate;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEnclosure() {
        return enclosure;
    }

    public void setEnclosure(String enclosure) {
        this.enclosure = enclosure;
    }

    @Override
    public String toString() {
        return "Item{" +
                "author='" + author + '\'' +
                ", title='" + title + '\'' +
                ", pubDate='" + pubDate + '\'' +
                ", guid='" + guid + '\'' +
                ", description='" + description + '\'' +
                ", enclosure='" + enclosure + '\'' +
                '}';
    }
}
