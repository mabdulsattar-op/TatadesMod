package com.tatadesmod.gui;

import com.tatadesmod.module.Module;
import com.tatadesmod.module.ModuleManager;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

import java.util.List;

public class ModMenuScreen extends Screen {
    private final List<Module> modules = ModuleManager.get().getModules();

    protected ModMenuScreen() {
        super(Text.of("Anticheat Test Suite"));
    }

    @Override
    protected void init() {
        int y = 40;
        int x = this.width / 4;
        // header: visible test mode
        y += 10;
        for (Module m : modules) {
            final Module mod = m;
            ButtonWidget toggle = new ButtonWidget(x, y, 150, 20, Text.of(mod.getName() + ": " + (mod.isEnabled() ? "ON" : "OFF")), btn -> {
                mod.toggle();
                btn.setMessage(Text.of(mod.getName() + ": " + (mod.isEnabled() ? "ON" : "OFF")));
            });
            this.addDrawableChild(toggle);
            y += 24;
        }

        this.addDrawableChild(new ButtonWidget(this.width/2 - 100, this.height - 30, 200, 20, Text.of("Close"), b -> this.client.setScreen(null)));
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);
        super.render(matrices, mouseX, mouseY, delta);
        // Title
        drawCenteredText(matrices, this.textRenderer, this.title, this.width/2, 8, 0xFFFFFF);
        // Prominent test mode indicator
        drawCenteredText(matrices, this.textRenderer, Text.of("ANTICHEAT TEST MODE"), this.width/2, 22, 0xFF4444);
        // Short descriptions for each module
        int descY = 40;
        int descX = this.width/2 + 140;
        for (Module m : modules) {
            String desc = getModuleDescription(m.getName());
            drawString(matrices, this.textRenderer, desc, descX, descY, 0xCCCCCC);
            descY += 18;
        }
    }

    private String getModuleDescription(String name) {
        switch (name) {
            case "AutoClicker":
                return "Simulates configurable click rates (CPS) for testing server click detection.";
            case "AutoAim":
                return "Simulates aim adjustments/rotations to test aim/assisted-aim detectors.";
            case "ESP":
                return "Renders entity bounding boxes/names for visibility testing of presence checks.";
            case "Freecam":
                return "Allows camera movement independent of player to test camera/position checks.";
            default:
                return "Test module: " + name;
        }
    }

    @Override
    public boolean shouldPause() { return false; }
}
