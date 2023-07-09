package ru.endienasg.foxworld.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.endienasg.foxworld.models.User;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.function.Function;
@Service
@Slf4j
public class JwtTokenService {
    @Value("${foxworld.jwt.secret}")
    private String secret = Base64.encodeBase64String("senkosansenkosansenkosansenkosansenkosan".getBytes());
    public boolean validateToken(String token){
        return !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token){
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public String generateToken(User user){
        Date now = new Date();
        Date exp = Date.from(LocalDateTime.now().plusMinutes(30).atZone(ZoneId.systemDefault()).toInstant());
        log.error(secret);
        String token = Jwts.builder().setExpiration(exp).setIssuedAt(now).setSubject(user.getUsername()).signWith(SignatureAlgorithm.HS256, secret).compact();
        return token;
    }

    public void revokeToken(@NonNull String token){

    }
    public String updateToken(@NonNull String refreshToken){
        if(refreshToken != null){

        }
        return "";
    }

    public <T> T extractClaim(String token, Function<Claims,T> claimsTFunction){
        final Claims claims = extractAllClaims(token);
        return claimsTFunction.apply(claims);
    }
    private Claims extractAllClaims(String token){
        return Jwts.parserBuilder().setSigningKey(secret).build().parseClaimsJws(token).getBody();
    }
}
