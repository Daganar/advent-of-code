package uk.co.techbound.adentofcode.y2020.p22;

import lombok.extern.log4j.Log4j2;
import one.util.streamex.EntryStream;
import one.util.streamex.LongStreamEx;
import one.util.streamex.StreamEx;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Component;
import uk.co.techbound.adentofcode.AbstractProblemSolver;

import java.util.*;

@Log4j2
@Component
public class Problem22 extends AbstractProblemSolver<Pair<List<Integer>, List<Integer>>, Long> {

    @Override
    protected Long partOne(Pair<List<Integer>, List<Integer>> input) {
        Queue<Integer> playerOneDeck = new LinkedList<>(input.getLeft());
        Queue<Integer> playerTwoDeck = new LinkedList<>(input.getRight());
        Queue<Integer> winner = playCombat(playerOneDeck, playerTwoDeck);
        Long[] cardValues = LongStreamEx.rangeClosed(winner.size(), 1, -1).boxed().toArray(Long.class);
        return EntryStream.zip(cardValues, winner.toArray(new Integer[0])).mapKeyValue((l, r) -> l * r).mapToLong(Long::valueOf).sum();
    }

    private Queue<Integer> playCombat(Queue<Integer> playerOneDeck, Queue<Integer> playerTwoDeck) {
        while(!playerOneDeck.isEmpty() && !playerTwoDeck.isEmpty()) {
            Integer playerOne = playerOneDeck.poll();
            Integer playerTwo = playerTwoDeck.poll();
            if(playerOne > playerTwo) {
                playerOneDeck.add(playerOne);
                playerOneDeck.add(playerTwo);
            } else {
                playerTwoDeck.add(playerTwo);
                playerTwoDeck.add(playerOne);
            }
        }
        return playerOneDeck.isEmpty() ? playerTwoDeck : playerOneDeck;
    }

    @Override
    protected Long partTwo(Pair<List<Integer>, List<Integer>> input) {
        LinkedList<Integer> playerOneDeck = new LinkedList<>(input.getLeft());
        LinkedList<Integer> playerTwoDeck = new LinkedList<>(input.getRight());
        LinkedList<Integer> winner = playRecursiveCombat(playerOneDeck, playerTwoDeck, 1) == 1 ? playerOneDeck : playerTwoDeck;
        Long[] cardValues = LongStreamEx.rangeClosed(winner.size(), 1, -1).boxed().toArray(Long.class);
        return EntryStream.zip(cardValues, winner.toArray(new Integer[0])).mapKeyValue((l, r) -> l * r).mapToLong(Long::valueOf).sum();
    }

    private int playRecursiveCombat(LinkedList<Integer> playerOneDeck, LinkedList<Integer> playerTwoDeck, int game) {
        log.info("=== Game {} ===", game);
        Set<Integer> previouslySeenPlayerOneDeckHashes = new HashSet<>();
        Set<Integer> previouslySeenPlayerTwoDeckHashes = new HashSet<>();
        int round = 1;
        while(!playerOneDeck.isEmpty() && !playerTwoDeck.isEmpty()) {
            log.info("-- Round {} (Game {}) --", round, game);
            log.info("Player 1's deck: {}", playerOneDeck);
            log.info("Player 2's deck: {}", playerTwoDeck);
            if(!previouslySeenPlayerOneDeckHashes.add(playerOneDeck.hashCode()) || !previouslySeenPlayerTwoDeckHashes.add(playerTwoDeck.hashCode())) {
                log.info("The winner of game {} is player {}!", game, 1);
                return 1;
            }
            Integer playerOne = playerOneDeck.poll();
            Integer playerTwo = playerTwoDeck.poll();
            log.info("Player 1 plays: {}", playerOne);
            log.info("Player 2 plays: {}", playerTwo);
            final int winner;
            if(playerOneDeck.size() >= playerOne && playerTwoDeck.size() >= playerTwo) {
                log.info("Playing a sub-game to determine the winner...");
                winner = playRecursiveCombat(new LinkedList<>(playerOneDeck.subList(0, playerOne)), new LinkedList<>(playerTwoDeck.subList(0, playerTwo)), game + 1);
                log.info("");
                log.info("...anyway, back to game {}.", game);
            } else {
                winner = playerOne > playerTwo ? 1 : 2;
            }
            log.info("Player {} wins round {} of game {}", winner, round, game);
            log.info("");
            if(winner == 1) {
                playerOneDeck.add(playerOne);
                playerOneDeck.add(playerTwo);
            } else {
                playerTwoDeck.add(playerTwo);
                playerTwoDeck.add(playerOne);
            }
            round++;
        }
        int winner = playerOneDeck.isEmpty() ? 2 : 1;
        log.info("The winner of game {} is player {}!", game, winner);
        log.info("");
        return winner;
    }

    @Override
    protected Pair<List<Integer>, List<Integer>> convertInput(StreamEx<String> lines) {
        return lines.groupRuns((l, r) -> !r.trim().isEmpty())
            .map(deck -> StreamEx.of(deck).remove(String::isBlank).skip(1).map(Integer::valueOf).toList())
            .toListAndThen(bothDecks -> Pair.of(bothDecks.get(0), bothDecks.get(1)));
    }
}
