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

import pw.ollie.politics.data.PoliticsFileSystem;
import pw.ollie.politics.group.privilege.PrivilegeManager;
import pw.ollie.politics.universe.UniverseManager;
import pw.ollie.politics.world.PlotManager;

import org.bukkit.plugin.java.JavaPlugin;

public final class Politics extends JavaPlugin {
    private static Politics instance;

    private PoliticsFileSystem fileSystem;
    private PrivilegeManager privilegeManager;
    private PlotManager plotManager;
    private UniverseManager universeManager;

    @Override
    public void onEnable() {
        instance = this;

        this.fileSystem = new PoliticsFileSystem(this);
        this.privilegeManager = new PrivilegeManager(this);
        this.plotManager = new PlotManager(this);
        this.universeManager = new UniverseManager(this);
    }

    @Override
    public void onDisable() {
        instance = null;
    }

    public PoliticsFileSystem getFileSystem() {
        return this.fileSystem;
    }

    public PrivilegeManager getPrivilegeManager() {
        return this.privilegeManager;
    }

    public PlotManager getPlotManager() {
        return this.plotManager;
    }

    public UniverseManager getUniverseManager() {
        return this.universeManager;
    }

    public static Politics instance() {
        return instance;
    }
}
