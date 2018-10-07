package com.bgpixel.prettyboxformatter;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PrettyBoxConfiguration {

    private final Boolean prefixEveryPrintWithNewline;

    private PrettyBoxConfiguration(Boolean prefixEveryPrintWithNewline) {
        this.prefixEveryPrintWithNewline = prefixEveryPrintWithNewline;
    }

    public Boolean isPrefixEveryPrintWithNewline() {
        return prefixEveryPrintWithNewline;
    }

    public static class Builder {

        @Nullable private Boolean prefixEveryPrintWithNewline = false;

        /** Creates a Builder by copying all values from the PrettyBoxConfiguration instance */
        @NotNull
        public static Builder createFromInstance(@NotNull PrettyBoxConfiguration configuration) {
            Builder builder = new Builder();
            builder.setPrefixEveryPrintWithNewline(configuration.isPrefixEveryPrintWithNewline());
            return builder;
        }

        /** Copies non-null values from the PrettyBoxConfiguration instance */
        @NotNull
        public Builder applyFromInstance(@NotNull PrettyBoxConfiguration configuration) {
            if(configuration.isPrefixEveryPrintWithNewline() != null)
                this.prefixEveryPrintWithNewline = configuration.isPrefixEveryPrintWithNewline();
            return this;
        }

        @NotNull
        public Builder setPrefixEveryPrintWithNewline(@Nullable Boolean shouldPrefix) {
            this.prefixEveryPrintWithNewline = shouldPrefix;
            return this;
        }

        @NotNull
        public PrettyBoxConfiguration build() {
            return new PrettyBoxConfiguration(
                    prefixEveryPrintWithNewline);
        }

    }

}
