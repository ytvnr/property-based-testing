package io.ytvnr.pbt.patterns;

import net.jqwik.api.Arbitraries;
import net.jqwik.api.Arbitrary;
import net.jqwik.api.ArbitrarySupplier;
import net.jqwik.api.ForAll;
import net.jqwik.api.Property;
import net.jqwik.api.constraints.BigRange;
import net.jqwik.api.constraints.Scale;
import org.assertj.core.api.Assertions;

import java.math.BigDecimal;

/**
 * @author Yann TAVERNIER (yann.tavernier at graviteesource.com)
 * @author GraviteeSource Team
 */
class SymmetryTest {

    @Property
    public void symmetry(@ForAll @BigRange(min = "0.001", max = "50000") @Scale(3) BigDecimal initialPrice) {
        System.out.println(initialPrice);
        final BigDecimal withTaxes = Symmetry.applyTaxes(initialPrice);
        final BigDecimal withoutTaxes = Symmetry.removeTaxes(withTaxes);

        Assertions.assertThat(initialPrice).isEqualTo(withoutTaxes);
    }

    @Property
    public void symmetryWithArbitrarySupplier(@ForAll(supplier = ValidPrices.class) BigDecimal initialPrice) {
        System.out.println(initialPrice);
        final BigDecimal withTaxes = Symmetry.applyTaxes(initialPrice);
        final BigDecimal withoutTaxes = Symmetry.removeTaxes(withTaxes);

        Assertions.assertThat(initialPrice).isEqualTo(withoutTaxes);
    }

    static class ValidPrices implements ArbitrarySupplier<BigDecimal> {
        @Override
        public Arbitrary<BigDecimal> get() {
            return Arbitraries.bigDecimals().between(BigDecimal.valueOf(0.001), BigDecimal.valueOf(50000)).ofScale(3);
        }
    }
}