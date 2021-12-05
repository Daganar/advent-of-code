package uk.co.techbound.adentofcode.y2020.p18;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class Problem18Test {

    @Test
    public void indexOfClosingBraces() {
        Assertions.assertEquals(6, Problem18.indexOfClosingBrace("(1 + 1)", 0));
        Assertions.assertEquals(6, Problem18.indexOfClosingBrace("(1 + 1) * (1 + 1)", 0));
        Assertions.assertEquals(8, Problem18.indexOfClosingBrace("((1 + 1)) * (1 + 1)", 0));
        Assertions.assertEquals(7, Problem18.indexOfClosingBrace("((1 + 1)) * (1 + 1)", 1));
    }

    @Test
    public void partOneFlatten() {
        Problem18 problem18 = new Problem18();
        Assertions.assertEquals(26, problem18.partOneFlatten(stripSpaces("2 * 3 + (4 * 5)")));
        Assertions.assertEquals(437, problem18.partOneFlatten(stripSpaces("5 + (8 * 3 + 9 + 3 * 4 * 3)")));
        Assertions.assertEquals(12240, problem18.partOneFlatten(stripSpaces("5 * 9 * (7 * 3 * 3 + 9 * 3 + (8 + 6 * 4))")));
        Assertions.assertEquals(13632, problem18.partOneFlatten(stripSpaces("((2 + 4 * 9) * (6 + 9 * 8 + 6) + 6) + 2 + 4 * 2")));
    }

    private String stripSpaces(String s) {
        return s.replace(" ", "");
    }

    @Test
    public void flatten() {
        Problem18 problem18 = new Problem18();
        Assertions.assertEquals(46, problem18.flatten("2 * 3 + (4 * 5)"));
        Assertions.assertEquals(1445, problem18.flatten("5 + (8 * 3 + 9 + 3 * 4 * 3)"));
        Assertions.assertEquals(669060, problem18.flatten("5 * 9 * (7 * 3 * 3 + 9 * 3 + (8 + 6 * 4))"));
        Assertions.assertEquals(23340, problem18.flatten("((2 + 4 * 9) * (6 + 9 * 8 + 6) + 6) + 2 + 4 * 2"));
        Assertions.assertEquals(51, problem18.flatten("1 + (2 * 3) + (4 * (5 + 6))"));
    }



}