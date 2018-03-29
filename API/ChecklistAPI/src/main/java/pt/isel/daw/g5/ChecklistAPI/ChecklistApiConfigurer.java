package pt.isel.daw.g5.ChecklistAPI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

@Configuration
public class ChecklistApiConfigurer extends WebMvcConfigurationSupport {
    @Autowired
    private ChecklistApiInterceptor checklistApiInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(checklistApiInterceptor);
    }
}