package com.cloud.mcsu.events;

import com.cloud.mcsu.announcements.Announcement;
import com.cloud.mcsu.commands.Commands;
import com.cloud.mcsu.config.Config;
import com.cloud.mcsu.event.Event;
import com.cloud.mcsu.gui.GUI;
import com.cloud.mcsu.items.Items;
import com.cloud.mcsu.minigames.*;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.command.defaults.ReloadCommand;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.event.vehicle.VehicleEnterEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import com.cloud.mcsu.MCSU;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scoreboard.Team;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.Random;

public class Events implements Listener {

    public static World world;
    public static Plugin plugin = Bukkit.getPluginManager().getPlugin("MCSU");
    public static Location spawn;
    public static ItemStack gameselector;
    public static int slimekouri = 100;
    public static int horseracei = 100;
    public static HashMap<Player, Integer> stopwatchtime = new HashMap<Player, Integer>();
    public static HashMap<Player, Integer> stopwatchtaskID = new HashMap<Player, Integer>();
    public static MCSU mcsu = MCSU.getPlugin(MCSU.class);

    /*
    @EventHandler
    public static void onSpaceBootsJump(PlayerMoveEvent e) {
        Player player = e.getPlayer();
        if(player.getInventory().getBoots().equals(Items.spaceboots)) {
            Location l = player.getLocation();
            l.add(0, -1, 0);
            Block b = l.getBlock();
            if(b.getType() != Material.AIR) {
                player.setVelocity(new Vector(0,1,0));
                player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_FALLING,5*20,1,false,false));
            }
        }
    }

     */

