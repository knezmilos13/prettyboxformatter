package com.bgpixel.prettyboxformatter;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings({"WeakerAccess"})
public class PrettyBoxFormatter {

    private static final char TOP_LEFT_CORNER = '┌';
    private static final char MIDDLE_LEFT_CORNER = '├';
    private static final char BOTTOM_LEFT_CORNER = '└';
    private static final char TOP_RIGHT_CORNER = '┐';
    private static final char BOTTOM_RIGHT_CORNER = '┘';
    private static final char MIDDLE_RIGHT_CORNER = '┤';
    private static final char VERTICAL_LINE = '│';

    private static final String NEWLINE = System.getProperty("line.separator");

    @NotNull
    private static final PrettyBoxConfiguration DEFAULT_CONFIGURATION =
            new PrettyBoxConfiguration.Builder()
                    .setPrefixEveryPrintWithNewline(false)
                    .setCharsPerLine(80)
                    .setWrapContent(true)
                    .setBorderLeft(true)
                    .setBorderRight(true)
                    .setBorderTop(true)
                    .setBorderBottom(true) // add helper methods like for margin/padding
                    .setHorizontalPadding(1)
                    .setVerticalPadding(0)
                    .setMargin(0)
                    .build();

    private static final String INVALID_PER_CALL_CONFIGURATION_MESSAGE =
            "Warning: this PrettyBoxFormatter has been configured using an invalid per-call " +
                    "PrettyBoxConfiguration. Falling back to instance-level configuration!";
    private static final String INVALID_INSTANCE_LEVEL_CONFIGURATION_MESSAGE =
            "Warning: this PrettyBoxFormatter has been configured using an invalid instance-level " +
                    "PrettyBoxConfiguration. Falling back to default configuration!";

    /** Instance-level configuration. Combined with per-call instances (if given). */
    @NotNull private PrettyBoxConfiguration configuration = DEFAULT_CONFIGURATION;

    /** Set to true if client attempted to set an invalid instance-level configuration instance. If
     *  true, will use a default configuration and display a warning message with every printing
     *  call. */
    private boolean invalidConfiguration = false;

    /** The maximum width of the box (exact width if wrap is false) that can be used for content. */
    private int maxContentWidth;

    /** The maximum width (exact width if wrap is false) of horizontal lines used for horizontal
     *  edges and to split content into sections. */
    private int maxLineWidth;


    public PrettyBoxFormatter() {
        this(DEFAULT_CONFIGURATION);
    }

    public PrettyBoxFormatter(@NotNull PrettyBoxConfiguration configuration) {
        setConfiguration(configuration);
    }

    /** Sets an instance-level PrettyBoxConfiguration instance that will be used for all printing.
     *  Settings not defined in the given instance will fallback to default settings. Individual
     *  settings can be overridden by passing a PrettyBoxConfiguration with each printing call.<br/>
     *  If the resulting configuration is not valid, previous configuration will not be changed and
     *  a warning message will be output with all future printing calls. */
    public void setConfiguration(@NotNull PrettyBoxConfiguration configuration) {
        PrettyBoxConfiguration combinedConfiguration =
                PrettyBoxConfiguration.Builder.createFromInstance(DEFAULT_CONFIGURATION)
                        .applyFromInstance(configuration)
                        .build();

        invalidConfiguration = !validateConfiguration(combinedConfiguration);
        this.configuration = invalidConfiguration ? DEFAULT_CONFIGURATION : combinedConfiguration;

        maxContentWidth = determineMaxContentWidth(this.configuration);
        maxLineWidth = determineMaxLineWidth(this.configuration);
    }

    /** Returns the used instance-level PrettyBoxConfiguration instance. */
    @NotNull public PrettyBoxConfiguration getConfiguration() { return configuration; }


    // -------------------------------------------------------------------------------------- FORMAT

    /** Formats content provided by a PrettyBoxable instance into a pretty box using the
     *  instance-level PrettyBoxConfiguration */
    @NotNull
    public String format(@NotNull PrettyBoxable thingy) {
        return format(thingy.toStringLines());
    }

