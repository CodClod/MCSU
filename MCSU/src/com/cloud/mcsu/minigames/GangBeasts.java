package com.cloud.mcsu.minigames;

import com.cloud.mcsu.MCSU;
import com.cloud.mcsu.announcements.Announcement;
import com.cloud.mcsu.config.Config;
import com.cloud.mcsu.event.Event;
import com.cloud.mcsu.worldreset.WorldManager;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scoreboard.Team;

import java.io.IOException;
import java.util.*;

public class GangBeasts implements CommandExecutor, Listener {

    public static int taskID;
    public static int time;
    public static int stopwatchtaskID;
    public static int stopwatchtime;
    public static Player player;
    public static World world;
    public static boolean gangbeastsStarted;
    public static MCSU mcsu = MCSU.getPlugin(MCSU.class);
    public static int round;
    public static ArrayList<Location> spawns = new ArrayList<Location>();
    public static String[] teams = {
            "Blue",
            "Red",
            "Green",
            "Yellow",
            "Aqua",
            "Pink",
            "Grey",
            "White"
    };
    public static int[] a;
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
        if (cmd.getName().equalsIgnoreCase("gangbeasts") && player.isOp()) {
            if(args.length >= 1) {
                int currentround = Integer.parseInt(args[0]);
                gangbeastsCommand(player,currentround);
            }
        }
        if (cmd.getName().equalsIgnoreCase("stopgangbeasts") && player.isOp()) {
            stopGangbeasts();
        }
        return true;
    }

    public static void gangbeastsCommand(Player p, int r) {
        world = p.getWorld();
        List<Entity> entList = world.getEntities();//get all entities in the world
        for(Entity current : entList) {//loop through the list
            if (current instanceof Item) {//make sure we aren't deleting mobs/players
                current.remove();//remove it
            }
            if (current instanceof Creeper) {//make sure we aren't deleting mobs/players
                current.remove();//remove it
            }
            if (current instanceof Skeleton) {//make sure we aren't deleting mobs/players
                current.remove();//remove it
            }
            if (current instanceof Arrow) {//make sure we aren't deleting mobs/players
                current.remove();//remove it
            }
        }
        for(Player players : Bukkit.getOnlinePlayers()) {
            players.setInvulnerable(false);
        }
        stopwatchtime = 0;
        stopTimer();
        stopstopwatch();
        gangbeastsStarted = false;
        if (Bukkit.getScheduler().isCurrentlyRunning(taskID)) {
            stopTimer();
        }
        round = r;
        player = p;
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

    public static void stopGangbeasts() {
        for (Player players : Bukkit.getOnlinePlayers()) {
            players.setGlowing(false);
            players.setInvulnerable(false);
        }
        gangbeastsStarted = false;
        stopwatchtime = 0;
        stopTimer();
        world.setPVP(true);
        stopstopwatch();
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
                    if (!gangbeastsStarted) {
                        gangbeastsStarted = true;
                        try {
                            gangbeastsStart();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        stopwatch();
                    }
                    return;
                }
                if (time % 1 == 0 && !gangbeastsStarted && time <= 10) {
                    for (Player players : Bukkit.getOnlinePlayers()) {
                        players.sendTitle("" + time, "", 1, 20, 1);
                        players.playSound(players.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 1, 1);
                    }
                }
                if (time == 15 && !gangbeastsStarted) {
                    int size = 8;
                    a = new int[size];
                    for (int i = 0; i < size; i++) {
                        a[i] = (int) (Math.random() * size);//note, this generates numbers from [0,9]
                        player.sendMessage(Arrays.toString(a));
                        for (int j = 0; j < i; j++) {
                            if (a[i] == a[j]) {
                                i--; //if a[i] is a duplicate of a[j], then run the outer loop on i again
                                break;
                            }
                        }
                    }
                    world.setPVP(false);
                    MCSU.currentgame = "Gang Beasts";
                    MCSU.gameround = round;
                    for (Player players : Bukkit.getOnlinePlayers()) {
                        players.sendTitle("Gang Beasts starting in " + time + " seconds!", "", 1, 20, 1);
                        players.sendMessage(ChatColor.AQUA + "Gang Beasts Starting in " + time + " seconds!");
                        players.teleport(new Location(world, -955.5, 102, -1914.5));
                        players.playSound(players.getLocation(), Sound.ENTITY_VILLAGER_WORK_FLETCHER, 1, 1);
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
                    String[] msgs = {
                            ChatColor.LIGHT_PURPLE+"Gang Beasts",
                            "",
                            ChatColor.WHITE+"Knock players off the map and get powerups that spawn on the beacons",
                            ChatColor.WHITE+"to gear yourself up to win.",
                            ChatColor.WHITE+"Good Luck and Have Fun!"
                    };
                    new Announcement(msgs);
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
                player.sendMessage("stopwatch: " + stopwatchtime);
                stopwatchtime = stopwatchtime + 1;
                if (stopwatchtime == 15 || stopwatchtime == 30 || stopwatchtime == 45) {
                    // Give powerups
                    ItemStack woodswordkb = new ItemStack(Material.WOODEN_SWORD, 1);
                    woodswordkb.addEnchantment(Enchantment.KNOCKBACK, 1);
                    ItemStack[] powerups = {
                            woodswordkb,
                            new ItemStack(Material.ENDER_PEARL, 1),
                            new ItemStack(Material.ARROW, 1),
                            new ItemStack(Material.ARROW, 2),
                            new ItemStack(Material.ARROW, 3),
                            new ItemStack(Material.CREEPER_SPAWN_EGG, 1),
                            new ItemStack(Material.SKELETON_SPAWN_EGG, 1),
                            new ItemStack(Material.NETHERITE_BOOTS, 1)
                    };
                    Random rand = new Random();
                    world.dropItem(new Location(world, -814.5, 38, -1912.5), powerups[rand.nextInt(8)]);
                    world.dropItem(new Location(world, -955.5, 43, -1914.5), powerups[rand.nextInt(8)]);
                    world.dropItem(new Location(world, -947.5, 35, -2089.5), powerups[rand.nextInt(8)]);
                    world.dropItem(new Location(world, -1186.5, 25, -2114.5), powerups[rand.nextInt(8)]);
                    world.dropItem(new Location(world, -827.5, 48, -2220.5), powerups[rand.nextInt(8)]);
                    for (Player players : Bukkit.getOnlinePlayers()) {
                        players.sendMessage(ChatColor.GREEN + "Powerups have spawned!");
                    }
                } else if (stopwatchtime == 120) {
                    stopGangbeasts();
                    for (Player players : Bukkit.getOnlinePlayers()) {
                        players.sendTitle(ChatColor.AQUA+"Game Over!","");
                    }
                } else if(stopwatchtime == 60) {
                    for(Player players : Bukkit.getOnlinePlayers()) {
                        ItemStack woodswordkb = new ItemStack(Material.WOODEN_SWORD, 1);
                        woodswordkb.addEnchantment(Enchantment.KNOCKBACK,2);
                        players.getInventory().addItem(woodswordkb);
                        players.getInventory().addItem(new ItemStack(Material.CREEPER_SPAWN_EGG,1));
                        players.getInventory().addItem(new ItemStack(Material.ENDER_PEARL,1));
                        players.getInventory().addItem(new ItemStack(Material.ARROW,10));
                    }
                } else if(stopwatchtime == 90) {
                    for(Player players : Bukkit.getOnlinePlayers()) {
                        ItemStack netheriteswordkb = new ItemStack(Material.NETHERITE_SWORD,1);
                        netheriteswordkb.addEnchantment(Enchantment.KNOCKBACK,2);
                        players.getInventory().addItem(netheriteswordkb);
                        players.getInventory().addItem(new ItemStack(Material.CREEPER_SPAWN_EGG,64));
                        players.getInventory().addItem(new ItemStack(Material.ARROW,64));
                    }
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

    public static void gangbeastsStart() throws IOException {
        for (Player players : Bukkit.getOnlinePlayers()) {
            players.setInvulnerable(false);
            players.sendTitle("§d§lFight!", "", 1, 20, 1);
            players.playSound(players.getLocation(), Sound.ENTITY_VILLAGER_WORK_FLETCHER, 1, 1);
        }
        world.setPVP(true);
        for (Player p : Bukkit.getOnlinePlayers()) {
            p.setGameMode(GameMode.SURVIVAL);
            p.getInventory().clear();
            p.setFoodLevel(20);
            p.setHealth(20);
            p.setFireTicks(1);
            p.setLevel(0);
            p.setExp(0);
            for (PotionEffect effect : p.getActivePotionEffects()) {
                p.removePotionEffect(effect.getType());
            }
            Team team = p.getScoreboard().getPlayerTeam(p);
            String teamname = team.getName();
            p.getInventory().addItem(new ItemStack(Material.ARROW, 1));
            ItemStack bow = new ItemStack(Material.BOW, 1);
            bow.addEnchantment(Enchantment.ARROW_FIRE, 1);
            bow.addEnchantment(Enchantment.ARROW_KNOCKBACK, 1);
            p.getInventory().addItem(bow);
            ItemStack fishingrod = new ItemStack(Material.FISHING_ROD, 1);
            p.getInventory().addItem(fishingrod);
            spawns.add(new Location(world, -946.5, 43, -1907.5)); // Map 1 Team 1 Spawn
            spawns.add(new Location(world, -963.5, 43, -1922.5)); // Map 1 Team 2 Spawn
            spawns.add(new Location(world, -964.5, 43, -1907.5)); // Map 1 Team 3 Spawn
            spawns.add(new Location(world, -945.5, 44, -1924.5)); // Map 1 Team 4 Spawn
            spawns.add(new Location(world, -1176, 27, -2098)); // Map 2 Team 1 Spawn
            spawns.add(new Location(world, -1176, 27, -2108)); // Map 2 Team 2 Spawn
            spawns.add(new Location(world, -1181.5, 27, -2104)); // Map 2 Team 3 Spawn
            spawns.add(new Location(world, -1170.5, 27, -2104)); // Map 2 Team 4 Spawn
            spawns.add(new Location(world, -948, 34, -2096.5)); // Map 3 Team 1 Spawn
            spawns.add(new Location(world, -948, 34, -2083.5)); // Map 3 Team 2 Spawn
            spawns.add(new Location(world, -954.5, 34, -2090)); // Map 3 Team 3 Spawn
            spawns.add(new Location(world, -941.5, 34, -2090)); // Map 3 Team 4 Spawn
            spawns.add(new Location(world, -820.5, 49, -2214.5)); // Map 4 Team 1 Spawn
            spawns.add(new Location(world, -834.5, 49, -2226.5)); // Map 4 Team 2 Spawn
            spawns.add(new Location(world, -820.5, 49, -2226.5)); // Map 4 Team 3 Spawn
            spawns.add(new Location(world, -834.5, 49, -2214.5)); // Map 4 Team 4 Spawn
            spawns.add(new Location(world, -811.5, 43, -1909.5)); // Map 5 Team 1 Spawn
            spawns.add(new Location(world, -817.5, 34, -1915.5)); // Map 5 Team 2 Spawn
            spawns.add(new Location(world, -811.5, 48, -1912.5)); // Map 5 Team 3 Spawn
            spawns.add(new Location(world, -817.5, 38, -1913.5)); // Map 5 Team 4 Spawn
            if (round == 1) {
                WorldManager.resetgbmap1(player);
                WorldManager.resetgbmap2(player);
                if(teams[a[0]].equals(teamname)) {
                    p.teleport(spawns.get(0));
                } else if(teams[a[1]].equals(teamname)) {
                    p.teleport(spawns.get(1));
                } else if(teams[a[2]].equals(teamname)) {
                    p.teleport(spawns.get(2));
                } else if(teams[a[3]].equals(teamname)) {
                    p.teleport(spawns.get(3));
                } else if(teams[a[4]].equals(teamname)) {
                    p.teleport(spawns.get(4));
                } else if(teams[a[5]].equals(teamname)) {
                    p.teleport(spawns.get(5));
                } else if(teams[a[6]].equals(teamname)) {
                    p.teleport(spawns.get(6));
                } else if(teams[a[7]].equals(teamname)) {
                    p.teleport(spawns.get(7));
                }
            } else if (round == 2) {
                WorldManager.resetgbmap2(player);
                WorldManager.resetgbmap3(player);
                if(teams[a[0]].equals(teamname)) {
                    p.teleport(spawns.get(4));
                } else if(teams[a[1]].equals(teamname)) {
                    p.teleport(spawns.get(5));
                } else if(teams[a[2]].equals(teamname)) {
                    p.teleport(spawns.get(6));
                } else if(teams[a[3]].equals(teamname)) {
                    p.teleport(spawns.get(7));
                } else if(teams[a[4]].equals(teamname)) {
                    p.teleport(spawns.get(8));
                } else if(teams[a[5]].equals(teamname)) {
                    p.teleport(spawns.get(9));
                } else if(teams[a[6]].equals(teamname)) {
                    p.teleport(spawns.get(10));
                } else if(teams[a[7]].equals(teamname)) {
                    p.teleport(spawns.get(11));
                }
            } else if (round == 3) {
                WorldManager.resetgbmap3(player);
                WorldManager.resetgbmap4(player);
                if(teams[a[0]].equals(teamname)) {
                    p.teleport(spawns.get(8));
                } else if(teams[a[1]].equals(teamname)) {
                    p.teleport(spawns.get(9));
                } else if(teams[a[2]].equals(teamname)) {
                    p.teleport(spawns.get(10));
                } else if(teams[a[3]].equals(teamname)) {
                    p.teleport(spawns.get(11));
                } else if(teams[a[4]].equals(teamname)) {
                    p.teleport(spawns.get(12));
                } else if(teams[a[5]].equals(teamname)) {
                    p.teleport(spawns.get(13));
                } else if(teams[a[6]].equals(teamname)) {
                    p.teleport(spawns.get(14));
                } else if(teams[a[7]].equals(teamname)) {
                    p.teleport(spawns.get(15));
                }
            } else if (round == 4) {
                WorldManager.resetgbmap4(player);
                WorldManager.resetgbmap5(player);
                if(teams[a[0]].equals(teamname)) {
                    p.teleport(spawns.get(12));
                } else if(teams[a[1]].equals(teamname)) {
                    p.teleport(spawns.get(13));
                } else if(teams[a[2]].equals(teamname)) {
                    p.teleport(spawns.get(14));
                } else if(teams[a[3]].equals(teamname)) {
                    p.teleport(spawns.get(15));
                } else if(teams[a[4]].equals(teamname)) {
                    p.teleport(spawns.get(16));
                } else if(teams[a[5]].equals(teamname)) {
                    p.teleport(spawns.get(17));
                } else if(teams[a[6]].equals(teamname)) {
                    p.teleport(spawns.get(18));
                } else if(teams[a[7]].equals(teamname)) {
                    p.teleport(spawns.get(19));
                }
            } else if (round == 5) {
                WorldManager.resetgbmap5(player);
                WorldManager.resetgbmap1(player);
                if(teams[a[0]].equals(teamname)) {
                    p.teleport(spawns.get(16));
                } else if(teams[a[1]].equals(teamname)) {
                    p.teleport(spawns.get(17));
                } else if(teams[a[2]].equals(teamname)) {
                    p.teleport(spawns.get(18));
                } else if(teams[a[3]].equals(teamname)) {
                    p.teleport(spawns.get(19));
                } else if(teams[a[4]].equals(teamname)) {
                    p.teleport(spawns.get(0));
                } else if(teams[a[5]].equals(teamname)) {
                    p.teleport(spawns.get(1));
                } else if(teams[a[6]].equals(teamname)) {
                    p.teleport(spawns.get(2));
                } else if(teams[a[7]].equals(teamname)) {
                    p.teleport(spawns.get(3));
                }
            }
        }
    }

    @EventHandler
    public static void onKill(PlayerDeathEvent e) {
        if(gangbeastsStarted) {
            Player killer = e.getEntity().getKiller();
            ItemStack[] items = {new ItemStack(Material.TNT,1),new ItemStack(Material.WHITE_WOOL,4),new ItemStack(Material.LADDER,2)};
            Random rand = new Random();
            killer.getInventory().addItem(items[rand.nextInt(3)]);
        }
    }

    @EventHandler
    public static void onKillPoint(PlayerDeathEvent e) {
        Player killer = e.getEntity().getKiller();
        Team team = killer.getScoreboard().getPlayerTeam(killer);
        String teamname = team.getName();
        if(gangbeastsStarted) {
            if(teamname.equals("Blue")) {
                int points = MCSU.bluepoints + 20;
                Config.get().set("Points.BluePoints",Integer.toString(points));
                MCSU.getPoints();
                Bukkit.broadcastMessage(ChatColor.BLUE+killer.getName()+ChatColor.WHITE+" just earned +"+ChatColor.GOLD+"20"+ChatColor.WHITE+" points!");
                killer.playSound(killer.getLocation(),Sound.ENTITY_ARROW_HIT_PLAYER,100,1);
                killer.playSound(killer.getLocation(),Sound.ENTITY_GENERIC_EXPLODE,100,1);
            }
            if(teamname.equals("Pink")) {
                int points = MCSU.pinkpoints + 20;
                Config.get().set("Points.PinkPoints",Integer.toString(points));
                MCSU.getPoints();
                Bukkit.broadcastMessage(ChatColor.BLUE+killer.getName()+ChatColor.WHITE+" just earned +"+ChatColor.GOLD+"20"+ChatColor.WHITE+" points!");
                killer.playSound(killer.getLocation(),Sound.ENTITY_ARROW_HIT_PLAYER,100,1);
                killer.playSound(killer.getLocation(),Sound.ENTITY_GENERIC_EXPLODE,100,1);
            }
            if(teamname.equals("Yellow")) {
                int points = MCSU.yellowpoints + 20;
                Config.get().set("Points.YellowPoints",Integer.toString(points));
                MCSU.getPoints();
                Bukkit.broadcastMessage(ChatColor.BLUE+killer.getName()+ChatColor.WHITE+" just earned +"+ChatColor.GOLD+"20"+ChatColor.WHITE+" points!");
                killer.playSound(killer.getLocation(),Sound.ENTITY_ARROW_HIT_PLAYER,100,1);
                killer.playSound(killer.getLocation(),Sound.ENTITY_GENERIC_EXPLODE,100,1);
            }
            if(teamname.equals("Green")) {
                int points = MCSU.greenpoints + 20;
                Config.get().set("Points.GreenPoints",Integer.toString(points));
                MCSU.getPoints();
                Bukkit.broadcastMessage(ChatColor.BLUE+killer.getName()+ChatColor.WHITE+" just earned +"+ChatColor.GOLD+"20"+ChatColor.WHITE+" points!");
                killer.playSound(killer.getLocation(),Sound.ENTITY_ARROW_HIT_PLAYER,100,1);
                killer.playSound(killer.getLocation(),Sound.ENTITY_GENERIC_EXPLODE,100,1);
            }
            if(teamname.equals("Red")) {
                int points = MCSU.redpoints + 20;
                Config.get().set("Points.RedPoints",Integer.toString(points));
                MCSU.getPoints();
                Bukkit.broadcastMessage(ChatColor.BLUE+killer.getName()+ChatColor.WHITE+" just earned +"+ChatColor.GOLD+"20"+ChatColor.WHITE+" points!");
                killer.playSound(killer.getLocation(),Sound.ENTITY_ARROW_HIT_PLAYER,100,1);
                killer.playSound(killer.getLocation(),Sound.ENTITY_GENERIC_EXPLODE,100,1);
            }
            if(teamname.equals("Grey")) {
                int points = MCSU.greypoints + 20;
                Config.get().set("Points.GreyPoints",Integer.toString(points));
                MCSU.getPoints();
                Bukkit.broadcastMessage(ChatColor.BLUE+killer.getName()+ChatColor.WHITE+" just earned +"+ChatColor.GOLD+"20"+ChatColor.WHITE+" points!");
                killer.playSound(killer.getLocation(),Sound.ENTITY_ARROW_HIT_PLAYER,100,1);
                killer.playSound(killer.getLocation(),Sound.ENTITY_GENERIC_EXPLODE,100,1);
            }
            if(teamname.equals("Aqua")) {
                int points = MCSU.aquapoints + 20;
                Config.get().set("Points.AquaPoints",Integer.toString(points));
                MCSU.getPoints();
                Bukkit.broadcastMessage(ChatColor.BLUE+killer.getName()+ChatColor.WHITE+" just earned +"+ChatColor.GOLD+"20"+ChatColor.WHITE+" points!");
                killer.playSound(killer.getLocation(),Sound.ENTITY_ARROW_HIT_PLAYER,100,1);
                killer.playSound(killer.getLocation(),Sound.ENTITY_GENERIC_EXPLODE,100,1);
            }
            if(teamname.equals("White")) {
                int points = MCSU.whitepoints + 20;
                Config.get().set("Points.WhitePoints",Integer.toString(points));
                MCSU.getPoints();
                Bukkit.broadcastMessage(ChatColor.BLUE+killer.getName()+ChatColor.WHITE+" just earned +"+ChatColor.GOLD+"20"+ChatColor.WHITE+" points!");
                killer.playSound(killer.getLocation(),Sound.ENTITY_ARROW_HIT_PLAYER,100,1);
                killer.playSound(killer.getLocation(),Sound.ENTITY_GENERIC_EXPLODE,100,1);
            }
        }
    }

    @EventHandler
    public static void onCreeperExplode(EntityExplodeEvent e) {
        if(gangbeastsStarted) {
            if(e.getEntity() instanceof Creeper) {
                e.setCancelled(true);
                e.getEntity().getWorld().createExplosion(e.getLocation(),10);
            }
        }
    }

    @EventHandler
    public static void onDeath(PlayerDeathEvent e) {
        if (gangbeastsStarted) {
            Player deadplayer = e.getEntity();
            Player killer = e.getEntity().getKiller();
            Team team = deadplayer.getScoreboard().getPlayerTeam(deadplayer);
            String teamname = team.getName();
            int i = 0;
            deadplayers.add(deadplayer);
            for (String players : team.getEntries()) {
                if (Bukkit.getOnlinePlayers().contains(Bukkit.getPlayer(players))) {
                    onlineteamplayers.put(teamname, onlineteamplayers.get(teamname) + 1);
                }
                if (deadplayers.contains(Bukkit.getServer().getPlayer(players))) {
                    i++;
                }
            }
            for (Player players : Bukkit.getOnlinePlayers()) {
                if (!onlineteams.contains(players.getScoreboard().getPlayerTeam(players).getName())) {
                    onlineteams.add(players.getScoreboard().getPlayerTeam(players).getName());
                }
            }
            if (i == onlineteamplayers.get(teamname)) {
                deadteams.add(teamname);
                for (Player players : Bukkit.getOnlinePlayers()) {
                    players.sendMessage(players.getScoreboard().getTeam(teamname).getColor() + teamname + ChatColor.WHITE + " has been eliminated.");
                }
            }
            int teamsleftcount = onlineteams.size() - deadteams.size();
            int j;
            if(onlineteams.size() >= 8) {
                j = 2;
            } else {
                j = 1;
            }
            if (teamsleftcount == j) {
                stopGangbeasts();
                for (Player players : Bukkit.getOnlinePlayers()) {
                    players.setInvulnerable(true);
                    players.sendTitle(ChatColor.AQUA+"Game Over!","");
                }
            }
        }
    }

    @EventHandler
    public static void onBreakBlocks(BlockBreakEvent e) {
        if(gangbeastsStarted) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public static void onDmg(EntityDamageEvent e) {
        if(gangbeastsStarted) {
            if(e.getCause() == EntityDamageEvent.DamageCause.LAVA) {
                e.setDamage(1000);
            } else if(e.getCause() == EntityDamageEvent.DamageCause.VOID) {
                e.setDamage(1000);
            } else {
                e.setDamage(0);
                e.getEntity().setFireTicks(1);
            }
        }
    }
}

