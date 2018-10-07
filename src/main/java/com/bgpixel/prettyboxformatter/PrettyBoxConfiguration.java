package com.bgpixel.prettyboxformatter;

import org.jetbrains.annotations.NotNull;

public class PrettyBoxConfiguration {

    private final boolean prefixEveryPrintWithNewline;

    private PrettyBoxConfiguration(boolean prefixEveryPrintWithNewline) {
        this.prefixEveryPrintWithNewline = prefixEveryPrintWithNewline;
    }

    public boolean shouldPrefixEveryPrintWithNewline() {
        return prefixEveryPrintWithNewline;
    }

    public static class Builder {
        private boolean prefixEveryPrintWithNewline = false;

        @NotNull
        public Builder shouldPrefixEveryPrintWithNewline(boolean shouldPrefix) {
            this.prefixEveryPrintWithNewline = shouldPrefix;
            return this;
        }

        public PrettyBoxConfiguration build() {
            return new PrettyBoxConfiguration(
                    prefixEveryPrintWithNewline);
        }
    }

}
