package com.cloud.mcsu.gui;

import com.cloud.mcsu.minigames.*;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GUI implements InventoryHolder, Listener {

    private Inventory inv;


    @EventHandler
    public void onInventoryClick(InventoryClickEvent e){
        Player player = (Player) e.getWhoClicked();
        ItemStack clicked = e.getCurrentItem();
        Inventory inventory = e.getInventory();

        if(inventory.getHolder() instanceof GUI){
            if(clicked.getType() != null || clicked.getType() != Material.AIR){
                e.setCancelled(true);
                player.closeInventory();

                switch(clicked.getType()){
                    case DIAMOND_SWORD:
                        SG.sgCommand(player);
                    case SADDLE:
                        HorseRace.horseraceCommand(player);
                    case SANDSTONE:
                        Skybattle.skybattleCommand(player);
                    case SLIME_BALL:
                        Slimekour.slimekourCommand(player);
                    case DARK_OAK_TRAPDOOR:
                        HideAndSeek.hideandseekCommand(player);
                    case STICK:
                        GangBeasts.gangbeastsCommand(player);
                }
            }
        }
    }

    public GUI() {
        inv = Bukkit.createInventory(this, 9, "§b§lGame Selector");
        init();
    }

    private void init() {
        ItemStack item;
        /*
        for(int i = 0; i < 4; i++) {

        }
         */
        item = createItem("§b§lSurvival Games",Material.DIAMOND_SWORD, Collections.singletonList("§7Starts a game of SG."));
        inv.setItem(0, item);
        item = createItem("§6§lHorse Race Course",Material.SADDLE, Collections.singletonList("§7Starts the horse race course."));
        inv.setItem(1,item);
        item = createItem("§e§lSkybattle",Material.SANDSTONE, Collections.singletonList("§7Starts a game of Skybattle."));
        inv.setItem(2,item);
        item = createItem("§a§lSlimekour",Material.SLIME_BALL, Collections.singletonList("§7Starts slimekour."));
        inv.setItem(3, item);
        item = createItem("§8§lHide and Seek",Material.DARK_OAK_TRAPDOOR, Collections.singletonList("§7Starts the hide and seek."));
        inv.setItem(4,item);
        item = createItem("§5§lGang Beasts",Material.STICK, Collections.singletonList("§7Starts a game of Gang Beasts."));
        inv.setItem(5,item);
        item = createItem("§1§lCapture the Flag",Material.BLUE_BANNER, Collections.singletonList("§7Starts capture the flag."));
        inv.setItem(6,item);
    }

    private ItemStack createItem(String name, Material mat, List<String> lore) {
        ItemStack item = new ItemStack(mat, 1);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }

    @Override
    public Inventory getInventory() {
        return inv;
    }
}
