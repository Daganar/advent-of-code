package uk.co.techbound.adentofcode.y2020.p14;

import lombok.extern.log4j.Log4j2;
import one.util.streamex.LongStreamEx;
import one.util.streamex.StreamEx;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import uk.co.techbound.adentofcode.AbstractProblemSolver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Log4j2
@Component
public class Problem14 extends AbstractProblemSolver<StreamEx<String>, Long> {

    @Override
    protected Long partOne(StreamEx<String> instructions) {
        Map<Long, Long> memory = new HashMap<>();
        long andMask = 0;
        long orMask = 0;
        for(String line : instructions) {
            if(line.contains("mask")) {
                String maskString = StringUtils.substringAfter(line, " = ");
                andMask = Long.parseLong(maskString.replace('X', '1'), 2);
                orMask = Long.parseLong(maskString.replace('X', '0'), 2);
            } else {
                long address = Long.parseLong(StringUtils.substringBetween(line, "[", "]"));
                long value = Long.parseLong(StringUtils.substringAfter(line, " = "));
                long maskedValue = (value | orMask) & andMask;
                memory.put(address, maskedValue);
            }
        }

        return LongStreamEx.of(memory.values()).sum();
    }

    @Override
    protected Long partTwo(StreamEx<String> instructions) {
        Map<Long, Long> memory = new HashMap<>();
        String mask = "";
        for(String line : instructions) {
            if(line.contains("mask")) {
                mask = StringUtils.substringAfter(line, " = ");
            } else {
                long address = Long.parseLong(StringUtils.substringBetween(line, "[", "]"));
                long value = Long.parseLong(StringUtils.substringAfter(line, " = "));
                List<Long> addresses = getAddresses(address, mask);
                addresses.forEach(a -> memory.put(a, value));
            }
        }

        return LongStreamEx.of(memory.values()).sum();
    }

    private List<Long> getAddresses(long address, String mask) {
        String paddedAddress = StringUtils.leftPad(Long.toBinaryString(address), 36, '0');
        String maskedAddress = "";
        for(int i = 0; i < 36; i++) {
            char charAt = mask.charAt(i);
            if(charAt == 'X' || charAt == '1') {
                maskedAddress += charAt;
            } else {
                maskedAddress += paddedAddress.charAt(i);
            }
        }
        ArrayList<Long> addresses = new ArrayList<>();
        addAddresses(addresses, maskedAddress);
        return addresses;
    }

    private void addAddresses(List<Long> longs, String maskedString) {
        int x = maskedString.lastIndexOf('X');
        if(x == -1 ) {
            long address = Long.parseLong(maskedString, 2);
            longs.add(address);
            return;
        }
        addAddresses(longs, setIndex(maskedString, x, '0'));
        addAddresses(longs, setIndex(maskedString, x, '1'));
    }

    private String setIndex(String s, int index, char newValue) {
        StringBuilder stringBuilder = new StringBuilder(s);
        stringBuilder.setCharAt(index, newValue);
        return stringBuilder.toString();
    }

    @Override
    protected StreamEx<String> convertInput(StreamEx<String> lines) {
        return lines;
    }
}
