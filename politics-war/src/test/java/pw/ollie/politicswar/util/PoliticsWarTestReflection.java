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
package pw.ollie.politicswar.util;

import pw.ollie.politicswar.war.War;

import org.bson.BasicBSONObject;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodHandles.Lookup;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public final class PoliticsWarTestReflection {
    private static final Lookup lookup = MethodHandles.lookup();

    public static War instantiateWar(BasicBSONObject bson) {
        try {
            Constructor warConstructor = War.class.getDeclaredConstructor(BasicBSONObject.class);
            warConstructor.setAccessible(true);
            MethodHandle newWarHandle = lookup.unreflectConstructor(warConstructor);
            return (War) newWarHandle.invoke(bson);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static void setWarActive(War war, boolean active) {
        try {
            Method setActiveMethod = War.class.getDeclaredMethod("setActive", boolean.class);
            setActiveMethod.setAccessible(true);
            MethodHandle setActiveHandle = lookup.unreflect(setActiveMethod);
            setActiveHandle.invoke(war, active);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private PoliticsWarTestReflection() {
        throw new UnsupportedOperationException();
    }
}
