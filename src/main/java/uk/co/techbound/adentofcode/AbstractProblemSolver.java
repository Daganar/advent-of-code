package uk.co.techbound.adentofcode;

import lombok.extern.log4j.Log4j2;
import one.util.streamex.StreamEx;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;

@Log4j2
public abstract class AbstractProblemSolver<T> implements ProblemSolver {

    protected StreamEx<String> getLinesOfProblemInput() {
        String path = getRelativePackageOfCurrentProblem().replace('.', '/') + "/input.txt";
        return getLinesOfProblemInput(path);
    }

    protected StreamEx<String> getLinesOfProblemInput(String filename) {
        try {
            ClassPathResource classPathResource = new ClassPathResource(filename);
            File input = classPathResource.getFile();
            return StreamEx.ofLines(input.toPath());
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Override
    public void solve() {
        T partOneInput = convertInput(getLinesOfProblemInput());
        log.info("Part 1: {}", partOne(partOneInput));

        T partTwoInput = convertInput(getLinesOfProblemInput());
        log.info("Part 1: {}", partTwo(partTwoInput));
    }

    @Override
    public ProblemName getProblemName() {
        String yearDayPackage = getRelativePackageOfCurrentProblem();
        String[] prefixedYearAndDay = yearDayPackage.split("\\.");

        int year = Integer.parseInt(removeLeadingCharacter(prefixedYearAndDay[0]));
        int day = Integer.parseInt(removeLeadingCharacter(prefixedYearAndDay[1]));

        return new ProblemName(year, day);
    }

    private String getRelativePackageOfCurrentProblem() {
        String problemPackage = this.getClass().getPackageName();
        int basePackageLength = AbstractProblemSolver.class.getPackageName().length() + 1;

        return problemPackage.substring(basePackageLength);
    }

    private String removeLeadingCharacter(String string) {
        return string.substring(1);
    }

    protected abstract Object partOne(T input);
    protected abstract Object partTwo(T input);

    protected abstract T convertInput(StreamEx<String> lines);
}
