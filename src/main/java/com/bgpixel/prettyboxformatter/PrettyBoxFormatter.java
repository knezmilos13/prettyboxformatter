package com.bgpixel.prettyboxformatter;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings({"WeakerAccess"})
public class PrettyBoxFormatter {

    private static final char TOP_LEFT_CORNER = '┌';
    private static final char MIDDLE_LEFT_CORNER = '├';
    private static final char BOTTOM_LEFT_CORNER = '└';
    private static final char TOP_RIGHT_CORNER = '┐';
    private static final char BOTTOM_RIGHT_CORNER = '┘';
    private static final char MIDDLE_RIGHT_CORNER = '┤';
    private static final char VERTICAL_LINE = '│';

    private static final String NEWLINE = System.getProperty("line.separator");

    @NotNull
    private static final PrettyBoxConfiguration DEFAULT_CONFIGURATION =
            new PrettyBoxConfiguration.Builder()
                    .setPrefixEveryPrintWithNewline(false)
                    .setCharsPerLine(80)
                    .setWrapContent(true)
                    .setCloseOnTheRight(true)
                    .setHorizontalPadding(1)
                    .setVerticalPadding(0)
                    .setHorizontalMargin(0)
                    .setVerticalMargin(0)
                    .build();

    @NotNull
    private static PrettyBoxConfiguration configuration = DEFAULT_CONFIGURATION;

    public static void setConfiguration(@NotNull PrettyBoxConfiguration configuration) {
        PrettyBoxFormatter.configuration =
                PrettyBoxConfiguration.Builder.createFromInstance(DEFAULT_CONFIGURATION)
                        .applyFromInstance(configuration)
                        .build();
    }

    @NotNull
    public static String format(@NotNull PrettyBoxable thingy) {
        return format(thingy.toStringLines());
    }

    @NotNull
    public static String format(@NotNull PrettyBoxable thingy,
                                @NotNull PrettyBoxConfiguration configuration) {
        return format(thingy.toStringLines(), configuration);
    }

    @NotNull
    public static String format(@NotNull List<String> lines) {
        return _format(lines, configuration);
    }

    @NotNull
    public static String format(@NotNull List<String> lines,
                                @NotNull PrettyBoxConfiguration configuration) {
        PrettyBoxConfiguration mergedConfig =
                PrettyBoxConfiguration.Builder.createFromInstance(PrettyBoxFormatter.configuration)
                        .applyFromInstance(configuration)
                        .build();
        return _format(lines, mergedConfig);
    }


    // ------------------------------------------------------------------------------ MAIN ALGORITHM

    // stuff is nullable, but we make sure the default settings provide fallback non-null values
    @SuppressWarnings("ConstantConditions")
    @NotNull
    private static String _format(@NotNull List<String> lines,
                                  @NotNull PrettyBoxConfiguration configuration) {
        StringBuilder stringBuilder = new StringBuilder();

        if(configuration.getPrefixEveryPrintWithNewline()) stringBuilder.append(" \n");

        int contentWidth = determineMaxBoxWidth(configuration);

        List<String> splitLines = splitLinesToFitBox(lines, contentWidth);

        // If wrap content is TRUE, make the box as wide as the longest line we have.
        if(configuration.getWrapContent()) {
            int longestContentWidth = 0;
            for(String line : splitLines)
                longestContentWidth = Math.max(line.length(), longestContentWidth);
            contentWidth = longestContentWidth;
        }

        int lineWidth = contentWidth +
                (configuration.getCloseOnTheRight()? 2:1) * configuration.getHorizontalPadding();

        drawVerticalMargin(stringBuilder, configuration.getVerticalMargin());

        drawTopLine(stringBuilder, lineWidth, configuration);

        drawVerticalPadding(stringBuilder, lineWidth, configuration);

        for(String line : splitLines) {
            if(line.length() == 0) drawInnerLine(stringBuilder, lineWidth, configuration);
            else drawContentLine(stringBuilder, line, contentWidth, configuration);
        }

        drawVerticalPadding(stringBuilder, lineWidth, configuration);

        drawBottomLine(stringBuilder, lineWidth, configuration);

        drawVerticalMargin(stringBuilder, configuration.getVerticalMargin());

        return stringBuilder.toString();
    }

    @SuppressWarnings("ConstantConditions") // @see _format
    private static int determineMaxBoxWidth(@NotNull PrettyBoxConfiguration configuration) {
        int numSides = configuration.getCloseOnTheRight()? 2 : 1;

        return configuration.getCharsPerLine()
                - numSides*configuration.getHorizontalPadding()
                - numSides*configuration.getHorizontalMargin()
                - numSides;

        // TODO sanitize values if margins and paddings are larger than box
    }

