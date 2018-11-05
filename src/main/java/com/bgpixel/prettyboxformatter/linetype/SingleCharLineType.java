package com.bgpixel.prettyboxformatter.linetype;

/** A LineType implementation where the same character is used for all drawing elements. */
public class SingleCharLineType implements LineType {

    private final char character;

    public SingleCharLineType(char character) {
        this.character = character;
    }

    public char getHorizontalLine() { return character; }
    public char getVerticalLine() { return character; }
    public char getTopLeftCorner() { return character; }
    public char getTopRightCorner() { return character; }
    public char getBottomLeftCorner() { return character; }
    public char getBottomRightCorner() { return character; }
    public char getRightTIntersection() { return character; }
    public char getLeftTIntersection() { return character; }
    public char getDownTIntersection() { return character; }
    public char getUpTIntersection() { return character; }
    public char getCrossIntersection() { return character; }

}
