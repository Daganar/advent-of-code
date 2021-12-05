package uk.co.techbound.adentofcode.y2020.p24;

import lombok.extern.log4j.Log4j2;
import one.util.streamex.StreamEx;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Component;
import uk.co.techbound.adentofcode.AbstractProblemSolver;

import java.util.*;

@Log4j2
@Component
public class Problem24 extends AbstractProblemSolver<List<String>, Long> {

    @Override
    protected Long partOne(List<String> input) {
        Set<Pair<Integer, Integer>> grid = makeFloorLayout(input);

        return (long) grid.size();
    }

    private Set<Pair<Integer, Integer>> makeFloorLayout(List<String> input) {
        List<List<String>> lists = StreamEx.of(input).map(this::moves).toList();
        int x = 0;
        int y = 0;
        Set<Pair<Integer, Integer>> blackTiles = new HashSet<>();
        for(List<String> line : lists) {
            for(String direction : line) {
                Pair<Integer, Integer> next = getNext(direction, x, y);
                x = next.getLeft();
                y = next.getRight();
            }
            Pair<Integer, Integer> tilePosition = Pair.of(x, y);
            if(blackTiles.contains(tilePosition)) {
                blackTiles.remove(tilePosition);
            } else {
                blackTiles.add(tilePosition);
            }
            x = 0;
            y = 0;
//            log.info("black tiles: {}", blackTiles);
        }
        return blackTiles;
    }

    @Override
    protected Long partTwo(List<String> input) {
        Set<Pair<Integer, Integer>> blackTilePositions = makeFloorLayout(input);
        for(int i = 1; i <= 100; i++) {
            blackTilePositions = applyRules(blackTilePositions, i + 30);
            log.info("Day {}: {}", i, blackTilePositions.size());
        }
        return (long) blackTilePositions.size();
    }

    private Set<Pair<Integer, Integer>> applyRules(Set<Pair<Integer, Integer>> grid, int maxSize) {
        HashSet<Pair<Integer, Integer>> newPositions = new HashSet<>();
        for(int i = -maxSize; i < maxSize; i++) {
            for(int j = -maxSize; j < maxSize; j++) {
                Pair<Integer, Integer> currentPosition = Pair.of(i, j);
                boolean isBlack = grid.contains(currentPosition);
                List<Pair<Integer, Integer>> neighbours = getNeighbours(i, j);
                int countNeighbours = countOccupiedNeighbours(grid, neighbours);
                if(!isBlack) {
                    if(countNeighbours == 2) {
//                        log.info("Flipping {} to black due to neighbours: {}", currentPosition, StreamEx.of(neighbours).filter(grid::contains).toSet());
                        newPositions.add(currentPosition);
                    }
                } else {
                    if(countNeighbours == 0 || countNeighbours > 2) {
//                        log.info("Flipping {} to white due to neighbours: {}", currentPosition, StreamEx.of(neighbours).filter(grid::contains).toSet());
                        // Skip
                    } else {
//                        log.info("Leaving {} as black due to neighbours: {}", currentPosition, StreamEx.of(neighbours).filter(grid::contains).toSet());
                        newPositions.add(currentPosition);
                    }
                }
            }
        }
        return newPositions;
    }

    private int countOccupiedNeighbours(Set<Pair<Integer, Integer>> occupiedPositions, List<Pair<Integer, Integer>> neighbours) {
        int count = 0;
        for (Pair<Integer, Integer> neighbour : neighbours) {
            if(occupiedPositions.contains(neighbour)) {
                count++;
            }
        }
        return count;
    }

    @Override
    protected List<String> convertInput(StreamEx<String> lines) {
        return lines.toList();
    }

    List<String> moves(String line) {
        List<String> directions = Arrays.asList("e", "se", "sw", "w", "nw", "ne");
        List<String> moves = new ArrayList<>();
        while(!line.isEmpty()) {
            for(String direction : directions) {
                if(line.startsWith(direction)) {
                    moves.add(direction);
                    line = line.substring(direction.length());
                }
            }
        }
        return moves;
    }

    Pair<Integer, Integer> getNext(String direction, int x, int y) {
        switch(direction) {
            case "e":
                return Pair.of(x, y - 1);
            case "se":
                return Pair.of(x + 1, x % 2 == 0 ? y - 1 : y);
            case "sw":
                return Pair.of(x + 1, x % 2 == 0 ? y : y + 1);
            case "w":
                return Pair.of(x, y + 1);
            case "nw":
                return Pair.of(x - 1, x % 2 == 0 ? y : y + 1);
            case "ne":
                return Pair.of(x - 1, x % 2 == 0 ? y - 1: y);
            default:
                throw new IllegalStateException(direction);
        }
    }

    public List<Pair<Integer, Integer>> getNeighbours(int x, int y) {
        List<Pair<Integer, Integer>> neighbours = new ArrayList<>();
        neighbours.add(Pair.of(x, y + 1));
        neighbours.add(Pair.of(x, y - 1 ));
        neighbours.add(Pair.of(x - 1, y));
        neighbours.add(Pair.of(x + 1, y));
        if(x % 2 == 0) {
            neighbours.add(Pair.of(x - 1, y - 1));
            neighbours.add(Pair.of(x + 1, y - 1));
        } else {
            neighbours.add(Pair.of(x - 1, y + 1));
            neighbours.add(Pair.of(x + 1, y + 1));
        }
        return neighbours;
    }
}
