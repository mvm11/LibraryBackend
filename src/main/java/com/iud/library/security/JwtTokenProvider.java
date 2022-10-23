package com.iud.library.security;


import com.iud.library.common.exception.LibraryException;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtTokenProvider {

    @Value("${app.jwt-secret}")
    private String jwtSecret;

    @Value("${app.jwt-expiration-milliseconds}")
    private int jwtExpirationInMs;

    public String generateToken(Authentication authentication){
        String username = authentication.getName();
        Date currentDate = new Date();
        Date expirationDate = new Date(currentDate.getTime() + jwtExpirationInMs);

        String token = Jwts.builder().setSubject(username).setIssuedAt(new Date()).setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS512, jwtSecret).compact();

        return token;

    }

    public String getJwtOfUsername(String token){
        Claims claims = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody();
        return claims.getSubject();
    }

    public Boolean validateToken(String token){
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);
            return true;
        }catch (SignatureException exception){
            throw new LibraryException(HttpStatus.BAD_REQUEST, "Signature JWT isn't valid");
        }
        catch (MalformedJwtException exception){
            throw new LibraryException(HttpStatus.BAD_REQUEST, "JWT Token isn't valid");
        }
        catch (ExpiredJwtException exception){
            throw new LibraryException(HttpStatus.BAD_REQUEST, "JWT Token has expired");
        }
        catch (UnsupportedJwtException exception){
            throw new LibraryException(HttpStatus.BAD_REQUEST, "JWT Token isn't compatible");
        }
        catch (IllegalArgumentException exception){
            throw new LibraryException(HttpStatus.BAD_REQUEST, "JWT Claims is empty");
        }
    }


}
