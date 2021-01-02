package com.willfp.talismans.config.configs;

import com.willfp.eco.util.config.BaseConfig;

import java.io.File;

public class Data extends BaseConfig {
    /**
     * data.yml.
     */
    public Data() {
        super("data.yml", false);
    }

    public File getFile() {
        return super.configFile;
    }
}
