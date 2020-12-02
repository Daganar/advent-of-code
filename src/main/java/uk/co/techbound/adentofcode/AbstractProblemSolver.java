package uk.co.techbound.adentofcode;

import one.util.streamex.StreamEx;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;

public abstract class AbstractProblemSolver implements ProblemSolver {

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

}
