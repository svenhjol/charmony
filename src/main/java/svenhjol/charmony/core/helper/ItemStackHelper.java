package svenhjol.charmony.core.helper;

import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public final class ItemStackHelper {
    public static void mergeStacks(Container container) {
        List<ItemStack> stacks = new ArrayList<>();
        var size = container.getContainerSize();
        for (var i = 0; i < size; i++) {
            stacks.add(container.getItem(i));
        }

        mergeStacks(stacks);
        container.clearContent();
        for (var i = 0; i < stacks.size(); i++) {
            container.setItem(i, stacks.get(i));
        }
    }

    /**
     * Core merging code adapted from Quark's SortingHandler.
     * @param stacks Inventory stack to merge within
     */
    public static void mergeStacks(List<ItemStack> stacks) {
        for (int i = 0; i < stacks.size(); i++) {
            var stack = stacks.get(i);
            if (stack.isEmpty()) continue;

            for (var j = 0; j < stacks.size(); j++) {
                if (i == j) continue;

                var stack1 = stacks.get(j);
                if (stack1.isEmpty()) continue;

                if (stack1.getCount() < stack1.getMaxStackSize()
                    && ItemStack.isSameItemSameComponents(stack, stack1)
                ) {
                    var setSize = stack1.getCount() + stack.getCount();
                    var carryover = Math.max(0, setSize - stack1.getMaxStackSize());
                    stack1.setCount(carryover);
                    stack.setCount(setSize - carryover);

                    if (stack.getCount() == stack.getMaxStackSize()) break;
                }
            }

            stacks.set(i, stack);
        }

        stacks.removeIf((ItemStack stack) -> stack.isEmpty() || stack.getCount() == 0);
    }
}
