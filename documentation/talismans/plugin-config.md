---
title: "Plugin Config"
sidebar_position: 4
---

The plugin's main settings live in `/plugins/Talismans/config.yml`. Edit the file, then run `/talismans reload` to apply your changes.

## Default config.yml

```yaml
crafting:
  discover: true # Automatically add talisman recipes to players' recipe books

read-inventory: true # Check a player's inventory for active talismans
read-enderchest: true # Check a player's ender chest for active talismans
read-shulkerboxes: true # Check a player's shulker boxes for active talismans
top-level-only: true # Only the highest level of any talisman counts as active

offhand-only: false # Require talismans to be in the offhand to work

bag:
  title: "Talisman Bag" # Title shown at the top of the talisman bag menu
  blocked-item: black_stained_glass_pane name:"&cYour talisman bag is not big enough!" # Filler shown in slots the player's bag size has not unlocked
  blocked-item-lore: [ ] # Lore lines for the blocked item. Optional
```

<hr/>

## Where to go next

- **Make a talisman:** the step-by-step [how-to](how-to-make-a-custom-talisman).
- **Commands and permissions:** the [Commands and Permissions](commands-and-permissions) list, including bag-size permissions.