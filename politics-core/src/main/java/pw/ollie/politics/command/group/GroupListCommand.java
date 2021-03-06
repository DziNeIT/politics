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
import pw.ollie.politics.command.args.Argument;
import pw.ollie.politics.command.args.Arguments;
import pw.ollie.politics.group.Group;
import pw.ollie.politics.group.level.GroupLevel;
import pw.ollie.politics.universe.Universe;
import pw.ollie.politics.util.collect.PagedList;
import pw.ollie.politics.util.message.MessageBuilder;
import pw.ollie.politics.util.message.MessageUtil;
import pw.ollie.politics.util.stream.CollectorUtil;

import org.bukkit.command.CommandSender;

import java.util.List;

public class GroupListCommand extends GroupSubcommand {
    public static final int PAGE_HEIGHT = 6;

    GroupListCommand(GroupLevel groupLevel) {
        super("list", groupLevel);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void runCommand(PoliticsPlugin plugin, CommandSender sender, Arguments args) throws CommandException {
        Universe universe = findUniverse(sender, args);

        PagedList<Group> paged = universe.streamGroups(level).collect(CollectorUtil.toPagedList());
        if (paged.isEmpty()) {
            throw new CommandException("There are no " + level.getPlural() + "!");
        }

        int page = 1;
        if (args.length(false) > 0) {
            Argument arg1 = args.get(0, false);
            if (!arg1.isInt()) {
                throw new CommandException("Invalid page number supplied!");
            }

            page = arg1.asInt();
        }

        paged.setElementsPerPage(PAGE_HEIGHT);
        if (page > paged.pages()) {
            throw new CommandException("There are only " + paged.pages() + " pages!");
        }

        MessageBuilder message = MessageUtil.startBlockMessage(level.getPlural().toUpperCase());
        List<Group> pageGroups = paged.getPage(page);
        for (Group group : pageGroups) {
            message.newLine().highlight().append(group.getName());
        }
        message.build().send(sender);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getPermission() {
        return getBasePermissionNode() + ".list";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getUsage() {
        return "/" + level.getId() + " list [page]";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDescription() {
        return "Provides a list of " + level.getPlural() + ".";
    }
}
