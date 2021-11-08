package com.cloud.mcsu.minigames;

import com.cloud.mcsu.MCSU;
import com.cloud.mcsu.announcements.Announcement;
import org.bukkit.*;
import org.bukkit.block.Banner;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scoreboard.Team;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class Battleships implements CommandExecutor, Listener {

    public static Player player;
    public static World world;
    public static int time;
    public static int taskID;
    public static boolean battleshipsStarted;
    public static MCSU mcsu = MCSU.getPlugin(MCSU.class);
    public static String team1;
    public static String team2;
    public static ArrayList<Player> deadplayers = new ArrayList<Player>();
    public static ArrayList<String> deadteams = new ArrayList<String>();
    public static ArrayList<String> onlineteams = new ArrayList<String>();
    public static HashMap<String, Integer> onlineteamplayers = new HashMap<String, Integer>();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can use that command!");
            return true;
        }
        player = (Player) sender;
        if (cmd.getName().equalsIgnoreCase("battleships") && player.isOp()) {
            battleshipsStarted = false;
            if(args.length >= 2) {
                team1 = args[0];
                team2 = args[1];
                world = player.getWorld();
            }
            if(Bukkit.getScheduler().isCurrentlyRunning(taskID)) {
                stopTimer();
            }
            setTimer(15);
            startTimer();
            deadplayers = null;
            deadplayers = new ArrayList<Player>();
            deadteams = null;
            deadteams = new ArrayList<String>();
            onlineteams = null;
            onlineteams = new ArrayList<String>();
            onlineteamplayers = null;
            onlineteamplayers = new HashMap<String, Integer>();
            onlineteamplayers.put("Blue",0);
            onlineteamplayers.put("Red",0);
            onlineteamplayers.put("Yellow",0);
            onlineteamplayers.put("Green",0);
            onlineteamplayers.put("Aqua",0);
            onlineteamplayers.put("Pink",0);
            onlineteamplayers.put("Grey",0);
            onlineteamplayers.put("White",0);
        }
        if (cmd.getName().equalsIgnoreCase("stopbattleships") && player.isOp()) {
            battleshipsStop();
        }
        return true;
    }

    public static void battleshipsStop() {
        battleshipsStarted = false;
        stopTimer();
        world.setPVP(true);
        deadplayers = null;
        deadteams = null;
        onlineteams = null;
        onlineteamplayers = null;
    }

    public static void stopTimer() {
        Bukkit.getScheduler().cancelTask(taskID);
    }

    public static void setTimer(int amount) {
        time = amount;
    }

    public static void startTimer() {
        BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
        taskID = scheduler.scheduleSyncRepeatingTask((Plugin) mcsu, new Runnable() {
            @Override
            public void run() {
                if (time == 0) {
                    if (!battleshipsStarted) {
                        battleshipsStarted = true;
                        battleshipsStart();
                    }
                    return;
                }
                if (time % 1 == 0 && !battleshipsStarted && time <= 10) {
                    for (Player players : Bukkit.getOnlinePlayers()) {
                        players.sendTitle("" + time, "", 1, 20, 1);
                        players.playSound(players.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 1, 1);
                    }
                }
                if (time == 15 && !battleshipsStarted) {
                    world.setPVP(false);
                    MCSU.currentgame = "Battleships";
                    MCSU.gameround = 0;
                    for (Player players : Bukkit.getOnlinePlayers()) {
                        players.sendTitle("Battleships starting in " + time + " seconds!", "", 1, 20, 1);
                        players.sendMessage(ChatColor.AQUA + "Battleships Starting in " + time + " seconds!");
                        players.teleport(new Location(world, -955.5, 102, -1914.5));
                        players.playSound(players.getLocation(), Sound.ENTITY_BOAT_PADDLE_WATER, 1, 1);
                        MCSU.createBoard(players);
                    }
                    for (Player p : Bukkit.getOnlinePlayers()) {
                        p.setGameMode(GameMode.ADVENTURE);
                        p.getInventory().clear();
                        p.setFoodLevel(20);
                        p.setHealth(20);
                        p.setFireTicks(1);
                        p.setLevel(0);
                        p.setExp(0);
                        for (PotionEffect effect : player.getActivePotionEffects()) {
                            player.removePotionEffect(effect.getType());
                        }
                    }
                    for(OfflinePlayer p : player.getScoreboard().getTeam(team1).getPlayers()) {
                        if(p.isOnline()) {
                            Player pl = p.getPlayer();
                            pl.teleport(new Location(world, -916.5, 60, -444.5));
                        }
                    }
                    for(OfflinePlayer p : player.getScoreboard().getTeam(team2).getPlayers()) {
                        if(p.isOnline()) {
                            Player pl = p.getPlayer();
                            pl.teleport(new Location(world, -916.5, 60, -482.5));
                        }
                    }
                    String[] msgs = {
                            ChatColor.RED+"Battleships",
                            "",
                            ChatColor.WHITE+"Fight to the death using your weapons and boat around your enemies.",
                            ChatColor.WHITE+"Kill your opponent to win MCSU!",
                            ChatColor.WHITE+"Good Luck and Have Fun!"
                    };
                    new Announcement(msgs);
                }
                time = time - 1;
            }
        }, 0L, 20L);
    }

    @EventHandler
    public static void onDeath(PlayerDeathEvent e) {
        if(battleshipsStarted) {
            Player deadplayer = e.getEntity();
            Team team = deadplayer.getScoreboard().getPlayerTeam(deadplayer);
            String teamname = team.getName();
            if (teamname.equalsIgnoreCase(team2)) {
                for(Player players : Bukkit.getOnlinePlayers()) {
                    players.sendTitle(deadplayer.getScoreboard().getTeam(team1).getColor()+deadplayer.getScoreboard().getTeam(team1).getDisplayName()+" win MCSU!","");
                }
                battleshipsStop();
            } else if (teamname.equalsIgnoreCase(team1)) {
                for(Player players : Bukkit.getOnlinePlayers()) {
                    players.sendTitle(deadplayer.getScoreboard().getTeam(team2).getColor()+deadplayer.getScoreboard().getTeam(team2).getDisplayName() + " win MCSU!", "");
                }
                battleshipsStop();
            }
        }
    }

    public static void battleshipsStart() {
        for(OfflinePlayer p : player.getScoreboard().getTeam(team1).getPlayers()) {
            Player pl = p.getPlayer();
            pl.teleport(new Location(world,-916.5, 60 ,-444.5));
            Boat boat = (Boat) world.spawn(pl.getLocation(),Boat.class);
            boat.addPassenger(pl);
            if(pl.getScoreboard().getPlayerTeam(pl).getName().equalsIgnoreCase("Blue")) {
                ItemStack chestplate = new ItemStack(Material.LEATHER_CHESTPLATE,1);
                LeatherArmorMeta meta = (LeatherArmorMeta) chestplate.getItemMeta();
                meta.setColor(Color.BLUE);
                pl.getInventory().addItem(new ItemStack(Material.STONE_SWORD,1));
                pl.getInventory().setItemInOffHand(new ItemStack(Material.CROSSBOW,1));
                pl.getInventory().addItem(new ItemStack(Material.BOW,1));
                pl.getInventory().addItem(new ItemStack(Material.ARROW,10));
                chestplate.setItemMeta(meta);
                pl.getInventory().setChestplate(chestplate);
            } else if(pl.getScoreboard().getPlayerTeam(pl).getName().equalsIgnoreCase("Red")) {
                ItemStack chestplate = new ItemStack(Material.LEATHER_CHESTPLATE,1);
                LeatherArmorMeta meta = (LeatherArmorMeta) chestplate.getItemMeta();
                meta.setColor(Color.RED);
                pl.getInventory().addItem(new ItemStack(Material.STONE_SWORD,1));
                pl.getInventory().setItemInOffHand(new ItemStack(Material.CROSSBOW,1));
                pl.getInventory().addItem(new ItemStack(Material.BOW,1));
                pl.getInventory().addItem(new ItemStack(Material.ARROW,10));
                chestplate.setItemMeta(meta);
                pl.getInventory().setChestplate(chestplate);
            } else if(pl.getScoreboard().getPlayerTeam(pl).getName().equalsIgnoreCase("Green")) {
                ItemStack chestplate = new ItemStack(Material.LEATHER_CHESTPLATE,1);
                LeatherArmorMeta meta = (LeatherArmorMeta) chestplate.getItemMeta();
                meta.setColor(Color.GREEN);
                pl.getInventory().addItem(new ItemStack(Material.STONE_SWORD,1));
                pl.getInventory().setItemInOffHand(new ItemStack(Material.CROSSBOW,1));
                pl.getInventory().addItem(new ItemStack(Material.BOW,1));
                pl.getInventory().addItem(new ItemStack(Material.ARROW,10));
                chestplate.setItemMeta(meta);
                pl.getInventory().setChestplate(chestplate);
            } else if(pl.getScoreboard().getPlayerTeam(pl).getName().equalsIgnoreCase("Yellow")) {
                ItemStack chestplate = new ItemStack(Material.LEATHER_CHESTPLATE,1);
                LeatherArmorMeta meta = (LeatherArmorMeta) chestplate.getItemMeta();
                meta.setColor(Color.YELLOW);
                pl.getInventory().addItem(new ItemStack(Material.STONE_SWORD,1));
                pl.getInventory().setItemInOffHand(new ItemStack(Material.CROSSBOW,1));
                pl.getInventory().addItem(new ItemStack(Material.BOW,1));
                pl.getInventory().addItem(new ItemStack(Material.ARROW,10));
                chestplate.setItemMeta(meta);
                pl.getInventory().setChestplate(chestplate);
            } else if(pl.getScoreboard().getPlayerTeam(pl).getName().equalsIgnoreCase("Aqua")) {
                ItemStack chestplate = new ItemStack(Material.LEATHER_CHESTPLATE,1);
                LeatherArmorMeta meta = (LeatherArmorMeta) chestplate.getItemMeta();
                meta.setColor(Color.AQUA);
                pl.getInventory().addItem(new ItemStack(Material.STONE_SWORD,1));
                pl.getInventory().setItemInOffHand(new ItemStack(Material.CROSSBOW,1));
                pl.getInventory().addItem(new ItemStack(Material.BOW,1));
                pl.getInventory().addItem(new ItemStack(Material.ARROW,10));
                chestplate.setItemMeta(meta);
                pl.getInventory().setChestplate(chestplate);
            } else if(pl.getScoreboard().getPlayerTeam(pl).getName().equalsIgnoreCase("Pink")) {
                ItemStack chestplate = new ItemStack(Material.LEATHER_CHESTPLATE,1);
                LeatherArmorMeta meta = (LeatherArmorMeta) chestplate.getItemMeta();
                meta.setColor(Color.PURPLE);
                pl.getInventory().addItem(new ItemStack(Material.STONE_SWORD,1));
                pl.getInventory().setItemInOffHand(new ItemStack(Material.CROSSBOW,1));
                pl.getInventory().addItem(new ItemStack(Material.BOW,1));
                pl.getInventory().addItem(new ItemStack(Material.ARROW,10));
                chestplate.setItemMeta(meta);
                pl.getInventory().setChestplate(chestplate);
            } else if(pl.getScoreboard().getPlayerTeam(pl).getName().equalsIgnoreCase("White")) {
                ItemStack chestplate = new ItemStack(Material.LEATHER_CHESTPLATE,1);
                LeatherArmorMeta meta = (LeatherArmorMeta) chestplate.getItemMeta();
                meta.setColor(Color.WHITE);
                pl.getInventory().addItem(new ItemStack(Material.STONE_SWORD,1));
                pl.getInventory().setItemInOffHand(new ItemStack(Material.CROSSBOW,1));
                pl.getInventory().addItem(new ItemStack(Material.BOW,1));
                pl.getInventory().addItem(new ItemStack(Material.ARROW,10));
                chestplate.setItemMeta(meta);
                pl.getInventory().setChestplate(chestplate);
            } else if(pl.getScoreboard().getPlayerTeam(pl).getName().equalsIgnoreCase("Grey")) {
                ItemStack chestplate = new ItemStack(Material.LEATHER_CHESTPLATE,1);
                LeatherArmorMeta meta = (LeatherArmorMeta) chestplate.getItemMeta();
                meta.setColor(Color.GRAY);
                pl.getInventory().addItem(new ItemStack(Material.STONE_SWORD,1));
                pl.getInventory().setItemInOffHand(new ItemStack(Material.CROSSBOW,1));
                pl.getInventory().addItem(new ItemStack(Material.BOW,1));
                pl.getInventory().addItem(new ItemStack(Material.ARROW,10));
                chestplate.setItemMeta(meta);
                pl.getInventory().setChestplate(chestplate);
            }
        }
        for(OfflinePlayer p : player.getScoreboard().getTeam(team2).getPlayers()) {
            Player pl = p.getPlayer();
            pl.teleport(new Location(world,-916.5, 60 ,-482.5));
            Boat boat = (Boat) world.spawn(pl.getLocation(),Boat.class);
            boat.addPassenger(pl);
            if(pl.getScoreboard().getPlayerTeam(pl).getName().equalsIgnoreCase("Blue")) {
                ItemStack chestplate = new ItemStack(Material.LEATHER_CHESTPLATE,1);
                LeatherArmorMeta meta = (LeatherArmorMeta) chestplate.getItemMeta();
                meta.setColor(Color.BLUE);
                pl.getInventory().addItem(new ItemStack(Material.STONE_SWORD,1));
                pl.getInventory().setItemInOffHand(new ItemStack(Material.CROSSBOW,1));
                pl.getInventory().addItem(new ItemStack(Material.BOW,1));
                pl.getInventory().addItem(new ItemStack(Material.ARROW,10));
                chestplate.setItemMeta(meta);
                pl.getInventory().setChestplate(chestplate);
            } else if(pl.getScoreboard().getPlayerTeam(pl).getName().equalsIgnoreCase("Red")) {
                ItemStack chestplate = new ItemStack(Material.LEATHER_CHESTPLATE,1);
                LeatherArmorMeta meta = (LeatherArmorMeta) chestplate.getItemMeta();
                meta.setColor(Color.RED);
                pl.getInventory().addItem(new ItemStack(Material.STONE_SWORD,1));
                pl.getInventory().setItemInOffHand(new ItemStack(Material.CROSSBOW,1));
                pl.getInventory().addItem(new ItemStack(Material.BOW,1));
                pl.getInventory().addItem(new ItemStack(Material.ARROW,10));
                chestplate.setItemMeta(meta);
                pl.getInventory().setChestplate(chestplate);
            } else if(pl.getScoreboard().getPlayerTeam(pl).getName().equalsIgnoreCase("Green")) {
                ItemStack chestplate = new ItemStack(Material.LEATHER_CHESTPLATE,1);
                LeatherArmorMeta meta = (LeatherArmorMeta) chestplate.getItemMeta();
                meta.setColor(Color.GREEN);
                pl.getInventory().addItem(new ItemStack(Material.STONE_SWORD,1));
                pl.getInventory().setItemInOffHand(new ItemStack(Material.CROSSBOW,1));
                pl.getInventory().addItem(new ItemStack(Material.BOW,1));
                pl.getInventory().addItem(new ItemStack(Material.ARROW,10));
                chestplate.setItemMeta(meta);
                pl.getInventory().setChestplate(chestplate);
            } else if(pl.getScoreboard().getPlayerTeam(pl).getName().equalsIgnoreCase("Yellow")) {
                ItemStack chestplate = new ItemStack(Material.LEATHER_CHESTPLATE,1);
                LeatherArmorMeta meta = (LeatherArmorMeta) chestplate.getItemMeta();
                meta.setColor(Color.YELLOW);
                pl.getInventory().addItem(new ItemStack(Material.STONE_SWORD,1));
                pl.getInventory().setItemInOffHand(new ItemStack(Material.CROSSBOW,1));
                pl.getInventory().addItem(new ItemStack(Material.BOW,1));
                pl.getInventory().addItem(new ItemStack(Material.ARROW,10));
                chestplate.setItemMeta(meta);
                pl.getInventory().setChestplate(chestplate);
            } else if(pl.getScoreboard().getPlayerTeam(pl).getName().equalsIgnoreCase("Aqua")) {
                ItemStack chestplate = new ItemStack(Material.LEATHER_CHESTPLATE,1);
                LeatherArmorMeta meta = (LeatherArmorMeta) chestplate.getItemMeta();
                meta.setColor(Color.AQUA);
                pl.getInventory().addItem(new ItemStack(Material.STONE_SWORD,1));
                pl.getInventory().setItemInOffHand(new ItemStack(Material.CROSSBOW,1));
                pl.getInventory().addItem(new ItemStack(Material.BOW,1));
                pl.getInventory().addItem(new ItemStack(Material.ARROW,10));
                chestplate.setItemMeta(meta);
                pl.getInventory().setChestplate(chestplate);
            } else if(pl.getScoreboard().getPlayerTeam(pl).getName().equalsIgnoreCase("Pink")) {
                ItemStack chestplate = new ItemStack(Material.LEATHER_CHESTPLATE,1);
                LeatherArmorMeta meta = (LeatherArmorMeta) chestplate.getItemMeta();
                meta.setColor(Color.PURPLE);
                pl.getInventory().addItem(new ItemStack(Material.STONE_SWORD,1));
                pl.getInventory().setItemInOffHand(new ItemStack(Material.CROSSBOW,1));
                pl.getInventory().addItem(new ItemStack(Material.BOW,1));
                pl.getInventory().addItem(new ItemStack(Material.ARROW,10));
                chestplate.setItemMeta(meta);
                pl.getInventory().setChestplate(chestplate);
            } else if(pl.getScoreboard().getPlayerTeam(pl).getName().equalsIgnoreCase("White")) {
                ItemStack chestplate = new ItemStack(Material.LEATHER_CHESTPLATE,1);
                LeatherArmorMeta meta = (LeatherArmorMeta) chestplate.getItemMeta();
                meta.setColor(Color.WHITE);
                pl.getInventory().addItem(new ItemStack(Material.STONE_SWORD,1));
                pl.getInventory().setItemInOffHand(new ItemStack(Material.CROSSBOW,1));
                pl.getInventory().addItem(new ItemStack(Material.BOW,1));
                pl.getInventory().addItem(new ItemStack(Material.ARROW,10));
                chestplate.setItemMeta(meta);
                pl.getInventory().setChestplate(chestplate);
            } else if(pl.getScoreboard().getPlayerTeam(pl).getName().equalsIgnoreCase("Grey")) {
                ItemStack chestplate = new ItemStack(Material.LEATHER_CHESTPLATE,1);
                LeatherArmorMeta meta = (LeatherArmorMeta) chestplate.getItemMeta();
                meta.setColor(Color.GRAY);
                pl.getInventory().addItem(new ItemStack(Material.STONE_SWORD,1));
                pl.getInventory().setItemInOffHand(new ItemStack(Material.CROSSBOW,1));
                pl.getInventory().addItem(new ItemStack(Material.BOW,1));
                pl.getInventory().addItem(new ItemStack(Material.ARROW,10));
                chestplate.setItemMeta(meta);
                pl.getInventory().setChestplate(chestplate);
            }
        }
    }

}
