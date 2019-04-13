/*
 * This file is part of Politics.
 *
 * Copyright (c) 2019 Oliver Stanley
 * Politics is licensed under the Affero General Public License Version 3.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package pw.ollie.politics.command.group;

import pw.ollie.politics.PoliticsPlugin;
import pw.ollie.politics.command.CommandException;
import pw.ollie.politics.command.args.Arguments;
import pw.ollie.politics.group.Group;
import pw.ollie.politics.group.level.GroupLevel;
import pw.ollie.politics.group.privilege.Privileges;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class GroupUninviteCommand extends GroupSubCommand {
    GroupUninviteCommand(GroupLevel groupLevel) {
        super("uninvite", groupLevel);
    }

    @Override
    public void runCommand(PoliticsPlugin plugin, CommandSender sender, Arguments args) throws CommandException {
        Group group = findGroup(sender, args);

        if (!group.can(sender, Privileges.Group.INVITE)) {
            throw new CommandException("You don't have permission to invite to the " + groupLevel.getName() + ".");
        }

        if (args.length(false) < 1) {
            throw new CommandException("There was no player specified to uninvite.");
        }

        Player player = plugin.getServer().getPlayer(args.getString(0, false));
        if (player == null) {
            throw new CommandException("That player is not online.");
        }

        UUID playerId = player.getUniqueId();
        if (!group.isInvited(playerId)) {
            throw new CommandException("That player is not invited.");
        }

        group.removeInvitation(playerId);
        sender.sendMessage("Successfully uninvited " + player.getName() + " to the " + groupLevel.getName() + ".");
    }

    @Override
    public String getPermission() {
        return getBasePermissionNode() + ".uninvite";
    }

    @Override
    public String getUsage() {
        return "/" + groupLevel.getId() + " uninvite <player> [-g " + groupLevel.getName() + "]";
    }

    @Override
    public String getDescription() {
        return "Uninvites a player to the " + groupLevel.getName() + ".";
    }
}