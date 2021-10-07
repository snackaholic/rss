package de.snackaholic.rss.model;

import java.util.List;

/**
 * Representation of an RSS 2.0 channel node
 * {@code}
 * <channel>
 * <link>mandatorylink<link>
 * <title>mandatorytitle</title>
 * <description>mandatorydescription</description>
 * <item></item>
 * <item></item>
 * </channel>
 * {@code}
 * TODO: pubdate, lastBuildDate are dates in format of RFC 822
 * TODO: category has attribute domain -> own model
 * TODO: cloud has attributes (domain, port, path, registerProcedure, protocol) -> own model
 * TODO: image has mandatory children(url, title, link ) and optional children (width, height, description) -> own model
 */
public class Channel {

    // mandatory members
    private String description;
    private String link;
    private String title;
    // optional members
    private String language;
    private String copyright;
    private String managingEditor;
    private String pubDate;
    private String webMaster;
    private String lastBuildDate;
    private String generator;
    private String docs;
    private String ttl;
    private String image;
    private String rating;
    private List<Item> items;
    private List<String> cloud;
    private List<String> categorys;
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

    public String getWebMaster() {
        return webMaster;
    }

    public void setWebMaster(String webMaster) {
        this.webMaster = webMaster;
    }

    public String getLastBuildDate() {
        return lastBuildDate;
    }

    public void setLastBuildDate(String lastBuildDate) {
        this.lastBuildDate = lastBuildDate;
    }

    public List<String> getCategorys() {
        return categorys;
    }

    public void setCategorys(List<String> categorys) {
        this.categorys = categorys;
    }

    public String getGenerator() {
        return generator;
    }

    public void setGenerator(String generator) {
        this.generator = generator;
    }

    public String getDocs() {
        return docs;
    }

    public void setDocs(String docs) {
        this.docs = docs;
    }

    public List<String> getCloud() {
        return cloud;
    }

    public void setCloud(List<String> cloud) {
        this.cloud = cloud;
    }

    public String getTtl() {
        return ttl;
    }

    public void setTtl(String ttl) {
        this.ttl = ttl;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public List<String> getSkipHours() {
        return skipHours;
    }

    public void setSkipHours(List<String> skipHours) {
        this.skipHours = skipHours;
    }

    public List<String> getSkipDays() {
        return skipDays;
    }

    public void setSkipDays(List<String> skipDays) {
        this.skipDays = skipDays;
    }

    @Override
    public String toString() {
        return "Channel{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", link='" + link + '\'' +
                ", language='" + language + '\'' +
                ", copyright='" + copyright + '\'' +
                ", managingEditor='" + managingEditor + '\'' +
                ", pubDate='" + pubDate + '\'' +
                ", webMaster='" + webMaster + '\'' +
                ", lastBuildDate='" + lastBuildDate + '\'' +
                ", generator='" + generator + '\'' +
                ", docs='" + docs + '\'' +
                ", ttl='" + ttl + '\'' +
                ", image='" + image + '\'' +
                ", rating='" + rating + '\'' +
                '}';
    }
}
