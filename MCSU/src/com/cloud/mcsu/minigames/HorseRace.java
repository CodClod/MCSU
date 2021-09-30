package com.cloud.mcsu.minigames;

import com.cloud.mcsu.MCSU;
import com.cloud.mcsu.announcements.Announcement;
import com.cloud.mcsu.config.Config;
import com.cloud.mcsu.gui.GUI;
import com.cloud.mcsu.items.Items;
import com.sk89q.worldedit.world.entity.EntityType;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.type.Gate;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.vehicle.VehicleExitEvent;
import org.bukkit.event.vehicle.VehicleMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scoreboard.Team;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class HorseRace implements CommandExecutor, Listener {

    public static Player player;
    public static int taskID;
    public static int stopwatchtaskID;
    public static int time;
    public static int stopwatchtime;
    public static MCSU mcsu = MCSU.getPlugin(MCSU.class);
    public static World world;
    public static ArrayList<Player> finishers = new ArrayList<Player>();
    public static HashMap<Player, Integer> laps = new HashMap<Player, Integer>();
    public static HashMap<Player, Boolean> lapallowed = new HashMap<Player, Boolean>();
    public static String fancytimer;
    public static int[] placementpoints = {
            350,
            300,
            270,
            250,
            220,
            200,
            150,
            140,
            130,
            120,
            110,
            100,
            90,
            80,
            70,
            60
    };
    public static Boolean horseraceStarted = false;

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can use that command!");
            return true;
        }
        player = (Player) sender;
        if (cmd.getName().equalsIgnoreCase("horserace") && player.isOp()) {
            horseraceCommand(player);
        }
        if(cmd.getName().equalsIgnoreCase("stophorserace") && player.isOp()) {
            stopHorseRace();
        }
        return true;
    }

    public static void stopHorseRace() {
        stopTimer();
        stopstopwatch();
        finishers = null;
        finishers = new ArrayList<Player>();
        horseraceStarted = false;
        List<Entity> entList = world.getEntities();//get all entities in the world
        for(Entity current : entList) {//loop through the list
            if (current instanceof Horse) {//make sure we aren't deleting mobs/players
                current.remove();//remove it
            }
        }
        for (Player players : Bukkit.getOnlinePlayers()) {
            players.setInvulnerable(false);
        }
    }

    public static void horseraceCommand(Player p) {
        world = player.getWorld();
        List<Entity> entList = world.getEntities();//get all entities in the world
        for(Entity current : entList) {//loop through the list
            if (current instanceof Item) {//make sure we aren't deleting mobs/players
                current.remove();//remove it
            }
            if (current instanceof Horse) {
                current.remove();
            }
        }
        if(Bukkit.getScheduler().isCurrentlyRunning(taskID)) {
            stopTimer();
        }
        stopstopwatch();
        setTimer(15);
        startTimer();
        player = p;
        for (Player players : Bukkit.getOnlinePlayers()) {
            world.setPVP(false);
            players.teleport(new Location(world,-31.5,4,-614.5));
            players.setGameMode(GameMode.ADVENTURE);
            players.getInventory().clear();
            players.setFoodLevel(20);
            players.setHealth(20);
            players.setFireTicks(1);
            players.setLevel(0);
            for (PotionEffect effect : player.getActivePotionEffects()) {
                players.removePotionEffect(effect.getType());
            }
        }
        horseraceStarted = false;
        finishers = null;
        laps = null;
        lapallowed = null;
        finishers = new ArrayList<Player>();
        laps = new HashMap<Player, Integer>();
        lapallowed = new HashMap<Player, Boolean>();
        for(Player players : Bukkit.getOnlinePlayers()) {
            laps.put(players,0);
        }
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
                    stopwatchtime = 0;
                    stopwatch();
                    for (Player players : Bukkit.getOnlinePlayers()) {
                        players.sendTitle("Go!", "", 1, 20, 1);
                        players.playSound(players.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL,1,5);
                    }
                    horseStart();
                    stopTimer();
                    horseraceStarted = true;
                    return;
                }
                if(time % 1 == 0 && time <= 10) {
                    for (Player players : Bukkit.getOnlinePlayers()) {
                        players.sendTitle(""+time, "", 1, 20, 1);
                        players.playSound(players.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL,1,1);
                    }
                }
                if(time == 15) {
                    MCSU.currentgame = "Horse Race";
                    MCSU.gameround = 1;
                    for (Player players : Bukkit.getOnlinePlayers()) {
                        players.sendTitle("Horse Race starting in "+time+" seconds!", "", 1, 20, 1);
                        players.sendMessage(ChatColor.AQUA+"Horse Race Starting in "+time+" seconds!");
                        players.playSound(players.getLocation(), Sound.ENTITY_HORSE_GALLOP,1,1);
                        MCSU.createBoard(players);
                    }
                    // Top Barriers
                    for(int i = 0; i < 3; i++) {
                        world.getBlockAt(new Location(world,-39+i,6,-617)).setType(Material.BARRIER);
                    }
                    for(int i = 0; i < 3; i++) {
                        world.getBlockAt(new Location(world,-35+i,6,-617)).setType(Material.BARRIER);
                    }
                    for(int i = 0; i < 3; i++) {
                        world.getBlockAt(new Location(world, -31 + i, 6, -617)).setType(Material.BARRIER);
                    }
                    for(int i = 0; i < 3; i++) {
                        world.getBlockAt(new Location(world,-27+i,6,-617)).setType(Material.BARRIER);
                    }
                    // Spruce Fence Gates
                    for(int i = 0; i < 16; i = i + 2) {
                        Block fencegate = world.getBlockAt(new Location(world,-39+i,5,-617));
                        fencegate.setType(Material.SPRUCE_FENCE_GATE);
                        Gate gate = (Gate) fencegate.getBlockData();
                        gate.setInWall(true);
                        fencegate.setBlockData(gate);
                    }
                    // Middle Spruce Fence
                    for(int i = 0; i < 16; i = i + 4) {
                        world.getBlockAt(new Location(world,-38+i,5,-617)).setType(Material.SPRUCE_FENCE);
                        world.getBlockAt(new Location(world,-38+i,5,-617));
                    }
                    // Bottom Barriers
                    for(int i = 0; i < 16; i = i + 2) {
                        world.getBlockAt(new Location(world,-39+i,4,-617)).setType(Material.BARRIER);
                    }
                    // Bottom Spruce Fence
                    for(int i = 0; i < 16; i = i + 4) {
                        world.getBlockAt(new Location(world,-38+i,4,-617)).setType(Material.SPRUCE_FENCE);
                    }
                    String[] msgs = {
                            ChatColor.GOLD+"Horse Race",
                            "",
                            ChatColor.WHITE+"Race across the map using your horse and finish every lap to win!",
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

    public static void stopwatch() {
        BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
        stopwatchtaskID = scheduler.scheduleSyncRepeatingTask((Plugin) mcsu, new Runnable() {
            @Override
            public void run() {
                if(horseraceStarted) {
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
                            players.spigot().sendMessage(ChatMessageType.ACTION_BAR,new TextComponent("Lap "+laps.get(players)+"/3 Time: "+fancytimer));
                        } else {
                            players.spigot().sendMessage(ChatMessageType.ACTION_BAR,new TextComponent("Finished Horse Race!"));
                        }
                    }
                }
            }
        }, 0L, 20L);
    }

    public static void stopstopwatch() {
        Bukkit.getScheduler().cancelTask(stopwatchtaskID);
    }

    public static void horseStart() {
        for (Player players : Bukkit.getOnlinePlayers()) {
            players.setInvulnerable(true);
            Horse horse = (Horse) world.spawn(players.getLocation(), Horse.class);
            horse.setCustomName(players.getName() + "'s Horse");
            horse.setJumpStrength(0.8);
            horse.setTamed(true);
            horse.getInventory().setSaddle(new ItemStack(Material.SADDLE,1));
            horse.setAdult();
            horse.setInvulnerable(true);
            horse.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(0.25);
            horse.addPassenger(players);
        }
        for(int i = 0; i < 3; i++) {
            world.getBlockAt(new Location(world,-39+i,6,-617)).setType(Material.AIR);
            world.getBlockAt(new Location(world,-35+i,6,-617)).setType(Material.AIR);
            world.getBlockAt(new Location(world,-31+i,6,-617)).setType(Material.AIR);
            world.getBlockAt(new Location(world,-27+i,6,-617)).setType(Material.AIR);
        }
        for(int i = 0; i < 3; i++) {
            world.getBlockAt(new Location(world,-39+i,5,-617)).setType(Material.AIR);
            world.getBlockAt(new Location(world,-35+i,5,-617)).setType(Material.AIR);
            world.getBlockAt(new Location(world,-31+i,5,-617)).setType(Material.AIR);
            world.getBlockAt(new Location(world,-27+i,5,-617)).setType(Material.AIR);
        }
        for(int i = 0; i < 3; i++) {
            world.getBlockAt(new Location(world,-39+i,4,-617)).setType(Material.AIR);
            world.getBlockAt(new Location(world,-35+i,4,-617)).setType(Material.AIR);
            world.getBlockAt(new Location(world,-31+i,4,-617)).setType(Material.AIR);
            world.getBlockAt(new Location(world,-27+i,4,-617)).setType(Material.AIR);
        }
    }

    @EventHandler
    public static void onHorseExit(VehicleExitEvent e) {
        if(e.getVehicle() instanceof Horse) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public static void onHorseMove(PlayerMoveEvent e) {
        Player horseplayer = e.getPlayer();
        Location l = horseplayer.getLocation();
        l.add(0, -1, 0);
        Block b = l.getBlock();
        if(b.getType() == Material.GRAY_STAINED_GLASS) {
            horseplayer.getVehicle().removePassenger(horseplayer);
            horseplayer.getVehicle().remove();
            horseplayer.teleport(new Location(world,-114.5,6,-1015.5,180,0));
            Horse horse = (Horse) world.spawn(horseplayer.getLocation(), Horse.class);
            horse.setCustomName(horseplayer.getName() + "'s Horse");
            horse.setJumpStrength(0.8);
            horse.setTamed(true);
            horse.getInventory().setSaddle(new ItemStack(Material.SADDLE,1));
            horse.setAdult();
            horse.setInvulnerable(true);
            horse.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(0.25);
            horse.addPassenger(horseplayer);
            /*
            List<Entity> entList = world.getEntities();//get all entities in the world
            for(Entity current : entList) {//loop through the list
                if (current instanceof Horse) {//make sure we aren't deleting mobs/players
                    current.remove();//remove it
                }
            }
             */
        }
    }

    @EventHandler
    public static void onHorseRaceLapFinish(PlayerMoveEvent e) {
        if(horseraceStarted) {
            Player horseplayer = e.getPlayer();
            Location l = horseplayer.getLocation();
            l.add(0, -1, 0);
            Block b = l.getBlock();
            int playerlap = 0;
            Boolean allowedlap = false;
            if(!(laps.get(horseplayer) == null)) {
                playerlap = laps.get(horseplayer);
                allowedlap = lapallowed.get(horseplayer);
                if(b.getType() == Material.SMOOTH_STONE || b.getType() == Material.PRISMARINE_BRICKS || b.getType() == Material.PRISMARINE && b.getY() == 3) {
                    if(playerlap == 1 && !allowedlap) {
                        lapallowed.put(horseplayer,Boolean.TRUE);
                    }
                    if(playerlap == 2 && !allowedlap) {
                        lapallowed.put(horseplayer,Boolean.TRUE);
                    }
                    if(playerlap == 3 && !allowedlap) {
                        lapallowed.put(horseplayer,Boolean.TRUE);
                    }
                }
            }
            if(b.getType() == Material.POLISHED_ANDESITE && b.getY() == 3) {
                if(playerlap == 0) {
                    laps.put(horseplayer,1);
                    lapallowed.put(horseplayer,Boolean.FALSE);
                }
                if(playerlap == 1 && allowedlap) {
                    laps.put(horseplayer,2);
                    lapallowed.put(horseplayer,Boolean.FALSE);
                    for(Player players : Bukkit.getOnlinePlayers()) {
                        players.sendMessage(ChatColor.BLUE+horseplayer.getName()+ChatColor.WHITE+" finished Lap 1/3 in "+fancytimer);
                    }
                }
                if(playerlap == 2 && allowedlap) {
                    laps.put(horseplayer,3);
                    lapallowed.put(horseplayer,Boolean.FALSE);
                    for(Player players : Bukkit.getOnlinePlayers()) {
                        players.sendMessage(ChatColor.BLUE+horseplayer.getName()+ChatColor.WHITE+" finished Lap 2/3 in "+fancytimer);
                    }
                }
                if(playerlap == 3 && allowedlap) {
                    if(!finishers.contains(horseplayer)) {
                        finishers.add(horseplayer);
                        Team team = horseplayer.getScoreboard().getPlayerTeam(horseplayer);
                        String teamname = team.getName();
                        int place = finishers.indexOf(horseplayer)+1;
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
                        for(Player players : Bukkit.getOnlinePlayers()) {
                            players.sendMessage(ChatColor.BLUE+horseplayer.getName()+ChatColor.WHITE+" finished Lap 3/3 in "+fancytimer);
                        }
                        Bukkit.broadcastMessage(ChatColor.BLUE+horseplayer.getName()+ChatColor.WHITE+" just earned +"+ChatColor.GOLD+placementpoints[place-1]+ChatColor.WHITE+" points!");
                        horseplayer.playSound(horseplayer.getLocation(),Sound.ENTITY_ARROW_HIT_PLAYER,100,1);
                        horseplayer.playSound(horseplayer.getLocation(),Sound.ENTITY_GENERIC_EXPLODE,100,1);
                        Bukkit.broadcastMessage(ChatColor.BLUE+horseplayer.getName()+ChatColor.WHITE+" finished the horse race in "+ChatColor.GREEN+place+"."+ChatColor.WHITE+" with a time of "+fancytimer);
                    }
                    horseplayer.getVehicle().removePassenger(horseplayer);
                    horseplayer.getVehicle().remove();
                    horseplayer.setGameMode(GameMode.SPECTATOR);
                    if(finishers.size() == Bukkit.getOnlinePlayers().size()) {
                        stopHorseRace();
                        for(Player players : Bukkit.getOnlinePlayers()) {
                            players.sendTitle(ChatColor.AQUA+"Game Over!","");
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public static void onRightClickBlock(PlayerInteractEvent e) {
        if(horseraceStarted) {
            if(e.getAction() == Action.RIGHT_CLICK_BLOCK) {
                e.setCancelled(true);
            }
        }
    }


}
