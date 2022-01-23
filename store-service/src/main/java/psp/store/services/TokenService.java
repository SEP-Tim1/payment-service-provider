package psp.store.services;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import psp.store.model.Store;
import psp.store.util.LoggerUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Slf4j
@Service
public class TokenService {

    @Value("psp")
    private String APP_NAME;

    @Value("${token.secret}")
    private String SECRET;

    //Token expiry date (3 days)
    @Value("259200000")
    private int EXPIRES_IN;

    @Autowired
    private LoggerUtil loggerUtil;

    private SignatureAlgorithm SIGNATURE_ALGORITHM = SignatureAlgorithm.HS512;

    public String generateToken(HttpServletRequest request, Store store){
        String apiToken = Jwts.builder()
                .setIssuer(APP_NAME)
                .setSubject(Long.toString(store.getId()))
                .setAudience(generateAudience())
                .setIssuedAt(new Date())
                .setExpiration(generateExpirationDate())
                .signWith(SIGNATURE_ALGORITHM, SECRET).compact();
        log.info(loggerUtil.getLogMessage(request, "Token generated for a store (id=" + store.getId() + ")"));
        return apiToken;
    }

    private String generateAudience(){
        return "web";
    }

    private Date generateExpirationDate() {
        return new Date(new Date().getTime() + EXPIRES_IN);
    }
}
