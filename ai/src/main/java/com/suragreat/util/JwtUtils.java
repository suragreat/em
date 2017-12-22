package com.suragreat.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.lang3.StringUtils;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;

public final class JwtUtils {
    private static final byte[] SECRET = {72, 40, 21, 65, 48, 100};
    public static final SignatureAlgorithm SIGNATURE_ALGORITHM = SignatureAlgorithm.HS512;

    public static String generateToken(Map<String, Object> claims) {
        return

                Jwts.builder().setClaims(claims).setExpiration(generateExpirationDate()).signWith(SIGNATURE_ALGORITHM, SECRET).compact();
    }

    private static Date generateExpirationDate() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, 30);
        return cal.getTime();
    }

    public static Claims getClaimsFromToken(String token) {
        if (StringUtils.isBlank(token)) {
            return null;
        }
        Claims claims = null;
        try {
            claims = (Claims) Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token).getBody();
        } catch (Exception e) {
        }
        return claims;
    }
}