    /** Formats content provided by a PrettyBoxable instance into a pretty box using the given
     *  configuration instance. Any settings not defined in given instance will fallback to the
     *  instance-level configuration instance. */
    @NotNull
    public String format(@NotNull PrettyBoxable thingy,
                         @NotNull PrettyBoxConfiguration configuration) {
        return format(thingy.toStringLines(), configuration);
    }

    /** Formats given string lines into a pretty box using the instance-level PrettyBoxConfiguration. */
    @NotNull
    public String format(@NotNull List<String> lines) {
        FormattingTaskData taskData =
                prepareFormattingTaskData(lines, configuration, maxContentWidth, maxLineWidth);
        if(invalidConfiguration) taskData.markPrintInvalidInstanceLevelConfigMessage();
        return drawBox(taskData, configuration);
    }

    /** Formats given string lines instance into a pretty box using the given configuration
     *  instance. Any settings not defined in given instance will fallback to the instance-level
     *  configuration instance. */
    @NotNull
    public String format(@NotNull List<String> lines,
                         @NotNull PrettyBoxConfiguration configuration) {
        PrettyBoxConfiguration mergedConfig =
                PrettyBoxConfiguration.Builder.createFromInstance(DEFAULT_CONFIGURATION)
                        .applyFromInstance(configuration)
                        .build();

        boolean validPerCallConfiguration = validateConfiguration(mergedConfig);

        PrettyBoxConfiguration configurationToUse;
        int maxContentWidth, maxLineWidth;
        if(validPerCallConfiguration) {
            configurationToUse = mergedConfig;
            maxContentWidth = determineMaxContentWidth(configurationToUse);
            maxLineWidth = determineMaxLineWidth(configurationToUse);
        } else {
            // Note: if this.invalidConfiguration is true, this.configuration has the valid fallback
            configurationToUse = this.configuration;
            maxContentWidth = this.maxContentWidth;
            maxLineWidth = this.maxLineWidth;
        }

        FormattingTaskData taskData =
                prepareFormattingTaskData(lines, configurationToUse, maxContentWidth, maxLineWidth);
        if(invalidConfiguration) taskData.markPrintInvalidInstanceLevelConfigMessage();
        if(!validPerCallConfiguration) taskData.markPrintInvalidPerCallConfigMessage();

        return drawBox(taskData, configurationToUse);
    }


    // ------------------------------------------------------------------------------ MAIN ALGORITHM

    @SuppressWarnings("ConstantConditions") // We make sure it's not null
    @NotNull
    private FormattingTaskData prepareFormattingTaskData(
            @NotNull List<String> lines,
            @NotNull PrettyBoxConfiguration configuration,
            int maxContentWidth,
            int maxLineWidth) {

        FormattingTaskData taskData = new FormattingTaskData();
        taskData.setContentLines(splitLinesToFitBox(lines, maxContentWidth));

        // If wrap content is TRUE, make the box as wide as the longest line we have.
        if (configuration.getWrapContent()) {
            int longestContentWidth = 0;
            for (String line : taskData.getContentLines())
                longestContentWidth = Math.max(line.length(), longestContentWidth);
            taskData.setContentWidth(longestContentWidth);
            taskData.setLineWidth(taskData.getContentWidth()
                    + configuration.getPaddingLeft() + configuration.getPaddingRight());
        } else {
            taskData.setContentWidth(maxContentWidth);
            taskData.setLineWidth(maxLineWidth);
        }

        return taskData;
    }

