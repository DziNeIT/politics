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
import pw.ollie.politics.command.PoliticsSubcommand;
import pw.ollie.politics.command.args.Arguments;
import pw.ollie.politics.universe.Universe;
import pw.ollie.politics.universe.UniverseRules;
import pw.ollie.politics.util.message.MessageBuilder;
import pw.ollie.politics.world.PoliticsWorld;

import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class UniverseCreateCommand extends PoliticsSubcommand {
    UniverseCreateCommand() {
        super("create");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void runCommand(PoliticsPlugin plugin, CommandSender sender, Arguments args) throws CommandException {
        if (args.length() < 3) {
            throw new CommandException("Please specify name of new universe, name of ruleset to use, and names of worlds included (separated by ',' without spaces).");
        }

        String name = args.getString(0).toLowerCase();

        if (name.contains("/") || name.contains("\\")) {
            throw new CommandException("Slashes are not allowed in universe names.");
        }

        Universe existing = plugin.getUniverseManager().getUniverse(name).orElse(null);
        if (existing != null) {
            throw new CommandException("A universe named '" + name
                    + "' already exists. Please destroy it via 'universe destroy' if you wish to overwrite it.");
        }

        String rules = args.getString(1).toLowerCase();
        UniverseRules theRules = plugin.getUniverseManager().getRules(rules);
        if (theRules == null) {
            throw new CommandException("Invalid ruleset. To see the available rulesets, use /universe rules.");
        }

        String worldsStr = args.getString(2);
        List<PoliticsWorld> worlds = new ArrayList<>();
        if (worldsStr == null) {
            plugin.getServer().getWorlds().stream().map(plugin.getWorldManager()::getWorld).forEach(worlds::add);
        } else {
            Arrays.stream(worldsStr.split(",")).map(String::trim).map(plugin.getServer()::getWorld)
                    .filter(Objects::nonNull).map(plugin.getWorldManager()::getWorld).forEach(worlds::add);
        }

        if (worlds.size() <= 0) {
            throw new CommandException("There were no valid worlds specified.");
        }

        plugin.getUniverseManager().createUniverse(name, theRules, worlds);
        MessageBuilder.begin("You have created the universe '").highlight(name)
                .normal("' with the rules '").highlight(rules).normal("'.").send(sender);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getPermission() {
        return "politics.universe.create";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> getAliases() {
        return Arrays.asList("make", "new");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getUsage() {
        return "/universe create <universe> <ruleset> <world1,world2...>";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDescription() {
        return "Creates a new universe.";
    }
}
