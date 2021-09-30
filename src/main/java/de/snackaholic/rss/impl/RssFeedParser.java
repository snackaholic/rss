package de.snackaholic.rss.impl;

import de.snackaholic.rss.api.IFeedByURLProvider;
import de.snackaholic.rss.model.Channel;
import de.snackaholic.rss.model.Feed;
import de.snackaholic.rss.model.Item;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.XMLEvent;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Implementation of the IFeedByURLProvider interface
 */
public class RssFeedParser implements IFeedByURLProvider {

    private static final Logger LOG = Logger.getLogger(RssFeedParser.class.getName());

    private final XMLInputFactory inputFactory = XMLInputFactory.newFactory();

    public RssFeedParser() {
        super();
    }

    @Override
    public Feed provideFeedByURL(URL url) {
        // try to get the file by the url
        try {
            // the feed
            Feed theParsedFeed = new Feed();
            theParsedFeed.setUrl(url);
            // the channel of the feed
            Channel theChannel = new Channel();
            // the list of items
            List<Item> items = new ArrayList<Item>();
            // the map for the value of channel
            Map<String, String> channelValueMap = new HashMap<String, String>();
            // the map for the value of one item
            Map<String, String> itemValueMap = new HashMap<String, String>();
            // flag whether or not we read <item> tag
            boolean firstItemAlreadyOccured = false;
            // get the inputstream
            InputStream fileInputStream = getInputStreamOfUrl(url);
            // open up eventreader to stream the provided xml
            XMLEventReader eventReader = inputFactory.createXMLEventReader(fileInputStream);
            // read the XML document
            while (eventReader.hasNext()) {
                // get the next element from the node
                XMLEvent event = eventReader.nextEvent();
                if (event.isStartElement()) {
                    // get its tag
                    String localPartStart = event.asStartElement().getName()
                            .getLocalPart();
                    // check if tag is item > we start creating items than
                    if (localPartStart.toLowerCase().equals("item")) {
                        firstItemAlreadyOccured = true;
                    }
                    LOG.log(Level.INFO, "GOT LOCAL START TAG NAME: " + localPartStart);
                    // get its data
                    XMLEvent nextEvent = eventReader.nextEvent();
                    if (nextEvent.isCharacters()) {
                        Characters data = nextEvent.asCharacters();
                        if (!data.isIgnorableWhiteSpace()) {
                            String sdata = data.toString();
                            LOG.log(Level.INFO, "GOT DATA: " + sdata);
                            // if we did not occure item yet
                            if (!firstItemAlreadyOccured) {
                                // add data to channeldata
                                channelValueMap.put(localPartStart.toLowerCase(), sdata);
                            } else {
                                // otherwise it is definitely item data
                                itemValueMap.put(localPartStart.toLowerCase(), sdata);
                            }
                        }
                    }
                } else if (event.isEndElement()) {
                    String localPartEnd = event.asEndElement().getName().getLocalPart();
                    LOG.log(Level.INFO, "GOT LOCAL END TAG NAME: " + localPartEnd);
                    // handle end of item
                    if (localPartEnd.toLowerCase().equals("item")) {
                        // create the item
                        Item newItem = new Item();
                        // add the values from the map
                        if (itemValueMap.containsKey("author")) {
                            newItem.setAuthor(itemValueMap.get("author"));
                        }
                        if (itemValueMap.containsKey("description")) {
                            newItem.setDescription(itemValueMap.get("description"));
                        }
                        if (itemValueMap.containsKey("enclosure")) {
                            newItem.setEnclosure(itemValueMap.get("enclosure"));
                        }
                        if (itemValueMap.containsKey("guid")) {
                            newItem.setGuid(itemValueMap.get("guid"));
                        }
                        if (itemValueMap.containsKey("title")) {
                            newItem.setTitle(itemValueMap.get("title"));
                        }
                        if (itemValueMap.containsKey("pubdate")) {
                            newItem.setPubDate(itemValueMap.get("pubdate"));
                        }
                        // clear the item map
                        itemValueMap = new HashMap<String, String>();
                        // add the item to the list of items
                        items.add(newItem);
                    }
                    // handle end of channel
                    if (localPartEnd.toLowerCase().equals("channel")) {
                        if (channelValueMap.containsKey("description")) {
                            theChannel.setDescription(channelValueMap.get("description"));
                        }
                        if (channelValueMap.containsKey("title")) {
                            theChannel.setTitle(channelValueMap.get("title"));
                        }
                        if (channelValueMap.containsKey("pubdate")) {
                            theChannel.setPubDate(channelValueMap.get("pubdate"));
                        }
                        if (channelValueMap.containsKey("copyright")) {
                            theChannel.setCopyright(channelValueMap.get("copyright"));
                        }
                        if (channelValueMap.containsKey("language")) {
                            theChannel.setLanguage(channelValueMap.get("language"));
                        }
                        if (channelValueMap.containsKey("link")) {
                            theChannel.setLink(channelValueMap.get("link"));
                        }
                    }
                }
            }
            LOG.log(Level.INFO, "SUCESSFULLY PARSED " + items.size() + " ITEMS FOR FEED " + theParsedFeed.getUrl());
            // close the reader
            eventReader.close();
            // close the inputstream
            fileInputStream.close();
            // transfer items to channel
            theChannel.setItems(items);
            // transfer channel to feed
            theParsedFeed.setChannel(theChannel);
            // return the feed
            return theParsedFeed;
        } catch (IOException e) {
            LOG.log(Level.SEVERE, "COULD NOT OPEN INPUTSTREAM BY URL", e);
        } catch (XMLStreamException e) {
            LOG.log(Level.SEVERE, "EXCEPTION STREAMING THE XML", e);
        }
        throw new RuntimeException("COULD NOT PARSE THAT FEED; ABORTING PROCESS");
    }

    private InputStream getInputStreamOfUrl(URL url) throws IOException {
        LOG.log(Level.INFO, "TRYING TO READ URL: " + url.toString());
        return url.openStream();
    }
}
