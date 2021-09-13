package com.cloud.mcsu.minigames;

import com.cloud.mcsu.MCSU;
import com.cloud.mcsu.worldreset.WorldManager;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scoreboard.Team;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class HideAndSeek implements CommandExecutor, Listener {

    public static int taskID;
    public static int time;
    public static int stopwatchtaskID;
    public static int stopwatchtime;
    public static int hidingtime = 30;
    public static Player player;
    public static World world;
    public static boolean hideandseekStarted;
    public static MCSU mcsu = MCSU.getPlugin(MCSU.class);
    public static int round;
    public static ArrayList<Player> tagged = new ArrayList<Player>();
    public static ArrayList<Player> hiders = new ArrayList<Player>();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can use that command!");
            return true;
        }
        player = (Player) sender;
        if (cmd.getName().equalsIgnoreCase("hideandseek")) {
            hideandseekCommand(player);
        }
        if(cmd.getName().equalsIgnoreCase("stophideandseek")) {
            stophideandseek();
        }
        if(cmd.getName().equalsIgnoreCase("timer")) {
            player.sendMessage("timer: "+stopwatchtime);
        }
        return true;
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
                    if(!hideandseekStarted) {
                        hideandseekStarted = true;
                        stopwatch();
                        stopTimer();
                        setTimer(hidingtime);
                        startTimer();
                        hideandseekStart();
                    } else {
                        Bukkit.broadcastMessage(ChatColor.DARK_RED+"Taggers Released!");
                        for(Player p : Bukkit.getOnlinePlayers()) {
                            Team team = p.getScoreboard().getPlayerTeam(p);
                            String teamname = team.getName();
                            if(round == 1) {
                                if(teamname.equals("Blue")) {
                                    p.teleport(new Location(world,-947,50,242));
                                } else if(teamname.equals("Red")) {
                                    p.teleport(new Location(world,-947,50,242));
                                } else if(teamname.equals("Green")) {
                                    p.teleport(new Location(world,-947,50,242));
                                } else if(teamname.equals("Yellow")) {
                                    p.teleport(new Location(world,-947,50,242));
                                }
                            } else if(round == 2) {
                                if(teamname.equals("Aqua")) {
                                    p.teleport(new Location(world,-947,50,242));
                                } else if(teamname.equals("Pink")) {
                                    p.teleport(new Location(world,-947,50,242));
                                } else if(teamname.equals("Grey")) {
                                    p.teleport(new Location(world,-947,50,242));
                                } else if(teamname.equals("White")) {
                                    p.teleport(new Location(world,-947,50,242));
                                }
                            }
                        }
                        stopTimer();
                        world.setPVP(true);
                    }
                    return;
                }
                if(time % 1 == 0 && !hideandseekStarted && time <= 10) {
                    for (Player players : Bukkit.getOnlinePlayers()) {
                        players.sendTitle(""+time, "", 1, 20, 1);
                        players.playSound(players.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL,1,1);
                    }
                }
                if(time == 15 && !hideandseekStarted) {
                    try {
                        WorldManager.resetHideAndSeek(player);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    MCSU.currentgame = "Hide and Seek";
                    MCSU.gameround = round;
                    world.setPVP(false);
                    for (Player players : Bukkit.getOnlinePlayers()) {
                        players.sendTitle("Hide and Seek starting in "+time+" seconds!", "", 1, 20, 1);
                        players.sendMessage(ChatColor.AQUA+"Hide and Seek Starting in "+time+" seconds!");
                        players.teleport(new Location(world,-947,50,242));
                        players.playSound(players.getLocation(), Sound.BLOCK_ENCHANTMENT_TABLE_USE,1,1);
                        MCSU.createBoard(players);
                    }
                    for(Player p: Bukkit.getOnlinePlayers()) {
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
                }
                time = time - 1;
            }
        }, 0L, 20L);
    }

    public static void stopwatch() {
        BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
        stopwatchtaskID = scheduler.scheduleSyncRepeatingTask((Plugin) mcsu, new Runnable() {
            @Override
            public void run() {
                player.sendMessage("stopwatch: "+stopwatchtime);
                stopwatchtime = stopwatchtime + 1;
                if(stopwatchtime == 270) {
                    for(Player players : Bukkit.getOnlinePlayers()) {
                        players.sendMessage(ChatColor.DARK_RED+"All taggers have been given elytras and a firework!");
                        players.sendMessage(ChatColor.RED+"Round ending in 30 seconds!");
                        Team team = players.getScoreboard().getPlayerTeam(players);
                        String teamname = team.getName();
                        if(round == 1) {
                            if(teamname.equals("Blue")) {
                                players.getInventory().setChestplate(new ItemStack(Material.ELYTRA,1));
                                players.getInventory().addItem(new ItemStack(Material.FIREWORK_ROCKET,1));
                            } else if(teamname.equals("Red")) {
                                players.getInventory().setChestplate(new ItemStack(Material.ELYTRA,1));
                                players.getInventory().addItem(new ItemStack(Material.FIREWORK_ROCKET,1));
                            } else if(teamname.equals("Green")) {
                                players.getInventory().setChestplate(new ItemStack(Material.ELYTRA,1));
                                players.getInventory().addItem(new ItemStack(Material.FIREWORK_ROCKET,1));
                            } else if(teamname.equals("Yellow")) {
                                players.getInventory().setChestplate(new ItemStack(Material.ELYTRA,1));
                                players.getInventory().addItem(new ItemStack(Material.FIREWORK_ROCKET,1));
                            }
                        } else if(round == 2) {
                            if(teamname.equals("Aqua")) {
                                players.getInventory().setChestplate(new ItemStack(Material.ELYTRA,1));
                                players.getInventory().addItem(new ItemStack(Material.FIREWORK_ROCKET,1));
                            } else if(teamname.equals("Pink")) {
                                players.getInventory().setChestplate(new ItemStack(Material.ELYTRA,1));
                                players.getInventory().addItem(new ItemStack(Material.FIREWORK_ROCKET,1));
                            } else if(teamname.equals("White")) {
                                players.getInventory().setChestplate(new ItemStack(Material.ELYTRA,1));
                                players.getInventory().addItem(new ItemStack(Material.FIREWORK_ROCKET,1));
                            } else if(teamname.equals("Grey")) {
                                players.getInventory().setChestplate(new ItemStack(Material.ELYTRA,1));
                                players.getInventory().addItem(new ItemStack(Material.FIREWORK_ROCKET,1));
                            }
                        }
                    }
                }
                if(stopwatchtime == 300 && round == 1) {
                    stopwatchtime = 0;
                    stopstopwatch();
                    stopTimer();
                    for(Player players : Bukkit.getOnlinePlayers()) {
                        players.sendMessage(ChatColor.DARK_RED+"Round 1 Over!");
                        players.setGlowing(false);
                    }
                    round = 2;
                    tagged = null;
                    hiders = null;
                    hideandseekStarted = false;
                    world = player.getWorld();
                    setTimer(15);
                    startTimer();
                }
                if(stopwatchtime == 300 && round == 2) {
                    stopstopwatch();
                    stopTimer();
                    stopwatchtime = 0;
                    for(Player players : Bukkit.getOnlinePlayers()) {
                        players.sendMessage(ChatColor.DARK_RED+"Round 2 Over!");
                        players.sendMessage(ChatColor.AQUA+"Game Over!");
                        round = 0;
                        players.setGlowing(false);
                    }
                    hideandseekStarted = false;
                    world.setPVP(true);
                    tagged = null;
                    hiders = null;
                }
            }
        }, 0L, 20L);
    }

    public static void stopstopwatch() {
        Bukkit.getScheduler().cancelTask(stopwatchtaskID);
    }

    public static void stopTimer() {
        Bukkit.getScheduler().cancelTask(taskID);
    }

    public static void hideandseekStart() {
        for (Player players : Bukkit.getOnlinePlayers()) {
            players.sendTitle("§b§lHide and Seek started", "", 1, 20, 1);
            players.teleport(new Location(world,-947,50,242));
            players.playSound(players.getLocation(), Sound.BLOCK_ENCHANTMENT_TABLE_USE,1,1);
            MCSU.createBoard(players);
        }
        Potion speed2 = new Potion(PotionType.SPEED,2,true);
        for(Player p: Bukkit.getOnlinePlayers()) {
            p.setGameMode(GameMode.ADVENTURE);
            p.getInventory().clear();
            p.setFoodLevel(20);
            p.setHealth(20);
            p.setFireTicks(1);
            p.setLevel(0);
            p.setExp(0);
            for (PotionEffect effect : p.getActivePotionEffects()) {
                p.removePotionEffect(effect.getType());
            }
            p.addPotionEffect(new PotionEffect(PotionEffectType.SATURATION,Integer.MAX_VALUE,255));
            p.addPotionEffect(new PotionEffect(PotionEffectType.WATER_BREATHING,Integer.MAX_VALUE,255));
            p.sendMessage(ChatColor.DARK_RED+"Taggers will be released in 30 seconds!");
            Team team = p.getScoreboard().getPlayerTeam(p);
            String teamname = team.getName();
            if(round == 1) {
                if(teamname.equals("Blue")) {
                    p.setGlowing(true);
                    p.sendMessage(ChatColor.RED+"You are a tagger!");
                    p.teleport(new Location(world,-957.5,137,238.5));
                    p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED,Integer.MAX_VALUE,1));
                } else if(teamname.equals("Red")) {
                    p.setGlowing(true);
                    p.sendMessage(ChatColor.RED+"You are a tagger!");
                    p.teleport(new Location(world,-957.5,137,238.5));
                    p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED,Integer.MAX_VALUE,1));
                } else if(teamname.equals("Green")) {
                    p.setGlowing(true);
                    p.sendMessage(ChatColor.RED+"You are a tagger!");
                    p.teleport(new Location(world,-957.5,137,238.5));
                    p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED,Integer.MAX_VALUE,1));
                } else if(teamname.equals("Yellow")) {
                    p.setGlowing(true);
                    p.sendMessage(ChatColor.RED+"You are a tagger!");
                    p.teleport(new Location(world,-957.5,137,238.5));
                    p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED,Integer.MAX_VALUE,1));
                } else if(teamname.equals("Aqua")) {
                    p.sendMessage(ChatColor.GREEN+"You are a hider!");
                    p.getInventory().addItem(speed2.toItemStack(1));
                    hiders.add(p);
                } else if(teamname.equals("Pink")) {
                    p.sendMessage(ChatColor.GREEN+"You are a hider!");
                    p.getInventory().addItem(speed2.toItemStack(1));
                    hiders.add(p);
                } else if(teamname.equals("Grey")) {
                    p.sendMessage(ChatColor.GREEN+"You are a hider!");
                    p.getInventory().addItem(speed2.toItemStack(1));
                    hiders.add(p);
                } else if(teamname.equals("White")) {
                    p.sendMessage(ChatColor.GREEN+"You are a hider!");
                    p.getInventory().addItem(speed2.toItemStack(1));
                    hiders.add(p);
                }
            } else if(round == 2) {
                if(teamname.equals("Aqua")) {
                    p.setGlowing(true);
                    p.sendMessage(ChatColor.RED+"You are a tagger!");
                    p.teleport(new Location(world,-957.5,137,238.5));
                    p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED,Integer.MAX_VALUE,1));
                } else if(teamname.equals("Pink")) {
                    p.setGlowing(true);
                    p.sendMessage(ChatColor.RED+"You are a tagger!");
                    p.teleport(new Location(world,-957.5,137,238.5));
                    p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED,Integer.MAX_VALUE,1));
                } else if(teamname.equals("Grey")) {
                    p.setGlowing(true);
                    p.sendMessage(ChatColor.RED+"You are a tagger!");
                    p.teleport(new Location(world,-957.5,137,238.5));
                    p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED,Integer.MAX_VALUE,1));
                } else if(teamname.equals("White")) {
                    p.setGlowing(true);
                    p.sendMessage(ChatColor.RED+"You are a tagger!");
                    p.teleport(new Location(world,-957.5,137,238.5));
                    p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED,Integer.MAX_VALUE,1));
                } else if(teamname.equals("Blue")) {
                    p.sendMessage(ChatColor.GREEN+"You are a hider!");
                    p.getInventory().addItem(speed2.toItemStack(1));
                    hiders.add(p);
                } else if(teamname.equals("Red")) {
                    p.sendMessage(ChatColor.GREEN+"You are a hider!");
                    p.getInventory().addItem(speed2.toItemStack(1));
                    hiders.add(p);
                } else if(teamname.equals("Yellow")) {
                    p.sendMessage(ChatColor.GREEN+"You are a hider!");
                    p.getInventory().addItem(speed2.toItemStack(1));
                    hiders.add(p);
                } else if(teamname.equals("Green")) {
                    p.sendMessage(ChatColor.GREEN+"You are a hider!");
                    p.getInventory().addItem(speed2.toItemStack(1));
                    hiders.add(p);
                }
            }
        }
    }

    @EventHandler
    public void onTag(EntityDamageByEntityEvent e) {
        if(e.getEntity() instanceof Player) {
            if(e.getDamager() instanceof Player) {
                Player damager = (Player) e.getDamager();
                Team team = damager.getScoreboard().getPlayerTeam(damager);
                String teamname = team.getName();
                Player damaged = (Player) e.getEntity();
                if(round == 1) {
                    if(teamname.equals("Blue")) {
                        for(Player players : Bukkit.getOnlinePlayers()) {
                            if(!tagged.contains(damaged)) {
                                players.sendMessage(ChatColor.AQUA+damaged.getName()+" was tagged");
                                damaged.setGameMode(GameMode.SPECTATOR);
                                tagged.add(damaged);
                            }
                        }
                    } else if(teamname.equals("Red")) {
                        for(Player players : Bukkit.getOnlinePlayers()) {
                            if(!tagged.contains(damaged)) {
                                players.sendMessage(ChatColor.AQUA+damaged.getName()+" was tagged");
                                damaged.setGameMode(GameMode.SPECTATOR);
                            }
                            tagged.add(damaged);
                        }
                    } else if(teamname.equals("Green")) {
                        for(Player players : Bukkit.getOnlinePlayers()) {
                            if(!tagged.contains(damaged)) {
                                players.sendMessage(ChatColor.AQUA+damaged.getName()+" was tagged");
                                damaged.setGameMode(GameMode.SPECTATOR);
                            }
                            tagged.add(damaged);
                        }
                    } else if(teamname.equals("Yellow")) {
                        for(Player players : Bukkit.getOnlinePlayers()) {
                            if(!tagged.contains(damaged)) {
                                players.sendMessage(ChatColor.AQUA+damaged.getName()+" was tagged");
                                damaged.setGameMode(GameMode.SPECTATOR);
                            }
                            tagged.add(damaged);
                        }
                    } else if(teamname.equals("Aqua")) {
                        e.setCancelled(true);
                    } else if(teamname.equals("Pink")) {
                        e.setCancelled(true);
                    } else if(teamname.equals("Grey")) {
                        e.setCancelled(true);
                    } else if(teamname.equals("White")) {
                        e.setCancelled(true);
                    }
                    if(tagged.size() == hiders.size()) {
                        for(Player players : Bukkit.getOnlinePlayers()) {
                            players.sendMessage(ChatColor.DARK_RED+"Round 1 Over!");
                            players.sendMessage(ChatColor.RED+"All hiders have been found.");
                            players.sendMessage(ChatColor.AQUA+"Round 2 starting..");
                        }
                        round = 2;
                    }
                } else if(round == 2) {
                    if(teamname.equals("Aqua")) {
                        for(Player players : Bukkit.getOnlinePlayers()) {
                            if(!tagged.contains(damaged)) {
                                players.sendMessage(ChatColor.AQUA+damaged.getName()+" was tagged");
                                damaged.setGameMode(GameMode.SPECTATOR);
                            }
                            tagged.add(damaged);
                        }
                    } else if(teamname.equals("Pink")) {
                        for(Player players : Bukkit.getOnlinePlayers()) {
                            if(!tagged.contains(damaged)) {
                                players.sendMessage(ChatColor.AQUA+damaged.getName()+" was tagged");
                                damaged.setGameMode(GameMode.SPECTATOR);
                            }
                            tagged.add(damaged);
                        }
                    } else if(teamname.equals("Grey")) {
                        for(Player players : Bukkit.getOnlinePlayers()) {
                            if(!tagged.contains(damaged)) {
                                players.sendMessage(ChatColor.AQUA+damaged.getName()+" was tagged");
                                damaged.setGameMode(GameMode.SPECTATOR);
                            }
                            tagged.add(damaged);
                        }
                    } else if(teamname.equals("White")) {
                        for(Player players : Bukkit.getOnlinePlayers()) {
                            if(!tagged.contains(damaged)) {
                                players.sendMessage(ChatColor.AQUA+damaged.getName()+" was tagged");
                                damaged.setGameMode(GameMode.SPECTATOR);
                            }
                            tagged.add(damaged);
                        }
                    } else if(teamname.equals("Blue")) {
                        e.setCancelled(true);
                    } else if(teamname.equals("Red")) {
                        e.setCancelled(true);
                    } else if(teamname.equals("Green")) {
                        e.setCancelled(true);
                    } else if(teamname.equals("Yellow")) {
                        e.setCancelled(true);
                    }
                    if(tagged.size() == hiders.size()) {
                        for(Player players : Bukkit.getOnlinePlayers()) {
                            players.sendMessage(ChatColor.DARK_RED+"Round 2 Over!");
                            players.sendMessage(ChatColor.RED+"All hiders have been found.");
                            players.sendMessage(ChatColor.AQUA+"Game Over!");
                            players.setGlowing(false);
                        }
                        hideandseekStarted = false;
                        stopTimer();
                        world.setPVP(true);
                        stopstopwatch();
                        tagged = null;
                        hiders = null;
                        round = 0;
                    }
                }
            }
        }
    }

    public static void hideandseekCommand(Player p) {
        stopstopwatch();
        round = 1;
        tagged = null;
        hiders = null;
        tagged = new ArrayList<Player>();
        for(Player players : Bukkit.getOnlinePlayers()) {
            players.setGlowing(false);
        }
        hideandseekStarted = false;
        if(Bukkit.getScheduler().isCurrentlyRunning(taskID)) {
            stopTimer();
        }
        player = p;
        world = player.getWorld();
        setTimer(15);
        startTimer();
    }

    public static void stophideandseek() {
        for(Player players : Bukkit.getOnlinePlayers()) {
            players.setGlowing(false);
        }
        hideandseekStarted = false;
        stopTimer();
        world.setPVP(true);
        stopstopwatch();
        tagged = null;
        hiders = null;
    }

}
