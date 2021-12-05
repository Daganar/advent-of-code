package uk.co.techbound.adentofcode.y2020.p20;

import one.util.streamex.StreamEx;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

class Problem20Test {
    Problem20 problem20 = new Problem20();

    @Test
    public void findInitialAlignment() {


        Problem20.TileNode right = makeTile(2311,
            "..##.#..#.",
            "##..#.....",
            "#...##..#.",
            "####.#...#",
            "##.##.###.",
            "##...#.###",
            ".#.#.#..##",
            "..#....#..",
            "###...#.#.",
            "..###..###");
        Problem20.TileNode bottom = makeTile(2729,
            "...#.#.#.#",
            "####.#....",
            "..#.#.....",
            "....#..#.#",
            ".##..##.#.",
            ".#.####...",
            "####.#.#..",
            "##.####...",
            "##..#.##..",
            "#.##...##."
        );
        Problem20.TileNode initialTile = makeTile(1951,
            "#.##...##.",
            "#.####...#",
            ".....#..##",
            "#...######",
            ".##.#....#",
            ".###.#####",
            "###.##.##.",
            ".###....#.",
            "..#.#..#.#",
            "#...##.#.."
        );

        initialTile.setAdjacentNodes(Set.of(right, bottom));

//        problem20.findInitialAlignment2(initialTile.getNode());

        List<String> expected = List.of(
            "#...##.#..",
            "..#.#..#.#",
            ".###....#.",
            "###.##.##.",
            ".###.#####",
            ".##.#....#",
            "#...######",
            ".....#..##",
            "#.####...#",
            "#.##...##."
        );

        char[][] expectedGrid = StreamEx.of(expected).map(String::toCharArray).toArray(char[].class);

        Assertions.assertTrue(Arrays.deepEquals(expectedGrid, initialTile.getNode().getGrid()));
    }

    private Problem20.TileNode makeTile(int id, String... lines) {
        char[][] grid = StreamEx.of(lines).map(String::toCharArray).toArray(char[].class);
        Problem20.TileNode tileNode = new Problem20.TileNode(null, null);
        Problem20.Tile initialTile = new Problem20.Tile(id, grid, tileNode);
        tileNode.setNode(initialTile);
        return tileNode;
    }

}