package com.willfp.talismans.talismans.util;

import com.google.common.collect.Sets;
import com.willfp.eco.util.integrations.antigrief.AntigriefManager;
import com.willfp.eco.util.internal.PluginDependent;
import com.willfp.eco.util.plugin.AbstractEcoPlugin;
import com.willfp.talismans.integrations.mcmmo.McmmoManager;
import com.willfp.talismans.proxy.proxies.TridentStackProxy;
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
import org.bukkit.inventory.ItemStack;
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
    public WatcherTriggers(@NotNull final AbstractEcoPlugin plugin) {
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

        TalismanChecks.getTalismansOnPlayer(attacker).forEach(talisman -> {
            if (event.isCancelled()) {
                return;
            }

            if (!talisman.isEnabled()) {
                return;
            }

            if (talisman.getDisabledWorlds().contains(attacker.getWorld())) {
                return;
            }

            talisman.onArrowDamage(attacker, victim, arrow, event);
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

        TalismanChecks.getTalismansOnPlayer(attacker).forEach(talisman -> {
            if (event.isCancelled()) {
                return;
            }

            if (!talisman.isEnabled()) {
                return;
            }

            if (talisman.getDisabledWorlds().contains(attacker.getWorld())) {
                return;
            }

            talisman.onTridentDamage(attacker, victim, trident, event);
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
                TalismanChecks.getTalismansOnPlayer(player).forEach(talisman -> {
                    if (event.isCancelled()) {
                        return;
                    }

                    if (!talisman.isEnabled()) {
                        return;
                    }

                    if (talisman.getDisabledWorlds().contains(player.getWorld())) {
                        return;
                    }

                    talisman.onJump(player, event);
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

        TalismanChecks.getTalismansOnPlayer(attacker).forEach(talisman -> {
            if (event.isCancelled()) {
                return;
            }

            if (!talisman.isEnabled()) {
                return;
            }

            if (talisman.getDisabledWorlds().contains(attacker.getWorld())) {
                return;
            }

            talisman.onMeleeAttack(attacker, victim, event);
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

        TalismanChecks.getTalismansOnPlayer(shooter).forEach(talisman -> {
            if (event.isCancelled()) {
                return;
            }

            if (!talisman.isEnabled()) {
                return;
            }

            if (talisman.getDisabledWorlds().contains(shooter.getWorld())) {
                return;
            }

            talisman.onBowShoot(shooter, arrow, event);
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

        TalismanChecks.getTalismansOnPlayer(victim).forEach(talisman -> {
            if (event.isCancelled()) {
                return;
            }

            if (!talisman.isEnabled()) {
                return;
            }

            if (talisman.getDisabledWorlds().contains(victim.getWorld())) {
                return;
            }

            talisman.onFallDamage(victim, event);
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

        TalismanChecks.getTalismansOnPlayer(shooter).forEach(talisman -> {
            if (!talisman.isEnabled()) {
                return;
            }

            if (talisman.getDisabledWorlds().contains(shooter.getWorld())) {
                return;
            }

            talisman.onArrowHit(shooter, event);
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

        TalismanChecks.getTalismansOnPlayer(shooter).forEach(talisman -> {
            if (!talisman.isEnabled()) {
                return;
            }

            if (talisman.getDisabledWorlds().contains(shooter.getWorld())) {
                return;
            }

            talisman.onTridentHit(shooter, event);
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

        TalismanChecks.getTalismansOnPlayer(player).forEach(talisman -> {
            if (event.isCancelled()) {
                return;
            }

            if (!talisman.isEnabled()) {
                return;
            }

            if (talisman.getDisabledWorlds().contains(player.getWorld())) {
                return;
            }

            talisman.onBlockBreak(player, block, event);
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

        TalismanChecks.getTalismansOnPlayer(victim).forEach(talisman -> {
            if (event.isCancelled()) {
                return;
            }

            if (!talisman.isEnabled()) {
                return;
            }

            if (talisman.getDisabledWorlds().contains(victim.getWorld())) {
                return;
            }

            talisman.onDamage(victim, event);
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

        TalismanChecks.getTalismansOnPlayer(victim).forEach(talisman -> {
            if (event.isCancelled()) {
                return;
            }

            if (!talisman.isEnabled()) {
                return;
            }

            if (talisman.getDisabledWorlds().contains(victim.getWorld())) {
                return;
            }

            talisman.onDamageByEntity(victim, event.getDamager(), event);
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

        TalismanChecks.getTalismansOnPlayer(player).forEach(talisman -> {
            if (event.isCancelled()) {
                return;
            }

            if (!talisman.isEnabled()) {
                return;
            }

            if (talisman.getDisabledWorlds().contains(player.getWorld())) {
                return;
            }

            talisman.onDamageBlock(player, block, event);
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
        ItemStack item = ProxyUtils.getProxy(TridentStackProxy.class).getTridentStack(trident);

        TalismanChecks.getTalismansOnPlayer(shooter).forEach(talisman -> {
            if (event.isCancelled()) {
                return;
            }

            if (!talisman.isEnabled()) {
                return;
            }

            if (talisman.getDisabledWorlds().contains(shooter.getWorld())) {
                return;
            }

            talisman.onTridentLaunch(shooter, trident, event);
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

        TalismanChecks.getTalismansOnPlayer(blocker).forEach(talisman -> {
            if (event.isCancelled()) {
                return;
            }

            if (!talisman.isEnabled()) {
                return;
            }

            if (talisman.getDisabledWorlds().contains(blocker.getWorld())) {
                return;
            }

            talisman.onDeflect(blocker, attacker, event);
        });
    }
}
