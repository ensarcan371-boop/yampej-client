package com.yampej.mixin;

import com.yampej.module.ModuleManager;
import com.yampej.module.modules.combat.Criticals;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin {

    @Inject(method = "attack", at = @At("HEAD"))
    private void onAttack(Entity target, CallbackInfo ci) {
        Criticals crit = ModuleManager.get(Criticals.class);
        if (crit != null && crit.isEnabled()) {
            crit.onAttack();
        }
    }
}
