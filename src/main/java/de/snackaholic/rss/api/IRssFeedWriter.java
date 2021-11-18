package de.snackaholic.rss.api;

import de.snackaholic.rss.model.*;

/**
 * The desired functionality of a rss feed writer
 */
public interface IRssFeedWriter {

    /**
     * Writes the feed to a string
     * @param feed the feed that should be written as a string
     * @return the string representation of the feed
     */
    String writeFeedToString(Feed feed);

    /**
     * Writes the image
     * @param image the image that shall be written
     */
    void writeImage(Image image);

    /**
     * Writes the item
     * @param item the item that shall be written
     */
    void writeItem(Item item);

    /**
     * Writes the enclosure
     * @param enclosure the enclosure that shall be written
     */
    void writeEnclosure(Enclosure enclosure);

    /**
     * Writes the cloud
     * @param cloud the cloud that shall be written
     */
    void writeCloud(Cloud cloud);

    /**
     * Writes the category
     * @param category the category that shall be written
     */
    void writeCategory(Category category);

    /**
     * Writes the channel
     * @param channel the channel that shall be written
     */
    void writeChannel(Channel channel);
}
