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
import pw.ollie.politics.event.group.GroupMemberJoinEvent;
import pw.ollie.politics.group.Group;
import pw.ollie.politics.group.GroupProperty;
import pw.ollie.politics.group.level.GroupLevel;
import pw.ollie.politics.util.message.MessageBuilder;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Optional;
import java.util.UUID;

public class GroupJoinCommand extends GroupSubcommand {
    GroupJoinCommand(GroupLevel groupLevel) {
        super("join", groupLevel);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void runCommand(PoliticsPlugin plugin, CommandSender sender, Arguments args) throws CommandException {
        if (!level.hasImmediateMembers()) {
            throw new CommandException("You cannot join a " + level.getName() + " other than through a sub-organisation.");
        }

        if (args.length(false) < 1) {
            throw new CommandException("There was no " + level.getName() + " specified to join.");
        }

        Optional<Group> groupLookup = plugin.getGroupManager().getGroupByTag(args.getString(0, false));
        if (!groupLookup.isPresent()) {
            throw new CommandException("That " + level.getName() + " does not exist.");
        }

        Group group = groupLookup.get();
        // safe cast as isPlayerOnly() returns true
        Player player = (Player) sender;
        if (!level.allowedMultiple() && plugin.getGroupManager().hasGroupOfLevel(player, level)) {
            throw new CommandException("You are already part of a " + level.getName() + ".");
        }

        if (!group.getBooleanProperty(GroupProperty.OPEN, false) && !group.isInvited(player)) {
            throw new CommandException("That " + level.getName() + " is closed and you don't have an invitation.");
        }

        GroupMemberJoinEvent joinEvent = PoliticsEventFactory.callGroupMemberJoinEvent(group, player, level.getInitial());
        if (joinEvent.isCancelled()) {
            throw new CommandException("You may not join that " + level.getName() + ".");
        }

        UUID playerId = player.getUniqueId();
        group.setRole(playerId, joinEvent.getRole());
        MessageBuilder.begin("Successfully joined ").highlight(group.getName()).normal(".").send(sender);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getPermission() {
        return getBasePermissionNode() + ".join";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getUsage() {
        return "/" + level.getId() + " join <" + level.getName() + ">";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDescription() {
        return "Joins the " + level.getName() + ".";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isPlayerOnly() {
        return true;
    }
}
