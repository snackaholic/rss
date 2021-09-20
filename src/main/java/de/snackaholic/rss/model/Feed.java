package de.snackaholic.rss.model;

import java.net.URL;
import java.util.List;

/**
 * Representation of a Rss Feed
 */
public class Feed {
    URL url;
    Channel channel;

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
