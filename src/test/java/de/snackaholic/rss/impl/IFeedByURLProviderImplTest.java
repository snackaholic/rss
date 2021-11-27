package de.snackaholic.rss.impl;

import de.snackaholic.rss.model.*;
import org.junit.Test;

import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;

// TODO check every possible model member of item, channel, feed
public class IFeedByURLProviderImplTest {

    private final Logger LOG = Logger.getLogger(IFeedByURLProviderImplTest.class.getName());

    public IFeedByURLProviderImplTest() {
        super();
    }

    /**
     * The parsing test for the local file
     */
    @Test
    public void localFileParseTest() {
        try {
            LOG.log(Level.INFO, "STARTING TEST: RssFeedParserTest - localParseTest");
            IFeedByURLProviderImpl iFeedByURLProvider = new IFeedByURLProviderImpl();
            Feed localTestFeed = iFeedByURLProvider.provideFeedByURL(Paths.get("src/test/resources/testfeed.xml").toUri().toURL());
            assertNotNull(localTestFeed.getUrl());
            assertNotNull(localTestFeed.getChannel());

            LOG.info("Checking channel cloud");
            assertNotNull(localTestFeed.getChannel().getCloud());
            List<Cloud> cloudList = localTestFeed.getChannel().getCloud();
            assertNotNull(cloudList.get(0));
            Cloud c = cloudList.get(0);
            // <cloud domain="randomdomain" port="80" path="/RPC2" registerProcedure="randomProcedure" protocol="xml-rpc" />
            assertEquals(c.getDomain(), "randomdomain");
            assertEquals(c.getPort(), Integer.valueOf("80"));
            assertEquals(c.getPath(), "/RPC2");
            assertEquals(c.getRegisterProcedure(), "randomProcedure");
            assertEquals(c.getProtocol(), "xml-rpc");

            LOG.info("Checking channel categories");
            assertNotNull(localTestFeed.getChannel().getCategory());
            String[] expectedChannelCategoryValueStrings = new String[]{"dies", "ist", "ein", "test"};
            for (int i = 0; i < expectedChannelCategoryValueStrings.length; i++) {
                String valueToFind = expectedChannelCategoryValueStrings[i];
                boolean found = false;
                for (int j = 0; j < localTestFeed.getChannel().getCategory().size(); j++) {
                    Category cat = localTestFeed.getChannel().getCategory().get(j);
                    if(cat.getValue().equals(valueToFind)) {
                        found = true;
                        break;
                    }
                }
                assertTrue(found);
            }

            LOG.log(Level.INFO, "CHECKING skipHours & skipDays:");
            assertNotNull(localTestFeed.getChannel().getSkipDays());
            List<String> channelSkipDays = localTestFeed.getChannel().getSkipDays();
            assertTrue(channelSkipDays.contains("Saturday"));
            assertTrue(channelSkipDays.contains("Sunday"));
            assertFalse(channelSkipDays.contains("invalid"));
            assertNotNull(localTestFeed.getChannel().getSkipHours());
            List<String> channelSkipHours = localTestFeed.getChannel().getSkipHours();
            assertTrue(channelSkipHours.contains("2"));
            assertTrue(channelSkipHours.contains("3"));
            assertTrue(channelSkipHours.contains("4"));
            assertTrue(channelSkipHours.contains("5"));
            assertTrue(channelSkipHours.contains("6"));
            assertFalse(channelSkipHours.contains("invalid"));

            LOG.log(Level.INFO, "CHECKING DESCRIPTION:");
            assertEquals("The Rough Cut features in-depth interviews with the top film and television post production professionals working in the industry today.  Hosted by @MattFeury of Avid Technology.", localTestFeed.getChannel().getDescription());

            LOG.log(Level.INFO, "CHECKING ITEMS:");
            assertEquals(118, localTestFeed.getChannel().getItems().size());

            LOG.log(Level.INFO, "CHECKING FIRST ITEM:");
            Item firstItem = localTestFeed.getChannel().getItems().get(0);
            assertEquals("© 2021 - The Rough Cut Studios", firstItem.getCopyright());
            assertEquals("en", firstItem.getLanguage());
            LOG.log(Level.INFO, "CHECKING FIRST ITEM ENCLOSURE:");
            Enclosure firstItemEnclosure = firstItem.getEnclosure();
            assertNotNull(firstItemEnclosure);
            assertEquals("audio/mpeg", firstItemEnclosure.getType());
            assertEquals("https://traffic.libsyn.com/secure/theroughcut/Candyman.mp3?dest-id=759218", firstItemEnclosure.getUrl());
            assertEquals("33512576", firstItemEnclosure.getLength());
            LOG.log(Level.INFO, "CHECKING FIRST ITEM CATEGORYS:");
            List<Category> firstItemCategorys = firstItem.getCategory();
            assertNotNull(firstItemCategorys);
            String[] expectedCategoryValueStrings = new String[]{"dies", "ist", "ein", "test"};
            for (int i = 0; i < firstItemCategorys.size(); i++) {
                assertEquals(firstItemCategorys.get(i).getValue(), expectedCategoryValueStrings[i]);
            }

            LOG.log(Level.INFO, "CHECKING CHANNEL IMAGE");
            Channel theChannel = localTestFeed.getChannel();
            Image theChannelImage = theChannel.getImage();
            assertNotNull(theChannelImage);
            assertEquals("The Rough Cut", theChannelImage.getTitle());
            assertEquals("https://ssl-static.libsyn.com/p/assets/3/d/3/c/3d3ccf0b358691dcbafc7308ab683e82/New_Rough_Cut_Logo_Bladev2XX.jpg", theChannelImage.getUrl());
            assertEquals("http://theroughcutpod.com", theChannel.getLink());
            LOG.log(Level.INFO, "TEST SUCCESSFUL: RssFeedParserTest - localParseTest");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }


    /**
     * the same test for the same file but loaded via the http protocol from the github repo
     * this should behave exactly the same as loading a local file - make sure that if you adjust above test that you cannot copy the code to this method until it was pushed...
     */
    @Test
    public void remoteFileParseTest() {
        try {
            LOG.log(Level.INFO, "STARTING TEST: RssFeedParserTest - remoteParseTest");
            IFeedByURLProviderImpl iFeedByURLProvider = new IFeedByURLProviderImpl();
            Feed localTestFeed = iFeedByURLProvider.provideFeedByURL(new URL("https://raw.githubusercontent.com/snackaholic/rss/main/src/test/resources/testfeed.xml"));
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