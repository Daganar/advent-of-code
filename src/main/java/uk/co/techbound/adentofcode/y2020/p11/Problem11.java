package uk.co.techbound.adentofcode.y2020.p11;

import lombok.extern.log4j.Log4j2;
import one.util.streamex.IntStreamEx;
import one.util.streamex.StreamEx;
import org.springframework.stereotype.Component;
import uk.co.techbound.adentofcode.AbstractProblemSolver;

@Log4j2
@Component
public class Problem11 extends AbstractProblemSolver<char[][], Long> {

    @Override
    protected Long partOne(char[][] currentGrid) {
        currentGrid = getEndStateOfGrid(currentGrid, this::getNextState);
        return countOccupiedSeats(currentGrid);
    }


    @Override
    protected Long partTwo(char[][] currentGrid) {
        currentGrid = getEndStateOfGrid(currentGrid, this::getNextStateNew);
        return countOccupiedSeats(currentGrid);
    }

    private char[][] getEndStateOfGrid(char[][] currentGrid, TriFunction<Character, char[][], Integer, Integer> getNextStateFunction) {
        int maxX = currentGrid.length;
        int maxY = currentGrid[0].length;
        boolean change = true;
        while (change) {
            char[][] nextGrid = new char[maxX][maxY];
            change = false;
            for(int x = 0; x < maxX; x++) {
                for(int y = 0; y < maxY; y++) {
                    char current = currentGrid[x][y];
                    char next = getNextStateFunction.apply(currentGrid, x, y);
                    nextGrid[x][y] = next;
                    change |= current != next;
                }
            }
            currentGrid = nextGrid;
        }
        return currentGrid;
    }

    public char getNextState(char[][] grid, int x, int y) {
        char current = grid[x][y];
        if(current == '.') {
            return current;
        }
        int countAdjacentToCurrent = 0;
        for(int i = -1; i <= 1; i++){
            for(int j = -1; j <= 1; j++) {
                if(i == 0 && j == 0) {
                    continue;
                }
                if(isOccupied(grid, x + i, y + j)) {
                    countAdjacentToCurrent++;
                }
            }
        }
        if(current == 'L' && countAdjacentToCurrent == 0) {
            return '#';
        } else if(current == '#' && countAdjacentToCurrent >= 4) {
            return 'L';
        } else {
            return current;
        }
    }

    public char getNextStateNew(char[][] grid, int x, int y) {
        char current = grid[x][y];
        if(current == '.') {
            return current;
        }
        int countAdjacentToCurrent = 0;
        for(int i = -1; i <=1; i++) {
            for(int j = -1; j <= 1; j++) {
                if(i==0 && j == 0) {
                    continue;
                }
                if(isOccupiedInDirection(grid, x, y, i, j)) {
                    countAdjacentToCurrent++;
                }
            }
        }

        if(current == 'L' && countAdjacentToCurrent == 0) {
            return '#';
        } else if(current == '#' && countAdjacentToCurrent >= 5) {
            return 'L';
        } else {
            return current;
        }
    }

    public boolean isOccupiedInDirection(char[][] grid, int x, int y, int xIncrement, int yIncrement) {
        int maxX = grid.length;
        int maxY = grid[0].length;
        int i = x + xIncrement;
        int j = y + yIncrement;
        while(i >= 0 && i < maxX && j >= 0 && j < maxY) {
            if(grid[i][j] == '#') {
                return true;
            } else if(grid[i][j] == 'L') {
                return false;
            }
            i+=xIncrement;
            j+=yIncrement;
        }
        return false;
    }


    public boolean isOccupied(char[][] grid, int x, int y) {
        int maxX = grid.length;
        int maxY = grid[0].length;
        if(x >= 0 && x < maxX) {
            if(y >= 0 && maxY > y) {
                return grid[x][y] == '#';
            }
        }
        return false;
    }

    private long countOccupiedSeats(char[][] currentGrid) {
        return StreamEx.of(currentGrid)
            .flatMapToInt(IntStreamEx::of)
            .filter(i -> i == '#')
            .count();
    }

    @Override
    protected char[][] convertInput(StreamEx<String> lines) {
        return lines
            .map(String::toCharArray)
            .toArray(char[][]::new);
    }
    @FunctionalInterface
    public interface TriFunction<R, A, B, C> {
        R apply(A a, B b, C c);
    }
}
