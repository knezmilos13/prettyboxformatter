package com.bgpixel.prettyboxformatter.data;

import org.jetbrains.annotations.NotNull;

public class BaseTestObject {

    private final int numberOfApples;
    @NotNull private final String appleSellerName;
    private final boolean hasGreenApples;

    public BaseTestObject(int numberOfApples,
                          @NotNull String appleSellerName,
                          boolean hasGreenApples) {
        this.numberOfApples = numberOfApples;
        this.appleSellerName = appleSellerName;
        this.hasGreenApples = hasGreenApples;
    }

    public int getNumberOfApples() { return numberOfApples; }
    @NotNull public String getAppleSellerName() { return appleSellerName; }
    public boolean isHasGreenApples() { return hasGreenApples; }

}
