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
package pw.ollie.politics.world.plot;

import gnu.trove.iterator.TIntIterator;
import gnu.trove.list.TIntList;
import gnu.trove.list.array.TIntArrayList;
import gnu.trove.set.hash.THashSet;

import pw.ollie.politics.Politics;
import pw.ollie.politics.data.Storable;
import pw.ollie.politics.event.plot.PlotOwnerChangeEvent;
import pw.ollie.politics.group.Group;
import pw.ollie.politics.group.level.Role;
import pw.ollie.politics.group.privilege.Privilege;
import pw.ollie.politics.universe.Universe;
import pw.ollie.politics.util.Position;
import pw.ollie.politics.world.PoliticsWorld;

import org.bson.BSONObject;
import org.bson.BasicBSONObject;
import org.bson.types.BasicBSONList;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

/**
 * A plot in Politics is made up of exactly one chunk, and may have sub-plots.
 */
public final class Plot implements Storable {
    private final PoliticsWorld world;
    private final TIntList owners;
    private final Chunk chunk;
    private final int baseX;
    private final int baseZ;
    private final Set<Subplot> subplots; // todo add access / modification methods

    public Plot(PoliticsWorld world, int x, int z) {
        this(world, new TIntArrayList(), x, z, new HashSet<>());
    }

    public Plot(PoliticsWorld world, TIntList owners, int x, int z, Set<Subplot> subplots) {
        this.world = world;
        this.owners = owners;
        this.subplots = new THashSet<>(subplots);

        World bukkitWorld = world.getWorld();
        chunk = bukkitWorld.getChunkAt(x, z);

        baseX = x * 16;
        baseZ = z * 16;
    }

    public Plot(BasicBSONObject bObj) {
        world = Politics.getWorld(bObj.getString("world", null));
        if (bObj.containsField("owners")) {
            TIntList ownersList = new TIntArrayList();
            BasicBSONList ownersBSON = (BasicBSONList) bObj.get("owners");
            for (Object obj : ownersBSON) {
                if (!(obj instanceof Integer)) {
                    throw new IllegalArgumentException("obj is not an Integer!");
                }
                int val = (Integer) obj;
                ownersList.add(val);
            }
            owners = ownersList;
        } else {
            owners = new TIntArrayList();
        }

        subplots = new THashSet<>();
        if (bObj.containsField("subplots")) {
            BasicBSONList subplotsList = (BasicBSONList) bObj.get("subplots");
            for (Object element : subplotsList) {
                BasicBSONObject subplotBson = (BasicBSONObject) element;
                subplots.add(new Subplot(subplotBson));
            }
        }

        Object x = bObj.get("x");
        Object z = bObj.get("z");
        if (!(x instanceof Integer)) {
            throw new IllegalArgumentException("X was not available.");
        }
        if (!(z instanceof Integer)) {
            throw new IllegalArgumentException("Z was not available.");
        }
        World bukkitWorld = getPoliticsWorld().getWorld();
        chunk = bukkitWorld.getChunkAt((Integer) x, (Integer) z);
        baseX = chunk.getX() * 16;
        baseZ = chunk.getZ() * 16;
    }

    public PoliticsWorld getPoliticsWorld() {
        return world;
    }

    public final int getX() {
        return getBasePoint().getBlockX();
    }

    public final int getZ() {
        return getBasePoint().getBlockZ();
    }

    public Chunk getChunk() {
        return chunk;
    }

    public Location getBasePoint() {
        // I think 0 is good here for y?
        return new Location(chunk.getWorld(), baseX, 0, baseZ);
    }

    public boolean contains(Location location) {
        return baseX <= location.getBlockX() && baseX + 16 <= location.getBlockX()
                && baseZ <= location.getBlockZ() && baseZ + 16 <= location.getBlockZ();
    }

    public boolean contains(Position position) {
        return contains(position.toLocation());
    }

    public Set<Subplot> getSubplots() {
        return new THashSet<>(subplots);
    }

    public void addSubplot(Subplot subplot) {
        subplots.add(subplot);
    }

    public void removeSubplot(Subplot subplot) {
        subplots.remove(subplot);
    }

    public Subplot getSubplotAt(Location location) {
        for (Subplot subplot : subplots) {
            if (subplot.contains(location)) {
                return subplot;
            }
        }
        return null;
    }

