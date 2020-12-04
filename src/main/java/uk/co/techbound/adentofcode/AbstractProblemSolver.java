package uk.co.techbound.adentofcode;

import lombok.extern.log4j.Log4j2;
import one.util.streamex.StreamEx;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;

@Log4j2
public abstract class AbstractProblemSolver<T,U> implements ProblemSolver {

    protected StreamEx<String> getLinesOfProblemInput(String filename) {
        String path = makeCurrentProblemPath(filename);
        File fileFromClasspath = getFileFromClasspath(path);
        return getLinesOfProblemInput(fileFromClasspath);
    }

    private String makeCurrentProblemPath(String filename) {
        return getRelativePackageOfCurrentProblem().replace('.', '/') + "/" + filename;
    }

    protected StreamEx<String> getLinesOfProblemInput(File file) {
        try {
            return StreamEx.ofLines(file.toPath());
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private File getFileFromClasspath(String path) {
        ClassPathResource classPathResource = new ClassPathResource(path);
        try {
            return classPathResource.getFile();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Override
    public void solve() {
        if(exampleExists()) {
            log.info("Example solution to part 1: {}", partOneExample());
            log.info("Example solution to part 2: {}", partTwoExample());
        }
        T partOneInput = convertInput(getLinesOfProblemInput("input.txt"));
        log.info("Part 1: {}", partOne(partOneInput));

        T partTwoInput = convertInput(getLinesOfProblemInput("input.txt"));
        log.info("Part 1: {}", partTwo(partTwoInput));
    }

    private boolean exampleExists() {
        return new ClassPathResource(makeCurrentProblemPath("example.txt")).exists();
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

    protected U partOneExample() {
        T exampleInput = convertInput(getLinesOfProblemInput("example.txt"));
        return partOne(exampleInput);
    }

    protected U partTwoExample() {
        T exampleInput = convertInput(getLinesOfProblemInput("example.txt"));
        return partTwo(exampleInput);
    }

    protected abstract U partOne(T input);
    protected abstract U partTwo(T input);

    protected abstract T convertInput(StreamEx<String> lines);
}
