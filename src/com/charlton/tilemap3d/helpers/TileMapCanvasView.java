package com.charlton.tilemap3d.helpers;

import javafx.embed.swing.SwingFXUtils;
import javafx.event.EventHandler;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.DragEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import com.charlton.tilemap3d.contracts.CanvasHandlerReferenceInterface;
import com.charlton.tilemap3d.contracts.CanvasImageReference;
import com.charlton.tilemap3d.models.Point;
import com.charlton.tilemap3d.models.Tile;
import com.charlton.tilemap3d.models.TileSet;

import java.awt.image.BufferedImage;
import java.io.File;

public class TileMapCanvasView extends BaseCanvasView implements EventHandler<MouseEvent> {

    private CanvasHandlerReferenceInterface canvasHandler;
    private CanvasImageReference imageReference;
    private TileMapCallback onTileMapCallback;
    private TileSet tileSet;
    private TileMapCallback tileMapCallback;
    private boolean collides;
    private boolean isobject;

    public interface TileMapCallback {
        void onTileClicked(Point point, Tile tile);
    }

    public TileMapCanvasView(Canvas canvas, TileSet tileSet, TileMapCallback tileMapCallback) {
        super(canvas, new File(String.format("%s/sets/%s", System.getProperty("user.dir"), tileSet.getMapImage())));
        this.tileSet = tileSet;
        this.tileMapCallback = tileMapCallback;
        setPixelDimension(tileSet.getTileWidth(), tileSet.getTileHeight());
    }

    public TileMapCanvasView(Canvas canvas, File file, int tile_width, int tile_height, TileMapCallback tileMapCallback) {
        super(canvas, file);
        this.tileMapCallback = tileMapCallback;
        tileSet = new TileSet();
        tileSet.setMapImage(file.getName());
        tileSet.setTileWidth(tile_width);
        tileSet.setTileHeight(tile_height);
        setPixelDimension(tile_width, tile_height);
        canvas.setOnDragDetected(this);
    }

    @Override
    public void setPixelDimension(int tile_width, int tile_height) {
        super.setPixelDimension(tile_width, tile_height);
        tileSet.setTileHeight(tile_height);
        tileSet.setTileWidth(tile_width);
    }

    @Override
    public void setFile(File file) {
        super.setFile(file);
        tileSet.setMapImage(file.getName());
        tileSet.clear();
        clear();
    }

    public void drawImage(MouseEvent e) {
        double spx = e.getX();
        double spy = e.getY();
        this.drawImage(spx, spy);
    }

    public void drawImage(double spx, double spy) {
        if (canvasHandler != null) {
            int selectedIndex = canvasHandler.getReference().getSelectedIndex();
            int spx_remainder = (int) (spx % tile_width);
            int spy_remainder = (int) (spy % tile_height);
            int real_column = (int) (spx - spx_remainder);
            int real_row = (int) (spy - spy_remainder);
            long pointKey = Point.toLong(real_column, real_row);
            long tileValue = 0;
            BufferedImage image = null;

            if (pressing[SPACE] && tileSet.has(pointKey)) {
                tileValue = tileSet.get(pointKey);
                tileValue = Tile.setCollision(tileValue, pressing[SHFT]);
                image = imageReference.getSubImageAtAddress(tileValue);
            } else if (pressing[CTRL] && tileSet.has(pointKey)) {
                tileValue = tileSet.get(pointKey);
                if (tileMapCallback != null) {
                    tileMapCallback.onTileClicked(Point.fromLong(pointKey), Tile.create(tileValue));
                }
                image = imageReference.getSubImageAtAddress(tileValue);
            } else {
                image = imageReference.getImage(selectedIndex);
                int tileGridXOffset = image.getTileGridXOffset();
                int tileGridYOffset = image.getTileGridYOffset();
                int tileGridZOffset = 0;
                tileValue = Tile.toLong(
                        Math.abs(tileGridXOffset) / tileSet.getTileWidth(),
                        Math.abs(tileGridYOffset) / tileSet.getTileHeight(),
                        Math.abs(tileGridZOffset) / tileSet.getTileWidth(),
                        image.getWidth(),
                        image.getHeight(),
                        collides,
                        isobject
                );
            }
            tileSet.put(pointKey, tileValue);
            canvas.getGraphicsContext2D().drawImage(SwingFXUtils.toFXImage(image, null), real_column, real_row);
            if (Tile.isCollisionTile(tileValue)) {
                Color rgb = Color.rgb(255, 0, 0, 0.2);
                canvas.getGraphicsContext2D().setFill(rgb);
                canvas.getGraphicsContext2D().fillRect(real_column, real_row, tile_width, tile_height);
            }
            if (Tile.isObjectTile(tileValue)) {
                Color rgb = Color.rgb(0, 255, 0, 0.2);
                canvas.getGraphicsContext2D().setFill(rgb);
                canvas.getGraphicsContext2D().fillOval(real_column, real_row, tile_width, tile_height);
            }
        }
    }


