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
        if(invalidConfiguration) taskData.setPrintInvalidInstanceLevelConfigMessage(true);
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

        boolean invalidPerCallConfiguration = !validateConfiguration(mergedConfig);

        PrettyBoxConfiguration configurationToUse;
        int maxContentWidth, maxLineWidth;
        if(invalidPerCallConfiguration) {
            // Note: if this.invalidConfiguration is true, this.configuration has the valid fallback
            configurationToUse = this.configuration;
            maxContentWidth = this.maxContentWidth;
            maxLineWidth = this.maxLineWidth;
        } else {
            configurationToUse = mergedConfig;
            maxContentWidth = determineMaxContentWidth(configurationToUse);
            maxLineWidth = determineMaxLineWidth(configurationToUse);
        }

        FormattingTaskData taskData =
                prepareFormattingTaskData(lines, configurationToUse, maxContentWidth, maxLineWidth);
        if(invalidConfiguration) taskData.setPrintInvalidInstanceLevelConfigMessage(true);
        if(invalidPerCallConfiguration) taskData.setPrintInvalidPerCallConfigMessage(true);

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
        taskData.setLines(splitLinesToFitBox(lines, maxContentWidth));

        // If wrap content is TRUE, make the box as wide as the longest line we have.
        if (configuration.getWrapContent()) {
            int longestContentWidth = 0;
            for (String line : taskData.getLines())
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

        StringBuilder stringBuilder = new StringBuilder();

        if(invalidConfiguration)
            stringBuilder.append(INVALID_PER_CALL_CONFIGURATION_MESSAGE).append(NEWLINE);
        if(invalidConfiguration)
            stringBuilder.append(INVALID_INSTANCE_LEVEL_CONFIGURATION_MESSAGE).append(NEWLINE);

        if (configuration.getPrefixEveryPrintWithNewline()) stringBuilder.append(NEWLINE);

        drawVerticalSpaces(stringBuilder, configuration.getMarginTop());

        drawTopLine(stringBuilder, taskData.getLineWidth(), configuration);

        drawVerticalPadding(stringBuilder, configuration.getPaddingTop(),
                taskData.getLineWidth(), configuration);

        for (String line : taskData.getLines()) {
            if (line.length() == 0) drawInnerLine(stringBuilder, taskData.getLineWidth(), configuration);
            else drawContentLine(stringBuilder, line, taskData.getContentWidth(), configuration);
        }

        drawVerticalPadding(stringBuilder, configuration.getPaddingBottom(),
                taskData.getLineWidth(), configuration);

        drawBottomLine(stringBuilder, taskData.getLineWidth(), configuration);

        drawVerticalSpaces(stringBuilder, configuration.getMarginBottom());

        return stringBuilder.toString();
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

    private void drawVerticalSpaces(@NotNull StringBuilder stringBuilder,
                                    int verticalMargin) {
        for(int i = 0; i < verticalMargin; i++) stringBuilder.append(NEWLINE);
    }

    @SuppressWarnings("ConstantConditions") // @see drawBox
    private void drawTopLine(@NotNull StringBuilder stringBuilder,
                             int lineWidth,
                             @NotNull PrettyBoxConfiguration configuration) {
        if(!configuration.getBorderTop()) return;

        stringBuilder
                .append(getHorizontalSpaces(configuration.getMarginLeft()));

        if(configuration.getBorderLeft())
            stringBuilder.append(TOP_LEFT_CORNER);

        stringBuilder.append(getDoubleDivider(lineWidth));

        if(configuration.getBorderRight())
            stringBuilder.append(TOP_RIGHT_CORNER);

        stringBuilder
                .append(getHorizontalSpaces(configuration.getMarginRight()));

        stringBuilder.append(NEWLINE);
    }

    @SuppressWarnings("ConstantConditions") // @see drawBox
    private void drawVerticalPadding(@NotNull StringBuilder stringBuilder,
                                     int padding,
                                     int lineWidth,
                                     @NotNull PrettyBoxConfiguration configuration) {
        for(int i = 0; i < padding; i++) {
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

            stringBuilder.append(NEWLINE);
        }
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

        stringBuilder.append(NEWLINE);
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

        stringBuilder.append(NEWLINE);
    }

    @SuppressWarnings("ConstantConditions") // @see drawBox
    private void drawBottomLine(@NotNull StringBuilder stringBuilder,
                                int lineWidth,
                                @NotNull PrettyBoxConfiguration configuration) {
        if(!configuration.getBorderBottom()) return;

        stringBuilder
                .append(getHorizontalSpaces(configuration.getMarginLeft()));

        if(configuration.getBorderLeft())
            stringBuilder.append(BOTTOM_LEFT_CORNER);

        stringBuilder.append(getDoubleDivider(lineWidth));

        if(configuration.getBorderRight())
            stringBuilder.append(BOTTOM_RIGHT_CORNER);

        stringBuilder
                .append(getHorizontalSpaces(configuration.getMarginRight()));
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
