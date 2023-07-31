package app.codeodyssey.codeodysseyapi.common.validations;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import app.codeodyssey.codeodysseyapi.common.exception.InvalidPasswordException;
import app.codeodyssey.codeodysseyapi.common.validations.ValidPasswordValidator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@DisplayName("test for the ValidPasswordValidator")
public class ValidPasswordValidatorTest {
    private final ValidPasswordValidator validPasswordValidator = new ValidPasswordValidator();

    @Test
    @DisplayName("try to validate valid passwords and success")
    void isValid_givenValidPassword_doesNotThrowException() {
        String[] validPassword = {"Password@123", "MyPa$$word2023", "SuperSecret!1", "Abcdefg@1234567890"};

        for (String password : validPassword) {
            assertDoesNotThrow(() -> validPasswordValidator.isValid(password, null));
        }
    }

    @Test
    @DisplayName("try to validate invalid passwords and throw exception")
    void isValid_givenInvalidPassword_exceptionThrown() {
        String[] invalidPasswords = {
            "", // Empty password
            "Short@1", // Password less than 8 characters
            "LongPasswordThatExceedsTheMaximumAllowedLength@123456789012345678901234567890123456789012345678901234567890", // Password more than 64 characters
            "passwordwithoutuppercase@123", // Password missing uppercase letter
            "PASSWORDWITHOUTLOWERCASE@123", // Password missing lowercase letter
            "passwordWithLowerCaseAndUpperCaseButNoSpecialChars123", // Password missing special character
            "passwordWithLowerCaseAndSpecialCharsButNoNumbers@@", // Password missing numbers
            "!@#$%&*^" // Password missing letters
        };

        for (String password : invalidPasswords) {
            assertThrows(InvalidPasswordException.class, () -> validPasswordValidator.isValid(password, null));
        }
    }
}