    // stuff is nullable, but we make sure the default settings provide fallback non-null values
    @SuppressWarnings("ConstantConditions")
    @NotNull
    private String drawBox(@NotNull FormattingTaskData taskData,
                           @NotNull PrettyBoxConfiguration configuration) {

        ArrayList<String> lines = new ArrayList<>();

        // Optimization. Set length to 0 before every use.
        StringBuilder stringBuilder = new StringBuilder();

        // Terminology note: "draw" methods take StringBuilder and "draw" into it. "Generate"
        // methods generate and return a list of Strings (lines to be output). They also take
        // StringBuilder for optimization purposes, but its content after the call is irrelevant

        if(invalidConfiguration) lines.add(INVALID_PER_CALL_CONFIGURATION_MESSAGE);
        if(invalidConfiguration) lines.add(INVALID_INSTANCE_LEVEL_CONFIGURATION_MESSAGE);

        if(configuration.getPrefixEveryPrintWithNewline()) lines.add("");

        if(configuration.getMarginTop() != 0)
            lines.addAll(generateVerticalSpaces(configuration.getMarginTop()));

        if(configuration.getBorderTop()) {
            stringBuilder.setLength(0);
            drawOuterLine(true, stringBuilder, taskData.getLineWidth(), configuration);
            lines.add(stringBuilder.toString());
        }

        if(configuration.getPaddingTop() != 0) {
            stringBuilder.setLength(0);
            lines.addAll(generateVerticalPadding(stringBuilder, configuration.getPaddingTop(),
                    taskData.getLineWidth(), configuration));
        }

        List<String> contentLines = taskData.getContentLines();
        for (String contentLine : contentLines) {
            stringBuilder.setLength(0);
            if (contentLine.length() == 0)
                drawInnerLine(stringBuilder, taskData.getLineWidth(), configuration);
            else
                drawContentLine(stringBuilder, contentLine, taskData.getContentWidth(), configuration);
            lines.add(stringBuilder.toString());
        }

        if(configuration.getPaddingBottom() != 0) {
            stringBuilder.setLength(0);
            lines.addAll(generateVerticalPadding(stringBuilder, configuration.getPaddingBottom(),
                    taskData.getLineWidth(), configuration));
        }

        if(configuration.getBorderBottom()) {
            stringBuilder.setLength(0);
            drawOuterLine(false, stringBuilder, taskData.getLineWidth(), configuration);
            lines.add(stringBuilder.toString());
        }

        if(configuration.getMarginBottom() != 0)
            lines.addAll(generateVerticalSpaces(configuration.getMarginBottom()));

        return JavaUtil.join(NEWLINE, lines);
    }

    @NotNull
    private List<String> splitLinesToFitBox(@NotNull List<String> lines, int contentWidth) {
        List<String> splitLines = new ArrayList<>();
        for(String line : lines) {
            if(line.length() <= contentWidth) splitLines.add(line);
            else splitLines.addAll(splitLineEveryNChars(line, contentWidth));
        }
        return splitLines;
    }

    @NotNull
    private List<String> generateVerticalSpaces(int verticalMargin) {
        List<String> lines = new ArrayList<>();
        for(int i = 0; i < verticalMargin; i++) lines.add("");
        return lines;
    }

    /** Draws a top or bottom outer line (i.e. top or bottom border). */
    @SuppressWarnings("ConstantConditions") // @see drawBox
    private void drawOuterLine(boolean top,
                                 @NotNull StringBuilder stringBuilder,
                                 int lineWidth,
                                 @NotNull PrettyBoxConfiguration configuration) {
        stringBuilder.append(getHorizontalSpaces(configuration.getMarginLeft()));

        if(configuration.getBorderLeft())
            stringBuilder.append(top? TOP_LEFT_CORNER : BOTTOM_LEFT_CORNER);

        stringBuilder.append(getDoubleDivider(lineWidth));

        if(configuration.getBorderRight())
            stringBuilder.append(top? TOP_RIGHT_CORNER : BOTTOM_RIGHT_CORNER);

        stringBuilder.append(getHorizontalSpaces(configuration.getMarginRight()));
    }

    @SuppressWarnings("ConstantConditions") // @see drawBox
    @NotNull
    private List<String> generateVerticalPadding(@NotNull StringBuilder stringBuilder,
                                                 int padding,
                                                 int lineWidth,
                                                 @NotNull PrettyBoxConfiguration configuration) {
        List<String> lines = new ArrayList<>();

        for(int i = 0; i < padding; i++) {
            stringBuilder.setLength(0);

            stringBuilder
                    .append(getHorizontalSpaces(configuration.getMarginLeft()));

            if(configuration.getBorderLeft())
                stringBuilder.append(VERTICAL_LINE);

            stringBuilder
                    .append(getHorizontalSpaces(lineWidth));

            if(configuration.getBorderRight())
                stringBuilder.append(VERTICAL_LINE);

            stringBuilder
                    .append(getHorizontalSpaces(configuration.getMarginRight()));

            lines.add(stringBuilder.toString());
        }

        return lines;
    }

