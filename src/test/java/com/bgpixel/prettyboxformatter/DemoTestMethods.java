package com.bgpixel.prettyboxformatter;

import com.bgpixel.prettyboxformatter.data.SimpleBoxableObject;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class DemoTestMethods {

    @Test
    public void formatLinesManually() {
        List<String> lines = new ArrayList<>();
        lines.add("First line");
        lines.add("Second line");
        lines.add("");
        lines.add("Third line");
        System.out.println(PrettyBoxFormatter.format(lines));
    }

    @Test
    public void printSimpleBoxableObject() {
        SimpleBoxableObject simpleBoxableObject = new SimpleBoxableObject(5, "John Johnson", true);
        System.out.println(PrettyBoxFormatter.format(simpleBoxableObject));
    }

}
