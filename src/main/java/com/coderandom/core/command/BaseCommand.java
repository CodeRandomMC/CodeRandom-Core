package com.coderandom.core.command;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

/**
 * Abstract base class for custom commands in the plugin.
 * Handles common command functionalities such as registration and permission checking.
 */
public abstract class BaseCommand extends BukkitCommand {

    /**
     * Constructs a new command with the specified parameters.
     *
     * @param plugin      the plugin instance
     * @param command     the name of the command
     * @param aliases     the aliases for the command
     * @param permission  the required permission to use the command
     * @param description the description of the command
     */
    public BaseCommand(Plugin plugin, String command, String[] aliases, String permission, String description) {
        super(command);
        setDescription(description);
        setPermission(permission);
        setUsage('/' + command);
        if (aliases != null) {
            setAliases(Arrays.asList(aliases));
        }

        registerCommand(plugin, command);
    }

    /**
     * Registers the command in the Bukkit command map.
     *
     * @param plugin  the plugin instance
     * @param command the name of the command to register
     */
    private void registerCommand(Plugin plugin, String command) {
        try {
            Field commandMapField = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            commandMapField.setAccessible(true);
            CommandMap commandMap = (CommandMap) commandMapField.get(Bukkit.getServer());
            commandMap.register(plugin.getName(), this);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            plugin.getLogger().severe("Failed to register command '" + command + "': " + e.getMessage());
        }
    }

    /**
     * Executes the command.
     *
     * @param commandSender the source of the command
     * @param alias         the alias of the command which was used
     * @param args          the arguments passed to the command
     * @return true if the command was handled successfully, otherwise false
     */
    @Override
    public boolean execute(CommandSender commandSender, String alias, String[] args) {
        // Prevents command block usage
        if (!(commandSender instanceof Player) && !(commandSender instanceof ConsoleCommandSender)) {
            commandSender.sendMessage("This command can only be used by a player or the console.");
            return true;
        }
        executeCommand(commandSender, args);
        return false;
    }

    /**
     * Abstract method to be implemented by subclasses with specific command logic.
     *
     * @param sender the source of the command
     * @param args   the arguments passed to the command
     */
    public abstract void executeCommand(CommandSender sender, String[] args);

    /**
     * Handles tab completion for the command.
     *
     * @param sender the source of the command
     * @param alias  the alias of the command which was used
     * @param args   the arguments passed to the command
     * @return a list of possible completions for the command
     * @throws IllegalArgumentException if an error occurs during tab completion
     */
    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
        return tabComplete(sender, args);
    }

    /**
     * Abstract method to be implemented by subclasses for handling tab completion.
     *
     * @param sender the source of the command
     * @param args   the arguments passed to the command
     * @return a list of possible completions for the command
     */
    public abstract List<String> tabComplete(CommandSender sender, String[] args);
}
