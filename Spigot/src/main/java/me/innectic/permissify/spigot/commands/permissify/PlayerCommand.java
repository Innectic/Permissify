package me.innectic.permissify.spigot.commands.permissify;

import me.innectic.permissify.api.PermissifyConstants;
import me.innectic.permissify.api.permission.Permission;
import me.innectic.permissify.api.permission.PermissionGroup;
import me.innectic.permissify.api.util.ArgumentUtil;
import me.innectic.permissify.spigot.PermissifyMain;
import me.innectic.permissify.spigot.commands.CommandResponse;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Innectic
 * @since 6/25/2017
 */
public class PlayerCommand {

    public CommandResponse handleAddPlayerToGroup(CommandSender sender, String[] args) {
        PermissifyMain plugin = PermissifyMain.getInstance();
        if (!plugin.getPermissifyAPI().getDatabaseHandler().isPresent())
            return new CommandResponse(PermissifyConstants.UNABLE_TO_ADD.replace("<REASON>", "No database handler."), false);
        if (!sender.hasPermission(PermissifyConstants.PERMISSIFY_PLAYER_GROUP_ADD))
            return new CommandResponse(PermissifyConstants.INSUFFICIENT_PERMISSIONS, false);
        if (args.length < 2) return new CommandResponse(PermissifyConstants.NOT_ENOUGH_ARGUMENTS_PLAYER_ADD_GROUP, false);
        Player targetPlayer = Bukkit.getPlayer(args[0]);
        if (targetPlayer == null) return new CommandResponse(PermissifyConstants.INVALID_PLAYER, false);
        Optional<PermissionGroup> group = plugin.getPermissifyAPI().getDatabaseHandler().get().getGroups().stream()
                .filter(permissionGroup -> permissionGroup.getName().equals(args[1])).findFirst();
        if (!group.isPresent()) return new CommandResponse(PermissifyConstants.INVALID_GROUP, false);
        plugin.getPermissifyAPI().getDatabaseHandler().get().addPlayerToGroup(targetPlayer.getUniqueId(), group.get());
        group.get().getPermissions().forEach(permission -> targetPlayer.addAttachment(plugin, permission.getPermission(), true));
        return new CommandResponse(PermissifyConstants.PLAYER_ADDED_TO_GROUP
                .replace("<PLAYER>", targetPlayer.getName()).replace("<GROUP>", group.get().getName()), true);
    }

    public CommandResponse handleRemovePlayerFromGroup(CommandSender sender, String[] args) {
        PermissifyMain plugin = PermissifyMain.getInstance();
        if (!plugin.getPermissifyAPI().getDatabaseHandler().isPresent())
            return new CommandResponse(PermissifyConstants.UNABLE_TO_REMOVE.replace("<REASON>", "No database handler."), false);
        if (!sender.hasPermission(PermissifyConstants.PERMISSIFY_PLAYER_GROUP_REMOVE))
            return new CommandResponse(PermissifyConstants.INSUFFICIENT_PERMISSIONS, false);
        if (args.length < 2) return new CommandResponse(PermissifyConstants.NOT_ENOUGH_ARGUMENTS_PLAYER_REMOVE_GROUP, false);
        Player targetPlayer = Bukkit.getPlayer(args[0]);
        if (targetPlayer == null) return new CommandResponse(PermissifyConstants.INVALID_PLAYER, false);
        Optional<PermissionGroup> group = plugin.getPermissifyAPI().getDatabaseHandler().get().getGroups().stream()
                .filter(permissionGroup -> permissionGroup.getName().equals(args[1])).findFirst();
        if (!group.isPresent()) return new CommandResponse(PermissifyConstants.INVALID_GROUP, false);
        plugin.getPermissifyAPI().getDatabaseHandler().get().removePlayerFromGroup(targetPlayer.getUniqueId(), group.get());
        group.get().getPermissions().forEach(permission -> targetPlayer.addAttachment(plugin, permission.getPermission(), false));
        return new CommandResponse(PermissifyConstants.PLAYER_REMOVED_FROM_GROUP
                .replace("<PLAYER>", targetPlayer.getName()).replace("<GROUP>", group.get().getName()), true);
    }

    public CommandResponse handleAddPermission(CommandSender sender, String[] args) {
        PermissifyMain plugin = PermissifyMain.getInstance();
        if (!plugin.getPermissifyAPI().getDatabaseHandler().isPresent())
            return new CommandResponse(PermissifyConstants.UNABLE_TO_ADD.replace("<REASON>", "No database handler."), false);
        if (!sender.hasPermission(PermissifyConstants.PERMISSIFY_PLAYER_PERMISSION_ADD))
            return new CommandResponse(PermissifyConstants.INSUFFICIENT_PERMISSIONS, false);
        if (args.length < 2) return new CommandResponse(PermissifyConstants.NOT_ENOUGH_ARGUMENTS_PLAYER_ADD_PERMMISSION, false);
        Player targetPlayer = Bukkit.getPlayer(args[0]);
        if (targetPlayer == null) return new CommandResponse(PermissifyConstants.INVALID_PLAYER, false);
        Optional<PermissionGroup> group = plugin.getPermissifyAPI().getDatabaseHandler().get().getGroups().stream()
                .filter(permissionGroup -> permissionGroup.getName().equals(args[1])).findFirst();
        if (!group.isPresent()) return new CommandResponse(PermissifyConstants.INVALID_GROUP, false);
        plugin.getPermissifyAPI().getDatabaseHandler().get().removePlayerFromGroup(targetPlayer.getUniqueId(), group.get());
        group.get().getPermissions().forEach(permission -> targetPlayer.addAttachment(plugin, permission.getPermission(), false));
        return new CommandResponse(PermissifyConstants.PERMISSION_ADDED_PLAYER
                .replace("<PLAYER>", targetPlayer.getName()).replace("<GROUP>", group.get().getName()), true);
    }