    @EventHandler
    public static void onSpaceBootsDmg(EntityDamageEvent e) {
        if(e.getEntity() instanceof Player) {
            Player player = (Player) e.getEntity();
            if(player.getInventory().getBoots().equals(Items.spaceboots)) {
                e.setCancelled(true);
                Location l = player.getLocation();
                l.add(0, -1, 0);
                Block b = l.getBlock();
                if(b.getType() != Material.AIR) {
                    player.setVelocity(new Vector(0,1,0));
                    player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_FALLING,5*20,1,false,false));
                }
            }
        }
    }

    @EventHandler
    public static void onEntityDmg(EntityDamageEvent e) {
        if(e.getEntity() instanceof Panda) {
            e.setCancelled(true);
        }
        if(e.getEntity() instanceof TropicalFish) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public static void onPlayerJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        spawn = new Location(world,MCSU.spawnx,MCSU.spawny,MCSU.spawnz,-180,0);
        if(player.getName().equals("Fluubs")) {
            e.setJoinMessage(ChatColor.BLUE+"Sex man"+ChatColor.WHITE+" is here!");
            // player.getInventory().setBoots(Items.spaceboots);
        } else {
            // player.getInventory().setBoots(Items.spaceboots);
            world = player.getWorld();
            // player.setAllowFlight(true);
            // player.setFlying(true);
            // player.teleport(spawn);
            e.setJoinMessage(ChatColor.BLUE+"Welcome to MCSU "+ChatColor.WHITE+player.getDisplayName()+"!");
            for(Player players : Bukkit.getOnlinePlayers()) {
                MCSU.createBoard(players);
            }
        }
    }

    @EventHandler
    public static void onVoidDmg(EntityDamageEvent e) {
        if(e.getCause().equals(EntityDamageEvent.DamageCause.VOID)) {
            e.setDamage(1000);
        }
    }

    @EventHandler
    public static void onPlayerChat(AsyncPlayerChatEvent e) {
        String msg = e.getMessage();
        Player sender = e.getPlayer();
        Team senderteam = sender.getScoreboard().getPlayerTeam(sender);
        ChatColor sendercolor = senderteam.getColor();
        e.setFormat(sendercolor+"["+senderteam.getDisplayName()+"] "+sendercolor+sender.getName()+ChatColor.WHITE+": "+ ChatColor.RESET + "%2$s");
    }

    @EventHandler
    public static void onRightClickGameSelector(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        if(e.getAction().equals(Action.RIGHT_CLICK_BLOCK) || e.getAction().equals(Action.RIGHT_CLICK_AIR)) {
            if(e.getItem().equals(gameselector)) {
                GUI gui = new GUI();
                player.openInventory(gui.getInventory());
            }
        }
    }

    @EventHandler
    public static void entityDeath(EntityDeathEvent e) {
        e.getEntity().getLastDamageCause();
    }

    public static boolean isPlayerInRegion(Player player, int[] coords1, int[] coords2) {

        Location pLoc = player.getLocation();
        int[] pCoords = { pLoc.getBlockX(), pLoc.getBlockY(), pLoc.getBlockZ() };

        for(int index = 0; index < pCoords.length; index++) {
            if(!isNumBetween(pCoords[index], coords1[index], coords2[index])) return false;
        }

        return true;
    }

    public static boolean isNumBetween(int targetNum, int min, int max) {
        if(min > max) {
            int i = min;
            min = max;
            max = i;
        }
        return ( (targetNum >= min) && (targetNum <= max) );
    }

    @EventHandler
    public static void noHunger(FoodLevelChangeEvent e) {
        int[] coords1 = {
                -165,
                3,
                120
        };
        int[] coords2 = {
                21,
                48,
                273
        };
        if(e.getEntity() instanceof Player) {
            Player player = (Player) e.getEntity();
            if(isPlayerInRegion(player,coords1,coords2)) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public static void onStartParkour(PlayerInteractEvent e) {
        if(e.getAction().equals(Action.PHYSICAL)) {
            Player p = e.getPlayer();
            int[] coords1 = {
                    -165,
                    3,
                    120
            };
            int[] coords2 = {
                    21,
                    48,
                    273
            };
            if(isPlayerInRegion(p,coords1,coords2)) {
                if(e.getClickedBlock().getType() == Material.HEAVY_WEIGHTED_PRESSURE_PLATE) {
                    stopwatchtime.put(p,0);
                    stopwatchtaskID.put(p,0);
                    //stopwatch(p);
                }
            }
        }
    }

    /*
    public static void stopwatch(Player p) {
        BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
        stopwatchtaskID.put(p,scheduler.scheduleSyncRepeatingTask((Plugin) mcsu, new Runnable() {
            @Override
            public void run() {
                stopwatchtime = stopwatchtime + 1;
                String fancytimer;
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
            }
        }, 0L, 20L));
    }

     */

    public static void stopstopwatch() {

    }

    @EventHandler
    public static void noDmgSpawn(EntityDamageEvent e) {
        int[] coords1 = {
                -165,
                3,
                120
        };
        int[] coords2 = {
                21,
                48,
                273
        };
        if(e.getEntity() instanceof Player) {
            Player player = (Player) e.getEntity();
            if(isPlayerInRegion(player,coords1,coords2)) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public static void deathMessages(PlayerDeathEvent e) {
        Player killer = e.getEntity().getKiller();
        Player deathplayer = e.getEntity();
        Team team = deathplayer.getScoreboard().getPlayerTeam(deathplayer);
        Random rand = new Random();
        String[] deathmsgs = {
                " got cancelled on twitter by "+ChatColor.RED+e.getEntity().getKiller().getName(),
                " was cheffed up by the impostor aka "+ChatColor.RED+e.getEntity().getKiller().getName(),
                " turned into ligma by "+ChatColor.RED+e.getEntity().getKiller().getName(),
                " got kidnapped by "+ChatColor.RED+e.getEntity().getKiller().getName(),
                " got 90'd on by "+ChatColor.RED+e.getEntity().getKiller().getName(),
                " was ejected from the amogus ship by "+ChatColor.RED+e.getEntity().getKiller().getName(),
                " got oofed by "+ChatColor.RED+e.getEntity().getKiller().getName(),
                " was stolen by "+ChatColor.RED+e.getEntity().getKiller().getName(),
                " was murdered by a Dream stan aka "+ChatColor.RED+e.getEntity().getKiller().getName(),
                " got catfished by "+ChatColor.RED+e.getEntity().getKiller().getName(),
                " was eaten by Candice aka "+ChatColor.RED+e.getEntity().getKiller().getName()
        };
        int randint = rand.nextInt(deathmsgs.length);
        /*
        if(team.getName().equalsIgnoreCase("Grey")) {
            e.setDeathMessage(ChatColor.BLUE+deathplayer.getName()+"'s forest got destroyed.");
        }
         */
        e.setDeathMessage(ChatColor.BLUE+deathplayer.getName()+ChatColor.WHITE+deathmsgs[randint]);
    }

    /*
    @EventHandler
    public void onPlayerInteractAtEntity(PlayerInteractAtEntityEvent e)
    {
        Player player = e.getPlayer();
        e.getRightClicked().addPassenger(player);
    }
     */

    @EventHandler
    public static void onPlayerLeave(PlayerQuitEvent e) {
        for(Player players : Bukkit.getOnlinePlayers()) {
            MCSU.createBoard(players);
        }
        Player player = e.getPlayer();
        Team team = player.getScoreboard().getPlayerTeam(player);
        String teamname = team.getName();
        MCSU.onlineplayers = Bukkit.getOnlinePlayers().size() - 1;
        MCSU.createBoard(player);
        if(SG.sgStarted) {
            player.setGameMode(GameMode.SPECTATOR);
            SG.deadplayers.add(player);
            int i = 0;
            for (String players : team.getEntries()) {
                if (Bukkit.getOnlinePlayers().contains(Bukkit.getPlayer(players))) {
                    SG.onlineteamplayers.put(teamname, SG.onlineteamplayers.get(teamname) + 1);
                }
                if (SG.deadplayers.contains(Bukkit.getServer().getPlayer(players))) {
                    i++;
                }
            }
            for (Player players : Bukkit.getOnlinePlayers()) {
                if (!SG.onlineteams.contains(players.getScoreboard().getPlayerTeam(players).getName())) {
                    SG.onlineteams.add(players.getScoreboard().getPlayerTeam(players).getName());
                }
            }
            if (i == SG.onlineteamplayers.get(teamname)) {
                SG.deadteams.add(teamname);
                for (Player players : Bukkit.getOnlinePlayers()) {
                    players.sendMessage(players.getScoreboard().getTeam(teamname).getColor() + teamname + ChatColor.WHITE + " has been eliminated.");
                }
            }
        }
        if(HorseRace.horseraceStarted) {
            player.setGameMode(GameMode.SPECTATOR);
            horseracei = horseracei + 1;
            HorseRace.finishers.set(horseracei,player);
        }
        if(Slimekour.slimekourStarted) {
            player.setGameMode(GameMode.SPECTATOR);
            slimekouri = slimekouri + 1;
            Slimekour.finishers.set(slimekouri,player);
        }
        if(GangBeasts.gangbeastsStarted) {
            player.setGameMode(GameMode.SPECTATOR);
            GangBeasts.deadplayers.add(player);
            int i = 0;
            for (String players : team.getEntries()) {
                if (Bukkit.getOnlinePlayers().contains(Bukkit.getPlayer(players))) {
                    GangBeasts.onlineteamplayers.put(teamname, GangBeasts.onlineteamplayers.get(teamname) + 1);
                }
                if (GangBeasts.deadplayers.contains(Bukkit.getServer().getPlayer(players))) {
                    i++;
                }
            }
            for (Player players : Bukkit.getOnlinePlayers()) {
                if (!GangBeasts.onlineteams.contains(players.getScoreboard().getPlayerTeam(players).getName())) {
                    GangBeasts.onlineteams.add(players.getScoreboard().getPlayerTeam(players).getName());
                }
            }
            if (i == GangBeasts.onlineteamplayers.get(teamname)) {
                GangBeasts.deadteams.add(teamname);
                for (Player players : Bukkit.getOnlinePlayers()) {
                    players.sendMessage(players.getScoreboard().getTeam(teamname).getColor() + teamname + ChatColor.WHITE + " has been eliminated.");
                }
            }
        }
        if(Skybattle.skybattleStarted) {
            player.setGameMode(GameMode.SPECTATOR);
            Skybattle.deadplayers.add(player);
            int i = 0;
            for (String players : team.getEntries()) {
                if (Bukkit.getOnlinePlayers().contains(Bukkit.getPlayer(players))) {
                    Skybattle.onlineteamplayers.put(teamname, Skybattle.onlineteamplayers.get(teamname) + 1);
                }
                if (Skybattle.deadplayers.contains(Bukkit.getServer().getPlayer(players))) {
                    i++;
                }
            }
            for (Player players : Bukkit.getOnlinePlayers()) {
                if (!Skybattle.onlineteams.contains(players.getScoreboard().getPlayerTeam(players).getName())) {
                    Skybattle.onlineteams.add(players.getScoreboard().getPlayerTeam(players).getName());
                }
            }
            if (i == Skybattle.onlineteamplayers.get(teamname)) {
                Skybattle.deadteams.add(teamname);
                for (Player players : Bukkit.getOnlinePlayers()) {
                    players.sendMessage(players.getScoreboard().getTeam(teamname).getColor() + teamname + ChatColor.WHITE + " has been eliminated.");
                }
            }
        }
        e.setQuitMessage(ChatColor.WHITE+player.getDisplayName()+" "+ChatColor.BLUE+"left MCSU.");
    }

    @EventHandler
    public static void onPlayerEatTaco(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        if(e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if(e.getItem().equals(Items.taco)) {
                player.playSound(player.getLocation(),Sound.ENTITY_PLAYER_BURP,1,1);
                e.getItem().setAmount(0);
                player.setHealth(20);
                player.setFoodLevel(20);
            }
        }
    }

    @EventHandler
    public static void onPlayerDeath(PlayerDeathEvent e) {
        Player player = e.getEntity();
        player.setGameMode(GameMode.SPECTATOR);
        if(player.getLocation().getY() < 0) {
            player.teleport(new Location(player.getWorld(),player.getLocation().getX(),60,player.getLocation().getZ()));
        } else {
            player.teleport(player.getLocation());
        }
    }

    /*
    @EventHandler
    public static void onDeath(PlayerDeathEvent e) {
        e.getEntity().setGameMode(GameMode.SPECTATOR);
    }
     */

    @EventHandler
    public void noUproot(PlayerInteractEvent e)
    {
        if(e.getAction() == Action.PHYSICAL && e.getClickedBlock().getType() == Material.FARMLAND) {
            e.setCancelled(true);
        }
    }

    /*
    @EventHandler
    public static void campfireHeal(EntityDamageEvent e) {
        Player player = (Player) e.getEntity();
        Location l = player.getLocation();
        Block b = l.getBlock();
        if(b.getType().equals(Material.CAMPFIRE)) {
            e.setCancelled(true);
            player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION,100,2));
        }
    }

     */

    @EventHandler
    public void onPlayerJump(PlayerMoveEvent e) {
        Player player = e.getPlayer();
        World world = player.getWorld();
        Location l = player.getLocation();
        l.add(0, -1, 0);
        Block b = l.getBlock();
        if (b.getType() == Material.BLACK_STAINED_GLASS || b.getType() == Material.RED_STAINED_GLASS || b.getType() == Material.BLUE_STAINED_GLASS) {
            player.setVelocity(new Vector(0, 2, 0));
            player.playSound(player.getLocation(), Sound.ENTITY_SLIME_SQUISH, 1, 1);
        } else {
            return;
        }
    }

    @EventHandler
    public void onPlayerFall(EntityDamageEvent e) {
        Player player = (Player) e.getEntity();
        World world = player.getWorld();
        Location l = player.getLocation();
        l.add(0, -1, 0);
        Block b = l.getBlock();
        if (b.getType() == Material.BLACK_STAINED_GLASS || b.getType() == Material.RED_STAINED_GLASS || b.getType() == Material.BLUE_STAINED_GLASS) {
            if (player.isSneaking()) {
                e.setCancelled(true);
            } else {
                e.setCancelled(true);
                player.setVelocity(new Vector(0, 2, 0));
                player.playSound(player.getLocation(), Sound.ENTITY_SLIME_SQUISH, 1, 1);
            }
        } else {
            return;
        }
    }

    @EventHandler
    public void onPlayerPlaceConcrete(BlockPlaceEvent e) {
        if(e.getBlock().getType() == Material.BLUE_CONCRETE) {
            e.getItemInHand().setAmount(64);
        } else if(e.getBlock().getType() == Material.BLUE_CONCRETE) {
            e.getItemInHand().setAmount(64);
        } else if(e.getBlock().getType() == Material.RED_CONCRETE) {
            e.getItemInHand().setAmount(64);
        } else if(e.getBlock().getType() == Material.GRAY_CONCRETE) {
            e.getItemInHand().setAmount(64);
        } else if(e.getBlock().getType() == Material.GREEN_CONCRETE) {
            e.getItemInHand().setAmount(64);
        } else if(e.getBlock().getType() == Material.LIGHT_BLUE_CONCRETE) {
            e.getItemInHand().setAmount(64);
        } else if(e.getBlock().getType() == Material.WHITE_CONCRETE) {
            e.getItemInHand().setAmount(64);
        } else if(e.getBlock().getType() == Material.PINK_CONCRETE) {
            e.getItemInHand().setAmount(64);
        } else if(e.getBlock().getType() == Material.YELLOW_CONCRETE) {
            e.getItemInHand().setAmount(64);
        }

    }


    @EventHandler
    public static void onDeath(PlayerDeathEvent e) {
        if(e.getEntity().getLocation().getY() < 0) {
            e.getEntity().getLocation().setY(64);
            e.getEntity().setBedSpawnLocation(e.getEntity().getLocation());
        } else {
            e.getEntity().setBedSpawnLocation(e.getEntity().getLocation());
        }
    }



    @EventHandler
    public static void onSit(PlayerInteractEvent e) {
        World world = e.getPlayer().getWorld();
        if(e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            if(e.getClickedBlock().getType().equals(Material.SPRUCE_STAIRS)) {
                Location loc = new Location(world,e.getClickedBlock().getX()+0.5,e.getClickedBlock().getY()-1.05,e.getClickedBlock().getZ()+0.5);
                ArmorStand stand = (ArmorStand) world.spawnEntity(loc,EntityType.ARMOR_STAND);
                stand.setGravity(false);
                stand.setInvisible(true);
                stand.addPassenger(e.getPlayer());
            }
        }
    }

    /*
    @EventHandler
    public static void onI(PlayerInteractEvent e) {
        if (e.getItem() == null) return;

        if (e.hasBlock())

            if (e.isBlockInHand()) {
                if (e.getItem().equals(Material.TNT)) {
                    Location loc = e.getPlayer().getLocation();

                    Entity tnt = world.spawn(loc, TNTPrimed.class);

                    ((TNTPrimed) tnt).setFuseTicks(40);

                    e.getItem().setAmount(0);
                }
            }
    }

     */

    /*
    @EventHandler
    public static void onEnterVehicle(VehicleEnterEvent e) {
        if(e.getVehicle() instanceof Minecart) {
            e.setCancelled(true);
            Entity minecart = world.spawnEntity(e.getEntered().getLocation(),EntityType.MINECART);
            minecart.addPassenger(e.getEntered());
        }
    }
     */

    @EventHandler
    public void onTNTPlace(BlockPlaceEvent e) {
        Block b = e.getBlock();
        world = e.getPlayer().getWorld();

        if(b.getType().equals(Material.TNT)) {
            Vector vec = new Vector(0.5,0,0.5);
            Location loc = b.getLocation().add(vec);
            loc.getBlock().setType(Material.AIR);

            Entity tnt = world.spawn(loc, TNTPrimed.class);

            ((TNTPrimed)tnt).setFuseTicks(40);
        }
    }

}
