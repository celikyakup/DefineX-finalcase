package patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.service.concretes;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;


@Service
public class JwtServiceImpl{
    @Value("${security.jwt.secret}")
    private String SECRET_KEY;
    private Set<String> blackList=new HashSet<>();

    public String findUserName(String token){
        return exportToken(token, Claims::getSubject);
    }

    private <T> T exportToken(String token, Function<Claims,T> claimsTFunction){
        final Claims claims= Jwts.parserBuilder().setSigningKey(getKey()).build().parseClaimsJws(token).getBody();
        return claimsTFunction.apply(claims);
    }

    private Key getKey(){
        byte [] key= Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(key);
    }

    public boolean tokenControl(String jwt, UserDetails userDetails){
        final String username=findUserName(jwt);
        return (username.equals(userDetails.getUsername()) && !exportToken(jwt,Claims::getExpiration).before(new Date()));
    }

    public String generateToken(UserDetails userDetails){
        return Jwts.builder().setClaims(new HashMap<>()).setSubject(userDetails.getUsername()).setIssuedAt(new Date(System.currentTimeMillis())).setExpiration(new Date(System.currentTimeMillis()*1000*60*24))
                .signWith(getKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public void addToBlackList(String token){
        blackList.add(token);
    }

    public boolean isBlackListed(String token){
        return blackList.contains(token);
    }
}
