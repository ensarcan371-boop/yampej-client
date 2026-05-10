package com.yampej;

import com.yampej.module.ModuleManager;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class YampejClient implements ClientModInitializer {
    public static final String MOD_ID = "yampej";
    public static final String MOD_NAME = "Yampej";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_NAME);

    public static KeyBinding guiKey;

    @Override
    public void onInitializeClient() {
        LOGGER.info("Yampej Client loading...");

        guiKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.yampej.gui",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_RIGHT_SHIFT,
            "category.yampej"
        ));

        ModuleManager.init();

        LOGGER.info("Yampej Client loaded! {} modules registered.", ModuleManager.getAll().size());
    }
}