    @NotNull
    private static List<String> splitLinesToFitBox(@NotNull List<String> lines, int contentWidth) {
        List<String> splitLines = new ArrayList<>();
        for(String line : lines) {
            if(line.length() <= contentWidth) splitLines.add(line);
            else splitLines.addAll(splitLineEveryNChars(line, contentWidth));
        }
        return splitLines;
    }

    private static void drawVerticalMargin(@NotNull StringBuilder stringBuilder,
                                           int verticalMargin) {
        for(int i = 0; i < verticalMargin; i++) stringBuilder.append(NEWLINE);
    }

    @SuppressWarnings("ConstantConditions") // @see _format
    private static void drawTopLine(@NotNull StringBuilder stringBuilder,
                                    int lineWidth,
                                    @NotNull PrettyBoxConfiguration configuration) {
        stringBuilder
                .append(getPadding(configuration.getHorizontalMargin()))
                .append(TOP_LEFT_CORNER)
                .append(getDoubleDivider(lineWidth));

        if(configuration.getCloseOnTheRight())
            stringBuilder.append(TOP_RIGHT_CORNER);

        stringBuilder.append(NEWLINE);
    }

    @SuppressWarnings("ConstantConditions") // @see _format
    private static void drawVerticalPadding(@NotNull StringBuilder stringBuilder,
                                            int lineWidth,
                                            @NotNull PrettyBoxConfiguration configuration) {
        for(int i = 0; i < configuration.getVerticalPadding(); i++) {
            stringBuilder
                    .append(getPadding(configuration.getHorizontalMargin()))
                    .append(VERTICAL_LINE);

            if(configuration.getCloseOnTheRight())
                stringBuilder
                        .append(getPadding(lineWidth))
                        .append(VERTICAL_LINE);

            stringBuilder.append(NEWLINE);
        }
    }

    @SuppressWarnings("ConstantConditions") // @see _format
    private static void drawInnerLine(@NotNull StringBuilder stringBuilder,
                                      int lineWidth,
                                      @NotNull PrettyBoxConfiguration configuration) {
        stringBuilder
                .append(getPadding(configuration.getHorizontalMargin()))
                .append(MIDDLE_LEFT_CORNER)
                .append(getSingleDivider(lineWidth));

        if(configuration.getCloseOnTheRight())
            stringBuilder.append(MIDDLE_RIGHT_CORNER);

        stringBuilder.append(NEWLINE);
    }

    @SuppressWarnings("ConstantConditions") // @see _format
    private static void drawContentLine(@NotNull StringBuilder stringBuilder,
                                        @NotNull String line,
                                        int contentWidth,
                                        @NotNull PrettyBoxConfiguration configuration) {
        stringBuilder
                .append(getPadding(configuration.getHorizontalMargin()))
                .append(VERTICAL_LINE)
                .append(getPadding(configuration.getHorizontalPadding()))
                .append(line);

        if(configuration.getCloseOnTheRight()) {
            int rightPadding = configuration.getHorizontalPadding() + (contentWidth - line.length());
            stringBuilder
                    .append(getPadding(rightPadding))
                    .append(VERTICAL_LINE);
        }

        stringBuilder.append(NEWLINE);
    }

    @SuppressWarnings("ConstantConditions") // @see _format
    private static void drawBottomLine(@NotNull StringBuilder stringBuilder,
                                       int lineWidth,
                                       @NotNull PrettyBoxConfiguration configuration) {
        stringBuilder
                .append(getPadding(configuration.getHorizontalMargin()))
                .append(BOTTOM_LEFT_CORNER)
                .append(getDoubleDivider(lineWidth));

        if(configuration.getCloseOnTheRight())
            stringBuilder.append(BOTTOM_RIGHT_CORNER);

        stringBuilder.append(NEWLINE);
    }


    // ------------------------------------------------------------------------------------- HELPERS

    @NotNull
    private static List<String> splitLineEveryNChars(@NotNull String string, int partitionSize) {
        List<String> parts = new ArrayList<>();
        int len = string.length();
        for (int i=0; i<len; i+=partitionSize)
            parts.add(string.substring(i, Math.min(len, i + partitionSize)));
        return parts;
    }

    @NotNull
    private static String getDoubleDivider(int length) {
        return getNCharacterString("─", length);
    }

    @NotNull
    private static String getSingleDivider(int length) {
        return getNCharacterString("┄", length);
    }

    @NotNull
    private static String getPadding(int length) {
        return getNCharacterString(" ", length);
    }

    @NotNull
    private static String getNCharacterString(@NotNull String character, int length) {
        StringBuilder outputBuffer = new StringBuilder(length);
        for (int i = 0; i < length; i++) outputBuffer.append(character);
        return outputBuffer.toString();
    }

}
