package com.willfp.talismans.config

import com.willfp.eco.core.EcoPlugin
import com.willfp.eco.core.config.BaseConfig
import com.willfp.eco.core.config.ConfigType
import com.willfp.eco.core.config.yaml.YamlBaseConfig

class TalismansYml(plugin: EcoPlugin) : BaseConfig("talismans", plugin, false, ConfigType.YAML)