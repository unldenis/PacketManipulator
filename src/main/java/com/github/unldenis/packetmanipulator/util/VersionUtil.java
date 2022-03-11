package com.github.unldenis.packetmanipulator.util;

import org.bukkit.*;
import org.jetbrains.annotations.*;

public class VersionUtil {

    public static final String VERSION;

    public static final String CLEAN_VERSION;

    static {
        String bpName = Bukkit.getServer().getClass().getPackage().getName();
        VERSION = bpName.substring(bpName.lastIndexOf(".") + 1);

        CLEAN_VERSION = VERSION.substring(0, VERSION.length() - 3);
    }

    public static boolean isCompatible(@NotNull VersionEnum ve){
        return VERSION.toLowerCase().contains(ve.toString().toLowerCase());
    }

    public static boolean isAbove(@NotNull VersionEnum ve) {
        return VersionEnum.valueOf(CLEAN_VERSION.toUpperCase()).getOrder() >= ve.getOrder();
    }

    public static boolean isBelow(@NotNull VersionEnum ve) {
        return VersionEnum.valueOf(CLEAN_VERSION.toUpperCase()).getOrder() <= ve.getOrder();
    }

    public static boolean isBetween(@NotNull VersionEnum ve1, @NotNull VersionEnum ve2) {
        return isAbove(ve1) && isBelow(ve2);
    }


    public enum VersionEnum {

        V1_8(1),
        V1_9(2),
        V1_10(3),
        V1_11(4),
        V1_12(5),
        V1_13(6),
        V1_14(7),
        V1_15(8),
        V1_16(9),
        V1_17(10),
        V1_18(11);

        private final int order;

        VersionEnum(int order) {
            this.order = order;
        }

        public int getOrder() {
            return order;
        }

    }
}


