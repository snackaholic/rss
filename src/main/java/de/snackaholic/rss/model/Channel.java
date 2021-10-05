package de.snackaholic.rss.model;

import java.util.List;

/**
 * Representation of an RSS 2.0 channel node
 * {@code}
 *  <channel>
 *    <link>mandatorylink<link>
 *    <title>mandatorytitle</title>
 *    <description>mandatorydescription</description>
 *    <item></item>
 *    <item></item>
 * </channel>
 * {@code}
 * TODO: pubdate, lastBuildDate are dates in format of RFC 822
 * TODO: category has attribute domain -> own model
 * TODO: cloud has attributes (domain, port, path, registerProcedure, protocol) -> own model
 * TODO: image has mandatory children(url, title, link ) and optional children (width, height, description) -> own model
 */
public class Channel {

    // mandatory members
    private String title;
    private String description;
    private String link;
    // optional members
    private List<Item> items;
    private String language;
    private String copyright;
    private String managingEditor;
    private String pubDate;
    private String webMaster;
    private String lastBuildDate;
    private List<String> categorys;
    private String generator;
    private String docs;
    private List<String> cloud;
    private String ttl;
    private String image;
    private String rating;
    private List<String> skipHours;
    private List<String> skipDays;

    public Channel() {
        super();
    }

    // accessors
    public String getManagingEditor() {
        return managingEditor;
    }

    public void setManagingEditor(String managingEditor) {
        this.managingEditor = managingEditor;
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

    public String getPubDate() {
        return pubDate;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    @Override
    public String toString() {
        return "Channel{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", language='" + language + '\'' +
                ", copyright='" + copyright + '\'' +
                ", pubDate='" + pubDate + '\'' +
                ", link='" + link + '\'' +
                ", items=" + items +
                '}';
    }
}
