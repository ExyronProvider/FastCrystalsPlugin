package xyz.exyron.fastcrystals;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.md_5.bungee.api.ChatColor;
import net.minecraft.network.protocol.game.PacketPlayOutEntityDestroy;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.craftbukkit.v1_19_R3.entity.CraftPlayer;
import org.bukkit.entity.EnderCrystal;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

public final class FastCrystalsPlugin extends JavaPlugin implements Listener {

  private final FileConfiguration configuration = this.getConfig();
  private final boolean currentStatus = configuration.getBoolean("defaultStatus");
  private final Set<UUID> playersUUID = new HashSet<>();
  private final Pattern HEX_PATTERN = Pattern.compile("#[A-Fa-f0-9]{6}");

  @Override
  public void onEnable() {
    saveDefaultConfig();

    String enableMessage = colorize(Objects.requireNonNull(this.getConfig().getString("enableMessage")));
    String disableMessage = colorize(Objects.requireNonNull(this.getConfig().getString("disableMessage")));

    Objects.requireNonNull(getCommand("fastcrystals")).setExecutor(((sender, command, label, args) -> {
      if (sender instanceof Player player) {
        if (this.playersUUID.contains(player.getUniqueId())) {
          player.sendMessage(disableMessage);
          this.playersUUID.remove(player.getUniqueId());
        } else {
          player.sendMessage(enableMessage);
          this.playersUUID.add(player.getUniqueId());
        }
      }
      return false;
    }));

    getServer().getPluginManager().registerEvents(this, this);
  }

  @EventHandler(priority = EventPriority.HIGHEST)
  public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
    if (event.getDamager() instanceof Player player && event.getEntity() instanceof EnderCrystal && this.playersUUID.contains(player.getUniqueId())) {
      ((CraftPlayer) player).getHandle().b.a(new PacketPlayOutEntityDestroy(event.getEntity().getEntityId()));
    }
  }

  @EventHandler(priority = EventPriority.MONITOR)
  public void onPlayerJoin(PlayerJoinEvent event) {
    if (currentStatus) {
      this.playersUUID.add(event.getPlayer().getUniqueId());
    }
  }

  private String colorize(String message) {
    Matcher matcher = HEX_PATTERN.matcher(message);
    StringBuilder result = new StringBuilder();

    while (matcher.find()) {
      matcher.appendReplacement(result, ChatColor.of(matcher.group()).toString());
    }

    matcher.appendTail(result);
    message = result.toString();

    return ChatColor.translateAlternateColorCodes('&', message
        .replace(">>", "\u00BB")
        .replace("<<", "\u00AB"));
  }
}