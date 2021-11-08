package com.cloud.mcsu.commands;

import com.cloud.mcsu.MCSU;
import com.cloud.mcsu.config.Config;
import com.cloud.mcsu.event.Event;
import com.cloud.mcsu.gui.GUI;
import com.cloud.mcsu.items.Items;
import com.cloud.mcsu.worldreset.WorldManager;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.Random;

public class Commands implements CommandExecutor {

    public static Player player;
    public static MCSU mcsu = MCSU.getPlugin(MCSU.class);
    public static int current = 0;
    public static int winnertime = 0;
    public static int taskID = 0;

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can use that command!");
            return true;
        }   
        player = (Player) sender;
        if (cmd.getName().equalsIgnoreCase("roll")) {
            if(args.length >= 2) {
                int min = Integer.parseInt(args[0]);
                int max = Integer.parseInt(args[1]);
                int result = (int)Math.floor(Math.random()*(max-min+1)+min);
                player.sendMessage(Integer.toString(result));
            }
        }
        if(cmd.getName().equalsIgnoreCase("resetpoints") && player.isOp()) {
            Config.get().set("Points.BluePoints","0");
            Config.get().set("Points.RedPoints","0");
            Config.get().set("Points.GreenPoints","0");
            Config.get().set("Points.YellowPoints","0");
            Config.get().set("Points.AquaPoints","0");
            Config.get().set("Points.PinkPoints","0");
            Config.get().set("Points.GreyPoints","0");
            Config.get().set("Points.WhitePoints","0");
            MCSU.getPoints();
        }
        if(cmd.getName().equalsIgnoreCase("winner") && player.isOp()) {
            if(args.length >= 3) {
                player.getWorld().setTime(15000);
                winnertime = 0;
                String first = args[0];
                String second = args[1];
                String third = args[2];
                for(Player players : Bukkit.getOnlinePlayers()) {
                    players.getInventory().clear();
                }
                taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(mcsu, new Runnable() {
                    @Override
                    public void run() {
                        for(Player players : Bukkit.getOnlinePlayers()) {
                            players.sendTitle(player.getScoreboard().getTeam(first).getColor()+player.getScoreboard().getTeam(first).getDisplayName()+" win MCSU!",player.getScoreboard().getTeam(second).getColor()+"2. "+player.getScoreboard().getTeam(second).getDisplayName()+player.getScoreboard().getTeam(third).getColor()+" 3. "+player.getScoreboard().getTeam(third).getDisplayName(),0,100,0);
                        }
                        winnertime = winnertime + 5;
                        Location loc1 = new Location(player.getWorld(),-73.5,23,155.5);
                        Event.spawnFireworks(loc1,1,Color.AQUA);
                        Event.spawnFireworks(loc1,1,Color.WHITE);
                        Location loc2 = new Location(player.getWorld(),-67.5,23,156);
                        Event.spawnFireworks(loc2,1,Color.YELLOW);
                        Event.spawnFireworks(loc2,1,Color.WHITE);
                        Location loc3 = new Location(player.getWorld(),-80.5,23,156);
                        Event.spawnFireworks(loc3,1,Color.SILVER);
                        Event.spawnFireworks(loc3,1,Color.WHITE);
                        if(winnertime == 200) {
                            for(Player players : Bukkit.getOnlinePlayers()) {
                                players.teleport(new Location(player.getWorld(), MCSU.spawnx, MCSU.spawny, MCSU.spawnz, -180, 0));
                                players.sendTitle(ChatColor.AQUA+"MCSU Finished!",ChatColor.GRAY+"Thanks for playing!");
                            }
                            Bukkit.getScheduler().cancelTask(taskID);
                        }
                    }
                }, 0L,20L);
                for(String players : player.getScoreboard().getTeam(first).getEntries()) {
                    if(Bukkit.getPlayer(players).isOnline()) {
                        Bukkit.getPlayer(players).teleport(new Location(Bukkit.getPlayer(players).getWorld(),-73.5,13,144.3));
                    }
                }
            }
        }
        if(cmd.getName().equalsIgnoreCase("sb") && player.isOp()) {
            for(Player players : Bukkit.getOnlinePlayers()) {
                MCSU.createBoard(players);
            }
        }
        if(cmd.getName().equalsIgnoreCase("ace") && player.isOp()) {
            for(Player players : Bukkit.getOnlinePlayers()) {
                MCSU.createAceyRank(players);
            }
        }
        if(cmd.getName().equalsIgnoreCase("taco")) {
            if(args.length >= 2) {
                String pizzaplayer = args[0];
                int amount = Integer.parseInt(args[1]);
                Items.createTaco(amount);
                Bukkit.getServer().getPlayer(pizzaplayer).getInventory().addItem(Items.taco);
            }
        }
        if(cmd.getName().equalsIgnoreCase("cosmetic")) {
            BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
            int taskID = scheduler.scheduleSyncRepeatingTask((Plugin) mcsu, new Runnable() {
                @Override
                public void run() {
                    double var = 0;
                    var += Math.PI / 16;
                    Location first = player.getLocation().add(Math.cos(var), Math.sin(var) + 1, Math.sin(var));
                    Location second = player.getLocation().add(Math.cos(var + Math.PI), Math.sin(var) + 1, Math.sin(var + Math.PI));
                    player.getWorld().spawnParticle(Particle.DAMAGE_INDICATOR,first,0);
                    player.getWorld().spawnParticle(Particle.DAMAGE_INDICATOR,second,0);
                }
            }, 0L, 5L);
        }
        if (cmd.getName().equalsIgnoreCase("trophy") && player.isOp()) {
            player.getInventory().addItem(Items.trophy);
        }
        if (cmd.getName().equalsIgnoreCase("gui") && player.isOp()) {
            GUI gui = new GUI();
            player.openInventory(gui.getInventory());
            player.sendMessage(ChatColor.AQUA + "Opened the MCSU GUI!");
        }
        if (cmd.getName().equalsIgnoreCase("v") && player.isOp()) {
            if(args.length >= 1) {
                if(args[0].equalsIgnoreCase("toggle")) {
                    if(player.isInvisible()) {
                        player.setInvisible(false);
                    } else {
                        player.setInvisible(true);
                    }
                } else {
                    player.sendMessage(ChatColor.RED+"Invalid argument.");
                }
            } else {
                player.sendMessage(ChatColor.RED+"Invalid command.");
            }
        }
        if(cmd.getName().equalsIgnoreCase("mcsuteam")) {
            if(args.length >= 3) {
                String action = args[0];
                String team = args[1];
                String teamplayer = args[2];
                Player playerteam = Bukkit.getServer().getPlayer(teamplayer);
                if(action.equalsIgnoreCase("join")) {
                    if(team.equalsIgnoreCase("Blue")) {
                        playerteam.getScoreboard().getPlayerTeam(playerteam).removeEntry(teamplayer);
                        MCSU.blueteam.addEntry(teamplayer);
                        player.sendMessage(ChatColor.BLUE+"Added "+teamplayer+" to blue team.");
                    } else if(team.equalsIgnoreCase("Red")) {
                        playerteam.getScoreboard().getPlayerTeam(playerteam).removeEntry(teamplayer);
                        MCSU.redteam.addEntry(teamplayer);
                        player.sendMessage(ChatColor.RED+"Added "+teamplayer+" to red team.");
                    } else if(team.equalsIgnoreCase("Yellow")) {
                        playerteam.getScoreboard().getPlayerTeam(playerteam).removeEntry(teamplayer);
                        MCSU.yellowteam.addEntry(teamplayer);
                        player.sendMessage(ChatColor.YELLOW+"Added "+teamplayer+" to yellow team.");
                    } else if(team.equalsIgnoreCase("Green")) {
                        playerteam.getScoreboard().getPlayerTeam(playerteam).removeEntry(teamplayer);
                        MCSU.greenteam.addEntry(teamplayer);
                        player.sendMessage(ChatColor.GREEN+"Added "+teamplayer+" to green team.");
                    } else if(team.equalsIgnoreCase("Aqua")) {
                        playerteam.getScoreboard().getPlayerTeam(playerteam).removeEntry(teamplayer);
                        MCSU.aquateam.addEntry(teamplayer);
                        player.sendMessage(ChatColor.AQUA+"Added "+teamplayer+" to aqua team.");
                    } else if(team.equalsIgnoreCase("Pink")) {
                        playerteam.getScoreboard().getPlayerTeam(playerteam).removeEntry(teamplayer);
                        MCSU.pinkteam.addEntry(teamplayer);
                        player.sendMessage(ChatColor.LIGHT_PURPLE+"Added "+teamplayer+" to pink team.");
                    } else if(team.equalsIgnoreCase("Grey")) {
                        playerteam.getScoreboard().getPlayerTeam(playerteam).removeEntry(teamplayer);
                        MCSU.greyteam.addEntry(teamplayer);
                        player.sendMessage(ChatColor.GRAY+"Added "+teamplayer+" to grey team.");
                    } else if(team.equalsIgnoreCase("White")) {
                        playerteam.getScoreboard().getPlayerTeam(playerteam).removeEntry(teamplayer);
                        MCSU.whiteteam.addEntry(teamplayer);
                        player.sendMessage(ChatColor.WHITE+"Added "+teamplayer+" to white team.");
                    } else {
                        player.sendMessage(ChatColor.RED+"Invalid team");
                    }
                }
                /*else if(action.equalsIgnoreCase("leave")) {
                    if(team.equalsIgnoreCase("Blue")) {
                        MCSU.blueteam.removeEntry(teamplayer);
                        player.sendMessage(ChatColor.BLUE+"Removed "+teamplayer+" from blue team.");
                    } else if(team.equalsIgnoreCase("Red")) {
                        MCSU.redteam.removeEntry(teamplayer);
                        player.sendMessage(ChatColor.RED+"Removed "+teamplayer+" from red team.");
                    } else if(team.equalsIgnoreCase("Yellow")) {
                        MCSU.yellowteam.removeEntry(teamplayer);
                        player.sendMessage(ChatColor.YELLOW+"Removed "+teamplayer+" from yellow team.");
                    } else if(team.equalsIgnoreCase("Green")) {
                        MCSU.greenteam.removeEntry(teamplayer);
                        player.sendMessage(ChatColor.GREEN+"Removed "+teamplayer+" from green team.");
                    } else {
                        player.sendMessage(ChatColor.RED+"Invalid team");
                    }
                }
                 */
                for(Player players : Bukkit.getOnlinePlayers()) {
                    MCSU.createBoard(players);
                }
            } else {
                player.sendMessage(ChatColor.RED+"Invalid command.");
            }
        }
        // if(cmd.getName().equalsIgnoreCase("") && player.getName().equalsIgnoreCase("Cloudinator"))
        if (cmd.getName().equalsIgnoreCase("hub")) {
            player.teleport(new Location(player.getWorld(), MCSU.spawnx, MCSU.spawny, MCSU.spawnz, -180, 0));
            player.sendMessage(ChatColor.AQUA + "Sending you to the hub!");
        }
        if (cmd.getName().equalsIgnoreCase("heal") && player.isOp()) {
            player.setHealth(20);
            player.setFoodLevel(20);
            player.sendMessage(ChatColor.GREEN + "You have been healed!");
        }
        return true;
    }

    @EventHandler
    public static void onCraftShield(CraftItemEvent e) {
        if(e.getInventory().getResult().equals(new ItemStack(Material.SHIELD, 1))) {
            e.setCancelled(true);
            player.sendMessage(ChatColor.RED+"Shields are disabled.");
        }
    }
}