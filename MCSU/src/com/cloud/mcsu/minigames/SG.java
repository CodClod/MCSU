package com.cloud.mcsu.minigames;

import com.cloud.mcsu.commands.Commands;
import com.cloud.mcsu.config.Config;
import com.cloud.mcsu.event.Event;
import com.cloud.mcsu.gui.GUI;
import com.cloud.mcsu.worldreset.WorldManager;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentWrapper;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionType;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scoreboard.Team;
import com.cloud.mcsu.MCSU;

import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class SG implements CommandExecutor, Listener {

    public static int time;
    public static int taskID;
    public static boolean sgStarted;
    public static World world;
    public static int graceperiod = 30;
    public static Player player;
    public static MCSU mcsu = MCSU.getPlugin(MCSU.class);
    public static ArrayList<Player> deadplayers = new ArrayList<Player>();
    public static ArrayList<String> deadteams = new ArrayList<String>();
    public static ArrayList<String> onlineteams = new ArrayList<String>();
    public static HashMap<String, Integer> onlineteamplayers = new HashMap<String, Integer>();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(!(sender instanceof Player)) {
            sender.sendMessage("Only players can use that command!");
            return true;
        }
        player = (Player) sender;
        if(player.isOp() && cmd.getName().equalsIgnoreCase("sg")) {
            sgCommand(player);
        }
        if(player.isOp() && cmd.getName().equalsIgnoreCase("stopsg")) {
            sgStarted = false;
            world.setPVP(true);
            world.getWorldBorder().setSize(10000,1);
            stopTimer();
            Bukkit.broadcastMessage(ChatColor.AQUA+"Stopping Game!");
            deadteams = null;
            deadplayers = null;
            onlineteams = null;
        }
        return true;
    }

    public static void sgCommand(Player p) {
        try {
            WorldManager.resetSG(player);
        } catch (IOException e) {
            e.printStackTrace();
        }
        sgStarted = false;
        if(Bukkit.getScheduler().isCurrentlyRunning(taskID)) {
            stopTimer();
        }
        player = p;
        world = player.getWorld();
        world.getWorldBorder().setCenter(1164,248);
        world.getWorldBorder().setSize(100);
        setTimer(15);
        startTimer();
        for (Player players : Bukkit.getOnlinePlayers()) {
            world.setPVP(false);
            players.teleport(new Location(world,1164,101,248));
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
    }

    public static void getChests() {
        // Mid Chests
        Location midlower1 = new Location(world,1162,4,249);
        Location midlower2 = new Location(world,1162,4,247);
        Location midlower3 = new Location(world,1163,4,246);
        Location midlower4 = new Location(world,1165,4,246);
        Location midlower5 = new Location(world,1166,4,247);
        Location midlower6 = new Location(world,1166,4,249);
        Location midlower7 = new Location(world,1165,4,250);
        Location midlower8 = new Location(world,1163,4,250);
        Location midmid1 = new Location(world,1163,5,248);
        Location midmid2 = new Location(world,1164,5,247);
        Location midmid3 = new Location(world,1165,5,248);
        Location midmid4 = new Location(world,1164,5,249);
        Location midtop1 = new Location(world,1164,6,248);

        //Forest Chests
        Location forest1 = new Location(world,1106,4,289);
        Location forest2 = new Location(world,1100,7,274);
        Location forest3 = new Location(world,1119,4,237);
        Location forest4 = new Location(world,1104,4,221);

        // Lake and Beach Chests
        Location lake = new Location(world,1212,2,292);
        Location beach1 = new Location(world,1209,4,324);
        Location beach2 = new Location(world,1185,3,314);
        Location nearlake = new Location(world,1186,5,275);

        // Playground Chest
        Location playground = new Location(world,1237,4,232);

        // Farm Chest
        Location farm = new Location(world,1231,4,180);

        // Mountain Chests
        Location icemountain1 = new Location(world,1241,5,265);
        Location icemountain2 = new Location(world,1132,4,177);
        Location mesamountain1 = new Location(world,1099,5,316);
        Location mesamountain2 = new Location(world,1157,4,326);

        // Tent Chests
        Location tent1 = new Location(world,1197,4,203);
        Location tent2 = new Location(world,1178,4,210);
        Location campfire = new Location(world,1167,4,214);

        // PogGamers Shack Chest
        Location shack = new Location(world,1150,7,204);

        // Clouds Manor Chests
        Location manor1 = new Location(world,1156,4,297);
        Location manor2 = new Location(world,1144,4,290);
        Location manor3 = new Location(world,1135,11,287);
        Location manor4 = new Location(world,1154,13,295);

        // House Chest
        Location house = new Location(world,1237,4,242);

        // Fill Chests
        fillChest(midlower1,world);
        fillChest(midlower2,world);
        fillChest(midlower3,world);
        fillChest(midlower4,world);
        fillChest(midlower5,world);
        fillChest(midlower6,world);
        fillChest(midlower7,world);
        fillChest(midlower8,world);
        fillChest(midmid1,world);
        fillChest(midmid2,world);
        fillChest(midmid3,world);
        fillChest(midmid4,world);
        fillChest(midtop1,world);
        fillChest(forest1,world);
        fillChest(forest2,world);
        fillChest(forest3,world);
        fillChest(forest4,world);
        fillChest(lake,world);
        fillChest(beach1,world);
        fillChest(beach2,world);
        fillChest(nearlake,world);
        fillChest(playground,world);
        fillChest(farm,world);
        fillChest(icemountain1,world);
        fillChest(icemountain2,world);
        fillChest(mesamountain1,world);
        fillChest(mesamountain2,world);
        fillChest(tent1,world);
        fillChest(tent2,world);
        fillChest(campfire,world);
        fillChest(shack,world);
        fillChest(manor1,world);
        fillChest(manor2,world);
        fillChest(manor3,world);
        fillChest(manor4,world);
        fillChest(house,world);
    }

    public static void sgstart() {
        List<Entity> entList = world.getEntities();//get all entities in the world
        for(Entity current : entList) {//loop through the list
            if (current instanceof Item) {//make sure we aren't deleting mobs/players
                current.remove();//remove it
            }
        }
        getChests();
        for(Player p: Bukkit.getOnlinePlayers()) {
            p.setGameMode(GameMode.SURVIVAL);
            p.getInventory().clear();
            p.setFoodLevel(20);
            p.setHealth(20);
            p.setFireTicks(1);
            p.setLevel(0);
            p.setExp(0);
            p.playSound(p.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL,100,1);
            for (PotionEffect effect : player.getActivePotionEffects()) {
                player.removePotionEffect(effect.getType());
            }
            Team team = p.getScoreboard().getPlayerTeam(p);
            String teamname = team.getName();
            if(teamname.equals("Blue")) {
                p.teleport(new Location(world,1168.5,5,258.5));
            }
            if(teamname.equals("Yellow")) {
                p.teleport(new Location(world,1174.5,5,245.5));
            }
            if(teamname.equals("Green")) {
                p.teleport(new Location(world,1168.5,5,238.5));
            }
            if(teamname.equals("Red")) {
                p.teleport(new Location(world,1154.5,5,244.5));
            }
            if(teamname.equals("Aqua")) {
                p.teleport(new Location(world,1174.5,5,252.5));
            }
            if(teamname.equals("Pink")) {
                p.teleport(new Location(world,1160.5,5,238.5));
            }
            if(teamname.equals("Grey")) {
                p.teleport(new Location(world,1154.5,5,252.5));
            }
            if(teamname.equals("White")) {
                p.teleport(new Location(world,1160.5,5,258.5));
            }
            p.sendTitle("Go!", "", 1, 20, 1);
            p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL,1,5);
        }
        stopTimer();
        sgStarted = true;
        world.getWorldBorder().setSize(200,0);
    }

    public static void fillChest(Location loc, World world) {
        // Loot
        // Diamond Boots
        ItemStack diamond_boots = new ItemStack(Material.DIAMOND_BOOTS,1);
        diamond_boots.addEnchantment(Enchantment.PROTECTION_FALL,2);

        // Diamond Leggings
        ItemStack diamond_leggings = new ItemStack(Material.DIAMOND_LEGGINGS,1);
        diamond_leggings.addEnchantment(Enchantment.DURABILITY,1);

        // Iron Boots
        ItemStack iron_boots = new ItemStack(Material.IRON_BOOTS,1);
        iron_boots.addEnchantment(Enchantment.DEPTH_STRIDER,3);

        // Iron Leggings
        ItemStack iron_leggings = new ItemStack(Material.IRON_LEGGINGS,1);
        iron_leggings.addEnchantment(Enchantment.THORNS,1);

        // Iron Chestplate
        ItemStack iron_chestplate = new ItemStack(Material.IRON_CHESTPLATE,1);
        iron_chestplate.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL,1);

        // Iron Helmet
        ItemStack iron_helmet = new ItemStack(Material.IRON_HELMET,1);
        iron_helmet.addEnchantment(Enchantment.WATER_WORKER,1);

        // Gold Chestplate
        ItemStack gold_chestplate = new ItemStack(Material.GOLDEN_CHESTPLATE);
        gold_chestplate.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL,1);

        // Gold Helmet
        ItemStack gold_helmet = new ItemStack(Material.GOLDEN_HELMET,1);
        gold_helmet.addEnchantment(Enchantment.OXYGEN,3);

        // Bow
        ItemStack bow = new ItemStack(Material.BOW,1);
        bow.addEnchantment(Enchantment.ARROW_KNOCKBACK,1);

        //Ender Pearl
        ItemStack ender_pearl = new ItemStack(Material.ENDER_PEARL,1);

        // Regen Pot
        ItemStack regen_pot = new ItemStack(Material.SPLASH_POTION,1);
        PotionMeta regen = (PotionMeta) regen_pot.getItemMeta();
        regen.setBasePotionData(new PotionData(PotionType.REGEN));
        regen_pot.setItemMeta(regen);

        // Fire Res Pot
        ItemStack fireres_pot = new ItemStack(Material.SPLASH_POTION,1);
        PotionMeta fireres = (PotionMeta) fireres_pot.getItemMeta();
        fireres.setBasePotionData(new PotionData(PotionType.FIRE_RESISTANCE));
        fireres_pot.setItemMeta(fireres);

        // Gapple
        ItemStack gapple = new ItemStack(Material.GOLDEN_APPLE,1);

        // Diamond Sword
        ItemStack diamond_sword = new ItemStack(Material.DIAMOND_SWORD,1);
        diamond_sword.addEnchantment(Enchantment.SWEEPING_EDGE,2);

        // Gold Axe
        ItemStack gold_axe = new ItemStack(Material.GOLDEN_AXE,1);
        gold_axe.addEnchantment(Enchantment.DURABILITY,3);

        // Iron Sword
        ItemStack iron_sword = new ItemStack(Material.IRON_SWORD,1);
        iron_sword.addEnchantment(Enchantment.DAMAGE_ALL,1);

        // Wood Sword
        ItemStack wood_sword = new ItemStack(Material.WOODEN_SWORD,1);
        wood_sword.addEnchantment(Enchantment.KNOCKBACK,2);

        // Steak
        ItemStack steak = new ItemStack(Material.COOKED_BEEF,8);

        // Potatos
        ItemStack potatos = new ItemStack(Material.BAKED_POTATO,16);

        Random rand = new Random();
        Random arrowrand = new Random();
        Random cobwebrand = new Random();
        Random xprand = new Random();
        int[] arrowamount = {16,32};
        int[] cobwebamount = {4,8};
        int[] xpamount = {8,32,64};

        // Arrow
        ItemStack arrow = new ItemStack(Material.ARROW,arrowamount[arrowrand.nextInt(arrowamount.length)]);

        // Lava
        ItemStack lava = new ItemStack(Material.LAVA_BUCKET,1);

        // Cobwebs
        ItemStack cobwebs = new ItemStack(Material.COBWEB,cobwebamount[cobwebrand.nextInt(cobwebamount.length)]);

        // Water Bucket
        ItemStack waterbucket = new ItemStack(Material.WATER_BUCKET,1);

        // XP Bottles
        ItemStack xpbottles = new ItemStack(Material.EXPERIENCE_BOTTLE,xpamount[xprand.nextInt(xpamount.length)]);

        // Anvil
        ItemStack anvil = new ItemStack(Material.ANVIL,1);

        // Crossbow
        // ItemStack crossbow = new ItemStack(Material.CROSSBOW,1);

        /* Book
        ItemStack powerbook = new ItemStack(Material.ENCHANTED_BOOK,1);
        ItemStack sharpbook = new ItemStack(Material.ENCHANTED_BOOK,1);
        ItemStack protbook = new ItemStack(Material.ENCHANTED_BOOK,1);
        ItemStack quickchargebook = new ItemStack(Material.ENCHANTED_BOOK,1);
        ItemStack kbbook = new ItemStack(Material.ENCHANTED_BOOK,1);
        EnchantmentStorageMeta power = (EnchantmentStorageMeta) powerbook.getItemMeta(); power.addStoredEnchant(Enchantment.ARROW_DAMAGE,new Random().nextInt(2)+1,true);
        EnchantmentStorageMeta sharpness = (EnchantmentStorageMeta) sharpbook.getItemMeta(); sharpness.addStoredEnchant(Enchantment.DAMAGE_ALL,new Random().nextInt(2)+1,true);
        EnchantmentStorageMeta prot = (EnchantmentStorageMeta) protbook.getItemMeta(); prot.addStoredEnchant(Enchantment.PROTECTION_ENVIRONMENTAL,new Random().nextInt(2)+1,true);
        EnchantmentStorageMeta quickcharge = (EnchantmentStorageMeta) quickchargebook.getItemMeta(); quickcharge.addStoredEnchant(Enchantment.QUICK_CHARGE,new Random().nextInt(3)+1,true);
        EnchantmentStorageMeta kb = (EnchantmentStorageMeta) kbbook.getItemMeta(); power.addStoredEnchant(Enchantment.KNOCKBACK,new Random().nextInt(2)+1,true);
         */
        ItemStack[] items = {diamond_boots,diamond_leggings,iron_boots,iron_leggings,iron_chestplate,iron_helmet,gold_chestplate,gold_helmet,bow,ender_pearl,regen_pot,gapple,diamond_sword,gold_axe,iron_sword,wood_sword,arrow,steak,potatos,cobwebs,lava,waterbucket,xpbottles,anvil,fireres_pot};//,powerbook,sharpbook,protbook,quickchargebook,kbbook};

        // Fill Chests
        Block b = world.getBlockAt(loc);
        Chest c = (Chest) b.getState();
        c.getInventory().clear();
        c.getInventory().setItem(rand.nextInt(27),items[rand.nextInt(items.length)]);
        c.getInventory().setItem(rand.nextInt(27),items[rand.nextInt(items.length)]);
        c.getInventory().setItem(rand.nextInt(27),items[rand.nextInt(items.length)]);
        c.getInventory().setItem(rand.nextInt(27),items[rand.nextInt(items.length)]);
        c.getInventory().setItem(rand.nextInt(27),items[rand.nextInt(items.length)]);
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
                    if(!sgStarted) {
                        sgstart();
                        setTimer(graceperiod);
                        startTimer();
                    } else {
                        Bukkit.broadcastMessage(ChatColor.AQUA+"PVP is enabled!");
                        Bukkit.broadcastMessage(ChatColor.RED+"Border shrinking!");
                        world.getWorldBorder().setCenter(1164,248);
                        world.getWorldBorder().setSize(10,450);
                        stopTimer();
                        world.setPVP(true);
                    }
                    return;
                }
                if(time % 1 == 0 && !sgStarted && time <= 10) {
                    for (Player players : Bukkit.getOnlinePlayers()) {
                        players.sendTitle(""+time, "", 1, 20, 1);
                        players.playSound(players.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL,1,1);
                    }
                }
                if(time == 15 && !sgStarted) {
                    MCSU.currentgame = "Survival Games";
                    MCSU.gameround = 1;
                    for (Player players : Bukkit.getOnlinePlayers()) {
                        players.sendTitle("SG starting in "+time+" seconds!", "", 1, 20, 1);
                        players.sendMessage(ChatColor.AQUA+"SG Starting in "+time+" seconds!");
                        players.playSound(players.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL,1,1);
                        MCSU.createBoard(players);
                    }
                }
                if (time == graceperiod) {
                    Bukkit.broadcastMessage(ChatColor.AQUA+"PVP will be enabled in "+graceperiod+" seconds.");
                }
                time = time - 1;
            }
        }, 0L, 20L);
    }

    public static void stopTimer() {
        Bukkit.getScheduler().cancelTask(taskID);
    }

    @EventHandler
    public static void onKillPoint(PlayerDeathEvent e) {
        Player killer = e.getEntity().getKiller();
        Team team = killer.getScoreboard().getPlayerTeam(killer);
        String teamname = team.getName();
        if(sgStarted) {
            if(teamname.equals("Blue")) {
                int points = MCSU.bluepoints + 20;
                Config.get().set("Points.BluePoints",Integer.toString(points));
                MCSU.getPoints();
                Bukkit.broadcastMessage(ChatColor.BLUE+killer.getName()+ChatColor.WHITE+" just earned +"+ChatColor.GOLD+"20"+ChatColor.WHITE+" points!");
                killer.playSound(killer.getLocation(),Sound.ENTITY_ARROW_HIT_PLAYER,100,1);
                killer.playSound(killer.getLocation(),Sound.ENTITY_GENERIC_EXPLODE,100,1);
            }
            if(teamname.equals("Pink")) {
                int points = MCSU.pinkpoints + 20;
                Config.get().set("Points.PinkPoints",Integer.toString(points));
                MCSU.getPoints();
                Bukkit.broadcastMessage(ChatColor.BLUE+killer.getName()+ChatColor.WHITE+" just earned +"+ChatColor.GOLD+"20"+ChatColor.WHITE+" points!");
                killer.playSound(killer.getLocation(),Sound.ENTITY_ARROW_HIT_PLAYER,100,1);
                killer.playSound(killer.getLocation(),Sound.ENTITY_GENERIC_EXPLODE,100,1);
            }
            if(teamname.equals("Yellow")) {
                int points = MCSU.yellowpoints + 20;
                Config.get().set("Points.YellowPoints",Integer.toString(points));
                MCSU.getPoints();
                Bukkit.broadcastMessage(ChatColor.BLUE+killer.getName()+ChatColor.WHITE+" just earned +"+ChatColor.GOLD+"20"+ChatColor.WHITE+" points!");
                killer.playSound(killer.getLocation(),Sound.ENTITY_ARROW_HIT_PLAYER,100,1);
                killer.playSound(killer.getLocation(),Sound.ENTITY_GENERIC_EXPLODE,100,1);
            }
            if(teamname.equals("Green")) {
                int points = MCSU.greenpoints + 20;
                Config.get().set("Points.GreenPoints",Integer.toString(points));
                MCSU.getPoints();
                Bukkit.broadcastMessage(ChatColor.BLUE+killer.getName()+ChatColor.WHITE+" just earned +"+ChatColor.GOLD+"20"+ChatColor.WHITE+" points!");
                killer.playSound(killer.getLocation(),Sound.ENTITY_ARROW_HIT_PLAYER,100,1);
                killer.playSound(killer.getLocation(),Sound.ENTITY_GENERIC_EXPLODE,100,1);
            }
            if(teamname.equals("Red")) {
                int points = MCSU.redpoints + 20;
                Config.get().set("Points.RedPoints",Integer.toString(points));
                MCSU.getPoints();
                Bukkit.broadcastMessage(ChatColor.BLUE+killer.getName()+ChatColor.WHITE+" just earned +"+ChatColor.GOLD+"20"+ChatColor.WHITE+" points!");
                killer.playSound(killer.getLocation(),Sound.ENTITY_ARROW_HIT_PLAYER,100,1);
                killer.playSound(killer.getLocation(),Sound.ENTITY_GENERIC_EXPLODE,100,1);
            }
            if(teamname.equals("Grey")) {
                int points = MCSU.greypoints + 20;
                Config.get().set("Points.GreyPoints",Integer.toString(points));
                MCSU.getPoints();
                Bukkit.broadcastMessage(ChatColor.BLUE+killer.getName()+ChatColor.WHITE+" just earned +"+ChatColor.GOLD+"20"+ChatColor.WHITE+" points!");
                killer.playSound(killer.getLocation(),Sound.ENTITY_ARROW_HIT_PLAYER,100,1);
                killer.playSound(killer.getLocation(),Sound.ENTITY_GENERIC_EXPLODE,100,1);
            }
            if(teamname.equals("Aqua")) {
                int points = MCSU.aquapoints + 20;
                Config.get().set("Points.AquaPoints",Integer.toString(points));
                MCSU.getPoints();
                Bukkit.broadcastMessage(ChatColor.BLUE+killer.getName()+ChatColor.WHITE+" just earned +"+ChatColor.GOLD+"20"+ChatColor.WHITE+" points!");
                killer.playSound(killer.getLocation(),Sound.ENTITY_ARROW_HIT_PLAYER,100,1);
                killer.playSound(killer.getLocation(),Sound.ENTITY_GENERIC_EXPLODE,100,1);
            }
            if(teamname.equals("White")) {
                int points = MCSU.whitepoints + 20;
                Config.get().set("Points.WhitePoints",Integer.toString(points));
                MCSU.getPoints();
                Bukkit.broadcastMessage(ChatColor.BLUE+killer.getName()+ChatColor.WHITE+" just earned +"+ChatColor.GOLD+"20"+ChatColor.WHITE+" points!");
                killer.playSound(killer.getLocation(),Sound.ENTITY_ARROW_HIT_PLAYER,100,1);
                killer.playSound(killer.getLocation(),Sound.ENTITY_GENERIC_EXPLODE,100,1);
            }
        }
    }

    @EventHandler
    public static void onBreakBlock(BlockBreakEvent e) {
        if(sgStarted) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public static void onDeath(PlayerDeathEvent e) {
        Player deadplayer = e.getEntity();
        Player killer = e.getEntity().getKiller();
        Team team = deadplayer.getScoreboard().getPlayerTeam(deadplayer);
        String teamname = team.getName();
        int i = 0;
        deadplayers.add(deadplayer);
        for(String players : team.getEntries()) {
            if(Bukkit.getOnlinePlayers().contains(Bukkit.getPlayer(players))) {
                onlineteamplayers.put(teamname,onlineteamplayers.get(teamname)+1);
            }
            if(deadplayers.contains(Bukkit.getServer().getPlayer(players))) {
                i++;
            }
        }
        player.sendMessage(i+"");
        for(Player players : Bukkit.getOnlinePlayers()) {
            if(!onlineteams.contains(players.getScoreboard().getPlayerTeam(players).getName())) {
                onlineteams.add(players.getScoreboard().getPlayerTeam(players).getName());
            }
        }
        if(i == onlineteamplayers.get(teamname)) {
            for(Player players : Bukkit.getOnlinePlayers()) {
                deadteams.add(teamname);
                players.sendMessage(players.getScoreboard().getTeam(teamname).getColor()+teamname+ChatColor.WHITE+" has been eliminated.");
            }
        }
        int teamsleftcount = onlineteams.size() - deadteams.size();
        player.sendMessage("Teams left: "+teamsleftcount+"Dead teams: "+deadteams.size());
        if(teamsleftcount == 4) {
            if(!deadteams.contains("Blue") && onlineteams.contains("Blue")) {
                int points = MCSU.bluepoints + 25;
                Config.get().set("Points.BluePoints",Integer.toString(points));
                MCSU.getPoints();
            } else if(!deadteams.contains("Red") && onlineteams.contains("Red")) {

            } else if(!deadteams.contains("Green") && onlineteams.contains("Green")) {

            } else if(!deadteams.contains("Yellow") && onlineteams.contains("Yellow")) {

            } else if(!deadteams.contains("Aqua") && onlineteams.contains("Aqua")) {

            } else if(!deadteams.contains("Pink") && onlineteams.contains("Pink")) {

            } else if(!deadteams.contains("Grey") && onlineteams.contains("Grey")) {

            } else if(!deadteams.contains("White") && onlineteams.contains("White")) {

            }
            Bukkit.broadcastMessage(ChatColor.BLUE+"4 Teams "+ChatColor.WHITE+" just earned +"+ChatColor.GOLD+"25"+ChatColor.WHITE+" points for surviving!");
            for(String player : killer.getScoreboard().getTeam("Blue").getEntries()) {
                Bukkit.getPlayer(player).playSound(killer.getLocation(),Sound.ENTITY_GENERIC_EXPLODE,100,1);
                Bukkit.getPlayer(player).playSound(killer.getLocation(),Sound.ENTITY_ARROW_HIT_PLAYER,100,1);
            }
        }
        if(teamsleftcount == 4) {

        }
        if(teamsleftcount == 4) {

        }
        if(teamsleftcount == 1) {
            if(!deadteams.contains("Blue") && onlineteams.contains("Blue")) {
                Bukkit.broadcastMessage(ChatColor.BLUE+"Blue Bears win SG!");
                Event.spawnFireworks(killer.getLocation(),1,Color.BLUE);
                Event.spawnFireworks(killer.getLocation(),1,Color.WHITE);
            } else if(!deadteams.contains("Red") && onlineteams.contains("Red")) {
                Bukkit.broadcastMessage(ChatColor.RED+"Red Reindeers win SG!");
                Event.spawnFireworks(killer.getLocation(),1,Color.RED);
                Event.spawnFireworks(killer.getLocation(),1,Color.WHITE);
            } else if(!deadteams.contains("Green") && onlineteams.contains("Green")) {
                Bukkit.broadcastMessage(ChatColor.GREEN+"Green Geckos win SG!");
                Event.spawnFireworks(killer.getLocation(),1,Color.GREEN);
                Event.spawnFireworks(killer.getLocation(),1,Color.WHITE);
            } else if(!deadteams.contains("Yellow") && onlineteams.contains("Yellow")) {
                Bukkit.broadcastMessage(ChatColor.YELLOW+"Yellow Yetis win SG!");
                Event.spawnFireworks(killer.getLocation(),1,Color.YELLOW);
                Event.spawnFireworks(killer.getLocation(),1,Color.WHITE);
            } else if(!deadteams.contains("Aqua") && onlineteams.contains("Aqua")) {
                Bukkit.broadcastMessage(ChatColor.AQUA+"Aqua Axolotols win SG!");
                Event.spawnFireworks(killer.getLocation(),1,Color.AQUA);
                Event.spawnFireworks(killer.getLocation(),1,Color.WHITE);
            } else if(!deadteams.contains("Pink") && onlineteams.contains("Pink")) {
                Bukkit.broadcastMessage(ChatColor.LIGHT_PURPLE+"Pink Pandas win SG!");
                Event.spawnFireworks(killer.getLocation(),1,Color.PURPLE);
                Event.spawnFireworks(killer.getLocation(),1,Color.WHITE);
            } else if(!deadteams.contains("Grey") && onlineteams.contains("Grey")) {
                Bukkit.broadcastMessage(ChatColor.GRAY+"Grey Gorillas win SG!");
                Event.spawnFireworks(killer.getLocation(),1,Color.GRAY);
                Event.spawnFireworks(killer.getLocation(),1,Color.WHITE);
            } else if(!deadteams.contains("White") && onlineteams.contains("White")) {
                Bukkit.broadcastMessage(ChatColor.WHITE+"White Walruses win SG!");
                Event.spawnFireworks(killer.getLocation(),1,Color.WHITE);
                Event.spawnFireworks(killer.getLocation(),1,Color.WHITE);
            }
        }
    }

    public static void spawnFireworks(Location location, int amount, Color color) {
        player.sendMessage("yes");
        Location loc = location;
        Firework fw = (Firework) loc.getWorld().spawnEntity(loc, EntityType.FIREWORK);
        FireworkMeta fwm = fw.getFireworkMeta();

        fwm.setPower(2);
        fwm.addEffect(FireworkEffect.builder().withColor(color).flicker(true).build());

        fw.setFireworkMeta(fwm);
        fw.detonate();

        for (int i = 0; i < amount; i++) {
            Firework fw2 = (Firework) loc.getWorld().spawnEntity(loc, EntityType.FIREWORK);
            fw2.setFireworkMeta(fwm);
        }
    }

}
