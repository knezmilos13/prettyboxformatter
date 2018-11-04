package com.bgpixel.prettyboxformatter.lines;

/** Defines characters that will be used for box drawing. Contains a number of predefined instances
 *  that can be used, or you can define a new one by implementing this interface or extending one
 *  of the available subclasses. */
public interface LineType {

    LineType SPACE = new SingleCharLineType(' ');
    LineType LINE = new BasicLineType('─', '│', '┌', '┐', '└', '┘', '├', '┤', '┬', '┴', '┼');
    LineType LINE_THICK = new BasicLineType('━', '┃', '┏', '┓', '┗', '┛', '┣', '┫', '┳', '┻', '╋');
    LineType DASH_DOUBLE = new BasicLineType('╌', '╎', '┌', '┐', '└', '┘', '├', '┤', '┬', '┴', '┼');
    LineType DASH_DOUBLE_THICK = new BasicLineType('╍', '╏', '┏', '┓', '┗', '┛', '┣', '┫', '┳', '┻', '╋');
    LineType DASH_TRIPLE = new BasicLineType('┄', '┆', '┌', '┐', '└', '┘', '├', '┤', '┬', '┴', '┼');
    LineType DASH_TRIPLE_THICK = new BasicLineType('┅', '┇', '┏', '┓', '┗', '┛', '┣', '┫', '┳', '┻', '╋');
    LineType DASH_QUADRUPLE = new BasicLineType('┈', '┊', '┌', '┐', '└', '┘', '├', '┤', '┬', '┴', '┼');
    LineType DASH_QUADRUPLE_THICK = new BasicLineType('┉', '┋', '┏', '┓', '┗', '┛', '┣', '┫', '┳', '┻', '╋');
    LineType LINE_DOUBLE = new BasicLineType('═', '║', '╔', '╗', '╚', '╝', '╠', '╣', '╦', '╩', '╬');
    LineType SIMPLE_PLUS_MINUS = new SimpleLineType('-', '|', '+');
    LineType SHADED_BLOCK_LIGHT = new SingleCharLineType('░');
    LineType SHADED_BLOCK_MEDIUM = new SingleCharLineType('▒');
    LineType SHADED_BLOCK_DARK = new SingleCharLineType('▓');
    LineType BLOCK_FULL = new SingleCharLineType('█');
    LineType BLOCK_HALF = new BasicLineType('▄', '▌', '▄', '▖', '▙', '▌', '▙', '▌', '▄', '▄', '▞');
    LineType FILLED_SQUARE = new BasicLineType('▤', '▥', '▨', '▧', '▧', '▨', '▦', '▦', '▦', '▦', '▩');
    LineType BRAILLE = new BasicLineType('⠒', '⠇', '⠖', '⠲', '⠓', '⠚', '⠗', '⠺', '⠏', '⠧', '⠗');

    char getHorizontalLine();
    char getVerticalLine();
    char getTopLeftCorner();
    char getTopRightCorner();
    char getBottomLeftCorner();
    char getBottomRightCorner();
    char getRightTIntersection();
    char getLeftTIntersection();
    char getDownTIntersection();
    char getUpTIntersection();
    char getCrossIntersection();

}
