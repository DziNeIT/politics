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
package pw.ollie.politics.event.plot.subplot;

import pw.ollie.politics.world.plot.Plot;
import pw.ollie.politics.world.plot.Subplot;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

import java.util.UUID;

public class SubplotOwnerChangeEvent extends SubplotEvent implements Cancellable {
    private static final HandlerList handlers = new HandlerList();

    private final UUID oldOwner;
    private final UUID newOwner;

    private boolean cancelled;

    public SubplotOwnerChangeEvent(Plot plot, Subplot subplot, UUID oldOwner, UUID newOwner) {
        super(plot, subplot);
        this.oldOwner = oldOwner;
        this.newOwner = newOwner;
    }

    public OfflinePlayer getOldOwner() {
        return Bukkit.getOfflinePlayer(oldOwner);
    }

    public OfflinePlayer getNewOwner() {
        return Bukkit.getOfflinePlayer(newOwner);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
