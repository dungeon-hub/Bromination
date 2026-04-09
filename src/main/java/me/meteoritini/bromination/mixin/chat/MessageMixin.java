package me.meteoritini.bromination.mixin.chat;

import me.meteoritini.bromination.ChatOptions;
import me.meteoritini.bromination.config.BrominationConfig;
import me.meteoritini.bromination.config.overrides.IChatHudLine;
import me.meteoritini.bromination.util.Miner;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.client.gui.hud.ChatHudLine;
import net.minecraft.text.Text;
import net.minecraft.util.Colors;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(value = ChatHud.class, priority = 200)
public class MessageMixin {
    @Shadow @Final private List<ChatHudLine> messages;

    @Shadow @Final private List<ChatHudLine.Visible> visibleMessages;


    @ModifyVariable(method = "addMessage(Lnet/minecraft/text/Text;Lnet/minecraft/network/message/MessageSignatureData;Lnet/minecraft/client/gui/hud/MessageIndicator;)V", at = @At("HEAD"), argsOnly = true)
    public Text mixin_addMessage(Text message) {
        if(!BrominationConfig.getInstance().utilitiesConfig.collapseChat) {
            ChatOptions.nextReference = ++ChatOptions.referenceCounter;
            return message;
        }

        String key = Miner.rawString(message.getString());
        ChatOptions.MessageOccurrence occurrence = ChatOptions.collapse.get(key);
        if(occurrence == null || occurrence.time() + 60000 < System.currentTimeMillis()) {
            ChatOptions.collapse.put(key, new ChatOptions.MessageOccurrence(1, ChatOptions.nextReference = ++ChatOptions.referenceCounter, System.currentTimeMillis()));
            return message;
        }
        messages.removeIf(line -> ((IChatHudLine) (Object) line).bromination$getReference() == occurrence.reference());
        visibleMessages.removeIf(line -> ((IChatHudLine) (Object) line).bromination$getReference() == occurrence.reference());
        message = message.copy().append(Text.literal(" (" + (occurrence.amount()+1) + ")").setStyle(Miner.STYLE_ERASE).withColor(Colors.GRAY));
        ChatOptions.collapse.put(key, new ChatOptions.MessageOccurrence(occurrence.amount()+1, ChatOptions.nextReference = occurrence.reference(), System.currentTimeMillis()));
        return message;
    }

    @Inject(method = "addMessage(Lnet/minecraft/client/gui/hud/ChatHudLine;)V", at = @At(value = "INVOKE", target = "Ljava/util/List;removeLast()Ljava/lang/Object;"), cancellable = true)
    private void mixin_addMessage(ChatHudLine message, CallbackInfo ci) {
        if(BrominationConfig.getInstance().utilitiesConfig.unlimitedChat) ci.cancel();
    }

    @Inject(method = "addVisibleMessage(Lnet/minecraft/client/gui/hud/ChatHudLine;)V", at = @At(value = "INVOKE", target = "Ljava/util/List;removeLast()Ljava/lang/Object;"), cancellable = true)
    private void mixin_addVisibleMessage(ChatHudLine message, CallbackInfo ci) {
        if(BrominationConfig.getInstance().utilitiesConfig.unlimitedChat) ci.cancel();
    }

    @Inject(method = "clear", at = @At("HEAD"), cancellable = true)
    private void mixin_clear(boolean clearHistory, CallbackInfo ci) {
        if(clearHistory && BrominationConfig.getInstance().utilitiesConfig.persistentChat) ci.cancel();
        else ChatOptions.reset();
    }
}