package com.bgpixel.prettyboxformatter.data;

import com.bgpixel.prettyboxformatter.PrettyBoxable;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class SimpleBoxableObject implements PrettyBoxable {

    private final int numberOfApples;
    @NotNull private final String appleSellerName;
    private final boolean hasGreenApples;

    public SimpleBoxableObject(int numberOfApples,
                               @NotNull String appleSellerName,
                               boolean hasGreenApples) {
        this.numberOfApples = numberOfApples;
        this.appleSellerName = appleSellerName;
        this.hasGreenApples = hasGreenApples;
    }

    public int getNumberOfApples() { return numberOfApples; }
    @NotNull public String getAppleSellerName() { return appleSellerName; }
    public boolean isHasGreenApples() { return hasGreenApples; }

    @NotNull @Override
    public List<CharSequence> toStringLines() {
        List<CharSequence> lines = new ArrayList<>();
        lines.add("Number of apples: " + numberOfApples);
        lines.add("Apple seller name: " + appleSellerName);
        lines.add("Has green apples: " + hasGreenApples);
        return lines;
    }

}
