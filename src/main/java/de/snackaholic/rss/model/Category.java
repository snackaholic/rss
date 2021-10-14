package de.snackaholic.rss.model;

/**
 * Documentation source from https://validator.w3.org/feed/docs/rss2.html
 * <category> sub-element of <item>
 * <category> is an optional sub-element of <item>.
 *
 * It has one optional attribute, domain, a string that identifies a categorization taxonomy.
 *
 * The value of the element is a forward-slash-separated string that identifies a hierarchic location in the indicated taxonomy. Processors may establish conventions for the interpretation of categories. Two examples are provided below:
 *
 * <category>Grateful Dead</category>
 *
 * <category domain="http://www.fool.com/cusips">MSFT</category>
 *
 * You may include as many category elements as you need to, for different domains, and to have an item cross-referenced in different parts of the same domain.
 */
public class Category {

    // the optional domain
    private String domain;
    // the node text value
    private String value;

    public Category() {
        super();
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
