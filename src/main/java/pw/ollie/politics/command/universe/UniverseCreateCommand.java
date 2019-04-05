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
import pw.ollie.politics.command.PoliticsSubCommand;
import pw.ollie.politics.command.args.Arguments;
import pw.ollie.politics.universe.Universe;
import pw.ollie.politics.universe.UniverseRules;
import pw.ollie.politics.world.PoliticsWorld;

import org.bukkit.World;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UniverseCreateCommand extends PoliticsSubCommand {
    UniverseCreateCommand() {
        super("create");
    }

    @Override
    public void runCommand(PoliticsPlugin plugin, CommandSender sender, Arguments args) {
        if (args.length() < 3) {
            sender.sendMessage("Please specify name of new universe, name of ruleset to use, and names of worlds included (separated by ',' without spaces).");
            return;
        }

        String name = args.getString(0).toLowerCase();

        if (name.contains("/") || name.contains("\\")) {
            sender.sendMessage("Slashes are not allowed in universe names.");
            return;
        }

        Universe existing = plugin.getUniverseManager().getUniverse(name);
        if (existing != null) {
            sender.sendMessage("A universe named '" + name
                    + "' already exists. Please destroy it via 'universe destroy' if you wish to overwrite it.");
            return;
        }

        String rules = args.getString(1).toLowerCase();
        UniverseRules theRules = plugin.getUniverseManager().getRules(rules);
        if (theRules == null) {
            sender.sendMessage("Invalid ruleset. To see the available rulesets, use /universe rules.");
            return;
        }

        String worldsStr = args.getString(2);
        List<PoliticsWorld> worlds = new ArrayList<>();
        if (worldsStr == null) {
            for (World world : plugin.getServer().getWorlds()) {
                worlds.add(plugin.getPlotManager().getWorld(world));
            }
        } else {
            String[] worldNames = worldsStr.split(",");

            for (String worldName : worldNames) {
                String trimmed = worldName.trim();
                World world = plugin.getServer().getWorld(trimmed);
                if (world == null) {
                    continue;
                }

                PoliticsWorld pw = plugin.getPlotManager().getWorld(world);
                worlds.add(pw);
            }
        }

        if (worlds.size() <= 0) {
            sender.sendMessage("There were no valid worlds specified.");
            return;
        }

        Universe universe = plugin.getUniverseManager().createUniverse(name, theRules);
        plugin.getEventFactory().callUniverseCreateEvent(universe);
        sender.sendMessage("You have created the universe '" + name + "' with the rules '" + rules + "'.");
    }

    @Override
    public String getPermission() {
        return "politics.universe.create";
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("make", "new");
    }

    @Override
    public String getUsage() {
        return "/universe create <universe> <ruleset> <world1,world2...>";
    }
}