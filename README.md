package com.tatadesmod.module.impl;

import com.tatadesmod.module.Module;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderSystem;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

public class ESPModule extends Module {
    private final MinecraftClient mc = MinecraftClient.getInstance();

    private boolean players = true;
    private boolean mobs = true;
    private boolean items = true;
    private boolean box = true;
    private boolean name = true;
    private boolean distance = true;
    private boolean throughWalls = true;

    public ESPModule() {
        super("ESP", Category.VISUAL);
    }

    @Override
    protected void onEnable() {
        WorldRenderEvents.LAST.register(this::onWorldRender);
    }

    @Override
    protected void onDisable() {
        // Fabric's event system does not provide an unregister; guard by isEnabled in handler
    }

    private void onWorldRender(MatrixStack matrices) {
        if (!isEnabled()) return;
        if (mc.world == null || mc.player == null) return;

        Vec3d cam = mc.gameRenderer.getCamera().getPos();
        for (Entity e : mc.world.getEntities()) {
            if (e == mc.player) continue;
            if (e instanceof PlayerEntity && !players) continue;
            if (e instanceof ItemEntity && !items) continue;
            if (e instanceof LivingEntity && !(e instanceof PlayerEntity) && !mobs) continue;

            Box bb = e.getBoundingBox().offset(-cam.x, -cam.y, -cam.z);
            // simple bounding box draw using lines
            drawBox(bb);
        }
    }

    private void drawBox(Box b) {
        // Lightweight immediate-mode rendering using LineRenderer
        RenderSystem.disableTexture();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        // Minimal drawing to keep it fast. Use existing debug render if possible.
        net.minecraft.client.render.DebugRenderer.drawBox(b.minX, b.minY, b.minZ, b.maxX, b.maxY, b.maxZ, 0f, 1f, 0f, 1f);
        RenderSystem.disableBlend();
        RenderSystem.enableTexture();
    }
}
