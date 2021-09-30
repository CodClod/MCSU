package com.cloud.mcsu.event;

import com.cloud.mcsu.MCSU;
import com.cloud.mcsu.config.Config;
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
import org.bukkit.scoreboard.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

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

    /*
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

     */

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
                        for(Player players : Bukkit.getOnlinePlayers()) {
                            MCSU.createBoard(players);
                        }
                    } else {
                        // startEventGame();
                    }
                    return;
                }
                if(time % 1 == 0) {
                    for(Player players : Bukkit.getOnlinePlayers()) {
                        createBoard(players);
                    }
                }
                /*
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
                            players.sendMessage(ChatColor.GRAY+"The Fifth game is"+ChatColor.GOLD+" Skybattle");
                        }
                    }
                }
                if(time == 3 && pregame) {
                    for(Player players : Bukkit.getOnlinePlayers()) {
                        players.sendMessage(ChatColor.GRAY+"Teleporting soon..");
                    }
                }

                 */
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

    public static void createBoard(Player player) {
        MCSU.manager = Bukkit.getScoreboardManager();
        MCSU.board = MCSU.manager.getNewScoreboard();
        Objective obj = MCSU.board.registerNewObjective("MCSU","dummy","§a§lMinecraft Sundays");
        obj.setDisplaySlot(DisplaySlot.SIDEBAR);
        Score online = obj.getScore(ChatColor.AQUA+" Online: "+ChatColor.WHITE+Bukkit.getOnlinePlayers().size());
        online.setScore(2);
        Score topblank = obj.getScore(" ");
        topblank.setScore(3);
        String fancytimer;
        int finish = time / 60;
        int remainder = time % 60;
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
        Score timeleft = obj.getScore(ChatColor.AQUA+" Time until MCSU: "+ChatColor.WHITE+fancytimer);
        timeleft.setScore(1);
        Score bottomblank = obj.getScore("");
        bottomblank.setScore(0);
        player.setScoreboard(MCSU.board);
        // Teams
        MCSU.blueteam = MCSU.board.registerNewTeam("Blue");
        MCSU.blueteam.setDisplayName(ChatColor.BLUE+"Blue Bears");
        MCSU.blueteam.setColor(ChatColor.BLUE);
        MCSU.blueteam.setPrefix(ChatColor.BLUE+"[Blue] ");
        MCSU.redteam = MCSU.board.registerNewTeam("Red");
        MCSU.redteam.setDisplayName(ChatColor.RED+"Red Reindeers");
        MCSU.redteam.setColor(ChatColor.RED);
        MCSU.redteam.setPrefix(ChatColor.RED+"[Red] ");
        MCSU.greenteam = MCSU.board.registerNewTeam("Green");
        MCSU.greenteam.setDisplayName(ChatColor.GREEN+"Green Gorillas");
        MCSU.greenteam.setColor(ChatColor.GREEN);
        MCSU.greenteam.setPrefix(ChatColor.GREEN+"[Green] ");
        MCSU.yellowteam = MCSU.board.registerNewTeam("Yellow");
        MCSU.yellowteam.setDisplayName(ChatColor.YELLOW+"Yellow Yetis");
        MCSU.yellowteam.setColor(ChatColor.YELLOW);
        MCSU.yellowteam.setPrefix(ChatColor.YELLOW+"[Yellow] ");
        MCSU.aquateam = MCSU.board.registerNewTeam("Aqua");
        MCSU.aquateam.setDisplayName(ChatColor.AQUA+"Aqua Axolotols");
        MCSU.aquateam.setColor(ChatColor.AQUA);
        MCSU.aquateam.setPrefix(ChatColor.AQUA+"[Aqua] ");
        MCSU.pinkteam = MCSU.board.registerNewTeam("Pink");
        MCSU.pinkteam.setDisplayName(ChatColor.LIGHT_PURPLE+"Pink Pandas");
        MCSU.pinkteam.setColor(ChatColor.LIGHT_PURPLE);
        MCSU.pinkteam.setPrefix(ChatColor.LIGHT_PURPLE+"[Pink] ");
        MCSU.greyteam = MCSU.board.registerNewTeam("Grey");
        MCSU.greyteam.setDisplayName(ChatColor.GRAY+"Grey Gorillas");
        MCSU.greyteam.setColor(ChatColor.GRAY);
        MCSU.greyteam.setPrefix(ChatColor.GRAY+"[Grey] ");
        MCSU.whiteteam = MCSU.board.registerNewTeam("White");
        MCSU.whiteteam.setDisplayName(ChatColor.WHITE+"White Walruses");
        MCSU.whiteteam.setColor(ChatColor.WHITE);
        MCSU.whiteteam.setPrefix(ChatColor.WHITE+"[White] ");

        // Adding Players
        MCSU.greenteam.addEntry("WaitWhosCandice");
        MCSU.aquateam.addEntry("axob");
        MCSU.blueteam.addEntry("AceyXS");
        MCSU.yellowteam.addEntry("robux_");
        MCSU.aquateam.addEntry("PogGamer");
        MCSU.greyteam.addEntry("Anonymous1252");
        MCSU.blueteam.addEntry("tunae");
        MCSU.whiteteam.addEntry("stanowar");
        MCSU.greenteam.addEntry("leaef");
        MCSU.redteam.addEntry("JackyWackers");
        MCSU.aquateam.addEntry("happygamer1977");
        MCSU.pinkteam.addEntry("CakeIsTasty");

    }

}
