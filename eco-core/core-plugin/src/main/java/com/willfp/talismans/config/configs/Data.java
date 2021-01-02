package com.willfp.talismans.config.configs;

import com.willfp.eco.util.config.BaseConfig;

import java.io.File;

public class Data extends BaseConfig {
    /**
     * data.yml.
     */
    public Data() {
        super("data", false);
    }

    /**
     * Get the config file.
     *
     * @return The file.
     */
    public File getFile() {
        return this.getConfigFile();
    }
}
