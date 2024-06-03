package xyz.exyron.fastcrystals;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FastCrystalsPlaceholder extends PlaceholderExpansion {

  private final String statusTrue, statusFalse;
  private final FastCrystalsPlugin plugin;

  public FastCrystalsPlaceholder(FastCrystalsPlugin fastCrystalsPlugin) {
    this.plugin = fastCrystalsPlugin;
    this.statusFalse = this.plugin.getConfiguration().getString("placeholderStatusFalse");
    this.statusTrue = this.plugin.getConfiguration().getString("placeholderStatusTrue");
  }

  @Override
  public @NotNull String getIdentifier() {
    return "fastcrystals";
  }

  @Override
  public @NotNull String getAuthor() {
    return "Exyron.XYZ";
  }

  @Override
  public @NotNull String getVersion() {
    return "1.0";
  }

  @Override
  public @Nullable String onRequest(OfflinePlayer player, @NotNull String params) {
    if (params.equalsIgnoreCase("status")) {
      return this.plugin.isUsingFastCrystals(player.getUniqueId()) ? this.statusTrue : this.statusFalse;
    }

    return null;
  }
}
