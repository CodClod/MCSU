package com.cloud.mcsu.minigames;

import com.cloud.mcsu.MCSU;
import com.cloud.mcsu.announcements.Announcement;
import com.cloud.mcsu.config.Config;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scoreboard.Team;

import java.util.ArrayList;
import java.util.HashMap;

public class Sumo implements CommandExecutor, Listener {

    public static Player player;
    public static World world;
    public static int time;
    public static int taskID;
    public static MCSU mcsu = MCSU.getPlugin(MCSU.class);
    public static boolean sumoStarted;
    public static ArrayList<Player> deadplayers = new ArrayList<Player>();
    public static ArrayList<String> deadteams = new ArrayList<String>();
    public static ArrayList<String> onlineteams = new ArrayList<String>();
    public static HashMap<String, Integer> onlineteamplayers = new HashMap<String, Integer>();
    public static String team1;
    public static String team2;

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can use that command!");
            return true;
        }
        player = (Player) sender;
        if (cmd.getName().equalsIgnoreCase("sumo") && player.isOp()) {
            if(args.length >= 2) {
                team1 = args[0];
                team2 = args[1];
            }
            sumoCommand(player);
        }
        if(cmd.getName().equalsIgnoreCase("stopsumo") && player.isOp()) {
            stopSumo();
        }
        return true;
    }

    public static void sumoCommand(Player p) {
        world = player.getWorld();
        player = p;
        if(Bukkit.getScheduler().isCurrentlyRunning(taskID)) {
            stopTimer();
        }
        sumoStarted = false;
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

    public static void stopSumo() {
        for (Player players : Bukkit.getOnlinePlayers()) {
            players.setInvulnerable(false);
        }
        world.setPVP(true);
        stopTimer();
        sumoStarted = false;
        deadteams = null;
        deadplayers = null;
        onlineteams = null;
    }

    public static void setTimer(int amount) {
        time = amount;
    }

    public static void startTimer() {
        BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
        taskID = scheduler.scheduleSyncRepeatingTask((Plugin) mcsu, new Runnable() {
            @Override
            public void run() {
                if(time == 0) {
                    if(!sumoStarted) {
                        spleefStart();
                        setTimer(150);
                        startTimer();
                    }
                    return;
                }
                if(time % 1 == 0 && !sumoStarted && time <= 10) {
                    for (Player players : Bukkit.getOnlinePlayers()) {
                        players.sendTitle(""+time, "", 1, 20, 1);
                        players.playSound(players.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL,1,1);
                    }
                }
                if(time == 15 && !sumoStarted) {
                    world.setPVP(false);
                    MCSU.currentgame = "Finale - Sumo";
                    MCSU.gameround = 1;
                    for (Player players : Bukkit.getOnlinePlayers()) {
                        players.sendTitle("Sumo starting in "+time+" seconds!", "", 1, 20, 1);
                        players.sendMessage(ChatColor.AQUA+"Sumo Starting in "+time+" seconds!");
                        players.teleport(new Location(world,2466.5,14,-50));
                        players.playSound(players.getLocation(), Sound.BLOCK_BEACON_ACTIVATE,1,1);
                        MCSU.createBoard(players);
                    }
                    for (Player players : Bukkit.getOnlinePlayers()) {
                        world.setPVP(false);
                        players.setGameMode(GameMode.ADVENTURE);
                        players.getInventory().clear();
                        players.setFoodLevel(20);
                        players.setHealth(20);
                        players.setFireTicks(1);
                        players.setLevel(0);
                        players.setExp(0);
                        for (PotionEffect effect : players.getActivePotionEffects()) {
                            players.removePotionEffect(effect.getType());
                        }
                    }
                    String[] msgs = {
                            ChatColor.GREEN+"Finale - Sumo",
                            "",
                            ChatColor.WHITE+"Hit the opposition of the map and be the last one standing!",
                            ChatColor.WHITE+"Good Luck and Have Fun!"
                    };
                    new Announcement(msgs);
                }
                time = time - 1;
            }
        }, 0L, 20L);
    }

    public static void stopTimer() {
        Bukkit.getScheduler().cancelTask(taskID);
    }

    public static void spleefStart() {
        world.setPVP(true);
        world.setTime(18000);
        stopTimer();
        sumoStarted = true;
        for (Player players : Bukkit.getOnlinePlayers()) {
            players.setInvulnerable(false);
            players.sendTitle("Go!", "", 1, 20, 1);
            players.playSound(players.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL,1,5);
            players.setGameMode(GameMode.ADVENTURE);
            players.getInventory().clear();
            players.setFoodLevel(20);
            players.setHealth(20);
            players.setFireTicks(1);
            players.setLevel(0);
            players.setExp(0);
            for (PotionEffect effect : players.getActivePotionEffects()) {
                players.removePotionEffect(effect.getType());
            }
            Team team = players.getScoreboard().getPlayerTeam(players);
            String teamname = team.getName();
            Location team1loc = new Location(world,2466.5,14,-66.5,-180,0);
            Location team2loc = new Location(world,2466.5,14,-78.5,0,0);
            if(teamname.equals(team1)) {
                players.teleport(team1loc);
            } else if(teamname.equals(team2)) {
                players.teleport(team2loc);
            } else {
                players.teleport(new Location(world, 2466.5, 14, -50));
            }
        }
    }

    @EventHandler
    public static void onDeath(PlayerDeathEvent e) {
        if(sumoStarted) {
            Player deadplayer = e.getEntity();
            Player killer = e.getEntity().getKiller();
            Team team = deadplayer.getScoreboard().getPlayerTeam(deadplayer);
            String teamname = team.getName();
            int i = 0;
            deadplayers.add(deadplayer);
            onlineteamplayers.put(teamname,0);
            for(String players : team.getEntries()) {
                if(Bukkit.getOnlinePlayers().contains(Bukkit.getPlayer(players))) {
                    onlineteamplayers.put(teamname,onlineteamplayers.get(teamname)+1);
                }
                if(deadplayers.contains(Bukkit.getServer().getPlayer(players))) {
                    i++;
                }
            }
            for(Player players : Bukkit.getOnlinePlayers()) {
                if(!onlineteams.contains(players.getScoreboard().getPlayerTeam(players).getName())) {
                    onlineteams.add(players.getScoreboard().getPlayerTeam(players).getName());
                }
            }
            if(i == onlineteamplayers.get(teamname)) {
                deadteams.add(teamname);
                for(Player players : Bukkit.getOnlinePlayers()) {
                    players.sendMessage(players.getScoreboard().getTeam(teamname).getColor()+teamname+ChatColor.WHITE+" has been eliminated.");
                }
                if (teamname.equalsIgnoreCase(team2)) {
                    for(Player players : Bukkit.getOnlinePlayers()) {
                        players.sendTitle(deadplayer.getScoreboard().getTeam(team1).getColor()+deadplayer.getScoreboard().getTeam(team1).getDisplayName()+" win MCSU!","");
                    }
                    stopSumo();
                } else if (teamname.equalsIgnoreCase(team1)) {
                    for(Player players : Bukkit.getOnlinePlayers()) {
                        players.sendTitle(deadplayer.getScoreboard().getTeam(team2).getColor()+deadplayer.getScoreboard().getTeam(team2).getDisplayName() + " win MCSU!", "");
                    }
                    stopSumo();
                }
            }
        }
    }

    @EventHandler
    public static void onTakeHunger(FoodLevelChangeEvent e) {
        if(sumoStarted) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public static void onPlayerDmg(EntityDamageByEntityEvent e) {
        if(sumoStarted) {
            if(e.getCause() == EntityDamageEvent.DamageCause.ENTITY_ATTACK) {
                e.setDamage(0);
            }
        }
    }

}
