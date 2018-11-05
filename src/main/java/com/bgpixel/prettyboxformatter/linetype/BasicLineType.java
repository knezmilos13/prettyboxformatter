package com.bgpixel.prettyboxformatter.linetype;

/** A LineType implementation where you define each drawing element separately. */
public class BasicLineType implements LineType {

    private final char horizontalLine;
    private final char verticalLine;
    private final char topLeftCorner;
    private final char topRightCorner;
    private final char bottomLeftCorner;
    private final char bottomRightCorner;
    private final char rightTIntersection;
    private final char leftTIntersection;
    private final char downTIntersection;
    private final char upTIntersection;
    private final char crossIntersection;

    public BasicLineType(char horizontalLine,
                         char verticalLine,
                         char topLeftCorner,
                         char topRightCorner,
                         char bottomLeftCorner,
                         char bottomRightCorner,
                         char rightTIntersection,
                         char leftTIntersection,
                         char downTIntersection,
                         char upTIntersection,
                         char crossIntersection) {
        this.horizontalLine = horizontalLine;
        this.verticalLine = verticalLine;
        this.topLeftCorner = topLeftCorner;
        this.topRightCorner = topRightCorner;
        this.bottomLeftCorner = bottomLeftCorner;
        this.bottomRightCorner = bottomRightCorner;
        this.rightTIntersection = rightTIntersection;
        this.leftTIntersection = leftTIntersection;
        this.downTIntersection = downTIntersection;
        this.upTIntersection = upTIntersection;
        this.crossIntersection = crossIntersection;
    }

    public char getHorizontalLine() { return horizontalLine; }
    public char getVerticalLine() { return verticalLine; }
    public char getTopLeftCorner() { return topLeftCorner; }
    public char getTopRightCorner() { return topRightCorner; }
    public char getBottomLeftCorner() { return bottomLeftCorner; }
    public char getBottomRightCorner() { return bottomRightCorner; }
    public char getRightTIntersection() { return rightTIntersection; }
    public char getLeftTIntersection() { return leftTIntersection; }
    public char getDownTIntersection() { return downTIntersection; }
    public char getUpTIntersection() { return upTIntersection; }
    public char getCrossIntersection() { return crossIntersection; }

}
