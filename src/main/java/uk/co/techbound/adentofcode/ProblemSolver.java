package uk.co.techbound.adentofcode;

import java.util.List;

public interface ProblemSolver {
    void solve(List<String> problemArguments);

    ProblemName getProblemName();
}
