package com.bgpixel.prettyboxformatter;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PrettyBoxConfiguration {

    @Nullable private final Boolean prefixEveryPrintWithNewline;
    @Nullable private final Integer charsPerLine;
    @Nullable private final Boolean wrapContent;

    private PrettyBoxConfiguration(@Nullable Boolean prefixEveryPrintWithNewline,
                                   @Nullable Integer charsPerLine,
                                   @Nullable Boolean wrapContent) {
        this.prefixEveryPrintWithNewline = prefixEveryPrintWithNewline;
        this.charsPerLine = charsPerLine;
        this.wrapContent = wrapContent;
    }

    @Nullable public Boolean getPrefixEveryPrintWithNewline() { return prefixEveryPrintWithNewline; }
    @Nullable public Integer getCharsPerLine() { return charsPerLine; }
    @Nullable public Boolean getWrapContent() { return wrapContent; }

    public static class Builder {

        @Nullable private Boolean prefixEveryPrintWithNewline = false;
        @Nullable private Integer charsPerLine;
        @Nullable private Boolean wrapContent;

        /** Creates a Builder by copying all values from the PrettyBoxConfiguration instance */
        @NotNull
        public static Builder createFromInstance(@NotNull PrettyBoxConfiguration configuration) {
            Builder builder = new Builder();
            builder.setPrefixEveryPrintWithNewline(configuration.getPrefixEveryPrintWithNewline());
            builder.setCharsPerLine(configuration.getCharsPerLine());
            builder.setWrapContent(configuration.getWrapContent());
            return builder;
        }

        /** Copies non-null values from the PrettyBoxConfiguration instance */
        @NotNull
        public Builder applyFromInstance(@NotNull PrettyBoxConfiguration configuration) {
            if(configuration.getPrefixEveryPrintWithNewline() != null)
                this.prefixEveryPrintWithNewline = configuration.getPrefixEveryPrintWithNewline();
            if(configuration.getCharsPerLine() != null)
                this.charsPerLine = configuration.getCharsPerLine();
            if(configuration.getWrapContent() != null)
                this.wrapContent = configuration.getWrapContent();
            return this;
        }

        @NotNull
        public Builder setPrefixEveryPrintWithNewline(@Nullable Boolean shouldPrefix) {
            this.prefixEveryPrintWithNewline = shouldPrefix;
            return this;
        }

        @NotNull
        public Builder setCharsPerLine(@Nullable Integer charsPerLine) {
            this.charsPerLine = charsPerLine;
            return this;
        }

        @NotNull
        public Builder setWrapContent(@Nullable Boolean wrapContent) {
            this.wrapContent = wrapContent;
            return this;
        }

        @NotNull
        public PrettyBoxConfiguration build() {
            return new PrettyBoxConfiguration(
                    prefixEveryPrintWithNewline,
                    charsPerLine,
                    wrapContent);
        }

    }

}
