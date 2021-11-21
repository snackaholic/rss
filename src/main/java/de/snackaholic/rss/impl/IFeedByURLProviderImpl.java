package de.snackaholic.rss.impl;

import de.snackaholic.rss.api.IFeedByURLProvider;
import de.snackaholic.rss.model.Feed;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

public class IFeedByURLProviderImpl implements IFeedByURLProvider {

    private static final Logger LOG = Logger.getLogger(IFeedByURLProviderImpl.class.getName());
    private RssFeedParser rssFeedParser;

    public IFeedByURLProviderImpl() {
        super();
        this.rssFeedParser = new RssFeedParser();
    }

    @Override
    public Feed provideFeedByURL(URL url) {
        LOG.info("TRYING TO PARSE FEED BY URL: " + url.toString());
        // try to get the file by the url
        try {
            InputStream inputStream = getInputStreamOfUrl(url);
            SAXParserFactory factory = SAXParserFactory.newInstance();
            try {
                SAXParser saxParser = factory.newSAXParser();
                saxParser.parse(inputStream, rssFeedParser);
                Feed theFeed = rssFeedParser.getFeed();
                theFeed.setUrl(url);
                return theFeed;
            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            } catch (SAXException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "COULD NOT OPEN INPUTSTREAM BY URL", e);
        }
        throw new RuntimeException("could not provide feed for that url");
    }

    /**
     * Provides inputstream by url
     *
     * @param url the url
     * @return the stream
     * @throws IOException when something goes wrong opening the stream
     */
    private InputStream getInputStreamOfUrl(URL url) throws IOException {
        LOG.info("TRYING TO READ URL: " + url.toString());
        return url.openStream();
    }
}
