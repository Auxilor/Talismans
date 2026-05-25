---
title: How to make a Talisman
sidebar_position: 1
---

## How to add talismans
Each talisman is its own config file, placed in the `/talismans/` folder, and you can add or remove them as you please. There's an example config called `_example.yml` to help you out!

The ID of the Talisman is the file name. This is what you use in commands and in the [Item Lookup System](https://plugins.auxilor.io/the-item-lookup-system).
ID's must be lowercase letters, numbers, and underscores only.

## Example Talisman Config

```yaml
name: "&aExample Talisman I"
description:
  - "&8Deal 10% more damage with bows"
higherLevelOf: []
item: "player_head texture:eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDBmOGRmYTVlZmM3NTYzMGNlMGRmNDBhNDliOGY1OWJjMjIyMTRkZTk3ZTNmYjQ0YjNjNTZlOGE5YzhhNTZiNiJ9fX0="
craftable: true
recipe-permission: talismans.craft.archery_1 
shapeless: false
recipe:
  - bow
  - crossbow
  - bow

  - crossbow
  - ecoitems:talisman_core_1 ? ender_eye
  - crossbow

  - bow
  - crossbow
  - bow

effects:
  - id: damage_multiplier
    args:
      multiplier: 1.1
    triggers:
      - bow_attack

conditions: []
```

## Understanding all the sections

### The Talisman Info Section
```yaml
name: "&aExample Talisman I"
description:
  - "&8Deal 10% more damage with bows"
higherLevelOf: [] # If the Talisman is higher level of another Talisman - Useful for only having the highest level Talisman active
```

### The Item Section
```yaml
item: "player_head texture:eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDBmOGRmYTVlZmM3NTYzMGNlMGRmNDBhNDliOGY1OWJjMjIyMTRkZTk3ZTNmYjQ0YjNjNTZlOGE5YzhhNTZiNiJ9fX0="
craftable: true # If the talisman is craftable
recipe-permission: talismans.craft.archery_1 # (Optional) The permission required to craft this talisman
shapeless: false # (Optional) Whether the crafting recipe is shapeless, default is false
recipe: # The recipe, read here for more: https://plugins.auxilor.io/the-item-lookup-system/recipes
  - bow
  - crossbow
  - bow

  - crossbow
  - ecoitems:talisman_core_1 ? ender_eye
  - crossbow

  - bow
  - crossbow
  - bow
```

:::tip

We support shaped and shapeless recipes. Check out [Recipes](https://plugins.auxilor.io/the-item-lookup-system/recipes) for more info on how to configure these.

:::

### The Talisman Effects Section
:::dangerEffects Section

The effects section is the core functionality of the talisman. You can configure effects, conditions, filters, mutators and triggers in this section to run whilst the talisman is active.

Check out [Configuring an Effect](https://plugins.auxilor.io/effects/configuring-an-effect) to understand how to configure this section correctly.

For more advanced users or setups, you can configure chains in this section to string together different effects under one trigger. Check out [Configuring an Effect Chain](https://plugins.auxilor.io/effects/configuring-a-chain) for more info.

:::
```yaml
# The effects of the item (i.e. the functionality)
# See here: https://plugins.auxilor.io/effects/configuring-an-effect
effects:
  - id: damage_multiplier
    args:
      multiplier: 1.1
    triggers:
      - bow_attack

# The conditions required for the effects to activate
conditions: []
```

<hr/>

## Default configs
The default configs can be found [here](https://github.com/Auxilor/Talismans/tree/master/eco-core/core-plugin/src/main/resources/talismans).
You can find additional user-created configs on [lrcdb](https://lrcdb.auxilor.io/).


