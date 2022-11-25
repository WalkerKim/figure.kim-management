package kim.figure.site.management.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindException;
import org.springframework.web.server.ResponseStatusException;

public abstract class AbstractBindException extends ResponseStatusException {
    BindException bindException;
    public AbstractBindException(BeanPropertyBindingResult errors) {
        super(HttpStatus.BAD_REQUEST);
        this.bindException = new BindException(errors);
    }
    public BindException getBindException(){
        return this.bindException;
    }
}