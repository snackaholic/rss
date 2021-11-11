package de.snackaholic.rss.api;

import de.snackaholic.rss.model.*;

import javax.xml.stream.XMLStreamException;


public interface IRssFeedWriter {

    /**
     * Writes the feed to a string
     * @param feed the feed that should be written as a string
     * @return the string representation of the feed
     */
    String writeFeedToString(Feed feed);

    void writeImage(Image image);

    void writeItem(Item item);

    void writeEnclosure(Enclosure enclosure);

    void writeCloud(Cloud cloud);

    void writeCategory(Category category) throws XMLStreamException;
}
