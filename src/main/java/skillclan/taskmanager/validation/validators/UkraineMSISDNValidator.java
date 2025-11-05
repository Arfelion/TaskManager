package skillclan.taskmanager.validation.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import skillclan.taskmanager.validation.UkraineMSISDN;

import java.util.regex.Pattern;

public class UkraineMSISDNValidator implements
        ConstraintValidator<UkraineMSISDN, String> {

    private Pattern pattern;

    @Override
    public void initialize(UkraineMSISDN constraintAnnotation) {
        this.pattern = Pattern.compile(constraintAnnotation.regex());
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {

        if (value == null || value.isEmpty()) {
            return true;
        }
        return this.pattern.matcher(value).matches();
    }
}
