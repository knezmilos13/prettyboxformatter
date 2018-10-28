package com.bgpixel.prettyboxformatter;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;

class JavaUtil {

    static String join(@NotNull CharSequence delimiter, @NotNull Object ... tokens) {
        StringBuilder sb = new StringBuilder();
        boolean firstTime = true;
        for (Object token: tokens) {
            if (firstTime) {
                firstTime = false;
            } else {
                sb.append(delimiter);
            }
            sb.append(token);
        }
        return sb.toString();
    }

    static String join(@NotNull CharSequence delimiter, @NotNull Iterable tokens) {
        StringBuilder sb = new StringBuilder();
        Iterator<?> it = tokens.iterator();
        if (it.hasNext()) {
            sb.append(it.next());
            while (it.hasNext()) {
                sb.append(delimiter);
                sb.append(it.next());
            }
        }
        return sb.toString();
    }

    static boolean isEmpty(@Nullable CharSequence str) {
        return str == null || str.length() == 0;
    }

}
