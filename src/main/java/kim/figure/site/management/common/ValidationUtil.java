package kim.figure.site.management.common;


import kim.figure.site.management.common.exception.AbstractBindException;
import kim.figure.site.management.common.exception.ParameterBindingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Validator;

@Component
public class ValidationUtil {

    @Autowired
    Validator validator;

    public <T> void validate(T target) throws AbstractBindException {
        BeanPropertyBindingResult errors = new BeanPropertyBindingResult(target, target.getClass().getSimpleName());
        validator.validate(target, errors);
        if (errors.hasErrors()) {
            throw new ParameterBindingException(errors);
        }
    }
    public <T> T validateWithIdentity(T target) throws AbstractBindException {
        validate(target);
        return target;
    }
}