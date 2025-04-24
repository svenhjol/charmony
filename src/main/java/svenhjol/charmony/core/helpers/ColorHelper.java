package svenhjol.charmony.core.helpers;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ARGB;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

@SuppressWarnings("unused")
public final class ColorHelper {
    public static final List<DyeColor> DARK_MODE_COLORS = List.of(
        DyeColor.BLACK,
        DyeColor.GRAY,
        DyeColor.PURPLE,
        DyeColor.BROWN,
        DyeColor.BLUE
    );

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

        public int getColor() {
            return color;
        }
    }

    public static int leavesColorFromBiome(BlockState state, BlockAndTintGetter level, BlockPos pos, int tintIndex) {
        var defaultColor = -12012264; // See BlockColors.java
        return level != null && pos != null ? BiomeColors.getAverageFoliageColor(level, pos) : defaultColor;
    }

    public static void tintTexture(GuiGraphics guiGraphics, ResourceLocation texture, ColorHelper.Color color, int x, int y, float u, float v, int width, int height) {
        tintTexture(guiGraphics, texture, color, x, y, u, v, width, height, 1.0f);
    }

    public static void tintTexture(GuiGraphics guiGraphics, ResourceLocation texture, ColorHelper.Color color, int x, int y, float u, float v, int width, int height, float alpha) {
        RenderType renderType = RenderType.guiTextured(texture);

        var xw = x + width;
        var yh = y + height;

        var uu = (u + 0.0f) / 256;
        var uw = (u + width) / 256;
        var vv = (v + 0.0f) / 256;
        var vh = (v + height) / 256;

        var pose = guiGraphics.pose();
        var bufferSource = guiGraphics.bufferSource;
        var matrix4f = pose.last().pose();

        VertexConsumer vertexConsumer = bufferSource.getBuffer(renderType);
        vertexConsumer.addVertex(matrix4f, (float)x, (float)y, 0.0F).setUv(u, v).setColor(color.getRed(), color.getGreen(), color.getBlue(), 1.0f);
        vertexConsumer.addVertex(matrix4f, (float)x, (float)yh, 0.0F).setUv(u, vh).setColor(color.getRed(), color.getGreen(), color.getBlue(), 1.0f);
        vertexConsumer.addVertex(matrix4f, (float)xw, (float)yh, 0.0F).setUv(uw, vh).setColor(color.getRed(), color.getGreen(), color.getBlue(), 1.0f);
        vertexConsumer.addVertex(matrix4f, (float)xw, (float)y, 0.0F).setUv(uw, v).setColor(color.getRed(), color.getGreen(), color.getBlue(), 1.0f);
    }

    public static ColorHelper.Color tintBackgroundColor(DyeColor color) {
        var col = switch (color) {
            case BLACK -> 0x505050;
            case GRAY -> 0x7a7a7a;
            case LIGHT_GRAY -> 0xa0a0a0;
            case WHITE -> 0xffffff;
            case BROWN -> 0x9a5830;
            case RED -> 0xf06a6a;
            case ORANGE -> 0xffb060;
            case YELLOW -> 0xffd060;
            case LIME -> 0xbfff70;
            case GREEN -> 0x70c060;
            case CYAN -> 0x60c0c0;
            case LIGHT_BLUE -> 0x80a5e0;
            case BLUE -> 0x405090;
            case PURPLE -> 0x704090;
            case MAGENTA -> 0xcf66c0;
            case PINK -> 0xffa2d5;
        };
        return new ColorHelper.Color(col);
    }

    public static int tintForegroundColor(DyeColor color) {
        return switch(color) {
            case BLACK -> 0xd0d0d0;
            case GRAY -> 0xd4d4d4;
            case LIGHT_GRAY -> 0x333333;
            case WHITE -> 0x222222;
            case BROWN -> 0xf0e0b0;
            case RED -> 0x550000;
            case ORANGE -> 0x552200;
            case YELLOW -> 0x333311;
            case LIME -> 0x205010;
            case GREEN -> 0x004000;
            case CYAN -> 0x002540;
            case LIGHT_BLUE -> 0x002040;
            case BLUE -> 0xa0afff;
            case PURPLE -> 0xcf99ff;
            case MAGENTA -> 0x300030;
            case PINK -> 0x300020;
        };
    }
}
