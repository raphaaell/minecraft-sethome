package fr.raph.sethome.cmd;

import java.io.IOException;
import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.raph.sethome.main.Main;
import net.md_5.bungee.api.ChatColor;

public class CommandsAdmin implements CommandExecutor {
	
	private Main plugin;
	
	private String prefixError = ChatColor.DARK_GREEN + "SethomeErreur : ";
	private String prefixPerm = ChatColor.DARK_GREEN + "SethomePerm : ";
	private String prefixGame = ChatColor.DARK_GREEN + "Sethome : ";
	private String prefixAdmin = ChatColor.DARK_GREEN + "SethomeAdmin : ";

	public CommandsAdmin(Main main) {
		this.plugin = main;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String msg, String[] args) {
		
		if(sender instanceof Player) {
			
			Player p = (Player) sender;
			
			if(cmd.getName().equalsIgnoreCase("homes")) {
				
				if(!p.hasPermission("sethome.homes")) {
					p.sendMessage(prefixPerm + ChatColor.RED + "Vous n'avez pas les permissions suffisantes pour exécuter cette commande ! Veuillez vous diriger vers un membre du staff si vous pensez que vous "
							+ "devriez avoir accès à cette commande.");
					return false;
				}
				
				if(args.length == 0) {
					p.sendMessage(prefixError + ChatColor.RED + "La commande est /homes <Nom du Joueur> !");
					return false;
				}
				
				try {
					Bukkit.getPlayerExact(args[0].toString()).isOnline();
				}catch (Exception e) {
					p.sendMessage(prefixError + ChatColor.RED + "Le joueur " + ChatColor.AQUA + args[0].toString() + ChatColor.RED + " n'est pas connecté ou n'existe pas !");
					return false;
				}
				
				Player player = Bukkit.getPlayer(args[0].toString()); 
				
				ArrayList<String> homes = getHomes(player);
				if(homes.size() == 0) {
					p.sendMessage(prefixAdmin + ChatColor.AQUA + "Ce joueur n'a pas de homes");
					return false;
				}
				
				p.sendMessage(prefixAdmin + ChatColor.RED + "Voici la liste des homes de " + ChatColor.AQUA + player.getName() + ChatColor.RED + " : " + ChatColor.AQUA + homes.toString());

			}else if(cmd.getName().equalsIgnoreCase("home-tp")) {

				if(!p.hasPermission("sethome.home-tp")) {
					p.sendMessage(prefixPerm + ChatColor.RED + "Vous n'avez pas les permissions suffisantes pour exécuter cette commande ! Veuillez vous diriger vers un membre du staff si vous pensez que vous "
							+ "devriez avoir accès à cette commande.");
					return false;
				}
				
				if(args.length == 0 || args.length == 1) {
					p.sendMessage(prefixError + ChatColor.RED + "La commande est /home-tp <Nom du Joueur> <Nom du home>!");
					return false;
				}
				
				try {
					Bukkit.getPlayerExact(args[0].toString()).isOnline();
				}catch (Exception e) {
					p.sendMessage(prefixError + ChatColor.RED + "Le joueur " + ChatColor.AQUA + args[0].toString() + ChatColor.RED + " n'est pas connecté ou n'existe pas !");
					return false;
				}
				
				Player player = Bukkit.getPlayer(args[0].toString()); 
				
				ArrayList<String> homes = getHomes(player);
				
				if(!homes.contains(args[1])) {
					p.sendMessage(prefixError + ChatColor.AQUA + player.getName() + ChatColor.RED + " n'a pas de home qui s'appelle " + ChatColor.AQUA + args[1]);
					return false;
				}
				
				Double x = plugin.getHomesConfig().getDouble("Home." + player.getUniqueId().toString() + "." + args[1].toString() + ".X");
				Double y = plugin.getHomesConfig().getDouble("Home." + p.getUniqueId().toString() + "." + args[1].toString() + ".Y");
				Double z = plugin.getHomesConfig().getDouble("Home." + p.getUniqueId().toString() + "." + args[1].toString() + ".Z");
				Float Yaw = (float) plugin.getHomesConfig().getLong("Home." + p.getUniqueId().toString() + "." + args[1].toString() + ".Yaw");
				Float Pitch = (float) plugin.getHomesConfig().getLong("Home." + p.getUniqueId().toString() + "." + args[1].toString() + ".Pitch");
				World world = Bukkit.getWorld(plugin.getHomesConfig().getString("Home." + p.getUniqueId().toString() + "." + args[1].toString() + ".World"));
				
				Location home = new Location(world, x, y, z, Yaw, Pitch);
				
				p.teleport(home);
				p.sendMessage(prefixAdmin + ChatColor.AQUA + "Vous avez été téléporter au home " + ChatColor.RED + args[1] + ChatColor.AQUA + " de " + ChatColor.RED + player.getName());

			}else if(cmd.getName().equalsIgnoreCase("delhomes")) {
				
				if(!p.hasPermission("sethome.delhomes")) {
					p.sendMessage(prefixPerm + ChatColor.RED + "Vous n'avez pas les permissions suffisantes pour exécuter cette commande ! Veuillez vous diriger vers un membre du staff si vous pensez que vous "
							+ "devriez avoir accès à cette commande.");
					return false;
				}
				
				if(args.length == 0 || args.length == 1) {
					p.sendMessage(prefixError + ChatColor.RED + "La commande est /delhome <Nom du Joueur> <Nom du home> !");
					return false;
				}
				
				try {
					Bukkit.getPlayerExact(args[0].toString()).isOnline();
				}catch (Exception e) {
					p.sendMessage(prefixError + ChatColor.RED + "Le joueur " + ChatColor.AQUA + args[0].toString() + ChatColor.RED + " n'est pas connecté ou n'existe pas !");
					return false;
				}
				
				Player player = Bukkit.getPlayer(args[0].toString()); 
				
				ArrayList<String> homes = getHomes(player);
				
				if(!homes.contains(args[1])) {
					p.sendMessage(prefixError + ChatColor.AQUA + player.getName() + ChatColor.RED + " n'a pas de home qui s'appelle " + ChatColor.AQUA + args[1]);
					return false;
				}
				
				plugin.getHomesConfig().set("Home." + player.getUniqueId().toString() + "." + args[1].toString(), null);
				
				try {
					plugin.getHomesConfig().save(plugin.homesFile);
				}catch (IOException e) {
					e.printStackTrace();
				}
				
				p.sendMessage(prefixGame + ChatColor.AQUA + "L'home " + ChatColor.RED + args[1].toString() + ChatColor.AQUA + " de " + ChatColor.RED + player.getName() +ChatColor.AQUA + " a été supprimé !");
				
			}
			
		}
		
		return false;
	}
	
	ArrayList<String> getHomes(Player p){
		ArrayList<String> homes = new ArrayList<String>();
		for(String string : plugin.getHomesConfig().getConfigurationSection("Home." + p.getUniqueId().toString()).getKeys(false)) {
			homes.add(string);
		}
		return homes;
	}

}
