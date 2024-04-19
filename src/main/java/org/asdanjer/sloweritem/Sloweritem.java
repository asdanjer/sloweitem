package org.asdanjer.sloweritem;

import org.bukkit.block.Sign;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public final class Sloweritem extends JavaPlugin implements Listener {
    double velmax = 0.15;
    String signText = "Deutsche Bahn";
    Material trigerblock = Material.BAMBOO_WALL_SIGN;
    List<Entity> dispensedItems = new ArrayList<>();

    @Override
    public void onEnable() {
        saveDefaultConfig();
        velmax = getConfig().getDouble("velmax");
        signText = getConfig().getString("signText");
        trigerblock = Material.getMaterial(getConfig().getString("signType") + "_WALL_SIGN");
        getServer().getPluginManager().registerEvents(this, this);
        getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
            @Override
            public void run() {
                Iterator<Entity> iterator = dispensedItems.iterator();
                while (iterator.hasNext()) {
                    Entity entity = iterator.next();
                    if (!entity.isValid()) {
                        iterator.remove();
                        continue;
                    }
                    double vel = entity.getVelocity().length();
                    Vector direction = entity.getVelocity().normalize();
                    if(vel>velmax) entity.setVelocity(direction.multiply(velmax));

                }
            }
        }, 0L, 1L);

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    @EventHandler
    public void onItemSpawn(ItemSpawnEvent event) {
        Block block = event.getLocation().getBlock();
        if (block.getType() == trigerblock) {
            Sign sign = (Sign) block.getState();
            if (sign.getLines()[0].equals(signText)){
                dispensedItems.add(event.getEntity());
            }
        }
    }
}