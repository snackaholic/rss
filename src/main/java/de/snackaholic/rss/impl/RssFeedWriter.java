package de.snackaholic.rss.impl;

import de.snackaholic.rss.api.IFeedToStringWriter;
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


public class RssFeedWriter implements IFeedToStringWriter {

    private static final Logger LOG = Logger.getLogger(RssFeedWriter.class.getName());

    private final XMLOutputFactory outputFactory = XMLOutputFactory.newFactory();

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

    void writeItem(Item item, XMLStreamWriter writer) throws XMLStreamException {
        writer.writeStartElement("item");
        // write item elements
        writeElementWithCDataIfNotNull(item.getDescription(), "description", writer);
        writeElementWithCDataIfNotNull(item.getPubDate(), "pubDate", writer);
        writeElementWithCDataIfNotNull(item.getTitle(), "title", writer);
        writeElementWithCDataIfNotNull(item.getAuthor(), "author", writer);
        // TODO fix me writeElementWithCDataIfNotNull(item.getEnclosure(), "enclosure", writer);
        writeElementWithCDataIfNotNull(item.getGuid(), "guid", writer);
        // close the item
        writer.writeEndElement();
    }

    void safeWriteAttribute(String value, String name, XMLStreamWriter writer) throws XMLStreamException {
        if (value != null && value.length() > 0) {
            writer.writeAttribute(name, value);
        }
    }

    void writeCloud(Cloud cloud, XMLStreamWriter writer) throws XMLStreamException {
        writer.writeEmptyElement("cloud");
        safeWriteAttribute(cloud.getDomain(), "domain", writer);
        safeWriteAttribute(cloud.getPath(), "path", writer);
        safeWriteAttribute(cloud.getProtocol(), "protocol", writer);
        safeWriteAttribute(cloud.getRegisterProcedure(), "registerProcedure", writer);
        safeWriteAttribute(String.valueOf(cloud.getPort()), "port", writer);
        writer.writeEndElement();
    }

    void writeCategory(Category category, XMLStreamWriter writer) throws XMLStreamException {
        if (category.getValue() != null && category.getValue().length() > 0) {
            writer.writeStartElement("category");
            safeWriteAttribute(category.getDomain(), "domain", writer);
            writer.writeCharacters(category.getValue());
            writer.writeEndElement();
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
            writeImage(theChannel.getImage(), writer);
        }
        // write lists
        List<Item> items = theChannel.getItems();
        if (items != null && !items.isEmpty()) {
            for (Item item : items) {
                writeItem(item, writer);
            }
        }
        List<Cloud> clouds = theChannel.getCloud();
        if (clouds != null && !clouds.isEmpty()) {
            for (Cloud cloud : clouds) {
                writeCloud(cloud, writer);
            }
        }
        List<Category> categories = theChannel.getCategory();
        if (categories != null && !categories.isEmpty()) {
            for (Category category : categories) {
                writeCategory(category, writer);
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
        // close the channel element
        writer.writeEndElement();
    }

    /***
     * @param theImage the image that shall be written
     * @param writer the writer instance
     * @throws XMLStreamException if something goes wrong
     */
    void writeImage(Image theImage, XMLStreamWriter writer) throws XMLStreamException {
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
    }

    @Override
    public String writeFeedToString(Feed feed) {
        if (feedIsWritable(feed)) {
            Channel theChannel = feed.getChannel();
            try {
                Writer stringWriter = new StringWriter();
                XMLStreamWriter writer = outputFactory.createXMLStreamWriter(stringWriter);
                writer.writeStartDocument();
                writer.writeStartElement("rss");
                writer.writeAttribute("version", "2.0");
                // write channel
                writeChannel(theChannel, writer);
                // close the rss element
                writer.writeEndElement();
                // close the doc
                writer.writeEndDocument();
                // close the writer
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
