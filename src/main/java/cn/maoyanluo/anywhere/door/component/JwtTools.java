package cn.maoyanluo.anywhere.door.component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtTools {

    private static final String TOKEN_SECRET = "maoyanluo";
    private static final long TOKEN_EXPIRE = 24 * 60 * 60;
    private static final long FLUSH_EXPIRE = 7 * 24 * 60 * 60;

    public static final String TAG = JwtTools.class.getSimpleName();

    private final LogTools logTools;

    @Autowired
    public JwtTools(LogTools logTools) {
        this.logTools = logTools;
    }

    public String generateToken(String username) {
        return generateTokenInner(username, TOKEN_EXPIRE);
    }

    public String generateFlushToken(String username) {
        return generateTokenInner(username, FLUSH_EXPIRE);
    }

    private String generateTokenInner(String username, long expire) {
        Map<String, Object> map = new HashMap<>();
        map.put("alg", "HS256");
        map.put("typ", "JWT");
        return JWT.create()
                .withHeader(map)
                .withClaim("username", username)
                .withClaim("expire_time", System.currentTimeMillis() + expire * 1000)
                .withIssuedAt(new Date())
                .sign(Algorithm.HMAC256(TOKEN_SECRET));
    }

    public Pair<String, Boolean> parseToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(TOKEN_SECRET);
            JWTVerifier verifier = JWT.require(algorithm).build();
            long expireTime = verifier.verify(token).getClaims().get("expire_time").asLong();
            String username = verifier.verify(token).getClaims().get("username").asString();
            return new Pair<>(username, expireTime > System.currentTimeMillis());
        } catch (Exception e) {
            logTools.w(TAG, "parseToken failed, token: " + token + ", error: " + e.getMessage());
            return null;
        }
    }


    @Data
    @AllArgsConstructor
    public static class Pair<T, U> {
        private T first;
        private U second;
    }

}
