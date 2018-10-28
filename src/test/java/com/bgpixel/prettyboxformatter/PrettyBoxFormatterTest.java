package com.bgpixel.prettyboxformatter;

import com.bgpixel.prettyboxformatter.data.SimpleBoxableObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class PrettyBoxFormatterTest {

    private static final String NLN = System.getProperty("line.separator");

    private static final SimpleBoxableObject SIMPLE_BOXABLE_OBJECT =
            new SimpleBoxableObject(5, "John Johnson", true);

    private PrettyBoxFormatter pbFormatter;

    @Before
    public void before() {
        pbFormatter = new PrettyBoxFormatter();
    }

    private static final String globalConfigWithPerCallConfig =
            "┌─────────────────────────────────────────┐" + NLN +
            "│                                         │" + NLN +
            "│     Number of apples: 5                 │" + NLN +
            "│     Apple seller name: John Johnson     │" + NLN +
            "│     Has green apples: true              │" + NLN +
            "│                                         │" + NLN +
            "└─────────────────────────────────────────┘";
    @Test
    public void globalConfigWithPerCallConfig() {
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
        Assert.assertEquals(globalConfigWithPerCallConfig, result);
    }

    // Don't want to check EVERYTHING, just some basic stuff. Check if some of the stuff that was
    // not set is not null and if some of the shorthand setters work.
    @Test
    public void basicMergeConfigurationChecks() {
        pbFormatter.setConfiguration(
                new PrettyBoxConfiguration.Builder()
                        .setHorizontalMargin(10)
                        .setVerticalMargin(1)
                        .setPadding(0)
                        .build());
        PrettyBoxConfiguration mergedConfiguration = pbFormatter.getConfiguration();

        Assert.assertNotNull(mergedConfiguration.getWrapContent());
        Assert.assertEquals(Integer.valueOf(10), mergedConfiguration.getMarginLeft());
        Assert.assertEquals(Integer.valueOf(1), mergedConfiguration.getMarginTop());
        Assert.assertEquals(Integer.valueOf(0), mergedConfiguration.getPaddingBottom());
    }

}
