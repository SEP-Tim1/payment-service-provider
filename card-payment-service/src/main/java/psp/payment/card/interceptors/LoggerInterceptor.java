package psp.payment.card.interceptors;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
public class LoggerInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info(request.getRemoteAddr() + " | " + request.getMethod() + " | " + request.getRequestURI() + " | request");
        return HandlerInterceptor.super.preHandle(request, response, handler);
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        log.info(request.getRemoteAddr() + " | " + request.getMethod() + " | " + request.getRequestURI() + " | response status = " + response.getStatus());
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }
}
