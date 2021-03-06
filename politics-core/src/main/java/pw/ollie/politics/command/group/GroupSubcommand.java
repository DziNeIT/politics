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

import pw.ollie.politics.command.CommandException;
import pw.ollie.politics.command.PoliticsCommandHelper;
import pw.ollie.politics.command.PoliticsSubcommand;
import pw.ollie.politics.command.args.Arguments;
import pw.ollie.politics.group.Group;
import pw.ollie.politics.group.level.GroupLevel;
import pw.ollie.politics.universe.Universe;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public abstract class GroupSubcommand extends PoliticsSubcommand {
    protected final GroupLevel level;

    protected GroupSubcommand(String name, GroupLevel level) {
        super(name);
        this.level = level;
    }

    protected String getBasePermissionNode() {
        return "politics.group." + level.getId();
    }

    protected Universe getUniverse(Player player) {
        return getUniverse(level, player);
    }

    /**
     * Gets a Universe - first checking to see if one was specified, then falling back on the one currently relating to
     * the player if there isn't one specified.
     *
     * @param sender  the relevant source of the command
     * @param context the arguments provided
     * @return the universe relevant to the context
     * @throws CommandException if context is not sufficient to find a Universe
     */
    protected Universe findUniverse(CommandSender sender, Arguments context) throws CommandException {
        return findUniverse(level, sender, context);
    }

    /**
     * Gets a Group - first checking to see if one was specified, then falling back on the one currently relating to
     * the player if there isn't one specified.
     *
     * @param sender  the relevant source of the command
     * @param context the arguments provided
     * @return the group relevant to the context
     * @throws CommandException if context is not sufficient to find a Group
     */
    protected Group findGroup(CommandSender sender, Arguments context) throws CommandException {
        return findGroup(level, sender, context);
    }

    public boolean hasAdmin(CommandSender source) {
        return PoliticsCommandHelper.hasGroupsAdmin(source);
    }
}
