package dev.novahud;

import dev.novahud.config.HudConfig;
import dev.novahud.hud.HudRenderer;
import dev.novahud.hud.HudMenu;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class NovaHUD implements ClientModInitializer {

    public static final String MOD_ID = "novahud";
    public static HudConfig config;
    public static HudMenu hudMenu;
    private static KeyBinding menuKey;

    @Override
    public void onInitializeClient() {
        config = HudConfig.load();
        hudMenu = new HudMenu();

        menuKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.novahud.menu",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_RIGHT_SHIFT,
            "category.novahud"
        ));

        HudRenderCallback.EVENT.register((drawContext, tickDelta) -> {
            HudRenderer.render(drawContext, tickDelta);
        });

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (menuKey.wasPressed()) {
                if (client.currentScreen == null) {
                    client.setScreen(hudMenu);
                }
            }
        });
    }
}
