package svenhjol.charmony.core.common.wood;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.block.state.properties.WoodType;
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

    default Map<CustomWoodType, Supplier<ItemLike>> creativeMenuPosition() {
        Map<CustomWoodType, Supplier<ItemLike>> map = new LinkedHashMap<>();
        map.put(CustomWoodType.BARREL, () -> Items.BARREL);
        map.put(CustomWoodType.CHEST_BOAT, () -> Items.ACACIA_CHEST_BOAT);
        map.put(CustomWoodType.BOAT, () -> Items.ACACIA_CHEST_BOAT);
        map.put(CustomWoodType.BOOKSHELF, () -> Items.BOOKSHELF);
        map.put(CustomWoodType.BUTTON, () -> Items.ACACIA_BUTTON);
        map.put(CustomWoodType.CHEST, () -> Items.CHEST);
        map.put(CustomWoodType.CHISELED_BOOKSHELF,   () -> Items.CHISELED_BOOKSHELF);
        map.put(CustomWoodType.DOOR, () -> Items.ACACIA_BUTTON);
        map.put(CustomWoodType.FENCE, () -> Items.ACACIA_BUTTON);
        map.put(CustomWoodType.GATE, () -> Items.ACACIA_BUTTON);
        map.put(CustomWoodType.HANGING_SIGN, () -> Items.ACACIA_HANGING_SIGN);
        map.put(CustomWoodType.LADDER, () -> Items.LADDER);
        map.put(CustomWoodType.LEAVES, () -> Items.ACACIA_LEAVES);
        map.put(CustomWoodType.LOG, () -> Items.ACACIA_BUTTON);
        map.put(CustomWoodType.PLANKS, () -> Items.ACACIA_BUTTON);
        map.put(CustomWoodType.PRESSURE_PLATE, () -> Items.ACACIA_BUTTON);
        map.put(CustomWoodType.SAPLING, () -> Items.ACACIA_SAPLING);
        map.put(CustomWoodType.SIGN, () -> Items.ACACIA_HANGING_SIGN);
        map.put(CustomWoodType.SLAB, () -> Items.ACACIA_BUTTON);
        map.put(CustomWoodType.STAIRS, () -> Items.ACACIA_BUTTON);
        map.put(CustomWoodType.STRIPPED_LOG, () -> Items.ACACIA_BUTTON);
        map.put(CustomWoodType.STRIPPED_WOOD, () -> Items.ACACIA_BUTTON);
        map.put(CustomWoodType.TRAPDOOR, () -> Items.ACACIA_BUTTON);
        map.put(CustomWoodType.TRAPPED_CHEST, () -> Items.TRAPPED_CHEST);
        map.put(CustomWoodType.WOOD, () -> Items.ACACIA_BUTTON);
        return map;
    }

    default Optional<ResourceKey<ConfiguredFeature<?, ?>>> tree() {
        return Optional.empty();
    }
}
