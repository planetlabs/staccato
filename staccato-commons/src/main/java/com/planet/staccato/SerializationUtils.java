package com.planet.staccato;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.planet.staccato.exception.SerializationException;
import com.planet.staccato.model.Item;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

/**
 * Utility class for serializing and deserializing {@link Item items}.
 * @author joshfix
 * Created on 6/22/18
 */
@Slf4j
public class SerializationUtils {

    /**
     * Serializes an {@link Item Item} to byte array.
     *
     * @param item The item to serialize
     * @param mapper The Jackson {@link ObjectMapper ObjectMapper}
     * @return The serialized byte array
     */
    public static byte[] serializeItem(Item item, ObjectMapper mapper) {
        try {
            return mapper.writeValueAsBytes(item);
        } catch (Exception e) {
            log.error("Error serializing item: \n" + item.toString(), e);
            throw new SerializationException("Error processing results.  Please contact an administrator with the details of your api parameters.");
        }
    }

    /**
     * Serializes an {@link Item Item} to string.
     *
     * @param item The item to serialize
     * @param mapper The Jackson {@link ObjectMapper ObjectMapper}
     * @return The serialized string
     */
    public static Optional<String> serializeItemToString(Item item, ObjectMapper mapper) {
        try {
            return Optional.of(mapper.writeValueAsString(item));
        } catch (JsonProcessingException e) {
            log.error("Error serializing Item to JSON string. " + item.toString(), e);
        }
        return Optional.empty();
    }

    /**
     * Deserializes a byte array into an {@link Item Item}.
     *
     * @param bytes The byte array to deserialize
     * @param mapper The Jackson {@link ObjectMapper ObjectMapper}
     * @return The deserialized {@link Item Item}
     */
    public static Item deserializeItem(byte[] bytes, ObjectMapper mapper) {
        try {
            Item item = mapper.readValue(bytes, Item.class);
            return item;
        } catch (Exception e) {
            log.error("Error deserializing hit for item: \n" + new String(bytes), e);
            throw new SerializationException("Error processing results.  Please contact an administrator with the details of your api parameters.");
        }
    }

    /**
     * Deserializes a string into an {@link Item Item}.
     *
     * @param item The string to deserialize
     * @param mapper The Jackson {@link ObjectMapper ObjectMapper}
     * @return The deserialized {@link Item Item}
     */
    public static Item deserializeItemFromString(String item, ObjectMapper mapper) {
        try {
            return mapper.readValue(item, Item.class);
        } catch (Exception e) {
            log.error("Error deserializing hit for item: \n" + item, e);
            throw new SerializationException("Error processing results.  Please contact an administrator with the details of your api parameters.");
        }
    }

}
