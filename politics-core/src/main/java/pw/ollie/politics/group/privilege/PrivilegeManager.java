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
package pw.ollie.politics.group.privilege;

import pw.ollie.politics.PoliticsPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

/**
 * Stores and provides access to {@link Privilege}s in Politics.
 */
public final class PrivilegeManager {
    // todo docs
    private final PoliticsPlugin plugin;
    private final Map<String, Privilege> privileges = new HashMap<>();

    public PrivilegeManager(PoliticsPlugin plugin) {
        this.plugin = plugin;

        loadDefaultPrivileges();
    }

    public PoliticsPlugin getPlugin() {
        return plugin;
    }

    private void loadDefaultPrivileges() {
        registerPrivileges(Privileges.Group.ALL);
        registerPrivileges(Privileges.Plot.ALL);
        registerPrivileges(Privileges.GroupPlot.ALL);
    }

    public boolean registerPrivilege(Privilege privilege) {
        // putIfAbsent as we don't want other plugins overriding default Politics privileges
        return privileges.putIfAbsent(privilege.getName(), privilege) == null;
    }

    public boolean registerPrivileges(Privilege... privileges) {
        return Stream.of(privileges).allMatch(this::registerPrivilege);
    }

    public Privilege getPrivilege(String name) {
        return privileges.get(name.toUpperCase().replaceAll(" ", "_"));
    }
}