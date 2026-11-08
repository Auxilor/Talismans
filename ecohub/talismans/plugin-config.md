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
  size: 0 # Starting bag size for everyone. Set to 0 to use bag-size permissions.

  close: # A close button. Optional.
    enabled: false
    item: barrier name:"&cClose"
    location:
      row: 6
      column: 5

  upgrade: # A button that buys one more bag slot. Optional.
    enabled: false
    item: emerald name:"&aBuy a bag slot"
    lore:
      - ""
      - "&fSize: &a%size%&f/&a%max_size%"
      - "&fPrice: %price%"
      - ""
      - "&eClick to buy!"
    max-item: barrier name:"&cYour bag is at its largest size" # Shown when the bag cannot grow any more.
    max-lore: [ ]
    location:
      row: 1
      column: 9
    max-size: 27 # The largest size a player can buy up to.
    price: # The price of one slot. %slot% is the size the bag will have after buying.
      value: "%slot% * 1000"
      type: coins
      display: "&a$%value%"

  custom-slots: [ ] # Extra items such as a back button or decorations. Optional.
```

## Bag size

A player's bag size is worked out in this order:

1. `size`, when it is above 0.
2. Otherwise the highest `talismans.bagsize.<size>` permission the player has.
3. Otherwise the bag has no limit.

Slots a player buys, or that `/talismans bagslots` gives them, are added on top of that size. They are stored with the player's data, so they follow the player across servers. A bag with no limit stays unlimited and cannot buy slots.

## Selling bag slots

Turn on `upgrade` to put a buy button in the bag. Each click buys one slot for the configured price, until the bag reaches `max-size`. The price takes any [price type](https://hub.auxilor.io/wiki/eco/the-price-lookup-system), and `%slot%` lets it rise with each slot: `"%slot% * 1000"` costs 2000 for the second slot and 10000 for the tenth.

To give every player 1 free slot and sell the rest, set `size: 1` and turn on `upgrade`.

| Placeholder | Value |
| --- | --- |
| `%size%` | The player's current bag size. |
| `%max_size%` | The largest size the player can buy up to. |
| `%price%` | The price of the next slot. |
| `%bought%` | How many slots the player has bought or been given. |

The bag always opens with enough rows to show the buy button, so keep it in row 1 if small bags should stay one row tall.

:::tip
To unlock slots through quests, levels or other plugins instead of a price, run `/talismans bagslots add <player> 1` from the console. The buy button is not needed for this.
:::

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
- **Commands and permissions:** the [Commands and Permissions](commands-and-permissions) list, including bag-size permissions and `/talismans bagslots`.
- **Defaults:** the shipped config lives [here](https://github.com/Auxilor/Talismans/blob/master/eco-core/core-plugin/src/main/resources/config.yml).