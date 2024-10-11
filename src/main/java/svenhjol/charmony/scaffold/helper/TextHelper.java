package svenhjol.charmony.scaffold.helper;

import com.google.common.base.Function;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.List;

public final class TextHelper {
    public static String capitalize(String string) {
        if (string == null || string.isEmpty()) {
            return string;
        }

        return string.substring(0, 1).toUpperCase() + string.substring(1);
    }

    public static List<Component> toComponents(String string, int lineLength) {
        List<Component> out = new ArrayList<>();
        int lineSize = 0;
        StringBuilder buffer = new StringBuilder();
        Function<String, Component> convertText =
            s -> Component.literal(s.trim().replace("\n", " "));

        for (int i = 0; i < string.length(); i++) {
            var currentChar = string.charAt(i);
            buffer.append(currentChar);
            if (lineSize++ >= lineLength) {
                if (currentChar == ' ' && string.length() - i > 4) {
                    out.add(convertText.apply(buffer.toString()));
                    buffer = new StringBuilder();
                    lineSize = 0;
                }
            }
        }

        out.add(convertText.apply(buffer.toString()));
        return out;
    }

    public static String truncateWithEllipsis(String string, int maxLength) {
        var copy = String.valueOf(string);
        if (copy.length() > maxLength) {
            copy = copy.substring(0, maxLength) + "...";
        }
        return copy;
    }
}
