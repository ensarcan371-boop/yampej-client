package com.yampej.module.modules.combat;

import com.yampej.module.Category;
import com.yampej.module.Module;
import com.yampej.module.setting.EnumSetting;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;

public class Criticals extends Module {
    public final EnumSetting mode = addSetting(new EnumSetting("Mode", new String[]{"Packet", "Jump", "NBT"}));

    public Criticals() {
        super("Criticals", "Makes your hits critical", Category.COMBAT);
        setEnabled(true);
    }

    public void onAttack() {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.player == null) return;
        if ("Packet".equals(mode.getValue())) {
            mc.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(
                mc.player.getX(), mc.player.getY() + 0.0625, mc.player.getZ(), false, mc.player.horizontalCollision));
            mc.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(
                mc.player.getX(), mc.player.getY(), mc.player.getZ(), false, mc.player.horizontalCollision));
        } else if ("Jump".equals(mode.getValue())) {
            if (mc.player.isOnGround()) mc.player.jump();
        }
    }
}
