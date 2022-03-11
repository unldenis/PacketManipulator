package com.github.unldenis.packetmanipulator.util;

import io.netty.channel.*;
import org.bukkit.entity.*;
import org.jetbrains.annotations.*;

import java.lang.reflect.*;

public class ReflectionUtils {

    private static Method GET_HANDLE;
    private static Field PLAYER_CONNECTION;
    private static Field NETWORK_MANAGER;
    private static Field CHANNEL;

    private static @Nullable Object getNMSPlayer(@NotNull Player player) {
        try {
            return GET_HANDLE.invoke(player);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static Object getPlayerConnection(@NotNull Player player) throws IllegalAccessException {
        Object nmsPlayer = getNMSPlayer(player);
        return PLAYER_CONNECTION.get(nmsPlayer);
    }

    public static Channel getPlayerChannel(@NotNull Player player) throws IllegalAccessException {
        Object playerConnection = getPlayerConnection(player);
        Object networkManager = NETWORK_MANAGER.get(playerConnection);
        return (Channel) CHANNEL.get(networkManager);
    }

    public static final String
            CRAFTBUKKIT = "org.bukkit.craftbukkit." + VersionUtil.VERSION + '.',
            NMS = VersionUtil.isAbove(VersionUtil.VersionEnum.V1_17) ? "net.minecraft." : "net.minecraft.server." + VersionUtil.VERSION + '.';

    static {
        Class<?> craftPlayer = getCraftClass("entity.CraftPlayer");
        Class<?> entityPlayer = getNMSClass("server.level", "EntityPlayer");
        Class<?> playerConnection = getNMSClass("server.network", "PlayerConnection");
        Class<?> networkManager = getNMSClass("network", "NetworkManager");

        try {
            GET_HANDLE = craftPlayer.getMethod("getHandle");
            if(VersionUtil.isAbove(VersionUtil.VersionEnum.V1_17)) {
                PLAYER_CONNECTION = entityPlayer.getField("b");
                NETWORK_MANAGER = playerConnection.getField("a");
                CHANNEL = networkManager.getField("k");
            } else {
                PLAYER_CONNECTION = entityPlayer.getField("playerConnection");
                NETWORK_MANAGER = playerConnection.getField("networkManager");
                CHANNEL = networkManager.getField("channel");
            }
        } catch (NoSuchMethodException | NoSuchFieldException e) {
            e.printStackTrace();
        }

    }

    public static @Nullable Class<?> getCraftClass(@NotNull String name) {
        try {
            return Class.forName(CRAFTBUKKIT + name);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
    public static Class<?> getNMSClass(@NotNull String newPackage, @NotNull String name) {
        if (VersionUtil.isAbove(VersionUtil.VersionEnum.V1_17)) {
            name = newPackage + '.' + name;
        }
        return getNMSClass(name);
    }

    public static @Nullable Class<?> getNMSClass(@NotNull String name) {
        try {
            return Class.forName(NMS + name);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

}
