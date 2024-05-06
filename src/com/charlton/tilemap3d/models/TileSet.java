package com.charlton.tilemap3d.models;


import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.function.Consumer;

public class TileSet implements Iterable<Long> {
    String map_image;
    LongMap tiles = new LongMap();
    int tile_width;
    int tile_height;

    public String getMapImage() {
        return map_image;
    }

    public void setMapImage(String map_image) {
        this.map_image = map_image;
    }

    public int getTileWidth() {
        return tile_width;
    }

    public void setTileWidth(int tile_width) {
        this.tile_width = tile_width;
    }

    public int getTileHeight() {
        return tile_height;
    }

    public void setTileHeight(int tile_height) {
        this.tile_height = tile_height;
    }

    public LongMap getTiles() {
        return tiles;
    }

    public void put(Long key, Long value) {
        tiles.put(key, value);
    }

    public void remove(Long key) {
        tiles.remove(key);
    }

    public void setTiles(@NotNull LongMap tiles) {
        this.tiles = tiles;
    }

    public void clear() {
        this.tiles.clear();
    }

    public void putAll(@NotNull LongMap tiles) {
        this.tiles.putAll(tiles);
    }

    public Iterable<Point> pointIterator() {
        return () -> tiles.keySet().stream().map(Point::fromLong).iterator();
    }

    @Override
    public Iterator<Long> iterator() {
        return this.tiles.keySet().iterator();
    }

    @Override
    public void forEach(Consumer<? super Long> action) {
        this.tiles.keySet().forEach(action);
    }

    @Override
    public Spliterator<Long> spliterator() {
        return this.tiles.keySet().spliterator();
    }

    public long get(Long key) {
        return this.tiles.get(key);
    }

    public long get(Point position) {
        return this.tiles.get(position.longValue());
    }

    public boolean has(long pointKey) {
        return this.tiles.containsKey(pointKey);
    }

    public static class LongMap extends HashMap<Long, Long> {
    }
}


