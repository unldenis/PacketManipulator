package com.github.unldenis.packetmanipulator;

import com.github.unldenis.packetmanipulator.util.*;
import io.netty.channel.*;
import org.bukkit.entity.*;
import org.jetbrains.annotations.*;

import java.util.*;
import java.util.concurrent.*;

public abstract class PacketListener extends ChannelDuplexHandler {

    private static final Map<UUID, PacketListener> LISTENERS = new ConcurrentHashMap<>();

    public static void eject(@NotNull Player player) {
        LISTENERS.computeIfPresent(player.getUniqueId(), ((uuid, packetListener) -> {
            try {
                Channel channel = ReflectionUtils.getPlayerChannel(player);
                channel.eventLoop().submit(() -> channel.pipeline().remove(packetListener));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            return null;
        }));
    }

    private final Player player;

    public PacketListener(@NotNull Player player) {
        this.player = player;
        inject();
        PacketListener.LISTENERS.put(player.getUniqueId(), this);
    }

    /**
     * Write PacketPlayOut...
     */
    @Override
    public void write(ChannelHandlerContext c, Object m, ChannelPromise promise) throws Exception {
        if(this.onPacketOut(new WrappedPacket(m))) {
            super.write(c, m, promise);
        }
    }

    /**
     * Read PacketPlayIn... and
     */
    @Override
    public void channelRead(ChannelHandlerContext c, Object m) throws Exception {
        if(this.onPacketIn(new WrappedPacket(m))) {
            super.channelRead(c, m);
        }
    }

    private void inject() {
        try {
            ChannelPipeline pipeline = ReflectionUtils.getPlayerChannel(player).pipeline();
            pipeline.addBefore("packet_handler", player.getUniqueId().toString(), this);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public abstract boolean onPacketOut(@NotNull WrappedPacket packet);

    public abstract boolean onPacketIn(@NotNull WrappedPacket packet);

    public @NotNull Player getPlayer() {
        return player;
    }
}