    @SuppressWarnings("ConstantConditions") // @see drawBox
    private void drawInnerLine(@NotNull StringBuilder stringBuilder,
                               int lineWidth,
                               @NotNull PrettyBoxConfiguration configuration) {
        stringBuilder
                .append(getHorizontalSpaces(configuration.getMarginLeft()));

        if(configuration.getBorderLeft())
            stringBuilder.append(MIDDLE_LEFT_CORNER);

        stringBuilder.append(getSingleDivider(lineWidth));

        if(configuration.getBorderRight())
            stringBuilder.append(MIDDLE_RIGHT_CORNER);

        stringBuilder
                .append(getHorizontalSpaces(configuration.getMarginRight()));
    }

    @SuppressWarnings("ConstantConditions") // @see drawBox
    private void drawContentLine(@NotNull StringBuilder stringBuilder,
                                 @NotNull String line,
                                 int contentWidth,
                                 @NotNull PrettyBoxConfiguration configuration) {

        stringBuilder.append(getHorizontalSpaces(configuration.getMarginLeft()));

        if(configuration.getBorderLeft()) stringBuilder.append(VERTICAL_LINE);

        stringBuilder
                .append(getHorizontalSpaces(configuration.getPaddingLeft()))
                .append(line);

        int rightPadding = configuration.getPaddingRight() + (contentWidth - line.length());
        stringBuilder
                .append(getHorizontalSpaces(rightPadding));

        if(configuration.getBorderRight()) stringBuilder.append(VERTICAL_LINE);

        stringBuilder.append(getHorizontalSpaces(configuration.getMarginRight()));
    }


    // ------------------------------------------------------------------------------------ INTERNAL

    /** Returns the maximum width of the box (exact width if wrap is false) that can be used for
     *  content. Can return invalid (zero, negative) values if configuration is invalid (e.g. too
     *  large padding, too small width) */
    @SuppressWarnings("ConstantConditions") // @see drawBox
    private int determineMaxContentWidth(@NotNull PrettyBoxConfiguration configuration) {
        int numSides = (configuration.getBorderLeft()? 1 : 0)
                + (configuration.getBorderRight()? 1 : 0);

        return configuration.getCharsPerLine()
                - configuration.getPaddingLeft()
                - configuration.getPaddingRight()
                - configuration.getMarginLeft()
                - configuration.getMarginRight()
                - numSides;
    }

    /** Returns true if given configuration is valid. Checks if there is enough space to actually
     *  print out content inside of the box. */
    private boolean validateConfiguration(@NotNull PrettyBoxConfiguration configuration) {
        int maxContentWidth = determineMaxContentWidth(configuration);
        return maxContentWidth > 0;
    }

    /** Returns the maximum width (exact width if wrap is false) of horizontal lines used for
     *  horizontal edges and to split content into sections. Can return invalid (zero, negative)
     *  values if configuration is invalid (e.g. too large margin, too small width) */
    @SuppressWarnings("ConstantConditions") // @see drawBox
    private int determineMaxLineWidth(@NotNull PrettyBoxConfiguration configuration) {
        int numSides = (configuration.getBorderLeft()? 1 : 0)
                + (configuration.getBorderRight()? 1 : 0);

        return configuration.getCharsPerLine()
                - configuration.getMarginLeft()
                - configuration.getMarginRight()
                - numSides;
    }

    @NotNull
    private List<String> splitLineEveryNChars(@NotNull String string, int partitionSize) {
        List<String> parts = new ArrayList<>();
        int len = string.length();
        for (int i=0; i<len; i+=partitionSize)
            parts.add(string.substring(i, Math.min(len, i + partitionSize)));
        return parts;
    }

    @NotNull
    private String getDoubleDivider(int length) {
        return getNCharacterString("─", length);
    }

    @NotNull
    private String getSingleDivider(int length) {
        return getNCharacterString("┄", length);
    }

    @NotNull
    private String getHorizontalSpaces(int length) {
        return getNCharacterString(" ", length);
    }

    @NotNull
    private String getNCharacterString(@NotNull String character, int length) {
        StringBuilder outputBuffer = new StringBuilder(length);
        for (int i = 0; i < length; i++) outputBuffer.append(character);
        return outputBuffer.toString();
    }

}
