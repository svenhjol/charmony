package svenhjol.charmony.core.helper;

import svenhjol.charmony.core.Charmony;
import svenhjol.charmony.core.base.Log;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public final class ConfigHelper {
    public static final Log LOGGER = new Log(Charmony.ID, "ConfigHelper");

    public static boolean setField(Field field, Object val) {
        try {
            val = filterEmptyStrings(val);
            field.set(null, val);
            return true;
        } catch (Exception e) {
            LOGGER.error("Could not set field value for " + field + ": " + e.getMessage());
            return false;
        }
    }

    public static Object getField(Field field) {
        try {
            var val = field.get(null);
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
            for (var i = 0; i < list.size(); i++) {
                var v = list.get(i);
                if (v instanceof String str && !str.isEmpty()) {
                    newList.add(str);
                }
            }
            val = newList;
        }
        return val;
    }
}
