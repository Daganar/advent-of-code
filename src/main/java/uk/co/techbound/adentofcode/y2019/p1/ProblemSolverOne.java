package uk.co.techbound.adentofcode.y2019.p1;

import lombok.extern.log4j.Log4j2;
import one.util.streamex.StreamEx;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import uk.co.techbound.adentofcode.ProblemName;
import uk.co.techbound.adentofcode.ProblemSolver;

import java.io.IOException;
import java.io.UncheckedIOException;

@Component
@Log4j2
public class ProblemSolverOne implements ProblemSolver {

    private static final ProblemName PROBLEM_NAME = new ProblemName(2019, 1);

    @Override
    public void solve() {
        try {
            long totalFuel = StreamEx.ofLines(new ClassPathResource("problems/1/input.txt").getFile().toPath())
                .mapToLong(Long::parseLong)
                .map(this::calculateFuelOfModuleAndFuel)
                .sum();
            log.info("Total fuel requirements: {}", totalFuel);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }

    }

    public long calculateFuelOfModuleAndFuel(long moduleMass) {
        long moduleFuel = calculateFuelOfModule(moduleMass);
        return moduleFuel + calculateFuelOfFuel(moduleFuel);
    }

    public long calculateFuelOfFuel(long mass) {
        long fuelOfFuel = calculateFuelOfModule(mass);
        long total = fuelOfFuel;
        while((fuelOfFuel = calculateFuelOfModule(fuelOfFuel)) > 0) {
            total += fuelOfFuel;
        }
        return total;
    }

    public long calculateFuelOfModule(long mass) {
        return Math.max((mass / 3) - 2, 0);
    }

    @Override
    public ProblemName getProblemName() {
        return PROBLEM_NAME;
    }
}
