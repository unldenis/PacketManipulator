package com.github.unldenis.packetmanipulator;

import com.github.unldenis.packetmanipulator.util.*;
import org.jetbrains.annotations.*;

import java.lang.reflect.*;
import java.util.*;
import java.util.concurrent.*;

public class WrappedPacket {

    private static final Map<Class<?>, Map<Class<?>, Long[]>> FIELD_OFFSET_CACHE = new ConcurrentHashMap<>();

    private final Class<?> packetClass;
    private final Object rawNMSPacket;

    public WrappedPacket(Object rawNMSPacket) {
        this(rawNMSPacket.getClass(), rawNMSPacket);
    }

    @ApiStatus.Internal
    private WrappedPacket(Class<?> packetClass, Object rawNMSPacket) {
        this.packetClass = packetClass;
        this.rawNMSPacket = rawNMSPacket;
    }

    //object
    public WrappedPacket writeObject(int index, Object value) {
        UnsafeUtil.setFinalObject(rawNMSPacket, UnsafeUtil.getOffsetField(packetClass.getDeclaredFields()[index]), value);
        return this;
    }

    public Object readObject(int index) {
        return UnsafeUtil.getFinalObject(rawNMSPacket, UnsafeUtil.getOffsetField(packetClass.getDeclaredFields()[index]));
    }

    //boolean
    public WrappedPacket writeBoolean(int index, boolean value) {
        UnsafeUtil.setFinal(rawNMSPacket, getField(boolean.class, index), value);
        return this;
    }

    public boolean readBoolean(int index) {
        return UnsafeUtil.getFinalBoolean(rawNMSPacket, getField(boolean.class, index));
    }

    //byte
    public WrappedPacket writeByte(int index, byte value) {
        UnsafeUtil.setFinal(rawNMSPacket, getField(byte.class, index), value);
        return this;
    }

    public byte readByte(int index) {
        return UnsafeUtil.getFinalByte(rawNMSPacket, getField(byte.class, index));
    }

    //short
    public WrappedPacket writeShort(int index, short value) {
        UnsafeUtil.setFinal(rawNMSPacket, getField(short.class, index), value);
        return this;
    }

    public short readShort(int index) {
        return UnsafeUtil.getFinalShort(rawNMSPacket, getField(short.class, index));
    }

    //int
    public WrappedPacket writeInt(int index, int value) {
        UnsafeUtil.setFinal(rawNMSPacket, getField(int.class, index), value);
        return this;
    }

    public int readInt(int index) {
        return UnsafeUtil.getFinalInt(rawNMSPacket, getField(int.class, index));
    }

    //long
    public WrappedPacket writeLong(int index, long value) {
        UnsafeUtil.setFinal(rawNMSPacket, getField(long.class, index), value);
        return this;
    }

    public long readLong(int index) {
        return UnsafeUtil.getFinalLong(rawNMSPacket, getField(long.class, index));
    }

    //float
    public WrappedPacket writeFloat(int index, float value) {
        UnsafeUtil.setFinal(rawNMSPacket, getField(float.class, index), value);
        return this;
    }

    public float readFloat(int index) {
        return UnsafeUtil.getFinalFloat(rawNMSPacket, getField(float.class, index));
    }

    //double
    public WrappedPacket writeDouble(int index, double value) {
        UnsafeUtil.setFinal(rawNMSPacket, getField(double.class, index), value);
        return this;
    }

    public double readDouble(int index) {
        return UnsafeUtil.getFinalDouble(rawNMSPacket, getField(double.class, index));
    }

    //String
    public WrappedPacket writeString(int index, String value) {
        UnsafeUtil.setFinalObject(rawNMSPacket, getField(String.class, index), value);
        return this;
    }

    public String readString(int index) {
        return (String) UnsafeUtil.getFinalObject(rawNMSPacket, getField(String.class, index));
    }

    private Long getField(@NotNull Class<?> type, int index) {
        Map<Class<?>, Long[]> cached = FIELD_OFFSET_CACHE.computeIfAbsent(packetClass, k -> new ConcurrentHashMap<>());
        Long[] fields = cached.computeIfAbsent(type, this::getFields);
        if(index >= fields.length) {
            throw new IndexOutOfBoundsException(String.format("The index field %d of type %s of class %s does not exist",
                    index, type.getSimpleName(), packetClass.getSimpleName()));
        }
        return fields[index];
    }

    private @NotNull Long[] getFields(@NotNull Class<?> type) {
        LinkedList<Long> fields = new LinkedList<>();
        for(Field field: packetClass.getDeclaredFields()) {
            if(field.getType().equals(type)) {
                fields.add(UnsafeUtil.getOffsetField(field));
            }
        }
        return fields.toArray(new Long[0]);
    }

    public @NotNull Class<?> getPacketClass() {
        return packetClass;
    }

    public @NotNull Object getRawNMSPacket() {
        return rawNMSPacket;
    }
}
