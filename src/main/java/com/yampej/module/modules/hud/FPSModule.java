package com.yampej.module.modules.hud;

import com.yampej.module.Category;
import com.yampej.module.Module;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

public class FPSModule extends Module {
    public FPSModule() {
        super("FPS", "FPS counter", Category.HUD);
        setEnabled(true);
    }

    public void render(DrawContext ctx) {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (!isEnabled() || mc.textRenderer == null) return;

        int fps = mc.getCurrentFps();
        String color = fps > 100 ? "§a" : fps > 50 ? "§e" : "§c";
        int screenH = ctx.getScaledWindowHeight();
        ctx.drawTextWithShadow(mc.textRenderer, color + "FPS: §f" + fps, 2, screenH - 10, 0xFFFFFF);
    }
}
