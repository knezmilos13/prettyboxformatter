package com.bgpixel.prettyboxformatter;

import com.bgpixel.prettyboxformatter.line.LineWithLevel;
import com.bgpixel.prettyboxformatter.line.LineWithType;
import com.bgpixel.prettyboxformatter.linetype.LineType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

@SuppressWarnings({"WeakerAccess"})
public class PrettyBoxFormatter {

    private static final String NEWLINE = System.getProperty("line.separator");

    @NotNull
    private static final PrettyBoxConfiguration DEFAULT_CONFIGURATION =
            new PrettyBoxConfiguration.Builder()
                    .setPrefixEveryPrintWithNewline(false)
                    .setCharsPerLine(80)
                    .setWrapContent(true)
                    .setBorders(true)
                    .setBorderLineType(LineType.LINE)
                    .setInnerLineType(LineType.DASH_TRIPLE)
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
     *  instance-level PrettyBoxConfiguration. */
    @NotNull
    public String format(@NotNull PrettyBoxable prettyBoxable) {
        return runFormattingTask(null, prettyBoxable.toStringLines(), null, prettyBoxable);
    }

    /** Formats content provided by a PrettyBoxable instance into a pretty box using the given
     *  configuration instance. Any settings not defined in given instance will fallback to the
     *  instance-level configuration instance. */
    @NotNull
    public String format(@NotNull PrettyBoxable prettyBoxable,
                         @NotNull PrettyBoxConfiguration configuration) {
        return runFormattingTask(null, prettyBoxable.toStringLines(), configuration, prettyBoxable);
    }

    /** Formats given string lines into a pretty box using the instance-level
     *  PrettyBoxConfiguration. */
    @NotNull
    public String format(@NotNull List<CharSequence> lines) {
        return runFormattingTask(null, lines, null, lines);
    }

    /** Formats given string lines instance into a pretty box using the given configuration
     *  instance. Any settings not defined in given instance will fallback to the instance-level
     *  configuration instance. */
    @NotNull
    public String format(@NotNull List<CharSequence> lines,
                         @NotNull PrettyBoxConfiguration configuration) {
        return runFormattingTask(null, lines, configuration, lines);
    }

    /** Convenience method that adds a title String to header. Otherwise works like
     * {@link #format(PrettyBoxable)}. */
    @NotNull
    public String format(@NotNull String title, @NotNull PrettyBoxable prettyBoxable) {
        return runFormattingTask(title, prettyBoxable.toStringLines(), null, prettyBoxable);
    }

    /** Convenience method that adds a title String to header. Otherwise works like
     * {@link #format(PrettyBoxable, PrettyBoxConfiguration)}. */
    @NotNull
    public String format(@NotNull String title,
                         @NotNull PrettyBoxable prettyBoxable,
                         @NotNull PrettyBoxConfiguration configuration) {
        return runFormattingTask(title, prettyBoxable.toStringLines(), configuration, prettyBoxable);
    }

    /** Convenience method that adds a title String to header. Otherwise works like
     * {@link #format(List)}. */
    @NotNull
    public String format(@NotNull String title, @NotNull List<CharSequence> lines) {
        return runFormattingTask(title, lines, null, lines);
    }

    /** Convenience method that adds a title String to header. Otherwise works like
     *  {@link #format(List, PrettyBoxConfiguration)}. */
    @NotNull
    public String format(@NotNull String title,
                         @NotNull List<CharSequence> lines,
                         @NotNull PrettyBoxConfiguration configuration) {
        return runFormattingTask(title, lines, configuration, lines);
    }


    // ------------------------------------------------------------------------------ MAIN ALGORITHM

    private String runFormattingTask(@Nullable String title,
                                     @NotNull List<CharSequence> lines,
                                     @Nullable PrettyBoxConfiguration perCallConfiguration,
                                     @Nullable Object sourceObject) {
        // values to use (if no per-call) or as fallback (if per-call invalid)
        PrettyBoxConfiguration configurationToUse = this.configuration;
        int maxContentWidth = this.maxContentWidth;
        int maxLineWidth = this.maxLineWidth;
        boolean invalidPerCallConfiguration = false;

        if(perCallConfiguration != null) {
            PrettyBoxConfiguration mergedConfig =
                    PrettyBoxConfiguration.Builder.createFromInstance(DEFAULT_CONFIGURATION)
                            .applyFromInstance(perCallConfiguration)
                            .build();

            invalidPerCallConfiguration = !validateConfiguration(mergedConfig);

            if (!invalidPerCallConfiguration) {
                configurationToUse = mergedConfig;
                maxContentWidth = determineMaxContentWidth(configurationToUse);
                maxLineWidth = determineMaxLineWidth(configurationToUse);
            }
        }

        FormattingTaskData taskData = prepareFormattingTaskData(
                title, lines, configurationToUse, sourceObject, maxContentWidth, maxLineWidth);
        if(invalidConfiguration) taskData.markPrintInvalidInstanceLevelConfigMessage();
        if(invalidPerCallConfiguration) taskData.markPrintInvalidPerCallConfigMessage();

        return drawBox(taskData, configurationToUse);
    }

