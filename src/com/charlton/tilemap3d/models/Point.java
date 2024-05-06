package com.charlton.tilemap3d.models;

public class Point extends Number implements Comparable<Long> {

    int x;
    int y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Point setX(int x) {
        this.x = x;
        return this;
    }

    public Point setY(int y) {
        this.y = y;
        return this;
    }

    public long toLong(){
        return toLong(x,y);
    }

    public static long toLong(int position_x, int position_y) {
        long address = position_x;
        address = (address << 12) + position_y;
        return address;
    }

    public static Point fromLong(long position) {
        long position_x = (position >> 12) & 0xfff;
        long position_y = (position) & 0xfff;
        return new Point((int) position_x, (int) position_y);
    }

    @Override
    public int intValue() {
        return (int) longValue();
    }

    @Override
    public long longValue() {
        return toLong(this.x, this.y);
    }

    @Override
    public float floatValue() {
        return longValue();
    }

    @Override
    public double doubleValue() {
        return longValue();
    }

    @Override
    public int compareTo(Long o) {
        return Long.compare(toLong(), o);
    }

}
