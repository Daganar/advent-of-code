package uk.co.techbound.adentofcode.y2021.p4;

import lombok.extern.log4j.Log4j2;
import one.util.streamex.IntStreamEx;
import one.util.streamex.StreamEx;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Component;
import uk.co.techbound.adentofcode.AbstractProblemSolver;

import java.util.Iterator;
import java.util.List;

import static uk.co.techbound.adentofcode.utils.StreamUtils.groupRuns;

@Log4j2
@Component
public class Problem4 extends AbstractProblemSolver<Pair<List<Integer>, List<BingoBoard>>, Integer> {


    @Override
    protected Integer partOne(Pair<List<Integer>, List<BingoBoard>> input) {
        List<Integer> calledNumbers = input.getLeft();
        List<BingoBoard> bingoBoards = input.getRight();

        for(Integer number : calledNumbers) {
            for(BingoBoard bingoBoard : bingoBoards) {
                if(bingoBoard.markNumber(number) && bingoBoard.hasLine()) {
                    List<Integer> integers = bingoBoard.unmarkedNumbers();
                    return IntStreamEx.of(integers).sum() * number;
                }
            }
        }
        return null;
    }

    @Override
    protected Integer partTwo(Pair<List<Integer>, List<BingoBoard>> input) {
        List<Integer> calledNumbers = input.getLeft();
        List<BingoBoard> bingoBoards = input.getRight();

        for(Integer number : calledNumbers) {
            for (Iterator<BingoBoard> iterator = bingoBoards.iterator(); iterator.hasNext(); ) {
                BingoBoard bingoBoard = iterator.next();
                if (bingoBoard.markNumber(number) && bingoBoard.hasLine()) {
                    if (bingoBoards.size() > 1) {
                        iterator.remove();
                        continue;
                    }
                    List<Integer> integers = bingoBoard.unmarkedNumbers();
                    return IntStreamEx.of(integers).sum() * number;
                }
            }
        }
        return null;
    }

    @Override
    protected Pair<List<Integer>, List<BingoBoard>> convertInput(StreamEx<String> lines) {
        List<String> input = lines.toList();
        List<Integer> calledNumbers = StreamEx.of(input.get(0).split(",")).map(Integer::parseInt).toList();

        List<BingoBoard> bingoBoards = StreamEx.of(StreamEx.of(input).skip(1).joining(" ").split("\\s+"))
            .remove(String::isBlank)
            .map(Integer::parseInt)
            .chain(groupRuns(25))
            .map(BingoBoard::new)
            .toList();

        return Pair.of(calledNumbers, bingoBoards);
    }

}
