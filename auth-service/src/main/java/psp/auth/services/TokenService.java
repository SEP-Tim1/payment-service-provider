package psp.auth.services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import psp.auth.exceptions.UnauthenticatedException;
import psp.auth.model.Role;
import psp.auth.model.User;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Service
public class TokenService {

    @Value("web-shop-back")
    private String APP_NAME;

    @Value("5B43E507D0762852D06EEDD2E30A24F10ABC72AC114D60D6BB592A15BC505B8E")
    private String SECRET;

    //Token expiry date (3 days)
    @Value("259200000")
    private int EXPIRES_IN;

    @Value("Authorization")
    private String AUTH_HEADER;

    private SignatureAlgorithm SIGNATURE_ALGORITHM = SignatureAlgorithm.HS512;

    public String generateToken(User user){
        return Jwts.builder()
                .setIssuer(APP_NAME)
                .setSubject(user.getUsername())
                .claim("role", user.getRole())
                .claim("id", user.getId())
                .setAudience(generateAudience())
                .setIssuedAt(new Date())
                .setExpiration(generateExpirationDate())
                .signWith(SIGNATURE_ALGORITHM, SECRET).compact();
    }

    private String generateAudience(){
        return "web";
    }

    private Date generateExpirationDate() {
        return new Date(new Date().getTime() + EXPIRES_IN);
    }

    public User getUser(HttpServletRequest request) throws UnauthenticatedException {
        try {
            String token = getToken(request);
            if (token == null || getExpirationDateFromToken(token).before(new Date())) {
                throw new UnauthenticatedException("You are not logged in");
            }
            final String username = getUsernameFromToken(token);
            final long id = getIdFromToken(token);
            final Role role = getRoleFromToken(token);

            return new User(id, username, null, role);
        } catch (Exception e) {
            throw new UnauthenticatedException("You are not logged in");
        }
    }

    public String getUsernameFromToken(String token) {
        final Claims claims = this.getAllClaimsFromToken(token);
        return claims.getSubject();
    }

    public long getIdFromToken(String token) {
        final Claims claims = this.getAllClaimsFromToken(token);
        return (long) claims.get("id");
    }

    public Role getRoleFromToken(String token) {
        final Claims claims = this.getAllClaimsFromToken(token);
        return (Role) claims.get("role");
    }

    public Date getIssuedAtDateFromToken(String token) {
        final Claims claims = this.getAllClaimsFromToken(token);
        return claims.getIssuedAt();
    }

    public Date getExpirationDateFromToken(String token) {
        final Claims claims = this.getAllClaimsFromToken(token);
        return claims.getExpiration();
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET)
                .parseClaimsJws(token)
                .getBody();
    }

    private String getToken(HttpServletRequest request) {
        String authHeader = getAuthHeaderFromHeader(request);
        String start = "Bearer ";
        if (authHeader != null && authHeader.startsWith(start)) {
            return authHeader.substring(start.length());
        }
        return null;
    }

    private String getAuthHeaderFromHeader(HttpServletRequest request) {
        return request.getHeader(AUTH_HEADER);
    }
}
