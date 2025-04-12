package dadn_SmartFarm.utils;

import java.util.Base64;

public class RoomKeyUtil {
    private static final String SALT = "SmartFarmSecret";
    private static final String SEPARATOR = ":";

    public static String encodeRoomKey(long roomId, String name) {
        String raw = roomId + SEPARATOR + name + SEPARATOR + SALT;
        return Base64.getEncoder().encodeToString(raw.getBytes());
    }

    public static DecodedRoomKey decodeRoomKey(String roomKey) {
        try {
            String decoded = new String(Base64.getDecoder().decode(roomKey));
            String[] parts = decoded.split(SEPARATOR);
            if (parts.length != 3 || !parts[2].equals(SALT)) {
                throw new IllegalArgumentException("Invalid roomKey format or salt");
            }
            long roomId = Long.parseLong(parts[0]);
            String name = parts[1];
            return new DecodedRoomKey(roomId, name);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid roomKey", e);
        }
    }

    public static class DecodedRoomKey {
        public long roomId;
        public String name;

        public DecodedRoomKey(long roomId, String name) {
            this.roomId = roomId;
            this.name = name;
        }
    }
}

