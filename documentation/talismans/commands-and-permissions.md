---
title: "Commands and Permissions"
sidebar_position: 3
---

| Command                                       | Description                                               | Permission                 |
|-----------------------------------------------|-----------------------------------------------------------|----------------------------|
| `/talismans give <player> <talisman> <level>` | Give a talisman                                           | `talismans.command.give`   |
| `/talismans bag <player>`                     | Open the talisman bag                                     | `talismans.command.bag`    |
| `/talismans import <id>`                      | Import a talisman from [lrcdb](https://lrcdb.auxilor.io/) | `talismans.command.import` |
| `/talismans export <id>`                      | Export a talisman to [lrcdb](https://lrcdb.auxilor.io/)   | `talismans.command.export` |
| `/talismans reload`                           | Reloads the plugin                                        | `talismans.command.reload` |

### Additional Permissions

| Permission                 | Description                                                                                                         |
|----------------------------|---------------------------------------------------------------------------------------------------------------------|
| `talismans.limit.<amount>` | Limit how many talismans can be active for a player at once. Example: `talismans.limit.5` allows 5 active talismans |
| `talismans.bagsize.<size>` | Set a player's talisman bag size. Example: `talismans.bagsize.8` allows 8 talismans in the bag                      |
