package psp.auth.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import psp.auth.exceptions.UnauthenticatedException;
import psp.auth.model.User;
import psp.auth.services.TokenService;

import javax.servlet.http.HttpServletRequest;

@Service
public class LoggerUtil {

    @Autowired
    private TokenService tokenService;

    public String getLogMessage(HttpServletRequest request, String message) {
        return request.getRemoteAddr() + " | " + request.getMethod() + " | " + request.getRequestURI() + " | " + getUser(request) + " |" + message;
    }

    private String getUser(HttpServletRequest request) {
        try {
            User user = tokenService.getUser(request);
            return "User ID=" + user.getId();
        } catch (UnauthenticatedException e) {
            return "Anonymous user";
        }
    }
}
