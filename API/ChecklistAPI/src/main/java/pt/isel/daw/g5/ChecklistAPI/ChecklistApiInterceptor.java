package pt.isel.daw.g5.ChecklistAPI;

import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import pt.isel.daw.g5.ChecklistAPI.exceptions.UnauthorizedException;
import pt.isel.daw.g5.ChecklistAPI.model.Token;
import pt.isel.daw.g5.ChecklistAPI.model.errorModel.ProblemJSON;
import pt.isel.daw.g5.ChecklistAPI.model.internalModel.InvalidParams;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;

@Component
public class ChecklistApiInterceptor extends HandlerInterceptorAdapter {
    private static final Logger log = LoggerFactory.getLogger(ChecklistApiInterceptor.class);
    private static final String INTROSPECT_URL = "http://35.189.110.248/openid-connect-server-webapp/introspect";
    private static String CLIENT_ID;
    private static String CLIENT_SECRET;
    private final Gson gson = new Gson();

    {
        InputStream resource = ClassLoader.getSystemResourceAsStream("credentials.txt");
        try(BufferedReader br = new BufferedReader(new InputStreamReader(resource))){
            String line = br.readLine();
            String[] part = line.split(":");
            CLIENT_ID = part[0];
            CLIENT_SECRET = part[1];
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!(handler instanceof HandlerMethod)) return true;
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

        URL url = new URL(INTROSPECT_URL);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setDoOutput(true);
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        String basic = new String(Base64.getEncoder().encode(String.format("%s:%s", CLIENT_ID, CLIENT_SECRET).getBytes()));
        connection.setRequestProperty("Authorization", "Basic " + basic);

        PrintWriter out = new PrintWriter(new OutputStreamWriter(connection.getOutputStream()));
        out.print("token=" + authentication.replace("Bearer ", ""));
        out.flush();
        BufferedReader input = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        Token token = gson.fromJson(input, Token.class);
        if(!token.isActive()) {
            InvalidParams notIncludedToken = new InvalidParams("token", "token is invalid");
            ProblemJSON problemJSON = new ProblemJSON("/authentication-error", "Authentication Failed.", 401, "The username or the password are not valid", request.getRequestURI(), new InvalidParams[]{notIncludedToken});
            throw new UnauthorizedException(problemJSON);
        }

        request.setAttribute("Username", token.getUser_id());
        return true;
    }
}
