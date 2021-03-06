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
package pw.ollie.politics.world;

import pw.ollie.politics.Politics;
import pw.ollie.politics.data.Storable;
import pw.ollie.politics.group.Group;
import pw.ollie.politics.group.level.GroupLevel;
import pw.ollie.politics.universe.Universe;
import pw.ollie.politics.util.stream.CollectorUtil;
import pw.ollie.politics.world.plot.Plot;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;

import org.bson.BSONObject;
import org.bson.BasicBSONObject;
import org.bson.types.BasicBSONList;

import org.bukkit.World;

import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Holds Politics data specific to a single world.
 */
public final class PoliticsWorld implements Storable {
    private final String name;
    private final WorldConfig config;
    private final Table<Integer, Integer, Plot> chunkPlots;

    PoliticsWorld(String name, WorldConfig config) {
        this(name, config, HashBasedTable.create());
    }

    private PoliticsWorld(String name, WorldConfig config, Table<Integer, Integer, Plot> chunkPlots) {
        this.name = name;
        this.config = config;
        this.chunkPlots = chunkPlots;
    }

    public PoliticsWorld(String name, WorldConfig config, BasicBSONObject object) {
        this.name = object.getString("name", name);
        chunkPlots = HashBasedTable.create();
        BasicBSONList list = (BasicBSONList) object.get("plots");
        for (Object o : list) {
            if (!(o instanceof BasicBSONObject)) {
                throw new IllegalArgumentException("List must only contain more objects!");
            }
            BasicBSONObject plotObj = (BasicBSONObject) o;
            Plot p = new Plot(plotObj);
            chunkPlots.put(p.getChunk().getX(), p.getChunk().getZ(), p);
        }
        this.config = config;
    }

    public Stream<Universe> streamUniverses() {
        return Politics.getUniverseManager().streamUniverses().filter(this::hasUniverse);
    }

    /**
     * Gets a {@link Stream} of all {@link GroupLevel}s present in this world.
     *
     * @return all present GroupLevels in this world
     */
    public Stream<GroupLevel> streamWorldLevels() {
        return Politics.getUniverseManager().streamWorldLevels(this);
    }

    /**
     * Gets the name of the world.
     *
     * @return the world name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the Politics {@link WorldConfig} of this world.
     *
     * @return this world's configuration
     */
    public WorldConfig getConfig() {
        return config;
    }

    /**
     * Gets the Bukkit {@link World} associated with this PoliticsWorld.
     *
     * @return the relevant Bukkit World to this object
     */
    public World getWorld() {
        return Politics.getServer().getWorld(name);
    }

    /**
     * Gets the {@link Universe} associated with this world at the given {@link GroupLevel}.
     *
     * @param level the GroupLevel of the universe to get
     * @return the universe of the given level for this world
     */
    public Optional<Universe> getUniverse(GroupLevel level) {
        return Politics.getUniverseManager().getUniverse(this, level);
    }

    public boolean hasUniverse(Universe universe) {
        return universe.containsWorld(this);
    }

    public boolean hasGroup(Group group) {
        return streamUniverses().anyMatch(universe -> universe.hasGroup(group));
    }

    /**
     * Gets the {@link Plot} at the given chunk position in this world.
     *
     * @param x chunk x coordinate
     * @param z chunk z coordinate
     * @return the Plot at the given chunk position in this world
     */
    public Plot getPlotAtChunkPosition(int x, int z) {
        if (!config.hasPlots()) {
            return null;
        }

        Plot result = chunkPlots.get(x, z);
        if (result == null) {
            result = new Plot(this, x, z);
            chunkPlots.put(x, z, result);
        }
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PoliticsWorld that = (PoliticsWorld) o;
        return name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public BSONObject toBSONObject() {
        BasicBSONObject bson = new BasicBSONObject();
        bson.put("name", name);
        bson.put("plots", chunkPlots.values().stream().filter(Plot::shouldStore)
                .map(Plot::toBSONObject).collect(CollectorUtil.toBSONList()));
        return bson;
    }

    @Override
    public boolean shouldStore() {
        return true;
    }
}
