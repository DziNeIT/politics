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
package pw.ollie.politics.command.politics;

import pw.ollie.politics.PoliticsPlugin;
import pw.ollie.politics.command.PoliticsSubCommand;
import pw.ollie.politics.command.args.Arguments;
import pw.ollie.politics.group.level.GroupLevel;

import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.List;

public class PoliticsHelpCommand extends PoliticsSubCommand {
    PoliticsHelpCommand() {
        super("help");
    }

    @Override
    public void runCommand(PoliticsPlugin plugin, CommandSender sender, Arguments args) {
        sender.sendMessage("Politics " + plugin.getDescription().getVersion() + " - Command Overview");
        sender.sendMessage("Universe Commands: /universe help");
        for (GroupLevel groupLevel : plugin.getUniverseManager().getGroupLevels()) {
            sender.sendMessage(groupLevel.getName() + " Commands: /" + groupLevel.getName() + " help");
        }
    }

    @Override
    public String getPermission() {
        return "politics.help";
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("commands", "cmds", "h");
    }

    @Override
    public String getUsage() {
        return "/politics help";
    }
}
