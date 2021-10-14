package de.snackaholic.rss.model;

/**
 * Documentation source from https://validator.w3.org/feed/docs/rss2.html
 * <cloud> sub-element of <channel>
 * <cloud> is an optional sub-element of <channel>.
 *
 * It specifies a web service that supports the rssCloud interface which can be implemented in HTTP-POST, XML-RPC or SOAP 1.1.
 *
 * Its purpose is to allow processes to register with a cloud to be notified of updates to the channel, implementing a lightweight publish-subscribe protocol for RSS feeds.
 *
 * <cloud domain="radio.xmlstoragesystem.com" port="80" path="/RPC2" registerProcedure="xmlStorageSystem.rssPleaseNotify" protocol="xml-rpc" />
 *
 * In this example, to request notification on the channel it appears in, you would send an XML-RPC message to radio.xmlstoragesystem.com on port 80, with a path of /RPC2. The procedure to call is xmlStorageSystem.rssPleaseNotify.
 */
public class Cloud {

    private String domain;
    private String path;
    private String protocol;
    private String registerProcedure;
    private int port;

    public Cloud() {
        super();
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getRegisterProcedure() {
        return registerProcedure;
    }

    public void setRegisterProcedure(String registerProcedure) {
        this.registerProcedure = registerProcedure;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
