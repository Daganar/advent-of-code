package uk.co.techbound.adentofcode.y2020.p4;

import lombok.Value;
import lombok.extern.log4j.Log4j2;
import one.util.streamex.EntryStream;
import one.util.streamex.StreamEx;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.stereotype.Component;
import uk.co.techbound.adentofcode.AbstractProblemSolver;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;

@Log4j2
@Component
public class Problem4 extends AbstractProblemSolver {

    Map<String, Predicate<String>> fieldValidators = Map.of(
        "byr", validateNumberPredicate(byr -> byr >= 1920 && byr <= 2002),
        "iyr", validateNumberPredicate(iyr -> iyr >= 2010 && iyr <= 2020),
        "eyr", validateNumberPredicate(eyr -> eyr >= 2020 && eyr <= 2030),
        "hgt", this::validateHeight,
        "hcl", hairColour -> hairColour.matches("#[0-9a-f]{6}"),
        "ecl", eyeColour -> eyeColour.matches("(amb|blu|brn|gry|grn|hzl|oth)"),
        "pid", pid -> pid.matches("\\d{9}")
    );

    @Override
    public void solve(List<String> problemArguments) {

        StreamEx<String> lines = getLinesOfProblemInput();
        List<Map<String, String>> passports = lines.groupRuns((first, second) -> !second.trim().isEmpty())
                .map(list ->
                        StreamEx.of(list)
                                .remove(String::isEmpty)
                                .flatMap(line -> StreamEx.of(line.split(" ")))
                                .map(fieldValue -> fieldValue.split(":"))
                                .toMap(keyValue -> keyValue[0], keyValue -> keyValue[1])
                )
                .filter(this::isValidPassport3)
                .toList();
        log.info("{}", passports);
        log.info("size: {}", passports.size());
    }

    private boolean isValidPassport1(Map<String,String> map) {
        return map.keySet().containsAll(fieldValidators.keySet());
    }

    private boolean isValidPassport3(Map<String, String> passport) {
        return isValidPassport1(passport) &&
                EntryStream.of(fieldValidators)
                    .mapKeys(passport::get)
                    .invert()
                    .allMatch(Predicate::test);
    }

    private boolean validateHeight(String heightString) {
        return getHeight(heightString)
                .filter(height -> {
                    int value = height.getValue();
                    if (height.getUnits().equals("cm")) {
                        return value >= 150 && value <= 193;
                    } else {
                        return value >= 59 && value <= 76;
                    }
                })
                .isPresent();
    }

    private Predicate<String> validateNumberPredicate(Predicate<Integer> isValid) {
        return field -> Optional.of(field)
                .filter(NumberUtils::isDigits)
                .map(Integer::valueOf)
                .filter(isValid)
                .isPresent();
    }

    private Optional<Height> getHeight(String heightString) {
        boolean matches = heightString.matches("\\d+(cm|in)");
        if(!matches) {
            return Optional.empty();
        }
        String value = heightString.substring(0, heightString.length() - 2);
        String units = heightString.substring(heightString.length() - 2);
        return Optional.of(new Height(Integer.parseInt(value), units));
    }

    @Value
    public static class Height {
        int value;
        String units;
    }
}
