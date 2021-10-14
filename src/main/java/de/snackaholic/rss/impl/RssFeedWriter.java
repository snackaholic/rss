package de.snackaholic.rss.impl;

import de.snackaholic.rss.api.IFeedToStringWriter;
import de.snackaholic.rss.model.Channel;
import de.snackaholic.rss.model.Feed;
import de.snackaholic.rss.model.Item;

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
            if (items != null && items.size() > 0) {
                return true;
            }
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

    void writeChannel(Channel theChannel, XMLStreamWriter writer) throws XMLStreamException {
        writeElementWithCDataIfNotNull(theChannel.getDescription(), "description", writer);
        writeElementWithCDataIfNotNull(theChannel.getCopyright(), "copyright", writer);
        writeElementWithCDataIfNotNull(theChannel.getLanguage(), "language", writer);
        writeElementWithCDataIfNotNull(theChannel.getPubDate(), "pubDate", writer);
        writeElementWithCDataIfNotNull(theChannel.getLink(), "link", writer);
        writeElementWithCDataIfNotNull(theChannel.getTitle(), "title", writer);
        writeElementWithCDataIfNotNull(theChannel.getManagingEditor(), "managingEditor", writer);
        writeElementWithCDataIfNotNull(theChannel.getWebMaster(), "webMaster", writer);
        writeElementWithCDataIfNotNull(theChannel.getLastBuildDate(), "lastBuildDate", writer);
        writeElementWithCDataIfNotNull(theChannel.getGenerator(), "generator", writer);
        writeElementWithCDataIfNotNull(theChannel.getDocs(), "docs", writer);
        writeElementWithCDataIfNotNull(theChannel.getTtl(), "ttl", writer);
        writeElementWithCDataIfNotNull(theChannel.getRating(), "rating", writer);
        // TODO fix me writeElementWithCDataIfNotNull(theChannel.getImage(), "image", writer);
        // TODO write lists?!
    }

    @Override
    public String writeFeedToString(Feed feed) {
        if (feedIsWritable(feed)) {
            Channel theChannel = feed.getChannel();
            List<Item> items = theChannel.getItems();
            try {
                Writer stringWriter = new StringWriter();
                XMLStreamWriter writer = outputFactory.createXMLStreamWriter(stringWriter);
                writer.writeStartDocument();
                writer.writeStartElement("rss");
                writer.writeAttribute("version", "2.0");
                writer.writeStartElement("channel");
                // write channel elements
                writeChannel(theChannel, writer);
                for (int i = 0; i < items.size(); i++) {
                    writeItem(items.get(i), writer);
                }
                // close the channel element
                writer.writeEndElement();
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
