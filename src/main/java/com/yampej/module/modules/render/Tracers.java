package com.yampej.module.modules.render;

import com.yampej.module.Category;
import com.yampej.module.Module;
import com.yampej.module.setting.EnumSetting;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.joml.Matrix4f;

public class Tracers extends Module {
    public final EnumSetting target = addSetting(new EnumSetting("Target", new String[]{"Players", "Mobs", "Both"}));

    public Tracers() {
        super("Tracers", "Draws lines to entities", Category.RENDER);
    }

    public void render(LivingEntity entity, MatrixStack matrices, VertexConsumerProvider consumers, float tickDelta) {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.player == null || mc.gameRenderer == null) return;
        if (entity == mc.player) return;

        String t = target.getValue();
        if ("Players".equals(t) && !(entity instanceof PlayerEntity)) return;
        if ("Mobs".equals(t) && !(entity instanceof MobEntity)) return;

        double cx = mc.getEntityRenderDispatcher().camera.getPos().x;
        double cy = mc.getEntityRenderDispatcher().camera.getPos().y;
        double cz = mc.getEntityRenderDispatcher().camera.getPos().z;

        float ex = (float)(entity.getLerpedX(tickDelta) - cx);
        float ey = (float)(entity.getLerpedY(tickDelta) + entity.getHeight() / 2.0 - cy);
        float ez = (float)(entity.getLerpedZ(tickDelta) - cz);

        matrices.push();
        VertexConsumer consumer = consumers.getBuffer(RenderLayer.getLines());
        Matrix4f matrix = matrices.peek().getPositionMatrix();
        consumer.vertex(matrix, 0, 0, 0).color(0.55f, 0.27f, 0.95f, 0.8f).normal(0, 1, 0);
        consumer.vertex(matrix, ex, ey, ez).color(0.55f, 0.27f, 0.95f, 0.8f).normal(0, 1, 0);
        matrices.pop();
    }
}
