package dadn_SmartHome.utils;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class FeedEncoder {
    public static String encodeFeeds(Map<String, Long> feeds) {
        StringBuilder sb = new StringBuilder();
        feeds.forEach((key, value) -> sb.append(key).append(":").append(value).append(";"));

        String rawString = sb.toString();
        return Base64.getEncoder().encodeToString(rawString.getBytes());
    }

    public static Map<String, Long> decodeFeeds(String encodedString) {
        String decoded = new String(Base64.getDecoder().decode(encodedString));
        Map<String, Long> feeds = new HashMap<>();

        String[] pairs = decoded.split(";");
        for (String pair : pairs) {
            if (!pair.isEmpty()) {
                String[] parts = pair.split(":");
                feeds.put(parts[0], Long.parseLong(parts[1]));
            }
        }
        return feeds;
    }

    public static boolean isValidEncodedString(String encodedString) {
        try {
            String decoded = new String(Base64.getDecoder().decode(encodedString));

            if (!decoded.matches("([a-zA-Z0-9_-]+:\\d+;)*")) {
                return false;
            }
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
