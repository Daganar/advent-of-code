package uk.co.techbound.adentofcode.y2020.p23;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import one.util.streamex.IntStreamEx;
import one.util.streamex.StreamEx;
import org.springframework.stereotype.Component;
import uk.co.techbound.adentofcode.AbstractProblemSolver;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

@Log4j2
@Component
public class Problem23 extends AbstractProblemSolver<List<Integer>, Object> {

    @Override
    protected Long partOne(List<Integer> input) {
        Node currentNode = makeNodes(input);
        Node lastNode = getLast(currentNode);
        Node additionalNodes = makeNodes(IntStreamEx.rangeClosed(10, 1_000_000).boxed().toList());
        insertAfter2(lastNode, additionalNodes);
        Map<Integer, Node> nodeMap = makeNodeMap(currentNode);
        for(int move = 1; move <= 10_000_000; move++) {
            Node pickedUp = removeNextThree(currentNode);
            int nextLabel = getNextLabel(currentNode, pickedUp, 1_000_000);
            Node destination = nodeMap.get(nextLabel);
            insertAfter(destination, pickedUp);
            currentNode = currentNode.getNext();
        }

        Node nodeOne = nodeMap.get(1);
        return nodeOne.getNext().getLabel() * (long) nodeOne.getNext().getNext().getLabel();
    }

    private Map<Integer, Node> makeNodeMap(Node currentNode) {
        return StreamEx.iterate(currentNode, Node::getNext).takeWhileInclusive(node -> node.getNext() != currentNode).toMap(Node::getLabel, Function.identity());
    }

    private Node getLast(Node currentNode) {
        return StreamEx.iterate(currentNode, Node::getNext).findFirst(node -> node.getNext() == currentNode).get();
    }

    private void insertAfter2(Node destination, Node pickedUp) {
        Node A = destination;
        Node B = pickedUp;
        Node C = StreamEx.iterate(pickedUp, Node::getNext).findFirst(node -> node.getNext() == pickedUp).get();
        Node D = destination.getNext();
        A.setNext(B);
        C.setNext(D);
    }

    private void insertAfter(Node destination, Node pickedUp) {
        Node temp = destination.getNext();
        destination.setNext(pickedUp);
        pickedUp.getNext().getNext().setNext(temp);
    }

    private String printFromCurrent(Node currentNode) {
        return StreamEx.iterate(currentNode, Node::getNext)
            .takeWhileInclusive(node -> node.getNext() != currentNode)
            .map(Node::getLabel)
            .joining();
    }

    private Node makeNodes(List<Integer> input) {
        int label = input.get(0);
        Node currentNode = new Node(null, label);
        Node firstNode = currentNode;
        for(int i = 1; i < input.size(); i++) {
            Node next = new Node(null, input.get(i));
            currentNode.setNext(next);
            currentNode = next;
        }
        currentNode.setNext(firstNode);
        return firstNode;
    }

    private int getNextLabel(Node input, Node pickedUp, int maxLabel) {
        Set<Integer> temp = Set.of(pickedUp.label, pickedUp.next.label, pickedUp.next.next.label);
        int currentLabel = input.label;
        do {
            currentLabel--;
            if(currentLabel == 0) {
                currentLabel = maxLabel;
            }
        } while(temp.contains(currentLabel));
        return currentLabel;
    }

    public Node removeNextThree(Node currentNode) {
        Node nextNode = currentNode.next;
        currentNode.setNext(nextNode.next.next.next);
        return nextNode;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Node {
        Node next;
        int label;

        @Override
        public String toString() {
            return "label=" + label + ", next=" + next.label;
        }
    }


    @Override
    protected String partTwo(List<Integer> input) {
        return null;
    }

    @Override
    protected List<Integer> convertInput(StreamEx<String> lines) {
        return lines.flatMapToInt(String::chars).mapToObj(Character::toString).map(Integer::valueOf).toList();
    }
}
