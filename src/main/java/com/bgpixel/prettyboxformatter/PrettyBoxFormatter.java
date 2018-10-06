package com.bgpixel.prettyboxformatter;

import org.jetbrains.annotations.NotNull;

import java.util.List;

@SuppressWarnings("WeakerAccess")
public class PrettyBoxFormatter {

    private static final char TOP_LEFT_CORNER = '┌';
    private static final char BOTTOM_LEFT_CORNER = '└';
    private static final char MIDDLE_CORNER = '├';
    private static final char HORIZONTAL_LINE = '│';
    private static final String DOUBLE_DIVIDER = "────────────────────────────────────────────────────────";
    private static final String SINGLE_DIVIDER = "┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄";
    private static final String TOP_BORDER = TOP_LEFT_CORNER + DOUBLE_DIVIDER + DOUBLE_DIVIDER;
    private static final String BOTTOM_BORDER = BOTTOM_LEFT_CORNER + DOUBLE_DIVIDER + DOUBLE_DIVIDER;
    private static final String MIDDLE_BORDER = MIDDLE_CORNER + SINGLE_DIVIDER + SINGLE_DIVIDER;

    private static final String NEWLINE = System.getProperty("line.separator");

    @NotNull
    public static String format(@NotNull PrettyBoxable thingy) {
        return format(thingy.toStringLines());
    }

    @NotNull
    public static String format(@NotNull List<String> lines) {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(" \n"); // Logcat now breaks the box on first line, so do a "\n"

        stringBuilder.append(TOP_BORDER).append(NEWLINE);

        for(String line : lines) {
            if(line.length() == 0) stringBuilder.append(MIDDLE_BORDER).append(NEWLINE);
            else stringBuilder.append(HORIZONTAL_LINE).append(" ").append(line).append(NEWLINE);
        }

        stringBuilder.append(BOTTOM_BORDER);

        return stringBuilder.toString();
    }

    @NotNull
    public static String format(@NotNull String headerLine, @NotNull PrettyBoxable thingy) {
        return format(headerLine, thingy.toStringLines());
    }

    /** Adds a header line before printing all other lines out. For convenience. */
    @NotNull
    public static String format(@NotNull String headerLine, @NotNull List<String> lines) {
        lines.add(0, headerLine);
        return format(lines);
    }

}
