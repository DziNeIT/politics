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
package pw.ollie.politics;

import pw.ollie.politics.activity.ActivityManager;
import pw.ollie.politics.command.PoliticsCommandManager;
import pw.ollie.politics.data.PoliticsFileSystem;
import pw.ollie.politics.group.Group;
import pw.ollie.politics.group.GroupManager;
import pw.ollie.politics.group.privilege.PrivilegeManager;
import pw.ollie.politics.group.war.WarManager;
import pw.ollie.politics.universe.UniverseManager;
import pw.ollie.politics.util.message.ColourScheme;
import pw.ollie.politics.util.visualise.Visualiser;
import pw.ollie.politics.world.PoliticsWorld;
import pw.ollie.politics.world.WorldManager;

import org.bukkit.Server;
import org.bukkit.World;

import java.util.logging.Logger;

public final class Politics {
    public static PoliticsPlugin getPlugin() {
        return PoliticsPlugin.instance();
    }

    public static Server getServer() {
        return Politics.getPlugin().getServer();
    }

    public static PoliticsFileSystem getFileSystem() {
        return Politics.getPlugin().getFileSystem();
    }

    public static PoliticsConfig getConfig() {
        return Politics.getPlugin().getPoliticsConfig();
    }

    public static ColourScheme getColourScheme() {
        return Politics.getConfig().getColourScheme();
    }

    public static PrivilegeManager getPrivilegeManager() {
        return Politics.getPlugin().getPrivilegeManager();
    }

    public static WorldManager getWorldManager() {
        return Politics.getPlugin().getWorldManager();
    }

    public static UniverseManager getUniverseManager() {
        return Politics.getPlugin().getUniverseManager();
    }

    public static GroupManager getGroupManager() {
        return Politics.getPlugin().getGroupManager();
    }

    public static ActivityManager getActivityManager() {
        return Politics.getPlugin().getActivityManager();
    }

    public static WarManager getWarManager() {
        return Politics.getPlugin().getWarManager();
    }

    public static PoliticsCommandManager getCommandManager() {
        return Politics.getPlugin().getCommandManager();
    }

    public static Visualiser getVisualiser() {
        return Politics.getPlugin().getVisualiser();
    }

    public static PoliticsWorld getWorld(World world) {
        return Politics.getWorldManager().getWorld(world);
    }

    public static PoliticsWorld getWorld(String world) {
        return Politics.getWorldManager().getWorld(world);
    }

    public static Group getGroupById(int id) {
        return Politics.getUniverseManager().getGroupById(id);
    }

    public static Logger getLogger() {
        return Politics.getPlugin().getLogger();
    }

    private Politics() {
        throw new UnsupportedOperationException();
    }
}
