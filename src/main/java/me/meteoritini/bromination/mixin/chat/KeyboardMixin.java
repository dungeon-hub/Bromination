package me.meteoritini.bromination.mixin.chat;

import me.meteoritini.bromination.ChatOptions;
import net.minecraft.client.Keyboard;
import net.minecraft.client.input.KeyInput;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Keyboard.class)
public class KeyboardMixin {
    @Inject(method = "onKey", at = @At("HEAD"))
    private void onKey(long window, int action, KeyInput input, CallbackInfo ci) {
        if(input.key() == GLFW.GLFW_KEY_LEFT_CONTROL || input.key() == GLFW.GLFW_KEY_RIGHT_CONTROL) ChatOptions.ctrlPressed = action >= 1;
    }
}
