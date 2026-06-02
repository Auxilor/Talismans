---
title: "Commands and Permissions"
sidebar_position: 3
---

Every command Talismans adds, and the permission node that gates it, is listed below. Operators have access to all of them by default.

## Commands

| Command | Description | Permission |
| --- | --- | --- |
| `/talismans give <player> <talisman> <level>` | Give a talisman | `talismans.command.give` |
| `/talismans bag <player>` | Open the talisman bag | `talismans.command.bag` |
| `/talismans import <id>` | Import a talisman from [lrcdb](https://lrcdb.auxilor.io/) | `talismans.command.import` |
| `/talismans export <id>` | Export a talisman to [lrcdb](https://lrcdb.auxilor.io/) | `talismans.command.export` |
| `/talismans reload` | Reload the plugin | `talismans.command.reload` |

## Additional permissions

| Permission | Description |
| --- | --- |
| `talismans.limit.<amount>` | Limit how many talismans can be active for a player at once. Example: `talismans.limit.5` allows 5 active talismans |
| `talismans.bagsize.<size>` | Set a player's talisman bag size. Example: `talismans.bagsize.8` allows 8 talismans in the bag |

<hr/>

## Where to go next

- **Make a talisman:** the step-by-step [how-to](how-to-make-a-custom-talisman).
- **Plugin settings:** the [Plugin Config](plugin-config) reference.