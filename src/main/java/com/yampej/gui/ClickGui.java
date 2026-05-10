package com.yampej.gui;

import com.yampej.module.Category;
import com.yampej.module.Module;
import com.yampej.module.ModuleManager;
import com.yampej.module.setting.BooleanSetting;
import com.yampej.module.setting.EnumSetting;
import com.yampej.module.setting.SliderSetting;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

import java.util.*;

public class ClickGui extends Screen {
    private static final int PANEL_W = 160;
    private static final int PANEL_HEADER_H = 16;
    private static final int MODULE_H = 22;
    private static final int SETTINGS_W = 150;

    // Panel positions
    private final Map<Category, int[]> panelPos = new LinkedHashMap<>();
    private final Map<Category, Boolean> panelMinimized = new HashMap<>();

    // Dragging state
    private Category dragging = null;
    private int dragOffX, dragOffY;

    // Right-click settings popup
    private Module settingsModule = null;
    private int settingsX, settingsY;
    private boolean draggingSlider = false;
    private int draggingSliderIdx = -1;

    public ClickGui() {
        super(Text.literal("Yampej GUI"));

        int startX = 10;
        for (Category cat : Category.values()) {
            panelPos.put(cat, new int[]{startX, 10});
            panelMinimized.put(cat, false);
            startX += PANEL_W + 10;
        }
    }

    @Override
    public boolean shouldPause() { return false; }

    @Override
    public void render(DrawContext ctx, int mouseX, int mouseY, float delta) {
        // Semi-transparent background
        ctx.fill(0, 0, width, height, 0x88000000);

        // Title bar
        ctx.fill(0, 0, width, 14, 0xBB0A0A14);
        ctx.drawCenteredTextWithShadow(textRenderer, "§5§lYAMPEJ §7v1.0.0", width / 2, 3, 0xFFFFFF);

        // Panels
        for (Category cat : Category.values()) {
            renderPanel(ctx, cat, mouseX, mouseY);
        }

        // Settings popup
        if (settingsModule != null) {
            renderSettings(ctx, mouseX, mouseY);
        }
    }

    private void renderPanel(DrawContext ctx, Category cat, int mouseX, int mouseY) {
        int[] pos = panelPos.get(cat);
        int px = pos[0], py = pos[1];
        boolean minimized = panelMinimized.get(cat);
        List<Module> modules = ModuleManager.getByCategory(cat);

        // Header
        ctx.fill(px, py, px + PANEL_W, py + PANEL_HEADER_H, 0xFF1A0A2A);
        ctx.fill(px, py, px + PANEL_W, py + 1, 0xFF8844CC);
        ctx.drawTextWithShadow(textRenderer, "§5§l" + cat.displayName, px + 4, py + 4, 0xFFFFFF);

        long enabledCount = modules.stream().filter(Module::isEnabled).count();
        if (enabledCount > 0) {
            String badge = String.valueOf(enabledCount);
            int bw = textRenderer.getWidth(badge) + 4;
            ctx.fill(px + PANEL_W - bw - 2, py + 3, px + PANEL_W - 2, py + PANEL_HEADER_H - 3, 0xFF8844CC);
            ctx.drawTextWithShadow(textRenderer, badge, px + PANEL_W - bw, py + 4, 0xFFFFFF);
        }

        // Minimize arrow
        String arrow = minimized ? "▲" : "▼";
        ctx.drawTextWithShadow(textRenderer, "§7" + arrow, px + PANEL_W - 12, py + 4, 0xAAAAAA);

        if (!minimized) {
            ctx.fill(px, py + PANEL_HEADER_H, px + PANEL_W, py + PANEL_HEADER_H + modules.size() * MODULE_H, 0xCC0D0D1A);
            ctx.fill(px, py + PANEL_HEADER_H, px + 1, py + PANEL_HEADER_H + modules.size() * MODULE_H, 0x888844CC);
            ctx.fill(px + PANEL_W - 1, py + PANEL_HEADER_H, px + PANEL_W, py + PANEL_HEADER_H + modules.size() * MODULE_H, 0x888844CC);
            ctx.fill(px, py + PANEL_HEADER_H + modules.size() * MODULE_H, px + PANEL_W, py + PANEL_HEADER_H + modules.size() * MODULE_H + 1, 0x888844CC);

            for (int i = 0; i < modules.size(); i++) {
                Module mod = modules.get(i);
                int my = py + PANEL_HEADER_H + i * MODULE_H;
                boolean hover = mouseX >= px && mouseX < px + PANEL_W && mouseY >= my && mouseY < my + MODULE_H;

                if (hover) ctx.fill(px + 1, my, px + PANEL_W - 1, my + MODULE_H, 0x22FFFFFF);
                if (mod.isEnabled()) ctx.fill(px + 1, my, px + 3, my + MODULE_H, 0xFF8844CC);

                ctx.drawTextWithShadow(textRenderer,
                    mod.isEnabled() ? "§f" + mod.getName() : "§7" + mod.getName(),
                    px + 6, my + 4, 0xFFFFFF);
                ctx.drawTextWithShadow(textRenderer, "§8" + mod.getDescription(),
                    px + 6, my + 13, 0x888888);

                // Toggle indicator
                int tx = px + PANEL_W - 14;
                int ty = my + 7;
                ctx.fill(tx, ty, tx + 10, ty + 6, mod.isEnabled() ? 0xFF8844CC : 0xFF444444);
                int knobX = mod.isEnabled() ? tx + 5 : tx + 1;
                ctx.fill(knobX, ty + 1, knobX + 4, ty + 5, 0xFFFFFFFF);
            }
        }
    }

