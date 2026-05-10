package com.yampej.module.modules.combat;

import com.yampej.module.Category;
import com.yampej.module.Module;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ArmorItem;
import net.minecraft.screen.slot.SlotActionType;

public class AutoArmor extends Module {
    public AutoArmor() {
        super("AutoArmor", "Automatically equips armor", Category.COMBAT);
    }

    @Override
    public void onTick() {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.player == null || mc.interactionManager == null) return;

        var inv = mc.player.getInventory();
        for (int i = 0; i < 36; i++) {
            var stack = inv.getStack(i);
            if (stack.getItem() instanceof ArmorItem armor) {
                int armorSlot = switch (armor.getType()) {
                    case HELMET     -> 5;
                    case CHESTPLATE -> 6;
                    case LEGGINGS   -> 7;
                    case BOOTS      -> 8;
                };
                if (armorSlot < 0) continue;
                if (mc.player.playerScreenHandler.getSlot(armorSlot).getStack().isEmpty()) {
                    int slot = i < 9 ? i + 36 : i;
                    mc.interactionManager.clickSlot(
                        mc.player.playerScreenHandler.syncId, slot, 0, SlotActionType.PICKUP, mc.player);
                    mc.interactionManager.clickSlot(
                        mc.player.playerScreenHandler.syncId, armorSlot, 0, SlotActionType.PICKUP, mc.player);
                }
            }
        }
    }
}
