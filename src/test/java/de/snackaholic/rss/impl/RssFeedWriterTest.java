package de.snackaholic.rss.impl;

import de.snackaholic.rss.model.Category;
import de.snackaholic.rss.model.Channel;
import de.snackaholic.rss.model.Feed;
import de.snackaholic.rss.model.Item;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;

public class RssFeedWriterTest {

    private final Logger LOG = Logger.getLogger(RssFeedWriterTest.class.getName());
    private final RssFeedWriter writer = new RssFeedWriter();

    public RssFeedWriterTest() {
        super();
    }

    @Test
    public void localWriteTest() {
        LOG.log(Level.INFO, "STARTING TEST: RssFeedWriterTest - localWriteTest");
        List<Item> items = new ArrayList<>();
        List<Category> categories = new ArrayList<>();

        // create testitem
        Item testItem = new Item();
        testItem.setDescription("testitem");
        items.add(testItem);
        // create test category
        Category cat = new Category();
        cat.setValue("random value");
        cat.setDomain("random domain");
        categories.add(cat);
        // create testchannel
        Channel testChannel = new Channel();
        testChannel.setLink("testlink");
        testChannel.setCopyright("testcopyright");
        testChannel.setLanguage("testlanguage");
        testChannel.setTitle("testtitle");
        testChannel.setDescription("testdescription");
        testChannel.setPubDate("testpubdate");
        testChannel.setItems(items);
        testChannel.setCategory(categories);
        // create the feed
        Feed testFeed = new Feed();
        testFeed.setChannel(testChannel);
        String output = writer.writeFeedToString(testFeed);
        assertNotEquals(output, null);
        assertNotEquals(output.length(), 0);
        assertEquals("<?xml version=\"1.0\" ?><rss version=\"2.0\"><channel><description><![CDATA[testdescription]]></description><link><![CDATA[testlink]]></link><title><![CDATA[testtitle]]></title><language><![CDATA[testlanguage]]></language><copyright><![CDATA[testcopyright]]></copyright><pubDate><![CDATA[testpubdate]]></pubDate><item><description><![CDATA[testitem]]></description></item><category domain=\"random domain\">random value</category></channel></rss>", output);
        LOG.info(output);
        LOG.log(Level.INFO, "TEST SUCCESSFUL: RssFeedWriterTest - localWriteTest");
    }
}