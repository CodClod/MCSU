package com.cloud.mcsu.announcements;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerChatEvent;

public class Announcement implements Listener  {

    public static boolean announcementHappening;

    public Announcement(String[] msgs) {
        announcementHappening = true;
        for(Player players : Bukkit.getOnlinePlayers()) {
            players.sendMessage(ChatColor.STRIKETHROUGH+"                                       ");
            players.sendMessage(msgs);
            players.sendMessage(ChatColor.STRIKETHROUGH+"                                       ");
        }
    }

    @EventHandler
    public static void onChatMessage(AsyncPlayerChatEvent e) {
        if(announcementHappening) {
            e.setCancelled(false);
        }
    }
}
