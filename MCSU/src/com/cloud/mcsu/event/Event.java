package com.cloud.mcsu.event;

import com.cloud.mcsu.MCSU;
import com.cloud.mcsu.minigames.*;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;

public class Event implements CommandExecutor {

    public static int taskID;
    public static int time;
    public static MCSU mcsu = MCSU.getPlugin(MCSU.class);
    public static Player player;
    public static World world;
    public static boolean preevent;
    public static boolean pregame;
    public static int currentgame;

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can use that command!");
            return true;
        }
        player = (Player) sender;
        world = player.getWorld();
        if(cmd.getName().equalsIgnoreCase("event") && player.getName().equals("WaitWhosCandice")) {
            if(Bukkit.getScheduler().isCurrentlyRunning(taskID)) {
                stopTimer();
            }
            preevent = true;
            setTimer(20);
            startTimer();
        }
        return true;
    }

    public static void startEventGame() {
        if(currentgame == 1) {
            GangBeasts.gangbeastsCommand(player);
        }
        if(currentgame == 2) {
            Slimekour.slimekourCommand(player);
        }
        if(currentgame == 3) {
            SG.sgCommand(player);
        }
        if(currentgame == 4) {
            HorseRace.horseraceCommand(player);
        }
        if(currentgame == 5) {
            HideAndSeek.hideandseekCommand(player);
        }
        if(currentgame == 6) {
            Skybattle.skybattleCommand(player);
        }


        stopTimer();
    }

    public static void setTimer(int amount) {
        time = amount;
    }

    public static void startTimer() {
        BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
        taskID = scheduler.scheduleSyncRepeatingTask((Plugin) mcsu, new Runnable() {
            @Override
            public void run() {
                // Pre Event
                if(time == 0) {
                    if(preevent) {
                        preevent = false;
                        for(Player players : Bukkit.getOnlinePlayers()) {
                            players.sendTitle("§aMCSU Starting!","§7Get Ready!");
                        }
                        stopTimer();
                        setTimer(5);
                        startTimer();
                        pregame = true;
                        currentgame = 1;
                    } else {
                        startEventGame();
                    }
                    return;
                }
                if(time == 4 && pregame) {
                    for(Player players : Bukkit.getOnlinePlayers()) {
                        if(currentgame == 1)  {
                            players.sendMessage(ChatColor.GRAY+"The First game is"+ChatColor.LIGHT_PURPLE+" Gang Beasts");
                        } else if(currentgame == 2)  {
                            players.sendMessage(ChatColor.GRAY+"The Second game is"+ChatColor.GREEN+" Slimekour");
                        } else if(currentgame == 3)  {
                            players.sendMessage(ChatColor.GRAY+"The Third game is"+ChatColor.RED+" Survival Games");
                        } else if(currentgame == 4)  {
                            players.sendMessage(ChatColor.GRAY+"The Fourth game is"+ChatColor.BLUE+" Horse Race");
                        } else if(currentgame == 5)  {
                            players.sendMessage(ChatColor.GRAY+"The Fifth game is"+ChatColor.BLACK+" Hide and Seek");
                        } else if(currentgame == 6)  {
                            players.sendMessage(ChatColor.GRAY+"The Sixth game is"+ChatColor.GOLD+" Skybattle");
                        }
                    }
                }
                if(time == 3 && pregame) {
                    for(Player players : Bukkit.getOnlinePlayers()) {
                        players.sendMessage(ChatColor.GRAY+"Teleporting soon..");
                    }
                }
                if(time % 1 == 0 && time <= 10 && preevent) {
                    for (Player players : Bukkit.getOnlinePlayers()) {
                        players.sendTitle(""+time, "", 1, 20, 1);
                        players.playSound(players.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL,1,1);
                    }
                }
                if(time % 5 == 0 && preevent) {
                    Location firework1 = new Location(world,-110,5,145);
                    Location firework2 = new Location(world,-37,5,150);
                    Location firework3 = new Location(world,-51,27,149);
                    Location firework4 = new Location(world,-91,29,142);
                    spawnFireworks(firework1,1,Color.LIME);
                    spawnFireworks(firework2,2,Color.AQUA);
                    spawnFireworks(firework3,3,Color.BLUE);
                    spawnFireworks(firework4,4,Color.RED);
                    spawnFireworks(firework1,1,Color.PURPLE);
                    spawnFireworks(firework2,2,Color.GREEN);
                    spawnFireworks(firework3,3,Color.ORANGE);
                    spawnFireworks(firework4,4,Color.YELLOW);
                }
                if(time == 20 && preevent) {
                    world.setTime(15000);
                }
                time = time - 1;
            }
        }, 0L, 20L);
    }

    public static void spawnFireworks(Location location, int amount, Color color){
        Location loc = location;
        Firework fw = (Firework) loc.getWorld().spawnEntity(loc, EntityType.FIREWORK);
        FireworkMeta fwm = fw.getFireworkMeta();

        fwm.setPower(2);
        fwm.addEffect(FireworkEffect.builder().withColor(color).flicker(true).build());

        fw.setFireworkMeta(fwm);
        fw.detonate();

        for(int i = 0;i<amount; i++){
            Firework fw2 = (Firework) loc.getWorld().spawnEntity(loc, EntityType.FIREWORK);
            fw2.setFireworkMeta(fwm);
        }
    }

    public static void stopTimer() {
        Bukkit.getScheduler().cancelTask(taskID);
    }

}