    @SuppressWarnings("ConstantConditions") // We make sure it's not null
    @NotNull
    private FormattingTaskData prepareFormattingTaskData(
            @Nullable String title,
            @NotNull List<CharSequence> lines,
            @NotNull PrettyBoxConfiguration configuration,
            @Nullable Object sourceObject,
            int maxContentWidth,
            int maxLineWidth) {

        // Add header/footer to content, if requested
        List<BoxMetaData> headerData = configuration.getHeaderMetadata();
        if(title != null || (headerData != null && headerData.size() > 0)) {
            lines.add(0, LineWithLevel.LEVEL_0);
            if(headerData != null && headerData.size() > 0)
                lines.addAll(0, generateMetadata(headerData, sourceObject));
            if(title != null) lines.add(0, title);
        }

        List<BoxMetaData> footerData = configuration.getFooterMetadata();
        if(footerData != null && footerData.size() > 0) {
            lines.add(LineWithLevel.LEVEL_0);
            lines.addAll(generateMetadata(footerData, sourceObject));
        }


        // Determine if there are content lines longer than max allowed width
        int maxSourceWidth = 0;
        for (CharSequence line : lines) maxSourceWidth = Math.max(maxSourceWidth, line.length());

        // If there are lines longer than charsPerLine, split them to fit
        if(maxSourceWidth > maxContentWidth) {
            maxSourceWidth = maxContentWidth;
            lines = splitLinesToFitBox(lines, maxContentWidth);
        }

        FormattingTaskData taskData = new FormattingTaskData();
        taskData.setContentLines(lines);

        // If wrap content is TRUE, make the box as wide as the longest line we have.
        if (configuration.getWrapContent()) {
            taskData.setContentWidth(maxSourceWidth);
            taskData.setLineWidth(taskData.getContentWidth()
                    + configuration.getPaddingLeft() + configuration.getPaddingRight());
        } else {
            taskData.setContentWidth(maxContentWidth);
            taskData.setLineWidth(maxLineWidth);
        }

        return taskData;
    }

    @NotNull
    private List<String> generateMetadata(
            @NotNull List<BoxMetaData> boxMetaDataList,
            @NotNull Object sourceObject) {

        List<String> metadata = new ArrayList<>();

        for(BoxMetaData boxMetaData : boxMetaDataList) {
            switch (boxMetaData) {
                case CURRENT_TIME:
                    TimeZone tz = TimeZone.getTimeZone("UTC");
                    DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'");
                    df.setTimeZone(tz);
                    metadata.add(df.format(new Date()));
                    break;
                case FULL_CLASS_NAME:
                    metadata.add(sourceObject.getClass().getCanonicalName());
                    break;
                case TIMESTAMP_SECONDS:
                    metadata.add(String.valueOf(System.currentTimeMillis()/1000));
                    break;
                case TIMESTAMP_MILLIS:
                    metadata.add(String.valueOf(System.currentTimeMillis()));
                    break;
                case SHORT_CLASS_NAME:
                    metadata.add(sourceObject.getClass().getSimpleName());
                    break;
                case IDENTITY_HASHCODE:
                    metadata.add(String.valueOf(System.identityHashCode(sourceObject)));
                    break;
            }
        }

        return metadata;
    }

