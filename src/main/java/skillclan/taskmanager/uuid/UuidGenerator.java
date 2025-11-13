package skillclan.taskmanager.uuid;

import java.util.UUID;

public class UuidGenerator {
    public static String generateRandomUuidString() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }
}
