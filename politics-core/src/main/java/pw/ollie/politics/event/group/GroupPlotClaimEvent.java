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
package pw.ollie.politics.event.group;

import pw.ollie.politics.event.Sourced;
import pw.ollie.politics.group.Group;
import pw.ollie.politics.world.plot.Plot;

import org.bukkit.command.CommandSender;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

/**
 * Called when a group attempts to claim a plot.
 * <p>
 * Note: this event being called and not being cancelled does <b>not</b> confirm that the group has taken ownership of
 * the plot. It is always followed by a {@link pw.ollie.politics.event.plot.PlotOwnerChangeEvent}.
 */
public class GroupPlotClaimEvent extends GroupPlotEvent implements Cancellable, Sourced {
    private static final HandlerList handlers = new HandlerList();

    private final CommandSender claimer;

    private boolean cancelled;

    public GroupPlotClaimEvent(Group group, Plot plot, CommandSender claimer) {
        super(group, plot);
        this.claimer = claimer;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CommandSender getSource() {
        return claimer;
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
