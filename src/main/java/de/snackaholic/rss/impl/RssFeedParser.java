package de.snackaholic.rss.impl;

import de.snackaholic.rss.api.IFeedByURLProvider;
import de.snackaholic.rss.model.Channel;
import de.snackaholic.rss.model.Feed;
import de.snackaholic.rss.model.Item;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Implementation of the IFeedByURLProvider interface
 * TODO write values for categorys (item) into multivaluemap and transform them to list
 */
public class RssFeedParser implements IFeedByURLProvider {

    private static final Logger LOG = Logger.getLogger(RssFeedParser.class.getName());
    private final XMLInputFactory inputFactory = XMLInputFactory.newFactory();

    public RssFeedParser() {
        super();
    }

    /**
     * TODO respect mandatory fields
     * TODO handle missing mandatory fields via options for desired strategy
     *
     * @param itemCandidate
     * @return whether or not the item is valid and fulfills the requirements
     */
    private boolean itemCandidateFulfillsRequirements(Item itemCandidate) {
        return true;
    }

    /**
     * Will transfer the values from the map to the channel
     * @param theChannel the channel that should get the values from the map
     * @param channelValueMap the map that contains the values for the channel. !lowercase keys!
     */
    private void patchChannelDataByValueMap(Channel theChannel, Map<String, String> channelValueMap) {
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
        if (channelValueMap.containsKey("managingeditor")) {
            theChannel.setManagingEditor(channelValueMap.get("managingeditor"));
        }
        if (channelValueMap.containsKey("webmaster")) {
            theChannel.setWebMaster(channelValueMap.get("webmaster"));
        }
        if (channelValueMap.containsKey("lastbuilddate")) {
            theChannel.setLastBuildDate(channelValueMap.get("lastbuilddate"));
        }
        if (channelValueMap.containsKey("generator")) {
            theChannel.setGenerator(channelValueMap.get("generator"));
        }
        if (channelValueMap.containsKey("docs")) {
            theChannel.setDocs(channelValueMap.get("docs"));
        }
        if (channelValueMap.containsKey("ttl")) {
            theChannel.setTtl(channelValueMap.get("ttl"));
        }
        /* TODO fix me
        if (channelValueMap.containsKey("image")) {
            theChannel.setImage(channelValueMap.get("image"));
        }
        */
        if (channelValueMap.containsKey("rating")) {
            theChannel.setRating(channelValueMap.get("rating"));
        }
    }

    /**
     * Will create a new item using a map; null if not all mandatory fields are provided.
     * @param itemValueMap the map that holds the potential values. !lowercase keys!
     * @return a new item - null if the item candidate does not fulfill all requirements
     */
    private Item getItemFromValueMap(Map<String, String> itemValueMap) {
        Item newItem = new Item();
        // add the values from the map
        if (itemValueMap.containsKey("author")) {
            newItem.setAuthor(itemValueMap.get("author"));
        }
        if (itemValueMap.containsKey("description")) {
            newItem.setDescription(itemValueMap.get("description"));
        }
        /* TODO fix me
        if (itemValueMap.containsKey("enclosure")) {
            newItem.setEnclosure(itemValueMap.get("enclosure"));
        }
        */
        if (itemValueMap.containsKey("guid")) {
            newItem.setGuid(itemValueMap.get("guid"));
        }
        if (itemValueMap.containsKey("title")) {
            newItem.setTitle(itemValueMap.get("title"));
        }
        if (itemValueMap.containsKey("pubdate")) {
            newItem.setPubDate(itemValueMap.get("pubdate"));
        }
        if (itemValueMap.containsKey("link")) {
            newItem.setLink(itemValueMap.get("link"));
        }
        if (itemValueMap.containsKey("language")) {
            newItem.setLanguage(itemValueMap.get("language"));
        }
        if (itemValueMap.containsKey("copyright")) {
            newItem.setCopyright(itemValueMap.get("copyright"));
        }
        if (itemCandidateFulfillsRequirements(newItem)) {
            return newItem;
        }
        return null;
    }

    private List<Attribute> getAttributes(StartElement startElement) {
        List<Attribute> newElementAttributeList = new ArrayList<>();
        Iterator<?> existingAttributesIterator = startElement.getAttributes();
        while (existingAttributesIterator.hasNext()) {
            Attribute attribute = (Attribute) existingAttributesIterator.next();
            newElementAttributeList.add(attribute);
        }
        return newElementAttributeList;
    }

    @Override
    public Feed provideFeedByURL(URL url) {
        LOG.log(Level.INFO, "TRYING TO PARSE FEED BY URL: " + url.toString());
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
            // the attributes of the last start tag
            List<Attribute> attributes;
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
                    // get the element name
                    String localPartStart = event.asStartElement().getName()
                            .getLocalPart();
                    // check if tag is item > we start creating items than
                    if (localPartStart.equalsIgnoreCase("item")) {
                        firstItemAlreadyOccured = true;
                    }
                    LOG.log(Level.INFO, " GOT LOCAL START TAG NAME: " + localPartStart);
                    // try to extract list of attributes
                    attributes = getAttributes(event.asStartElement());
                    LOG.log(Level.INFO, localPartStart + " GOT " + attributes.size() + " ATTRIBUTES");
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
                                addElementDataToMap(channelValueMap, attributes, localPartStart, sdata);
                            } else {
                                // otherwise it is item data
                                addElementDataToMap(itemValueMap, attributes, localPartStart, sdata);
                            }
                        }
                    }
                } else if (event.isEndElement()) {
                    String localPartEnd = event.asEndElement().getName().getLocalPart();
                    LOG.log(Level.INFO, "GOT LOCAL END TAG NAME: " + localPartEnd);
                    // handle end of item
                    if (localPartEnd.equalsIgnoreCase("item")) {
                        // create the item
                        Item newItem = getItemFromValueMap(itemValueMap);
                        // add the item to the list of items
                        if (newItem != null) {
                            items.add(newItem);
                        }
                        // clear the item map nonetheless
                        itemValueMap = new HashMap<>();
                    }
                    // handle end of channel
                    if (localPartEnd.equalsIgnoreCase("channel")) {
                        patchChannelDataByValueMap(theChannel, channelValueMap);
                        // clear the channelValueMap, although we only respect one channel at the moment
                        channelValueMap = new HashMap<>();
                    }
                }
            }
            LOG.log(Level.INFO, "SUCCESSFULLY PARSED " + items.size() + " ITEMS FOR FEED " + theParsedFeed.getUrl());
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

    private void addElementDataToMap(Map<String, String> map, List<Attribute> attributes, String localPartStart, String text) {
        String lowerLocalPartStart = localPartStart.toLowerCase();
        map.put(lowerLocalPartStart, text);
        if (attributes.size() != 0) {
            for (int i = 0; i < attributes.size(); i++) {
                Attribute attr = attributes.get(i);
                map.put(lowerLocalPartStart +"$"+ attr.getName().getLocalPart().toLowerCase(), attr.getValue());
            }
        }
    }

    /**
     * Provides inputstream by url
     * @param url the url
     * @return the stream
     * @throws IOException
     */
    private InputStream getInputStreamOfUrl(URL url) throws IOException {
        LOG.log(Level.INFO, "TRYING TO READ URL: " + url.toString());
        return url.openStream();
    }
}
