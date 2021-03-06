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
import pw.ollie.politics.command.PoliticsSubcommand;
import pw.ollie.politics.command.args.Arguments;
import pw.ollie.politics.util.message.MessageBuilder;

import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.List;

public class PoliticsAboutCommand extends PoliticsSubcommand {
    PoliticsAboutCommand() {
        super("about");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void runCommand(PoliticsPlugin plugin, CommandSender sender, Arguments args) {
        // todo configurable block message
        MessageBuilder.begin().highlight("Politics ").normal(plugin.getDescription().getVersion())
                .newLine().normal("Politics is a self-serve community management system.")
                .newLine().normal("Type ").highlight("/politics help").normal(" for general command help.")
                .send(sender);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getPermission() {
        return "politics.about";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> getAliases() {
        return Arrays.asList("info", "information");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getUsage() {
        return "/politics about";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDescription() {
        return "Provides basic information about Politics.";
    }
}
