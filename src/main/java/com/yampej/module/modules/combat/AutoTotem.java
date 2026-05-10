package com.yampej.module.modules.combat;

import com.yampej.module.Category;
import com.yampej.module.Module;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.Items;
import net.minecraft.screen.slot.SlotActionType;

public class AutoTotem extends Module {
    public AutoTotem() {
        super("AutoTotem", "Automatically equips totems", Category.COMBAT);
        setEnabled(true);
    }

    @Override
    public void onTick() {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.player == null || mc.interactionManager == null) return;

        var offhand = mc.player.getOffHandStack();
        if (offhand.getItem() == Items.TOTEM_OF_UNDYING) return;

        var inv = mc.player.getInventory();
        for (int i = 0; i < 36; i++) {
            var stack = inv.getStack(i);
            if (stack.getItem() == Items.TOTEM_OF_UNDYING) {
                int slot = i < 9 ? i + 36 : i;
                mc.interactionManager.clickSlot(
                    mc.player.playerScreenHandler.syncId, slot, 0, SlotActionType.PICKUP, mc.player);
                mc.interactionManager.clickSlot(
                    mc.player.playerScreenHandler.syncId, 45, 0, SlotActionType.PICKUP, mc.player);
                if (!mc.player.currentScreenHandler.getCursorStack().isEmpty()) {
                    mc.interactionManager.clickSlot(
                        mc.player.playerScreenHandler.syncId, slot, 0, SlotActionType.PICKUP, mc.player);
                }
                break;
            }
        }
    }
}
