package com.bgpixel.prettyboxformatter;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface PrettyBoxable {
    @NotNull List<String> toStringLines();
}
