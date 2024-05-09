package xyz.exyron.fastcrystals;

import net.minecraft.server.v1_16_R3.PacketPlayOutEntityDestroy;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class CrystalDamage_v1_16 implements CrystalDamage {

  @Override
  public void handle(Player damager, Entity crystal) {
    ((CraftPlayer) damager).getHandle().playerConnection.sendPacket(new PacketPlayOutEntityDestroy(crystal.getEntityId()));
  }
}
