package svenhjol.charmony.core.common;

import net.minecraft.core.Holder;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.alchemy.Potion;

import java.util.function.Supplier;

public record PotionRecipe(Holder<Potion> input, Supplier<Item> reagent, Holder<Potion> output) { }
