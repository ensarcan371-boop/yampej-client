package com.yampej.module.modules.hud;

import com.yampej.module.Category;
import com.yampej.module.Module;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

public class Coordinates extends Module {
    public Coordinates() {
        super("Coordinates", "Shows player coordinates", Category.HUD);
        setEnabled(true);
    }

    public void render(DrawContext ctx) {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (!isEnabled() || mc.player == null || mc.textRenderer == null) return;

        int x = (int) mc.player.getX();
        int y = (int) mc.player.getY();
        int z = (int) mc.player.getZ();

        int screenH = ctx.getScaledWindowHeight();
        ctx.drawTextWithShadow(mc.textRenderer, "§7X: §f" + x + " §7Y: §f" + y + " §7Z: §f" + z, 2, screenH - 20, 0xFFFFFF);
    }
}
