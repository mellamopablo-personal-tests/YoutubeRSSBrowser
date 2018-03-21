package io.github.mellamopablo.youtuberssbrowser.support;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import io.github.mellamopablo.youtuberssbrowser.model.Video;

public abstract class RSSParser {
    public static List<Video> parse(String raw) {
        if (raw == null) {
            return Collections.emptyList();
        }

        try {
            return nodeListToList(
                    DocumentBuilderFactory
                            .newInstance()
                            .newDocumentBuilder()
                            .parse(new ByteArrayInputStream(raw.getBytes()))
                            .getElementsByTagName("entry")
            )
                    .parallelStream()
                    .map(entry -> {
                        List<Node> children = nodeListToList(entry.getChildNodes());
                        List<Node> media = getSubNode(children, "media:group")
                                .map(Node::getChildNodes)
                                .map(RSSParser::nodeListToList)
                                .orElse(Collections.emptyList());

                        String videoId = getSubNodeContent(children, "yt:videoId");
                        String videoTitle = getSubNodeContent(media, "media:title");
                        String videoDescription = getSubNodeContent(media, "media:description");
                        Bitmap videoThumbnail = getSubNode(media, "media:thumbnail")
                                .map(Node::getAttributes)
                                .flatMap(it -> Optional.ofNullable(it.getNamedItem("url")))
                                .map(Node::getNodeValue)
                                .map(RSSParser::getBitmap)
                                .orElse(null);

                        return new Video(
                                videoId,
                                videoTitle,
                                videoDescription,
                                videoThumbnail
                        );
                    })
                    .collect(Collectors.toList());
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    private static List<Node> nodeListToList(NodeList nodeList) {
        List<Node> list = new ArrayList<>();
        for (int i = 0; i < nodeList.getLength(); i++) {
            list.add(nodeList.item(i));
        }
        return list;
    }

    private static Optional<Node> getSubNode(List<Node> nodes, String name) {
        return nodes
                .stream()
                .filter(it -> it.getNodeName().equals(name))
                .findFirst();
    }

    private static String getSubNodeContent(List<Node> nodes, String name) {
        return getSubNode(nodes, name).map(Node::getTextContent).orElse("");
    }

    private static Bitmap getBitmap(String url)  {
        try {
            return BitmapFactory.decodeStream(new URL(url).openStream());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
