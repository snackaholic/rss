package de.snackaholic.rss.impl;

import de.snackaholic.rss.api.IRssFeedWriter;
import de.snackaholic.rss.model.*;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


public class RssFeedWriter implements IRssFeedWriter {

    private static final Logger LOG = Logger.getLogger(RssFeedWriter.class.getName());

    private final XMLOutputFactory outputFactory = XMLOutputFactory.newFactory();
    private Writer stringWriter;
    private XMLStreamWriter writer;

    public RssFeedWriter() {
        super();
    }

    private boolean feedIsWritable(Feed feed) {
        Channel channel = feed.getChannel();
        if (channel != null) {
            List<Item> items = channel.getItems();
            return items != null && items.size() > 0;
        }
        return false;
    }

    void writeElementWithCDataIfNotNull(String cdata, String elementName, XMLStreamWriter writer) throws XMLStreamException {
        if (cdata != null && cdata.length() > 0) {
            writer.writeStartElement(elementName);
            writer.writeCData(cdata);
            writer.writeEndElement();
        }
    }

    @Override
    public void writeItem(Item item) {
        try {
            writer.writeStartElement("item");
            // write mandatory item elements
            writeElementWithCDataIfNotNull(item.getDescription(), "description", writer);
            writeElementWithCDataIfNotNull(item.getTitle(), "title", writer);
            writeElementWithCDataIfNotNull(item.getLink(), "link", writer);
            // write optionals
            if (item.getEnclosure() != null) {
                writeEnclosure(item.getEnclosure());
            }
            if (item.getCategory() != null && item.getCategory().size() > 0) {
                for (Category cat : item.getCategory()) {
                    writeCategory(cat);
                }
            }
            writeElementWithCDataIfNotNull(item.getAuthor(), "author", writer);
            writeElementWithCDataIfNotNull(item.getCopyright(), "copyright", writer);
            writeElementWithCDataIfNotNull(item.getGuid(), "guid", writer);
            writeElementWithCDataIfNotNull(item.getLanguage(), "language", writer);
            writeElementWithCDataIfNotNull(item.getPubDate(), "pubDate", writer);
            // close the item
            writer.writeEndElement();
        } catch (XMLStreamException xmlStreamException) {
            xmlStreamException.printStackTrace();
        }

    }

    void safeWriteAttribute(String value, String name, XMLStreamWriter writer) throws XMLStreamException {
        if (value != null && value.length() > 0) {
            writer.writeAttribute(name, value);
        }
    }

    @Override
    public void writeEnclosure(Enclosure enclosure) {
        try {
            writer.writeEmptyElement("enclosure");
            safeWriteAttribute(enclosure.getUrl(), "url", writer);
            safeWriteAttribute(enclosure.getLength(), "length", writer);
            safeWriteAttribute(enclosure.getType(), "type", writer);
        } catch (XMLStreamException xmlStreamException) {
            xmlStreamException.printStackTrace();
        }
    }

    @Override
    public void writeCloud(Cloud cloud) {
        try {
            writer.writeEmptyElement("cloud");
            safeWriteAttribute(cloud.getDomain(), "domain", writer);
            safeWriteAttribute(cloud.getPath(), "path", writer);
            safeWriteAttribute(cloud.getProtocol(), "protocol", writer);
            safeWriteAttribute(cloud.getRegisterProcedure(), "registerProcedure", writer);
            safeWriteAttribute(String.valueOf(cloud.getPort()), "port", writer);
        } catch (XMLStreamException xmlStreamException) {
            xmlStreamException.printStackTrace();
        }
    }

    @Override
    public void writeCategory(Category category)  {
        if (category.getValue() != null && category.getValue().length() > 0) {
            try {
                writer.writeStartElement("category");
                safeWriteAttribute(category.getDomain(), "domain", writer);
                writer.writeCharacters(category.getValue());
                writer.writeEndElement();
            } catch (XMLStreamException xmlStreamException) {
                xmlStreamException.printStackTrace();
            }
        }
    }

