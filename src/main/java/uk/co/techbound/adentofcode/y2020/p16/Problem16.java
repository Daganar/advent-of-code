package uk.co.techbound.adentofcode.y2020.p16;

import com.google.common.collect.Range;
import lombok.Value;
import lombok.extern.log4j.Log4j2;
import one.util.streamex.EntryStream;
import one.util.streamex.IntStreamEx;
import one.util.streamex.StreamEx;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Triple;
import org.springframework.stereotype.Component;
import uk.co.techbound.adentofcode.AbstractProblemSolver;
import uk.co.techbound.adentofcode.utils.MathFunctions;

import java.util.*;

@Log4j2
@Component
public class Problem16 extends AbstractProblemSolver<Triple<List<Problem16.Rule>, Problem16.Ticket, List<Problem16.Ticket>>, Long> {

    @Override
    protected Long partOne(Triple<List<Rule>, Ticket, List<Ticket>> input) {
        return StreamEx.of(input.getRight())
            .map(Ticket::getFields)
            .flatMap(List::stream)
            .remove(v -> input.getLeft().stream().anyMatch(r -> r.withinEitherRange(v)))
            .mapToLong(Long::longValue)
            .sum();
    }

    @Override
    protected Long partTwo(Triple<List<Rule>, Ticket, List<Ticket>> input) {
        List<Rule> rules = input.getLeft();
        List<Ticket> validTickets = getValidTickets(input.getRight(), rules);
        validTickets.add(input.getMiddle());

        List<Rule> validRulesOrder = getValidRulesOrder(rules, validTickets);

        return EntryStream.of(validRulesOrder)
            .filterValues(r -> r.getName().startsWith("departure"))
            .keys()
            .mapToLong(k -> input.getMiddle().getFields().get(k))
            .chain(MathFunctions::product);
    }

    private List<Rule> getValidRulesOrder(List<Rule> rules, List<Ticket> validTickets) {
        List<List<Rule>> potentialRulesInFieldOrder = IntStreamEx.range(rules.size()).mapToObj(i -> (List<Rule>)new ArrayList<Rule>()).toList();
        for(int ruleIndex = 0; ruleIndex < rules.size(); ruleIndex++) {
            Rule rule = rules.get(ruleIndex);
            for(int fieldIndex = 0; fieldIndex < rules.size(); fieldIndex++) {
                boolean validForField = true;
                for(Ticket ticket : validTickets) {
                    Long field = ticket.getFields().get(fieldIndex);
                    if(!rule.withinEitherRange(field)) {
                        validForField = false;
                        break;
                    }
                }
                if(validForField) {
                    potentialRulesInFieldOrder.get(fieldIndex).add(rule);
                }
            }
        }
        Set<Rule> singleRules = new HashSet<>();
        while(true) {
            Optional<Rule> singleRule = StreamEx.of(potentialRulesInFieldOrder).filter(list -> list.size() == 1).flatMap(List::stream).remove(singleRules::contains).findAny();
            if(singleRule.isEmpty()) {
                break;
            }
            Rule rule = singleRule.get();
            potentialRulesInFieldOrder.forEach(list -> {
                if(list.size() > 1) {
                    list.remove(rule);
                }
                singleRules.add(rule);
            });
        }

        return StreamEx.of(potentialRulesInFieldOrder).map(list -> list.iterator().next()).toList();
    }

    private List<Ticket> getValidTickets(List<Ticket> allTickets, List<Rule> rules) {
        return StreamEx.of(allTickets)
            .filter(ticket -> {
                List<List<Rule>> rulesThatSatisfyTicket = StreamEx.of(ticket.getFields())
                    .map(field -> StreamEx.of(rules).filter(r -> r.withinEitherRange(field)).toList())
                    .remove(List::isEmpty)
                    .toList();
                return rulesThatSatisfyTicket.size() == rules.size() &&
                    StreamEx.of(rulesThatSatisfyTicket).flatMap(List::stream).distinct().count() == rules.size();
            })
            .toList();
    }

    @Override
    protected Triple<List<Rule>, Ticket, List<Ticket>> convertInput(StreamEx<String> lines) {
        List<List<String>> input = lines.groupRuns((l, r) -> !r.trim().isEmpty()).toList();
        List<Rule> ticketRules = StreamEx.of(input.get(0))
            .map(line -> {
                String name = StringUtils.substringBefore(line, ":");
                List<Range<Long>> bounds = StreamEx.of(StringUtils.substringAfter(line, ": "))
                    .flatArray(rules -> rules.split(" or "))
                    .map(rules -> rules.split("-"))
                    .map(rules -> StreamEx.of(rules).map(Long::valueOf).toList())
                    .map(rules -> Range.closed(rules.get(0), rules.get(1)))
                    .toList();
                return new Rule(name, bounds.get(0), bounds.get(1));
            }).toList();
        Ticket myTicket = StreamEx.of(input.get(1))
            .skip(2)
            .flatArray(line -> line.split(","))
            .map(Long::valueOf)
            .toListAndThen(Ticket::new);
        List<Ticket> otherTickets = StreamEx.of(input.get(2))
            .skip(2)
            .map(line -> line.split(","))
            .map(split -> StreamEx.of(split).map(Long::valueOf).toList())
            .map(Ticket::new)
            .toList();
        return Triple.of(ticketRules, myTicket, otherTickets);
    }

    @Value
    public static class Ticket {
        List<Long> fields;
    }

    @Value
    public static class Rule {
        String name;
        Range<Long> lowerRange;
        Range<Long> upperRange;

        boolean withinEitherRange(long value) {
            return lowerRange.contains(value) || upperRange.contains(value);
        }
    }
}
