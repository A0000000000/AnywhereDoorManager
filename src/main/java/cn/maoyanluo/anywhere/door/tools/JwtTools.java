package cn.maoyanluo.anywhere.door.tools;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtTools {

    private static final String TOKEN_SECRET = "maoyanluo";
    private static final long TOKEN_EXPIRE = 24 * 60 * 60;
    private static final long FLUSH_EXPIRE = 7 * 24 * 60 * 60;


    public String generateToken(String username) {
        return generateTokenInner(username, TOKEN_EXPIRE);
    }

    public String generateFlushToken(String username) {
        return generateTokenInner(username, FLUSH_EXPIRE);
    }

    private String generateTokenInner(String username, long expire) {
        Date expireDate = new Date(System.currentTimeMillis() + expire * 1000);
        Map<String, Object> map = new HashMap<>();
        map.put("alg", "HS256");
        map.put("typ", "JWT");
        return JWT.create()
                .withHeader(map)
                .withClaim("username", username)
                .withExpiresAt(expireDate)
                .withIssuedAt(new Date())
                .sign(Algorithm.HMAC256(TOKEN_SECRET));
    }

    public Pair<String, Boolean> parseToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(TOKEN_SECRET);
            JWTVerifier verifier = JWT.require(algorithm).build();
            Date expiresAt = verifier.verify(token).getExpiresAt();
            String username = verifier.verify(token).getClaims().get("username").asString();
            return new Pair<>(username, expiresAt.after(new Date()));
        } catch (Exception e) {
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
