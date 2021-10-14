package de.snackaholic.rss.model;

import java.net.URL;

/**
 * Representation of a Rss 2.0 Feed
 * {@code}
 * <rss version="2.0">
 *    <channel> ... </channel>
 * </rss>
 * {@code}
 */
public class Feed {

    private URL url;
    private Channel channel;

    public Feed() {
        super();
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public URL getUrl() {
        return url;
    }

    public void setUrl(URL url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "Feed{" +
                "url=" + url +
                ", channel=" + channel +
                '}';
    }


}
