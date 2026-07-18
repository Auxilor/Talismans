---
title: "How to Make a Talisman"
sidebar_position: 1
---

A **talisman** is a config-driven item that runs **effects** while it sits in a player's inventory, ender chest, or shulker box. Each talisman is one `.yml` file built from a few parts: its **display**, its **item and recipe**, and its **effects**. This page walks you from a blank file to a working, craftable talisman.

## Quick start

1. Open `/plugins/Talismans/talismans/`.
2. Copy `_example.yml` to a new file, e.g. `archery_1.yml`. The file name (without `.yml`) becomes the talisman's ID.
3. Edit the `name`, `description`, `item`, `recipe`, and `effects` to taste.
4. Run `/talismans reload`.
5. Give it to yourself with `/talismans give <player> archery_1 1`, put it in your inventory, and confirm the buff applies.

:::tip
`_example.yml` is included as a reference and is **never loaded**, so copy or rename it to make a real talisman. You can also organise talismans into subfolders inside `talismans/`, and they'll still load.
:::

## Naming and IDs

The file name without `.yml` is the talisman's ID. That ID is what you pass to commands and to the [Item Lookup System](https://plugins.auxilor.io/the-item-lookup-system).

:::warning ID rules
IDs may only contain lowercase letters, numbers, and underscores (a-z, 0-9, _). No spaces, capitals, or hyphens, or the talisman will not load.
:::

## The structure of a talisman

| Part | What it controls |
| --- | --- |
| **Display** | The name and description shown on the item |
| **Leveling** | Whether this talisman outranks another level |
| **Item and recipe** | The item players get and how they craft it |
| **Effects** | What the talisman does while active |

```yaml
# === Display: what the player sees ===
name: "&aExample Talisman I" # Display name; supports color codes
description: # Lore lines shown on the item
  - "&8Deal 10% more damage with bows"

# === Leveling: how levels stack ===
higherLevelOf: [] # IDs this talisman outranks; only the highest level a player holds stays active. Optional

# === Item and recipe: what players get and how ===
item: "player_head texture:eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDBmOGRmYTVlZmM3NTYzMGNlMGRmNDBhNDliOGY1OWJjMjIyMTRkZTk3ZTNmYjQ0YjNjNTZlOGE5YzhhNTZiNiJ9fX0=" # The item, using Item Lookup syntax
craftable: true # Whether players can craft this talisman
recipe-permission: talismans.craft.archery_1 # Permission required to craft it. Optional
shapeless: false # Whether the recipe ignores slot positions. Optional, default false
recipe: # The 3x3 grid, read left to right, top to bottom
  - bow
  - crossbow
  - bow

  - crossbow
  - ecoitems:talisman_core_1 ? ender_eye
  - crossbow

  - bow
  - crossbow
  - bow

# === Effects: what the talisman does while active ===
effects: # Effects that run while the talisman is held
  - id: damage_multiplier
    args:
      multiplier: 1.1
    triggers:
      - bow_attack
conditions: [] # Conditions that must pass for the effects to run
```

### Display

The name and lore the player sees on the item.

```yaml
name: "&aExample Talisman I" # Display name; supports color codes
description: # Lore lines shown on the item
  - "&8Deal 10% more damage with bows"
```

### Leveling

Stack levels of the same talisman so only the strongest one a player holds counts.

```yaml
higherLevelOf: [] # IDs this talisman outranks; only the highest level a player holds stays active. Optional
```

### Item and recipe

The item players receive and, optionally, the recipe to craft it.

```yaml
item: "player_head texture:eyJ0ZXh0dXJlcyI6..." # The item, using Item Lookup syntax
craftable: true # Whether players can craft this talisman
recipe-permission: talismans.craft.archery_1 # Permission required to craft it. Optional
shapeless: false # Whether the recipe ignores slot positions. Optional, default false
recipe: # The 3x3 grid, read left to right, top to bottom
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
We support shaped and shapeless recipes. See [Recipes](https://plugins.auxilor.io/the-item-lookup-system/recipes) for the full syntax.
:::

### Effects

What the talisman does while it is active, and the conditions that gate it.

```yaml
effects: # Effects that run while the talisman is held
  - id: damage_multiplier
    args:
      multiplier: 1.1
    triggers:
      - bow_attack
conditions: [] # Conditions that must pass for the effects to run
```

:::danger Effects are their own system
Effects and conditions are a shared system across every eco plugin, documented in full elsewhere.

- [Configuring an Effect](https://plugins.auxilor.io/effects/configuring-an-effect)
- [Configuring an Effect Chain](https://plugins.auxilor.io/effects/configuring-a-chain)
:::

:::tip Troubleshooting
- **Talisman not loading?** Check the ID rules above; capitals, spaces, or hyphens in the file name stop it loading.
- **Buff not applying?** Confirm the talisman is in a read source (inventory, ender chest, or shulker box) and that the relevant `read-*` option is enabled in the [Plugin Config](plugin-config).
- **Can't craft it?** Make sure `craftable` is `true` and the player has the `recipe-permission`, if you set one.
- **Lower level still active?** List the lower talisman's ID under `higherLevelOf` on the higher one, and keep `top-level-only` enabled.
:::

<hr/>

## Where to go next

- **Default talismans:** the shipped configs on [GitHub](https://github.com/Auxilor/Talismans/tree/master/eco-core/core-plugin/src/main/resources/talismans).
- **Community configs:** user-created talismans on [lrcdb](https://lrcdb.auxilor.io/).
- **Effects deep-dive:** [Configuring an Effect](https://plugins.auxilor.io/effects/configuring-an-effect).
- **Plugin settings:** the [Plugin Config](plugin-config) reference.