package kim.figure.site.management.configuration;


import kim.figure.site.management.common.exception.GlobalException;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.DefaultErrorAttributes;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;

import java.util.Map;

@Component
public class GlobalErrorAttributes extends DefaultErrorAttributes {
    @Autowired
    MessageSource messageSource;

    @Override
    public Map<String, Object> getErrorAttributes(ServerRequest request, ErrorAttributeOptions options) {
        Map<String, Object> map = super.getErrorAttributes(request, options);

        Throwable throwable = getError(request);

        if (throwable instanceof GlobalException) {
            GlobalException ex = (GlobalException) getError(request);
            map.put("exception", ex.getClass().getSimpleName());
            map.put("messageCode", ex.getClass().getSimpleName());
            map.put("status", ex.getStatusCode().value());
            map.put("error", ex.getReason());
            map.put("defaultMessage", throwable.getMessage());
            return map;
        } else if(throwable instanceof AuthenticationCredentialsNotFoundException){
            map.put("status", HttpStatus.UNAUTHORIZED.value());
            map.put("messageCode", "session.notfound.message");
            map.put("defaultMessage", throwable.getMessage());
            return map;
        }else{
            throwable.printStackTrace();
            map.put("exception", throwable.getClass().getSimpleName());
            map.put("messageCode", "unhandledException");
            map.put("messageCode", "unhandledException");
            map.put("defaultMessage", throwable.getMessage());
            return map;

        }


    }
}