---
title: "API"
sidebar_position: 5
---

This page is for developers who want to work with talismans from their own plugin. Talismans is open-source, so you can build against it directly.

## Source code

Browse the full source on [GitHub](https://github.com/Auxilor/Talismans).

## Adding the dependency

1. Add the Auxilor repository to your `build.gradle.kts`.
2. Add Talismans as a `compileOnly` dependency.

```kotlin
repositories {
    maven("https://repo.auxilor.io/repository/maven-public/")
}

dependencies {
    compileOnly("com.willfp:Talismans:<version>")
}
```

The latest version available on the repo can be found [here](https://github.com/Auxilor/Talismans/tags).

<hr/>

## Where to go next

- **Shared APIs:** the [eco framework](https://github.com/Auxilor/eco), where the common APIs live.
- **Config-side setup:** the [how-to](how-to-make-a-custom-talisman) for building talismans without code.