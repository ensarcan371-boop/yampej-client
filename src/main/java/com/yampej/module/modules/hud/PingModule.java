package com.yampej.module.modules.hud;

import com.yampej.module.Category;
import com.yampej.module.Module;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.network.PlayerListEntry;

public class PingModule extends Module {
    public PingModule() {
        super("Ping", "Network latency", Category.HUD);
    }

    public void render(DrawContext ctx) {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (!isEnabled() || mc.player == null || mc.textRenderer == null) return;

        PlayerListEntry entry = mc.player.networkHandler.getPlayerListEntry(mc.player.getUuid());
        if (entry == null) return;

        int ping = entry.getLatency();
        String color = ping < 80 ? "§a" : ping < 150 ? "§e" : "§c";
        int screenH = ctx.getScaledWindowHeight();
        ctx.drawTextWithShadow(mc.textRenderer, color + "Ping: §f" + ping + "ms", 2, screenH - 30, 0xFFFFFF);
    }
}
