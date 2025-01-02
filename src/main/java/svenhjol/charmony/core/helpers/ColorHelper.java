package svenhjol.charmony.core.helpers;

import net.minecraft.util.ARGB;
import net.minecraft.world.item.DyeColor;

@SuppressWarnings("unused")
public final class ColorHelper {
    public static class Color {
        private final int color;

        public Color(int color) {
            this.color = color;
        }

        public Color(DyeColor color) {
            this.color = color.getTextureDiffuseColor();
        }

        public float getRed() {
            return (float) ARGB.red(this.color) / 255.0f;
        }

        public float getGreen() {
            return (float) ARGB.green(this.color) / 255.0f;
        }

        public float getBlue() {
            return (float) ARGB.blue(this.color) / 255.0f;
        }
    }
}
