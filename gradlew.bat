package com.tatadesmod.module.impl;

import com.tatadesmod.config.ConfigManager;
import com.tatadesmod.module.Module;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class AutoAimModule extends Module {
    private final MinecraftClient mc = MinecraftClient.getInstance();

    private double maxRange = 6.0;
    private boolean players = true;
    private boolean mobs = true;
    private boolean visibleOnly = true;
    private double rotationSpeed = 10.0; // degrees per tick
    private boolean smooth = true;

    public AutoAimModule() {
        super("AutoAim", Category.COMBAT);
    }

    @Override
    protected void onEnable() {
        Object v;
        ConfigManager.ConfigData cd = ConfigManager.get();
        v = cd.values.getOrDefault("autoaim.range", maxRange);
        maxRange = ((Number) v).doubleValue();
    }

    @Override
    public void tick() {
        if (!isEnabled()) return;
        if (mc.player == null || mc.world == null) return;
        if (mc.currentScreen != null) return;

        Optional<Entity> target = findTarget();
        target.ifPresent(this::aimAt);
    }

    private Optional<Entity> findTarget() {
        List<Entity> ents = mc.world.getEntities();
        return ents.stream()
                .filter(e -> e != mc.player)
                .filter(e -> e instanceof LivingEntity)
                .filter(e -> {
                    double d = e.squaredDistanceTo(mc.player);
                    return d <= (maxRange * maxRange);
                })
                .filter(e -> {
                    if (e instanceof PlayerEntity && !players) return false;
                    if (!(e instanceof PlayerEntity) && !mobs) return false;
                    return true;
                })
                .min(Comparator.comparingDouble(e -> mc.player.squaredDistanceTo(e)));
    }

    private void aimAt(Entity e) {
        Vec3d eye = mc.player.getEyePos();
        Vec3d targetVec = e.getPos().add(0, e.getHeight() * 0.7, 0);
        Vec3d diff = targetVec.subtract(eye);
        double dx = diff.x;
        double dy = diff.y;
        double dz = diff.z;
        double dist = Math.sqrt(dx*dx+dz*dz);
        double yaw = Math.toDegrees(Math.atan2(dz, dx)) - 90.0;
        double pitch = -Math.toDegrees(Math.atan2(dy, dist));

        // smooth rotation
        if (smooth) {
            double curYaw = mc.player.getYaw();
            double curPitch = mc.player.getPitch();
            double yawDelta = wrapDegrees(yaw - curYaw);
            double pitchDelta = wrapDegrees(pitch - curPitch);
            double factor = Math.min(1.0, rotationSpeed / Math.max(0.001, Math.hypot(yawDelta, pitchDelta)));
            mc.player.setYaw((float)(curYaw + yawDelta * factor));
            mc.player.setPitch((float)(curPitch + pitchDelta * factor));
        } else {
            mc.player.setYaw((float)yaw);
            mc.player.setPitch((float)pitch);
        }
    }

    private double wrapDegrees(double d) {
        d %= 360.0;
        if (d >= 180.0) d -= 360.0;
        if (d < -180.0) d += 360.0;
        return d;
    }

    // setters/getters for GUI
    public double getMaxRange() { return maxRange; }
    public void setMaxRange(double r) { maxRange = r; ConfigManager.get().values.put("autoaim.range", r); ConfigManager.save(); }
}
