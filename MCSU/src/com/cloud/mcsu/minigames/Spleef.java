package com.cloud.mcsu.minigames;

import com.cloud.mcsu.MCSU;
import com.cloud.mcsu.announcements.Announcement;
import com.cloud.mcsu.config.Config;
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
import org.bukkit.event.block.BlockPlaceEvent;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Spleef implements CommandExecutor, Listener {

    public static Player player;
    public static World world;
    public static int time;
    public static int taskID;
    public static MCSU mcsu = MCSU.getPlugin(MCSU.class);
    public static boolean spleefStarted;
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
        if (cmd.getName().equalsIgnoreCase("spleef") && player.isOp()) {
            spleefCommand(player);
        }
        if(cmd.getName().equalsIgnoreCase("stopspleef") && player.isOp()) {
            stopSpleef();
        }
        return true;
    }

    public static void spleefCommand(Player p) {
        world = player.getWorld();
        player = p;
        if(Bukkit.getScheduler().isCurrentlyRunning(taskID)) {
            stopTimer();
        }
        spleefStarted = false;
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
        setSquare(Material.SNOW_BLOCK,new Location(world,2305,4,91),new Location(world,2344,4,52));
        setSquare(Material.SNOW_BLOCK,new Location(world,2305,9,91),new Location(world,2344,9,52));
    }

    public static void stopSpleef() {
        for (Player players : Bukkit.getOnlinePlayers()) {
            players.setInvulnerable(false);
        }
        world.setPVP(true);
        stopTimer();
        spleefStarted = false;
        deadteams = null;
        deadplayers = null;
        onlineteams = null;
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

    public static void setTimer(int amount) {
        time = amount;
    }

    public static void startTimer() {
        BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
        taskID = scheduler.scheduleSyncRepeatingTask((Plugin) mcsu, new Runnable() {
            @Override
            public void run() {
                if(time == 0) {
                    if(!spleefStarted) {
                        spleefStart();
                        setTimer(150);
                        startTimer();
                    }
                    return;
                }
                if(time % 1 == 0 && !spleefStarted && time <= 10) {
                    for (Player players : Bukkit.getOnlinePlayers()) {
                        players.sendTitle(""+time, "", 1, 20, 1);
                        players.playSound(players.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL,1,1);
                    }
                }
                if(time == 15 && !spleefStarted) {
                    world.setPVP(false);
                    MCSU.currentgame = "Spleef";
                    MCSU.gameround = 1;
                    for (Player players : Bukkit.getOnlinePlayers()) {
                        players.sendTitle("Spleef starting in "+time+" seconds!", "", 1, 20, 1);
                        players.sendMessage(ChatColor.AQUA+"Spleef Starting in "+time+" seconds!");
                        players.teleport(new Location(world,2325,50,72));
                        players.playSound(players.getLocation(), Sound.BLOCK_SNOW_FALL,1,1);
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
                            ChatColor.GRAY+"Spleef",
                            "",
                            ChatColor.WHITE+"Spleef people with your shovel and be the last one standing!",
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
        world.setPVP(false);
        stopTimer();
        spleefStarted = true;
        for (Player players : Bukkit.getOnlinePlayers()) {
            players.setInvulnerable(false);
            players.sendTitle("Go!", "", 1, 20, 1);
            players.playSound(players.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL,1,5);
            players.setGameMode(GameMode.SURVIVAL);
            players.getInventory().clear();
            players.setFoodLevel(20);
            players.setHealth(20);
            players.setFireTicks(1);
            players.setLevel(0);
            players.setExp(0);
            for (PotionEffect effect : players.getActivePotionEffects()) {
                players.removePotionEffect(effect.getType());
            }
            ItemStack spleefer = new ItemStack(Material.NETHERITE_SHOVEL,1);
            spleefer.addEnchantment(Enchantment.DIG_SPEED,5);
            ItemMeta meta = spleefer.getItemMeta();
            meta.setDisplayName(ChatColor.GOLD+"Spleefer");
            meta.setUnbreakable(true);
            spleefer.setItemMeta(meta);
            players.getInventory().addItem(spleefer);
            Team team = players.getScoreboard().getPlayerTeam(players);
            String teamname = team.getName();
            if(teamname.equals("Blue")) {
                players.teleport(new Location(world,2307,10,90));
            }
            if(teamname.equals("Pink")) {
                players.teleport(new Location(world,2324,10, 90));
            }
            if(teamname.equals("Yellow")) {
                players.teleport(new Location(world,2343,10,90));
            }
            if(teamname.equals("Green")) {
                players.teleport(new Location(world,2343,10,54));
            }
            if(teamname.equals("Aqua")) {
                players.teleport(new Location(world,2343,10,71));
            }
            if(teamname.equals("Red")) {
                players.teleport(new Location(world,2324,10,54));
            }
            if(teamname.equals("Grey")) {
                players.teleport(new Location(world,2307,10,54));
            }
            if(teamname.equals("White")) {
                players.teleport(new Location(world,2307,10,71));
            }
        }
    }

    @EventHandler
    public static void onDeath(PlayerDeathEvent e) {
        if(spleefStarted) {
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
            }
            int teamsleftcount = onlineteams.size() - deadteams.size();
            if (teamsleftcount == 1) {
                if (!deadteams.contains("Blue") && onlineteams.contains("Blue")) {
                    Bukkit.broadcastMessage(ChatColor.BLUE + "Blue Bears win Spleef!");
                    for(Player players : Bukkit.getOnlinePlayers()) {
                        players.sendTitle("§1Blue Bears win Spleef!", "");
                    }
                    int points = MCSU.bluepoints + 175;
                    Config.get().set("Points.BluePoints", Integer.toString(points));
                    MCSU.getPoints();
                } else if (!deadteams.contains("Red") && onlineteams.contains("Red")) {
                    Bukkit.broadcastMessage(ChatColor.RED + "Red Reindeers win Spleef!");
                    for(Player players : Bukkit.getOnlinePlayers()) {
                        players.sendTitle("§cRed Reindeers win Spleef!", "");
                    }
                    int points = MCSU.redpoints + 175;
                    Config.get().set("Points.RedPoints", Integer.toString(points));
                    MCSU.getPoints();
                } else if (!deadteams.contains("Green") && onlineteams.contains("Green")) {
                    Bukkit.broadcastMessage(ChatColor.GREEN + "Green Geckos win Spleef!");
                    for(Player players : Bukkit.getOnlinePlayers()) {
                        players.sendTitle("§aGreen Geckos win Spleef!", "");
                    }
                    int points = MCSU.greenpoints + 175;
                    Config.get().set("Points.GreenPoints", Integer.toString(points));
                    MCSU.getPoints();
                } else if (!deadteams.contains("Yellow") && onlineteams.contains("Yellow")) {
                    Bukkit.broadcastMessage(ChatColor.YELLOW + "Yellow Yetis win Spleef!");
                    for(Player players : Bukkit.getOnlinePlayers()) {
                        players.sendTitle("§eYellow Yetis win Spleef!", "");
                    }
                    int points = MCSU.yellowpoints + 175;
                    Config.get().set("Points.YellowPoints", Integer.toString(points));
                    MCSU.getPoints();
                } else if (!deadteams.contains("Aqua") && onlineteams.contains("Aqua")) {
                    Bukkit.broadcastMessage(ChatColor.AQUA + "Aqua Axolotols win Spleef!");
                    for(Player players : Bukkit.getOnlinePlayers()) {
                        players.sendTitle("§bAqua Axolotols win Spleef!", "");
                    }
                    int points = MCSU.aquapoints + 175;
                    Config.get().set("Points.AquaPoints", Integer.toString(points));
                    MCSU.getPoints();
                } else if (!deadteams.contains("Pink") && onlineteams.contains("Pink")) {
                    Bukkit.broadcastMessage(ChatColor.LIGHT_PURPLE + "Pink Pandas win Spleef!");
                    for(Player players : Bukkit.getOnlinePlayers()) {
                        players.sendTitle("§dPink Pandas win Spleef!", "");
                    }
                    int points = MCSU.pinkpoints + 175;
                    Config.get().set("Points.PinkPoints", Integer.toString(points));
                    MCSU.getPoints();
                } else if (!deadteams.contains("Grey") && onlineteams.contains("Grey")) {
                    for(Player players : Bukkit.getOnlinePlayers()) {
                        players.sendTitle("§7Grey Gorillas win Spleef!", "");
                    }
                    Bukkit.broadcastMessage(ChatColor.GRAY + "Grey Gorillas win Spleef!");
                    int points = MCSU.greypoints + 175;
                    Config.get().set("Points.GreyPoints", Integer.toString(points));
                    MCSU.getPoints();
                } else if (!deadteams.contains("White") && onlineteams.contains("White")) {
                    for(Player players : Bukkit.getOnlinePlayers()) {
                        players.sendTitle("§fWhite Walruses win Spleef!", "");
                    }
                    Bukkit.broadcastMessage(ChatColor.WHITE + "White Walruses win Spleef!");
                    int points = MCSU.whitepoints + 175;
                    Config.get().set("Points.WhitePoints", Integer.toString(points));
                    MCSU.getPoints();
                }
                for (Player players : Bukkit.getOnlinePlayers()) {
                    if(!deadplayers.contains(players)) {
                        players.setGameMode(GameMode.ADVENTURE);
                    }
                    players.setInvulnerable(true);
                }
                stopSpleef();
            }
        }
    }

    @EventHandler
    public static void onPlaceBlock(BlockPlaceEvent e) {
        if(spleefStarted) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public static void onBreakSnow(BlockBreakEvent e) {
        if(spleefStarted) {
            e.setDropItems(false);
        }
    }

    @EventHandler
    public static void onFallDmg(EntityDamageEvent e) {
        if(spleefStarted) {
            if(e.getCause() == EntityDamageEvent.DamageCause.FALL) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public static void onTakeHunger(FoodLevelChangeEvent e) {
        if(spleefStarted) {
            e.setCancelled(true);
        }
    }

}
