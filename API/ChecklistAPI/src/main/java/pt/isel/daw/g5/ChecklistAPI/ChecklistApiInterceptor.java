package pt.isel.daw.g5.ChecklistAPI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import pt.isel.daw.g5.ChecklistAPI.exceptions.UnauthorizedException;
import pt.isel.daw.g5.ChecklistAPI.model.errorModel.ProblemJSON;
import pt.isel.daw.g5.ChecklistAPI.model.inputModel.User;
import pt.isel.daw.g5.ChecklistAPI.model.internalModel.InvalidParams;
import pt.isel.daw.g5.ChecklistAPI.repository.UserRepository;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Base64;
import java.util.Optional;

@Component
public class ChecklistApiInterceptor extends HandlerInterceptorAdapter {
    private static final Logger log = LoggerFactory.getLogger(ChecklistApiInterceptor.class);

    @Autowired
    private UserRepository userRepository;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HandlerMethod hm = (HandlerMethod) handler;
        Method methodAnnotation = hm.getMethod();
        if(!methodAnnotation.getDeclaringClass().isAnnotationPresent(RequiresAuthentication.class))
            return true;

        String authentication = request.getHeader("Authorization");
        if(authentication == null){
            log.info("!!! Requires Authorization header !!!");

            InvalidParams notIncludedUser = new InvalidParams("username", "username must be provided");
            InvalidParams notIncludedPassword = new InvalidParams("password", "password must be provided");
            ProblemJSON problemJSON = new ProblemJSON("/authentication-error", "Authentication Failed.", 401, "The username and it's corresponding password must be provided", request.getRequestURI(), new InvalidParams[]{notIncludedUser, notIncludedPassword});
            throw new UnauthorizedException(problemJSON);
        }

        String auth = new String(Base64.getDecoder().decode(authentication));
        String[] params = auth.split(":");

        Optional<User> user = userRepository.findById(params[0]);
        if (!user.isPresent() || !user.get().getPassword().equals(params[1])){
            InvalidParams notIncludedUser = new InvalidParams("username", "username is invalid");
            InvalidParams notIncludedPassword = new InvalidParams("password", "password is invalid");
            ProblemJSON problemJSON = new ProblemJSON("/authentication-error", "Authentication Failed.", 401, "The username or the password are not valid", request.getRequestURI(), new InvalidParams[]{notIncludedUser, notIncludedPassword});
            throw new UnauthorizedException(problemJSON);
        }
        request.setAttribute("Username", params[0]);
        return true;
    }
}
