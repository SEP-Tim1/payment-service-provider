package psp.store.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import psp.store.client.AuthClient;

import javax.servlet.http.HttpServletRequest;

@Service
public class LoggerUtil {

    @Autowired
    private AuthClient authClient;

    public String getLogMessage(HttpServletRequest request, String message) {
        return request.getRemoteAddr() + " | " + request.getMethod() + " | " + request.getRequestURI() + " | " + getUser(request) + " |" + message;
    }

    private String getUser(HttpServletRequest request) {
        try {
            String token = request.getHeader("Authorization");
            long id = authClient.getUserId(token);
            return "User ID=" + id;
        } catch (Exception e) {
            return "Anonymous user";
        }
    }
}
