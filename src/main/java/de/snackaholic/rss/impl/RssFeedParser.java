package de.snackaholic.rss.impl;

import de.snackaholic.rss.api.IRssFeedParser;
import de.snackaholic.rss.model.*;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.events.Attribute;
import java.util.*;
import java.util.logging.Logger;

/**
 * Implementation of the IFeedByURLProvider interface
 * TODO write values for lists (channel)
 */
public class RssFeedParser extends DefaultHandler implements IRssFeedParser {

    private static final Logger LOG = Logger.getLogger(RssFeedParser.class.getName());
    private final XMLInputFactory inputFactory = XMLInputFactory.newFactory();

    // the feed
    Feed theParsedFeed = new Feed();
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
    StringBuilder elementValue;

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

    @Override
    public void startElement(String uri, String localName,
                             String qName, Attributes attributes) throws SAXException {
        LOG.info("startElement: uri=" + uri + ", localName=" + localName + ", qName=" + qName + ", attributes=" + attributes);
        elementValue = new StringBuilder();
        lastEventWasClosingEvent = false;
        // get the element name
        currentElementName = qName;
        // check if tag is item or image > we start creating item / image than
        if (currentElementName.equalsIgnoreCase("item")) {
            firstItemAlreadyOccured = true;
        } else if (currentElementName.equalsIgnoreCase("image")) {
            inImage = true;
        }
        LOG.info(" GOT LOCAL START TAG NAME: " + currentElementName);
        // try to extract list of attributes
        LOG.info(currentElementName + " GOT " + attributes.getLength() + " ATTRIBUTES");
        // add them to the correct map
        if (attributes.getLength() > 0) {
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
    }

    @Override
    public void endElement(String uri, String localName,
                           String qName)
            throws SAXException {
        LOG.info("endElement: uri=" + uri + ", localName=" + localName + ", qName=" + qName);
        lastEventWasClosingEvent = true;
        currentElementName = qName;
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
    }

    @Override
    public void ignorableWhitespace(char ch[], int start, int length)
            throws SAXException {
        LOG.info("ignorableWhitespace: start: " + start + ", length: " + length);
    }

    @Override
    public void skippedEntity(String name)
            throws SAXException {
        LOG.info("skippedEntity: name: " + name);
    }

    @Override
    public void processingInstruction(String target, String data)
            throws SAXException {
        LOG.info("processingInstruction: target: " + target + ", data: " + data);
    }

    @Override
    public void characters(char ch[], int start, int length)
            throws SAXException {
        // characterdata can be split over multiple calls
        StringBuilder temp = new StringBuilder();
        if (elementValue == null) {
            elementValue = new StringBuilder();
        } else {
            elementValue.append(ch, start, length);
            temp.append(ch, start, length);
        }
        LOG.info("characters: start: " + start + ", length: " + length + ", content:" + temp.toString());
        // we ignore text after closing element
        if (lastEventWasClosingEvent) {
            LOG.info("since last event was closing event we ignore characters: start: " + start + ", length: " + length + ", content: " + temp.toString());
            lastEventWasClosingEvent = false;
        } else {
            String characterData = temp.toString();
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
                LOG.info("IMAGE HAS NOW FOLLOWING DATA " + image);
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

    @Override
    public void startDocument()
            throws SAXException {
        LOG.info("START DOCUMENT");
    }

    @Override
    public void endDocument()
            throws SAXException {
        LOG.info("END DOCUMENT");
        // transfer items to channel
        theChannel.setItems(items);
        LOG.info("CHANNEL HAS " + items.size() + " ITEMS");
        // transfer channel to feed
        theParsedFeed.setChannel(theChannel);
    }

    private void addAttributesToMap(Map<String, List<String>> map, Attributes attributes, String prefix) {
        if (prefix != null) {
            prefix = prefix.toLowerCase();
            if (attributes.getLength() != 0) {
                for (int i = 0; i < attributes.getLength(); i++) {
                    String key = prefix + "$" + attributes.getLocalName(i);
                    if (!map.containsKey(key)) {
                        map.put(key, new ArrayList<>());
                    }
                    ArrayList<String> l = (ArrayList<String>) map.get(key);
                    l.add(attributes.getValue(i));
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

    @Override
    public Feed getFeed() {
        return theParsedFeed;
    }
}
