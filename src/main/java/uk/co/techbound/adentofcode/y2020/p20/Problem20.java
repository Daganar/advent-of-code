package uk.co.techbound.adentofcode.y2020.p20;

import lombok.*;
import lombok.extern.log4j.Log4j2;
import one.util.streamex.IntStreamEx;
import one.util.streamex.StreamEx;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Component;
import uk.co.techbound.adentofcode.AbstractProblemSolver;

import java.util.*;
import java.util.function.BiConsumer;

@Log4j2
@Component
public class Problem20 extends AbstractProblemSolver<List<Problem20.Tile>, Long> {

    @Override
    protected Long partOne(List<Tile> input) {

        Set<TileNode> nodes = joinAdjacentNodes(input);

        return StreamEx.of(nodes).filter(node -> node.getAdjacentNodes().size() == 2).map(TileNode::getNode).mapToLong(Tile::getId).reduce(1, (l, r) -> l * r);
    }

    private Set<TileNode> joinAdjacentNodes(List<Tile> input) {
        Set<TileNode> nodes = StreamEx.of(input).map(Tile::getNode).toSet();

        Map<Integer, TileNode> sides = new HashMap<>();
        for (TileNode tileNode : nodes) {
            Tile currentTile = tileNode.getNode();
            doSide(sides, tileNode, currentTile.left(), makeAdjacent());
            doSide(sides, tileNode, currentTile.right(), makeAdjacent());
            doSide(sides, tileNode, currentTile.top(), makeAdjacent());
            doSide(sides, tileNode, currentTile.bottom(), makeAdjacent());

            doSide(sides, tileNode, reverse(currentTile.left()), makeAdjacent());
            doSide(sides, tileNode, reverse(currentTile.right()), makeAdjacent());
            doSide(sides, tileNode, reverse(currentTile.top()), makeAdjacent());
            doSide(sides, tileNode, reverse(currentTile.bottom()), makeAdjacent());
        }
        return nodes;
    }

    private BiConsumer<TileNode, TileNode> makeAdjacent() {
        return (a, b) -> {
            a.getAdjacentNodes().add(b);
            b.getAdjacentNodes().add(a);
        };
    }

    private void doSide(Map<Integer, TileNode> sides, TileNode tileNode, char[] side, BiConsumer<TileNode, TileNode> join) {
        int sideHash = Arrays.hashCode(side);
        if (sides.containsKey(sideHash)) {
            TileNode adjacentNode = sides.get(sideHash);
            if(!adjacentNode.equals(tileNode)) {
                join.accept(adjacentNode, tileNode);
                sides.remove(sideHash);
            }
        } else {
            sides.put(sideHash, tileNode);
        }
    }

    @Override
    protected Long partTwo(List<Tile> input) {
        Set<TileNode> tileNodes = joinAdjacentNodes(input);
        List<List<char[][]>> lists = arrangeTiles(tileNodes);
        char[][] image = removeBordersAndStitchTogether(lists);
        Tile imageAsTile = makeTile(image, -1, Set.of());
        Arrays.stream(image)
            .forEach(line -> log.info("{}", line));
        long sum = possibleTilesNodes(Set.of(imageAsTile.getNode()))
            .map(tileNode -> tileNode.getNode().getGrid())
            .mapToInt(grid -> {
                return countSeaMonsters(grid);
            }).sum();
        long monsterHashes = sum * 15;
        long totalHashes = StreamEx.of(image).flatMapToInt(IntStreamEx::of).filter(i -> i == '#').count();
        return totalHashes - monsterHashes;
    }

    private int countSeaMonsters(char[][] grid) {
        int count = 0;
        for(int i = 0; i < grid.length; i++) {
            for(int j = 0; j < grid[i].length; j++) {
                if(isSeaMonsterAtPosition(i, j, grid)) {
                    count++;
                }
            }
        }
        return count;
    }

