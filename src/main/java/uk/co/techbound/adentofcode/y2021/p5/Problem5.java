package uk.co.techbound.adentofcode.y2021.p5;

import lombok.extern.log4j.Log4j2;
import one.util.streamex.IntStreamEx;
import one.util.streamex.StreamEx;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Component;
import uk.co.techbound.adentofcode.AbstractProblemSolver;

import java.util.List;

@Log4j2
@Component
public class Problem5 extends AbstractProblemSolver<List<Pair<Coordinate, Coordinate>>, Long> {

    @Override
    protected Long partOne(List<Pair<Coordinate, Coordinate>> input) {
        int gridMax = StreamEx.of(input)
            .flatMapToInt(pair -> IntStreamEx.of(pair.getLeft().getX(), pair.getLeft().getY(), pair.getRight().getX(), pair.getRight().getY()))
            .max().getAsInt() + 1;
        int[][] grid = new int[gridMax][gridMax];
        StreamEx.of(input)
            .filter(pair -> pair.getLeft().getX() == pair.getRight().getX() || pair.getLeft().getY() == pair.getRight().getY())
            .forEach(pair -> incrementGrid(pair, grid));

        return StreamEx.of(grid)
            .flatMapToInt(IntStreamEx::of).filter(i -> i > 1).count();
    }

    private void incrementGrid(Pair<Coordinate, Coordinate> pair, int[][] grid) {
        Coordinate left = pair.getLeft();
        Coordinate right = pair.getRight();
        int leftX = left.getX();
        int rightX = right.getX();
        int leftY = left.getY();
        int rightY = right.getY();

        int yDirection = Integer.signum(rightY - leftY);
        int xDirection = Integer.signum(rightX - leftX);
        int currentX = leftX;
        int currentY = leftY;
        while(currentX != rightX || currentY != rightY) {
            grid[currentY][currentX]++;
            currentX += xDirection;
            currentY += yDirection;
        }
        grid[currentY][currentX]++;
    }

    @Override
    protected Long partTwo(List<Pair<Coordinate, Coordinate>> input) {
        return calculate(input);
    }

    private long calculate(List<Pair<Coordinate, Coordinate>> input) {
        int gridMax = StreamEx.of(input)
            .flatMapToInt(pair -> IntStreamEx.of(pair.getLeft().getX(), pair.getLeft().getY(), pair.getRight().getX(), pair.getRight().getY()))
            .max().getAsInt() + 1;
        int[][] grid = new int[gridMax][gridMax];
        StreamEx.of(input)
            .forEach(pair -> incrementGrid(pair, grid));

        return StreamEx.of(grid)
            .flatMapToInt(IntStreamEx::of).filter(i -> i > 1).count();
    }

    @Override
    protected List<Pair<Coordinate, Coordinate>> convertInput(StreamEx<String> lines) {
        return StreamEx.of(lines)
            .map(line -> line.split(" -> "))
            .map(split -> Pair.of(makeCoordinate(split[0]), makeCoordinate(split[1])))
            .toList();
    }

    private Coordinate makeCoordinate(String coordinateString) {
        String[] parts = coordinateString.split(",");
        Coordinate coordinate = new Coordinate(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]));
        return coordinate;
    }
}
