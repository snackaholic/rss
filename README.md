<div id="top"></div>

[![Contributors][contributors-shield]][contributors-url]
[![Forks][forks-shield]][forks-url]
[![Stargazers][stars-shield]][stars-url]
[![Issues][issues-shield]][issues-url]
[![MIT License][license-shield]][license-url]


<details>
  <summary>Table of Contents</summary>
  <ol>
    <li>
      <a href="#about-the-project">About The Project</a>
    </li>
    <li>
      <a href="#getting-started">Getting Started</a>
      <ul>
        <li><a href="#prerequisites">Prerequisites</a></li>
        <li><a href="#installation">Installation</a></li>
      </ul>
    </li>
    <li><a href="#usage">Usage</a></li>
    <li><a href="#roadmap">Roadmap</a></li>
    <li><a href="#contributing">Contributing</a></li>
    <li><a href="#license">License</a></li>
    <li><a href="#contact">Contact</a></li>
    <li><a href="#sources">Sources</a></li>
  </ol>
</details>


## About The Project

Java RSS 2.0 reader & writer


## Getting Started


### Prerequisites

Make sure you have java and maven installed.

### Installation


1. Clone the repo
   ```sh
   git clone https://github.com/snackaholic/rss
   ```
2. Install dependencies
   ```sh
   mvn clean install
   ```

<p align="right">(<a href="#top">back to top</a>)</p>



## Usage
Get feed by URL
```java
IFeedByURLProvider provider = new IFeedByURLProviderImpl();
Feed feed = provider.provideFeedByURL(new URL("https://raw.githubusercontent.com/snackaholic/rss/main/src/test/resources/testfeed.xml"));
assertEquals("The Rough Cut features in-depth interviews with the top film and television post production professionals working in the industry today.  Hosted by @MattFeury of Avid Technology.", feed.getChannel().getDescription());
assertEquals(118, feed.getChannel().getItems().size());
```


Generate a feed:

```java
boolean useCDATA = true;
IRssFeedWriter writer = new RssFeedWriter(new StringWriter(), useCDATA);
Feed testFeed = new Feed();
Channel testChannel = new Channel();
testChannel.setDescription("channel description");
testFeed.setChannel(testChannel);
String output = writer.writeFeedToString(testFeed);
assertEquals("<?xml version=\"1.0\" ?><rss version=\"2.0\"><channel><description><![CDATA[channel description]]></description></channel></rss>", output);
```


## Roadmap


- [ ] Provide functionality as webservice
- [ ] Add documentation


See the [open issues](https://github.com/snackaholic/rss/issues) for a full list of proposed features (and known issues).

<p align="right">(<a href="#top">back to top</a>)</p>



## Contributing

Any contributions you make are **greatly appreciated**.

If you have a suggestion , please fork the repo and create a pull request or simply open an issue.

<p align="right">(<a href="#top">back to top</a>)</p>


## License

Distributed under the MIT License. See `LICENSE.txt` for more information.

<p align="right">(<a href="#top">back to top</a>)</p>


## Contact

Snackaholic - [@Snackaholic95](https://twitter.com/Snackaholic95)

<p align="right">(<a href="#top">back to top</a>)</p>



## Sources

* [RSS 2.0 specification](https://validator.w3.org/feed/docs/rss2.html)

<p align="right">(<a href="#top">back to top</a>)</p>


[contributors-shield]: https://img.shields.io/github/contributors/snackaholic/rss.svg?style=for-the-badge
[contributors-url]: https://github.com/snackaholic/rss/graphs/contributors
[forks-shield]: https://img.shields.io/github/forks/snackaholic/rss.svg?style=for-the-badge
[forks-url]: https://github.com/snackaholic/rss/network/members
[stars-shield]: https://img.shields.io/github/stars/snackaholic/rss?style=for-the-badge
[stars-url]: https://github.com/snackaholic/rss/stargazers
[issues-shield]: https://img.shields.io/github/issues/snackaholic/rss.svg?style=for-the-badge
[issues-url]: https://github.com/snackaholic/rss/issues
[license-shield]: https://img.shields.io/github/license/snackaholic/rss.svg?style=for-the-badge
[license-url]: https://github.com/snackaholic/rss/blob/master/LICENSE.txt