    public Subplot getSubplotAt(Position position) {
        return getSubplotAt(position.toLocation());
    }

    public TIntList getOwnerIds() {
        return new TIntArrayList(owners);
    }

    public List<Group> getOwners() {
        List<Group> ret = new ArrayList<>();
        TIntIterator it = owners.iterator();
        while (it.hasNext()) {
            int id = it.next();
            Group group = Politics.getUniverseManager().getGroupById(id);
            if (group == null) {
                owners.remove(id); // Group no longer exists
            } else {
                ret.add(group);
            }
        }
        return ret;
    }

    public Group getOwner(Universe universe) {
        for (Group owner : getOwners()) {
            if (owner.getUniverse().equals(universe)) {
                return owner;
            }
        }
        return null;
    }

    public List<Group> getOwners(Universe universe) {
        List<Group> owners = new ArrayList<>();
        Group group = getOwner(universe);
        while (group != null) {
            owners.add(group);
            group = group.getParent();
        }
        return owners;
    }

    public boolean addOwner(int id) {
        return addOwner(Politics.getUniverseManager().getGroupById(id));
    }

    public boolean addOwner(Group group) {
        PlotOwnerChangeEvent event = Politics.getEventFactory().callPlotOwnerChangeEvent(this, group.getUid(), true);
        if (event.isCancelled()) {
            return false;
        }

        for (Group g : getOwners()) {
            if (g.equals(group)) {
                return false; // Already owns the plot
            }

            if (g.getUniverse().equals(group.getUniverse()) && g.equals(group.getParent())) {
                removeOwner(g);
                break; // ownership transfers to the group from its parent
            }
        }

        return owners.add(group.getUid());
    }

    public boolean removeOwner(int id) {
        if (!owners.contains(id)) {
            return true; // Not in there
        }
        PlotOwnerChangeEvent event = Politics.getEventFactory().callPlotOwnerChangeEvent(this, id, true);
        if (event.isCancelled()) {
            return false;
        }
        return owners.remove(id);
    }

    public boolean removeOwner(Group group) {
        return removeOwner(group.getUid());
    }

    public boolean isOwner(int id) {
        return owners.contains(id);
    }

    public boolean isOwner(Group group) {
        return isOwner(group.getUid());
    }

    // this works so that if the plot has one owner group, the player has whatever privileges afforded to their role in
    // the owner group, but if the plot has multiple owner groups the player must have a privilege in all of those owner
    // groups in order to be have it for the plot - seems reasonable but may want to consider if it's best at some point
    public Set<Privilege> getPrivileges(Player player) {
        Set<Privilege> privileges = new HashSet<>();
        UUID playerId = player.getUniqueId();
        TIntIterator groupIdIt = owners.iterator();
        boolean first = true;
        while (groupIdIt.hasNext()) {
            int groupId = groupIdIt.next();
            Group group = Politics.getGroupManager().getGroupById(groupId);
            Role playerRole = group.getRole(playerId);

            if (playerRole == null) {
                privileges.clear();
                break;
            }

            if (first) {
                privileges.addAll(playerRole.getPrivileges());
                first = false;
                continue;
            }

            privileges.retainAll(playerRole.getPrivileges());
        }
        return privileges;
    }

    @Override
    public BSONObject toBSONObject() {
        BasicBSONObject obj = new BasicBSONObject();
        obj.put("world", world.getName());
        obj.put("owners", owners); // todo I don't think we should be storing the TIntList directly? need to check this
        obj.put("x", getX());
        obj.put("z", getZ());
        if (!subplots.isEmpty()) {
            BasicBSONList subplotList = new BasicBSONList();
            for (Subplot subplot : subplots) {
                if (!subplot.canStore()) {
                    continue;
                }
                subplotList.add(subplot.toBSONObject());
            }
            obj.put("subplots", subplotList);
        }
        return obj;
    }

    @Override
    public boolean canStore() {
        return true;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Plot)) {
            return false;
        }
        Plot other = (Plot) obj;
        if (!Objects.equals(chunk, other.chunk)) {
            return false;
        }
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 79 * hash + (chunk != null ? chunk.hashCode() : 0);
        return hash;
    }
}
