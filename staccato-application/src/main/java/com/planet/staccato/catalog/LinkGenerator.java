package com.planet.staccato.catalog;

import com.planet.staccato.collection.CollectionMetadata;
import com.planet.staccato.config.LinksConfigProps;
import com.planet.staccato.config.StaccatoMediaType;
import com.planet.staccato.model.Link;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;


/**
 * Utility service to generate catalog/collection links.
 *
 * @author joshfix
 * Created on 10/24/18
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class LinkGenerator {

    private final LinksConfigProps linksConfigProps;
    private static final Link ROOT = new Link()
            .href(appendLinkPath(LinksConfigProps.LINK_PREFIX, "stac"))
            .type(MediaType.APPLICATION_JSON_VALUE)
            .rel("root");

    /**
     * Creates links to subcatalogs based on property fieldsExtension that are eligible for being subcataloged.
     *
     * @param request The server request object
     * @param collection The collection metadata
     * @param remainingProperties A list of property fieldsExtension that are eligible for being subcataloged and have not yet
     *                            been used traversed by the client.
     */
    public void generatePropertyFieldLinks(ServerRequest request, CollectionMetadata collection, List<PropertyField> remainingProperties) {
        collection.getLinks().clear();

        String self = getSelfString(request);


        for (PropertyField property : remainingProperties) {
            collection.getLinks().add(
                    new Link()
                            .href(appendLinkPath(self, property.getJsonName()))
                            .type(MediaType.APPLICATION_JSON_VALUE)
                            .rel("child"));
        }

        collection.getLinks().add(ROOT);
        collection.getLinks().add(new Link()
                .href(self)
                .type(MediaType.APPLICATION_JSON_VALUE)
                .rel("self"));
        collection.getLinks().add(new Link()
                .href(appendLinkPath(self, "items"))
                .type(MediaType.APPLICATION_JSON_VALUE)
                .rel("items"));
        collection.getLinks().add(new Link()
                .href(self.substring(0, self.lastIndexOf("/")))
                .type(MediaType.APPLICATION_JSON_VALUE)
                .rel("parent"));
    }

    /**
     * Creates links to subcatalogs based on unique values matched in the database for the selected properties field.
     *
     * @param request The server request object
     * @param collection The collection metadata
     * @param values A list of unique values in the database for the selected subcataloged field
     */
    public void generatePropertyValueLinks(ServerRequest request, CollectionMetadata collection, List<String> values) {
        values.forEach(value -> collection.getLinks().add(
                new Link()
                        .href(appendLinkPath(LinksConfigProps.LINK_PREFIX + request.path(), value))
                        .type(MediaType.APPLICATION_JSON_VALUE)
                        .rel("child")));

        String self = getSelfString(request);
        collection.getLinks().add(ROOT);
        collection.getLinks().add(new Link()
                .href(self)
                .type(MediaType.APPLICATION_JSON_VALUE)
                .rel("self"));
        collection.getLinks().add(new Link()
                .href(appendLinkPath(self, "items"))
                .type(StaccatoMediaType.APPLICATION_GEO_JSON_VALUE)
                .rel("items"));
        collection.getLinks().add(new Link()
                .href(self.substring(0, self.lastIndexOf("/")))
                .type(MediaType.APPLICATION_JSON_VALUE)
                .rel("parent"));
    }

    /**
     * Determines the host portion of the URL used by the client to make the request.  Used to build links that match
     * the client's request.
     * TODO: Should not always assume https over http.  Need a way to determine the protocol.  Actually, should replace
     * this and use the values in {@link LinksConfigProps} instead.
     *
     * @param request The server request object
     * @return The protocol/host/port portion of the URL
     */
    public String getSelfString(ServerRequest request) {
        // request.uri() shceme seems to always be "http"
        String self = request.uri().toASCIIString();
        if (request.uri().getScheme().equalsIgnoreCase("http") &&
                linksConfigProps.getSelf().getScheme().equalsIgnoreCase("https")) {
            self = self.replace("http:", "https:");
        }
        return self;
    }

    /**
     * Builds a link to a specific item in a collection.
     *
     * @param collectionId The id of the collection
     * @param itemId The id of the item
     * @return A link to the item in the collection
     */
    public Link buildItemLink(String collectionId, String itemId) {
        return new Link()
                .href(appendLinkPath(LinksConfigProps.LINK_PREFIX, "collections", collectionId, "items", itemId))
                .type(StaccatoMediaType.APPLICATION_GEO_JSON_VALUE)
                .rel("item");
    }

    /**
     * Appends path to url while preserving url context
     *
     * @param url The original url
     * @param subPaths The sub paths to be appended at the end of the url,
     *                 sub paths should not contain leading or trailing slashes
     * @return The url with appended sub path. If arguments are malformed, the original url is returned.
     */
    public static String appendLinkPath(String url, String ...subPaths) {
        String result = url;
        try {
            URL originalLink = new URL(url);
            String newPath = originalLink.getPath();

            for (String subPath: subPaths) {
                String separator = newPath.endsWith("/") ? "" : "/";
                newPath += separator + subPath;
            }
            URI newLink = new URI(
                    originalLink.getProtocol(),
                    originalLink.getAuthority(),
                    newPath,
                    originalLink.getQuery(),
                    null
            );
            result = newLink.toString();
        } catch (MalformedURLException e) {
            log.warn("LinkGenerator encounters malformed url " + e.toString());
            return url;
        } catch(URISyntaxException e) {
            log.warn("LinkGenerator encounteres uri syntax exception " + e.toString());
        }
        return result;
    }

}