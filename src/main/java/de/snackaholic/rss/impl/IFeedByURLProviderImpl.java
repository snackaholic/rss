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
        LOG.info("Trying to provide Feed by URL: " + url.toString());
        try {
            InputStream inputStream = url.openStream();
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();
            saxParser.parse(inputStream, rssFeedParser);
            Feed theFeed = rssFeedParser.getFeed();
            theFeed.setUrl(url);
            return theFeed;
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
            LOG.log(Level.SEVERE, "ParserConfigurationException", e);
        } catch (SAXException e) {
            e.printStackTrace();
            LOG.log(Level.SEVERE, "SaxException", e);
        } catch (IOException e) {
            e.printStackTrace();
            LOG.log(Level.SEVERE, "IOException", e);
        } catch (Exception e) {
            e.printStackTrace();
            LOG.log(Level.SEVERE, "Unhandled Exception", e);
        }
        throw new RuntimeException("Could not provide valid Feed for that URL");
    }
}
