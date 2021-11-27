package de.snackaholic.rss.impl;

import de.snackaholic.rss.api.IRssFeedParser;
import de.snackaholic.rss.model.*;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.*;
import java.util.logging.Logger;

/**
 * Implementation of the IFeedByURLProvider interface
 */
public class RssFeedParser extends DefaultHandler implements IRssFeedParser {

    private static final Logger LOG = Logger.getLogger(RssFeedParser.class.getName());

    // currently scanned element informations
    private String currentElementName = "";

    // attribute maps for the diffrent elements that will be created based on contained values
    private Map<String, List<String>> channelValueMap = new HashMap<>();
    private Map<String, List<String>> itemValueMap = new HashMap<>();
    private Map<String, List<String>> imageValueMap = new HashMap<>();

    // state flags
    private boolean inItem = false;
    private boolean inImage = false;
    private boolean lastEventWasEndElementEvent = false;
    private boolean inSkipHours = false;
    private boolean inSkipDays = false;

    // the specific objects we care about
    private Feed theParsedFeed = new Feed();
    private Channel theChannel = new Channel();
    private List<Item> items = new ArrayList<>();
    // the current builded image
    private Image image = new Image();
    // channel skiphours & skipdays
    private List<String> channelSkipHours = new ArrayList<>();
    private List<String> channelSkipDays = new ArrayList<>();

    public RssFeedParser() {
        super();
    }

    @Override
    public void startDocument()
            throws SAXException {
        LOG.info("START DOCUMENT");
    }

    @Override
    public void startElement(String uri, String localName,
                             String qName, Attributes attributes) throws SAXException {
        LOG.info("startElement: uri=" + uri + ", localName=" + localName + ", qName=" + qName + ", attributessize=" + attributes.getLength());
        lastEventWasEndElementEvent = false;
        currentElementName = qName;
        handleStartElementInFlags(currentElementName);
        if (attributes.getLength() > 0) {
            handleStartElementAttributes(attributes);
        }
    }

    @Override
    public void endElement(String uri, String localName,
                           String qName)
            throws SAXException {
        LOG.info("endElement: uri=" + uri + ", localName=" + localName + ", qName=" + qName);
        lastEventWasEndElementEvent = true;
        currentElementName = qName;
        handleEndOfElement(currentElementName);
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
        StringBuilder temp = new StringBuilder();
        temp.append(ch, start, length);
        LOG.info("characters: start: " + start + ", length: " + length + ", content: " + temp.toString());
        // we ignore text after closing element
        if (lastEventWasEndElementEvent) {
            LOG.info("since last event was closing event we ignored those characters");
            lastEventWasEndElementEvent = false;
        } else {
            String characterData = temp.toString();
            // check if we are in channel, item, or image right now
            if (inImage) {
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
            } else if (inItem) {
                addCharacterDataToMap(itemValueMap, currentElementName, characterData);
            } else if (inSkipDays) {
                if (currentElementName.equalsIgnoreCase("day") && Day.fromString(characterData) != null) {
                    channelSkipDays.add(Day.fromString(characterData).getValue());
                }
            } else if (inSkipHours) {
                if (currentElementName.equalsIgnoreCase("hour") && isNumeric(characterData)) {
                    channelSkipHours.add(characterData);
                }
            } else {
                addCharacterDataToMap(channelValueMap, currentElementName, characterData);
            }
        }
    }

    @Override
    public void endDocument()
            throws SAXException {
        LOG.info("END DOCUMENT");
        // transfer items to channel
        theChannel.setItems(items);
        if (channelSkipHours.size() > 0) {
            LOG.warning("CHANNEL HAS SKIPHOURS");
            theChannel.setSkipHours(channelSkipHours);
        }
        if (channelSkipDays.size() > 0) {
            LOG.warning("CHANNEL HAS SKIPDAYS");
            theChannel.setSkipDays(channelSkipDays);
        }
        LOG.info("CHANNEL HAS " + items.size() + " ITEMS");
        // transfer channel to feed
        theParsedFeed.setChannel(theChannel);
    }

    @Override
    public Feed getFeed() {
        return theParsedFeed;
    }

    private void handleStartElementInFlags(String currentElementName) {
        switch (currentElementName.toLowerCase(Locale.ROOT)) {
            case "item":
                inItem = true;
                break;
            case "image":
                inImage = true;
                break;
            case "skiphours":
                inSkipHours = true;
                break;
            case "skipdays":
                inSkipDays = true;
                break;
        }
    }

    private void handleStartElementAttributes(Attributes attributes) {
        if (inImage) {
            addAttributesToMap(imageValueMap, attributes, currentElementName);
        } else if (inItem) {
            addAttributesToMap(itemValueMap, attributes, currentElementName);
        } else {
            addAttributesToMap(channelValueMap, attributes, currentElementName);
        }
    }

