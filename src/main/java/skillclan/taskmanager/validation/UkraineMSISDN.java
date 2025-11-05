package skillclan.taskmanager.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import skillclan.taskmanager.validation.validators.UkraineMSISDNValidator;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Constraint(validatedBy = UkraineMSISDNValidator.class)
@Target({ FIELD })
@Retention(RUNTIME)
public @interface UkraineMSISDN {

    String regex() default "^380(\\d){9}$";

    String message() default "MSISDN must contain 380 and 9 digits";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
