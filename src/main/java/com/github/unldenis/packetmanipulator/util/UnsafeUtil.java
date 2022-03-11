package com.github.unldenis.packetmanipulator.util;


import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.NoSuchElementException;

public class UnsafeUtil {

    private static final Unsafe unsafe = getUnsafe();

    public static Object allocateInstance(Class<?> clazz) throws InstantiationException {
        return unsafe.allocateInstance(clazz);
    }

    public static void throwException(Throwable exception) {
        unsafe.throwException(exception);
    }

    @SuppressWarnings("unchecked")
    public static <U> U shallowCopy(U obj) throws InstantiationException {
        U instance = (U) unsafe.allocateInstance(obj.getClass());
        long start = toAddress(obj);
        long size = sizeOf(instance);
        long address = toAddress(instance);
        unsafe.copyMemory(start, address, size);
        return (U) fromAddress(address);
    }

    public static long getOffsetField(Field field) {
        return unsafe.objectFieldOffset(field);
    }

    //object
    public static void setFinalObject(Object object, long offset, Object value) {
        unsafe.putObject(object, offset, value);
    }

    public static Object getFinalObject(Object object, long offset) {
        return unsafe.getObject(object, offset);
    }

    //int
    public static void setFinal(Object object, long offset, int value) {
        unsafe.putInt(object, offset, value);
    }

    public static int getFinalInt(Object object, long offset) {
        return unsafe.getInt(object, offset);
    }

    //double
    public static void setFinal(Object object, long offset, double value) {
        unsafe.putDouble(object, offset, value);
    }

    public static double getFinalDouble(Object object, long offset) {
        return unsafe.getDouble(object, offset);
    }

    //float
    public static void setFinal(Object object, long offset, float value) {
        unsafe.putFloat(object, offset, value);
    }

    public static float getFinalFloat(Object object, long offset) {
        return unsafe.getFloat(object, offset);
    }

    //boolean
    public static void setFinal(Object object, long offset, boolean value) {
        unsafe.putBoolean(object, offset, value);
    }

    public static boolean getFinalBoolean(Object object, long offset) {
        return unsafe.getBoolean(object, offset);
    }

    //byte
    public static void setFinal(Object object, long offset, byte value) {
        unsafe.putByte(object, offset, value);
    }

    public static byte getFinalByte(Object object, long offset) {
        return unsafe.getByte(object, offset);
    }

    //char
    public static void setFinal(Object object, long offset, char value) {
        unsafe.putChar(object, offset, value);
    }

    public static char getFinalChar(Object object, long offset) {
        return unsafe.getChar(object, offset);
    }

    //long
    public static void setFinal(Object object, long offset, long value) {
        unsafe.putLong(object, offset, value);
    }

    public static long getFinalLong(Object object, long offset) {
        return unsafe.getLong(object, offset);
    }

    //short
    public static void setFinal(Object object, long offset, short value) {
        unsafe.putShort(object, offset, value);
    }

    public static short getFinalShort(Object object, long offset) {
        return unsafe.getShort(object, offset);
    }

    private static long sizeOf(Object o) {
        Class<?> c = o.getClass();
        long maxSize = 0;
        while (c != Object.class) {
            for (Field f : c.getDeclaredFields()) {
                if ((f.getModifiers() & Modifier.STATIC) == 0) {
                    long offset = unsafe.objectFieldOffset(f);
                    if (offset > maxSize)
                        maxSize = offset;
                }
            }
            c = c.getSuperclass();
        }
        return ((maxSize/8) + 1) * 8;   // padding
    }

    private static long toAddress(Object obj) {
        Object[] array = new Object[] {obj};
        long baseOffset = unsafe.arrayBaseOffset(Object[].class);
        return normalize(unsafe.getInt(array, baseOffset));
    }

    private static Object fromAddress(long address) {
        Object[] array = new Object[] {null};
        long baseOffset = unsafe.arrayBaseOffset(Object[].class);
        unsafe.putLong(array, baseOffset, address);
        return array[0];
    }

    private static long normalize(int value) {
        if(value >= 0) return value;
        return (~0L >>> 32) & value;
    }

    private static Unsafe getUnsafe() {
        try {
            Field unsafeField = Unsafe.class.getDeclaredField("theUnsafe");
            unsafeField.setAccessible(true);
            return (Unsafe) unsafeField.get(null);
        }catch (NoSuchFieldException exception){
            throw new NoSuchElementException("Cannot find unsafe field in wrapper class");
        }catch (IllegalAccessException exception){
            throw new UnsupportedOperationException("Access to the unsafe wrapper has been blocked ", exception);
        }
    }


}
