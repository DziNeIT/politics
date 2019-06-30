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
package pw.ollie.politics.command;

import gnu.trove.map.hash.THashMap;

import com.google.mu.util.stream.BiStream;

import java.util.Map;

public class CommandException extends Exception {
    private static final long serialVersionUID = -5692868272498195396L;

    private Map<String, String> errorMessageVars;

    public CommandException(String key) {
        this(key, new THashMap<>());
    }

    public CommandException(String key, Map<String, String> errorMessageVars) {
        super(key);
        this.errorMessageVars = errorMessageVars;
    }

    public BiStream<String, String> streamErrorMessageVars() {
        return BiStream.from(errorMessageVars);
    }
}
