package com.bgpixel.prettyboxformatter;

import com.bgpixel.prettyboxformatter.data.SimpleBoxableObject;
import com.bgpixel.prettyboxformatter.lines.LineType;
import org.junit.*;
import org.junit.rules.TestName;
import org.junit.runners.MethodSorters;

import java.util.ArrayList;
import java.util.List;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class VariousConfigurationsTest {

    private static final String NLN = System.getProperty("line.separator");

    @Rule public final TestName testNameRule = new TestName();

    private static final SimpleBoxableObject SIMPLE_BOXABLE_OBJECT =
            new SimpleBoxableObject(5, "John Johnson", true);

    private PrettyBoxFormatter pbFormatter;

    @Before
    public void before() {
        pbFormatter = new PrettyBoxFormatter();
        System.out.println();
        System.out.println();
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
            " " + NLN +
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
            " " + NLN +
            "          ┌─────────────────────────────────┐          " + NLN +
            "          │ Number of apples: 5             │          " + NLN +
            "          │ Apple seller name: John Johnson │          " + NLN +
            "          │ Has green apples: true          │          " + NLN +
            "          └─────────────────────────────────┘          " + NLN +
            " ";
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
            "┌───────────────────────────────────────────────────────────────────────────────" + NLN +
            "│ Number of apples: 5                                                           " + NLN +
            "│ Apple seller name: John Johnson                                               " + NLN +
            "│ Has green apples: true                                                        " + NLN +
            "└───────────────────────────────────────────────────────────────────────────────";
    @Test
    public void _009_notClosedOnRight() {
        String result = pbFormatter.format(SIMPLE_BOXABLE_OBJECT,
                new PrettyBoxConfiguration.Builder()
                        .setWrapContent(false)
                        .setBorderRight(false)
                        .build());
        Assert.assertEquals(test009expectedResult, result);
        System.out.println(result);
    }

    private static final String test010expectedResult =
            "─────────────────────────────────┐" + NLN +
            " Number of apples: 5             │" + NLN +
            " Apple seller name: John Johnson │" + NLN +
            " Has green apples: true          │" + NLN +
            "─────────────────────────────────┘";
    @Test
    public void _010_notClosedOnLeft() {
        String result = pbFormatter.format(SIMPLE_BOXABLE_OBJECT,
                new PrettyBoxConfiguration.Builder()
                        .setBorderLeft(false)
                        .build());
        Assert.assertEquals(test010expectedResult, result);
        System.out.println(result);
    }

    private static final String test011expectedResult =
            "─────────────────────────────────" + NLN +
            " Number of apples: 5             " + NLN +
            " Apple seller name: John Johnson " + NLN +
            " Has green apples: true          " + NLN +
            "─────────────────────────────────";
    @Test
    public void _011_noVerticalBorders() {
        String result = pbFormatter.format(SIMPLE_BOXABLE_OBJECT,
                new PrettyBoxConfiguration.Builder()
                        .setVerticalBorders(false)
                        .build());
        Assert.assertEquals(test011expectedResult, result);
        System.out.println(result);
    }

    private static final String test012expectedResult =
            "│ Number of apples: 5             │" + NLN +
            "│ Apple seller name: John Johnson │" + NLN +
            "│ Has green apples: true          │";
    @Test
    public void _012_noHorizontalBorders() {
        String result = pbFormatter.format(SIMPLE_BOXABLE_OBJECT,
                new PrettyBoxConfiguration.Builder()
                        .setHorizontalBorders(false)
                        .build());
        Assert.assertEquals(test012expectedResult, result);
        System.out.println(result);
    }

    private static final String test013expectedResult =
            " Number of apples: 5             " + NLN +
            " Apple seller name: John Johnson " + NLN +
            " Has green apples: true          ";
    @Test
    public void _013_noBorders() {
        String result = pbFormatter.format(SIMPLE_BOXABLE_OBJECT,
                new PrettyBoxConfiguration.Builder()
                        .setBorders(false)
                        .build());
        Assert.assertEquals(test013expectedResult, result);
        System.out.println(result);
    }

    private static final String test014expectedResult =
            "┌─────────────────────────────────┐" + NLN +
            "│ SimpleBoxableObject             │" + NLN +
            "├┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┤" + NLN +
            "│ Number of apples: 5             │" + NLN +
            "│ Apple seller name: John Johnson │" + NLN +
            "│ Has green apples: true          │" + NLN +
            "└─────────────────────────────────┘";
    @Test
    public void _014_header() {
        String result = pbFormatter.format(SIMPLE_BOXABLE_OBJECT,
                new PrettyBoxConfiguration.Builder()
                        .setHeaderMetadata(new ArrayList<BoxMetaData>() {{
                            add(BoxMetaData.SHORT_CLASS_NAME);
                        }})
                        .build());
        Assert.assertEquals(test014expectedResult, result);
        System.out.println(result);
    }

    private static final String test015expectedResult =
            "┌─────────────────────────────────┐" + NLN +
            "│ Number of apples: 5             │" + NLN +
            "│ Apple seller name: John Johnson │" + NLN +
            "│ Has green apples: true          │" + NLN +
            "├┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┤" + NLN +
            "│ SimpleBoxableObject             │" + NLN +
            "└─────────────────────────────────┘";
    @Test
    public void _015_footer() {
        String result = pbFormatter.format(SIMPLE_BOXABLE_OBJECT,
                new PrettyBoxConfiguration.Builder()
                        .setFooterMetadata(new ArrayList<BoxMetaData>() {{
                            add(BoxMetaData.SHORT_CLASS_NAME);
                        }})
                        .build());
        Assert.assertEquals(test015expectedResult, result);
        System.out.println(result);
    }

    private static final String test016expectedResult =
            "┌─────────────────────────────────┐" + NLN +
            "│ Status of southern apple stall  │" + NLN +
            "├┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┤" + NLN +
            "│ Number of apples: 5             │" + NLN +
            "│ Apple seller name: John Johnson │" + NLN +
            "│ Has green apples: true          │" + NLN +
            "└─────────────────────────────────┘";
    @Test
    public void _016_title() {
        String result = pbFormatter.format("Status of southern apple stall", SIMPLE_BOXABLE_OBJECT);
        Assert.assertEquals(test016expectedResult, result);
        System.out.println(result);
    }

    private static final String test017expectedResult =
            "┌─────────────────────────────────┐" + NLN +
            "│ Status of southern apple stall  │" + NLN +
            "│ SimpleBoxableObject             │" + NLN +
            "├┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┤" + NLN +
            "│ Number of apples: 5             │" + NLN +
            "│ Apple seller name: John Johnson │" + NLN +
            "│ Has green apples: true          │" + NLN +
            "└─────────────────────────────────┘";
    @Test
    public void _017_title_and_header() {
        String result = pbFormatter.format("Status of southern apple stall", SIMPLE_BOXABLE_OBJECT,
                new PrettyBoxConfiguration.Builder()
                        .setHeaderMetadata(new ArrayList<BoxMetaData>() {{
                            add(BoxMetaData.SHORT_CLASS_NAME);
                        }})
                        .build());
        Assert.assertEquals(test017expectedResult, result);
        System.out.println(result);
    }

    private static final String test018expectedResult =
            "+-------------+" + NLN +
            "| First line  |" + NLN +
            "| Second line |" + NLN +
            "+             +" + NLN +
            "| Third line  |" + NLN +
            "+-------------+";
    @Test
    public void _018_differentLineTypes() {
        List<String> lines = new ArrayList<>();
        lines.add("First line");
        lines.add("Second line");
        lines.add("");
        lines.add("Third line");

        String result = pbFormatter.format(lines,
                new PrettyBoxConfiguration.Builder()
                        .setBorderLineType(LineType.PLUS_MINUS)
                        .setInnerLineType(LineType.SPACE)
                        .build());
        Assert.assertEquals(test018expectedResult, result);
        System.out.println(result);
    }

}
