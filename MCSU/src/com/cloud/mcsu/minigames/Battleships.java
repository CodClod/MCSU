package com.cloud.mcsu.minigames;

import org.bukkit.Material;
import org.bukkit.block.Banner;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;

public class Battleships implements CommandExecutor {

    public static Player player;

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can use that command!");
            return true;
        }
        player = (Player) sender;
        if (cmd.getName().equalsIgnoreCase("battleships") && player.isOp()) {
            Boat boat = (Boat) player.getWorld().spawn(player.getLocation(),Boat.class);
            ArmorStand stand = (ArmorStand) player.getWorld().spawnEntity(player.getLocation(), EntityType.ARMOR_STAND);
            stand.setInvisible(true);
            ItemStack banner = new ItemStack(Material.BLUE_BANNER,1);
            stand.setHelmet(banner);
            boat.addPassenger(stand);
        }
        return true;
    }

}
