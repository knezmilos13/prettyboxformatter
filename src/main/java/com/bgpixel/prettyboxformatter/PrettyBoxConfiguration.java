package com.bgpixel.prettyboxformatter;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PrettyBoxConfiguration {

    @Nullable private final Boolean prefixEveryPrintWithNewline;
    @Nullable private final Integer charsPerLine;
    @Nullable private final Boolean wrapContent;
    @Nullable private final Boolean closeOnTheRight;
    @Nullable private final Integer horizontalPadding;
    @Nullable private final Integer verticalPadding;
    @Nullable private final Integer horizontalMargin;
    @Nullable private final Integer verticalMargin;

    private PrettyBoxConfiguration(@Nullable Boolean prefixEveryPrintWithNewline,
                                   @Nullable Integer charsPerLine,
                                   @Nullable Boolean wrapContent,
                                   @Nullable Boolean closeOnTheRight,
                                   @Nullable Integer horizontalPadding,
                                   @Nullable Integer verticalPadding,
                                   @Nullable Integer horizontalMargin,
                                   @Nullable Integer verticalMargin) {
        this.prefixEveryPrintWithNewline = prefixEveryPrintWithNewline;
        this.charsPerLine = charsPerLine;
        this.wrapContent = wrapContent;
        this.closeOnTheRight = closeOnTheRight;
        this.horizontalPadding = horizontalPadding;
        this.verticalPadding = verticalPadding;
        this.horizontalMargin = horizontalMargin;
        this.verticalMargin = verticalMargin;
    }

    @Nullable public Boolean getPrefixEveryPrintWithNewline() { return prefixEveryPrintWithNewline; }
    @Nullable public Integer getCharsPerLine() { return charsPerLine; }
    @Nullable public Boolean getWrapContent() { return wrapContent; }
    @Nullable public Boolean getCloseOnTheRight() { return closeOnTheRight; }
    @Nullable public Integer getHorizontalPadding() { return horizontalPadding; }
    @Nullable public Integer getVerticalPadding() { return verticalPadding; }
    @Nullable public Integer getHorizontalMargin() { return horizontalMargin; }
    @Nullable public Integer getVerticalMargin() { return verticalMargin; }

    public static class Builder {

        @Nullable private Boolean prefixEveryPrintWithNewline = false;
        @Nullable private Integer charsPerLine;
        @Nullable private Boolean wrapContent;
        @Nullable private Boolean closeOnTheRight;
        @Nullable private Integer horizontalPadding;
        @Nullable private Integer verticalPadding;
        @Nullable private Integer horizontalMargin;
        @Nullable private Integer verticalMargin;

        /** Creates a Builder by copying all values from the PrettyBoxConfiguration instance */
        @NotNull
        public static Builder createFromInstance(@NotNull PrettyBoxConfiguration configuration) {
            Builder builder = new Builder();
            builder.setPrefixEveryPrintWithNewline(configuration.getPrefixEveryPrintWithNewline());
            builder.setCharsPerLine(configuration.getCharsPerLine());
            builder.setWrapContent(configuration.getWrapContent());
            builder.setCloseOnTheRight(configuration.getCloseOnTheRight());
            builder.setHorizontalPadding(configuration.getHorizontalPadding());
            builder.setVerticalPadding(configuration.getVerticalPadding());
            builder.setHorizontalMargin(configuration.getHorizontalMargin());
            builder.setVerticalMargin(configuration.getVerticalMargin());
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
            if(configuration.getCloseOnTheRight() != null)
                this.closeOnTheRight = configuration.getCloseOnTheRight();
            if(configuration.getHorizontalPadding() != null)
                this.horizontalPadding = configuration.getHorizontalPadding();
            if(configuration.getVerticalPadding() != null)
                this.verticalPadding = configuration.getVerticalPadding();
            if(configuration.getHorizontalMargin() != null)
                this.horizontalMargin = configuration.getHorizontalMargin();
            if(configuration.getVerticalMargin() != null)
                this.verticalMargin = configuration.getVerticalMargin();
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
        public Builder setCloseOnTheRight(@Nullable Boolean closeOnTheRight) {
            this.closeOnTheRight = closeOnTheRight;
            return this;
        }

        @NotNull
        public Builder setHorizontalPadding(@Nullable Integer horizontalPadding) {
            this.horizontalPadding = horizontalPadding;
            return this;
        }

        @NotNull
        public Builder setVerticalPadding(@Nullable Integer verticalPadding) {
            this.verticalPadding = verticalPadding;
            return this;
        }

        @NotNull
        public Builder setHorizontalMargin(@Nullable Integer horizontalMargin) {
            this.horizontalMargin = horizontalMargin;
            return this;
        }

        @NotNull
        public Builder setVerticalMargin(@Nullable Integer verticalMargin) {
            this.verticalMargin = verticalMargin;
            return this;
        }

        @NotNull
        public PrettyBoxConfiguration build() {
            return new PrettyBoxConfiguration(
                    prefixEveryPrintWithNewline,
                    charsPerLine,
                    wrapContent,
                    closeOnTheRight,
                    horizontalPadding,
                    verticalPadding,
                    horizontalMargin,
                    verticalMargin);
        }

    }

}
