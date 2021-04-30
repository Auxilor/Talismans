package com.willfp.talismans.talismans.util;

import com.google.common.collect.Sets;
import com.willfp.eco.core.EcoPlugin;
import com.willfp.eco.core.PluginDependent;
import com.willfp.eco.core.integrations.antigrief.AntigriefManager;
import com.willfp.eco.core.integrations.mcmmo.McmmoManager;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Trident;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.util.Set;
import java.util.UUID;

@SuppressWarnings("deprecation")
public class WatcherTriggers extends PluginDependent implements Listener {
    /**
     * For jump listeners.
     */
    private static final Set<UUID> PREVIOUS_PLAYERS_ON_GROUND = Sets.newHashSet();

    /**
     * For jump listeners.
     */
    private static final DecimalFormat FORMAT = new DecimalFormat("0.00");

    /**
     * Create new listener for watcher events.
     *
     * @param plugin The plugin to link the events to.
     */
    public WatcherTriggers(@NotNull final EcoPlugin plugin) {
        super(plugin);
    }

    /**
     * Called when an entity shoots another entity with an arrow.
     *
     * @param event The event to listen for.
     */
    @EventHandler(ignoreCancelled = true)
    public void onArrowDamage(@NotNull final EntityDamageByEntityEvent event) {
        if (McmmoManager.isFake(event)) {
            return;
        }

        if (!(event.getDamager() instanceof Arrow)) {
            return;
        }

        if (!(event.getEntity() instanceof LivingEntity)) {
            return;
        }

        if (((Arrow) event.getDamager()).getShooter() == null) {
            return;
        }

        if (!(((Arrow) event.getDamager()).getShooter() instanceof Player)) {
            return;
        }

        Player attacker = (Player) ((Arrow) event.getDamager()).getShooter();
        Arrow arrow = (Arrow) event.getDamager();
        LivingEntity victim = (LivingEntity) event.getEntity();

        if (victim.hasMetadata("NPC")) {
            return;
        }

        if (!AntigriefManager.canInjure(attacker, victim)) {
            return;
        }

        if (event.isCancelled()) {
            return;
        }

        TalismanChecks.getTalismansOnPlayer(attacker).forEach(talismanLevel -> {
            if (event.isCancelled()) {
                return;
            }

            if (!talismanLevel.getTalisman().isEnabled()) {
                return;
            }

            if (talismanLevel.getTalisman().getDisabledWorlds().contains(attacker.getWorld())) {
                return;
            }

            talismanLevel.getTalisman().onArrowDamage(talismanLevel, attacker, victim, arrow, event);
        });
    }

    /**
     * Called when an entity damages another entity with a trident throw.
     *
     * @param event The event to listen for.
     */
    @EventHandler(ignoreCancelled = true)
    public void onTridentDamage(@NotNull final EntityDamageByEntityEvent event) {
        if (McmmoManager.isFake(event)) {
            return;
        }

        if (!(event.getDamager() instanceof Trident)) {
            return;
        }

        if (!(((Trident) event.getDamager()).getShooter() instanceof Player)) {
            return;
        }

        if (((Trident) event.getDamager()).getShooter() == null) {
            return;
        }

        if (!(event.getEntity() instanceof LivingEntity)) {
            return;
        }

        if (event.isCancelled()) {
            return;
        }

        Trident trident = (Trident) event.getDamager();
        Player attacker = (Player) trident.getShooter();

        LivingEntity victim = (LivingEntity) event.getEntity();

        if (victim.hasMetadata("NPC")) {
            return;
        }

        if (!AntigriefManager.canInjure(attacker, victim)) {
            return;
        }

        TalismanChecks.getTalismansOnPlayer(attacker).forEach(talismanLevel -> {
            if (event.isCancelled()) {
                return;
            }

            if (!talismanLevel.getTalisman().isEnabled()) {
                return;
            }

            if (talismanLevel.getTalisman().getDisabledWorlds().contains(attacker.getWorld())) {
                return;
            }

            talismanLevel.getTalisman().onTridentDamage(talismanLevel, attacker, victim, trident, event);
        });
    }

