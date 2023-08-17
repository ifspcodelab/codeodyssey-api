package app.codeodyssey.codeodysseyapi.common.validations;

import app.codeodyssey.codeodysseyapi.common.exception.InvalidPasswordException;
import app.codeodyssey.codeodysseyapi.common.exception.PasswordProblem;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class ValidPasswordValidator implements ConstraintValidator<ValidPassword, String> {

    private static final int MIN_PASSWORD_LENGTH = 8;
    private static final int MAX_PASSWORD_LENGTH = 64;
    private static final String PASSWORD_PATTERN =
            "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&#])[\\p{L}\\p{N}@\\$!%*?&#\\s]{8,64}$";

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        if (password == null) {
            throw new InvalidPasswordException(PasswordProblem.NULL.getMessage());
        }

        if (password.isEmpty()) {
            throw new InvalidPasswordException(PasswordProblem.EMPTY.getMessage());
        }

        int passwordLength = password.length();

        if (passwordLength < MIN_PASSWORD_LENGTH) {
            throw new InvalidPasswordException(PasswordProblem.MIN_LENGTH.getMessage());
        }

        if (passwordLength > MAX_PASSWORD_LENGTH) {
            throw new InvalidPasswordException(PasswordProblem.MAX_LENGTH.getMessage());
        }

        if (!Pattern.matches(PASSWORD_PATTERN, password)) {
            throw new InvalidPasswordException(PasswordProblem.PATTERN.getMessage());
        }

        return true;
    }
}
