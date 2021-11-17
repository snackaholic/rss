package de.snackaholic.rss.impl;

import de.snackaholic.rss.model.*;
import org.junit.Test;

import java.sql.Array;
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

    private List<Item> provideTestItemList() {
        List<Item> itemLists = new ArrayList<>();
        itemLists.add(provideTestItem());
        itemLists.add(provideTestItem());
        return itemLists;
    }

    private List<Category> provideTestCategoryList() {
        List<Category> categoryList = new ArrayList<>();
        categoryList.add(provideTestCategory());
        categoryList.add(provideTestCategory());
        return categoryList;
    }

    private Category provideTestCategory() {
        Category category = new Category();
        category.setDomain("category domain");
        category.setValue("category value");
        return category;
    }

    private Enclosure provideTestEnclosure() {
        Enclosure enclosure = new Enclosure();
        enclosure.setUrl("enclosure url");
        enclosure.setType("enclosure type");
        enclosure.setLength("enclosure length");
        return enclosure;
    }

    private Item provideTestItem() {
        Item item = new Item();
        // set mandatory
        item.setDescription("item description");
        item.setLink("item link");
        item.setTitle("item title");
        // set optionals
        item.setAuthor("item author");
        item.setPubDate("item pubDate");
        item.setGuid("item guid");
        item.setLanguage("item language");
        item.setCopyright("item copyright");
        // more complex members
        item.setEnclosure(provideTestEnclosure());
        item.setCategory(provideTestCategoryList());
        return item;
    }

    private Channel provideTestChannel() {
        // create testchannel
        Channel testChannel = new Channel();
        // mandatory members
        testChannel.setDescription("channel description");
        testChannel.setLink("channel link");
        testChannel.setTitle("channel title");
        // optional members
        testChannel.setLanguage("channel language");
        testChannel.setCopyright("channel copyright");
        testChannel.setManagingEditor("channel managingEditor");
        testChannel.setPubDate("channel pubdate");
        testChannel.setWebMaster("channel webMaster");
        testChannel.setLastBuildDate("channel lastBuildDate");
        testChannel.setGenerator("channel generator");
        testChannel.setDocs("channel docs");
        testChannel.setTtl("channel ttl");
        testChannel.setRating("channel rating");
        // more complex children
        testChannel.setImage(provideTestImage());
        testChannel.setItems(provideTestItemList());
        testChannel.setCategory(provideTestCategoryList());
        testChannel.setCloud(provideTestCloudList());
        testChannel.setSkipHours(provideTestSkipHours());
        testChannel.setSkipDays(provideTestSkipDays());
        return testChannel;
    }

    private List<String> provideTestSkipDays() {
        List<String> skipDaysList = new ArrayList<>();
        skipDaysList.add(Day.MONDAY.getValue());
        skipDaysList.add(Day.FRIDAY.getValue());
        return skipDaysList;
    }

    private List<String> provideTestSkipHours() {
        List<String> skipHoursList = new ArrayList<>();
        skipHoursList.add("1");
        skipHoursList.add("23");
        return skipHoursList;
    }

    private List<Cloud> provideTestCloudList() {
        List<Cloud> cloudList = new ArrayList<>();
        cloudList.add(provideTestCloud());
        cloudList.add(provideTestCloud());
        return cloudList;
    }

    private Cloud provideTestCloud() {
        Cloud cloud = new Cloud();
        cloud.setDomain("cloud domain");
        cloud.setPath("cloud path");
        cloud.setPort(123);
        cloud.setProtocol("cloud protocol");
        cloud.setRegisterProcedure("cloud register procedure");
        return cloud;
    }

    private Image provideTestImage() {
        Image testImage = new Image();
        testImage.setDescription("image description");
        testImage.setLink("image link");
        testImage.setTitle("image title");
        testImage.setUrl("image url");
        testImage.setHeight(200);
        testImage.setWidth(200);
        return testImage;
    }

    @Test
    public void writeFeedToString() {
        LOG.log(Level.INFO, "STARTING TEST: RssFeedWriterTest - localWriteTest");
        Feed testFeed = new Feed();
        testFeed.setChannel(provideTestChannel());
        String output = writer.writeFeedToString(testFeed);
        assertNotEquals(output, null);
        assertNotEquals(output.length(), 0);
        // check if the set contents are within the xml
        assertTrue(output.contains(""));
        assertEquals("<?xml version=\"1.0\" ?><rss version=\"2.0\"><channel><description><![CDATA[channel description]]></description><link><![CDATA[channel link]]></link><title><![CDATA[channel title]]></title><language><![CDATA[channel language]]></language><copyright><![CDATA[channel copyright]]></copyright><managingEditor><![CDATA[channel managingEditor]]></managingEditor><pubDate><![CDATA[channel pubdate]]></pubDate><webMaster><![CDATA[channel webMaster]]></webMaster><lastBuildDate><![CDATA[channel lastBuildDate]]></lastBuildDate><generator><![CDATA[channel generator]]></generator><docs><![CDATA[channel docs]]></docs><ttl><![CDATA[channel ttl]]></ttl><rating><![CDATA[channel rating]]></rating><image><url><![CDATA[image url]]></url><title><![CDATA[image title]]></title><link><![CDATA[image link]]></link><description><![CDATA[image description]]></description><width><![CDATA[200]]></width><height><![CDATA[200]]></height></image><cloud domain=\"cloud domain\" path=\"cloud path\" protocol=\"cloud protocol\" registerProcedure=\"cloud register procedure\" port=\"123\"/><cloud domain=\"cloud domain\" path=\"cloud path\" protocol=\"cloud protocol\" registerProcedure=\"cloud register procedure\" port=\"123\"/><category domain=\"category domain\">category value</category><category domain=\"category domain\">category value</category><skipHours><hour><![CDATA[1]]></hour><hour><![CDATA[23]]></hour></skipHours><skipDays><day><![CDATA[Monday]]></day><day><![CDATA[Friday]]></day></skipDays><item><description><![CDATA[item description]]></description><title><![CDATA[item title]]></title><link><![CDATA[item link]]></link><enclosure url=\"enclosure url\" length=\"enclosure length\" type=\"enclosure type\"/><category domain=\"category domain\">category value</category><category domain=\"category domain\">category value</category><author><![CDATA[item author]]></author><copyright><![CDATA[item copyright]]></copyright><guid><![CDATA[item guid]]></guid><language><![CDATA[item language]]></language><pubDate><![CDATA[item pubDate]]></pubDate></item><item><description><![CDATA[item description]]></description><title><![CDATA[item title]]></title><link><![CDATA[item link]]></link><enclosure url=\"enclosure url\" length=\"enclosure length\" type=\"enclosure type\"/><category domain=\"category domain\">category value</category><category domain=\"category domain\">category value</category><author><![CDATA[item author]]></author><copyright><![CDATA[item copyright]]></copyright><guid><![CDATA[item guid]]></guid><language><![CDATA[item language]]></language><pubDate><![CDATA[item pubDate]]></pubDate></item></channel></rss>", output);
        LOG.info(output);
        LOG.log(Level.INFO, "TEST SUCCESSFUL: RssFeedWriterTest - localWriteTest");
    }
}