    /**
     * Called when a player jumps.
     *
     * @param event The event to listen for.
     */
    @EventHandler(ignoreCancelled = true)
    public void onJump(@NotNull final PlayerMoveEvent event) {
        if (McmmoManager.isFake(event)) {
            return;
        }

        Player player = event.getPlayer();
        if (player.getVelocity().getY() > 0) {
            float jumpVelocity = 0.42f;
            if (player.hasPotionEffect(PotionEffectType.JUMP)) {
                jumpVelocity += ((float) player.getPotionEffect(PotionEffectType.JUMP).getAmplifier() + 1) * 0.1F;
            }
            jumpVelocity = Float.parseFloat(FORMAT.format(jumpVelocity).replace(',', '.'));
            if (event.getPlayer().getLocation().getBlock().getType() != Material.LADDER
                    && PREVIOUS_PLAYERS_ON_GROUND.contains(player.getUniqueId())
                    && !player.isOnGround()
                    && Float.compare((float) player.getVelocity().getY(), jumpVelocity) == 0) {
                TalismanChecks.getTalismansOnPlayer(player).forEach(talismanLevel -> {
                    if (event.isCancelled()) {
                        return;
                    }

                    if (!talismanLevel.getTalisman().isEnabled()) {
                        return;
                    }

                    if (talismanLevel.getTalisman().getDisabledWorlds().contains(player.getWorld())) {
                        return;
                    }

                    talismanLevel.getTalisman().onJump(talismanLevel, player, event);
                });
            }
        }
        if (player.isOnGround()) {
            PREVIOUS_PLAYERS_ON_GROUND.add(player.getUniqueId());
        } else {
            PREVIOUS_PLAYERS_ON_GROUND.remove(player.getUniqueId());
        }
    }

    /**
     * Called when an entity attacks another entity with a melee attack.
     *
     * @param event The event to listen for.
     */
    @EventHandler(ignoreCancelled = true)
    public void onMeleeAttack(@NotNull final EntityDamageByEntityEvent event) {
        if (McmmoManager.isFake(event)) {
            return;
        }

        if (!(event.getDamager() instanceof Player)) {
            return;
        }

        if (!(event.getEntity() instanceof LivingEntity)) {
            return;
        }

        if (event.isCancelled()) {
            return;
        }

        Player attacker = (Player) event.getDamager();
        LivingEntity victim = (LivingEntity) event.getEntity();

        if (victim.hasMetadata("NPC")) {
            return;
        }

        if (!AntigriefManager.canInjure(attacker, victim)) {
            return;
        }

        TalismanChecks.getTalismansOnPlayer(attacker).forEach(talismanLevel -> {
            if (event.isCancelled()) {
                return;
            }

            if (!talismanLevel.getTalisman().isEnabled()) {
                return;
            }

            if (talismanLevel.getTalisman().getDisabledWorlds().contains(attacker.getWorld())) {
                return;
            }

            talismanLevel.getTalisman().onMeleeAttack(talismanLevel, attacker, victim, event);
        });
    }

    /**
     * Called when an entity shoots a bow.
     *
     * @param event The event to listen for.
     */
    @EventHandler(ignoreCancelled = true)
    public void onBowShoot(@NotNull final EntityShootBowEvent event) {
        if (McmmoManager.isFake(event)) {
            return;
        }

        if (event.getProjectile().getType() != EntityType.ARROW) {
            return;
        }

        if (!(event.getEntity() instanceof Player)) {
            return;
        }

        Player shooter = (Player) event.getEntity();
        Arrow arrow = (Arrow) event.getProjectile();

        TalismanChecks.getTalismansOnPlayer(shooter).forEach(talismanLevel -> {
            if (event.isCancelled()) {
                return;
            }

            if (!talismanLevel.getTalisman().isEnabled()) {
                return;
            }

            if (talismanLevel.getTalisman().getDisabledWorlds().contains(shooter.getWorld())) {
                return;
            }

            talismanLevel.getTalisman().onBowShoot(talismanLevel, shooter, arrow, event);
        });
    }

