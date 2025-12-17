package com.phonecompany.billing;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;

class TelephoneBillCalculatorTest {

    private final TelephoneBillCalculator calculator =
            new TelephoneBillCalculatorImpl();

    @Test
    void shouldCalculateBillCorrectly() {
        String log = """
                420774577453,13-01-2020 18:10:15,13-01-2020 18:12:57
                420776562353,18-02-2020 08:59:20,18-02-2020 09:10:00
                """;

        BigDecimal result = calculator.calculate(log);

        assertEquals(new BigDecimal("1.50"), result);
    }

    @Test
    void shouldCalculateBillFailed() {
        String log = """
                420774577453,13-01-2020 18:10:15,13-01-2020 18:12:57
                420776562353,18-02-2020 08:59:20,18-02-2020 09:10:00
                420776562353,18-03-2020 08:59:20,19-03-2020 09:10:00
                """;

        BigDecimal result = calculator.calculate(log);

        assertNotSame(new BigDecimal("1.50"), result);
    }
}
