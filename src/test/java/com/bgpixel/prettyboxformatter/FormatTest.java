package com.bgpixel.prettyboxformatter;

import com.bgpixel.prettyboxformatter.data.SimpleBoxableObject;
import org.junit.*;
import org.junit.rules.TestName;
import org.junit.runners.MethodSorters;

import java.util.ArrayList;
import java.util.List;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class FormatTest {

    private static final String NLN = System.getProperty("line.separator");

    @Rule public TestName testNameRule = new TestName();

    private static final SimpleBoxableObject SIMPLE_BOXABLE_OBJECT =
            new SimpleBoxableObject(5, "John Johnson", true);

    private PrettyBoxFormatter pbFormatter;

    @Before
    public void before() {
        pbFormatter = new PrettyBoxFormatter();
        System.out.println("");
        System.out.println("");
        System.out.println("Test: " + testNameRule.getMethodName());
    }

    private static final String test001expectedResult =
            "┌─────────────┐" + NLN +
            "│ First line  │" + NLN +
            "│ Second line │" + NLN +
            "├┄┄┄┄┄┄┄┄┄┄┄┄┄┤" + NLN +
            "│ Third line  │" + NLN +
            "└─────────────┘";
    @Test
    public void _001_formatLinesManually() {
        List<String> lines = new ArrayList<>();
        lines.add("First line");
        lines.add("Second line");
        lines.add("");
        lines.add("Third line");

        String result = pbFormatter.format(lines);
        Assert.assertEquals(test001expectedResult, result);
        System.out.println(result);
    }

    private static final String test002expectedResult =
            "┌─────────────────────────────────┐" + NLN +
            "│ Number of apples: 5             │" + NLN +
            "│ Apple seller name: John Johnson │" + NLN +
            "│ Has green apples: true          │" + NLN +
            "└─────────────────────────────────┘";
    @Test
    public void _002_printSimpleBoxableObject() {
        String result = pbFormatter.format(SIMPLE_BOXABLE_OBJECT);
        Assert.assertEquals(test002expectedResult, result);
        System.out.println(result);
    }

    private static final String test003expectedResult =
            NLN +
            "┌─────────────────────────────────┐" + NLN +
            "│ Number of apples: 5             │" + NLN +
            "│ Apple seller name: John Johnson │" + NLN +
            "│ Has green apples: true          │" + NLN +
            "└─────────────────────────────────┘";
    @Test
    public void _003_prefixNewline() {
        String result = pbFormatter.format(SIMPLE_BOXABLE_OBJECT,
                new PrettyBoxConfiguration.Builder()
                        .setPrefixEveryPrintWithNewline(true)
                        .build());
        Assert.assertEquals(test003expectedResult, result);
        System.out.println(result);
    }

    private static final String test004expectedResult =
            "┌──────────────────────────────┐" + NLN +
            "│ Number of apples: 5          │" + NLN +
            "│ Apple seller name: John John │" + NLN +
            "│ son                          │" + NLN +
            "│ Has green apples: true       │" + NLN +
            "└──────────────────────────────┘";
    @Test
    public void _004_width_smaller_fixed() {
        String result = pbFormatter.format(SIMPLE_BOXABLE_OBJECT,
                new PrettyBoxConfiguration.Builder()
                        .setCharsPerLine(32)
                        .setWrapContent(false)
                        .build());
        Assert.assertEquals(test004expectedResult, result);
        System.out.println(result);
    }

    private static final String test005expectedResult =
            "┌──────────────────────────────────────────────────────────┐" + NLN +
            "│ Number of apples: 5                                      │" + NLN +
            "│ Apple seller name: John Johnson                          │" + NLN +
            "│ Has green apples: true                                   │" + NLN +
            "└──────────────────────────────────────────────────────────┘";
    @Test
    public void _005_width_larger_fixed() {
        String result = pbFormatter.format(SIMPLE_BOXABLE_OBJECT,
                new PrettyBoxConfiguration.Builder()
                        .setCharsPerLine(60)
                        .setWrapContent(false)
                        .build());
        Assert.assertEquals(test005expectedResult, result);
        System.out.println(result);
    }

    private static final String test006expectedResult =
            "┌─────────────────────────────────┐" + NLN +
            "│ Number of apples: 5             │" + NLN +
            "│ Apple seller name: John Johnson │" + NLN +
            "│ Has green apples: true          │" + NLN +
            "└─────────────────────────────────┘";
    @Test
    public void _006_width_wrap() {
        String result = pbFormatter.format(SIMPLE_BOXABLE_OBJECT,
                new PrettyBoxConfiguration.Builder()
                        .setCharsPerLine(60)
                        .setWrapContent(true)
                        .build());
        Assert.assertEquals(test006expectedResult, result);
        System.out.println(result);
    }

    private static final String test007expectedResult =
            "┌───────────────────────────────────────────────────┐" + NLN +
            "│                                                   │" + NLN +
            "│          Number of apples: 5                      │" + NLN +
            "│          Apple seller name: John Johnson          │" + NLN +
            "│          Has green apples: true                   │" + NLN +
            "│                                                   │" + NLN +
            "└───────────────────────────────────────────────────┘";
    @Test
    public void _007_padding() {
        String result = pbFormatter.format(SIMPLE_BOXABLE_OBJECT,
                new PrettyBoxConfiguration.Builder()
                        .setHorizontalPadding(10)
                        .setVerticalPadding(1)
                        .build());
        Assert.assertEquals(test007expectedResult, result);
        System.out.println(result);
    }

    private static final String test008expectedResult =
            NLN +
            "          ┌─────────────────────────────────┐          " + NLN +
            "          │ Number of apples: 5             │          " + NLN +
            "          │ Apple seller name: John Johnson │          " + NLN +
            "          │ Has green apples: true          │          " + NLN +
            "          └─────────────────────────────────┘          " + NLN;
    @Test
    public void _008_margin() {
        String result = pbFormatter.format(SIMPLE_BOXABLE_OBJECT,
                new PrettyBoxConfiguration.Builder()
                        .setHorizontalMargin(10)
                        .setVerticalMargin(1)
                        .build());
        Assert.assertEquals(test008expectedResult, result);
        System.out.println(result);
    }

    private static final String test009expectedResult =
            "┌─────────────────────────────────────────┐" + NLN +
            "│                                         │" + NLN +
            "│     Number of apples: 5                 │" + NLN +
            "│     Apple seller name: John Johnson     │" + NLN +
            "│     Has green apples: true              │" + NLN +
            "│                                         │" + NLN +
            "└─────────────────────────────────────────┘";
    @Test
    public void _009_globalConfigWithPerCallConfig() {
        pbFormatter.setConfiguration(
                new PrettyBoxConfiguration.Builder()
                        .setHorizontalMargin(10)
                        .setVerticalMargin(1)
                        .build());
        String result = pbFormatter.format(SIMPLE_BOXABLE_OBJECT,
                new PrettyBoxConfiguration.Builder()
                        .setHorizontalPadding(5)
                        .setVerticalPadding(1)
                        .build());
        Assert.assertEquals(test009expectedResult, result);
        System.out.println(result);
    }

    private static final String test010expectedResult =
            "┌───────────────────────────────────────────────────────────────────────────────" + NLN +
            "│ Number of apples: 5                                                           " + NLN +
            "│ Apple seller name: John Johnson                                               " + NLN +
            "│ Has green apples: true                                                        " + NLN +
            "└───────────────────────────────────────────────────────────────────────────────";
    @Test
    public void _010_notClosedOnRight() {
        String result = pbFormatter.format(SIMPLE_BOXABLE_OBJECT,
                new PrettyBoxConfiguration.Builder()
                        .setWrapContent(false)
                        .setBorderRight(false)
                        .build());
        Assert.assertEquals(test010expectedResult, result);
        System.out.println(result);
    }

    private static final String test011expectedResult =
            "─────────────────────────────────┐" + NLN +
            " Number of apples: 5             │" + NLN +
            " Apple seller name: John Johnson │" + NLN +
            " Has green apples: true          │" + NLN +
            "─────────────────────────────────┘";
    @Test
    public void _011_notClosedOnLeft() {
        String result = pbFormatter.format(SIMPLE_BOXABLE_OBJECT,
                new PrettyBoxConfiguration.Builder()
                        .setWrapContent(true)
                        .setBorderLeft(false)
                        .build());
        Assert.assertEquals(test011expectedResult, result);
        System.out.println(result);
    }

    private static final String test012expectedResult =
            "─────────────────────────────────" + NLN +
            " Number of apples: 5             " + NLN +
            " Apple seller name: John Johnson " + NLN +
            " Has green apples: true          " + NLN +
            "─────────────────────────────────";
    @Test
    public void _012_noVerticalBorders() {
        String result = pbFormatter.format(SIMPLE_BOXABLE_OBJECT,
                new PrettyBoxConfiguration.Builder()
                        .setWrapContent(true)
                        .setVerticalBorders(false)
                        .build());
        Assert.assertEquals(test012expectedResult, result);
        System.out.println(result);
    }

    private static final String test013expectedResult =
            "│ Number of apples: 5             │" + NLN +
            "│ Apple seller name: John Johnson │" + NLN +
            "│ Has green apples: true          │";
    @Test
    public void _013_noHorizontalBorders() {
        String result = pbFormatter.format(SIMPLE_BOXABLE_OBJECT,
                new PrettyBoxConfiguration.Builder()
                        .setWrapContent(true)
                        .setHorizontalBorders(false)
                        .build());
        Assert.assertEquals(test013expectedResult, result);
        System.out.println(result);
    }

    private static final String test014expectedResult =
            " Number of apples: 5             " + NLN +
            " Apple seller name: John Johnson " + NLN +
            " Has green apples: true          ";
    @Test
    public void _014_noBorders() {
        String result = pbFormatter.format(SIMPLE_BOXABLE_OBJECT,
                new PrettyBoxConfiguration.Builder()
                        .setWrapContent(true)
                        .setBorders(false)
                        .build());
        Assert.assertEquals(test014expectedResult, result);
        System.out.println(result);
    }

}
