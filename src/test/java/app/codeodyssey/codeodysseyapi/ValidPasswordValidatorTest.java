package app.codeodyssey.codeodysseyapi;

import app.codeodyssey.codeodysseyapi.common.exception.InvalidPasswordException;
import app.codeodyssey.codeodysseyapi.validations.ValidPasswordValidator;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;



@SpringBootTest
public class ValidPasswordValidatorTest {
    private final ValidPasswordValidator validPasswordValidator = new ValidPasswordValidator();

    @Test
    void testExecute_validPassword() {
        String[] validPassword = {
                "Password@123",
                "MyPa$$word2023",
                "SuperSecret!1",
                "Abcdefg@1234567890"
        };

        for (String password : validPassword) {
            assertDoesNotThrow(() -> validPasswordValidator.isValid(password, null));
        }
    }

    @Test
    void testExecute_invalidPassword() {
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
