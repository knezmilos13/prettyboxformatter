package com.bgpixel.prettyboxformatter;

import com.bgpixel.prettyboxformatter.data.SimpleBoxableObject;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.junit.runners.MethodSorters;

import java.util.ArrayList;
import java.util.List;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DemoTestMethods {

    private static final SimpleBoxableObject SIMPLE_BOXABLE_OBJECT =
            new SimpleBoxableObject(5, "John Johnson", true);

    private static final PrettyBoxConfiguration DEFAULT_CONFIGURATION =
            new PrettyBoxConfiguration.Builder().build();

    @Rule public TestName testNameRule = new TestName();

    @Before
    public void before() {
        PrettyBoxFormatter.setConfiguration(DEFAULT_CONFIGURATION);
        System.out.println("");
        System.out.println("");
        System.out.println("Test: " + testNameRule.getMethodName());
    }

    @Test
    public void _001_formatLinesManually() {
        List<String> lines = new ArrayList<>();
        lines.add("First line");
        lines.add("Second line");
        lines.add("");
        lines.add("Third line");
        System.out.println(PrettyBoxFormatter.format(lines));
    }

    @Test
    public void _002_printSimpleBoxableObject() {
        System.out.println(PrettyBoxFormatter.format(SIMPLE_BOXABLE_OBJECT));
    }

    @Test
    public void _003_printSimpleBoxableObject_prefixNewline() {
        PrettyBoxFormatter.setConfiguration(
                new PrettyBoxConfiguration.Builder()
                        .setPrefixEveryPrintWithNewline(true)
                        .build());
        System.out.println(PrettyBoxFormatter.format(SIMPLE_BOXABLE_OBJECT));
    }

    // TODO: demo per-call configuration with something better than "newline before printing"

}
