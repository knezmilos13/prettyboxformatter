package com.bgpixel.prettyboxformatter.linetype;

/** A LineType implementation where horizontal and vertical line have separate characters, while
 *  all other drawing elements use the same (third) character. */
public class SimpleLineType implements LineType {

    private final char horizontalLine;
    private final char verticalLine;
    private final char otherCharacter;

    public SimpleLineType(char horizontalLine,
                          char verticalLine,
                          char otherCharacter) {
        this.horizontalLine = horizontalLine;
        this.verticalLine = verticalLine;
        this.otherCharacter = otherCharacter;
    }

    public char getHorizontalLine() { return horizontalLine; }
    public char getVerticalLine() { return verticalLine; }
    public char getTopLeftCorner() { return otherCharacter; }
    public char getTopRightCorner() { return otherCharacter; }
    public char getBottomLeftCorner() { return otherCharacter; }
    public char getBottomRightCorner() { return otherCharacter; }
    public char getRightTIntersection() { return otherCharacter; }
    public char getLeftTIntersection() { return otherCharacter; }
    public char getDownTIntersection() { return otherCharacter; }
    public char getUpTIntersection() { return otherCharacter; }
    public char getCrossIntersection() { return otherCharacter; }

}