    private boolean isSeaMonsterAtPosition(int x, int y, char[][] grid) {
        char[][] seaMonster = {
            {' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ','#',' '},
            {'#',' ',' ',' ',' ','#','#',' ',' ',' ',' ','#','#',' ',' ',' ',' ','#','#','#'},
            {' ','#',' ',' ','#',' ',' ','#',' ',' ','#',' ',' ','#',' ',' ','#',' ',' ',' '}
        };
        int max = grid.length;
        if(x + 20 >= max || y + 2 >= max) {
            return false;
        }
        for(int i = 0; i < seaMonster.length; i++) {
            for(int j = 0; j < seaMonster[i].length; j++) {
                if(seaMonster[i][j] == '#') {
                    if(grid[y + i][x + j] != '#') {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private char[][] removeBordersAndStitchTogether(List<List<char[][]>> lists) {
        char[][] image = StreamEx.of(lists)
            .map(line -> StreamEx.of(line).map(this::removeBorder).reduce((l, r) -> {
                int height = l.length;
                int leftWidth = l[0].length;
                int width = leftWidth + r[0].length;
                char[][] chars = new char[height][width];
                for (int i = 0; i < height; i++) {
                    for (int j = 0; j < width; j++) {
                        chars[i][j] = j < leftWidth ? l[i][j] : r[i][j - leftWidth];
                    }
                }
                return chars;
            }))
            .map(Optional::get)
            .reduce((l, r) -> {
                int leftHeight = l.length;
                int height = leftHeight + r.length;
                int width = l[0].length;
                char[][] chars = new char[height][width];
                for (int i = 0; i < height; i++) {
                    for (int j = 0; j < width; j++) {
                        chars[i][j] = i < leftHeight ? l[i][j] : r[i - leftHeight][j];
                    }
                }
                return chars;
            }).get();
        return image;
    }

    private char[][] removeBorder(char[][] grid) {
        int length = grid.length - 2;
        char[][] newGrid = new char[length][length];
        for (int i = 0; i < length; i++) {
            for(int j = 0; j < length; j++) {
                newGrid[i][j] = grid[i + 1][j + 1];
            }
        }
        return newGrid;
    }

    private List<List<char[][]>> arrangeTiles(Set<Problem20.TileNode> tileNodes) {
        int length = (int)Math.sqrt(tileNodes.size());
        Problem20.TileNode cornerNode = StreamEx.of(tileNodes).filter(tileNode -> tileNode.getAdjacentNodes().size() == 2).skip(1).findFirst().get();
        Iterator<Problem20.TileNode> initialIterator = possibleTilesNodes(Collections.singleton(cornerNode)).iterator();
        List<List<Pair<Iterator<Problem20.TileNode>, Problem20.TileNode>>> bigGrid =
            IntStreamEx.range(length).mapToObj(unused -> IntStreamEx.range(length).<Pair<Iterator<Problem20.TileNode>, Problem20.TileNode>>mapToObj(unused2 -> null).toList()).toList();
        bigGrid.get(0).set(0, Pair.of(initialIterator, initialIterator.next()));
        int row = 0;
        int col = 1;
        while(row < length && col < length) {
            log.info("row: {}, col: {}", row, col);
            Pair<Iterator<Problem20.TileNode>, Problem20.TileNode> currentValue = getValueFromPrevious(bigGrid, row, col, length);
            boolean isValid = false;
            Iterator<Problem20.TileNode> iterator = currentValue.getLeft();
            while(!(isValid = checkPosition(bigGrid, currentValue.getRight(),  row, col))) {
                if(!iterator.hasNext()) {
                    break;
                }
                currentValue = Pair.of(iterator, iterator.next());
            }
            if(isValid) {
                bigGrid.get(row).set(col, currentValue);
                log.info("row: {}, col: {}, id: {}", row, col, currentValue.getRight().getNode().getId());
                col++;
                if(col == length) {
                    col = 0;
                    row++;
                }
            } else {
                bigGrid.get(row).set(col, null);
                col--;
                if(col == -1) {
                    col = length - 1;
                    row--;
                }
            }
        }
        return StreamEx.of(bigGrid)
            .map(rowList -> StreamEx.of(rowList).map(pair -> pair.getRight().getNode().getGrid()).toList())
            .toList();
    }

    private boolean checkPosition(List<List<Pair<Iterator<TileNode>, TileNode>>> bigGrid, TileNode currentNode, int row, int col) {
        if(col > 0) {
            TileNode leftNode = bigGrid.get(row).get(col - 1).getRight();
            return Arrays.equals(leftNode.getNode().right(), currentNode.getNode().left());
        }
        TileNode aboveNode = bigGrid.get(row - 1).get(col).getRight();
        return Arrays.equals(aboveNode.getNode().bottom(), currentNode.getNode().top());
    }

    private Pair<Iterator<TileNode>, TileNode> getValueFromPrevious(List<List<Pair<Iterator<TileNode>, TileNode>>> bigGrid, int row, int col, int length) {
        if(col == 0) {
            col = 0;
            row--;
        } else {
            col--;
        }
        Pair<Iterator<TileNode>, TileNode> previous = bigGrid.get(row).get(col);
        Iterator<TileNode> tileNodes = possibleTilesNodes(previous.getRight().getAdjacentNodes()).iterator();
        return Pair.of(tileNodes, tileNodes.next());
    }

    private StreamEx<TileNode> possibleTilesNodes(Set<TileNode> nodes) {
        return StreamEx.of(nodes)
            .map(TileNode::getNode)
            .flatMap(node -> {
                char[][] grid = node.getGrid();
                return StreamEx.iterate(grid, this::rotateClockwise).limit(4)
                        .append(
                            StreamEx.iterate(flip(grid), this::rotateClockwise).limit(4)
                        ).map(newGrid -> makeTile(newGrid, node.getId(), node.getNode().getAdjacentNodes()).getNode());
                }
            );
    }

    private char[][] rotateClockwise(char[][] grid) {
        int size = grid.length;
        char[][] newGrid = new char[size][size];

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                newGrid[i][j] = grid[size - j - 1][i];
            }
        }
//        IntStreamEx.range(size)
//            .mapToObj(i -> StreamEx.of(grid, newGrid).map(g -> g[i]).map(Arrays::toString).joining(" -> "))
//            .forEach(log::info);

        return newGrid;
    }



    @Override
    protected List<Tile> convertInput(StreamEx<String> lines) {
        List<Tile> tileList = lines.groupRuns((l, r) -> !r.trim().isEmpty())
            .map(StreamEx::of)
            .map(tileStream -> tileStream.remove(String::isBlank))
            .flatMap(tileStream -> tileStream.headTail((head, tail) -> {
                int tileId = Integer.parseInt(StringUtils.substringBetween(head, "Tile ", ":"));
                char[][] grid = tail.map(line -> IntStreamEx.of(line.chars()).toCharArray())
                    .toArray(new char[0][]);
                Tile tile = makeTile(grid, tileId, Set.of());
                return StreamEx.of(tile);
            }))
            .toList();
        return tileList;
    }

    private Tile makeTile(char[][] grid, int id, Set<TileNode> adjacentNodes) {
        Tile tile = new Tile(id, grid, null);
        TileNode node = TileNode.builder().node(tile).adjacentNodes(new HashSet<>(adjacentNodes)).build();
        tile.setNode(node);
        return tile;
    }

    public char[][] flip(char[][] grid) {
        return StreamEx.of(grid).map(this::reverse).toArray(char[].class);
    }

    public char[] reverse(char[] chars) {
        char[] copy = ArrayUtils.clone(chars);
        ArrayUtils.reverse(copy);
        return copy;
    }

    @Data
    @EqualsAndHashCode(onlyExplicitlyIncluded = true)
    @Builder(toBuilder = true)
    public static class TileNode {
        @EqualsAndHashCode.Include
        Tile node;
        Set<TileNode> adjacentNodes = new HashSet<>();

        @Override
        public String toString() {
            return "node=" + node.getId() + ", adjacentNodes=(" + StreamEx.of(adjacentNodes).map(TileNode::getNode).map(Tile::getId).joining(",") + ")";
        }
    }

    public static char[] getLeftSide(char[][] grid) {
        return getColumn(grid, 0);
    }

    public static char[] getRightSide(char[][] grid) {
        return getColumn(grid, grid.length -1);
    }

    public static char[] getColumn(char[][] grid, int column) {
        char[] chars = new char[grid.length];
        for (int i = 0; i < chars.length; i++) {
            chars[i] = grid[i][column];
        }
        return chars;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @EqualsAndHashCode(onlyExplicitlyIncluded = true)
    @ToString(exclude = "node")
    public static class Tile {
        @EqualsAndHashCode.Include
        int id;
        char[][] grid;
        TileNode node;

        char[] top() {
            return grid[0];
        }

        char[] bottom() {
            return grid[grid.length - 1];
        }

        char[] left() {
            return getLeftSide(grid);
        }

        char[] right() {
            return getRightSide(grid);
        }

    }

}
