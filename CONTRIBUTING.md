# How to contribute to Talismans

## Codestyle
1. The Talismans checkstyle is in /config/checkstyle.xml
- The pull request must not have any checkstyle issues.
- Every method and field must have a javadoc attached.

2. Use lombok wherever possible.
- @Getter, @Setter, @ToString, @EqualsAndHashCode, @UtilityClass are the most important.

3. Use JetBrains annotations
- Every parameter should be annotated with @NotNull or @Nullable
- Use @NotNull over lombok @NonNull

4. Imports
- No group (*) imports.
- No static imports.

## Dependency Injection
- Any calls to AbstractEcoPlugin#getInstance are code smells and should never be used unless **absolutely necessary**.
- NamespacedKeys, FixedMetadataValues, Runnables, and Schedules should be managed using TalismansPlugin through DI.
- Any DI class should extend PluginDependent where possible. If the class extends another, then you **must** store the plugin instance in a private final variable called **plugin** with a private or protected getter.

## Other
- All drops **must** be sent through a DropQueue - calls to World#dropItem will get your PR rejected.
- Talismans is built with java 8. Usage of J9+ will get your PR rejected.
- Any non-plugin-specific changes **must** be made to eco-util, or core-proxy, rather than core-plugin.