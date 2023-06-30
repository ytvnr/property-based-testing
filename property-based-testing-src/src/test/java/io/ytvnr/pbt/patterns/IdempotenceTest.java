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

import net.jqwik.api.ForAll;
import net.jqwik.api.Property;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Yann TAVERNIER (yann.tavernier at graviteesource.com)
 * @author GraviteeSource Team
 */
public class IdempotenceTest {

    @Property
    public void idempotence(@ForAll List<String> anyStrings) {
        final List<String> result1 = Idempotence.sort(anyStrings);
        final List<String> result2 = Idempotence.sort(result1);

        assertThat(result1).isEqualTo(result2);
    }
}
