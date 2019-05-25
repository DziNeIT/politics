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
package pw.ollie.politics.group;

import gnu.trove.set.hash.THashSet;

import pw.ollie.politics.PoliticsPlugin;
import pw.ollie.politics.group.level.GroupLevel;
import pw.ollie.politics.universe.Universe;

import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Provides helper methods for accessing {@link Group}s and related features.
 */
public final class GroupManager {
    // todo docs
    private final PoliticsPlugin plugin;

    public GroupManager(PoliticsPlugin plugin) {
        this.plugin = plugin;

        PluginManager pluginManager = plugin.getServer().getPluginManager();
        pluginManager.registerEvents(new GroupMessageListener(plugin), plugin);
        pluginManager.registerEvents(new GroupProtectionListener(plugin), plugin);
    }

    public Set<Group> getAllGroups() {
        Set<Group> result = new THashSet<>();
        plugin.getUniverseManager().getUniverses().stream()
                .map(Universe::getGroups).forEach(result::addAll);
        return result;
    }

    public Set<Group> getAllCitizenGroups(UUID playerId) {
        Set<Group> result = new THashSet<>();
        plugin.getUniverseManager().getUniverses().stream()
                .map(universe -> universe.getCitizenGroups(playerId))
                .forEach(result::addAll);
        return result;
    }

    public Set<Group> getCitizenGroups(UUID playerId, Collection<Universe> universes) {
        return getAllGroups().stream()
                .filter(group -> universes.contains(group.getUniverse()))
                .filter(group -> group.isMember(playerId))
                .collect(Collectors.toSet());
    }

    public List<GroupLevel> getGroupLevels() {
        return plugin.getUniverseManager().getGroupLevels();
    }

    public GroupLevel getGroupLevel(String name) {
        for (GroupLevel level : getGroupLevels()) {
            if (level.getName().equalsIgnoreCase(name)) {
                return level;
            }
        }
        return null;
    }

    public Group getGroupById(int id) {
        return plugin.getUniverseManager().getGroupById(id);
    }

    public Group getGroupByTag(String tag) {
        return plugin.getUniverseManager().getGroupByTag(tag);
    }

    public boolean hasGroupOfLevel(Player player, GroupLevel level) {
        return getAllCitizenGroups(player.getUniqueId()).stream().map(Group::getLevel).anyMatch(level::equals);
    }

    public PoliticsPlugin getPlugin() {
        return plugin;
    }
}