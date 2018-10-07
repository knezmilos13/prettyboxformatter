package com.bgpixel.prettyboxformatter;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("WeakerAccess")
public class PrettyBoxFormatter {

    private static final char TOP_LEFT_CORNER = '┌';
    private static final char MIDDLE_LEFT_CORNER = '├';
    private static final char BOTTOM_LEFT_CORNER = '└';
    private static final char TOP_RIGHT_CORNER = '┐';
    private static final char BOTTOM_RIGHT_CORNER = '┘';
    private static final char MIDDLE_RIGHT_CORNER = '┤';
    private static final char HORIZONTAL_LINE = '│';

    private static final String NEWLINE = System.getProperty("line.separator");

    @NotNull
    private static final PrettyBoxConfiguration DEFAULT_CONFIGURATION =
            new PrettyBoxConfiguration.Builder()
                    .setPrefixEveryPrintWithNewline(false)
                    .setCharsPerLine(80)
                    .setWrapContent(true)
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


    // ------------------------------------------------------------------------------------ INTERNAL

    @NotNull
    private static String _format(@NotNull List<String> lines,
                                  @NotNull PrettyBoxConfiguration configuration) {
        StringBuilder stringBuilder = new StringBuilder();

        if(configuration.getPrefixEveryPrintWithNewline()) stringBuilder.append(" \n");

        int horizontalPadding = 1; // TODO

        int contentWidth = configuration.getCharsPerLine() - 2*horizontalPadding - 2;
        int lineWidth = configuration.getCharsPerLine() - 2;

        List<String> splitLines = new ArrayList<>();
        int longestContentWidth = 0;
        for(String line : lines) {
            if(line.length() <= contentWidth) {
                splitLines.add(line);
                longestContentWidth = Math.max(line.length(), longestContentWidth);
            }
            else {
                splitLines.addAll(splitLineEveryNChars(line, contentWidth));
                longestContentWidth = contentWidth;
            }
        }


        if(configuration.getWrapContent()) {
            contentWidth = longestContentWidth;
            lineWidth = contentWidth + 2*horizontalPadding;
        }


        stringBuilder.append(TOP_LEFT_CORNER).append(getDoubleDivider(lineWidth))
                .append(TOP_RIGHT_CORNER).append(NEWLINE);

        for(String line : splitLines) {
            if(line.length() == 0) {
                stringBuilder.append(MIDDLE_LEFT_CORNER).append(getSingleDivider(lineWidth))
                        .append(MIDDLE_RIGHT_CORNER).append(NEWLINE);
            }
            else {
                int rightPadding = horizontalPadding + (contentWidth - line.length());
                stringBuilder.append(HORIZONTAL_LINE).append(" ").append(line)
                        .append(getPadding(rightPadding)).append(HORIZONTAL_LINE).append(NEWLINE);
            }
        }

        stringBuilder.append(BOTTOM_LEFT_CORNER).append(getDoubleDivider(lineWidth))
                .append(BOTTOM_RIGHT_CORNER);

        return stringBuilder.toString();
    }

    @NotNull
    private static List<String> splitLineEveryNChars(@NotNull String string, int partitionSize) {
        List<String> parts = new ArrayList<>();
        int len = string.length();
        for (int i=0; i<len; i+=partitionSize)
            parts.add(string.substring(i, Math.min(len, i + partitionSize)));
        return parts;
    }

    // TODO temp code - implement more advanced handling of drawing elements, possibly in a helper class

    @NotNull
    private static String getDoubleDivider(int length) {
        StringBuilder outputBuffer = new StringBuilder(length);
        for (int i = 0; i < length; i++) outputBuffer.append("─");
        return outputBuffer.toString();
    }

    @NotNull
    private static String getSingleDivider(int length) {
        StringBuilder outputBuffer = new StringBuilder(length);
        for (int i = 0; i < length; i++) outputBuffer.append("┄");
        return outputBuffer.toString();
    }

    @NotNull
    private static String getPadding(int length) {
        StringBuilder outputBuffer = new StringBuilder(length);
        for (int i = 0; i < length; i++) outputBuffer.append(" ");
        return outputBuffer.toString();
    }

}
