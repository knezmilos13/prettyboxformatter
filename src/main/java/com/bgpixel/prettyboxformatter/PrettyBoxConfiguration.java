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

        /** Add a newline before every box. This helps with loggers that add tags and other stuff
         *  before every printout which splits the first line of the box (example: Logcat in
         *  Android). */
        @NotNull
        public Builder setPrefixEveryPrintWithNewline(@Nullable Boolean shouldPrefix) {
            this.prefixEveryPrintWithNewline = shouldPrefix;
            return this;
        }

        /** Number of characters to show per line. If wrapContent is set to true, this represents
         *  only the maximum possible width of the box. If wrapContent is set to false, this
         *  represents the fixed width of the box.<br/>
         *  Note: both the horizontal padding and margin are included in the charsPerLine value. */
        @NotNull
        public Builder setCharsPerLine(@Nullable Integer charsPerLine) {
            this.charsPerLine = charsPerLine;
            return this;
        }

        /** If set to true, the box will take the minimum width needed to show all content without
         *  splitting lines, if possible. The box will not be wider than charsPerLine.<br/>
         *  If set to false, the box will have a fixed width of charsPerLine */
        @NotNull
        public Builder setWrapContent(@Nullable Boolean wrapContent) {
            this.wrapContent = wrapContent;
            return this;
        }

        /** If set to true, the box will be closed on the right side.<br/>
         *  If set to false, the right side of the box will be left open.<br/>
         *  Note: many "monospaced" fonts are not fully monospaced so closed boxes might not work
         *  properly (i.e. the lengths of the lines won't be the same). In that case you should set
         *  this value to false. */
        @NotNull
        public Builder setCloseOnTheRight(@Nullable Boolean closeOnTheRight) {
            this.closeOnTheRight = closeOnTheRight;
            return this;
        }

        /** Sets the number of spaces between the text and the left/right sides of the box. Is part
         *  of the box width, e.g. a closed box with edges 1 space wide, horizontal padding of 10,
         *  and width of 40 would have 18 spaces left for content. */
        @NotNull
        public Builder setHorizontalPadding(@Nullable Integer horizontalPadding) {
            this.horizontalPadding = horizontalPadding;
            return this;
        }

        /** Sets the number of newlines between the text and the top/bottom sides of the box. */
        @NotNull
        public Builder setVerticalPadding(@Nullable Integer verticalPadding) {
            this.verticalPadding = verticalPadding;
            return this;
        }

        /** Sets the number of spaces between the box left/right sides and the surrounding elements
         *  (e.g. line start, other boxes). Is part of the box width, e.g. a closed box with edges 1
         *  space wide, horizontal margin of 10, and width of 40 would have 18 spaces left for
         *  content. */
        @NotNull
        public Builder setHorizontalMargin(@Nullable Integer horizontalMargin) {
            this.horizontalMargin = horizontalMargin;
            return this;
        }

        /** Sets the number of newlines before and after the box is drawn. */
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
