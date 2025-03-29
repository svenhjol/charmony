package svenhjol.charmony.core.common.wood;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import svenhjol.charmony.core.common.material.Material;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

public interface WoodMaterial extends Material {
    @Override
    default NoteBlockInstrument noteBlockInstrument() {
        return NoteBlockInstrument.BASS; // Vanilla default for wood
    }

    @Override
    default boolean isFlammable() {
        return true; // Vanilla default for wood
    }

    BlockSetType blockSetType();

    WoodType woodType();

    default Map<WoodType, Supplier<ItemLike>> creativeMenuPosition() {
        Map<WoodType, Supplier<ItemLike>> map = new LinkedHashMap<>();
        map.put(WoodType.BARREL, () -> Items.BARREL);
        map.put(WoodType.CHEST_BOAT, () -> Items.ACACIA_CHEST_BOAT);
        map.put(WoodType.BOAT, () -> Items.ACACIA_CHEST_BOAT);
        map.put(WoodType.BOOKSHELF, () -> Items.BOOKSHELF);
        map.put(WoodType.BUTTON, () -> Items.ACACIA_BUTTON);
        map.put(WoodType.CHEST, () -> Items.CHEST);
        map.put(WoodType.CHISELED_BOOKSHELF,   () -> Items.CHISELED_BOOKSHELF);
        map.put(WoodType.DOOR, () -> Items.ACACIA_BUTTON);
        map.put(WoodType.FENCE, () -> Items.ACACIA_BUTTON);
        map.put(WoodType.GATE, () -> Items.ACACIA_BUTTON);
        map.put(WoodType.HANGING_SIGN, () -> Items.ACACIA_HANGING_SIGN);
        map.put(WoodType.LADDER, () -> Items.LADDER);
        map.put(WoodType.LEAVES, () -> Items.ACACIA_LEAVES);
        map.put(WoodType.LOG, () -> Items.ACACIA_BUTTON);
        map.put(WoodType.PLANKS, () -> Items.ACACIA_BUTTON);
        map.put(WoodType.PRESSURE_PLATE, () -> Items.ACACIA_BUTTON);
        map.put(WoodType.SAPLING, () -> Items.ACACIA_SAPLING);
        map.put(WoodType.SIGN, () -> Items.ACACIA_HANGING_SIGN);
        map.put(WoodType.SLAB, () -> Items.ACACIA_BUTTON);
        map.put(WoodType.STAIRS, () -> Items.ACACIA_BUTTON);
        map.put(WoodType.STRIPPED_LOG, () -> Items.ACACIA_BUTTON);
        map.put(WoodType.STRIPPED_WOOD, () -> Items.ACACIA_BUTTON);
        map.put(WoodType.TRAPDOOR, () -> Items.ACACIA_BUTTON);
        map.put(WoodType.TRAPPED_CHEST, () -> Items.TRAPPED_CHEST);
        map.put(WoodType.WOOD, () -> Items.ACACIA_BUTTON);
        return map;
    }

    default Optional<ResourceKey<ConfiguredFeature<?, ?>>> tree() {
        return Optional.empty();
    }
}
