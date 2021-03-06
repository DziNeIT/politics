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
import pw.ollie.politics.group.GroupProperty;
import pw.ollie.politics.group.level.GroupLevel;
import pw.ollie.politics.group.privilege.Privileges;
import pw.ollie.politics.util.message.MessageUtil;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

public class GroupMotdCommand extends GroupSubcommand {
    GroupMotdCommand(GroupLevel groupLevel) {
        super("motd", groupLevel);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void runCommand(PoliticsPlugin plugin, CommandSender sender, Arguments args) throws CommandException {
        Group group = findGroup(sender, args);

        if (args.length(false) > 0) {
            if (!group.can(sender, Privileges.Group.SET_INFO) && !hasAdmin(sender)) {
                throw new CommandException("You don't have permissions to set the MOTD of your " + level.getName() + "!");
            }

            StringBuilder motdBuilder = new StringBuilder();
            for (int i = 0; i < args.length(false); i++) {
                motdBuilder.append(args.getString(i, false)).append(' ');
            }

            group.setProperty(GroupProperty.MOTD, motdBuilder.toString());
            MessageUtil.message(sender, "The MOTD of your " + level.getName() + " was set successfully!");
        } else {
            if (sender instanceof Player && !group.isMember(((Player) sender).getUniqueId())) {
                throw new CommandException("You must be a member of the group to view its MOTD.");
            }

            MessageUtil.message(sender, group.getStringProperty(GroupProperty.MOTD, "The " + level.getName() + " has no MOTD."));
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getPermission() {
        return getBasePermissionNode() + ".motd";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getUsage() {
        return "/" + level.getId() + " motd [-g " + level.getName() + "]";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDescription() {
        return "View or set the MOTD for a " + level.getName() + ".";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> getAliases() {
        return Collections.singletonList("message");
    }
}
