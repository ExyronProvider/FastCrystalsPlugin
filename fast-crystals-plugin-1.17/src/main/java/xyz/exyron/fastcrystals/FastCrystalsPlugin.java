package xyz.exyron.fastcrystals;

import net.md_5.bungee.api.ChatColor;
import net.minecraft.network.protocol.game.PacketPlayOutEntityDestroy;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftPlayer;
import org.bukkit.entity.EnderCrystal;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class FastCrystalsPlugin extends JavaPlugin implements Listener {
    private final Set<UUID> playersUUID = new HashSet<>();

    @Override
    public void onEnable() {
        this.saveDefaultConfig();

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

        this.getServer().getPluginManager().registerEvents(this, this);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void event(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player player && event.getEntity() instanceof EnderCrystal && this.playersUUID.contains(player.getUniqueId()))
            ((CraftPlayer) player).getHandle().b.sendPacket(new PacketPlayOutEntityDestroy(event.getEntity().getEntityId()));
    }

    private final Pattern HEX_PATTERN = Pattern.compile("#[A-Fa-f0-9]{6}");

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