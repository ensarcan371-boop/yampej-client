package com.yampej.module.modules.render;

import com.yampej.module.Category;
import com.yampej.module.Module;
import com.yampej.module.setting.BooleanSetting;
import com.yampej.module.setting.EnumSetting;
import com.yampej.module.setting.SliderSetting;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Box;
import org.joml.Matrix4f;

public class ESP extends Module {
    public final EnumSetting mode     = addSetting(new EnumSetting("Mode", new String[]{"Box", "Outline", "Corners"}));
    public final BooleanSetting players = addSetting(new BooleanSetting("Players", true));
    public final BooleanSetting mobs    = addSetting(new BooleanSetting("Mobs", false));
    public final SliderSetting distance = addSetting(new SliderSetting("Distance", 64, 10, 128, 1));

    public ESP() {
        super("ESP", "Draws boxes around entities", Category.RENDER);
        setEnabled(true);
    }

    public void renderBox(LivingEntity entity, MatrixStack matrices, VertexConsumerProvider consumers, float tickDelta) {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.player == null) return;
        if (mc.player.distanceTo(entity) > distance.getValue()) return;
        if (entity == mc.player) return;

        if (!players.getValue() && entity instanceof PlayerEntity) return;
        if (!mobs.getValue() && entity instanceof MobEntity) return;

        double x = entity.getX() - mc.getEntityRenderDispatcher().camera.getPos().x;
        double y = entity.getY() - mc.getEntityRenderDispatcher().camera.getPos().y;
        double z = entity.getZ() - mc.getEntityRenderDispatcher().camera.getPos().z;

        Box box = entity.getBoundingBox().offset(-entity.getX() + x, -entity.getY() + y, -entity.getZ() + z);

        matrices.push();
        VertexConsumer consumer = consumers.getBuffer(RenderLayer.getLines());
        Matrix4f matrix = matrices.peek().getPositionMatrix();

        float r = entity instanceof PlayerEntity ? 0.55f : 1f;
        float g = entity instanceof PlayerEntity ? 0.27f : 0.5f;
        float b = entity instanceof PlayerEntity ? 0.95f : 0.1f;

        drawBox(consumer, matrix, box, r, g, b, 1f);
        matrices.pop();
    }

    private void drawBox(VertexConsumer c, Matrix4f m, Box b, float r, float g, float bv, float a) {
        float x1 = (float) b.minX, y1 = (float) b.minY, z1 = (float) b.minZ;
        float x2 = (float) b.maxX, y2 = (float) b.maxY, z2 = (float) b.maxZ;

        line(c, m, x1, y1, z1, x2, y1, z1, r, g, bv, a);
        line(c, m, x2, y1, z1, x2, y1, z2, r, g, bv, a);
        line(c, m, x2, y1, z2, x1, y1, z2, r, g, bv, a);
        line(c, m, x1, y1, z2, x1, y1, z1, r, g, bv, a);
        line(c, m, x1, y2, z1, x2, y2, z1, r, g, bv, a);
        line(c, m, x2, y2, z1, x2, y2, z2, r, g, bv, a);
        line(c, m, x2, y2, z2, x1, y2, z2, r, g, bv, a);
        line(c, m, x1, y2, z2, x1, y2, z1, r, g, bv, a);
        line(c, m, x1, y1, z1, x1, y2, z1, r, g, bv, a);
        line(c, m, x2, y1, z1, x2, y2, z1, r, g, bv, a);
        line(c, m, x2, y1, z2, x2, y2, z2, r, g, bv, a);
        line(c, m, x1, y1, z2, x1, y2, z2, r, g, bv, a);
    }

    private void line(VertexConsumer c, Matrix4f m, float x1, float y1, float z1, float x2, float y2, float z2, float r, float g, float b, float a) {
        float nx = x2 - x1, ny = y2 - y1, nz = z2 - z1;
        float len = (float) Math.sqrt(nx * nx + ny * ny + nz * nz);
        if (len == 0) return;
        c.vertex(m, x1, y1, z1).color(r, g, b, a).normal(nx / len, ny / len, nz / len);
        c.vertex(m, x2, y2, z2).color(r, g, b, a).normal(nx / len, ny / len, nz / len);
    }
}
