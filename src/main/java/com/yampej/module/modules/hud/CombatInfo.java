package com.yampej.module.modules.hud;

import com.yampej.module.Category;
import com.yampej.module.Module;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.entity.LivingEntity;

public class CombatInfo extends Module {
    public CombatInfo() {
        super("CombatInfo", "Enemy health and distance", Category.HUD);
    }

    public void render(DrawContext ctx) {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (!isEnabled() || mc.player == null || mc.textRenderer == null) return;
        if (!(mc.targetedEntity instanceof LivingEntity target)) return;

        String name = target.getName().getString();
        int hp = (int) target.getHealth();
        int dist = (int) mc.player.distanceTo(target);

        int y = 30;
        ctx.drawTextWithShadow(mc.textRenderer, "§cTarget: §f" + name, 2, y, 0xFFFFFF);
        ctx.drawTextWithShadow(mc.textRenderer, "§cHP: §f" + hp, 2, y + 10, 0xFFFFFF);
        ctx.drawTextWithShadow(mc.textRenderer, "§cDist: §f" + dist + "m", 2, y + 20, 0xFFFFFF);
    }
}
