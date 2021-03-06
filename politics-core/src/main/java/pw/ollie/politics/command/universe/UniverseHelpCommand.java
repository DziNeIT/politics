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
package pw.ollie.politics.command.universe;

import pw.ollie.politics.PoliticsPlugin;
import pw.ollie.politics.command.CommandException;
import pw.ollie.politics.command.PoliticsCommandHelper;
import pw.ollie.politics.command.PoliticsSubcommand;
import pw.ollie.politics.command.args.Argument;
import pw.ollie.politics.command.args.Arguments;

import org.bukkit.command.CommandSender;

public class UniverseHelpCommand extends PoliticsSubcommand {
    UniverseHelpCommand() {
        super("help");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void runCommand(PoliticsPlugin plugin, CommandSender sender, Arguments args) throws CommandException {
        int pageNumber = 1;
        if (args.length(false) > 0) {
            Argument first = args.get(0, false);
            if (first.isInt()) {
                pageNumber = first.asInt();
            }
        }

        PoliticsCommandHelper.sendCommandHelp(sender, plugin.getCommandManager().getPoliticsCommand("universe"), pageNumber);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getPermission() {
        return "politics.universe.help";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getUsage() {
        return "/universe help";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDescription() {
        return "Gives help for universe commands.";
    }
}