    /**
     * Called when an entity takes fall damage.
     *
     * @param event The event to listen for.
     */
    @EventHandler(ignoreCancelled = true)
    public void onFallDamage(@NotNull final EntityDamageEvent event) {
        if (McmmoManager.isFake(event)) {
            return;
        }

        if (!event.getCause().equals(EntityDamageEvent.DamageCause.FALL)) {
            return;
        }

        if (!(event.getEntity() instanceof Player)) {
            return;
        }

        Player victim = (Player) event.getEntity();

        TalismanChecks.getTalismansOnPlayer(victim).forEach(talismanLevel -> {
            if (event.isCancelled()) {
                return;
            }

            if (!talismanLevel.getTalisman().isEnabled()) {
                return;
            }

            if (talismanLevel.getTalisman().getDisabledWorlds().contains(victim.getWorld())) {
                return;
            }

            talismanLevel.getTalisman().onFallDamage(talismanLevel, victim, event);
        });
    }

    /**
     * Called when an arrow hits a block or entity.
     *
     * @param event The event to listen for.
     */
    @EventHandler(ignoreCancelled = true)
    public void onArrowHit(@NotNull final ProjectileHitEvent event) {
        if (McmmoManager.isFake(event)) {
            return;
        }

        if (!(event.getEntity().getShooter() instanceof Player)) {
            return;
        }

        if (!(event.getEntity() instanceof Arrow)) {
            return;
        }

        if (event.getEntity().getShooter() == null) {
            return;
        }

        Player shooter = (Player) event.getEntity().getShooter();

        TalismanChecks.getTalismansOnPlayer(shooter).forEach(talismanLevel -> {
            if (!talismanLevel.getTalisman().isEnabled()) {
                return;
            }

            if (talismanLevel.getTalisman().getDisabledWorlds().contains(shooter.getWorld())) {
                return;
            }

            talismanLevel.getTalisman().onArrowHit(talismanLevel, shooter, event);
        });
    }

    /**
     * Called when a trident hits a block or entity.
     *
     * @param event The event to listen for.
     */
    @EventHandler(ignoreCancelled = true)
    public void onTridentHit(@NotNull final ProjectileHitEvent event) {
        if (McmmoManager.isFake(event)) {
            return;
        }

        if (!(event.getEntity().getShooter() instanceof Player)) {
            return;
        }

        if (event.getEntity().getShooter() == null) {
            return;
        }

        if (!(event.getEntity() instanceof Trident)) {
            return;
        }

        Trident trident = (Trident) event.getEntity();
        Player shooter = (Player) event.getEntity().getShooter();

        TalismanChecks.getTalismansOnPlayer(shooter).forEach(talismanLevel -> {
            if (!talismanLevel.getTalisman().isEnabled()) {
                return;
            }

            if (talismanLevel.getTalisman().getDisabledWorlds().contains(shooter.getWorld())) {
                return;
            }

            talismanLevel.getTalisman().onTridentHit(talismanLevel, shooter, event);
        });
    }

    /**
     * Called when a player breaks a block.
     *
     * @param event The event to listen for.
     */
    @EventHandler(ignoreCancelled = true)
    public void onBlockBreak(@NotNull final BlockBreakEvent event) {
        if (McmmoManager.isFake(event)) {
            return;
        }

        Player player = event.getPlayer();
        Block block = event.getBlock();

        if (!AntigriefManager.canBreakBlock(player, block)) {
            return;
        }

        if (event.isCancelled()) {
            return;
        }

        TalismanChecks.getTalismansOnPlayer(player).forEach(talismanLevel -> {
            if (event.isCancelled()) {
                return;
            }

            if (!talismanLevel.getTalisman().isEnabled()) {
                return;
            }

            if (talismanLevel.getTalisman().getDisabledWorlds().contains(player.getWorld())) {
                return;
            }

            talismanLevel.getTalisman().onBlockBreak(talismanLevel, player, block, event);
        });
    }

    /**
     * Called when an entity takes damage wearing armor.
     *
     * @param event The event to listen for.
     */
    @EventHandler(ignoreCancelled = true)
    public void onDamage(@NotNull final EntityDamageEvent event) {
        if (McmmoManager.isFake(event)) {
            return;
        }

        if (!(event.getEntity() instanceof Player)) {
            return;
        }

        Player victim = (Player) event.getEntity();

        TalismanChecks.getTalismansOnPlayer(victim).forEach(talismanLevel -> {
            if (event.isCancelled()) {
                return;
            }

            if (!talismanLevel.getTalisman().isEnabled()) {
                return;
            }

            if (talismanLevel.getTalisman().getDisabledWorlds().contains(victim.getWorld())) {
                return;
            }

            talismanLevel.getTalisman().onDamage(talismanLevel, victim, event);
        });
    }

