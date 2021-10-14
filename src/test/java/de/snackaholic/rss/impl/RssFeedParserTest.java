package de.snackaholic.rss.impl;

import de.snackaholic.rss.model.Feed;
import de.snackaholic.rss.model.Item;
import org.junit.Test;

import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;

// TODO check every possible model member of item, channel, feed
public class RssFeedParserTest {

    private final Logger LOG = Logger.getLogger(RssFeedParserTest.class.getName());

    public RssFeedParserTest() {
        super();
    }

    /**
     * The parsing test for the local file
     */
    @Test
    public void localFileParseTest() {
        try {
            LOG.log(Level.INFO, "STARTING TEST: RssFeedParserTest - localParseTest");
            RssFeedParser parser = new RssFeedParser();
            Feed localTestFeed = parser.provideFeedByURL(Paths.get("src/test/resources/testfeed.xml").toUri().toURL());
            LOG.log(Level.INFO, "CHECKING DESCRIPTION:");
            assertEquals("The Rough Cut features in-depth interviews with the top film and television post production professionals working in the industry today.  Hosted by @MattFeury of Avid Technology.", localTestFeed.getChannel().getDescription());
            LOG.log(Level.INFO, "CHECKING ITEMS:");
            assertEquals(118, localTestFeed.getChannel().getItems().size());
            LOG.log(Level.INFO, "CHECKING FIRST ITEM:");
            Item firstItem = localTestFeed.getChannel().getItems().get(0);
            assertEquals("© 2021 - The Rough Cut Studios", firstItem.getCopyright());
            assertEquals("en", firstItem.getLanguage());
            LOG.log(Level.INFO, "TEST SUCCESSFUL: RssFeedParserTest - localParseTest");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }


    /**
     * the same test for the same file but loaded via the http protocol from the github repo
     * this should behave exactly the same as loading a local file
     */
    @Test
    public void remoteFileParseTest() {
        try {
            LOG.log(Level.INFO, "STARTING TEST: RssFeedParserTest - remoteParseTest");
            RssFeedParser parser = new RssFeedParser();
            Feed localTestFeed = parser.provideFeedByURL(new URL("https://raw.githubusercontent.com/snackaholic/rss/main/src/test/resources/testfeed.xml"));
            LOG.log(Level.INFO, "CHECKING DESCRIPTION:");
            assertEquals("The Rough Cut features in-depth interviews with the top film and television post production professionals working in the industry today.  Hosted by @MattFeury of Avid Technology.", localTestFeed.getChannel().getDescription());
            LOG.log(Level.INFO, "CHECKING ITEMS:");
            assertEquals(118, localTestFeed.getChannel().getItems().size());
            LOG.log(Level.INFO, "CHECKING FIRST ITEM:");
            Item firstItem = localTestFeed.getChannel().getItems().get(0);
            assertEquals("© 2021 - The Rough Cut Studios", firstItem.getCopyright());
            assertEquals("en", firstItem.getLanguage());
            LOG.log(Level.INFO, "TEST SUCCESSFUL: RssFeedParserTest - remoteParseTest");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }
}