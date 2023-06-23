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
package io.ytvnr.pbt;

import net.jqwik.api.Arbitraries;
import net.jqwik.api.Arbitrary;
import net.jqwik.api.ForAll;
import net.jqwik.api.Property;
import net.jqwik.api.Provide;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class FizzBuzzTest {
    @Property
    boolean every_third_element_starts_with_Fizz(@ForAll("divisibleBy3") int i) {
        return fizzBuzz().get(i - 1).startsWith("Fizz");
    }

    @Property
    boolean every_fifth_element_starts_with_Buzz(@ForAll("divisibleBy5") int i) {
        return fizzBuzz().get(i - 1).endsWith("Buzz");
    }

    @Property
    boolean every_third_and_fifth_element_starts_with_Buzz(@ForAll("divisibleBy3And5") int i) {
        return fizzBuzz().get(i - 1).equals("FizzBuzz");
    }

    @Provide
    Arbitrary<Integer> divisibleBy3() {
        return Arbitraries.integers().between(1, 100).filter(i -> i % 3 == 0);
    }

    @Provide
    Arbitrary<Integer> divisibleBy5() {
        return Arbitraries.integers().between(1, 100).filter(i -> i % 5 == 0);
    }

    @Provide
    Arbitrary<Integer> divisibleBy3And5() {
        return Arbitraries.integers().between(1, 100).filter(i -> i % 5 == 0 && i % 3 == 0);
    }

    private List<String> fizzBuzz() {
        return IntStream.range(1, 101)
               .mapToObj((int i) -> {
                   boolean divBy3 = i % 3 == 0;
                   boolean divBy5 = i % 5 == 0;

                   return divBy3 && divBy5 ? "FizzBuzz"
                          : divBy3 ? "Fizz"
                          : divBy5 ? "Buzz"
                          : String.valueOf(i);
               })
               .collect(Collectors.toList());
    }
}
