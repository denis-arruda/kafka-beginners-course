package com.denisarruda.kafkabasic;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class KafkaBasicsAppTest {
    @Test
    void testMainRuns() {
        KafkaBasicsApp.main(new String[]{});
        assertTrue(true);
    }
}
