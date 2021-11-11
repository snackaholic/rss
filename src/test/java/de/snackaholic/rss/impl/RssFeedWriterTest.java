package de.snackaholic.rss.impl;

import de.snackaholic.rss.model.*;
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
        List<Category> channelCategories = new ArrayList<>();
        List<Category> itemCategories = new ArrayList<>();
        // create test cat for item
        Category itemCategory = new Category();
        itemCategory.setValue("random value");
        itemCategory.setDomain("random domain");
        itemCategories.add(itemCategory);
        // create enclosure for item
        Enclosure randomEnclosure = new Enclosure();
        randomEnclosure.setLength("12216320");
        randomEnclosure.setType("audio/mpeg");
        randomEnclosure.setUrl("http://www.scripting.com/mp3s/weatherReportSuite.mp3");
        // create testitem
        Item testItem = new Item();
        testItem.setDescription("testitem");
        testItem.setEnclosure(randomEnclosure);
        testItem.setCategory(itemCategories);
        items.add(testItem);
        // create test category for channel
        Category cat = new Category();
        cat.setValue("random value");
        cat.setDomain("random domain");
        channelCategories.add(cat);
        // create testchannel
        Channel testChannel = new Channel();
        // mandatory members
        testChannel.setDescription("testdescription");
        testChannel.setLink("testlink");
        testChannel.setTitle("testtitle");
        // optional members
        testChannel.setManagingEditor("testManagingEditor");
        testChannel.setCopyright("testcopyright");
        testChannel.setLanguage("testlanguage");
        testChannel.setPubDate("testpubdate");
        testChannel.setWebMaster("testWebMaster");
        testChannel.setLastBuildDate("testLastBuildDate");
        testChannel.setGenerator("testGenerator");
        testChannel.setDocs("testDocs");
        testChannel.setTtl("testTtl");
        testChannel.setRating("testRating");
        Image testImage = new Image();
        testImage.setDescription("testDescription");
        testImage.setLink("testLink");
        testImage.setTitle("testTitle");
        testImage.setUrl("testUrl");
        testImage.setHeight(200);
        testImage.setWidth(200);
        testChannel.setImage(testImage);
        testChannel.setItems(items);
        testChannel.setCategory(channelCategories);
        // TODO set all channel members
        // create the feed
        Feed testFeed = new Feed();
        testFeed.setChannel(testChannel);
        String output = writer.writeFeedToString(testFeed);
        assertNotEquals(output, null);
        assertNotEquals(output.length(), 0);
        assertEquals("<?xml version=\"1.0\"?><rss version=\"2.0\"><channel><description><![CDATA[testdescription]]></description><link><![CDATA[testlink]]></link><title><![CDATA[testtitle]]></title><language><![CDATA[testlanguage]]></language><copyright><![CDATA[testcopyright]]></copyright><managingEditor><![CDATA[testManagingEditor]]></managingEditor><pubDate><![CDATA[testpubdate]]></pubDate><webMaster><![CDATA[testWebMaster]]></webMaster><lastBuildDate><![CDATA[testLastBuildDate]]></lastBuildDate><generator><![CDATA[testGenerator]]></generator><docs><![CDATA[testDocs]]></docs><ttl><![CDATA[testTtl]]></ttl><rating><![CDATA[testRating]]></rating><image><url><![CDATA[testUrl]]></url><title><![CDATA[testTitle]]></title><link><![CDATA[testLink]]></link><description><![CDATA[testDescription]]></description><width><![CDATA[200]]></width><height><![CDATA[200]]></height></image><category domain=\"random domain\">random value</category><item><description><![CDATA[testitem]]></description><enclosure url=\"http://www.scripting.com/mp3s/weatherReportSuite.mp3\" length=\"12216320\" type=\"audio/mpeg\"/><category domain=\"random domain\">random value</category></item></channel></rss>", output);
        LOG.info(output);
        LOG.log(Level.INFO, "TEST SUCCESSFUL: RssFeedWriterTest - localWriteTest");
    }
}