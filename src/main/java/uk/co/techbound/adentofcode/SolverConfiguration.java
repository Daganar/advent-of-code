package uk.co.techbound.adentofcode;

import one.util.streamex.StreamEx;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Configuration
public class SolverConfiguration {

    @Bean
    @Primary
    public Map<ProblemName, ProblemSolver> problemSolvers(List<ProblemSolver> problemSolvers) {
        return StreamEx.of(problemSolvers).toMap(ProblemSolver::getProblemName, Function.identity());
    }
}
