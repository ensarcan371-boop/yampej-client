package com.yampej.mixin;

import com.yampej.module.ModuleManager;
import com.yampej.module.modules.render.ESP;
import com.yampej.module.modules.render.Tracers;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WorldRenderer.class)
public class GameRendererMixin {

    @Inject(method = "render", at = @At("TAIL"))
    private void onRender(RenderTickCounter tickCounter, boolean renderBlockOutline, Camera camera,
                          GameRenderer gameRenderer, LightmapTextureManager lightmapTextureManager,
                          org.joml.Matrix4f matrix4f, org.joml.Matrix4f matrix4f2, CallbackInfo ci) {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.world == null || mc.player == null) return;

        ESP esp = ModuleManager.get(ESP.class);
        Tracers tracers = ModuleManager.get(Tracers.class);

        if ((esp != null && esp.isEnabled()) || (tracers != null && tracers.isEnabled())) {
            MatrixStack matrices = new MatrixStack();
            matrices.multiplyPositionMatrix(matrix4f);

            VertexConsumerProvider.Immediate consumers = mc.getBufferBuilders().getEntityVertexConsumers();

            for (var entity : mc.world.getEntities()) {
                if (!(entity instanceof LivingEntity living)) continue;
                float td = tickCounter.getTickDelta(false);
                if (esp != null && esp.isEnabled()) esp.renderBox(living, matrices, consumers, td);
                if (tracers != null && tracers.isEnabled()) tracers.render(living, matrices, consumers, td);
            }
            consumers.draw();
        }
    }
}
