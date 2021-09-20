package de.snackaholic.rss.impl;

import de.snackaholic.rss.api.IFeedByURLProvider;
import de.snackaholic.rss.model.Feed;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Implementation of the IFeedByURLProvider interface
 */
public class RssFeedParser implements IFeedByURLProvider {

    private final Logger LOG = Logger.getLogger(RssFeedParser.class.getName());

    @Override
    public Feed provideFeedByURL(URL url) {
        // try to get the file by the url
        try{
            InputStream fileInputStream = getInputStreamOfUrl(url);
        } catch(IOException e) {
            LOG.log(Level.SEVERE, "COULD NOT OPEN INPUTSTREAM BY URL", e);
        }
        // open input stream of it
       throw new RuntimeException("not implemented yet...");
    }

    private InputStream getInputStreamOfUrl(URL url) throws IOException {
        LOG.log(Level.INFO, "TRYING TO READ URL: " + url.toString());
        return url.openStream();
    }
}
