package com.cloud.mcsu.minigames;

import com.cloud.mcsu.MCSU;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Rotatable;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scoreboard.Team;

public class CaptureTheFlag implements Listener, CommandExecutor {

    public static Player player;
    public static World world;
    public static int taskID;
    public static int time;
    public static MCSU mcsu = MCSU.getPlugin(MCSU.class);
    public static String winningteam1;
    public static String winningteam2;
    public static boolean ctfStarted;
    public static int round;
    public static int winningteam1wins;
    public static int winningteam2wins;

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can use that command!");
            return true;
        }
        player = (Player) sender;
        if(cmd.getName().equalsIgnoreCase("ctf") && player.isOp()) {
            if(args.length >= 2) {
                winningteam1 = args[0];
                winningteam2 = args[1];
                world = player.getWorld();
                if(Bukkit.getScheduler().isCurrentlyRunning(taskID)) {
                    stopTimer();
                }
                setTimer(15);
                startTimer();
            } else {
                player.sendMessage(ChatColor.RED+"Invalid command.");
            }
            round = 1;
            winningteam1wins = 0;
            winningteam2wins = 0;
        }
        if(cmd.getName().equalsIgnoreCase("stopctf") && player.isOp()) {
            world.setPVP(true);
            stopTimer();
            player.sendMessage(ChatColor.AQUA+"Stopping Capture the Flag!");
            ctfStarted = false;
        }
        return true;
    }

    public static void ctfCommand(String team1, String team2) {
        winningteam1 = team1;
        winningteam2 = team2;
        ctfStarted = false;
        world = player.getWorld();
        if(Bukkit.getScheduler().isCurrentlyRunning(taskID)) {
            stopTimer();
        }
        setTimer(15);
        startTimer();
        round = 1;
        winningteam1wins = 0;
        winningteam2wins = 0;
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
                    stopTimer();
                    ctfStart(winningteam1,winningteam2);
                }
                if(time % 1 == 0 && time <= 10 && time > 0) {
                    for (Player players : Bukkit.getOnlinePlayers()) {
                        players.sendTitle(""+time, "", 1, 20, 1);
                        players.playSound(players.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL,1,1);
                    }
                }
                if(time == 15) {
                    MCSU.currentgame = "Capture the Flag";
                    MCSU.gameround = 1;
                    for (Player players : Bukkit.getOnlinePlayers()) {
                        players.sendTitle("Capture the Flag starting in "+time+" seconds!", "", 1, 20, 1);
                        players.sendMessage(ChatColor.AQUA+"Capture the Flag Starting in "+time+" seconds!");
                        players.playSound(players.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL,1,1);
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
                    Block hometeam1banner = world.getBlockAt(1186,5,-847);
                    Block hometeam2banner = world.getBlockAt(1232,5,-851);
                    world.getBlockAt(1227,5,-849).setType(Material.BARRIER);
                    world.getBlockAt(1227,6,-849).setType(Material.BARRIER);
                    world.getBlockAt(1237,5,-849).setType(Material.BARRIER);
                    world.getBlockAt(1237,6,-849).setType(Material.BARRIER);
                    world.getBlockAt(1191,5,-849).setType(Material.BARRIER);
                    world.getBlockAt(1191,6,-849).setType(Material.BARRIER);
                    world.getBlockAt(1181,5,-849).setType(Material.BARRIER);
                    world.getBlockAt(1181,6,-849).setType(Material.BARRIER);
                    /*
                    Rotatable hometeam1data = (Rotatable) hometeam1banner.getBlockData();
                    hometeam1data.setRotation(BlockFace.NORTH);
                    hometeam1banner.setBlockData(hometeam1data);
                    Rotatable hometeam2data = (Rotatable) hometeam2banner.getBlockData();
                    hometeam2data.setRotation(BlockFace.SOUTH);
                    hometeam2banner.setBlockData(hometeam2data);

                     */
                    String team1 = winningteam1;
                    String team2 = winningteam2;
                    for(Player players : Bukkit.getOnlinePlayers()) {
                        Team team = players.getScoreboard().getPlayerTeam(players);
                        String teamname = team.getName();
                        if(team1.equalsIgnoreCase("Blue")) {
                            hometeam1banner.setType(Material.BLUE_BANNER);
                            world.getBlockAt(1186,4,-848).setType(Material.BLUE_CONCRETE);
                            world.getBlockAt(1187,4,-847).setType(Material.BLUE_CONCRETE);
                            world.getBlockAt(1186,4,-846).setType(Material.BLUE_CONCRETE);
                            world.getBlockAt(1185,4,-847).setType(Material.BLUE_CONCRETE);
                            world.getBlockAt(1232,4,-848).setType(Material.BLUE_CONCRETE);
                            world.getBlockAt(1233,4,-847).setType(Material.BLUE_CONCRETE);
                            world.getBlockAt(1232,4,-846).setType(Material.BLUE_CONCRETE);
                            world.getBlockAt(1231,4,-847).setType(Material.BLUE_CONCRETE);

                        } else if(team1.equalsIgnoreCase("Red")) {
                            hometeam1banner.setType(Material.RED_BANNER);
                            world.getBlockAt(1186,4,-848).setType(Material.RED_CONCRETE);
                            world.getBlockAt(1187,4,-847).setType(Material.RED_CONCRETE);
                            world.getBlockAt(1186,4,-846).setType(Material.RED_CONCRETE);
                            world.getBlockAt(1185,4,-847).setType(Material.RED_CONCRETE);
                            world.getBlockAt(1232,4,-848).setType(Material.RED_CONCRETE);
                            world.getBlockAt(1233,4,-847).setType(Material.RED_CONCRETE);
                            world.getBlockAt(1232,4,-846).setType(Material.RED_CONCRETE);
                            world.getBlockAt(1231,4,-847).setType(Material.RED_CONCRETE);


                        } else if(team1.equalsIgnoreCase("Green")) {
                            hometeam1banner.setType(Material.GREEN_BANNER);
                            world.getBlockAt(1186,4,-848).setType(Material.GREEN_CONCRETE);
                            world.getBlockAt(1187,4,-847).setType(Material.GREEN_CONCRETE);
                            world.getBlockAt(1186,4,-846).setType(Material.GREEN_CONCRETE);
                            world.getBlockAt(1185,4,-847).setType(Material.GREEN_CONCRETE);
                            world.getBlockAt(1232,4,-848).setType(Material.GREEN_CONCRETE);
                            world.getBlockAt(1233,4,-847).setType(Material.GREEN_CONCRETE);
                            world.getBlockAt(1232,4,-846).setType(Material.GREEN_CONCRETE);
                            world.getBlockAt(1231,4,-847).setType(Material.GREEN_CONCRETE);

                        } else if(team1.equalsIgnoreCase("Yellow")) {
                            hometeam1banner.setType(Material.YELLOW_BANNER);
                            world.getBlockAt(1186,4,-848).setType(Material.YELLOW_CONCRETE);
                            world.getBlockAt(1187,4,-847).setType(Material.YELLOW_CONCRETE);
                            world.getBlockAt(1186,4,-846).setType(Material.YELLOW_CONCRETE);
                            world.getBlockAt(1185,4,-847).setType(Material.YELLOW_CONCRETE);
                            world.getBlockAt(1232,4,-848).setType(Material.YELLOW_CONCRETE);
                            world.getBlockAt(1233,4,-847).setType(Material.YELLOW_CONCRETE);
                            world.getBlockAt(1232,4,-846).setType(Material.YELLOW_CONCRETE);
                            world.getBlockAt(1231,4,-847).setType(Material.YELLOW_CONCRETE);


                        } else if(team1.equalsIgnoreCase("Aqua")) {
                            hometeam1banner.setType(Material.LIGHT_BLUE_BANNER);
                            world.getBlockAt(1186,4,-848).setType(Material.LIGHT_BLUE_CONCRETE);
                            world.getBlockAt(1187,4,-847).setType(Material.LIGHT_BLUE_CONCRETE);
                            world.getBlockAt(1186,4,-846).setType(Material.LIGHT_BLUE_CONCRETE);
                            world.getBlockAt(1185,4,-847).setType(Material.LIGHT_BLUE_CONCRETE);
                            world.getBlockAt(1232,4,-848).setType(Material.LIGHT_BLUE_CONCRETE);
                            world.getBlockAt(1233,4,-847).setType(Material.LIGHT_BLUE_CONCRETE);
                            world.getBlockAt(1232,4,-846).setType(Material.LIGHT_BLUE_CONCRETE);
                            world.getBlockAt(1231,4,-847).setType(Material.LIGHT_BLUE_CONCRETE);


                        } else if(team1.equalsIgnoreCase("Pink")) {
                            hometeam1banner.setType(Material.PINK_BANNER);
                            world.getBlockAt(1186,4,-848).setType(Material.PINK_CONCRETE);
                            world.getBlockAt(1187,4,-847).setType(Material.PINK_CONCRETE);
                            world.getBlockAt(1186,4,-846).setType(Material.PINK_CONCRETE);
                            world.getBlockAt(1185,4,-847).setType(Material.PINK_CONCRETE);
                            world.getBlockAt(1232,4,-848).setType(Material.PINK_CONCRETE);
                            world.getBlockAt(1233,4,-847).setType(Material.PINK_CONCRETE);
                            world.getBlockAt(1232,4,-846).setType(Material.PINK_CONCRETE);
                            world.getBlockAt(1231,4,-847).setType(Material.PINK_CONCRETE);

                        } else if(team1.equalsIgnoreCase("Grey")) {
                            hometeam1banner.setType(Material.GRAY_BANNER);
                            world.getBlockAt(1186,4,-848).setType(Material.GRAY_CONCRETE);
                            world.getBlockAt(1187,4,-847).setType(Material.GRAY_CONCRETE);
                            world.getBlockAt(1186,4,-846).setType(Material.GRAY_CONCRETE);
                            world.getBlockAt(1185,4,-847).setType(Material.GRAY_CONCRETE);
                            world.getBlockAt(1232,4,-848).setType(Material.GRAY_CONCRETE);
                            world.getBlockAt(1233,4,-847).setType(Material.GRAY_CONCRETE);
                            world.getBlockAt(1232,4,-846).setType(Material.GRAY_CONCRETE);
                            world.getBlockAt(1231,4,-847).setType(Material.GRAY_CONCRETE);

                        } else if(team1.equalsIgnoreCase("White")) {
                            hometeam1banner.setType(Material.WHITE_BANNER);
                            world.getBlockAt(1186, 4, -848).setType(Material.WHITE_CONCRETE);
                            world.getBlockAt(1187, 4, -847).setType(Material.WHITE_CONCRETE);
                            world.getBlockAt(1186, 4, -846).setType(Material.WHITE_CONCRETE);
                            world.getBlockAt(1185, 4, -847).setType(Material.WHITE_CONCRETE);
                            world.getBlockAt(1232, 4, -848).setType(Material.WHITE_CONCRETE);
                            world.getBlockAt(1233, 4, -847).setType(Material.WHITE_CONCRETE);
                            world.getBlockAt(1232, 4, -846).setType(Material.WHITE_CONCRETE);
                            world.getBlockAt(1231, 4, -847).setType(Material.WHITE_CONCRETE);


                        }
                        if(team2.equalsIgnoreCase("Blue")) {
                            hometeam2banner.setType(Material.BLUE_BANNER);
                            world.getBlockAt(1232,4,-852).setType(Material.BLUE_CONCRETE);
                            world.getBlockAt(1233,4,-851).setType(Material.BLUE_CONCRETE);
                            world.getBlockAt(1232,4,-850).setType(Material.BLUE_CONCRETE);
                            world.getBlockAt(1231,4,-851).setType(Material.BLUE_CONCRETE);
                            world.getBlockAt(1186,4,-852).setType(Material.BLUE_CONCRETE);
                            world.getBlockAt(1187,4,-851).setType(Material.BLUE_CONCRETE);
                            world.getBlockAt(1186,4,-850).setType(Material.BLUE_CONCRETE);
                            world.getBlockAt(1185,4,-851).setType(Material.BLUE_CONCRETE);

                        } else if(team2.equalsIgnoreCase("Red")) {
                            hometeam2banner.setType(Material.RED_BANNER);
                            world.getBlockAt(1232,4,-852).setType(Material.RED_CONCRETE);
                            world.getBlockAt(1233,4,-851).setType(Material.RED_CONCRETE);
                            world.getBlockAt(1232,4,-850).setType(Material.RED_CONCRETE);
                            world.getBlockAt(1231,4,-851).setType(Material.RED_CONCRETE);
                            world.getBlockAt(1186,4,-852).setType(Material.RED_CONCRETE);
                            world.getBlockAt(1187,4,-851).setType(Material.RED_CONCRETE);
                            world.getBlockAt(1186,4,-850).setType(Material.RED_CONCRETE);
                            world.getBlockAt(1185,4,-851).setType(Material.RED_CONCRETE);

                        } else if(team2.equalsIgnoreCase("Green")) {
                            hometeam2banner.setType(Material.GREEN_BANNER);
                            world.getBlockAt(1232,4,-852).setType(Material.GREEN_CONCRETE);
                            world.getBlockAt(1233,4,-851).setType(Material.GREEN_CONCRETE);
                            world.getBlockAt(1232,4,-850).setType(Material.GREEN_CONCRETE);
                            world.getBlockAt(1231,4,-851).setType(Material.GREEN_CONCRETE);
                            world.getBlockAt(1186,4,-852).setType(Material.GREEN_CONCRETE);
                            world.getBlockAt(1187,4,-851).setType(Material.GREEN_CONCRETE);
                            world.getBlockAt(1186,4,-850).setType(Material.GREEN_CONCRETE);
                            world.getBlockAt(1185,4,-851).setType(Material.GREEN_CONCRETE);


                        } else if(team2.equalsIgnoreCase("Yellow")) {
                            hometeam2banner.setType(Material.YELLOW_BANNER);
                            world.getBlockAt(1232,4,-852).setType(Material.YELLOW_CONCRETE);
                            world.getBlockAt(1233,4,-851).setType(Material.YELLOW_CONCRETE);
                            world.getBlockAt(1232,4,-850).setType(Material.YELLOW_CONCRETE);
                            world.getBlockAt(1231,4,-851).setType(Material.YELLOW_CONCRETE);
                            world.getBlockAt(1186,4,-852).setType(Material.YELLOW_CONCRETE);
                            world.getBlockAt(1187,4,-851).setType(Material.YELLOW_CONCRETE);
                            world.getBlockAt(1186,4,-850).setType(Material.YELLOW_CONCRETE);
                            world.getBlockAt(1185,4,-851).setType(Material.YELLOW_CONCRETE);


                        } else if(team2.equalsIgnoreCase("Aqua")) {
                            hometeam2banner.setType(Material.LIGHT_BLUE_BANNER);
                            world.getBlockAt(1232,4,-852).setType(Material.LIGHT_BLUE_CONCRETE);
                            world.getBlockAt(1233,4,-851).setType(Material.LIGHT_BLUE_CONCRETE);
                            world.getBlockAt(1232,4,-850).setType(Material.LIGHT_BLUE_CONCRETE);
                            world.getBlockAt(1231,4,-851).setType(Material.LIGHT_BLUE_CONCRETE);
                            world.getBlockAt(1186,4,-852).setType(Material.LIGHT_BLUE_CONCRETE);
                            world.getBlockAt(1187,4,-851).setType(Material.LIGHT_BLUE_CONCRETE);
                            world.getBlockAt(1186,4,-850).setType(Material.LIGHT_BLUE_CONCRETE);
                            world.getBlockAt(1185,4,-851).setType(Material.LIGHT_BLUE_CONCRETE);


                        } else if(team2.equalsIgnoreCase("Pink")) {
                            hometeam2banner.setType(Material.PINK_BANNER);
                            world.getBlockAt(1232,4,-852).setType(Material.PINK_CONCRETE);
                            world.getBlockAt(1233,4,-851).setType(Material.PINK_CONCRETE);
                            world.getBlockAt(1232,4,-850).setType(Material.PINK_CONCRETE);
                            world.getBlockAt(1231,4,-851).setType(Material.PINK_CONCRETE);
                            world.getBlockAt(1186,4,-852).setType(Material.PINK_CONCRETE);
                            world.getBlockAt(1187,4,-851).setType(Material.PINK_CONCRETE);
                            world.getBlockAt(1186,4,-850).setType(Material.PINK_CONCRETE);
                            world.getBlockAt(1185,4,-851).setType(Material.PINK_CONCRETE);

                        } else if(team2.equalsIgnoreCase("Grey")) {
                            hometeam2banner.setType(Material.GRAY_BANNER);
                            world.getBlockAt(1232,4,-852).setType(Material.GRAY_CONCRETE);
                            world.getBlockAt(1233,4,-851).setType(Material.GRAY_CONCRETE);
                            world.getBlockAt(1232,4,-850).setType(Material.GRAY_CONCRETE);
                            world.getBlockAt(1231,4,-851).setType(Material.GRAY_CONCRETE);
                            world.getBlockAt(1186,4,-852).setType(Material.GRAY_CONCRETE);
                            world.getBlockAt(1187,4,-851).setType(Material.GRAY_CONCRETE);
                            world.getBlockAt(1186,4,-850).setType(Material.GRAY_CONCRETE);
                            world.getBlockAt(1185,4,-851).setType(Material.GRAY_CONCRETE);

                        } else if(team2.equalsIgnoreCase("White")) {
                            hometeam2banner.setType(Material.WHITE_BANNER);
                            world.getBlockAt(1232,4,-852).setType(Material.WHITE_CONCRETE);
                            world.getBlockAt(1233,4,-851).setType(Material.WHITE_CONCRETE);
                            world.getBlockAt(1232,4,-850).setType(Material.WHITE_CONCRETE);
                            world.getBlockAt(1231,4,-851).setType(Material.WHITE_CONCRETE);
                            world.getBlockAt(1186,4,-852).setType(Material.WHITE_CONCRETE);
                            world.getBlockAt(1187,4,-851).setType(Material.WHITE_CONCRETE);
                            world.getBlockAt(1186,4,-850).setType(Material.WHITE_CONCRETE);
                            world.getBlockAt(1185,4,-851).setType(Material.WHITE_CONCRETE);

                        }
                        if(winningteam1.equalsIgnoreCase(teamname)) {
                            players.teleport(new Location(world,1186.8,6,-848.5,-90,0));
                        } else if(winningteam2.equalsIgnoreCase(teamname)) {
                            players.teleport(new Location(world,1232.2,6,-848.5,90,0));
                        }
                    }
                }
                time = time - 1;
            }
        }, 0L, 20L);
    }

    public static void stopTimer() {
        Bukkit.getScheduler().cancelTask(taskID);
    }

    @EventHandler
    public static void onBreakBlock(BlockBreakEvent e) {
        if(ctfStarted) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public static void onPlaceBanner(BlockPlaceEvent e) {
        if(ctfStarted) {
            if(e.getBlock().getLocation().equals(new Location(world,1186,5,-851))) {
                if(winningteam1wins <= 2) {
                    winningteam1wins = winningteam1wins + 1;
                    for(Player players : Bukkit.getOnlinePlayers()) {
                        Team team = players.getScoreboard().getPlayerTeam(players);
                        String teamname = team.getName();
                        if(teamname.equalsIgnoreCase(winningteam1)) {
                            players.setGameMode(GameMode.SPECTATOR);
                        } else if(teamname.equalsIgnoreCase(winningteam2)) {
                            players.setGameMode(GameMode.SPECTATOR);
                        }
                        if(winningteam2.equalsIgnoreCase("Blue")) {
                            players.sendTitle("§9Blue Bears won the round!","");
                        } else if(winningteam2.equalsIgnoreCase("Red")) {
                            players.sendTitle("§cRed Reindeers won the round!","");
                        } else if(winningteam2.equalsIgnoreCase("Green")) {
                            players.sendTitle("§aGreen Geckos won the round!","");
                        } else if(winningteam2.equalsIgnoreCase("Yellow")) {
                            players.sendTitle("§eYellow Yetis won the round!","");
                        } else if(winningteam2.equalsIgnoreCase("Aqua")) {
                            players.sendTitle("§bAqua Axolotols won the round!","");
                        } else if(winningteam2.equalsIgnoreCase("Pink")) {
                            players.sendTitle("§dPink Pandas won the round!","");
                        } else if(winningteam2.equalsIgnoreCase("Grey")) {
                            players.sendTitle("§7Grey Gorillas won the round!","");
                        } else if(winningteam2.equalsIgnoreCase("White")) {
                            players.sendTitle("§fWhite Walruses won the round!","");
                        }
                    }
                }
                if(winningteam1wins == 3) {
                    for(Player players : Bukkit.getOnlinePlayers()) {
                        if(winningteam2.equalsIgnoreCase("Blue")) {
                            players.sendTitle("§9Blue Bears win MCSU!","");
                        } else if(winningteam2.equalsIgnoreCase("Red")) {
                            players.sendTitle("§cRed Reindeers win MCSU!","");
                        } else if(winningteam2.equalsIgnoreCase("Green")) {
                            players.sendTitle("§aGreen Geckos win MCSU!","");
                        } else if(winningteam2.equalsIgnoreCase("Yellow")) {
                            players.sendTitle("§eYellow Yetis win MCSU!!","");
                        } else if(winningteam2.equalsIgnoreCase("Aqua")) {
                            players.sendTitle("§bAqua Axolotols win MCSU!","");
                        } else if(winningteam2.equalsIgnoreCase("Pink")) {
                            players.sendTitle("§dPink Pandas win MCSU!","");
                        } else if(winningteam2.equalsIgnoreCase("Grey")) {
                            players.sendTitle("§7Grey Gorillas win MCSU!","");
                        } else if(winningteam2.equalsIgnoreCase("White")) {
                            players.sendTitle("§fWhite Walruses win MCSU!","");
                        }
                    }
                }
                round++;
                stopTimer();
                setTimer(20);
                startTimer();
            }
            if(e.getBlock().getLocation().equals(new Location(world,1232,5,-847))) {
                if(winningteam1wins <= 2) {
                    winningteam1wins = winningteam1wins + 1;
                    for(Player players : Bukkit.getOnlinePlayers()) {
                        Team team = players.getScoreboard().getPlayerTeam(players);
                        String teamname = team.getName();
                        if(teamname.equalsIgnoreCase(winningteam1)) {
                            players.setGameMode(GameMode.SPECTATOR);
                        } else if(teamname.equalsIgnoreCase(winningteam2)) {
                            players.setGameMode(GameMode.SPECTATOR);
                        }
                        if(winningteam2.equalsIgnoreCase("Blue")) {
                            players.sendTitle("§9Blue Bears won the round!","");
                        } else if(winningteam2.equalsIgnoreCase("Red")) {
                            players.sendTitle("§cRed Reindeers won the round!","");
                        } else if(winningteam2.equalsIgnoreCase("Green")) {
                            players.sendTitle("§aGreen Geckos won the round!","");
                        } else if(winningteam2.equalsIgnoreCase("Yellow")) {
                            players.sendTitle("§eYellow Yetis won the round!","");
                        } else if(winningteam2.equalsIgnoreCase("Aqua")) {
                            players.sendTitle("§bAqua Axolotols won the round!","");
                        } else if(winningteam2.equalsIgnoreCase("Pink")) {
                            players.sendTitle("§dPink Pandas won the round!","");
                        } else if(winningteam2.equalsIgnoreCase("Grey")) {
                            players.sendTitle("§7Grey Gorillas won the round!","");
                        } else if(winningteam2.equalsIgnoreCase("White")) {
                            players.sendTitle("§fWhite Walruses won the round!","");
                        }
                    }
                }
                if(winningteam1wins == 3) {
                    for(Player players : Bukkit.getOnlinePlayers()) {
                        if(winningteam2.equalsIgnoreCase("Blue")) {
                            players.sendTitle("§9Blue Bears win MCSU!","");
                        } else if(winningteam2.equalsIgnoreCase("Red")) {
                            players.sendTitle("§cRed Reindeers win MCSU!","");
                        } else if(winningteam2.equalsIgnoreCase("Green")) {
                            players.sendTitle("§aGreen Geckos win MCSU!","");
                        } else if(winningteam2.equalsIgnoreCase("Yellow")) {
                            players.sendTitle("§eYellow Yetis win MCSU!!","");
                        } else if(winningteam2.equalsIgnoreCase("Aqua")) {
                            players.sendTitle("§bAqua Axolotols win MCSU!","");
                        } else if(winningteam2.equalsIgnoreCase("Pink")) {
                            players.sendTitle("§dPink Pandas win MCSU!","");
                        } else if(winningteam2.equalsIgnoreCase("Grey")) {
                            players.sendTitle("§7Grey Gorillas win MCSU!","");
                        } else if(winningteam2.equalsIgnoreCase("White")) {
                            players.sendTitle("§fWhite Walruses win MCSU!","");
                        }
                    }
                }
                round++;
                stopTimer();
                setTimer(20);
                startTimer();
            }
        }
    }

    @EventHandler
    public static void onRightClickBanner(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        Team team = player.getScoreboard().getPlayerTeam(player);
        String teamname = team.getName();
        if(e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if(teamname.equalsIgnoreCase(winningteam1)) {
                
            }
            if(e.getClickedBlock().getType() == Material.BLUE_BANNER) {
                e.getClickedBlock().setType(Material.AIR);
                player.getInventory().addItem(new ItemStack(Material.BLUE_BANNER,1));
            } else if(e.getClickedBlock().getType() == Material.RED_BANNER) {
                e.getClickedBlock().setType(Material.AIR);
                player.getInventory().addItem(new ItemStack(Material.RED_BANNER,1));
            } else if(e.getClickedBlock().getType() == Material.GREEN_BANNER) {
                e.getClickedBlock().setType(Material.AIR);
                player.getInventory().addItem(new ItemStack(Material.GREEN_BANNER,1));
            } else if(e.getClickedBlock().getType() == Material.WHITE_BANNER) {
                e.getClickedBlock().setType(Material.AIR);
                player.getInventory().addItem(new ItemStack(Material.WHITE_BANNER,1));
            } else if(e.getClickedBlock().getType() == Material.YELLOW_BANNER) {
                e.getClickedBlock().setType(Material.AIR);
                player.getInventory().addItem(new ItemStack(Material.YELLOW_BANNER,1));
            } else if(e.getClickedBlock().getType() == Material.LIGHT_BLUE_BANNER) {
                e.getClickedBlock().setType(Material.AIR);
                player.getInventory().addItem(new ItemStack(Material.LIGHT_BLUE_BANNER,1));
            } else if(e.getClickedBlock().getType() == Material.GRAY_BANNER) {
                e.getClickedBlock().setType(Material.AIR);
                player.getInventory().addItem(new ItemStack(Material.GRAY_BANNER,1));
            } else if(e.getClickedBlock().getType() == Material.PINK_BANNER) {
                e.getClickedBlock().setType(Material.AIR);
                player.getInventory().addItem(new ItemStack(Material.PINK_BANNER,1));
            }
        }
    }

    public static void ctfStart(String team1, String team2) {
        world.getBlockAt(1227,5,-849).setType(Material.AIR);
        world.getBlockAt(1227,6,-849).setType(Material.AIR);
        world.getBlockAt(1237,5,-849).setType(Material.AIR);
        world.getBlockAt(1237,6,-849).setType(Material.AIR);
        world.getBlockAt(1191,5,-849).setType(Material.AIR);
        world.getBlockAt(1191,6,-849).setType(Material.AIR);
        world.getBlockAt(1181,5,-849).setType(Material.AIR);
        world.getBlockAt(1181,6,-849).setType(Material.AIR);
        ctfStarted = true;
        for (Player players : Bukkit.getOnlinePlayers()) {
            world.setPVP(true);
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
        }
        for(Player players : Bukkit.getOnlinePlayers()) {
            players.sendTitle("§lFight!","");
            Team team = players.getScoreboard().getPlayerTeam(players);
            String teamname = team.getName();
            if(team1.equalsIgnoreCase(teamname)) {
                players.teleport(new Location(world,1186.8,6,-848.5,-90,0));
            }
            if(team1.equalsIgnoreCase("Blue")) {

                // Items
                players.getInventory().setItem(0,new ItemStack(Material.STONE_SWORD,1));
                players.getInventory().setItem(1,new ItemStack(Material.BOW,1));
                players.getInventory().setItem(2,new ItemStack(Material.ARROW,6));
                players.getInventory().setItemInOffHand(new ItemStack(Material.CROSSBOW,1));
                ItemStack chestplate = new ItemStack(Material.LEATHER_CHESTPLATE,1);
                LeatherArmorMeta meta = (LeatherArmorMeta) chestplate.getItemMeta();
                meta.setColor(Color.BLUE);
                chestplate.setItemMeta(meta);
                players.getInventory().setChestplate(chestplate);
            } else if(team1.equalsIgnoreCase("Red")) {

                // Items
                players.getInventory().setItem(0,new ItemStack(Material.STONE_SWORD,1));
                players.getInventory().setItem(1,new ItemStack(Material.BOW,1));
                players.getInventory().setItem(2,new ItemStack(Material.ARROW,6));
                players.getInventory().setItemInOffHand(new ItemStack(Material.CROSSBOW,1));
                ItemStack chestplate = new ItemStack(Material.LEATHER_CHESTPLATE,1);
                LeatherArmorMeta meta = (LeatherArmorMeta) chestplate.getItemMeta();
                meta.setColor(Color.RED);
                chestplate.setItemMeta(meta);
                players.getInventory().setChestplate(chestplate);
            } else if(team1.equalsIgnoreCase("Green")) {

                // Items
                players.getInventory().setItem(0,new ItemStack(Material.STONE_SWORD,1));
                players.getInventory().setItem(1,new ItemStack(Material.BOW,1));
                players.getInventory().setItem(2,new ItemStack(Material.ARROW,6));
                players.getInventory().setItemInOffHand(new ItemStack(Material.CROSSBOW,1));
                ItemStack chestplate = new ItemStack(Material.LEATHER_CHESTPLATE,1);
                LeatherArmorMeta meta = (LeatherArmorMeta) chestplate.getItemMeta();
                meta.setColor(Color.GREEN);
                chestplate.setItemMeta(meta);
                players.getInventory().setChestplate(chestplate);
            } else if(team1.equalsIgnoreCase("Yellow")) {

                // Items
                players.getInventory().setItem(0,new ItemStack(Material.STONE_SWORD,1));
                players.getInventory().setItem(1,new ItemStack(Material.BOW,1));
                players.getInventory().setItem(2,new ItemStack(Material.ARROW,6));
                players.getInventory().setItemInOffHand(new ItemStack(Material.CROSSBOW,1));
                ItemStack chestplate = new ItemStack(Material.LEATHER_CHESTPLATE,1);
                LeatherArmorMeta meta = (LeatherArmorMeta) chestplate.getItemMeta();
                meta.setColor(Color.YELLOW);
                chestplate.setItemMeta(meta);
                players.getInventory().setChestplate(chestplate);
            } else if(team1.equalsIgnoreCase("Aqua")) {


                // Items
                players.getInventory().setItem(0,new ItemStack(Material.STONE_SWORD,1));
                players.getInventory().setItem(1,new ItemStack(Material.BOW,1));
                players.getInventory().setItem(2,new ItemStack(Material.ARROW,6));
                players.getInventory().setItemInOffHand(new ItemStack(Material.CROSSBOW,1));
                ItemStack chestplate = new ItemStack(Material.LEATHER_CHESTPLATE,1);
                LeatherArmorMeta meta = (LeatherArmorMeta) chestplate.getItemMeta();
                meta.setColor(Color.AQUA);
                chestplate.setItemMeta(meta);
                players.getInventory().setChestplate(chestplate);
            } else if(team1.equalsIgnoreCase("Pink")) {

                // Items
                players.getInventory().setItem(0,new ItemStack(Material.STONE_SWORD,1));
                players.getInventory().setItem(1,new ItemStack(Material.BOW,1));
                players.getInventory().setItem(2,new ItemStack(Material.ARROW,6));
                players.getInventory().setItemInOffHand(new ItemStack(Material.CROSSBOW,1));
                ItemStack chestplate = new ItemStack(Material.LEATHER_CHESTPLATE,1);
                LeatherArmorMeta meta = (LeatherArmorMeta) chestplate.getItemMeta();
                meta.setColor(Color.PURPLE);
                chestplate.setItemMeta(meta);
                players.getInventory().setChestplate(chestplate);
            } else if(team1.equalsIgnoreCase("Grey")) {

                // Items
                players.getInventory().setItem(0,new ItemStack(Material.STONE_SWORD,1));
                players.getInventory().setItem(1,new ItemStack(Material.BOW,1));
                players.getInventory().setItem(2,new ItemStack(Material.ARROW,6));
                players.getInventory().setItemInOffHand(new ItemStack(Material.CROSSBOW,1));
                ItemStack chestplate = new ItemStack(Material.LEATHER_CHESTPLATE,1);
                LeatherArmorMeta meta = (LeatherArmorMeta) chestplate.getItemMeta();
                meta.setColor(Color.GRAY);
                chestplate.setItemMeta(meta);
                players.getInventory().setChestplate(chestplate);
            } else if(team1.equalsIgnoreCase("White")) {

                // Items
                players.getInventory().setItem(0, new ItemStack(Material.STONE_SWORD, 1));
                players.getInventory().setItem(1, new ItemStack(Material.BOW, 1));
                players.getInventory().setItem(2, new ItemStack(Material.ARROW, 6));
                players.getInventory().setItemInOffHand(new ItemStack(Material.CROSSBOW, 1));
                ItemStack chestplate = new ItemStack(Material.LEATHER_CHESTPLATE, 1);
                LeatherArmorMeta meta = (LeatherArmorMeta) chestplate.getItemMeta();
                meta.setColor(Color.WHITE);
                chestplate.setItemMeta(meta);
                players.getInventory().setChestplate(chestplate);
            }
            if(team2.equalsIgnoreCase("Blue")) {

                // Items
                players.getInventory().setItem(0,new ItemStack(Material.STONE_SWORD,1));
                players.getInventory().setItem(1,new ItemStack(Material.BOW,1));
                players.getInventory().setItem(2,new ItemStack(Material.ARROW,6));
                players.getInventory().setItemInOffHand(new ItemStack(Material.CROSSBOW,1));
                ItemStack chestplate = new ItemStack(Material.LEATHER_CHESTPLATE,1);
                LeatherArmorMeta meta = (LeatherArmorMeta) chestplate.getItemMeta();
                meta.setColor(Color.BLUE);
                chestplate.setItemMeta(meta);
                players.getInventory().setChestplate(chestplate);
            } else if(team2.equalsIgnoreCase("Red")) {

                // Items
                players.getInventory().setItem(0,new ItemStack(Material.STONE_SWORD,1));
                players.getInventory().setItem(1,new ItemStack(Material.BOW,1));
                players.getInventory().setItem(2,new ItemStack(Material.ARROW,6));
                players.getInventory().setItemInOffHand(new ItemStack(Material.CROSSBOW,1));
                ItemStack chestplate = new ItemStack(Material.LEATHER_CHESTPLATE,1);
                LeatherArmorMeta meta = (LeatherArmorMeta) chestplate.getItemMeta();
                meta.setColor(Color.RED);
                chestplate.setItemMeta(meta);
                players.getInventory().setChestplate(chestplate);
            } else if(team2.equalsIgnoreCase("Green")) {

                // Items
                players.getInventory().setItem(0,new ItemStack(Material.STONE_SWORD,1));
                players.getInventory().setItem(1,new ItemStack(Material.BOW,1));
                players.getInventory().setItem(2,new ItemStack(Material.ARROW,6));
                players.getInventory().setItemInOffHand(new ItemStack(Material.CROSSBOW,1));
                ItemStack chestplate = new ItemStack(Material.LEATHER_CHESTPLATE,1);
                LeatherArmorMeta meta = (LeatherArmorMeta) chestplate.getItemMeta();
                meta.setColor(Color.GREEN);
                chestplate.setItemMeta(meta);
                players.getInventory().setChestplate(chestplate);
            } else if(team2.equalsIgnoreCase("Yellow")) {

                // Items
                players.getInventory().setItem(0,new ItemStack(Material.STONE_SWORD,1));
                players.getInventory().setItem(1,new ItemStack(Material.BOW,1));
                players.getInventory().setItem(2,new ItemStack(Material.ARROW,6));
                players.getInventory().setItemInOffHand(new ItemStack(Material.CROSSBOW,1));
                ItemStack chestplate = new ItemStack(Material.LEATHER_CHESTPLATE,1);
                LeatherArmorMeta meta = (LeatherArmorMeta) chestplate.getItemMeta();
                meta.setColor(Color.YELLOW);
                chestplate.setItemMeta(meta);
                players.getInventory().setChestplate(chestplate);
            } else if(team2.equalsIgnoreCase("Aqua")) {

                // Items
                players.getInventory().setItem(0,new ItemStack(Material.STONE_SWORD,1));
                players.getInventory().setItem(1,new ItemStack(Material.BOW,1));
                players.getInventory().setItem(2,new ItemStack(Material.ARROW,6));
                players.getInventory().setItemInOffHand(new ItemStack(Material.CROSSBOW,1));
                ItemStack chestplate = new ItemStack(Material.LEATHER_CHESTPLATE,1);
                LeatherArmorMeta meta = (LeatherArmorMeta) chestplate.getItemMeta();
                meta.setColor(Color.AQUA);
                chestplate.setItemMeta(meta);
                players.getInventory().setChestplate(chestplate);
            } else if(team2.equalsIgnoreCase("Pink")) {

                // Items
                players.getInventory().setItem(0,new ItemStack(Material.STONE_SWORD,1));
                players.getInventory().setItem(1,new ItemStack(Material.BOW,1));
                players.getInventory().setItem(2,new ItemStack(Material.ARROW,6));
                players.getInventory().setItemInOffHand(new ItemStack(Material.CROSSBOW,1));
                ItemStack chestplate = new ItemStack(Material.LEATHER_CHESTPLATE,1);
                LeatherArmorMeta meta = (LeatherArmorMeta) chestplate.getItemMeta();
                meta.setColor(Color.PURPLE);
                chestplate.setItemMeta(meta);
                players.getInventory().setChestplate(chestplate);
            } else if(team2.equalsIgnoreCase("Grey")) {
                // Items
                players.getInventory().setItem(0,new ItemStack(Material.STONE_SWORD,1));
                players.getInventory().setItem(1,new ItemStack(Material.BOW,1));
                players.getInventory().setItem(2,new ItemStack(Material.ARROW,6));
                players.getInventory().setItemInOffHand(new ItemStack(Material.CROSSBOW,1));
                ItemStack chestplate = new ItemStack(Material.LEATHER_CHESTPLATE,1);
                LeatherArmorMeta meta = (LeatherArmorMeta) chestplate.getItemMeta();
                meta.setColor(Color.GRAY);
                chestplate.setItemMeta(meta);
                players.getInventory().setChestplate(chestplate);
            } else if(team2.equalsIgnoreCase("White")) {
                // Items
                players.getInventory().setItem(0,new ItemStack(Material.STONE_SWORD,1));
                players.getInventory().setItem(1,new ItemStack(Material.BOW,1));
                players.getInventory().setItem(2,new ItemStack(Material.ARROW,6));
                players.getInventory().setItemInOffHand(new ItemStack(Material.CROSSBOW,1));
                ItemStack chestplate = new ItemStack(Material.LEATHER_CHESTPLATE,1);
                LeatherArmorMeta meta = (LeatherArmorMeta) chestplate.getItemMeta();
                meta.setColor(Color.WHITE);
                chestplate.setItemMeta(meta);
                players.getInventory().setChestplate(chestplate);
            }
        }
    }

}
