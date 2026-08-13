package com.tatadesmod;

import com.tatadesmod.config.ConfigManager;
import com.tatadesmod.gui.ModMenuScreen;
import com.tatadesmod.module.ModuleManager;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class TatadesMod implements ClientModInitializer {

    public static KeyBinding OPEN_MENU_KEY;

    @Override
    public void onInitializeClient() {
        // Load config
        ConfigManager.init();

        // Init modules
        ModuleManager.get();

        // Register keybind
        OPEN_MENU_KEY = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.tatadesmod.open_menu",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_RIGHT_SHIFT,
                "category.tatadesmod"
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (OPEN_MENU_KEY.wasPressed()) {
                if (client.currentScreen == null) {
                    client.setScreen(new ModMenuScreen());
                } else {
                    client.setScreen(null);
                }
            }
            // tick modules
            ModuleManager.get().tickAll();
        });
    }
}
