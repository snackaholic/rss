package de.snackaholic.rss.impl;

import de.snackaholic.rss.api.IFeedByURLProvider;
import de.snackaholic.rss.model.Feed;

import java.net.URL;

/**
 * Implementation of the IFeedByURLProvider interface
 */
public class RssFeedParser implements IFeedByURLProvider {

    @Override
    public Feed provideFeedByURL(URL url) {
       throw new RuntimeException("not implemented yet...");
    }
}
