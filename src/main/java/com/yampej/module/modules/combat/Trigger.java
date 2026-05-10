package com.yampej.module.modules.combat;

import com.yampej.module.Category;
import com.yampej.module.Module;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.LivingEntity;

public class Trigger extends Module {
    public Trigger() {
        super("Trigger", "Attacks when entity is in crosshair", Category.COMBAT);
    }

    @Override
    public void onTick() {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.player == null || mc.interactionManager == null) return;
        if (mc.targetedEntity instanceof LivingEntity) {
            mc.interactionManager.attackEntity(mc.player, mc.targetedEntity);
            mc.player.swingHand(net.minecraft.util.Hand.MAIN_HAND);
        }
    }
}
