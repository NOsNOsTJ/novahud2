package dev.novahud.mixin;

import dev.novahud.hud.CpsTracker;
import net.minecraft.client.Mouse;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Mouse.class)
public class GameRendererMixin {

    @Inject(method = "onMouseButton", at = @At("HEAD"))
    private void onMouseButton(long window, int button, int action, int mods, CallbackInfo ci) {
        if (action == 1) {
            if (button == 0) CpsTracker.registerLeft();
            if (button == 1) CpsTracker.registerRight();
        }
    }
}
