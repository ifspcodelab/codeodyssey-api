package app.codeodyssey.codeodysseyapi.validations;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

public class CustomEmailValidator implements ConstraintValidator<CustomEmail, String> {

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9._%+-]{64}@[A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*\\.[A-Za-z]{2,255}$");
    private static final String DANGEROUS_CHARACTERS_REGEX = "[\\x00`'\"\\\\]";

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        if (email == null) {
            return false;
        }

        if (email.length() > 350) {
            return false;
        }

        if (Pattern.compile(DANGEROUS_CHARACTERS_REGEX).matcher(email).find()) {
            return false;
        }
        return EMAIL_PATTERN.matcher(email).matches();
    }
}
