package com.phonecompany.billing;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class TelephoneBillCalculatorImpl implements TelephoneBillCalculator {

    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

    private static final LocalTime DAY_START = LocalTime.of(8, 0);
    private static final LocalTime DAY_END = LocalTime.of(16, 0);

    @Override
    public BigDecimal calculate(String phoneLog) {
        if (phoneLog == null || phoneLog.isBlank()) {
            return BigDecimal.ZERO;
        }

        List<Call> calls = parseCalls(phoneLog);
        String promoNumber = findPromoNumber(calls);

        BigDecimal total = BigDecimal.ZERO;

        for (Call call : calls) {
            if (call.phoneNumber.equals(promoNumber)) {
                continue; // promo – zdarma
            }
            total = total.add(calculateCallPrice(call));
        }

        return total.setScale(2, RoundingMode.HALF_UP);
    }

    // ---------------------- interní logika ----------------------

    private List<Call> parseCalls(String phoneLog) {
        return Arrays.stream(phoneLog.split("\\R"))
                .map(line -> {
                    String[] parts = line.split(",");
                    return new Call(
                            parts[0],
                            LocalDateTime.parse(parts[1], FORMATTER),
                            LocalDateTime.parse(parts[2], FORMATTER)
                    );
                })
                .collect(Collectors.toList());
    }

    private String findPromoNumber(List<Call> calls) {
        Map<String, Long> frequency = calls.stream()
                .collect(Collectors.groupingBy(c -> c.phoneNumber, Collectors.counting()));

        long maxCount = frequency.values().stream().mapToLong(Long::longValue).max().orElse(0);

        return frequency.entrySet().stream()
                .filter(e -> e.getValue() == maxCount)
                .map(Map.Entry::getKey)
                .max(Comparator.comparingLong(Long::parseLong))
                .orElse(null);
    }

    private BigDecimal calculateCallPrice(Call call) {
        BigDecimal price = BigDecimal.ZERO;

        LocalDateTime minuteStart = call.start;
        int minuteIndex = 1;

        while (minuteStart.isBefore(call.end)) {
            BigDecimal minuteRate;

            if (minuteIndex > 5) {
                minuteRate = BigDecimal.valueOf(0.20);
            } else {
                minuteRate = isDayRate(minuteStart.toLocalTime())
                        ? BigDecimal.ONE
                        : BigDecimal.valueOf(0.50);
            }

            price = price.add(minuteRate);
            minuteStart = minuteStart.plusMinutes(1);
            minuteIndex++;
        }

        return price;
    }

    private boolean isDayRate(LocalTime time) {
        return !time.isBefore(DAY_START) && time.isBefore(DAY_END);
    }

    // ---------------------- model ----------------------

    private static class Call {
        final String phoneNumber;
        final LocalDateTime start;
        final LocalDateTime end;

        Call(String phoneNumber, LocalDateTime start, LocalDateTime end) {
            this.phoneNumber = phoneNumber;
            this.start = start;
            this.end = end;
        }
    }
}
