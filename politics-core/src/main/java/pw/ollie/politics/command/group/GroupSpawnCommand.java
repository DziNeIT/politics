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
import pw.ollie.politics.event.PoliticsEventFactory;
import pw.ollie.politics.event.group.GroupMemberSpawnEvent;
import pw.ollie.politics.group.Group;
import pw.ollie.politics.group.GroupProperty;
import pw.ollie.politics.group.level.GroupLevel;
import pw.ollie.politics.group.privilege.Privileges;
import pw.ollie.politics.util.message.MessageBuilder;
import pw.ollie.politics.util.message.MessageUtil;

import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GroupSpawnCommand extends GroupSubcommand {
    GroupSpawnCommand(GroupLevel groupLevel) {
        super("spawn", groupLevel);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void runCommand(PoliticsPlugin plugin, CommandSender sender, Arguments args) throws CommandException {
        Group group = findGroup(sender, args);

        if (!group.can(sender, Privileges.Group.SPAWN) && !hasAdmin(sender)) {
            throw new CommandException("You don't have permissions to spawn to that " + level.getName() + ".");
        }

        Location spawn = group.getLocationProperty(GroupProperty.SPAWN);
        if (spawn == null) {
            throw new CommandException("The " + level.getName() + " doesn't have a spawn!");
        }

        Player player = null;
        String playerName = null;
        if (args.hasValueFlag("p")) {
            if (!sender.hasPermission("politics.admin.group." + level.getId() + ".spawnother")) {
                throw new CommandException("You aren't allowed to spawn other players!");
            }

            playerName = args.getValueFlag("p").getStringValue();
            player = plugin.getServer().getPlayer(playerName);
        } else {
            if (sender instanceof Player) {
                player = (Player) sender;
            }
        }

        if (player == null) {
            throw new CommandException("The player wasn't specified, or the specified player is offline!");
        }

        GroupMemberSpawnEvent spawnEvent = PoliticsEventFactory.callGroupMemberSpawnEvent(group, player, sender);
        if (spawnEvent.isCancelled()) {
            throw new CommandException("Cannot teleport to spawn now.");
        }

        player.teleport(spawn);

        if (playerName != null) {
            MessageBuilder.begin().highlight(playerName).normal(" was teleported to the " + level.getName() + " spawn.")
                    .send(sender);
        }

        MessageUtil.message(sender, "You have been teleported to the " + level.getName() + " spawn.");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getPermission() {
        return getBasePermissionNode() + ".spawn";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getUsage() {
        return "/" + level.getId() + " spawn [-p player] [-g " + level.getName() + "] [-u universe]";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDescription() {
        return "Travel to the spawn location of a " + level.getName() + ".";
    }
}
