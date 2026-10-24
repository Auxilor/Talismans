---
title: "Plugin Config"
sidebar_position: 3
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
  size: 0 # Fixed bag size for everyone. Set to 0 to use bag-size permissions.

  close: # A close button. Optional.
    enabled: false
    item: barrier name:"&cClose"
    location:
      row: 6
      column: 5

  custom-slots: [ ] # Extra items such as a back button or decorations. Optional.
```

## Bag buttons and custom slots

The bag can hold a close button and any number of custom slots, so it can match your other menus. Each custom slot takes an `item`, optional `lore`, a `location` (or a `locations` list to fill several slots with the same item), and click commands such as `left-click`.

```yaml
bag:
  close:
    enabled: true
    item: barrier name:"&cClose"
    location:
      row: 6
      column: 5

  custom-slots:
    - item: arrow name:"&fBack"
      location:
        row: 6
        column: 1
      left-click:
        - "menu open main" # Runs as the player. Prefix with console: to run from the console.
    - item: gray_stained_glass_pane name:" "
      locations:
        - row: 6
          column: 2
        - row: 6
          column: 3
```

Bag slots move around buttons and custom slots, so a player's bag keeps its full size. The bag opens with as few rows as its size needs, and a button or custom slot shows only when the bag has enough rows to reach it.

:::tip
Put the buttons in a row every bag reaches, or set `size` so every bag opens with the same number of rows.
:::

<hr/>

## Where to go next

- **Make a talisman:** the step-by-step [how-to](how-to-make-a-custom-talisman).
- **Commands and permissions:** the [Commands and Permissions](commands-and-permissions) list, including bag-size permissions.