    private void renderSettings(DrawContext ctx, int mouseX, int mouseY) {
        List<?> settings = settingsModule.getSettings();
        if (settings.isEmpty()) return;

        int sh = 14 + settings.size() * 22 + 4;
        int sx = Math.min(settingsX, width - SETTINGS_W - 4);
        int sy = Math.min(settingsY, height - sh - 4);

        // Background
        ctx.fill(sx, sy, sx + SETTINGS_W, sy + sh, 0xEE0A0A14);
        ctx.fill(sx, sy, sx + SETTINGS_W, sy + 1, 0xFF8844CC);
        ctx.fill(sx, sy, sx + 1, sy + sh, 0xFF8844CC);
        ctx.fill(sx + SETTINGS_W - 1, sy, sx + SETTINGS_W, sy + sh, 0xFF8844CC);
        ctx.fill(sx, sy + sh - 1, sx + SETTINGS_W, sy + sh, 0xFF8844CC);

        ctx.drawTextWithShadow(textRenderer, "§5" + settingsModule.getName(), sx + 4, sy + 3, 0xFFFFFF);

        for (int i = 0; i < settings.size(); i++) {
            Object setting = settings.get(i);
            int ry = sy + 14 + i * 22;

            if (setting instanceof BooleanSetting bs) {
                ctx.drawTextWithShadow(textRenderer, "§7" + bs.getName(), sx + 4, ry + 4, 0xCCCCCC);
                int tx = sx + SETTINGS_W - 24;
                ctx.fill(tx, ry + 3, tx + 18, ry + 11, bs.getValue() ? 0xFF8844CC : 0xFF444444);
                int kx = bs.getValue() ? tx + 10 : tx + 2;
                ctx.fill(kx, ry + 4, kx + 7, ry + 10, 0xFFFFFFFF);
            } else if (setting instanceof SliderSetting ss) {
                ctx.drawTextWithShadow(textRenderer, "§7" + ss.getName() + " §5" + String.format("%.1f", ss.getValue()), sx + 4, ry, 0xCCCCCC);
                int bx = sx + 4, bw = SETTINGS_W - 8;
                ctx.fill(bx, ry + 10, bx + bw, ry + 14, 0xFF333333);
                ctx.fill(bx, ry + 10, bx + (int)(bw * ss.getPercent()), ry + 14, 0xFF8844CC);
                int knobX = bx + (int)(bw * ss.getPercent()) - 3;
                ctx.fill(knobX, ry + 8, knobX + 6, ry + 16, 0xFFFFFFFF);
            } else if (setting instanceof EnumSetting es) {
                ctx.drawTextWithShadow(textRenderer, "§7" + es.getName(), sx + 4, ry + 4, 0xCCCCCC);
                String[] opts = es.getOptions();
                int optW = (SETTINGS_W - 8) / opts.length;
                for (int j = 0; j < opts.length; j++) {
                    int ox = sx + 4 + j * optW;
                    boolean sel = opts[j].equals(es.getValue());
                    ctx.fill(ox, ry + 12, ox + optW - 1, ry + 20, sel ? 0xFF8844CC : 0xFF222222);
                    ctx.drawCenteredTextWithShadow(textRenderer, opts[j], ox + optW / 2, ry + 13, sel ? 0xFFFFFF : 0x888888);
                }
            }
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        int mx = (int) mouseX, my = (int) mouseY;

        // Close settings popup if clicking outside
        if (settingsModule != null) {
            List<?> settings = settingsModule.getSettings();
            int sh = 14 + settings.size() * 22 + 4;
            int sx = Math.min(settingsX, width - SETTINGS_W - 4);
            int sy = Math.min(settingsY, height - sh - 4);
            if (mx < sx || mx > sx + SETTINGS_W || my < sy || my > sy + sh) {
                if (button == 0 && !handleSettingsClick(mx, my, button)) {
                    settingsModule = null;
                }
            } else {
                handleSettingsClick(mx, my, button);
                return true;
            }
        }

        for (Category cat : Category.values()) {
            int[] pos = panelPos.get(cat);
            int px = pos[0], py = pos[1];
            boolean minimized = panelMinimized.get(cat);
            List<Module> modules = ModuleManager.getByCategory(cat);

            // Header click — minimize or drag start
            if (mx >= px && mx < px + PANEL_W && my >= py && my < py + PANEL_HEADER_H) {
                if (button == 0) {
                    dragging = cat;
                    dragOffX = mx - px;
                    dragOffY = my - py;
                } else if (button == 1) {
                    panelMinimized.put(cat, !panelMinimized.get(cat));
                }
                return true;
            }

            if (!minimized) {
                for (int i = 0; i < modules.size(); i++) {
                    Module mod = modules.get(i);
                    int modY = py + PANEL_HEADER_H + i * MODULE_H;
                    if (mx >= px && mx < px + PANEL_W && my >= modY && my < modY + MODULE_H) {
                        if (button == 0) {
                            mod.toggle();
                        } else if (button == 1) {
                            if (!mod.getSettings().isEmpty()) {
                                settingsModule = mod;
                                settingsX = mx;
                                settingsY = my;
                            }
                        }
                        return true;
                    }
                }
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    private boolean handleSettingsClick(int mx, int my, int button) {
        if (settingsModule == null) return false;
        List<?> settings = settingsModule.getSettings();
        int sh = 14 + settings.size() * 22 + 4;
        int sx = Math.min(settingsX, width - SETTINGS_W - 4);
        int sy = Math.min(settingsY, height - sh - 4);

        if (mx < sx || mx > sx + SETTINGS_W || my < sy || my > sy + sh) return false;

        for (int i = 0; i < settings.size(); i++) {
            Object setting = settings.get(i);
            int ry = sy + 14 + i * 22;

            if (setting instanceof BooleanSetting bs) {
                int tx = sx + SETTINGS_W - 24;
                if (mx >= tx && mx <= tx + 18 && my >= ry + 3 && my <= ry + 11) {
                    bs.toggle();
                    return true;
                }
            } else if (setting instanceof SliderSetting ss) {
                int bx = sx + 4, bw = SETTINGS_W - 8;
                if (mx >= bx && mx <= bx + bw && my >= ry + 8 && my <= ry + 16) {
                    double pct = (double)(mx - bx) / bw;
                    ss.setValue(ss.getMin() + pct * (ss.getMax() - ss.getMin()));
                    draggingSlider = true;
                    draggingSliderIdx = i;
                    return true;
                }
            } else if (setting instanceof EnumSetting es) {
                String[] opts = es.getOptions();
                int optW = (SETTINGS_W - 8) / opts.length;
                for (int j = 0; j < opts.length; j++) {
                    int ox = sx + 4 + j * optW;
                    if (mx >= ox && mx <= ox + optW - 1 && my >= ry + 12 && my <= ry + 20) {
                        es.setValue(opts[j]);
                        return true;
                    }
                }
            }
        }
        return true;
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        int mx = (int) mouseX, my = (int) mouseY;

        if (dragging != null) {
            int[] pos = panelPos.get(dragging);
            pos[0] = Math.max(0, Math.min(width - PANEL_W, mx - dragOffX));
            pos[1] = Math.max(14, Math.min(height - 20, my - dragOffY));
            return true;
        }

        if (draggingSlider && settingsModule != null) {
            List<?> settings = settingsModule.getSettings();
            int sh = 14 + settings.size() * 22 + 4;
            int sx = Math.min(settingsX, width - SETTINGS_W - 4);
            int sy = Math.min(settingsY, height - sh - 4);

            if (draggingSliderIdx >= 0 && draggingSliderIdx < settings.size()) {
                Object setting = settings.get(draggingSliderIdx);
                if (setting instanceof SliderSetting ss) {
                    int bx = sx + 4, bw = SETTINGS_W - 8;
                    double pct = Math.max(0, Math.min(1, (double)(mx - bx) / bw));
                    ss.setValue(ss.getMin() + pct * (ss.getMax() - ss.getMin()));
                }
            }
            return true;
        }

        return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        dragging = null;
        draggingSlider = false;
        draggingSliderIdx = -1;
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == 344) { // Right Shift
            MinecraftClient.getInstance().setScreen(null);
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }
}