    /**
     * @param theChannel the channel that shall be written
     * @param writer the writer instance
     * @throws XMLStreamException if writing goes wrong
     */
    void writeChannel(Channel theChannel, XMLStreamWriter writer) throws XMLStreamException {

        writer.writeStartElement("channel");
        // write mandatorys
        writeElementWithCDataIfNotNull(theChannel.getDescription(), "description", writer);
        writeElementWithCDataIfNotNull(theChannel.getLink(), "link", writer);
        writeElementWithCDataIfNotNull(theChannel.getTitle(), "title", writer);
        // write optionals
        writeElementWithCDataIfNotNull(theChannel.getLanguage(), "language", writer);
        writeElementWithCDataIfNotNull(theChannel.getCopyright(), "copyright", writer);
        writeElementWithCDataIfNotNull(theChannel.getManagingEditor(), "managingEditor", writer);
        writeElementWithCDataIfNotNull(theChannel.getPubDate(), "pubDate", writer);
        writeElementWithCDataIfNotNull(theChannel.getWebMaster(), "webMaster", writer);
        writeElementWithCDataIfNotNull(theChannel.getLastBuildDate(), "lastBuildDate", writer);
        writeElementWithCDataIfNotNull(theChannel.getGenerator(), "generator", writer);
        writeElementWithCDataIfNotNull(theChannel.getDocs(), "docs", writer);
        writeElementWithCDataIfNotNull(theChannel.getTtl(), "ttl", writer);
        writeElementWithCDataIfNotNull(theChannel.getRating(), "rating", writer);
        if (theChannel.getImage() != null) {
            writeImage(theChannel.getImage());
        }
        // write lists
        List<Cloud> clouds = theChannel.getCloud();
        if (clouds != null && !clouds.isEmpty()) {
            for (Cloud cloud : clouds) {
                writeCloud(cloud);
            }
        }
        List<Category> categories = theChannel.getCategory();
        if (categories != null && !categories.isEmpty()) {
            for (Category category : categories) {
                writeCategory(category);
            }
        }
        List<String> skipHours = theChannel.getSkipHours();
        if (skipHours != null && !skipHours.isEmpty()) {
            writer.writeStartElement("skipHours");
            for (String skipHour : skipHours) {
                writeElementWithCDataIfNotNull(skipHour, "hour", writer);
            }
            writer.writeEndElement();
        }
        List<String> skipDays = theChannel.getSkipDays();
        if (skipDays != null && !skipDays.isEmpty()) {
            writer.writeStartElement("skipDays");
            for (String skipDay : skipDays) {
                writeElementWithCDataIfNotNull(skipDay, "day", writer);
            }
            writer.writeEndElement();
        }
        List<Item> items = theChannel.getItems();
        if (items != null && !items.isEmpty()) {
            for (Item item : items) {
                writeItem(item);
            }
        }
        // close the channel element
        writer.writeEndElement();
    }

    /***
     * @param theImage the image that shall be written
     * @throws XMLStreamException if something goes wrong
     */
    @Override
    public void writeImage(Image theImage) {
        try {
            writer.writeStartElement("image");
            // write mandatorys
            writeElementWithCDataIfNotNull(theImage.getUrl(), "url", writer);
            writeElementWithCDataIfNotNull(theImage.getTitle(), "title", writer);
            writeElementWithCDataIfNotNull(theImage.getLink(), "link", writer);
            // write optionals
            writeElementWithCDataIfNotNull(theImage.getDescription(), "description", writer);
            writeElementWithCDataIfNotNull(String.valueOf(theImage.getWidth()), "width", writer);
            writeElementWithCDataIfNotNull(String.valueOf(theImage.getHeight()), "height", writer);
            writer.writeEndElement();
        } catch (XMLStreamException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String writeFeedToString(Feed feed) {
        if (feedIsWritable(feed)) {
            Channel theChannel = feed.getChannel();
            try {
                // create writers
                stringWriter = new StringWriter();
                writer = outputFactory.createXMLStreamWriter(stringWriter);
                // start doc
                writer.writeStartDocument();
                writer.writeStartElement("rss");
                writer.writeAttribute("version", "2.0");
                // write channel
                writeChannel(theChannel, writer);
                // close the rss element
                writer.writeEndElement();
                // close the doc
                writer.writeEndDocument();
                // close the writers
                writer.flush();
                writer.close();
                stringWriter.flush();
                stringWriter.close();
                return stringWriter.toString();
            } catch (XMLStreamException e) {
                LOG.log(Level.SEVERE, "EXCEPTION CREATING THE STREAMWRITER", e);
            } catch (IOException e) {
                LOG.log(Level.SEVERE, "SOMETHING WENT WRONG WITH THE WRITER", e);
            }
        }
        throw new RuntimeException("COULD NOT WRITE THAT FEED; ABORTING PROCESS");
    }
}
