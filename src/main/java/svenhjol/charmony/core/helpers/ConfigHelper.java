package svenhjol.charmony.core.helpers;

import svenhjol.charmony.core.Charmony;
import svenhjol.charmony.core.base.Log;

import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public final class ConfigHelper {
    public static final Log LOGGER = new Log(Charmony.ID, "ConfigHelper");

    @SuppressWarnings("UnusedReturnValue")
    public static boolean setField(Field field, Object val) {
        return setField(field, val, null);
    }

    public static boolean setField(Field field, Object val, @Nullable Object obj) {
        try {
            val = filterEmptyStrings(val);
            field.set(obj, val);
            return true;
        } catch (Exception e) {
            LOGGER.error("Could not set field value for " + field + ": " + e.getMessage());
            return false;
        }
    }

    public static Object getField(Field field) {
        return getField(field, null);
    }

    public static Object getField(Field field, @Nullable Object obj) {
        try {
            var val = field.get(obj);
            val = filterEmptyStrings(val);
            return val;
        } catch (Exception e) {
            LOGGER.error("Could not get field value for " + field + ": " + e.getMessage());
            return null;
        }
    }

    private static Object filterEmptyStrings(Object val) {
        if (val instanceof List<?> list) {
            // Handle empty strings in list.
            List<String> newList = new ArrayList<>();
            for (var v : list) {
                if (v instanceof String str && !str.isEmpty()) {
                    newList.add(str);
                }
            }
            val = newList;
        }
        return val;
    }
}
