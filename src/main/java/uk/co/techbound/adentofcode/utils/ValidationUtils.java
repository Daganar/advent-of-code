package uk.co.techbound.adentofcode.utils;

import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.math.NumberUtils;

import java.util.Optional;
import java.util.function.Predicate;

@UtilityClass
public class ValidationUtils {

    public static Predicate<String> validateIntegerPredicate(Predicate<Integer> isValid) {
        return field -> Optional.ofNullable(field)
                .filter(NumberUtils::isDigits)
                .map(Integer::valueOf)
                .filter(isValid)
                .isPresent();
    }

    public static Predicate<String> validateStringRegexPredicate(String regex) {
        return field -> field != null && field.matches(regex);
    }

    public static Predicate<String> validateStringPredicate(Predicate<String> isValid) {
        return field -> field != null && isValid.test(field);
    }
}
