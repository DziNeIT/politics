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

public class GroupDescriptionCommand extends GroupSubcommand {
    GroupDescriptionCommand(GroupLevel groupLevel) {
        super("description", groupLevel);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void runCommand(PoliticsPlugin plugin, CommandSender sender, Arguments args) throws CommandException {
        Group group = findGroup(sender, args);

        if (args.length(false) > 0) {
            if (!group.can(sender, Privileges.Group.SET_INFO) && !hasAdmin(sender)) {
                throw new CommandException("You don't have permissions to set the description of your " + level.getName() + "!");
            }

            StringBuilder descriptionBuilder = new StringBuilder();
            for (int i = 0; i < args.length(false); i++) {
                descriptionBuilder.append(args.getString(i, false)).append(' ');
            }

            group.setProperty(GroupProperty.DESCRIPTION, descriptionBuilder.toString());
            MessageUtil.message(sender, "The description of your " + level.getName() + " was set successfully!");
        } else {
            MessageUtil.message(sender, group.getStringProperty(GroupProperty.DESCRIPTION, "The " + level.getName() + " has no description."));
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getPermission() {
        return getBasePermissionNode() + ".description";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getUsage() {
        return "/" + level.getId() + " description [-g " + level.getName() + "]";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDescription() {
        return "View or set the description for a " + level.getName() + ".";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> getAliases() {
        return Collections.singletonList("desc");
    }
}
