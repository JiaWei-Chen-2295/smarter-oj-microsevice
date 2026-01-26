package fun.javierchen.jcojbackendroomservice.utils;

import fun.javierchen.jcojbackendcommon.utils.SpringContextUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.env.Environment;
import org.springframework.util.DigestUtils;

public class AuthTokenUtils {

    private static final String DEFAULT_SECRET_KEY = "your-secret-key-change-in-production";

    public static String generateToken(Long userId, Long roomId, Long timestamp) {
        String raw = userId + ":" + roomId + ":" + timestamp + ":" + getSecretKey();
        return DigestUtils.md5DigestAsHex(raw.getBytes());
    }

    public static boolean verifyToken(Long userId, Long roomId, String token, Long timestamp, int validityMinutes) {
        if (userId == null || roomId == null || token == null || timestamp == null) {
            return false;
        }
        long currentTime = System.currentTimeMillis();
        long validityMillis = validityMinutes * 60 * 1000L;
        if (Math.abs(currentTime - timestamp) > validityMillis) {
            return false;
        }
        String expectedToken = generateToken(userId, roomId, timestamp);
        return expectedToken.equals(token);
    }

    public static boolean verifyToken(Long userId, Long roomId, String token, Long timestamp) {
        return verifyToken(userId, roomId, token, timestamp, 5);
    }

    private static String getSecretKey() {
        try {
            Environment environment = SpringContextUtils.getBean(Environment.class);
            String secret = environment.getProperty("room.auth.secret");
            if (StringUtils.isNotBlank(secret)) {
                return secret;
            }
        } catch (Exception ignored) {
        }
        return DEFAULT_SECRET_KEY;
    }
}

