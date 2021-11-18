package de.snackaholic.rss.api;

import de.snackaholic.rss.model.Feed;

import java.net.URL;

/**
 * The desired functionality of a url to feed transformer
 */
public interface IFeedByURLProvider {

    /**
     * Implementation shall provide an instance of Feed based on the provided url
     * @param url the url that the feed should get extracted from
     * @return an instance of feed, representing the parsed data
     */
    Feed provideFeedByURL(URL url);
}
