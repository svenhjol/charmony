package svenhjol.charmony.core.events;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.Optional;

/**
 * Return an optional AnvilRecipe to set the anvil state.
 * If nothing returned then vanilla behavior will be used.
 */
@SuppressWarnings("unused")
public class AnvilUpdateEvent extends CharmonyEvent<AnvilUpdateEvent.Handler> {
    public static final AnvilUpdateEvent INSTANCE = new AnvilUpdateEvent();

    private AnvilUpdateEvent() {}

    public Optional<AnvilRecipe> invoke(Player player, ItemStack input, ItemStack material, long cost) {
        for (var handler : getHandlers()) {
            var result = handler.run(player, input, material, cost);

            // If an AnvilRecipe has been provided then stop processing.
            if (result.isPresent()) {
                return result;
            }
        }

        return Optional.empty();
    }

    @FunctionalInterface
    public interface Handler {
        Optional<AnvilRecipe> run(Player player, ItemStack input, ItemStack material, long cost);
    }

    public static class AnvilRecipe {
        public ItemStack output;
        public int experienceCost;
        public int materialCost;
    }
}
