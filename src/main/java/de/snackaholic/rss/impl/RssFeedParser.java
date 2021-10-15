package de.snackaholic.rss.impl;

import de.snackaholic.rss.api.IFeedByURLProvider;
import de.snackaholic.rss.model.*;

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
 * TODO write values for lists (channel)
 * TODO extend DefaultHandler for event handling and use SAXParserFactory and Saxparser for this
 */
public class RssFeedParser implements IFeedByURLProvider {

    private static final Logger LOG = Logger.getLogger(RssFeedParser.class.getName());
    private final XMLInputFactory inputFactory = XMLInputFactory.newFactory();

    public RssFeedParser() {
        super();
    }

    /**
     * Validates a potential item against mandatory fields.
     *
     * @param itemCandidate the item
     * @return whether the item is valid and fulfills the requirements
     */
    private boolean itemCandidateFulfillsRequirements(Item itemCandidate) {
        if (itemCandidate.getDescription() != null && itemCandidate.getLink() != null && itemCandidate.getTitle() != null) {
            return true;
        } else {
            LOG.severe("ITEM DID NOT FULFILL REQUIREMENTS; IGNORING IT: " + itemCandidate);
            return false;
        }
    }

    /**
     * Will transfer the values from the map to the channel
     *
     * @param theChannel      the channel that should get the values from the map
     * @param channelValueMap the map that contains the values for the channel. !lowercase keys!
     */
    private void patchChannelDataByValueMap(Channel theChannel, Map<String, List<String>> channelValueMap) {
        // transfer simple string values
        if (channelValueMap.containsKey("description")) {
            theChannel.setDescription(channelValueMap.get("description").get(0));
        }
        if (channelValueMap.containsKey("title")) {
            theChannel.setTitle(channelValueMap.get("title").get(0));
        }
        if (channelValueMap.containsKey("pubdate")) {
            theChannel.setPubDate(channelValueMap.get("pubdate").get(0));
        }
        if (channelValueMap.containsKey("copyright")) {
            theChannel.setCopyright(channelValueMap.get("copyright").get(0));
        }
        if (channelValueMap.containsKey("language")) {
            theChannel.setLanguage(channelValueMap.get("language").get(0));
        }
        if (channelValueMap.containsKey("link")) {
            theChannel.setLink(channelValueMap.get("link").get(0));
        }
        if (channelValueMap.containsKey("managingeditor")) {
            theChannel.setManagingEditor(channelValueMap.get("managingeditor").get(0));
        }
        if (channelValueMap.containsKey("webmaster")) {
            theChannel.setWebMaster(channelValueMap.get("webmaster").get(0));
        }
        if (channelValueMap.containsKey("lastbuilddate")) {
            theChannel.setLastBuildDate(channelValueMap.get("lastbuilddate").get(0));
        }
        if (channelValueMap.containsKey("generator")) {
            theChannel.setGenerator(channelValueMap.get("generator").get(0));
        }
        if (channelValueMap.containsKey("docs")) {
            theChannel.setDocs(channelValueMap.get("docs").get(0));
        }
        if (channelValueMap.containsKey("ttl")) {
            theChannel.setTtl(channelValueMap.get("ttl").get(0));
        }
        if (channelValueMap.containsKey("rating")) {
            theChannel.setRating(channelValueMap.get("rating").get(0));
        }
        // TODO SET LISTS
    }

