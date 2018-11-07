package com.bgpixel.prettyboxformatter.line;

import org.jetbrains.annotations.NotNull;

/** Represents an inner line of a specified level. Adding an instance of this class to an array
 *  of CharSequences prepared for drawing will draw an inner horizontal line at that position.
 *  Which LineType is used will depend on the lineset value set in PrettyBoxConfiguration. */
public class LineWithLevel implements CharSequence {

    @NotNull public static final LineWithLevel LEVEL_0 = new LineWithLevel(0);
    @NotNull public static final LineWithLevel LEVEL_1 = new LineWithLevel(1);
    @NotNull public static final LineWithLevel LEVEL_2 = new LineWithLevel(2);
    @NotNull public static final LineWithLevel LEVEL_3 = new LineWithLevel(3);
    @NotNull public static final LineWithLevel LEVEL_4 = new LineWithLevel(4);
    @NotNull public static final LineWithLevel LEVEL_5 = new LineWithLevel(5);

    @NotNull public static final LineWithLevel BORDER_LINE = LEVEL_0;
    @NotNull public static final LineWithLevel INNER_LINE = LEVEL_1;

    private final int level;

    public LineWithLevel(int level) {
        this.level = level;
    }

    @Override public int length() { return 0; }
    @Override public char charAt(int index) { return 0; }
    @Override public CharSequence subSequence(int start, int end) { return this; }
    @NotNull @Override public String toString() { return ""; }

    public int getLineLevel() { return level; }

}
