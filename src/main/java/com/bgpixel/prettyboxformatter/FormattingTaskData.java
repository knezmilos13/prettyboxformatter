package com.bgpixel.prettyboxformatter;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class FormattingTaskData {

    @NotNull private List<String> lines = new ArrayList<>();
    private int contentWidth;
    private int lineWidth;

    private boolean printInvalidInstanceLevelConfigMessage = false;
    private boolean printInvalidPerCallConfigMessage = false;

    @NotNull public List<String> getLines() { return lines; }
    public void setLines(@NotNull List<String> lines) { this.lines = lines; }

    public int getContentWidth() { return contentWidth; }
    public void setContentWidth(int contentWidth) { this.contentWidth = contentWidth; }

    public int getLineWidth() { return lineWidth; }
    public void setLineWidth(int lineWidth) { this.lineWidth = lineWidth; }

    public boolean isPrintInvalidInstanceLevelConfigMessage() { return printInvalidInstanceLevelConfigMessage; }
    public void setPrintInvalidInstanceLevelConfigMessage(boolean printInvalidGlobalConfigMessage) {
        this.printInvalidInstanceLevelConfigMessage = printInvalidGlobalConfigMessage;
    }

    public boolean isPrintInvalidPerCallConfigMessage() { return printInvalidPerCallConfigMessage; }
    public void setPrintInvalidPerCallConfigMessage(boolean printInvalidPerCallConfigMessage) {
        this.printInvalidPerCallConfigMessage = printInvalidPerCallConfigMessage;
    }

}