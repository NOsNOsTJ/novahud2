package dev.novahud.hud;

import dev.novahud.NovaHUD;
import dev.novahud.config.HudConfig;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

import java.util.LinkedHashMap;
import java.util.Map;

public class HudMenu extends Screen {

    private static final Map<String, String> MODULES = new LinkedHashMap<>();

    static {
        MODULES.put("fps",    "FPS Counter");
        MODULES.put("ping",   "Ping");
        MODULES.put("cps",    "CPS (click-uri/s)");
        MODULES.put("wasd",   "Taste WASD");
        MODULES.put("coords", "Coordonate XYZ");
        MODULES.put("speed",  "Vitezometru");
        MODULES.put("time",   "Timp in-game");
        MODULES.put("armor",  "Armura");
    }

    private static final int PANEL_W = 260;
    private static final int ROW_H   = 24;
    private static final int PAD     = 10;

    public HudMenu() {
        super(Text.literal("NovaHUD - Meniu"));
    }

    @Override
    protected void init() {
        HudConfig cfg = NovaHUD.config;
        int panelH = PAD * 2 + 20 + MODULES.size() * ROW_H + 30;
        int px = (this.width  - PANEL_W) / 2;
        int py = (this.height - panelH)  / 2;

        int y = py + PAD + 22;
        for (Map.Entry<String, String> entry : MODULES.entrySet()) {
            String id    = entry.getKey();
            String label = entry.getValue();
            boolean on   = cfg.isEnabled(id);

            ButtonWidget btn = ButtonWidget.builder(
                Text.literal((on ? "\u00a7a[ON] " : "\u00a7c[OFF] ") + label),
                b -> {
                    boolean cur = cfg.isEnabled(id);
                    cfg.enabled.put(id, !cur);
                    b.setMessage(Text.literal((!cur ? "\u00a7a[ON] " : "\u00a7c[OFF] ") + label));
                    cfg.save();
                }
            ).dimensions(px + PAD, y, PANEL_W - PAD * 2, ROW_H - 2).build();

            this.addDrawableChild(btn);
            y += ROW_H;
        }

        ButtonWidget closeBtn = ButtonWidget.builder(
            Text.literal("Inchide"),
            b -> this.close()
        ).dimensions(px + PAD, y + 4, PANEL_W - PAD * 2, 20).build();
        this.addDrawableChild(closeBtn);
    }

    @Override
    public void render(DrawContext ctx, int mouseX, int mouseY, float delta) {
        this.renderBackground(ctx, mouseX, mouseY, delta);

        int panelH = PAD * 2 + 20 + MODULES.size() * ROW_H + 30;
        int px = (this.width  - PANEL_W) / 2;
        int py = (this.height - panelH)  / 2;

        ctx.fill(px, py, px + PANEL_W, py + panelH, 0xCC111122);
        ctx.fill(px, py, px + PANEL_W, py + 20, 0xFF1a1a3a);
        ctx.drawBorder(px, py, PANEL_W, panelH, 0xFF5555aa);

        ctx.drawCenteredTextWithShadow(
            this.textRenderer,
            Text.literal("\u00a7bNovaHUD \u00a77- Module"),
            this.width / 2, py + 6, 0xFFFFFF
        );

        super.render(ctx, mouseX, mouseY, delta);
    }

    @Override
    public boolean shouldPause() {
        return false;
    }
}