    public CommandResponse handleRemovePermission(CommandSender sender, String[] args) {
        PermissifyMain plugin = PermissifyMain.getInstance();
        if (!plugin.getPermissifyAPI().getDatabaseHandler().isPresent())
            return new CommandResponse(PermissifyConstants.UNABLE_TO_REMOVE.replace("<REASON>", "No database handler."), false);
        if (!sender.hasPermission(PermissifyConstants.PERMISSIFY_PLAYER_PERMISSION_REMOVE))
            return new CommandResponse(PermissifyConstants.INSUFFICIENT_PERMISSIONS, false);
        if (args.length < 2) return new CommandResponse(PermissifyConstants.NOT_ENOUGH_ARGUMENTS_PLAYER_REMOVE_PERMMISSION, false);
        Player targetPlayer = Bukkit.getPlayer(args[0]);
        if (targetPlayer == null) return new CommandResponse(PermissifyConstants.INVALID_PLAYER, false);
        List<String> remaining = Arrays.asList(ArgumentUtil.getRemainingArgs(1, args));
        plugin.getPermissifyAPI().getDatabaseHandler().get().removePermission(targetPlayer.getUniqueId(), remaining.toArray(new String[]{}));
        remaining.forEach(permission -> targetPlayer.addAttachment(plugin, permission, false));
        return new CommandResponse(PermissifyConstants.PERMISSION_REMOVED_PLAYER
                .replace("<PLAYER>", targetPlayer.getName()).replace("<PERMISSION>", String.join(", ", remaining)), true);
    }

    public CommandResponse handleListGroups(CommandSender sender, String[] args) {
        PermissifyMain plugin = PermissifyMain.getInstance();
        if (!plugin.getPermissifyAPI().getDatabaseHandler().isPresent())
            return new CommandResponse(PermissifyConstants.UNABLE_TO_LIST.replace("<REASON>", "No database handler."), false);
        if (!sender.hasPermission(PermissifyConstants.PERMISSIFY_PLAYER_GROUP_LIST))
            return new CommandResponse(PermissifyConstants.INSUFFICIENT_PERMISSIONS, false);
        if (args.length < 1) return new CommandResponse(PermissifyConstants.NOT_ENOUGH_ARGUMENTS_PLAYER_LIST_GROUP, false);
        Player targetPlayer = Bukkit.getPlayer(args[0]);
        if (targetPlayer == null) return new CommandResponse(PermissifyConstants.INVALID_PLAYER, false);
        List<String> groups = plugin.getPermissifyAPI().getDatabaseHandler().get().getGroups().stream()
                .filter(permissionGroup -> permissionGroup.hasPlayer(targetPlayer.getUniqueId()))
                .map(PermissionGroup::getName)
                .collect(Collectors.toList());
        return new CommandResponse(PermissifyConstants.PLAYER_GROUP_LIST.replace("<PLAYER>", targetPlayer.getName())
                .replace("<GROUPS>", String.join(", ", groups)), true);
    }

    public CommandResponse handleListPermissions(CommandSender sender, String[] args) {
        PermissifyMain plugin = PermissifyMain.getInstance();
        if (!plugin.getPermissifyAPI().getDatabaseHandler().isPresent())
            return new CommandResponse(PermissifyConstants.UNABLE_TO_LIST.replace("<REASON>", "No database handler."), false);
        if (!sender.hasPermission(PermissifyConstants.PERMISSIFY_PLAYER_PERMISSION_LIST))
            return new CommandResponse(PermissifyConstants.INSUFFICIENT_PERMISSIONS, false);
        if (args.length < 1) return new CommandResponse(PermissifyConstants.NOT_ENOUGH_ARGUMENTS_PLAYER_LIST_PERMMISSIONS, false);
        Player targetPlayer = Bukkit.getPlayer(args[0]);
        if (targetPlayer == null) return new CommandResponse(PermissifyConstants.INVALID_PLAYER, false);
        // This sucks, can probably be cleanup up.
        List<PermissionGroup> groups = plugin.getPermissifyAPI().getDatabaseHandler().get().getGroups().stream()
                .filter(permissionGroup -> permissionGroup.hasPlayer(targetPlayer.getUniqueId()))
                .collect(Collectors.toList());
        List<String> permissions = new ArrayList<>();
        groups.forEach(group -> permissions.addAll(group.getPermissions().stream().map(Permission::getPermission).collect(Collectors.toList())));
        permissions.addAll(plugin.getPermissifyAPI().getDatabaseHandler().get().getPermissions(targetPlayer.getUniqueId())
                .stream().map(Permission::getPermission).collect(Collectors.toList()));
        return new CommandResponse(PermissifyConstants.PLAYER_GROUP_LIST.replace("<PLAYER>", targetPlayer.getName())
                .replace("<GROUPS>", String.join(", ", permissions)), true);
    }
}
