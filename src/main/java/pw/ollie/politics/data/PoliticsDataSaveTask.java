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
package pw.ollie.politics.data;

import pw.ollie.politics.PoliticsPlugin;

import org.bukkit.scheduler.BukkitRunnable;

public final class PoliticsDataSaveTask extends BukkitRunnable {
    private final PoliticsPlugin plugin;

    public PoliticsDataSaveTask(PoliticsPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        if (plugin.getPoliticsConfig().areWarsEnabled()) {
            plugin.getWarManager().saveWars();
        }

        plugin.getWorldManager().saveWorlds();
        plugin.getUniverseManager().saveRules();
        plugin.getUniverseManager().saveUniverses();
    }
}
