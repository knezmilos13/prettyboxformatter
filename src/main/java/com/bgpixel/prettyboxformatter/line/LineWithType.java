package com.bgpixel.prettyboxformatter.line;

import com.bgpixel.prettyboxformatter.linetype.LineType;
import org.jetbrains.annotations.NotNull;

/** Represents an inner line of a specified LineType. Adding an instance of this class to an array
 *  of CharSequences prepared for drawing will draw an inner horizontal line at that position. */
public class LineWithType implements CharSequence {

    @NotNull private final LineType lineType;

    public LineWithType(@NotNull LineType lineType) {
        this.lineType = lineType;
    }

    @Override public int length() { return 0; }
    @Override public char charAt(int index) { return 0; }
    @Override public CharSequence subSequence(int start, int end) { return this; }
    @NotNull @Override public String toString() { return ""; }

    @NotNull public LineType getLineType() { return lineType; }

}
