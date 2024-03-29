package uk.co.techbound.adentofcode;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import uk.co.techbound.adentofcode.utils.TimerUtils;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Component
@Log4j2
@AllArgsConstructor
public class CommandLineRunner implements org.springframework.boot.CommandLineRunner {

    Map<ProblemName, ProblemSolver> problemSolvers;

    @Override
    public void run(String... args) {
        List<String> arguments = Arrays.asList(args);

        ProblemName problemName;
        if(arguments.isEmpty()) {
            problemName = getTodaysProblemName();
        } else if(arguments.size() == 2) {
            problemName = getProblemNameFromArguments(arguments);
        } else {
            log.error("Unknown arguments: {}", arguments);
            return;
        }

        if(problemSolvers.containsKey(problemName)) {
            log.info("Solving problem: {}", problemName);
            long timeTaken = TimerUtils.timeIt(() -> problemSolvers.get(problemName).solve());
            log.info("Time taken to solve: {}ms", timeTaken);
        } else {
            log.error("Unknown problem: {}", problemName);
        }
    }

    private ProblemName getProblemNameFromArguments(List<String> arguments) {
        Iterator<String> iterator = arguments.iterator();
        int year = Integer.parseInt(iterator.next());
        int day = Integer.parseInt(iterator.next());
        return new ProblemName(year, day);
    }

    private ProblemName getTodaysProblemName() {
        LocalDate today = LocalDate.now();
        return new ProblemName(today.getYear(), today.getDayOfMonth());
    }

}
