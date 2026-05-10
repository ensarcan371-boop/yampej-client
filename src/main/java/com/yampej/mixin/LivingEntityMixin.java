package com.yampej.mixin;

import com.yampej.module.ModuleManager;
import com.yampej.module.modules.combat.AntiKnockback;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import net.minecraft.client.MinecraftClient;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {

    @ModifyArg(method = "takeKnockback", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;addVelocity(DDD)V"), index = 1)
    private double modifyKnockbackY(double y) {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.player == null) return y;
        LivingEntity self = (LivingEntity)(Object)this;
        if (self != mc.player) return y;

        AntiKnockback ak = ModuleManager.get(AntiKnockback.class);
        if (ak == null || !ak.isEnabled()) return y;
        return y * (1.0 - ak.vertical.getValue());
    }

    @ModifyArg(method = "takeKnockback", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;addVelocity(DDD)V"), index = 0)
    private double modifyKnockbackX(double x) {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.player == null) return x;
        LivingEntity self = (LivingEntity)(Object)this;
        if (self != mc.player) return x;

        AntiKnockback ak = ModuleManager.get(AntiKnockback.class);
        if (ak == null || !ak.isEnabled()) return x;
        return x * (1.0 - ak.horizontal.getValue());
    }
}
