package com.yampej.module.modules.render;

import com.yampej.module.Category;
import com.yampej.module.Module;
import net.minecraft.client.MinecraftClient;

public class FullBright extends Module {
    private float prevGamma;

    public FullBright() {
        super("FullBright", "Removes darkness", Category.RENDER);
        setEnabled(true);
    }

    @Override
    public void onEnable() {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.options != null) {
            prevGamma = (float)(double) mc.options.getGamma().getValue();
            mc.options.getGamma().setValue(16.0);
        }
    }

    @Override
    public void onDisable() {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.options != null) {
            mc.options.getGamma().setValue((double) prevGamma);
        }
    }
}
