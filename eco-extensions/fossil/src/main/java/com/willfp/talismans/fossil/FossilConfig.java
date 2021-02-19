package com.willfp.talismans.fossil;

import com.willfp.eco.util.config.ExtendableConfig;
import com.willfp.talismans.TalismansPlugin;

public class FossilConfig extends ExtendableConfig {
    public FossilConfig() {
        super("config", true, TalismansPlugin.getInstance(), FossilMain.class, "talismans/fossil/");
    }
}