    // stuff is nullable, but we make sure the default settings provide fallback non-null values
    @SuppressWarnings("ConstantConditions")
    @NotNull
    private String drawBox(@NotNull FormattingTaskData taskData,
                           @NotNull PrettyBoxConfiguration configuration) {
        ArrayList<CharSequence> lines = new ArrayList<>();

        // Optimization. Set length to 0 before every use.
        StringBuilder stringBuilder = new StringBuilder();

        // Terminology note: "draw" methods take StringBuilder and "draw" into it. "Generate"
        // methods generate and return a list of Strings (lines to be output). They also take
        // StringBuilder for optimization purposes, but its content after the call is irrelevant

        if(invalidConfiguration) lines.add(INVALID_PER_CALL_CONFIGURATION_MESSAGE);
        if(invalidConfiguration) lines.add(INVALID_INSTANCE_LEVEL_CONFIGURATION_MESSAGE);

        if(configuration.getPrefixEveryPrintWithNewline())
            lines.add(" "); // add one space because of logcat (won't print initial \n lines)

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

        List<CharSequence> contentLines = taskData.getContentLines();
        for (CharSequence contentLine : contentLines) {
            stringBuilder.setLength(0);
            if (contentLine instanceof LineWithLevel || contentLine instanceof LineWithType) {
                LineType lineType;
                if(contentLine instanceof LineWithType)
                    lineType = ((LineWithType) contentLine).getLineType();
                else if(contentLine instanceof LineWithLevel)
                    lineType = configuration.getLineTypeForLevel(
                            ((LineWithLevel) contentLine).getLineLevel());
                else
                    lineType = configuration.getInnerLineType();
                drawInnerLine(stringBuilder, taskData.getLineWidth(), lineType, configuration); // TODO maybe we should call this lineLength and not width?
            } else
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
    private List<CharSequence> splitLinesToFitBox(@NotNull List<CharSequence> lines, int contentWidth) {
        List<CharSequence> splitLines = new ArrayList<>();
        for(CharSequence line : lines) {
            if(line.length() <= contentWidth) splitLines.add(line);
            else splitLines.addAll(splitLineEveryNChars(line, contentWidth));
        }
        return splitLines;
    }

    @NotNull
    private List<CharSequence> generateVerticalSpaces(int verticalMargin) {
        List<CharSequence> lines = new ArrayList<>();
        for(int i = 0; i < verticalMargin; i++) lines.add(" "); // one space because of logcat
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
            stringBuilder.append(top?
                    configuration.getBorderLineType().getTopLeftCorner()
                    : configuration.getBorderLineType().getBottomLeftCorner());

        stringBuilder.append(getNCharacterString(
                configuration.getBorderLineType().getHorizontalLine(), lineWidth));

        if(configuration.getBorderRight())
            stringBuilder.append(top?
                    configuration.getBorderLineType().getTopRightCorner()
                    : configuration.getBorderLineType().getBottomRightCorner());

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
                stringBuilder.append(configuration.getBorderLineType().getVerticalLine());

            stringBuilder
                    .append(getHorizontalSpaces(lineWidth));

            if(configuration.getBorderRight())
                stringBuilder.append(configuration.getBorderLineType().getVerticalLine());

            stringBuilder
                    .append(getHorizontalSpaces(configuration.getMarginRight()));

            lines.add(stringBuilder.toString());
        }

        return lines;
    }

    @SuppressWarnings("ConstantConditions") // @see drawBox
    private void drawInnerLine(@NotNull StringBuilder stringBuilder,
                               int lineWidth,
                               @NotNull LineType lineType,
                               @NotNull PrettyBoxConfiguration configuration) {
        stringBuilder
                .append(getHorizontalSpaces(configuration.getMarginLeft()));

        if(configuration.getBorderLeft())
            stringBuilder.append(configuration.getBorderLineType().getRightTIntersection());

        stringBuilder.append(getNCharacterString(lineType.getHorizontalLine(), lineWidth));

        if(configuration.getBorderRight())
            stringBuilder.append(configuration.getBorderLineType().getLeftTIntersection());

        stringBuilder
                .append(getHorizontalSpaces(configuration.getMarginRight()));
    }

    @SuppressWarnings("ConstantConditions") // @see drawBox
    private void drawContentLine(@NotNull StringBuilder stringBuilder,
                                 @NotNull CharSequence line,
                                 int contentWidth,
                                 @NotNull PrettyBoxConfiguration configuration) {

        stringBuilder.append(getHorizontalSpaces(configuration.getMarginLeft()));

        if(configuration.getBorderLeft())
            stringBuilder.append(configuration.getBorderLineType().getVerticalLine());

        stringBuilder
                .append(getHorizontalSpaces(configuration.getPaddingLeft()))
                .append(line);

        int rightPadding = configuration.getPaddingRight() + (contentWidth - line.length());
        stringBuilder
                .append(getHorizontalSpaces(rightPadding));

        if(configuration.getBorderRight())
            stringBuilder.append(configuration.getBorderLineType().getVerticalLine());

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
    private List<CharSequence> splitLineEveryNChars(@NotNull CharSequence string, int partitionSize) {
        List<CharSequence> parts = new ArrayList<>();
        int len = string.length();
        if(len <= partitionSize) parts.add(string);
        else {
            for (int i = 0; i < len; i += partitionSize)
                parts.add(string.subSequence(i, Math.min(len, i + partitionSize)));
        }

        return parts;
    }

    @NotNull
    private String getHorizontalSpaces(int length) {
        return getNCharacterString(' ', length);
    }

    @NotNull
    private String getNCharacterString(char character, int length) {
        StringBuilder outputBuffer = new StringBuilder(length);
        for (int i = 0; i < length; i++) outputBuffer.append(character);
        return outputBuffer.toString();
    }

}