    /**
     * Will create a new item using a map; null if not all mandatory fields are provided.
     *
     * @param itemValueMap the map that holds the potential values. !lowercase keys!
     * @return a new item - null if the item candidate does not fulfill all requirements
     */
    private Item getItemFromValueMap(Map<String, List<String>> itemValueMap) {
        Item newItem = new Item();
        if (itemValueMap.containsKey("description")) {
            newItem.setDescription(itemValueMap.get("description").get(0));
        }
        if (itemValueMap.containsKey("title")) {
            newItem.setTitle(itemValueMap.get("title").get(0));
        }
        if (itemValueMap.containsKey("link")) {
            newItem.setLink(itemValueMap.get("link").get(0));
        }
        if (itemValueMap.containsKey("author")) {
            newItem.setAuthor(itemValueMap.get("author").get(0));
        }
        if (itemValueMap.containsKey("guid")) {
            newItem.setGuid(itemValueMap.get("guid").get(0));
        }
        if (itemValueMap.containsKey("pubdate")) {
            newItem.setPubDate(itemValueMap.get("pubdate").get(0));
        }
        if (itemValueMap.containsKey("language")) {
            newItem.setLanguage(itemValueMap.get("language").get(0));
        }
        if (itemValueMap.containsKey("copyright")) {
            newItem.setCopyright(itemValueMap.get("copyright").get(0));
        }
        if (itemValueMap.containsKey("enclosure$url")) {
            String url = null;
            String length = null;
            String type = null;
            if (itemValueMap.containsKey("enclosure$url")) {
                url = itemValueMap.get("enclosure$url").get(0);
            }
            if (itemValueMap.containsKey("enclosure$length")) {
                length = itemValueMap.get("enclosure$length").get(0);
            }
            if (itemValueMap.containsKey("enclosure$type")) {
                type = itemValueMap.get("enclosure$type").get(0);
            }
            if (url != null && length != null && type != null) {
                Enclosure enc = new Enclosure();
                enc.setUrl(url);
                enc.setLength(length);
                enc.setType(type);
                newItem.setEnclosure(enc);
            }
        }
        if (itemValueMap.containsKey("category")) {
            List<String> categoryStrings = itemValueMap.get("category");
            if (categoryStrings.size() > 0) {
                List<Category> itemCategoryList = new ArrayList<>();
                for (String categoryString : categoryStrings) {
                    Category newCat = new Category();
                    String catValue = categoryString.trim().replaceAll("\n$", "");
                    if (catValue.length() > 0) {
                        newCat.setValue(categoryString);
                        itemCategoryList.add(newCat);
                    }
                }
                newItem.setCategory(itemCategoryList);
            }
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
        LOG.info("TRYING TO PARSE FEED BY URL: " + url.toString());
        // try to get the file by the url
        try {
            // the feed
            Feed theParsedFeed = new Feed();
            theParsedFeed.setUrl(url);
            // the channel of the feed
            Channel theChannel = new Channel();
            // the list of items
            List<Item> items = new ArrayList<>();
            // the map for the value of channel
            Map<String, List<String>> channelValueMap = new HashMap<>();
            // the map for the value of one item
            Map<String, List<String>> itemValueMap = new HashMap<>();
            // the map for the attributes of an image
            Map<String, List<String>> imageValueMap = new HashMap<>();
            // the image that is currently read by the parser
            Image image = new Image();
            // the attributes of the last start tag
            List<Attribute> attributes;
            // flag whether we read <item> tag
            boolean firstItemAlreadyOccured = false;
            boolean inImage = false;
            boolean lastEventWasClosingEvent = false;
            String currentElementName = "";
            InputStream fileInputStream = getInputStreamOfUrl(url);
            XMLEventReader eventReader = inputFactory.createXMLEventReader(fileInputStream);
            while (eventReader.hasNext()) {
                // get the next element from the node
                XMLEvent event = eventReader.nextEvent();
                // handle each event diffrently
                if (event.isStartElement()) {
                    lastEventWasClosingEvent = false;
                    // get the element name
                    currentElementName = event.asStartElement().getName()
                            .getLocalPart();
                    // check if tag is item or image > we start creating item / image than
                    if (currentElementName.equalsIgnoreCase("item")) {
                        firstItemAlreadyOccured = true;
                    } else if (currentElementName.equalsIgnoreCase("image")) {
                        inImage = true;
                    }
                    LOG.info(" GOT LOCAL START TAG NAME: " + currentElementName);
                    // try to extract list of attributes
                    attributes = getAttributes(event.asStartElement());
                    LOG.info(currentElementName + " GOT " + attributes.size() + " ATTRIBUTES");
                    // add them to the correct map
                    if (attributes.size() > 0) {
                        if (inImage) {
                            addAttributesToMap(imageValueMap, attributes, currentElementName);
                        } else {
                            // we are processing the channel
                            if (!firstItemAlreadyOccured) {
                                addAttributesToMap(channelValueMap, attributes, currentElementName);
                            } else {
                                // we are processing an item
                                addAttributesToMap(itemValueMap, attributes, currentElementName);
                            }
                        }
                    }
                } else if (event.isEndElement()) {
                    lastEventWasClosingEvent = true;
                    currentElementName = event.asEndElement().getName().getLocalPart();
                    LOG.info("GOT LOCAL END TAG NAME: " + currentElementName);
                    // handle end of image tag
                    if (currentElementName.equalsIgnoreCase("image")) {
                        inImage = false;
                        // TODO add image to the item -> see itunes / google for possible attr
                        // TODO handle attributes
                        // TODO hanlde single item image
                        // TODO it looks like there can be mutliple images...
                        if (!firstItemAlreadyOccured) {
                            if (theChannel.getImage() == null) {
                                LOG.info("SETTING IMAGE FOR CHANNEL" + image);
                                theChannel.setImage(image.clone());
                            } else {
                                LOG.severe("DID NOT OVERWRITE IMAGE FOR CHANNEL WITH FOLLOWING IMAGE:" + image);
                            }

                        }
                        // reset the image
                        image = new Image();
                        LOG.info("IMAGE FROM CHANNEL NOW HAS VALUE" + theChannel.getImage());
                        imageValueMap = new HashMap<>();
                    }
                    // handle end of item
                    if (currentElementName.equalsIgnoreCase("item")) {
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
                    if (currentElementName.equalsIgnoreCase("channel")) {
                        patchChannelDataByValueMap(theChannel, channelValueMap);
                        // clear the channelValueMap, although we only respect one channel at the moment
                        channelValueMap = new HashMap<>();
                    }
                } else if (event.isCharacters()) {
                    // we ignore text after closing element
                    if (lastEventWasClosingEvent) {
                        lastEventWasClosingEvent = false;
                    } else {
                        Characters data = event.asCharacters();
                        if (!data.isIgnorableWhiteSpace()) {
                            String characterData = data.toString();
                            LOG.info("GOT CHARACTER DATA: " + characterData);
                            // check if we are in channel, item, or image right now
                            if (inImage) {
                                LOG.info("IN IMAGE SETTING IMAGE DATA: " + characterData);
                                if (currentElementName.equalsIgnoreCase("title")) {
                                    image.setTitle(characterData);
                                } else if (currentElementName.equalsIgnoreCase("link")) {
                                    image.setLink(characterData);
                                } else if (currentElementName.equalsIgnoreCase("description")) {
                                    image.setDescription(characterData);
                                } else if (currentElementName.equalsIgnoreCase("width")) {
                                    image.setWidth(Integer.parseInt(characterData));
                                } else if (currentElementName.equalsIgnoreCase("height")) {
                                    image.setHeight(Integer.parseInt(characterData));
                                } else if (currentElementName.equalsIgnoreCase("url")) {
                                    image.setUrl(characterData);
                                }
                                LOG.info("IMAGE HAS NOW FOLLWING DATA " + image);
                            } else {
                                // if we did not occur item yet
                                if (!firstItemAlreadyOccured) {
                                    // add data to channeldatamap
                                    addCharacterDataToMap(channelValueMap, currentElementName, characterData);
                                } else {
                                    // otherwise, it is item data
                                    addCharacterDataToMap(itemValueMap, currentElementName, characterData);
                                }
                            }
                        }
                    }
                }
            }
            LOG.info("SUCCESSFULLY PARSED " + items.size() + " ITEMS FOR FEED " + theParsedFeed.getUrl());
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

    private void addAttributesToMap(Map<String, List<String>> map, List<Attribute> attributes, String prefix) {
        if (prefix != null) {
            prefix = prefix.toLowerCase();
            if (attributes.size() != 0) {
                for (Attribute attr : attributes) {
                    String key = prefix + "$" + attr.getName().getLocalPart().toLowerCase();
                    if (!map.containsKey(key)) {
                        map.put(key, new ArrayList<>());
                    }
                    ArrayList<String> l = (ArrayList<String>) map.get(key);
                    l.add(attr.getValue());
                    map.put(key, l);
                }
            }
        }
    }

    private void addCharacterDataToMap(Map<String, List<String>> map, String localPartStart, String text) {
        String lowerLocalPartStart = localPartStart.toLowerCase();
        if (text.length() > 0) {
            if (!map.containsKey(lowerLocalPartStart)) {
                map.put(lowerLocalPartStart, new ArrayList<>());
            }
            ArrayList<String> l = (ArrayList<String>) map.get(lowerLocalPartStart);
            l.add(text);
            map.put(lowerLocalPartStart, l);
        }
    }

    /**
     * Provides inputstream by url
     *
     * @param url the url
     * @return the stream
     * @throws IOException when something goes wrong opening the stream
     */
    private InputStream getInputStreamOfUrl(URL url) throws IOException {
        LOG.info("TRYING TO READ URL: " + url.toString());
        return url.openStream();
    }
}
