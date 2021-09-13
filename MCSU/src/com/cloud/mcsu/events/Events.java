package com.cloud.mcsu.events;

import com.cloud.mcsu.announcements.Announcement;
import com.cloud.mcsu.commands.Commands;
import com.cloud.mcsu.config.Config;
import com.cloud.mcsu.event.Event;
import com.cloud.mcsu.gui.GUI;
import com.cloud.mcsu.items.Items;
import com.cloud.mcsu.minigames.SG;
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
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import com.cloud.mcsu.MCSU;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.Team;
import org.bukkit.util.Vector;

import java.util.Random;

public class Events implements Listener {

    public static World world;
    public static Plugin plugin = Bukkit.getPluginManager().getPlugin("MCSU");
    public static Location spawn;
    public static ItemStack gameselector;

    @EventHandler
    public static void onPlayerJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        world = player.getWorld();
        world.setAutoSave(false);
        // player.setAllowFlight(true);
        // player.setFlying(true);
        spawn = new Location(world,MCSU.spawnx,MCSU.spawny,MCSU.spawnz,-180,0);
        // player.teleport(spawn);
        e.setJoinMessage(ChatColor.BLUE+"Welcome to MCSU "+ChatColor.WHITE+player.getDisplayName()+"!");
        for(Player players : Bukkit.getOnlinePlayers()) {
            MCSU.createBoard(players);
        }
        gameselector = new ItemStack(Material.NETHER_STAR,1);
        ItemMeta meta = gameselector.getItemMeta();
        meta.setDisplayName("§b§lGame Selector");
        gameselector.setItemMeta(meta);
        player.getInventory().setItem(4,gameselector);
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
        if(e.getItem() == null) return;

        if(e.hasBlock())

        if(e.isBlockInHand()) {
            if(e.getItem().equals(Material.TNT)) {
                Location loc = e.getPlayer().getLocation();

                Entity tnt = world.spawn(loc, TNTPrimed.class);

                ((TNTPrimed)tnt).setFuseTicks(40);

                e.getItem().setAmount(0);
            }
        }
    }

     */

    @EventHandler
    public void onTNTPlace(BlockPlaceEvent e) {
        Block b = e.getBlock();

        if(b.getType().equals(Material.TNT)) {
            Vector vec = new Vector(0.5,0.5,0.5);
            Location loc = b.getLocation().add(vec);

            Entity tnt = world.spawn(loc, TNTPrimed.class);

            ((TNTPrimed)tnt).setFuseTicks(40);

            loc.getBlock().setType(Material.AIR);
        }
    }

}
