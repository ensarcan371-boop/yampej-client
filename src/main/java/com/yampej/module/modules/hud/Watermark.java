package com.yampej.module.modules.hud;

import com.yampej.module.Category;
import com.yampej.module.Module;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

public class Watermark extends Module {
    public Watermark() {
        super("Watermark", "Shows Yampej watermark", Category.HUD);
        setEnabled(true);
    }

    public void render(DrawContext ctx) {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (!isEnabled() || mc.textRenderer == null) return;
        ctx.drawTextWithShadow(mc.textRenderer, "§5§lYampej §7v1.0.0", 2, 2, 0xFFFFFF);
    }
}
