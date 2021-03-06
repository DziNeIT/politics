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
package pw.ollie.politics.util.serial;

import pw.ollie.politics.Politics;
import pw.ollie.politics.PoliticsPlugin;

import org.bukkit.configuration.ConfigurationSection;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.logging.Level;

/**
 * Configuration-related utility methods for Politics.
 */
public final class ConfigUtil {
    /**
     * Gets the {@link ConfigurationSection} with the given name from the given parent ConfigurationSection. If there is
     * not a corresponding section, a new one is created, added to the given parent section and returned.
     *
     * @param parent the parent ConfigurationSection to get a subsection of
     * @param name   the name of the section to get or create
     * @return a ConfigurationSection with the given name, as a child of the given parent
     */
    public static ConfigurationSection getOrCreateSection(ConfigurationSection parent, String name) {
        ConfigurationSection result = parent.getConfigurationSection(name);
        if (result == null) {
            result = parent.createSection(name);
        }
        return result;
    }

    /**
     * Copies a universe rules template from the running plugin's JAR to the rules directory.
     *
     * @param name the file name of the template to copy, as saved
     * @param as   the file name to save the copied template as inside the rules directory
     * @return whether the template was successfully copied
     */
    public static boolean copyUniverseRulesTemplate(String name, String as) {
        PoliticsPlugin plugin = Politics.getPlugin();
        plugin.saveResource("templates/" + name.toLowerCase() + ".yml", false);
        File savedPath = new File(plugin.getDataFolder(), "templates/" + name.toLowerCase() + ".yml");
        File toPath = new File(plugin.getFileSystem().getRulesDir(), as.toLowerCase() + ".yml");
        try {
            Files.move(savedPath.toPath(), toPath.toPath());
            File templatesDir = savedPath.getParentFile();
            if (templatesDir.isDirectory()) {
                templatesDir.delete();
            }
            return true;
        } catch (IOException e) {
            Politics.getLogger().log(Level.SEVERE, "Could not write template as " + toPath.getPath() + "!", e);
            return false;
        }
    }

    private ConfigUtil() {
        throw new UnsupportedOperationException();
    }
}
