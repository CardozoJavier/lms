package org.lms.utils;

import java.util.UUID;

public class UUIDUtils {

    /**
     * Validates if the provided string is a valid UUID.
     *
     * @param uuid the string to be validated
     * @return true if the string is a valid UUID, false otherwise
     */
    public static boolean isValid(String uuid) {
        try {
            UUID.fromString(uuid);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
