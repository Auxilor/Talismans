package com.willfp.talismans.talismans;

import com.willfp.eco.core.EcoPlugin;
import com.willfp.eco.core.config.Config;
import com.willfp.eco.core.display.Display;
import com.willfp.eco.core.items.CustomItem;
import com.willfp.eco.core.items.Items;
import com.willfp.eco.core.items.builder.ItemStackBuilder;
import com.willfp.eco.core.recipe.recipes.ShapedCraftingRecipe;
import com.willfp.eco.util.StringUtils;
import com.willfp.talismans.TalismansPlugin;
import com.willfp.talismans.talismans.util.TalismanChecks;
import com.willfp.talismans.talismans.util.TalismanUtils;
import lombok.AccessLevel;
import lombok.Getter;
import org.apache.commons.lang.Validate;
import org.apache.commons.lang.WordUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

public class TalismanLevel {
    /**
     * Instance of Talismans for talismans to be able to access.
     */
    @Getter(AccessLevel.PROTECTED)
    private final EcoPlugin plugin = TalismansPlugin.getInstance();

    /**
     * The key to store talismans in meta.
     */
    @Getter
    private final NamespacedKey key;

    /**
     * UUID created for the talisman.
     */
    @Getter
    private final UUID uuid;

    /**
     * The display name of the talisman.
     */
    @Getter
    private String name;

    /**
     * The description of the talisman.
     */
    @Getter
    private String description;

    /**
     * Get description of talisman formatted.
     */
    @Getter
    private List<String> formattedDescription;

    /**
     * The talisman level.
     */
    @Getter
    private final int level;

    /**
     * The talisman.
     */
    @Getter
    private final Talisman talisman;

    /**
     * The talisman's config.
     */
    @Getter
    private final Config config;

    /**
     * The talisman item.
     */
    @Getter
    private ItemStack itemStack;

    /**
     * The talisman recipe.
     */
    @Getter
    private ShapedCraftingRecipe recipe = null;

    /**
     * If the talisman is craftable.
     */
    @Getter
    private boolean craftable = true;

    /**
     * The base64 skull texture.
     */
    @Getter
    private String skullBase64;

    /**
     * Create a new Talisman Level.
     *
     * @param talisman The talisman.
     * @param level    The level.
     * @param config   The config.
     */
    protected TalismanLevel(@NotNull final Talisman talisman,
                            final int level,
                            @NotNull final Config config) {
        this.config = config;
        this.level = level;
        this.talisman = talisman;
        this.key = this.getPlugin().getNamespacedKeyFactory().create(talisman.getKey().getKey() + "_" + level);
        this.uuid = UUID.nameUUIDFromBytes(this.getKey().getKey().getBytes());

        if (Bukkit.getPluginManager().getPermission("talismans.fromtable." + this.getKey().getKey()) == null) {
            Permission permission = new Permission(
                    "talismans.fromtable." + this.getKey().getKey(),
                    "Allows getting " + this.getKey().getKey() + " from a Crafting Table",
                    PermissionDefault.TRUE
            );
            permission.addParent(Objects.requireNonNull(Bukkit.getPluginManager().getPermission("talismans.fromtable.*")), true);
            Bukkit.getPluginManager().addPermission(permission);
        }
    }

    /**
     * Update the talisman based off config values.
     * This can be overridden but may lead to unexpected behavior.
     */
    public void update() {
        name = config.getString("name");
        description = config.getString("description");
        skullBase64 = config.getString(Talismans.GENERAL_LOCATION + "texture");
        Material material = Material.getMaterial(config.getString(Talismans.GENERAL_LOCATION + "material").toUpperCase());
        Validate.notNull(material, "Material specified for " + this.getKey().getKey() + " is invalid!");
        TalismanUtils.registerTalismanMaterial(material);

        formattedDescription = Arrays.stream(
                WordUtils.wrap(description, this.getPlugin().getConfigYml().getInt("description.wrap"), "\n", false)
                        .split("[\\r\\n]+")
        ).map(s -> Display.PREFIX + StringUtils.translate(this.getPlugin().getLangYml().getString("description-color") + s)).collect(Collectors.toList());

        craftable = config.getBool(Talismans.OBTAINING_LOCATION + "craftable");

        ItemStack out = new ItemStackBuilder(material)
                .setAmount(1)
                .writeMetaKey(this.getTalisman().getKey(), PersistentDataType.INTEGER, this.getLevel())
                .build();

        Display.display(out);

        this.itemStack = out;

        new CustomItem(this.getKey(), test -> Objects.equals(this, TalismanChecks.getTalismanOnItem(test)), out).register();

        if (this.isCraftable()) {
            ShapedCraftingRecipe.Builder builder = ShapedCraftingRecipe.builder(this.getPlugin(), this.getKey().getKey())
                    .setOutput(out);

            List<String> recipeStrings = this.getConfig().getStrings(Talismans.OBTAINING_LOCATION + "recipe");

            for (int i = 0; i < 9; i++) {
                builder.setRecipePart(i, Items.lookup(recipeStrings.get(i)));
            }

            this.recipe = builder.build();
            this.recipe.register();
        }

        postUpdate();
    }

    protected void postUpdate() {
        // Unused as some talismans may have postUpdate tasks, however most won't.
    }

    @Override
    public boolean equals(@Nullable final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TalismanLevel that)) {
            return false;
        }
        return getLevel() == that.getLevel() && Objects.equals(getTalisman(), that.getTalisman());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getLevel(), getTalisman());
    }
}
