package uk.co.techbound.adentofcode.y2020.p4;

import lombok.Value;
import lombok.extern.log4j.Log4j2;
import one.util.streamex.StreamEx;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.stereotype.Component;
import uk.co.techbound.adentofcode.AbstractProblemSolver;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

@Log4j2
@Component
public class Problem4 extends AbstractProblemSolver {

    Set<String> expectedFields = Set.of(
            "byr",
            "iyr",
            "eyr",
            "hgt",
            "hcl",
            "ecl",
            "pid"
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
                .filter(this::isValidPassport2)
                .toList();
        log.info("{}", passports);
        log.info("size: {}", passports.size());
    }

    private boolean isValidPassport(Map<String,String> map) {
        return map.keySet().containsAll(expectedFields);
    }

    private boolean isValidPassport2(Map<String,String> map) {
        if(!isValidPassport(map)) {
            return false;
        }
//        byr (Birth Year) - four digits; at least 1920 and at most 2002.
        boolean byrValid = validateNumber(map.get("byr"), byr -> byr >= 1920 && byr <= 2002);

//        iyr (Issue Year) - four digits; at least 2010 and at most 2020.
        boolean iyrValid = validateNumber(map.get("iyr"), iyr -> iyr >= 2010 && iyr <= 2020);

//        eyr (Expiration Year) - four digits; at least 2020 and at most 2030.
        boolean eyrValid = validateNumber(map.get("eyr"), eyr -> eyr >= 2020 && eyr <= 2030);

//        hgt (Height) - a number followed by either cm or in:
//        If cm, the number must be at least 150 and at most 193.
//        If in, the number must be at least 59 and at most 76.
        boolean hgtValid = getHeight(map.get("hgt"))
                .map(height -> {
                    int value = height.getValue();
                    if(height.getUnits().equals("cm")) {
                        return value >= 150 && value <= 193;
                    } else {
                        return value >= 59 && value <= 76;
                    }
                })
                .orElse(false);

//        hcl (Hair Color) - a # followed by exactly six characters 0-9 or a-f.
        boolean hclValid = map.get("hcl").matches("#[0-9a-f]{6}");

//        ecl (Eye Color) - exactly one of: amb blu brn gry grn hzl oth.
        boolean eclValid = map.get("ecl").matches("(amb|blu|brn|gry|grn|hzl|oth)");

//        pid (Passport ID) - a nine-digit number, including leading zeroes.
        boolean pidValid = map.get("pid").matches("\\d{9}");


        return byrValid && iyrValid && eyrValid && hgtValid && hclValid && eclValid && pidValid;
    }

    private Boolean validateNumber(String field, Function<Integer, Boolean> isValid) {
        return Optional.of(field)
                .filter(NumberUtils::isDigits)
                .map(Integer::valueOf)
                .map(isValid)
                .orElse(false);
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