    private void handleEndOfElement(String qName) {
        switch (qName.toLowerCase(Locale.ROOT)) {
            case "item":
                inItem = false;
                handleEndItemElement();
                break;
            case "image":
                inImage = false;
                handleEndImageElement();
                break;
            case "skiphours":
                inSkipHours = false;
                break;
            case "skipdays":
                inSkipDays = false;
                break;
            case "channel":
                handleEndChannelElement();
                break;
        }
    }

    private void handleEndImageElement() {
        // TODO handle attributes
        if (!inItem) {
            if (theChannel.getImage() == null) {
                LOG.info("SETTING IMAGE FOR CHANNEL" + image);
                theChannel.setImage(image.clone());
            } else {
                // TODO it looks like there can be multiple images for channel & item...
                LOG.severe("DID NOT OVERWRITE IMAGE FOR CHANNEL WITH FOLLOWING IMAGE:" + image);
            }
        } else {
            // TODO add image to the item -> see itunes / google for possible attr
        }
        // reset the image
        image = new Image();
        LOG.info("IMAGE FROM CHANNEL NOW HAS VALUE" + theChannel.getImage());
        imageValueMap = new HashMap<>();
    }

    private void handleEndItemElement() {
        Optional<Item> newItem = getValidItemFromValueMap(itemValueMap);
        if (newItem.isPresent()) {
            items.add(newItem.get());
        }
        itemValueMap = new HashMap<>();
    }

    private void handleEndChannelElement() {
        patchChannelDataByValueMap(theChannel, channelValueMap);
        // clear the channelValueMap, although we only respect one channel at the moment
        channelValueMap = new HashMap<>();
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
        if (text.length() > 0) {
            String lowerLocalPartStart = localPartStart.toLowerCase();
            if (!map.containsKey(lowerLocalPartStart)) {
                map.put(lowerLocalPartStart, new ArrayList<>());
            }
            ArrayList<String> l = (ArrayList<String>) map.get(lowerLocalPartStart);
            l.add(text);
            map.put(lowerLocalPartStart, l);
        }
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
        if (channelValueMap.containsKey("category")) {
            // todo category can have domain attribute and we loose it rn
            List<String> categories = channelValueMap.get("category");
            List<Category> categoryList = new ArrayList<>();
            for (int i = 0; i < categories.size(); i++) {
                String value = categories.get(i);
                if (value != null) {
                    Category theCategory = new Category();
                    theCategory.setValue(value);
                    categoryList.add(theCategory);
                }
            }
            if (categoryList.size() > 0) {
                theChannel.setCategory(categoryList);
            }
        }
        if (channelValueMap.containsKey("cloud")) {
            List<Cloud> cloudList = new ArrayList<>();
            // todo we need to be able to handle multiple cloud elements
            String domain = null;
            String port = null;
            String path = null;
            String registerProcedure = null;
            String protocol = null;
            if (channelValueMap.containsKey("cloud$domain")) {
                domain = channelValueMap.get("cloud$domain").get(0);
            }
            if (channelValueMap.containsKey("cloud$port")) {
                port = channelValueMap.get("cloud$port").get(0);
            }
            if (channelValueMap.containsKey("cloud$path")) {
                path = channelValueMap.get("cloud$path").get(0);
            }
            if (channelValueMap.containsKey("cloud$registerProcedure")) {
                registerProcedure = channelValueMap.get("cloud$registerProcedure").get(0);
            }
            if (channelValueMap.containsKey("cloud$protocol")) {
                protocol = channelValueMap.get("cloud$protocol").get(0);
            }
            if (domain != null && port != null && path != null) {
                Cloud cloud = new Cloud();
                cloud.setDomain(domain);
                cloud.setPort(Integer.parseInt(port));
                cloud.setPath(path);
                if (protocol != null) {
                    cloud.setProtocol(protocol);
                }
                if (registerProcedure != null) {
                    cloud.setRegisterProcedure(registerProcedure);
                }
                cloudList.add(cloud);
            }
            if (cloudList.size() > 0) {
                theChannel.setCloud(cloudList);
            }
        }
    }

    /**
     * Will create a new item using the provided values if all mandatory fields are provided.
     *
     * @param itemValueMap the map that holds the potential values. !lowercase keys!
     * @return a new item - null if the item candidate does not fulfill all requirements
     */
    private Optional<Item> getValidItemFromValueMap(Map<String, List<String>> itemValueMap) {

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
            return Optional.of(newItem);
        }
        return Optional.empty();
    }

    private boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch(NumberFormatException e){}
        return false;
    }

    private void debugMap(Map<String, List<String>> map) {
        String[] keys = map.keySet().toArray(new String[0]);
        LOG.info("Map key amount: " + keys.length);
        for (int i = 0; i < keys.length; i++) {
            List<String> valueList = map.get(keys[i]);
            LOG.info("key: " + keys[i] + ", value: " + valueList);
        }
    }
}
