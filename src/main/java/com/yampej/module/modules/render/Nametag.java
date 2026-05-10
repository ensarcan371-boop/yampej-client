package com.yampej.module.modules.render;

import com.yampej.module.Category;
import com.yampej.module.Module;
import com.yampej.module.setting.BooleanSetting;
import com.yampej.module.setting.SliderSetting;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import org.joml.Matrix4f;

public class Nametag extends Module {
    public final SliderSetting scale    = addSetting(new SliderSetting("Scale", 1.0, 0.5, 3.0, 0.1));
    public final BooleanSetting health  = addSetting(new BooleanSetting("Health", true));
    public final BooleanSetting ping    = addSetting(new BooleanSetting("Ping", true));
    public final BooleanSetting distanceSetting = addSetting(new BooleanSetting("Distance", false));

    public Nametag() {
        super("Nametag", "Shows custom nametags above players", Category.RENDER);
    }

    public void render(PlayerEntity entity, MatrixStack matrices, VertexConsumerProvider consumers, float tickDelta, int light) {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.player == null || mc.textRenderer == null) return;
        if (entity == mc.player) return;

        StringBuilder sb = new StringBuilder(entity.getName().getString());
        if (health.getValue()) sb.append(" §c").append((int) entity.getHealth()).append("§f HP");
        if (distanceSetting.getValue()) sb.append(" §7").append((int) mc.player.distanceTo(entity)).append("m");

        Text text = Text.literal(sb.toString());
        TextRenderer tr = mc.textRenderer;

        float s = (float) scale.getValue() * 0.025f;
        matrices.push();
        matrices.translate(0, entity.getHeight() + 0.5, 0);
        matrices.multiply(mc.getEntityRenderDispatcher().getRotation());
        matrices.scale(-s, -s, s);

        Matrix4f matrix = matrices.peek().getPositionMatrix();
        float x = -tr.getWidth(text) / 2f;

        VertexConsumer bg = consumers.getBuffer(RenderLayer.getGuiOverlay());
        tr.draw(text, x, 0, 0xFFFFFF, true, matrix, consumers, TextRenderer.TextLayerType.NORMAL, 0x55000000, light);
        matrices.pop();
    }
}
