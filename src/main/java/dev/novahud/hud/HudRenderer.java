package dev.novahud.hud;

import dev.novahud.NovaHUD;
import dev.novahud.config.HudConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.glfw.GLFW;

public class HudRenderer {

    private static final int WHITE   = 0xFFFFFFFF;
    private static final int YELLOW  = 0xFFFFFF55;
    private static final int GREEN   = 0xFF55FF55;
    private static final int AQUA    = 0xFF55FFFF;
    private static final int RED     = 0xFFFF5555;
    private static final int GRAY    = 0xFFAAAAAA;
    private static final int BG      = 0x88000000;

    private static Vec3d lastPos = Vec3d.ZERO;
    private static long lastTime = System.currentTimeMillis();
    private static double speed = 0.0;

    public static void render(DrawContext ctx, float tickDelta) {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.player == null || mc.world == null) return;
        if (mc.currentScreen != null && !(mc.currentScreen instanceof HudMenu)) return;

        HudConfig cfg = NovaHUD.config;
        ClientPlayerEntity player = mc.player;

        // --- FPS ---
        if (cfg.isEnabled("fps")) {
            int[] p = cfg.getPos("fps");
            int fps = mc.getCurrentFps();
            int col = fps >= 60 ? GREEN : fps >= 30 ? YELLOW : RED;
            drawBg(ctx, p[0], p[1], 60, 10);
            ctx.drawText(mc.textRenderer, "FPS: " + fps, p[0] + 2, p[1] + 1, col, false);
        }

        // --- Ping ---
        if (cfg.isEnabled("ping")) {
            int[] p = cfg.getPos("ping");
            int ping = getPing(mc);
            int col = ping < 80 ? GREEN : ping < 150 ? YELLOW : RED;
            drawBg(ctx, p[0], p[1], 75, 10);
            ctx.drawText(mc.textRenderer, "Ping: " + ping + "ms", p[0] + 2, p[1] + 1, col, false);
        }

        // --- CPS ---
        if (cfg.isEnabled("cps")) {
            int[] p = cfg.getPos("cps");
            int lc = CpsTracker.getLeft();
            int rc = CpsTracker.getRight();
            drawBg(ctx, p[0], p[1], 110, 10);
            ctx.drawText(mc.textRenderer,
                "CPS: " + lc + " L  " + rc + " R",
                p[0] + 2, p[1] + 1, AQUA, false);
        }

        // --- WASD ---
        if (cfg.isEnabled("wasd")) {
            int[] p = cfg.getPos("wasd");
            long win = mc.getWindow().getHandle();
            boolean w = GLFW.glfwGetKey(win, GLFW.GLFW_KEY_W) == GLFW.GLFW_PRESS;
            boolean a = GLFW.glfwGetKey(win, GLFW.GLFW_KEY_A) == GLFW.GLFW_PRESS;
            boolean s = GLFW.glfwGetKey(win, GLFW.GLFW_KEY_S) == GLFW.GLFW_PRESS;
            boolean d = GLFW.glfwGetKey(win, GLFW.GLFW_KEY_D) == GLFW.GLFW_PRESS;
            boolean sp = GLFW.glfwGetKey(win, GLFW.GLFW_KEY_SPACE) == GLFW.GLFW_PRESS;

            drawBg(ctx, p[0], p[1], 40, 30);
            drawKey(ctx, mc, "W", p[0] + 14, p[1] + 1,  w);
            drawKey(ctx, mc, "A", p[0] + 1,  p[1] + 11, a);
            drawKey(ctx, mc, "S", p[0] + 14, p[1] + 11, s);
            drawKey(ctx, mc, "D", p[0] + 27, p[1] + 11, d);
            drawKey(ctx, mc, "[ ]", p[0] + 1, p[1] + 21, sp);
        }

        // --- Coords ---
        if (cfg.isEnabled("coords")) {
            int[] p = cfg.getPos("coords");
            int x = (int) player.getX();
            int y = (int) player.getY();
            int z = (int) player.getZ();
            drawBg(ctx, p[0], p[1], 120, 10);
            ctx.drawText(mc.textRenderer,
                "X:" + x + " Y:" + y + " Z:" + z,
                p[0] + 2, p[1] + 1, WHITE, false);
        }

        // --- Speed ---
        if (cfg.isEnabled("speed")) {
            int[] p = cfg.getPos("speed");
            Vec3d pos = player.getPos();
            long now = System.currentTimeMillis();
            long dt = now - lastTime;
            if (dt >= 100) {
                double dx = pos.x - lastPos.x;
                double dz = pos.z - lastPos.z;
                speed = Math.sqrt(dx*dx + dz*dz) / dt * 1000.0;
                lastPos = pos;
                lastTime = now;
            }
            drawBg(ctx, p[0], p[1], 110, 10);
            ctx.drawText(mc.textRenderer,
                String.format("Speed: %.2f b/s", speed),
                p[0] + 2, p[1] + 1, YELLOW, false);
        }

        // --- Time ---
        if (cfg.isEnabled("time")) {
            int[] p = cfg.getPos("time");
            long worldTime = mc.world.getTimeOfDay() % 24000;
            int hours = (int) ((worldTime / 1000 + 6) % 24);
            int minutes = (int) ((worldTime % 1000) * 60 / 1000);
            drawBg(ctx, p[0], p[1], 90, 10);
            ctx.drawText(mc.textRenderer,
                String.format("Timp: %02d:%02d", hours, minutes),
                p[0] + 2, p[1] + 1, AQUA, false);
        }

        // --- Armor ---
        if (cfg.isEnabled("armor")) {
            int[] p = cfg.getPos("armor");
            int armor = player.getArmor();
            int col = armor >= 15 ? GREEN : armor >= 8 ? YELLOW : RED;
            drawBg(ctx, p[0], p[1], 80, 10);
            ctx.drawText(mc.textRenderer,
                "Armura: " + armor,
                p[0] + 2, p[1] + 1, col, false);
        }
    }

    private static void drawBg(DrawContext ctx, int x, int y, int w, int h) {
        ctx.fill(x, y, x + w, y + h, BG);
    }

    private static void drawKey(DrawContext ctx, MinecraftClient mc,
                                String label, int x, int y, boolean pressed) {
        int bg = pressed ? 0xAA55FF55 : 0x88222222;
        int tc = pressed ? 0xFF003300 : GRAY;
        int w = label.length() * 5 + 4;
        ctx.fill(x, y, x + w, y + 9, bg);
        ctx.drawText(mc.textRenderer, label, x + 2, y + 1, tc, false);
    }

    private static int getPing(MinecraftClient mc) {
        if (mc.player == null || mc.getNetworkHandler() == null) return 0;
        PlayerListEntry entry = mc.getNetworkHandler()
            .getPlayerListEntry(mc.player.getUuid());
        return entry != null ? entry.getLatency() : 0;
    }
}
