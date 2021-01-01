package com.willfp.talismans.talismans.util;

import org.bukkit.block.Block;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Trident;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.jetbrains.annotations.NotNull;

public interface Watcher {
    /**
     * Called when a player shoots another entity with an arrow.
     *
     * @param attacker The shooter.
     * @param victim   The victim.
     * @param arrow    The arrow entity.
     * @param event    The event that called this watcher.
     */
    default void onArrowDamage(@NotNull final Player attacker,
                               @NotNull final LivingEntity victim,
                               @NotNull final Arrow arrow,
                               @NotNull final EntityDamageByEntityEvent event) {
        // Empty default as talismans only override required watchers.
    }

    /**
     * Called when a player damages another entity with a trident throw.
     *
     * @param attacker The shooter.
     * @param victim   The victim.
     * @param trident  The trident entity.
     * @param event    The event that called this watcher.
     */
    default void onTridentDamage(@NotNull final Player attacker,
                                 @NotNull final LivingEntity victim,
                                 @NotNull final Trident trident,
                                 @NotNull final EntityDamageByEntityEvent event) {
        // Empty default as talismans only override required watchers.
    }

    /**
     * Called when a player jumps.
     *
     * @param player The player.
     * @param event  The event that called this watcher.
     */
    default void onJump(@NotNull final Player player,
                        @NotNull final PlayerMoveEvent event) {
        // Empty default as talismans only override required watchers.
    }

    /**
     * Called when a player another entity with a melee attack.
     *
     * @param attacker The attacker.
     * @param victim   The victim.
     * @param event    The event that called this watcher.
     */
    default void onMeleeAttack(@NotNull final Player attacker,
                               @NotNull final LivingEntity victim,
                               @NotNull final EntityDamageByEntityEvent event) {
        // Empty default as talismans only override required watchers.
    }

    /**
     * Called when a player shoots a bow.
     *
     * @param shooter The player that shot the bow.
     * @param arrow   The arrow that was shot.
     * @param event   The event that called this watcher.
     */
    default void onBowShoot(@NotNull final Player shooter,
                            @NotNull final Arrow arrow,
                            @NotNull final EntityShootBowEvent event) {
        // Empty default as talismans only override required watchers.
    }

    /**
     * Called when a player takes fall damage.
     *
     * @param faller The player that took the fall damage.
     * @param event  The event that called this watcher.
     */
    default void onFallDamage(@NotNull final Player faller,
                              @NotNull final EntityDamageEvent event) {
        // Empty default as talismans only override required watchers.
    }

    /**
     * Called when an arrow hits a block or entity.
     *
     * @param shooter The player that shot the arrow.
     * @param event   The event that called this watcher.
     */
    default void onArrowHit(@NotNull final Player shooter,
                            @NotNull final ProjectileHitEvent event) {
        // Empty default as talismans only override required watchers.
    }

    /**
     * Called when a trident hits a block or entity.
     *
     * @param shooter The player that threw the trident.
     * @param event   The event that called this watcher.
     */
    default void onTridentHit(@NotNull final Player shooter,
                              @NotNull final ProjectileHitEvent event) {
        // Empty default as talismans only override required watchers.
    }

    /**
     * Called when a player breaks a block.
     *
     * @param player The player.
     * @param block  The block that was broken.
     * @param event  The event that called this watcher.
     */
    default void onBlockBreak(@NotNull final Player player,
                              @NotNull final Block block,
                              @NotNull final BlockBreakEvent event) {
        // Empty default as talismans only override required watchers.
    }

    /**
     * Called when a player takes damage.
     *
     * @param victim The player that took damage.
     * @param event  The event that called this watcher.
     */
    default void onDamage(@NotNull final Player victim,
                          @NotNull final EntityDamageEvent event) {
        // Empty default as talismans only override required watchers.
    }

    /**
     * Called when a player damages a block.
     *
     * @param player The player that damaged the block.
     * @param block  The damaged block.
     * @param event  The event that called this watcher.
     */
    default void onDamageBlock(@NotNull final Player player,
                               @NotNull final Block block,
                               @NotNull final BlockDamageEvent event) {
        // Empty default as talismans only override required watchers.
    }

    /**
     * Called when a player throws a trident.
     *
     * @param shooter The player that threw the trident.
     * @param trident The trident that was thrown.
     * @param event   The event that called this watcher.
     */
    default void onTridentLaunch(@NotNull final Player shooter,
                                 @NotNull final Trident trident,
                                 @NotNull final ProjectileLaunchEvent event) {
        // Empty default as talismans only override required watchers.
    }

    /**
     * Called when a player blocks an attack with a shield.
     *
     * @param blocker  The player that blocked the attack.
     * @param attacker The attacker.
     * @param event    The event that called this watcher.
     */
    default void onDeflect(@NotNull final Player blocker,
                           @NotNull final LivingEntity attacker,
                           @NotNull final EntityDamageByEntityEvent event) {
        // Empty default as talismans only override required watchers.
    }
}
