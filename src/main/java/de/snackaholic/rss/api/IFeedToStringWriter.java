package de.snackaholic.rss.api;

import de.snackaholic.rss.model.Feed;

public interface IFeedToStringWriter {

    /**
     * Writes the feed to a string
     * @param feed the feed that should be written as a string
     * @return the string representation of the feed
     */
    String writeFeedToString(Feed feed);
}
