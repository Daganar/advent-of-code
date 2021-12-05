package uk.co.techbound.adentofcode.y2020.p17;

import lombok.Value;
import lombok.extern.log4j.Log4j2;
import one.util.streamex.StreamEx;
import org.springframework.stereotype.Component;
import uk.co.techbound.adentofcode.AbstractProblemSolver;

import java.util.HashSet;
import java.util.Set;

@Log4j2
@Component
public class Problem17 extends AbstractProblemSolver<char[][], Long> {

    @Override
    protected Long partOne(char[][] input) {
        Set<Position> occupiedPositions = new HashSet<>();
        addInitialState(occupiedPositions, input);
        int initialSize = input.length;
        for(int iteration = 1; iteration <= 6; iteration++) {
            log.info("Iteration: {}, occupiedPositions: {}", iteration - 1, occupiedPositions.size());
            Set<Position> newOccupiedPositions = new HashSet<>();
            for(int x =  0 - iteration; x < initialSize + iteration; x++) {
                for(int y = 0 - iteration; y < initialSize + iteration; y++) {
                    for(int z = 0 - iteration; z < initialSize + iteration; z++) {
                        int occupiedNeighboursCount = countOccupiedNeighbours(occupiedPositions, x, y, z);
                        Position currentPosition = new Position(x, y, z, 0);
                        if(occupiedPositions.contains(currentPosition)) {
                            if(occupiedNeighboursCount == 2 || occupiedNeighboursCount == 3) {
                                newOccupiedPositions.add(currentPosition);
                            }
                        } else if(occupiedNeighboursCount == 3) {
                            newOccupiedPositions.add(currentPosition);
                        }
                    }
                }
            }
            occupiedPositions = newOccupiedPositions;
        }
        return (long) occupiedPositions.size();
    }

    private int countOccupiedNeighbours(Set<Position> occupiedPositions, int x, int y, int z) {
        int count = 0;
        for(int i = x - 1; i <= x + 1; i++) {
            for(int j = y - 1; j <= y + 1; j++) {
                for(int k = z - 1; k <= z + 1; k++) {
                    if(i == x && j == y && k == z) {
                        continue;
                    }
                    if(occupiedPositions.contains(new Position(i, j, k, 0))) {
                        count++;
                    }
                }
            }
        }
        return count;
    }

    private int countOccupiedNeighbours(Set<Position> occupiedPositions, int x, int y, int z, int w) {
        int count = 0;
        for(int i = x - 1; i <= x + 1; i++) {
            for(int j = y - 1; j <= y + 1; j++) {
                for(int k = z - 1; k <= z + 1; k++) {
                    for(int m = w - 1; m <= w + 1; m++) {
                        if (i == x && j == y && k == z && m == w) {
                            continue;
                        }
                        if (occupiedPositions.contains(new Position(i, j, k, m))) {
                            count++;
                        }
                    }
                }
            }
        }
        return count;
    }

    private void addInitialState(Set<Position> occupiedPositions, char[][] input) {
        for(int x = 0; x < input.length; x++) {
            char[] row = input[x];
            for(int y = 0; y < row.length; y++) {
                if(row[y] == '#') {
                    occupiedPositions.add(new Position(x, y, 0, 0));
                }
            }
        }
    }

    @Override
    protected Long partTwo(char[][] input) {
        Set<Position> occupiedPositions = new HashSet<>();
        addInitialState(occupiedPositions, input);
        int initialSize = input.length;
        for(int iteration = 1; iteration <= 6; iteration++) {
            log.info("Iteration: {}, occupiedPositions: {}", iteration - 1, occupiedPositions.size());
            Set<Position> newOccupiedPositions = new HashSet<>();
            for(int x = 0 - iteration; x < initialSize + iteration; x++) {
                for(int y = 0 - iteration; y < initialSize + iteration; y++) {
                    for(int z = 0 - iteration; z < initialSize + iteration; z++) {
                        for(int w = 0 - iteration; w < initialSize + iteration; w++){
                            int occupiedNeighboursCount = countOccupiedNeighbours(occupiedPositions, x, y, z, w);
                            Position currentPosition = new Position(x, y, z, w);
                            if (occupiedPositions.contains(currentPosition)) {
                                if (occupiedNeighboursCount == 2 || occupiedNeighboursCount == 3) {
                                    newOccupiedPositions.add(currentPosition);
                                }
                            } else if (occupiedNeighboursCount == 3) {
                                newOccupiedPositions.add(currentPosition);
                            }
                        }
                    }
                }
            }
            occupiedPositions = newOccupiedPositions;
        }
        return (long) occupiedPositions.size();
    }

    @Override
    protected char[][] convertInput(StreamEx<String> lines) {
        return lines
            .map(String::toCharArray)
            .toArray(new char[0][]);
    }

    @Value
    public static class Position {
        int x;
        int y;
        int z;
        int w;
    }

}
