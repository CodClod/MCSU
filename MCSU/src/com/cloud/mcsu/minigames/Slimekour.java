package com.cloud.mcsu.minigames;

import com.cloud.mcsu.MCSU;
import com.cloud.mcsu.announcements.Announcement;
import com.cloud.mcsu.config.Config;
import com.cloud.mcsu.event.Event;
import com.cloud.mcsu.items.Items;
import com.cloud.mcsu.worldreset.WorldManager;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.block.BlockFace;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scoreboard.Team;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class Slimekour implements CommandExecutor, Listener {

    public static int time;
    public static int stopwatchtime;
    public static Player player;
    public static boolean slimekourStarted;
    public static World world;
    public static MCSU mcsu = MCSU.getPlugin(MCSU.class);
    public static int taskID;
    public static int stopwatchtaskID;
    public static ArrayList<Player> finishers = new ArrayList<Player>();
    public static HashMap<Player, Integer> finisherpositions = new HashMap<Player, Integer>();
    public static String fancytimer;
    public static int[] placementpoints = {
            175,
            150,
            135,
            125,
            110,
            100,
            75,
            70,
            65,
            60,
            55,
            50,
            45,
            40,
            35,
            30
    };
    public static int countdowntime;
    public static int countdowntaskID;
    public static int map;

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can use that command!");
            return true;
        }
        player = (Player) sender;
        if (cmd.getName().equalsIgnoreCase("slimekour") && player.isOp()) {
            slimekourCommand(player);
        }
        if(cmd.getName().equalsIgnoreCase("stopslimekour") && player.isOp()) {
            stopSlimekour();
        }
        return true;
    }

    public static void slimekourCommand(Player p) {
        stopTimer();
        stopstopwatch();
        slimekourStarted = false;
        player = p;
        world = player.getWorld();
        setTimer(15);
        startTimer();
        finishers = null;
        finisherpositions = null;
        finishers = new ArrayList<Player>();
        finisherpositions = new HashMap<Player, Integer>();
        map = new Random().nextInt(3);
    }

    public static void stopSlimekour() {
        for (Player players : Bukkit.getOnlinePlayers()) {
            players.setInvulnerable(false);
        }
        stopTimer();
        slimekourStarted = false;
        stopstopwatch();
        finishers = null;
        finisherpositions = null;
        finishers = new ArrayList<Player>();
        finisherpositions = new HashMap<Player, Integer>();
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
                    if(!slimekourStarted) {
                        stopwatchtime = 0;
                        stopwatch();
                        slimekourStart();
                        slimekourStarted = true;
                    } else {
                        stopTimer();
                    }
                    return;
                }
                if(time % 1 == 0 && !slimekourStarted && time <= 10) {
                    for (Player players : Bukkit.getOnlinePlayers()) {
                        players.sendTitle(""+time, "", 1, 20, 1);
                        players.playSound(players.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL,1,1);
                    }
                }
                if(time == 15 && !slimekourStarted) {
                    if(map == 0) {
                        setSquare(Material.WHITE_STAINED_GLASS,new Location(world,-143,13,884),new Location(world,-143,14,883));
                        setSquare(Material.WHITE_STAINED_GLASS,new Location(world,-143,13,880),new Location(world,-143,15,879));
                        setSquare(Material.WHITE_STAINED_GLASS,new Location(world,-142,16,884),new Location(world,-141,16,879));
                        /*
                        // Set blocks to glass at spawn
                        world.getBlockAt(new Location(world,-143,14,884)).setType(Material.WHITE_STAINED_GLASS);
                        world.getBlockAt(new Location(world,-143,14,883)).setType(Material.WHITE_STAINED_GLASS);
                        world.getBlockAt(new Location(world,-143,13,884)).setType(Material.WHITE_STAINED_GLASS);
                        world.getBlockAt(new Location(world,-143,13,883)).setType(Material.WHITE_STAINED_GLASS);
                        world.getBlockAt(new Location(world,-143,15,880)).setType(Material.WHITE_STAINED_GLASS);
                        world.getBlockAt(new Location(world,-143,15,879)).setType(Material.WHITE_STAINED_GLASS);
                        world.getBlockAt(new Location(world,-143,14,880)).setType(Material.WHITE_STAINED_GLASS);
                        world.getBlockAt(new Location(world,-143,14,879)).setType(Material.WHITE_STAINED_GLASS);
                        world.getBlockAt(new Location(world,-143,13,880)).setType(Material.WHITE_STAINED_GLASS);
                        world.getBlockAt(new Location(world,-143,13,879)).setType(Material.WHITE_STAINED_GLASS);
                        world.getBlockAt(new Location(world,-142,16,884)).setType(Material.WHITE_STAINED_GLASS);
                        world.getBlockAt(new Location(world,-141,16,884)).setType(Material.WHITE_STAINED_GLASS);
                        world.getBlockAt(new Location(world,-142,16,883)).setType(Material.WHITE_STAINED_GLASS);
                        world.getBlockAt(new Location(world,-141,16,883)).setType(Material.WHITE_STAINED_GLASS);
                        world.getBlockAt(new Location(world,-142,16,882)).setType(Material.WHITE_STAINED_GLASS);
                        world.getBlockAt(new Location(world,-141,16,882)).setType(Material.WHITE_STAINED_GLASS);
                        world.getBlockAt(new Location(world,-142,16,881)).setType(Material.WHITE_STAINED_GLASS);
                        world.getBlockAt(new Location(world,-141,16,881)).setType(Material.WHITE_STAINED_GLASS);
                        world.getBlockAt(new Location(world,-142,16,880)).setType(Material.WHITE_STAINED_GLASS);
                        world.getBlockAt(new Location(world,-141,16,880)).setType(Material.WHITE_STAINED_GLASS);
                        world.getBlockAt(new Location(world,-142,16,879)).setType(Material.WHITE_STAINED_GLASS);
                        world.getBlockAt(new Location(world,-141,16,879)).setType(Material.WHITE_STAINED_GLASS);
                        world.getBlockAt(new Location(world,-143,16,880)).setType(Material.WHITE_STAINED_GLASS);
                        world.getBlockAt(new Location(world,-143,16,879)).setType(Material.WHITE_STAINED_GLASS);

                         */
                    } else if(map == 1) {
                        setSquare(Material.WHITE_STAINED_GLASS,new Location(world,168,5,912),new Location(world,168,7,911));
                        setSquare(Material.WHITE_STAINED_GLASS,new Location(world,166,5,909),new Location(world,165,7,909));
                        /*
                        world.getBlockAt(new Location(world,168,5,912)).setType(Material.WHITE_STAINED_GLASS);
                        world.getBlockAt(new Location(world,168,6,912)).setType(Material.WHITE_STAINED_GLASS);
                        world.getBlockAt(new Location(world,168,7,912)).setType(Material.WHITE_STAINED_GLASS);
                        world.getBlockAt(new Location(world,168,5,911)).setType(Material.WHITE_STAINED_GLASS);
                        world.getBlockAt(new Location(world,168,6,911)).setType(Material.WHITE_STAINED_GLASS);
                        world.getBlockAt(new Location(world,168,7,911)).setType(Material.WHITE_STAINED_GLASS);
                        world.getBlockAt(new Location(world,166,5,909)).setType(Material.WHITE_STAINED_GLASS);
                        world.getBlockAt(new Location(world,166,6,909)).setType(Material.WHITE_STAINED_GLASS);
                        world.getBlockAt(new Location(world,166,7,909)).setType(Material.WHITE_STAINED_GLASS);
                        world.getBlockAt(new Location(world,165,5,909)).setType(Material.WHITE_STAINED_GLASS);
                        world.getBlockAt(new Location(world,165,6,909)).setType(Material.WHITE_STAINED_GLASS);
                        world.getBlockAt(new Location(world,165,7,909)).setType(Material.WHITE_STAINED_GLASS);

                         */
                    } else if(map == 2) {
                        setSquare(Material.WHITE_STAINED_GLASS,new Location(world,-528,9,991),new Location(world,-534,11,991));
                    }
                    world.setPVP(false);
                    MCSU.currentgame = "Slimekour";
                    MCSU.gameround = 1;
                    for (Player players : Bukkit.getOnlinePlayers()) {
                        players.sendTitle("Slimekour starting in "+time+" seconds!", "", 1, 20, 1);
                        players.sendMessage(ChatColor.AQUA+"Slimekour Starting in "+time+" seconds!");
                        if(map == 0) {
                            players.teleport(new Location(world,-141,14,882));
                        } else if(map == 1) {
                            players.teleport(new Location(world,170,6,907));
                        } else if(map == 2) {
                            players.teleport(new Location(world,-530.5,9.5,989.5));
                        }
                        players.playSound(players.getLocation(), Sound.ENTITY_SLIME_SQUISH,1,1);
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
                    String[] msgs = {
                            ChatColor.GREEN+"Slimekour",
                            "",
                            ChatColor.WHITE+"Parkour your way through the map by jumping onto slime blocks. Most jumps will be from the same colour to the same colour.",
                            ChatColor.WHITE+"Use the arrows on signs to help you and remember to not press spacebar when on a slime block",
                            ChatColor.WHITE+"or you won't get a powerful jump and also hit people to mess up their jump.",
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
                if(slimekourStarted) {
                    stopwatchtime = stopwatchtime + 1;
                    int finish = stopwatchtime / 60;
                    int remainder = stopwatchtime % 60;
                    if(Integer.toString(remainder).length() == 1 && Integer.toString(finish).length() == 1) {
                        String finishremainder = "0"+remainder;
                        String finishtime = "0"+finish;
                        fancytimer = finishtime+":"+finishremainder;
                    } else if(Integer.toString(finish).length() == 1) {
                        String finishtime = "0"+finish;
                        fancytimer = finishtime+":"+remainder;
                    } else if(Integer.toString(remainder).length() == 1) {
                        String finishremainder = "0"+remainder;
                        fancytimer = finish+":"+finishremainder;
                    } else {
                        fancytimer = finish+":"+remainder;
                    }
                    for(Player players : Bukkit.getOnlinePlayers()) {
                        if(!finishers.contains(players)) {
                            players.spigot().sendMessage(ChatMessageType.ACTION_BAR,new TextComponent("Time: "+fancytimer));
                        } else {
                            players.spigot().sendMessage(ChatMessageType.ACTION_BAR,new TextComponent("Finished Slimekour!"));
                        }
                    }
                }
            }
        }, 0L, 20L);
    }

    @EventHandler
    public static void onTakeDmg(EntityDamageEvent e) {
        if(slimekourStarted) {
            if(e.getEntity() instanceof Player) {
                if(e.getCause() == EntityDamageEvent.DamageCause.FALL) {
                    e.setCancelled(true);
                } else {
                    e.setDamage(0);
                }
            }
        }
    }

    public static void stopstopwatch() {
        Bukkit.getScheduler().cancelTask(stopwatchtaskID);
    }

    public static void stopTimer() {
        Bukkit.getScheduler().cancelTask(taskID);
    }

    public static void slimekourStart() {
        world.setPVP(true);
        if(map == 0) {
            setSquare(Material.AIR,new Location(world,-143,13,884),new Location(world,-143,14,883));
            setSquare(Material.AIR,new Location(world,-143,13,880),new Location(world,-143,15,879));
            setSquare(Material.AIR,new Location(world,-142,16,884),new Location(world,-141,16,879));
            // Replace glass at slimekour spawn
            /*
            world.getBlockAt(new Location(world,-143,14,884)).setType(Material.AIR);
            world.getBlockAt(new Location(world,-143,14,883)).setType(Material.AIR);
            world.getBlockAt(new Location(world,-143,13,884)).setType(Material.AIR);
            world.getBlockAt(new Location(world,-143,13,883)).setType(Material.AIR);
            world.getBlockAt(new Location(world,-143,15,880)).setType(Material.AIR);
            world.getBlockAt(new Location(world,-143,15,879)).setType(Material.AIR);
            world.getBlockAt(new Location(world,-143,14,880)).setType(Material.AIR);
            world.getBlockAt(new Location(world,-143,14,879)).setType(Material.AIR);
            world.getBlockAt(new Location(world,-143,13,880)).setType(Material.AIR);
            world.getBlockAt(new Location(world,-143,13,879)).setType(Material.AIR);
            world.getBlockAt(new Location(world,-142,16,884)).setType(Material.AIR);
            world.getBlockAt(new Location(world,-141,16,884)).setType(Material.AIR);
            world.getBlockAt(new Location(world,-142,16,883)).setType(Material.AIR);
            world.getBlockAt(new Location(world,-141,16,883)).setType(Material.AIR);
            world.getBlockAt(new Location(world,-142,16,882)).setType(Material.AIR);
            world.getBlockAt(new Location(world,-141,16,882)).setType(Material.AIR);
            world.getBlockAt(new Location(world,-142,16,881)).setType(Material.AIR);
            world.getBlockAt(new Location(world,-141,16,881)).setType(Material.AIR);
            world.getBlockAt(new Location(world,-142,16,880)).setType(Material.AIR);
            world.getBlockAt(new Location(world,-141,16,880)).setType(Material.AIR);
            world.getBlockAt(new Location(world,-142,16,879)).setType(Material.AIR);
            world.getBlockAt(new Location(world,-141,16,879)).setType(Material.AIR);
            world.getBlockAt(new Location(world,-143,16,880)).setType(Material.AIR);
            world.getBlockAt(new Location(world,-143,16,879)).setType(Material.AIR);

             */
        } else if(map == 1) {
            setSquare(Material.AIR,new Location(world,168,5,912),new Location(world,168,7,911));
            setSquare(Material.AIR,new Location(world,166,5,909),new Location(world,165,7,909));
            /*
            world.getBlockAt(new Location(world,168,5,912)).setType(Material.AIR);
            world.getBlockAt(new Location(world,168,6,912)).setType(Material.AIR);
            world.getBlockAt(new Location(world,168,7,912)).setType(Material.AIR);
            world.getBlockAt(new Location(world,168,5,911)).setType(Material.AIR);
            world.getBlockAt(new Location(world,168,6,911)).setType(Material.AIR);
            world.getBlockAt(new Location(world,168,7,911)).setType(Material.AIR);
            world.getBlockAt(new Location(world,166,5,909)).setType(Material.AIR);
            world.getBlockAt(new Location(world,166,6,909)).setType(Material.AIR);
            world.getBlockAt(new Location(world,166,7,909)).setType(Material.AIR);
            world.getBlockAt(new Location(world,165,5,909)).setType(Material.AIR);
            world.getBlockAt(new Location(world,165,6,909)).setType(Material.AIR);
            world.getBlockAt(new Location(world,165,7,909)).setType(Material.AIR);

             */
        } else if(map == 2) {
            setSquare(Material.AIR,new Location(world,-528,9,991),new Location(world,-534,11,991));
        }
        for(Player p: Bukkit.getOnlinePlayers()) {
            p.setGameMode(GameMode.ADVENTURE);
            p.getInventory().clear();
            p.getInventory().addItem(Items.kbstick);
            p.setFoodLevel(20);
            p.setHealth(20);
            p.setFireTicks(1);
            p.setLevel(0);
            p.setExp(0);
            p.sendTitle("§2§lSlimekour", "§aParkour your way through the map using slime blocks to help you.");
            p.playSound(p.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL,100,1);
            for (PotionEffect effect : p.getActivePotionEffects()) {
                p.removePotionEffect(effect.getType());
            }
            p.addPotionEffect(new PotionEffect(PotionEffectType.SATURATION,Integer.MAX_VALUE,255));
        }
    }

    public static void setSquare(final Material material, final Location location1, final Location location2)
    {
        final World world = location1.getWorld();
        int highestX = Math.max(location2.getBlockX(), location1.getBlockX());
        int lowestX = Math.min(location2.getBlockX(), location1.getBlockX());

        int highestY = Math.max(location2.getBlockY(), location1.getBlockY());
        int lowestY = Math.min(location2.getBlockY(), location1.getBlockY());

        int highestZ = Math.max(location2.getBlockZ(), location1.getBlockZ());
        int lowestZ = Math.min(location2.getBlockZ(), location1.getBlockZ());

        new BukkitRunnable()
        {
            int lastRun = 0;
            @Override
            public void run() {
                for(int x = lowestX; x <= highestX; x++)
                {
                    for(int z = lowestZ; z <= highestZ; z++)
                    {
                        for(int y = lowestY; y <= highestY; y++)
                        {
                            Location location = new Location(world, x, y, z);
                            new BukkitRunnable()
                            {
                                @Override
                                public void run() {
                                    location.getBlock().setType(material);
                                }
                            }.runTaskLater(mcsu, (long) (lastRun * 0.01));
                            lastRun++;
                        }
                    }
                }
            }
        }.runTaskAsynchronously(mcsu);
    }

    public List<Player> getAll() {
        return new ArrayList<Player>();
    }

    @EventHandler
    public static void endSlimekour(PlayerInteractEvent e) {
        if(slimekourStarted) {
            Player slimekourplayer = e.getPlayer();
            Team team = slimekourplayer.getScoreboard().getPlayerTeam(slimekourplayer);
            String teamname = team.getName();
            if(e.getAction().equals(Action.PHYSICAL)) {
                if(e.getClickedBlock().getType() == Material.LIGHT_WEIGHTED_PRESSURE_PLATE) {
                    if(!finishers.contains(slimekourplayer)) {
                        int finish = stopwatchtime / 60;
                        int remainder = stopwatchtime % 60;
                        finishers.add(slimekourplayer);
                        int place = finishers.indexOf(slimekourplayer)+1;
                        if(teamname.equalsIgnoreCase("Blue")) {
                            int points = MCSU.bluepoints + placementpoints[place-1];
                            Config.get().set("Points.BluePoints",Integer.toString(points));
                            MCSU.getPoints();
                        } else if(teamname.equalsIgnoreCase("Red")) {
                            int points = MCSU.redpoints + placementpoints[place-1];
                            Config.get().set("Points.RedPoints",Integer.toString(points));
                            MCSU.getPoints();
                        } else if(teamname.equalsIgnoreCase("Yellow")) {
                            int points = MCSU.yellowpoints + placementpoints[place-1];
                            Config.get().set("Points.YellowPoints",Integer.toString(points));
                            MCSU.getPoints();
                        } else if(teamname.equalsIgnoreCase("Green")) {
                            int points = MCSU.greenpoints + placementpoints[place-1];
                            Config.get().set("Points.GreenPoints",Integer.toString(points));
                            MCSU.getPoints();
                        } else if(teamname.equalsIgnoreCase("Aqua")) {
                            int points = MCSU.aquapoints + placementpoints[place-1];
                            Config.get().set("Points.AquaPoints",Integer.toString(points));
                            MCSU.getPoints();
                        } else if(teamname.equalsIgnoreCase("Pink")) {
                            int points = MCSU.pinkpoints + placementpoints[place-1];
                            Config.get().set("Points.PinkPoints",Integer.toString(points));
                            MCSU.getPoints();

                        } else if(teamname.equalsIgnoreCase("Grey")) {
                            int points = MCSU.greypoints + placementpoints[place-1];
                            Config.get().set("Points.GreyPoints",Integer.toString(points));
                            MCSU.getPoints();
                        } else if(teamname.equalsIgnoreCase("White")) {
                            int points = MCSU.whitepoints + placementpoints[place-1];
                            Config.get().set("Points.WhitePoints",Integer.toString(points));
                            MCSU.getPoints();
                        }
                        if(finishers.size() == Bukkit.getOnlinePlayers().size()) {
                            stopSlimekour();
                            for(Player players : Bukkit.getOnlinePlayers()) {
                                players.sendTitle(ChatColor.AQUA+"Game Over!","");
                            }
                            Bukkit.getScheduler().cancelTask(countdowntaskID);
                        }
                        if(finishers.size() == 1) {
                            countdown();
                        }
                        Bukkit.broadcastMessage(ChatColor.BLUE+slimekourplayer.getName()+ChatColor.WHITE+" finished the slimekour in "+ChatColor.GREEN+place+"."+ChatColor.WHITE+" with a time of "+fancytimer);
                        Bukkit.broadcastMessage(ChatColor.BLUE+slimekourplayer.getName()+ChatColor.WHITE+" just earned +"+ChatColor.GOLD+placementpoints[place-1]+ChatColor.WHITE+" points!");
                        slimekourplayer.playSound(slimekourplayer.getLocation(),Sound.ENTITY_ARROW_HIT_PLAYER,100,1);
                        slimekourplayer.playSound(slimekourplayer.getLocation(),Sound.ENTITY_GENERIC_EXPLODE,100,1);
                    }
                    slimekourplayer.setGameMode(GameMode.SPECTATOR);
                }
            }
        }
    }

    @EventHandler
    public static void onDropItem(PlayerDropItemEvent e) {
        if(slimekourStarted) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public static void onStickUse(EntityDamageByEntityEvent e) {
        /*
        if(((Player)e.getDamager()).getInventory().getItemInMainHand().equals(Items.kbstick) || ((Player)e.getDamager()).getInventory().getItemInOffHand().equals(Items.kbstick)) {

        } else {
            e.setCancelled(true);
        }

         */
    }

    public static void stickCooldown() {
        BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
    }

    public static void countdown() {
        countdowntime = 300;
        BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
        countdowntaskID = scheduler.scheduleSyncRepeatingTask((Plugin) mcsu, new Runnable() {
            @Override
            public void run() {
                if(countdowntime == 0) {
                    stopSlimekour();
                    for(Player players : Bukkit.getOnlinePlayers()) {
                        players.sendTitle(ChatColor.AQUA+"Game Over!","");
                    }
                    Bukkit.getScheduler().cancelTask(countdowntaskID);
                    return;
                }
                if(countdowntime == 300) {
                    String[] msgs = {
                            ChatColor.RED+"5 minutes remaining!"
                    };
                    new Announcement(msgs);
                }
                if(countdowntime == 60) {
                    String[] msgs = {
                            ChatColor.RED+"1 minute remaining!"
                    };
                    new Announcement(msgs);
                }
                if(countdowntime == 30) {
                    String[] msgs = {
                            ChatColor.RED+"30 seconds remaining!"
                    };
                    new Announcement(msgs);
                }
                if(time % 1 == 0 && !slimekourStarted && time <= 10) {
                    for (Player players : Bukkit.getOnlinePlayers()) {
                        players.sendTitle(ChatColor.RED+""+time, "", 1, 20, 1);
                    }
                }
                countdowntime = countdowntime - 1;
            }
        }, 0L, 20L);
    }

}
