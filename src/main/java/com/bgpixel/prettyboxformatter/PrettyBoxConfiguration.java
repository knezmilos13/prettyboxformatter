package com.bgpixel.prettyboxformatter;

import com.bgpixel.prettyboxformatter.linetype.LineType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("WeakerAccess") // this is a library, and this class is part of library interface
public class PrettyBoxConfiguration {

    @Nullable private final Boolean prefixEveryPrintWithNewline;
    @Nullable private final Integer charsPerLine;
    @Nullable private final Boolean wrapContent;
    @Nullable private final Boolean borderLeft;
    @Nullable private final Boolean borderRight;
    @Nullable private final Boolean borderTop;
    @Nullable private final Boolean borderBottom;
    @Nullable private final List<LineType> lineset;
    @Nullable private final Integer paddingLeft;
    @Nullable private final Integer paddingRight;
    @Nullable private final Integer paddingTop;
    @Nullable private final Integer paddingBottom;
    @Nullable private final Integer marginLeft;
    @Nullable private final Integer marginRight;
    @Nullable private final Integer marginTop;
    @Nullable private final Integer marginBottom;
    @Nullable private final List<BoxMetaData> headerMetadata;
    @Nullable private final List<BoxMetaData> footerMetadata;

    private PrettyBoxConfiguration(@Nullable Boolean prefixEveryPrintWithNewline,
                                   @Nullable Integer charsPerLine,
                                   @Nullable Boolean wrapContent,
                                   @Nullable Boolean borderLeft,
                                   @Nullable Boolean borderRight,
                                   @Nullable Boolean borderTop,
                                   @Nullable Boolean borderBottom,
                                   @Nullable List<LineType> lineset,
                                   @Nullable Integer paddingLeft,
                                   @Nullable Integer paddingRight,
                                   @Nullable Integer paddingTop,
                                   @Nullable Integer paddingBottom,
                                   @Nullable Integer marginLeft,
                                   @Nullable Integer marginRight,
                                   @Nullable Integer marginTop,
                                   @Nullable Integer marginBottom,
                                   @Nullable List<BoxMetaData> headerMetadata,
                                   @Nullable List<BoxMetaData> footerMetadata) {
        this.prefixEveryPrintWithNewline = prefixEveryPrintWithNewline;
        this.charsPerLine = charsPerLine;
        this.wrapContent = wrapContent;
        this.borderLeft = borderLeft;
        this.borderRight = borderRight;
        this.borderTop = borderTop;
        this.borderBottom = borderBottom;
        this.lineset = lineset;
        this.paddingLeft = paddingLeft;
        this.paddingRight = paddingRight;
        this.paddingTop = paddingTop;
        this.paddingBottom = paddingBottom;
        this.marginLeft = marginLeft;
        this.marginRight = marginRight;
        this.marginTop = marginTop;
        this.marginBottom = marginBottom;
        this.headerMetadata = headerMetadata;
        this.footerMetadata = footerMetadata;
    }

    @Nullable public Boolean getPrefixEveryPrintWithNewline() { return prefixEveryPrintWithNewline; }
    @Nullable public Integer getCharsPerLine() { return charsPerLine; }
    @Nullable public Boolean getWrapContent() { return wrapContent; }
    @Nullable public Boolean getBorderLeft() { return borderLeft; }
    @Nullable public Boolean getBorderRight() { return borderRight; }
    @Nullable public Boolean getBorderTop() { return borderTop; }
    @Nullable public Boolean getBorderBottom() { return borderBottom; }
    @NotNull public LineType getBorderLineType() {
        return lineset == null || lineset.size() == 0? LineType.SPACE : lineset.get(0);
    }
    @NotNull public LineType getInnerLineType() {
        return lineset == null || lineset.size() <= 1? getBorderLineType() : lineset.get(1);
    }
    @Nullable public List<LineType> getLineset() { return lineset; }
    @Nullable public Integer getPaddingLeft() { return paddingLeft; }
    @Nullable public Integer getPaddingRight() { return paddingRight; }
    @Nullable public Integer getPaddingTop() { return paddingTop; }
    @Nullable public Integer getPaddingBottom() { return paddingBottom; }
    @Nullable public Integer getMarginLeft() { return marginLeft; }
    @Nullable public Integer getMarginRight() { return marginRight; }
    @Nullable public Integer getMarginTop() { return marginTop; }
    @Nullable public Integer getMarginBottom() { return marginBottom; }
    @Nullable public List<BoxMetaData> getHeaderMetadata() { return headerMetadata; }
    @Nullable public List<BoxMetaData> getFooterMetadata() { return footerMetadata; }

    @NotNull
    public LineType getLineTypeForLevel(int lineLevel) {
        if(lineLevel == 0 || lineset == null || lineset.size() == 0) return getBorderLineType();
        else if(lineLevel == 1) return getInnerLineType();
        else if(lineLevel >= lineset.size()) return lineset.get(lineset.size() - 1);
        else return lineset.get(lineLevel);
    }

    @SuppressWarnings("UnusedReturnValue")
    public static class Builder {

