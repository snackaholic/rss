package de.snackaholic.rss.model;

import java.util.List;

/**
 * Representation of an RSS item node <item></item>
 *
 *
 * TODO: respect itunes doc http://www.itunes.com/dtds/podcast-1.0.dtd
 * TODO: respect google doc http://www.google.com/schemas/play-podcasts/1.0
 */
public class Item {

    // mandatory members according to rss specification
    private String description;
    private String link;
    private String title;
    // optional members according to rss specification
    private String author;
    private String pubDate;
    private String guid;
    private Enclosure enclosure;
    private String language;
    private String copyright;
    private List<Category> category;

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

    public Enclosure getEnclosure() {
        return enclosure;
    }

    public void setEnclosure(Enclosure enclosure) {
        this.enclosure = enclosure;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getCopyright() {
        return copyright;
    }

    public void setCopyright(String copyright) {
        this.copyright = copyright;
    }

    public List<Category> getCategory() {
        return category;
    }

    public void setCategory(List<Category> category) {
        this.category = category;
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
                ", link='" + link + '\'' +
                ", language='" + language + '\'' +
                ", copyright='" + copyright + '\'' +
                '}';
    }
}
