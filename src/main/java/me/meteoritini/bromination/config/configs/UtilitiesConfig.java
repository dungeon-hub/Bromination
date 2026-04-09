package me.meteoritini.bromination.config.configs;

import dev.isxander.yacl3.config.v2.api.SerialEntry;

public class UtilitiesConfig {
    @SerialEntry
    public boolean collapseChat = true;

    @SerialEntry
    public boolean unlimitedChat = true;

    @SerialEntry
    public boolean persistentChat = true;
}