    @Override
    public void handle(MouseEvent e) {

        canvas.requestFocus();
        if (e.getEventType() == MouseEvent.MOUSE_CLICKED) {
            drawImage(e);
        }
        if (e.getEventType() == MouseEvent.MOUSE_PRESSED) {
            System.out.println("MOUSE PRESSING");
        }
        if (e.getEventType() == MouseEvent.MOUSE_DRAGGED) {
            drawImage(e);
        }
        if (e.getEventType() == MouseEvent.DRAG_DETECTED) {
            canvas.startDragAndDrop();
            System.out.println("DRAG_DETECTED");
        }
        if (e.getEventType() == MouseEvent.MOUSE_ENTERED_TARGET) {
            mode = MODE_ENTERED;
            System.out.println("ENTERED TARGET");
        }
        if (e.getEventType() == MouseEvent.MOUSE_EXITED_TARGET) {
            mode = MODE_EXITED;
            System.out.println("EXITED TARGET");
        }
        if (e.getEventType() == MouseEvent.MOUSE_ENTERED) {
            mode = MODE_ENTERED;
            System.out.println("ENTERED");
        }
        if (e.getEventType() == MouseEvent.MOUSE_EXITED) {
            mode = MODE_EXITED;
            System.out.println("EXITED");
        }
    }

    public TileSet getTiles() {
        return tileSet;
    }

    public void setImageReference(CanvasImageReference imageReference) {
        this.imageReference = imageReference;
    }

    public void setCanvasHandlerReference(CanvasHandlerReferenceInterface tileSetHandler) {
        this.canvasHandler = tileSetHandler;
    }


    public void setTiles(TileSet tileSet) {
        clear();
        if (this.tileSet != null) {
            this.tileSet.clear();
        }
        this.tileSet = tileSet;
        for (Point position : tileSet.pointIterator()) {
            long address = tileSet.get(position);
            Tile tile = Tile.create(address);
            BufferedImage subImage = imageReference.getSubImage(
                    Math.abs(tile.getPositionX()),
                    Math.abs(tile.getPositionY()),
                    tile_width,
                    tile_height
            );
            canvas.getGraphicsContext2D().drawImage(SwingFXUtils.toFXImage(subImage, null), position.getX(), position.getY());
            if (Tile.isCollisionTile(address)) {
                Color rgb = Color.rgb(255, 0, 0, 0.2);
                canvas.getGraphicsContext2D().setFill(rgb);
                canvas.getGraphicsContext2D().fillRect(position.getX(), position.getY(), tile_width, tile_height);
            }
            if (Tile.isObjectTile(address)) {
                Color rgb = Color.rgb(0, 255, 0, 0.2);
                canvas.getGraphicsContext2D().setFill(rgb);
                canvas.getGraphicsContext2D().fillOval(position.getX(), position.getY(), tile_width, tile_height);
            }
        }
    }


    public void handle(DragEvent event) {
        System.out.println("DRAGGING");
        drawImage(event.getX(), event.getY());
    }

    @Override
    public void keyPressed(KeyEvent e) {
        super.keyPressed(e);
        System.out.println("LOL");
    }

    @Override
    public void keyReleased(KeyEvent e) {
        super.keyReleased(e);
        System.out.println("LMAO");
        if (e.getCode() == KeyCode.Z) {
            isobject = !isobject;
        }
        if (e.getCode() == KeyCode.SHIFT) {
            collides = !collides;
        }
    }
}
