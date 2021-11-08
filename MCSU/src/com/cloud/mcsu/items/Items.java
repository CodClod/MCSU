package com.cloud.mcsu.items;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.List;

public class Items {

    public static ItemStack trophy;
    public static ItemStack beesteeringwheel;
    public static ItemStack taco;
    public static ItemStack spaceboots;
    public static ItemStack kbstick;

    public static void init() {
        createTrophy();
        createBeesteeringwheel();
        createTaco(1);
        createSpaceBoots();
        createKbStick();
    }

    private static void createKbStick() {
        ItemStack item = new ItemStack(Material.STICK, 1);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("§5Annoying Stick");
        meta.addEnchant(Enchantment.KNOCKBACK,1,true);
        List<String> lore = new ArrayList<>();
        lore.add("§7Use this stick to annoy enemies or to boost teammates");
        lore.add("§415 Second cooldown on this stick");
        meta.setLore(lore);
        item.setItemMeta(meta);
        kbstick = item;
    }

    private static void createTrophy() {
        ItemStack item = new ItemStack(Material.DIAMOND, 1);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("§6Trophy");
        List<String> lore = new ArrayList<>();
        lore.add("§b#Victory Royale");
        meta.setLore(lore);
        item.setItemMeta(meta);
        trophy = item;
    }

    private static void createBeesteeringwheel() {
        ItemStack item = new ItemStack(Material.GLOWSTONE_DUST, 1);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("§eBee Steering Wheel");
        List<String> lore = new ArrayList<>();
        lore.add("§fControl your bee with this steering wheel.");
        meta.setLore(lore);
        item.setItemMeta(meta);
        beesteeringwheel = item;
    }

    public static void createTaco(int amount) {
        ItemStack item = new ItemStack(Material.PLAYER_HEAD, amount, (short) SkullType.PLAYER.ordinal());
        SkullMeta skullmeta = (SkullMeta) item.getItemMeta();
        skullmeta.setOwner("Crunchy_Taco34");
        item.setItemMeta(skullmeta);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("§e§lTaco");
        List<String> lore = new ArrayList<>();
        lore.add("§7Yum.");
        meta.setLore(lore);
        item.setItemMeta(meta);
        taco = item;
    }

    private static void createSpaceBoots() {
        ItemStack item = new ItemStack(Material.LEATHER_BOOTS,1);
        LeatherArmorMeta meta = (LeatherArmorMeta) item.getItemMeta();
        meta.setColor(Color.WHITE);
        meta.setDisplayName("§fNasa Space Boots");
        List<String> lore = new ArrayList<>();
        lore.add("§7Fly to the moon with these space boots!");
        meta.setUnbreakable(true);
        meta.setLore(lore);
        item.setItemMeta(meta);
        spaceboots = item;
    }
}
