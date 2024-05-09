package xyz.exyron.fastcrystals;

import net.minecraft.network.protocol.game.PacketPlayOutEntityDestroy;
import org.bukkit.craftbukkit.v1_18_R2.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class CrystalDamage_v1_18 implements CrystalDamage {

  @Override
  public void handle(Player damager, Entity crystal) {
    ((CraftPlayer) damager).getHandle().b.a(new PacketPlayOutEntityDestroy(crystal.getEntityId()));
  }
}