        @Nullable private Boolean prefixEveryPrintWithNewline = false;
        @Nullable private Integer charsPerLine;
        @Nullable private Boolean wrapContent;
        @Nullable private Boolean borderLeft;
        @Nullable private Boolean borderRight;
        @Nullable private Boolean borderTop;
        @Nullable private Boolean borderBottom;
        @Nullable private List<LineType> lineset;
        @Nullable private Integer paddingLeft;
        @Nullable private Integer paddingRight;
        @Nullable private Integer paddingTop;
        @Nullable private Integer paddingBottom;
        @Nullable private Integer marginLeft;
        @Nullable private Integer marginRight;
        @Nullable private Integer marginTop;
        @Nullable private Integer marginBottom;
        @Nullable private List<BoxMetaData> headerMetadata;
        @Nullable private List<BoxMetaData> footerMetadata;

        /** Creates a Builder by copying all values from the PrettyBoxConfiguration instance */
        @NotNull
        public static Builder createFromInstance(@NotNull PrettyBoxConfiguration configuration) {
            Builder builder = new Builder();
            builder.setPrefixEveryPrintWithNewline(configuration.getPrefixEveryPrintWithNewline());
            builder.setCharsPerLine(configuration.getCharsPerLine());
            builder.setWrapContent(configuration.getWrapContent());
            builder.setBorderLeft(configuration.getBorderLeft());
            builder.setBorderRight(configuration.getBorderRight());
            builder.setBorderTop(configuration.getBorderTop());
            builder.setBorderBottom(configuration.getBorderBottom());
            builder.setBorderLineType(configuration.getBorderLineType());
            builder.setInnerLineType(configuration.getInnerLineType());
            builder.setPaddingLeft(configuration.getPaddingLeft());
            builder.setPaddingRight(configuration.getPaddingRight());
            builder.setPaddingTop(configuration.getPaddingTop());
            builder.setPaddingBottom(configuration.getPaddingBottom());
            builder.setMarginLeft(configuration.getMarginLeft());
            builder.setMarginRight(configuration.getMarginRight());
            builder.setMarginTop(configuration.getMarginTop());
            builder.setMarginBottom(configuration.getMarginBottom());
            builder.setHeaderMetadata(configuration.getHeaderMetadata());
            builder.setFooterMetadata(configuration.getFooterMetadata());
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
            if(configuration.getBorderLeft() != null)
                this.borderLeft = configuration.getBorderLeft();
            if(configuration.getBorderRight() != null)
                this.borderRight = configuration.getBorderRight();
            if(configuration.getBorderTop() != null)
                this.borderTop = configuration.getBorderTop();
            if(configuration.getBorderBottom() != null)
                this.borderBottom = configuration.getBorderBottom();
            if(configuration.getLineset() != null)
                this.lineset = configuration.getLineset();
            if(configuration.getPaddingLeft() != null)
                this.paddingLeft = configuration.getPaddingLeft();
            if(configuration.getPaddingRight() != null)
                this.paddingRight = configuration.getPaddingRight();
            if(configuration.getPaddingTop() != null)
                this.paddingTop = configuration.getPaddingTop();
            if(configuration.getPaddingBottom() != null)
                this.paddingBottom = configuration.getPaddingBottom();
            if(configuration.getMarginLeft() != null)
                this.marginLeft = configuration.getMarginLeft();
            if(configuration.getMarginRight() != null)
                this.marginRight = configuration.getMarginRight();
            if(configuration.getMarginTop() != null)
                this.marginTop = configuration.getMarginTop();
            if(configuration.getMarginBottom() != null)
                this.marginBottom = configuration.getMarginBottom();
            if(configuration.getHeaderMetadata() != null)
                this.headerMetadata = configuration.getHeaderMetadata();
            if(configuration.getFooterMetadata() != null)
                this.footerMetadata = configuration.getFooterMetadata();
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

        @NotNull
        public Builder setBorderLeft(@Nullable Boolean borderLeft) {
            this.borderLeft = borderLeft;
            return this;
        }
        /** Note: many "monospaced" fonts are not fully monospaced so closed boxes might not work
         *  properly (i.e. the lengths of the lines won't be the same). In that case you should set
         *  this value to false. */
        @NotNull
        public Builder setBorderRight(@Nullable Boolean borderRight) {
            this.borderRight = borderRight;
            return this;
        }
        @NotNull
        public Builder setBorderTop(@Nullable Boolean borderTop) {
            this.borderTop = borderTop;
            return this;
        }
        @NotNull
        public Builder setBorderBottom(@Nullable Boolean borderBottom) {
            this.borderBottom = borderBottom;
            return this;
        }
        /** Shorthand for setting both left and right border */
        @NotNull
        public Builder setVerticalBorders(@Nullable Boolean verticalBorders) {
            setBorderLeft(verticalBorders);
            setBorderRight(verticalBorders);
            return this;
        }
        /** Shorthand for setting both top and bottom border */
        @NotNull
        public Builder setHorizontalBorders(@Nullable Boolean horizontalBorders) {
            setBorderTop(horizontalBorders);
            setBorderBottom(horizontalBorders);
            return this;
        }
        /** Shorthand for setting all borders with a single call */
        @NotNull
        public Builder setBorders(@Nullable Boolean borders) {
            setVerticalBorders(borders);
            setHorizontalBorders(borders);
            return this;
        }

        /** Sets LineType that will be used for box borders. Alias for lineset[0]. */
        @NotNull
        public Builder setBorderLineType(@Nullable LineType borderLineType) {
            if(lineset == null) lineset = new ArrayList<>();

            if(lineset.size() == 0) lineset.add(borderLineType);
            else lineset.set(0, borderLineType);

            return this;
        }

        /** Sets LineType that will be used for inner lines (i.e. separators).
         *  Alias for lineset[1]. */
        @NotNull
        public Builder setInnerLineType(@Nullable LineType innerLineType) {
            if(lineset == null) lineset = new ArrayList<>();

            if(lineset.size() == 0) lineset.add(innerLineType);

            if(lineset.size() <= 1) lineset.add(innerLineType);
            else lineset.set(1, innerLineType);

            return this;
        }

        /** A list of LineType instances that will be used for border of the box (LineType at index
         *  0) and for inner lines (indexes 1+). borderLineType setting is an alias for
         *  lineLevels[0], while innerLineType is an alias for lineLevels[1]. Setting the lineset
         *  will overwrite any values previously set with borderLineType or innerLineType. */
        @NotNull
        public Builder setLineset(@Nullable List<LineType> lineset) {
            this.lineset = lineset;
            return this;
        }

        @NotNull
        public Builder setPaddingLeft(@Nullable Integer paddingLeft) {
            this.paddingLeft = paddingLeft;
            return this;
        }
        @NotNull
        public Builder setPaddingRight(@Nullable Integer paddingRight) {
            this.paddingRight = paddingRight;
            return this;
        }
        @NotNull
        public Builder setPaddingTop(@Nullable Integer paddingTop) {
            this.paddingTop = paddingTop;
            return this;
        }
        @NotNull
        public Builder setPaddingBottom(@Nullable Integer paddingBottom) {
            this.paddingBottom = paddingBottom;
            return this;
        }
        /** Shorthand for setting both left and right padding */
        @NotNull
        public Builder setHorizontalPadding(@Nullable Integer horizontalPadding) {
            setPaddingLeft(horizontalPadding);
            setPaddingRight(horizontalPadding);
            return this;
        }
        /** Shorthand for setting both top and bottom padding */
        @NotNull
        public Builder setVerticalPadding(@Nullable Integer verticalPadding) {
            setPaddingTop(verticalPadding);
            setPaddingBottom(verticalPadding);
            return this;
        }
        /** Shorthand for setting all paddings with a single call. */
        @NotNull
        public Builder setPadding(@Nullable Integer padding) {
            setVerticalPadding(padding);
            setHorizontalPadding(padding);
            return this;
        }

        @NotNull
        public Builder setMarginLeft(@Nullable Integer marginLeft) {
            this.marginLeft = marginLeft;
            return this;
        }
        @NotNull
        public Builder setMarginRight(@Nullable Integer marginRight) {
            this.marginRight = marginRight;
            return this;
        }
        @NotNull
        public Builder setMarginTop(@Nullable Integer marginTop) {
            this.marginTop = marginTop;
            return this;
        }
        @NotNull
        public Builder setMarginBottom(@Nullable Integer marginBottom) {
            this.marginBottom = marginBottom;
            return this;
        }
        /** Shorthand for setting both left and right margin. */
        @NotNull
        public Builder setHorizontalMargin(@Nullable Integer horizontalMargin) {
            setMarginLeft(horizontalMargin);
            setMarginRight(horizontalMargin);
            return this;
        }
        /** Shorthand for setting both top and bottom margin */
        @NotNull
        public Builder setVerticalMargin(@Nullable Integer verticalMargin) {
            setMarginTop(verticalMargin);
            setMarginBottom(verticalMargin);
            return this;
        }
        /** Shorthand for setting all margins with a single call. */
        @NotNull
        public Builder setMargin(@Nullable Integer margin) {
            setVerticalMargin(margin);
            setHorizontalMargin(margin);
            return this;
        }

        /** Sets which metadata elements will be shown in box header. If no elements are set, the
         *  header will not be shown. */
        @NotNull
        public Builder setHeaderMetadata(@Nullable List<BoxMetaData> headerMetadata) {
            this.headerMetadata = headerMetadata;
            return this;
        }

        /** Sets which metadata elements will be shown in box footer. If no elements are set, the
         *  footer will not be shown. */
        @NotNull
        public Builder setFooterMetadata(@Nullable List<BoxMetaData> footerMetadata) {
            this.footerMetadata = footerMetadata;
            return this;
        }

        @NotNull
        public PrettyBoxConfiguration build() {
            return new PrettyBoxConfiguration(
                    prefixEveryPrintWithNewline,
                    charsPerLine,
                    wrapContent,
                    borderLeft, borderRight, borderTop, borderBottom,
                    lineset,
                    paddingLeft, paddingRight, paddingTop, paddingBottom,
                    marginLeft, marginRight, marginTop, marginBottom,
                    headerMetadata, footerMetadata);
        }

    }

}
