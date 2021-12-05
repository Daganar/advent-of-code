package uk.co.techbound.adentofcode.y2020.p18;

import lombok.extern.log4j.Log4j2;
import one.util.streamex.EntryStream;
import one.util.streamex.StreamEx;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Component;
import uk.co.techbound.adentofcode.AbstractProblemSolver;

import java.util.Map;

@Log4j2
@Component
public class Problem18 extends AbstractProblemSolver<StreamEx<String>, Long> {

    @Override
    protected Long partOne(StreamEx<String> input) {
        return input
            .mapToLong(this::partOneFlatten)
            .sum();
    }

    @Override
    protected Long partTwo(StreamEx<String> input) {
        return input
            .mapToLong(this::flatten)
            .sum();
    }

    public long partOneFlatten(String input) {
        if(input.contains("(")) {
            int start = input.indexOf("(");
            int end = indexOfClosingBrace(input, start);
            String sub = input.substring(start + 1, end);
            String next = input.replace("(" + sub + ")", "" + partOneFlatten(sub));
            return partOneFlatten(next);
        }
        return partOneCalculate(input);
    }

    public long partOneCalculate(String input) {
        return EntryStream.zip(input.split("\\d+"), input.split("[+*]"))
            .foldLeft((left, right) -> {
                if(right.getKey().equals("+")) {
                    return Pair.of(right.getKey(), add(left.getValue(), right.getValue()));
                } else {
                    return Pair.of(right.getKey(), multiply(left.getValue(), right.getValue()));
                }
            })
            .map(Map.Entry::getValue)
            .map(Long::parseLong)
            .get();
    }

    public String add(String left, String right) {
        return String.valueOf(Long.parseLong(left) + Long.parseLong(right));
    }

    public String multiply(String left, String right) {
        return String.valueOf(Long.parseLong(left) * Long.parseLong(right));
    }

    public static int indexOfClosingBrace(String input, int start) {
        char[] inputChars = input.toCharArray();
        int count = 1;
        for(int i = start + 1; i < inputChars.length; i++) {
            if(inputChars[i] == '(') {
                count++;
            } else if(inputChars[i] == ')') {
                count--;
            }
            if(count == 0) {
                return i;
            }
        }
        throw new IllegalStateException("Mismatched braces in input: " + input);
    }

    public long flatten(String input) {
        if(input.contains("(")) {
            int start = input.indexOf("(");
            int end = indexOfClosingBrace(input, start);
            String sub = input.substring(start + 1, end);
            String next = input.replace("(" + sub + ")", "" + flatten(sub));
            return flatten(next);
        }
        return calculate(input);
    }

    public long calculate(String input) {
        return StreamEx.of(input.split("\\*"))
            .map(adds -> adds.split("\\+"))
            .mapToLong(adds -> StreamEx.of(adds).mapToLong(Long::parseLong).sum())
            .reduce(1, (l, r) -> l * r);
    }

    @Override
    protected StreamEx<String> convertInput(StreamEx<String> lines) {
        return lines.map(line -> line.replace(" ", ""));
    }

}
