package uk.co.techbound.adentofcode.y2021.p4;

import one.util.streamex.EntryStream;
import one.util.streamex.StreamEx;

import java.util.List;

public class BingoBoard {
    List<Integer> numbers;
    List<Boolean> calledNumbers;

    public BingoBoard(List<Integer> numbers) {
        if(numbers.size() != 25) {
            throw new IllegalArgumentException("Invalid bingo board: " + numbers);
        }
        this.numbers = numbers;
        this.calledNumbers = StreamEx.generate(() -> false).limit(25).toList();
    }

    public boolean markNumber(int number) {
        int indexOf = numbers.indexOf(number);
        if(indexOf == -1) {
            return false;
        }
        calledNumbers.set(indexOf, true);
        return true;
    }

    public boolean hasLine() {
        for(int i = 0; i < 25; i+=5) {
            boolean allTrue = true;
            for(int j = 0; j < 5; j++) {
                allTrue &= calledNumbers.get(i + j);
            }
            if(allTrue) {
                return true;
            }
        }
        for(int j = 0; j < 5; j++) {
            boolean allTrue = true;
            for(int i = 0; i < 25; i+=5) {
                allTrue &= calledNumbers.get(i + j);
            }
            if(allTrue) {
                return true;
            }
        }
        return false;
    }

    public List<Integer> unmarkedNumbers() {
        return EntryStream.of(calledNumbers)
            .mapKeys(numbers::get)
            .filterValues(value -> !value)
            .keys()
            .toList();
    }


}
