package pt.isel.daw.g5.ChecklistAPI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Optional;

@Component
public class ChecklistApiInterceptor extends HandlerInterceptorAdapter {

    private static final Logger log = LoggerFactory.getLogger(ChecklistApiInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String pattern = (String) Optional.ofNullable(request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE))
                .orElse("[unknown]");
        log.info("on preHandle for {}", pattern);
        HandlerMethod hm = (HandlerMethod) handler;
        //RequiresAuthentication
        Method methodAnnotation = hm.getMethod();
        if(methodAnnotation.getDeclaringClass().isAnnotationPresent(RequiresAuthentication.class)){
            log.info("!!! Requires authentication !!!");
        }
        String authorization = request.getHeader("Authorization");
        //getDecoder().decode(authorization.substring(6));
        return true;
    }
}
