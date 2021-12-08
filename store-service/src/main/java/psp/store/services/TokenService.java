package psp.store.services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import psp.store.exceptions.NotFoundException;
import psp.store.model.Store;

import java.util.Date;

@Service
public class TokenService {

    @Value("psp")
    private String APP_NAME;

    @Value("5B43E507D1232852D06EEDD2E30A24F10ABC72AC114D60D6BB592A15BC505B8E")
    private String SECRET;

    //Token expiry date (3 days)
    @Value("259200000")
    private int EXPIRES_IN;

    private SignatureAlgorithm SIGNATURE_ALGORITHM = SignatureAlgorithm.HS512;

    public String generateToken(Store store){
        return Jwts.builder()
                .setIssuer(APP_NAME)
                .setSubject(Long.toString(store.getId()))
                .setAudience(generateAudience())
                .setIssuedAt(new Date())
                .setExpiration(generateExpirationDate())
                .signWith(SIGNATURE_ALGORITHM, SECRET).compact();
    }

    public long getStore(String token) throws NotFoundException {
        try {
            return getStoreFromToken(token);
        } catch (Exception e) {
            throw new NotFoundException("Store could not be extracted");
        }
    }

    private long getStoreFromToken(String token) {
        final Claims claims = this.getAllClaimsFromToken(token);
        return Long.parseLong(claims.getSubject());
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET)
                .parseClaimsJws(token)
                .getBody();
    }

    private String generateAudience(){
        return "web";
    }

    private Date generateExpirationDate() {
        return new Date(new Date().getTime() + EXPIRES_IN);
    }
}
