package de.snackaholic.rss.impl;

import de.snackaholic.rss.model.Feed;
import org.junit.Test;

import java.net.MalformedURLException;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;

public class RssFeedParserTest {

    private final Logger LOG = Logger.getLogger(RssFeedParserTest.class.getName());

    public RssFeedParserTest() {
        super();
    }

    @Test
    public void localParseTest() {
        try {
            LOG.log(Level.INFO, "STARTING TEST: RssFeedParserTest - localParseTest");
            RssFeedParser parser = new RssFeedParser();
            Feed localTestFeed = parser.provideFeedByURL(Paths.get("src/test/resources/testfeed.xml").toUri().toURL());
            LOG.log(Level.INFO, "CHECKING DESCRIPTION:");
            assertEquals("The Rough Cut features in-depth interviews with the top film and television post production professionals working in the industry today.  Hosted by @MattFeury of Avid Technology.", localTestFeed.getChannel().getDescription());
            LOG.log(Level.INFO, "CHECKING ITEMS:");
            assertEquals(118, localTestFeed.getChannel().getItems().size());
            LOG.log(Level.INFO, "TEST SUCCESSFUL: RssFeedParserTest - localParseTest");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }
}