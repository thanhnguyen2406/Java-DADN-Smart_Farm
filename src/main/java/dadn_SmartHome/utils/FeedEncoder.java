package dadn_SmartHome.utils;

import dadn_SmartHome.model.FeedInfo;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class FeedEncoder {
    public static String encodeFeeds(Map<String, FeedInfo> feeds) {
        StringBuilder sb = new StringBuilder();

        feeds.forEach((feedKey, feedInfo) -> sb.append(feedKey)
                .append(":").append(feedInfo.getFeedId())
                .append(":").append(feedInfo.getThreshold_max())
                .append(":").append(feedInfo.getThreshold_min())
                .append(";"));

        String rawString = sb.toString();
        return Base64.getEncoder().encodeToString(rawString.getBytes());
    }

    public static Map<String, FeedInfo> decodeFeeds(String encodedString) {
        String decoded = new String(Base64.getDecoder().decode(encodedString));
        Map<String, FeedInfo> feeds = new HashMap<>();

        String[] pairs = decoded.split(";");
        for (String pair : pairs) {
            if (!pair.isEmpty()) {
                String[] parts = pair.split(":");
                if (parts.length == 4) {
                    String feedKey = parts[0];
                    Long feedId = Long.parseLong(parts[1]);
                    Double thresholdMax = Double.parseDouble(parts[2]);
                    Double thresholdMin = Double.parseDouble(parts[3]);

                    feeds.put(feedKey, new FeedInfo(feedId, thresholdMax, thresholdMin));
                }
            }
        }
        return feeds;
    }

    public static boolean isValidEncodedString(String encodedString) {
        try {
            String decoded = new String(Base64.getDecoder().decode(encodedString));

            if (!decoded.matches("([a-zA-Z0-9_-]+:\\d+:\\d*\\.?\\d*:\\d*\\.?\\d*;)*")) {
                return false;
            }
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
