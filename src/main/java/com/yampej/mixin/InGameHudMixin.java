package com.yampej.mixin;

import com.yampej.module.ModuleManager;
import com.yampej.module.modules.hud.*;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.RenderTickCounter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public class InGameHudMixin {

    @Inject(method = "render", at = @At("TAIL"))
    private void onRenderHud(DrawContext ctx, RenderTickCounter tickCounter, CallbackInfo ci) {
        Watermark wm = ModuleManager.get(Watermark.class);
        if (wm != null) wm.render(ctx);

        ActiveModules am = ModuleManager.get(ActiveModules.class);
        if (am != null) am.render(ctx);

        Coordinates coord = ModuleManager.get(Coordinates.class);
        if (coord != null) coord.render(ctx);

        FPSModule fps = ModuleManager.get(FPSModule.class);
        if (fps != null) fps.render(ctx);

        PingModule ping = ModuleManager.get(PingModule.class);
        if (ping != null) ping.render(ctx);

        CombatInfo ci2 = ModuleManager.get(CombatInfo.class);
        if (ci2 != null) ci2.render(ctx);
    }
}
