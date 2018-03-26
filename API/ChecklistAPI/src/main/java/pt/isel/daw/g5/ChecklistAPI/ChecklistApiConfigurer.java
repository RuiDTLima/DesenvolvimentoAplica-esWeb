package pt.isel.daw.g5.ChecklistAPI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class ChecklistApiConfigurer extends WebMvcConfigurationSupport {

    @Autowired
    ChecklistApiInterceptor checklistApiInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(checklistApiInterceptor);
    }
}
