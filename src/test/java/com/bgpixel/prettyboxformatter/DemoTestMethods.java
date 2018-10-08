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
    public void _003_prefixNewline() {
        System.out.println(PrettyBoxFormatter.format(SIMPLE_BOXABLE_OBJECT,
                new PrettyBoxConfiguration.Builder()
                        .setPrefixEveryPrintWithNewline(true)
                        .build()));
    }

    @Test
    public void _004_width_smaller_fixed() {
        System.out.println(PrettyBoxFormatter.format(SIMPLE_BOXABLE_OBJECT,
                new PrettyBoxConfiguration.Builder()
                        .setCharsPerLine(32)
                        .setWrapContent(false)
                        .build()));
    }

    @Test
    public void _005_width_larger_fixed() {
        System.out.println(PrettyBoxFormatter.format(SIMPLE_BOXABLE_OBJECT,
                new PrettyBoxConfiguration.Builder()
                        .setCharsPerLine(60)
                        .setWrapContent(false)
                        .build()));
    }

    @Test
    public void _006_width_wrap() {
        System.out.println(
                PrettyBoxFormatter.format(SIMPLE_BOXABLE_OBJECT,
                new PrettyBoxConfiguration.Builder()
                        .setCharsPerLine(60)
                        .setWrapContent(true)
                        .build())
        );
    }

    @Test
    public void _007_padding() {
        System.out.println(PrettyBoxFormatter.format(SIMPLE_BOXABLE_OBJECT,
                new PrettyBoxConfiguration.Builder()
                        .setHorizontalPadding(10)
                        .setVerticalPadding(1)
                        .build()));
    }

    @Test
    public void _008_margin() {
        System.out.println(PrettyBoxFormatter.format(SIMPLE_BOXABLE_OBJECT,
                new PrettyBoxConfiguration.Builder()
                        .setHorizontalMargin(10)
                        .setVerticalMargin(1)
                        .build()));
    }

    @Test
    public void _009_notClosedOnRight() {
        System.out.println(PrettyBoxFormatter.format(SIMPLE_BOXABLE_OBJECT,
                new PrettyBoxConfiguration.Builder()
                        .setHorizontalMargin(10)
                        .setVerticalMargin(1)
                        .setHorizontalPadding(5)
                        .setVerticalPadding(1)
                        .setCharsPerLine(80)
                        .setWrapContent(false)
                        .setCloseOnTheRight(false)
                        .build()));
    }

    @Test
    public void _010_globalConfigWithPerCallConfig() {
        PrettyBoxFormatter.setConfiguration(
                new PrettyBoxConfiguration.Builder()
                        .setHorizontalMargin(10)
                        .setVerticalMargin(1)
                        .build());
        System.out.println(PrettyBoxFormatter.format(SIMPLE_BOXABLE_OBJECT,
                new PrettyBoxConfiguration.Builder()
                        .setHorizontalPadding(5)
                        .setVerticalPadding(1)
                        .build()));
    }

}
