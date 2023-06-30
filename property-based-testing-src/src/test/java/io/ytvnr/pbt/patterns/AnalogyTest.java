package io.ytvnr.pbt.patterns;

import net.jqwik.api.Arbitraries;
import net.jqwik.api.Arbitrary;
import net.jqwik.api.Builders;
import net.jqwik.api.Disabled;
import net.jqwik.api.ForAll;
import net.jqwik.api.From;
import net.jqwik.api.Group;
import net.jqwik.api.Property;
import net.jqwik.api.Provide;
import net.jqwik.api.constraints.Size;
import org.assertj.core.api.Assertions;

import java.util.List;

/**
 * @author Yann TAVERNIER (yann.tavernier at graviteesource.com)
 * @author GraviteeSource Team
 */
class AnalogyTest {

    @Property
    public void shouldHaveSameResultInRefactorAndLegacy(@ForAll @Size(min = 1) List<Analogy.User> users) {
        System.out.println(users);
        final double legacy = Analogy.averageAgeLegacy(users);
        final double refactor = Analogy.averageAgeRefacto(users);
        Assertions.assertThat(legacy).isEqualTo(refactor);
    }














    @Group
    class AnalogySolutionTest {
        @Property
        public void shouldHaveSameResultInRefactorAndLegacy(@ForAll @Size(min = 1) List<Analogy.@From("generateUsers") User> users) {
            final double legacy = Analogy.averageAgeLegacy(users);
            final double refactor = Analogy.averageAgeRefacto(users);
            Assertions.assertThat(legacy).isEqualTo(refactor);
        }


        @Provide
        Arbitrary<Analogy.User> generateUsers() {
            Arbitrary<String> names = Arbitraries.strings().withCharRange('a', 'z').ofMinLength(3).ofMaxLength(21);
            Arbitrary<Integer> ages = Arbitraries.integers().between(1, 130);

            return Builders.withBuilder(Analogy.User::new)
                   .use(names).inSetter(Analogy.User::setName)
                   .use(ages).inSetter(Analogy.User::setAge)
                   .build();
        }
    }
}