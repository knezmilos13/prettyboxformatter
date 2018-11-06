package com.bgpixel.prettyboxformatter;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

class FormattingTaskData {

    @NotNull private List<CharSequence> contentLines = new ArrayList<>();
    private int contentWidth;
    private int lineWidth;

    private boolean printInvalidInstanceLevelConfigMessage = false;
    private boolean printInvalidPerCallConfigMessage = false;

    @NotNull List<CharSequence> getContentLines() { return contentLines; }
    void setContentLines(@NotNull List<CharSequence> contentLines) {
        this.contentLines = contentLines;
    }

    int getContentWidth() { return contentWidth; }
    void setContentWidth(int contentWidth) { this.contentWidth = contentWidth; }

    int getLineWidth() { return lineWidth; }
    void setLineWidth(int lineWidth) { this.lineWidth = lineWidth; }

    boolean isPrintInvalidInstanceLevelConfigMessage() { return printInvalidInstanceLevelConfigMessage; }
    void markPrintInvalidInstanceLevelConfigMessage() {
        this.printInvalidInstanceLevelConfigMessage = true;
    }

    boolean isPrintInvalidPerCallConfigMessage() { return printInvalidPerCallConfigMessage; }
    void markPrintInvalidPerCallConfigMessage() {
        this.printInvalidPerCallConfigMessage = true;
    }

}
