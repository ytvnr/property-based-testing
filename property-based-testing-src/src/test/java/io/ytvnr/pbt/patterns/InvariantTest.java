/**
 * Copyright (C) 2015 The Gravitee team (http://gravitee.io)
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.ytvnr.pbt.patterns;

import net.jqwik.api.Assume;
import net.jqwik.api.Disabled;
import net.jqwik.api.ForAll;
import net.jqwik.api.Group;
import net.jqwik.api.Label;
import net.jqwik.api.Property;
import net.jqwik.api.constraints.IntRange;

import java.time.LocalDate;

/**
 * @author Yann TAVERNIER (yann.tavernier at graviteesource.com)
 * @author GraviteeSource Team
 */
public class InvariantTest {

    @Property
    @Label("If the day is 31 and month 12, then it should systematically be new year's eve")
    public boolean invariant(@ForAll int anyYear) {
        return Invariant.isNewYearEve(LocalDate.of(anyYear, 12, 31));
    }

    @Property
    @Label("If the day is not 31 and month 12, then it should not be new year's eve")
    public boolean invariant_not_31_12(@ForAll LocalDate anyDate) {
        return !Invariant.isNewYearEve(anyDate);
    }







    @Group
    class InvariantSolutionTest {
        @Property
        @Label("If the day is 31 and month 12, then it should systematically be new year's eve")
        public boolean invariant(@ForAll @IntRange(min = -999999999, max = 999999999) int anyYear) {
            return Invariant.isNewYearEve(LocalDate.of(anyYear, 12, 31));
        }

        @Property
//    @Property(tries = 10000, afterFailure = AfterFailureMode.RANDOM_SEED, generation = GenerationMode.RANDOMIZED)
        @Label("If the day is not 31 and month 12, then it should not be new year's eve")
        public boolean invariant_not_31_12(@ForAll LocalDate anyDate) {
            Assume.that(!(anyDate.getMonthValue() == 12 && anyDate.getDayOfMonth() == 31));
//            Assumptions.assumeThat(anyDate.getMonthValue() == 12 && anyDate.getDayOfMonth() == 31).isFalse();
            return !Invariant.isNewYearEve(anyDate);
        }
    }
}
