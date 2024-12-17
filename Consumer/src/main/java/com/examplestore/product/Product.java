package com.examplestore.product;

import lombok.*;

import java.text.NumberFormat;
import java.util.Locale;

@AllArgsConstructor(access = AccessLevel.PUBLIC)
@Builder(access = AccessLevel.PUBLIC)
@Getter
@EqualsAndHashCode
public class Product {

    @NonNull
    private final ProductId productId;

    @NonNull
    @Setter
    private Price price;

    @AllArgsConstructor
    @Setter
    @Getter
    @EqualsAndHashCode
    public static class ProductId {

        @NonNull
        private final String id;

        public String toString() {
            return id;
        }
    }

    @AllArgsConstructor
    @Setter
    @Getter
    @EqualsAndHashCode
    public static class Price {

        private final long milliCents;

        public Price(int euro, int cents) {
            this.milliCents = ((euro * 100) + cents) * 1000;
        }

        @Override
        public String toString() {
            NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.GERMANY);

            return nf.format(milliCents / 100000.0);
        }

        public long toNumber() {
            return milliCents;
        }
    }
}
