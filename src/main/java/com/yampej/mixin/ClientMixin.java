package com.yampej.mixin;

import com.yampej.YampejClient;
import com.yampej.gui.ClickGui;
import com.yampej.module.ModuleManager;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public class ClientMixin {

    @Inject(method = "tick", at = @At("HEAD"))
    private void onTick(CallbackInfo ci) {
        MinecraftClient mc = MinecraftClient.getInstance();

        // Handle GUI key (Right Shift)
        while (YampejClient.guiKey.wasPressed()) {
            if (mc.currentScreen == null) {
                mc.setScreen(new ClickGui());
            } else if (mc.currentScreen instanceof ClickGui) {
                mc.setScreen(null);
            }
        }

        // Tick modules if in-game
        if (mc.player != null && mc.world != null && mc.currentScreen == null) {
            ModuleManager.onTick();
        }
    }
}
