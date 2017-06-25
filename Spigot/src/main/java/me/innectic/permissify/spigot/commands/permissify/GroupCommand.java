/*
*
* This file is part of Permissify, licensed under the MIT License (MIT).
* Copyright (c) Innectic
* Copyright (c) contributors
*
* Permission is hereby granted, free of charge, to any person obtaining a copy
* of this software and associated documentation files (the "Software"), to deal
* in the Software without restriction, including without limitation the rights
* to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
* copies of the Software, and to permit persons to whom the Software is
* furnished to do so, subject to the following conditions:
*
* The above copyright notice and this permission notice shall be included in all
* copies or substantial portions of the Software.
*
* THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
* IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
* FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
* AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
* LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
* OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
* SOFTWARE.
 */
package me.innectic.permissify.spigot.commands.permissify;

import me.innectic.permissify.api.PermissifyConstants;
import me.innectic.permissify.spigot.PermissifyMain;
import me.innectic.permissify.spigot.commands.CommandResponse;
import me.innectic.permissify.spigot.utils.ColorUtil;
import org.bukkit.command.CommandSender;

/**
 * @author Innectic
 * @since 6/15/2017
 */
public class GroupCommand {

    /**
     * Handle the add permission subcommand
     *
     * @param sender the sender of the command
     * @param args   the extra arguments of the command
     * @return the response, and if it was successful
     */
    public CommandResponse handleAddPermission(CommandSender sender, String[] args) {
        // Check permissions and arguments
        if (!sender.hasPermission(PermissifyConstants.PERMISSIFY_GROUP_CREATE)) return new CommandResponse(PermissifyConstants.INSUFFICIENT_PERMISSIONS, false);
        if (args.length < 4) return new CommandResponse(PermissifyConstants.NOT_ENOUGH_ARGUMENTS_GROUP_ADD, false);
        if (!ColorUtil.isValidChatColor(args[3])) return new CommandResponse(PermissifyConstants.INVALID_CHATCOLOR.replace("<COLOR>", args[3]), true);
        // Check if there's even a database handler that can be used
        PermissifyMain plugin = PermissifyMain.getInstance();
        if (!plugin.getPermissifyAPI().getDatabaseHandler().isPresent())
            return new CommandResponse(PermissifyConstants.UNABLE_TO_CREATE.replace("<TYPE>", "group").replace("<REASON>", "No database handler."), false);
        // Create the new group
        boolean created = plugin.getPermissifyAPI().getDatabaseHandler().get().createGroup(args[0], args[1], args[2], args[3]);
        if (created) return new CommandResponse(PermissifyConstants.GROUP_CREATED.replace("<GROUP>", args[0]), true);
        return new CommandResponse(PermissifyConstants.UNABLE_TO_CREATE.replace("<TYPE>", "group").replace("<REASON>", "Unable to connect to database."), false);
    }

    /**
     * Handle the remove permission subcommand.
     *
     * @param sender the sender of the command
     * @param args the arguments of the command
     * @return the response, and if it was successful
     */
    public CommandResponse handleRemovePermission(CommandSender sender, String[] args) {
        // Check permissions and arguments
        if (!sender.hasPermission(PermissifyConstants.PERMISSIFY_GROUP_REMOVE)) return new CommandResponse(PermissifyConstants.INSUFFICIENT_PERMISSIONS, false);
        if (args.length < 1) return new CommandResponse(PermissifyConstants.NOT_ENOUGH_ARGUMENTS_GROUP_REMOVE, false);
        // Check if there's even a database handler that can be used
        PermissifyMain plugin = PermissifyMain.getInstance();
        if (!plugin.getPermissifyAPI().getDatabaseHandler().isPresent())
            return new CommandResponse(PermissifyConstants.UNABLE_TO_REMOVE.replace("<TYPE>", "group").replace("<REASON>", "No database handler"), false);
        boolean removed = plugin.getPermissifyAPI().getDatabaseHandler().get().deleteGroup(args[0]);
        if (removed) return new CommandResponse(PermissifyConstants.GROUP_REMOVED.replace("<GROUP>", args[0]), false);
        return new CommandResponse(PermissifyConstants.UNABLE_TO_REMOVE.replace("<TYPE>", "group").replace("<REASON>", "Unable to connect to database"), false);
    }
}
