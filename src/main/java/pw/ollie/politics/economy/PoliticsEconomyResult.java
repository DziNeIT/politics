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
package pw.ollie.politics.economy;

/**
 * Represents the result of an economic event in Politics.
 */
public enum PoliticsEconomyResult {
    INSUFFICIENT_BALANCE,
    FAILURE,
    SUCCESS(true);

    private final boolean success;

    PoliticsEconomyResult() {
        this(false);
    }

    PoliticsEconomyResult(boolean success) {
        this.success = success;
    }

    public boolean isSuccess() {
        return success;
    }
}