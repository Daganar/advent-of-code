package uk.co.techbound.adentofcode.y2020.p21;

import com.google.common.collect.Iterables;
import com.google.common.collect.Sets;
import lombok.extern.log4j.Log4j2;
import one.util.streamex.EntryStream;
import one.util.streamex.StreamEx;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Component;
import uk.co.techbound.adentofcode.AbstractProblemSolver;

import java.util.*;

@Log4j2
@Component
public class Problem21 extends AbstractProblemSolver<List<Pair<List<String>, List<String>>>, Object> {

    @Override
    protected Long partOne(List<Pair<List<String>, List<String>>> input) {
        Map<String, Set<String>> reducedPotentialAllergenIngredients = calculatePotentialAllergenIngredients(input);


        Map<String, String> ingredientToAllergen = findAllergenicIngredients(reducedPotentialAllergenIngredients);

        return StreamEx.of(input).map(Pair::getLeft).flatMap(List::stream).remove(ingredientToAllergen.values()::contains).count();
    }

    @Override
    protected String partTwo(List<Pair<List<String>, List<String>>> input) {
        Map<String, Set<String>> reducedPotentialAllergenIngredients = calculatePotentialAllergenIngredients(input);
        Map<String, String> allergenToIngredient = findAllergenicIngredients(reducedPotentialAllergenIngredients);

        return EntryStream.of(allergenToIngredient).sortedBy(Map.Entry::getKey).values().joining(",");
    }

    private Map<String, String> findAllergenicIngredients(Map<String, Set<String>> reducedPotentialIngredients) {
        while(EntryStream.of(reducedPotentialIngredients).values().mapToInt(Collection::size).max().orElseThrow() > 1) {
            for(Map.Entry<String, Set<String>> entry : reducedPotentialIngredients.entrySet()) {
                if(entry.getValue().size() == 1) {
                    String value = entry.getValue().iterator().next();
                    EntryStream.of(reducedPotentialIngredients)
                        .removeKeys(k -> k.equals(entry.getKey()))
                        .values()
                        .forEach(set -> set.remove(value));
                }
            }
        }
        return EntryStream.of(reducedPotentialIngredients).mapValues(Iterables::getOnlyElement).toMap();
    }

    private Map<String, Set<String>> calculatePotentialAllergenIngredients(List<Pair<List<String>, List<String>>> input) {
        Map<String, Set<List<String>>> allergenToPotentialIngredients = StreamEx.of(input)
            .map(Pair::getRight)
            .flatMap(List::stream)
            .distinct()
            .mapToEntry(allergen -> StreamEx.of(input)
                .filter(line -> line.getRight().contains(allergen))
                .map(Pair::getLeft)
                .toSet()
            ).toMap();

        return EntryStream.of(allergenToPotentialIngredients)
            .<Set<String>>mapValues(foods -> {
                Set<String> intersection = StreamEx.of(foods)
                    .<Set<String>>map(HashSet::new)
                    .reduce(Sets::intersection)
                    .orElseThrow();
                return new HashSet<>(intersection);
            }).toMap();
    }

    @Override
    protected List<Pair<List<String>, List<String>>> convertInput(StreamEx<String> lines) {
        return lines.map(line -> {
            String ingredientsString = StringUtils.substringBefore(line, " (");
            List<String> ingredients = Arrays.asList(ingredientsString.split(" "));
            String allergensString = StringUtils.substringBetween(line, "(contains ", ")");
            List<String> potentialAllergens = Arrays.asList(allergensString.split(", "));
            return Pair.of(ingredients, potentialAllergens);
        }).toList();
    }
}
