package com.tatadesmod.module.impl;

import com.tatadesmod.module.Module;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;

public class FreecamModule extends Module {
    private final MinecraftClient mc = MinecraftClient.getInstance();
    private Vec3d savedPos = null;
    private float savedYaw, savedPitch;
    private boolean enabledInternal = false;
    private double speed = 0.5;

    private Vec3d cameraPos = null;
    private float cameraYaw = 0f, cameraPitch = 0f;

    public FreecamModule() {
        super("Freecam", Category.CAMERA);
    }

    @Override
    protected void onEnable() {
        if (mc.player == null) return;
        savedPos = mc.player.getPos();
        savedYaw = mc.player.getYaw();
        savedPitch = mc.player.getPitch();
        cameraPos = savedPos;
        cameraYaw = savedYaw;
        cameraPitch = savedPitch;
        enabledInternal = true;
    }

    @Override
    protected void onDisable() {
        if (!enabledInternal) return;
        // restore camera to player
        if (mc.player != null && savedPos != null) {
            mc.player.setPosition(savedPos.x, savedPos.y, savedPos.z);
            mc.player.setYaw(savedYaw);
            mc.player.setPitch(savedPitch);
        }
        enabledInternal = false;
    }

    @Override
    public void tick() {
        if (!isEnabled()) return;
        if (!enabledInternal) return;
        // Simple freecam: allow rotation via mouse and movement via keyboard by moving the cameraPos
        // For safety and simplicity we do not change the real player position until disabled.
        // Movement input
        if (mc.player == null) return;
        double forward = 0, sideways = 0, vertical = 0;
        if (mc.options.forwardKey.isPressed()) forward += 1;
        if (mc.options.backKey.isPressed()) forward -= 1;
        if (mc.options.leftKey.isPressed()) sideways += 1;
        if (mc.options.rightKey.isPressed()) sideways -= 1;
        if (mc.options.jumpKey.isPressed()) vertical += 1;
        if (mc.options.sneakKey.isPressed()) vertical -= 1;

        // compute direction
        float yawRad = (float)Math.toRadians(cameraYaw);
        Vec3d forwardVec = new Vec3d(-Math.sin(yawRad), 0, Math.cos(yawRad)).multiply(forward);
        Vec3d rightVec = new Vec3d(-forwardVec.z, 0, forwardVec.x).multiply(sideways);

        Vec3d delta = forwardVec.add(rightVec).add(new Vec3d(0, vertical, 0)).multiply(speed);
        cameraPos = cameraPos.add(delta);

        // apply camera position to player's render camera
        mc.gameRenderer.getCamera().setPos(cameraPos);
        mc.player.setYaw(cameraYaw);
        mc.player.setPitch(cameraPitch);
    }

    public double getSpeed() { return speed; }
    public void setSpeed(double s) { speed = s; }
}
