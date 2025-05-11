package svenhjol.charmony.core.helpers;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.Registry;
import net.minecraft.tags.TagKey;

import java.util.LinkedList;
import java.util.List;

@SuppressWarnings("unused")
public final class TagHelper {
    /**
     * Get all individual values of a given tag using a registry reference.
     * Must be run after world initialisation and data packs have been loaded!
     */
    public static <T> List<T> getValues(Registry<T> registry, TagKey<T> tags) {
        List<T> items = new LinkedList<>();

        var iter = registry.getTagOrEmpty(tags);
        for (Holder<T> holder : iter) {
            items.add(holder.value());
        }

        return items;
    }

    /**
     * Get all individual values of a given tag using a HolderGetter.
     * Must be run after world initialisation and data packs have been loaded!
     */
    public static <T> List<T> getValues(HolderGetter<T> holderGetter, TagKey<T> tags) {
        List<T> items = new LinkedList<>();

        holderGetter.get(tags).ifPresent(holders -> {
            for (var holder : holders) {
                items.add(holder.value());
            }
        });

        return items;
    }
}
