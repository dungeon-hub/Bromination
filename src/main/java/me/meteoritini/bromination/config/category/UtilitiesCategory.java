package me.meteoritini.bromination.config.category;

import dev.isxander.yacl3.api.*;
import dev.isxander.yacl3.api.controller.BooleanControllerBuilder;
import me.meteoritini.bromination.ChatOptions;
import me.meteoritini.bromination.config.BrominationConfig;
import net.minecraft.text.Text;

public class UtilitiesCategory {
    public static ConfigCategory create(BrominationConfig defaults, BrominationConfig config) {
        return ConfigCategory.createBuilder()
                .name(Text.literal("Utilities Config"))
                .group(
                        OptionGroup.createBuilder()
                                .name(Text.literal("Chat settings"))
                                .description(OptionDescription.of(Text.literal("Utilities for utilizing the minecraft chat")))
                                .option(
                                        Option.<Boolean>createBuilder()
                                                .name(Text.literal("Collapse chat"))
                                                .description(OptionDescription.of(Text.literal("Collapse repeating messages.")))
                                                .binding(
                                                        defaults.utilitiesConfig.collapseChat,
                                                        () -> config.utilitiesConfig.collapseChat,
                                                        newVal -> {
                                                            if(!newVal) ChatOptions.reset();
                                                            config.utilitiesConfig.collapseChat = newVal;
                                                        }
                                                )
                                                .controller(BooleanControllerBuilder::create)
                                                .build()
                                )
                                .option(
                                        Option.<Boolean>createBuilder()
                                                .name(Text.literal("Unlimited chat"))
                                                .description(OptionDescription.of(Text.literal("Don't delete old messages if the chat gets too long.")))
                                                .binding(
                                                        defaults.utilitiesConfig.unlimitedChat,
                                                        () -> config.utilitiesConfig.unlimitedChat,
                                                        newVal -> config.utilitiesConfig.unlimitedChat = newVal
                                                )
                                                .controller(BooleanControllerBuilder::create)
                                                .build()
                                )
                                .option(
                                        Option.<Boolean>createBuilder()
                                                .name(Text.literal("Persistent chat"))
                                                .description(OptionDescription.of(Text.literal("Keep chat messages between servers and reconnects.")))
                                                .binding(
                                                        defaults.utilitiesConfig.persistentChat,
                                                        () -> config.utilitiesConfig.persistentChat,
                                                        newVal -> config.utilitiesConfig.persistentChat = newVal
                                                )
                                                .controller(BooleanControllerBuilder::create)
                                                .build()
                                )
                                .build()
                ).build();
    }
}
