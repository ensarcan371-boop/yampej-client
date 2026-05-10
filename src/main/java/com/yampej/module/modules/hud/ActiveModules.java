package com.yampej.module.modules.hud;

import com.yampej.module.Category;
import com.yampej.module.Module;
import com.yampej.module.ModuleManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

import java.util.Comparator;
import java.util.List;

public class ActiveModules extends Module {
    public ActiveModules() {
        super("ActiveModules", "List of active modules", Category.HUD);
        setEnabled(true);
    }

    public void render(DrawContext ctx) {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (!isEnabled() || mc.textRenderer == null) return;

        List<Module> enabled = ModuleManager.getEnabled().stream()
            .filter(m -> m.getCategory() != Category.HUD)
            .sorted(Comparator.comparingInt(m -> -mc.textRenderer.getWidth(m.getName())))
            .toList();

        int y = 2;
        int screenW = ctx.getScaledWindowWidth();

        for (Module module : enabled) {
            String name = module.getName();
            int textW = mc.textRenderer.getWidth(name);
            int x = screenW - textW - 2;

            ctx.fill(x - 1, y - 1, screenW, y + 9, 0x55000000);
            ctx.fill(screenW - 1, y - 1, screenW, y + 9, 0xFF8844CC);
            ctx.drawTextWithShadow(mc.textRenderer, name, x, y, 0xFFFFFF);
            y += 11;
        }
    }
}
