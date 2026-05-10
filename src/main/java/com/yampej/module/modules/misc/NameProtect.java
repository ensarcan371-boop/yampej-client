package com.yampej.module.modules.misc;

import com.yampej.module.Category;
import com.yampej.module.Module;
import net.minecraft.client.MinecraftClient;

public class NameProtect extends Module {
    public NameProtect() {
        super("NameProtect", "Hides your name", Category.MISC);
    }

    public String protect(String text) {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (!isEnabled() || mc.player == null) return text;
        return text.replace(mc.player.getName().getString(), "Player");
    }
}
