package de.snackaholic.rss.impl;

import de.snackaholic.rss.model.Feed;
import org.junit.Test;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

public class RssFeedParserTest {

    public RssFeedParserTest() {
        super();
    }

    @Test
    public void localParseTest() {
        try {
            RssFeedParser parser = new RssFeedParser();
            URL fileRef = null;
            fileRef = Paths.get("../resources/testfeed.xml").toUri().toURL();
            // this should fail since it is not implemented yet...
            Feed localTestFeed = parser.provideFeedByURL(fileRef);
            assertEquals("testdescription", localTestFeed.getChannel().getDescription());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }
}