    /**
     * Called when an entity takes damage wearing armor.
     *
     * @param event The event to listen for.
     */
    @EventHandler(ignoreCancelled = true)
    public void onDamageByEntity(@NotNull final EntityDamageByEntityEvent event) {
        if (McmmoManager.isFake(event)) {
            return;
        }

        if (!(event.getEntity() instanceof Player)) {
            return;
        }

        Player victim = (Player) event.getEntity();

        TalismanChecks.getTalismansOnPlayer(victim).forEach(talismanLevel -> {
            if (event.isCancelled()) {
                return;
            }

            if (!talismanLevel.getTalisman().isEnabled()) {
                return;
            }

            if (talismanLevel.getTalisman().getDisabledWorlds().contains(victim.getWorld())) {
                return;
            }

            talismanLevel.getTalisman().onDamageByEntity(talismanLevel, victim, event.getDamager(), event);
        });
    }

    /**
     * Called when a player damages a block.
     *
     * @param event The event to listen for.
     */
    @EventHandler(ignoreCancelled = true)
    public void onDamageBlock(@NotNull final BlockDamageEvent event) {
        if (McmmoManager.isFake(event)) {
            return;
        }

        Player player = event.getPlayer();
        Block block = event.getBlock();

        if (event.getBlock().getDrops(player.getInventory().getItemInMainHand()).isEmpty()) {
            return;
        }

        TalismanChecks.getTalismansOnPlayer(player).forEach(talismanLevel -> {
            if (event.isCancelled()) {
                return;
            }

            if (!talismanLevel.getTalisman().isEnabled()) {
                return;
            }

            if (talismanLevel.getTalisman().getDisabledWorlds().contains(player.getWorld())) {
                return;
            }

            talismanLevel.getTalisman().onDamageBlock(talismanLevel, player, block, event);
        });
    }

    /**
     * Called when an entity throws a trident.
     *
     * @param event The event to listen for.
     */
    @EventHandler(ignoreCancelled = true)
    public void onTridentLaunch(@NotNull final ProjectileLaunchEvent event) {
        if (McmmoManager.isFake(event)) {
            return;
        }

        if (!(event.getEntity() instanceof Trident)) {
            return;
        }

        if (!(event.getEntity().getShooter() instanceof Player)) {
            return;
        }

        Trident trident = (Trident) event.getEntity();
        Player shooter = (Player) trident.getShooter();

        TalismanChecks.getTalismansOnPlayer(shooter).forEach(talismanLevel -> {
            if (event.isCancelled()) {
                return;
            }

            if (!talismanLevel.getTalisman().isEnabled()) {
                return;
            }

            if (talismanLevel.getTalisman().getDisabledWorlds().contains(shooter.getWorld())) {
                return;
            }

            talismanLevel.getTalisman().onTridentLaunch(talismanLevel, shooter, trident, event);
        });
    }

    /**
     * Called when a player blocks an attack with a shield.
     *
     * @param event The event to listen for.
     */
    @EventHandler(ignoreCancelled = true)
    public void onDeflect(@NotNull final EntityDamageByEntityEvent event) {
        if (McmmoManager.isFake(event)) {
            return;
        }

        if (!(event.getEntity() instanceof Player)) {
            return;
        }

        if (!(event.getDamager() instanceof LivingEntity)) {
            return;
        }

        Player blocker = (Player) event.getEntity();

        LivingEntity attacker = (LivingEntity) event.getDamager();

        if (!blocker.isBlocking()) {
            return;
        }

        if (!AntigriefManager.canInjure(blocker, attacker)) {
            return;
        }

        TalismanChecks.getTalismansOnPlayer(blocker).forEach(talismanLevel -> {
            if (event.isCancelled()) {
                return;
            }

            if (!talismanLevel.getTalisman().isEnabled()) {
                return;
            }

            if (talismanLevel.getTalisman().getDisabledWorlds().contains(blocker.getWorld())) {
                return;
            }

            talismanLevel.getTalisman().onDeflect(talismanLevel, blocker, attacker, event);
        });
    }
}
