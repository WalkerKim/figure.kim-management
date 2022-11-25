package kim.figure.site.management.common.exception;

import org.springframework.validation.BeanPropertyBindingResult;

/**
 * author         : walker
 * date           : 2022. 11. 21.
 * description    :
 */
public class ParameterBindingException extends AbstractBindException{
    public ParameterBindingException(BeanPropertyBindingResult errors) {
        super(errors);
    }
}
