package com.cloud.mcsu.worldreset;

import com.cloud.mcsu.MCSU;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.session.ClipboardHolder;
import com.sk89q.worldedit.world.DataException;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import java.io.*;
import java.util.*;

public class WorldManager implements Listener {

    public static MCSU mcsu = MCSU.getPlugin(MCSU.class);

    public static void resetSkybattle(Player player) throws IOException, MaxChangedBlocksException {
        int x = 1344;
        int y = 147;
        int z = 1526;
        com.sk89q.worldedit.world.World adaptedWorld = BukkitAdapter.adapt(player.getWorld());
        EditSession editSession = WorldEdit.getInstance().getEditSessionFactory().getEditSession(adaptedWorld, -1);
        File skybattleschem = new File(mcsu.getDataFolder() + File.separator + "/schematics/skybattle.schem");

        Bukkit.getScheduler().runTaskAsynchronously(
                mcsu.getInstance(), new Runnable() {
                    @Override
                    public void run() {
                        try {
                            ClipboardFormat format = ClipboardFormats.findByFile(skybattleschem);
                            ClipboardReader reader = format.getReader(new FileInputStream(skybattleschem));
                            Clipboard clipboard = reader.read();
                            Operation operation = new ClipboardHolder(clipboard).createPaste(editSession).to(BlockVector3.at(x, y, z)).ignoreAirBlocks(false).build();
                            Operations.complete(operation);
                            editSession.flushSession();
                        } catch (WorldEditException | IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
        );
    }

    public static void resetHideAndSeek(Player player) throws IOException, MaxChangedBlocksException {
        int x = -1024;
        int y = 123;
        int z = 299;
        com.sk89q.worldedit.world.World adaptedWorld = BukkitAdapter.adapt(player.getWorld());
        EditSession editSession = WorldEdit.getInstance().getEditSessionFactory().getEditSession(adaptedWorld, -1);
        File schem = new File(mcsu.getDataFolder() + File.separator + "/schematics/hideandseek.schem");

        Bukkit.getScheduler().runTaskAsynchronously(
                mcsu.getInstance(), new Runnable() {
                    @Override
                    public void run() {
                        try {
                            ClipboardFormat format = ClipboardFormats.findByFile(schem);
                            ClipboardReader reader = format.getReader(new FileInputStream(schem));
                            Clipboard clipboard = reader.read();
                            Operation operation = new ClipboardHolder(clipboard).createPaste(editSession).to(BlockVector3.at(x, y, z)).ignoreAirBlocks(true).build();
                            Operations.complete(operation);
                            editSession.flushSession();
                        } catch (WorldEditException | IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
        );
    }

    public static void resetSG(Player player) throws IOException, MaxChangedBlocksException {
        int x = 1069;
        int y = 69;
        int z = 145;
        com.sk89q.worldedit.world.World adaptedWorld = BukkitAdapter.adapt(player.getWorld());
        EditSession editSession = WorldEdit.getInstance().getEditSessionFactory().getEditSession(adaptedWorld, -1);
        File sgschem = new File(mcsu.getDataFolder() + File.separator + "/schematics/sg.schem");

        Bukkit.getScheduler().runTaskAsynchronously(
                mcsu.getInstance(), new Runnable() {
                    @Override
                    public void run() {
                        try {
                            ClipboardFormat format = ClipboardFormats.findByFile(sgschem);
                            ClipboardReader reader = format.getReader(new FileInputStream(sgschem));
                            Clipboard clipboard = reader.read();
                            Operation operation = new ClipboardHolder(clipboard).createPaste(editSession).to(BlockVector3.at(x, y, z)).ignoreAirBlocks(false).build();
                            Operations.complete(operation);
                            editSession.flushSession();
                        } catch (WorldEditException | IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
        );
    }

    public static void resetgbmap1(Player player) throws IOException {
        int x = -975;
        int y = 70;
        int z = -1934;
        com.sk89q.worldedit.world.World adaptedWorld = BukkitAdapter.adapt(player.getWorld());
        EditSession editSession = WorldEdit.getInstance().getEditSessionFactory().getEditSession(adaptedWorld, -1);
        File schem = new File(mcsu.getDataFolder() + File.separator + "/schematics/gangbeastsmap1.schem");
        Bukkit.getScheduler().runTaskAsynchronously(
                mcsu.getInstance(), new Runnable() {
                    @Override
                    public void run() {
                        try {
                            ClipboardFormat format = ClipboardFormats.findByFile(schem);
                            ClipboardReader reader = format.getReader(new FileInputStream(schem));
                            Clipboard clipboard = reader.read();
                            Operation operation = new ClipboardHolder(clipboard).createPaste(editSession).to(BlockVector3.at(x, y, z)).ignoreAirBlocks(false).build();
                            Operations.complete(operation);
                            editSession.flushSession();
                        } catch (WorldEditException | IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
        );
    }

    public static void resetgbmap2(Player player) throws IOException {
        int x = -1193;
        int y =  40;
        int z = -2121;
        com.sk89q.worldedit.world.World adaptedWorld = BukkitAdapter.adapt(player.getWorld());
        EditSession editSession = WorldEdit.getInstance().getEditSessionFactory().getEditSession(adaptedWorld, -1);
        File schem = new File(mcsu.getDataFolder() + File.separator + "/schematics/gangbeastsmap2.schem");
        Bukkit.getScheduler().runTaskAsynchronously(
                mcsu.getInstance(), new Runnable() {
                    @Override
                    public void run() {
                        try {
                            ClipboardFormat format = ClipboardFormats.findByFile(schem);
                            ClipboardReader reader = format.getReader(new FileInputStream(schem));
                            Clipboard clipboard = reader.read();
                            Operation operation = new ClipboardHolder(clipboard).createPaste(editSession).to(BlockVector3.at(x, y, z)).ignoreAirBlocks(false).build();
                            Operations.complete(operation);
                            editSession.flushSession();
                        } catch (WorldEditException | IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
        );
    }

    public static void resetgbmap3(Player player) throws IOException {
        int x = -933;
        int y = 51;
        int z = -2076;
        com.sk89q.worldedit.world.World adaptedWorld = BukkitAdapter.adapt(player.getWorld());
        EditSession editSession = WorldEdit.getInstance().getEditSessionFactory().getEditSession(adaptedWorld, -1);
        File schem = new File(mcsu.getDataFolder() + File.separator + "/schematics/gangbeastsmap3.schem");
        Bukkit.getScheduler().runTaskAsynchronously(
                mcsu.getInstance(), new Runnable() {
                    @Override
                    public void run() {
                        try {
                            ClipboardFormat format = ClipboardFormats.findByFile(schem);
                            ClipboardReader reader = format.getReader(new FileInputStream(schem));
                            Clipboard clipboard = reader.read();
                            Operation operation = new ClipboardHolder(clipboard).createPaste(editSession).to(BlockVector3.at(x, y, z)).ignoreAirBlocks(false).build();
                            Operations.complete(operation);
                            editSession.flushSession();
                        } catch (WorldEditException | IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
        );
    }

    public static void resetgbmap4(Player player) throws IOException {
        int x = -793;
        int y = 65;
        int z = -2239;
        com.sk89q.worldedit.world.World adaptedWorld = BukkitAdapter.adapt(player.getWorld());
        EditSession editSession = WorldEdit.getInstance().getEditSessionFactory().getEditSession(adaptedWorld, -1);
        File schem = new File(mcsu.getDataFolder() + File.separator + "/schematics/gangbeastsmap4.schem");
        Bukkit.getScheduler().runTaskAsynchronously(
                mcsu.getInstance(), new Runnable() {
                    @Override
                    public void run() {
                        try {
                            ClipboardFormat format = ClipboardFormats.findByFile(schem);
                            ClipboardReader reader = format.getReader(new FileInputStream(schem));
                            Clipboard clipboard = reader.read();
                            Operation operation = new ClipboardHolder(clipboard).createPaste(editSession).to(BlockVector3.at(x, y, z)).ignoreAirBlocks(false).build();
                            Operations.complete(operation);
                            editSession.flushSession();
                        } catch (WorldEditException | IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
        );
    }
    public static void resetgbmap5(Player player) throws IOException {
        int x = -804;
        int y = 50;
        int z = -1902;
        com.sk89q.worldedit.world.World adaptedWorld = BukkitAdapter.adapt(player.getWorld());
        EditSession editSession = WorldEdit.getInstance().getEditSessionFactory().getEditSession(adaptedWorld, -1);
        File schem = new File(mcsu.getDataFolder() + File.separator + "/schematics/gangbeastsmap5.schem");
        Bukkit.getScheduler().runTaskAsynchronously(
                mcsu.getInstance(), new Runnable() {
                    @Override
                    public void run() {
                        try {
                            ClipboardFormat format = ClipboardFormats.findByFile(schem);
                            ClipboardReader reader = format.getReader(new FileInputStream(schem));
                            Clipboard clipboard = reader.read();
                            Operation operation = new ClipboardHolder(clipboard).createPaste(editSession).to(BlockVector3.at(x, y, z)).ignoreAirBlocks(false).build();
                            Operations.complete(operation);
                            editSession.flushSession();
                        } catch (WorldEditException | IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
        );
    }

}
