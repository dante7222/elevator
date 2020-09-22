package com.introlab_systems.utils;

import org.junit.jupiter.api.RepeatedTest;

import static org.assertj.core.api.Assertions.assertThat;

class UtilsTest {

    @RepeatedTest(20)
    void generateAmount() {
        assertThat(Utils.generateAmount(0, 10))
                .isGreaterThanOrEqualTo(0)
                .isLessThanOrEqualTo(10);
    }

    @RepeatedTest(20)
    void generateAmountExceptS() {
        assertThat(Utils.generateAmountExcept(10,2))
                .isGreaterThan(0)
                .isLessThanOrEqualTo(10)
                .isNotEqualTo(2);

    }
}
