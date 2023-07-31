package app.codeodyssey.codeodysseyapi.common.validations;

import app.codeodyssey.codeodysseyapi.common.exception.InvalidPasswordException;
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
        if (password == null || password.trim().isEmpty()) {
            throw new InvalidPasswordException(
                    "A senha deve conter pelo menos uma letra maiúscula, uma letra minúscula, um número, um caractere especial e ter entre 8 e 64 caracteres.");
        }

        int passwordLength = password.length();
        boolean isValid = passwordLength >= MIN_PASSWORD_LENGTH
                && passwordLength <= MAX_PASSWORD_LENGTH
                && Pattern.matches(PASSWORD_PATTERN, password);

        if (!isValid) {
            throw new InvalidPasswordException(
                    "The password must contain at least one uppercase letter, one lowercase letter, one number, one special character, and be between 8 and 64 characters.");
        }

        return true;
    }
}
