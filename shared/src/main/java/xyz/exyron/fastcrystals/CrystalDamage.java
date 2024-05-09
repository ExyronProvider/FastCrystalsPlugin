package xyz.exyron.fastcrystals;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public interface CrystalDamage {

  void handle(Player damager, Entity crystal);

}
