package com.cloud.mcsu.minigames;

import com.cloud.mcsu.MCSU;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.scoreboard.Team;

public class MiningMayhem implements Listener {

    @EventHandler
    public static void onPlayerMine(BlockBreakEvent e) {
        Player player = e.getPlayer();
        Team team = player.getScoreboard().getPlayerTeam(player);
        String teamname = team.getName();
        if(e.getBlock().getType() == Material.DIAMOND_ORE) {
            if(teamname.equals("Blue")) {
                MCSU.bluepoints = MCSU.bluepoints + 50;
            } else if(teamname.equals("Yellow")) {
                MCSU.yellowpoints = MCSU.yellowpoints + 50;
            } else if(teamname.equals("Green")) {
                MCSU.greenpoints = MCSU.greenpoints + 50;
            } else if(teamname.equals("Red")) {
                MCSU.redpoints = MCSU.redpoints + 50;
            }
        }
        if(e.getBlock().getType() == Material.EMERALD_ORE) {
            if(teamname.equals("Blue")) {
                MCSU.bluepoints = MCSU.bluepoints + 40;
            } else if(teamname.equals("Yellow")) {
                MCSU.yellowpoints = MCSU.yellowpoints + 40;
            } else if(teamname.equals("Green")) {
                MCSU.greenpoints = MCSU.greenpoints + 40;
            } else if(teamname.equals("Red")) {
                MCSU.redpoints = MCSU.redpoints + 40;
            }
        }
        if(e.getBlock().getType() == Material.GOLD_ORE) {
            if(teamname.equals("Blue")) {
                MCSU.bluepoints = MCSU.bluepoints + 30;
            } else if(teamname.equals("Yellow")) {
                MCSU.yellowpoints = MCSU.yellowpoints + 30;
            } else if(teamname.equals("Green")) {
                MCSU.greenpoints = MCSU.greenpoints + 30;
            } else if(teamname.equals("Red")) {
                MCSU.redpoints = MCSU.redpoints + 30;
            }
        }
        if(e.getBlock().getType() == Material.LAPIS_ORE) {
            if(teamname.equals("Blue")) {
                MCSU.bluepoints = MCSU.bluepoints + 20;
            } else if(teamname.equals("Yellow")) {
                MCSU.yellowpoints = MCSU.yellowpoints + 20;
            } else if(teamname.equals("Green")) {
                MCSU.greenpoints = MCSU.greenpoints + 20;
            } else if(teamname.equals("Red")) {
                MCSU.redpoints = MCSU.redpoints + 20;
            }
        }
        if(e.getBlock().getType() == Material.REDSTONE_ORE) {
            if(teamname.equals("Blue")) {
                MCSU.bluepoints = MCSU.bluepoints + 15;
            } else if(teamname.equals("Yellow")) {
                MCSU.yellowpoints = MCSU.yellowpoints + 15;
            } else if(teamname.equals("Green")) {
                MCSU.greenpoints = MCSU.greenpoints + 15;
            } else if(teamname.equals("Red")) {
                MCSU.redpoints = MCSU.redpoints + 15;
            }
        }
        if(e.getBlock().getType() == Material.IRON_ORE) {
            if(teamname.equals("Blue")) {
                MCSU.bluepoints = MCSU.bluepoints + 10;
            } else if(teamname.equals("Yellow")) {
                MCSU.yellowpoints = MCSU.yellowpoints + 10;
            } else if(teamname.equals("Green")) {
                MCSU.greenpoints = MCSU.greenpoints + 10;
            } else if(teamname.equals("Red")) {
                MCSU.redpoints = MCSU.redpoints + 10;
            }
        }
        if(e.getBlock().getType() == Material.COAL_ORE) {
            if(teamname.equals("Blue")) {
                MCSU.bluepoints = MCSU.bluepoints + 5;
            } else if(teamname.equals("Yellow")) {
                MCSU.yellowpoints = MCSU.yellowpoints + 5;
            } else if(teamname.equals("Green")) {
                MCSU.greenpoints = MCSU.greenpoints + 5;
            } else if(teamname.equals("Red")) {
                MCSU.redpoints = MCSU.redpoints + 5;
            }
        }
        /*
        player.sendMessage(ChatColor.BLUE+"Blue points: "+MCSU.bluepoints);
        player.sendMessage(ChatColor.RED+"Red points: "+MCSU.redpoints);
        player.sendMessage(ChatColor.GREEN+"Green points "+MCSU.greenpoints);
        player.sendMessage(ChatColor.YELLOW+"Yellow points "+MCSU.yellowpoints);
         */
    }

}
