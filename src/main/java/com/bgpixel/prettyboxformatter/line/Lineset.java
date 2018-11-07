package com.bgpixel.prettyboxformatter.line;

import com.bgpixel.prettyboxformatter.linetype.LineType;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public interface Lineset {

    @NotNull
    static List<LineType> getSimpleLineLineset() {
        List<LineType> lineset = new ArrayList<>();
        lineset.add(LineType.LINE);
        lineset.add(LineType.DASH_DOUBLE);
        lineset.add(LineType.DASH_TRIPLE);
        lineset.add(LineType.DASH_QUADRUPLE);
        return lineset;
    }

    @NotNull
    static List<LineType> getFullLineLineset() {
        List<LineType> lineset = new ArrayList<>();
        lineset.add(LineType.LINE_DOUBLE);
        lineset.add(LineType.LINE_THICK);
        lineset.add(LineType.DASH_DOUBLE_THICK);
        lineset.add(LineType.DASH_TRIPLE_THICK);
        lineset.add(LineType.DASH_QUADRUPLE_THICK);
        lineset.add(LineType.LINE);
        lineset.add(LineType.DASH_DOUBLE);
        lineset.add(LineType.DASH_TRIPLE);
        lineset.add(LineType.DASH_QUADRUPLE);
        return lineset;
    }

    @NotNull
    static List<LineType> getBlockLineset() {
        List<LineType> lineset = new ArrayList<>();
        lineset.add(LineType.BLOCK_FULL);
        lineset.add(LineType.SHADED_BLOCK_DARK);
        lineset.add(LineType.SHADED_BLOCK_MEDIUM);
        lineset.add(LineType.SHADED_BLOCK_LIGHT);
        return lineset;
    }

    @NotNull
    static List<LineType> getAsciiLineset() {
        List<LineType> lineset = new ArrayList<>();
        lineset.add(LineType.STAR);
        lineset.add(LineType.PLUS_MINUS);
        lineset.add(LineType.DOT);
        return lineset;
    }

}
