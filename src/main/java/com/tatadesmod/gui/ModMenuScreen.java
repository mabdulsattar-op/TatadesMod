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
        super(Text.of("Tatade's Mod Menu"));
    }

    @Override
    protected void init() {
        int y = 20;
        int x = this.width / 4;
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
        drawCenteredText(matrices, this.textRenderer, this.title, this.width/2, 8, 0xFFFFFF);
    }

    @Override
    public boolean shouldPause() { return false; }
}
