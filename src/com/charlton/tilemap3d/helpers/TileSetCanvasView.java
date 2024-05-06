package com.charlton.tilemap3d.helpers;

import javafx.geometry.Orientation;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Paint;
import com.charlton.tilemap3d.contracts.CanvasHandlerReferenceInterface;
import com.charlton.tilemap3d.utils.PixelParser;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TileSetCanvasView extends BaseCanvasView implements CanvasHandlerReferenceInterface {

    private BufferedImage tileSetImage;
    private int selected_index;
    private List<BufferedImage> tileImages;

    public TileSetCanvasView(Canvas canvas, File file,
                             int tile_width,
                             int tile_height) {
        super(canvas, file);
        this.setPixelDimension(tile_width, tile_height);
        try {
            initialize();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void initialize() throws IOException {
        if (tileSetImage != null) {
            tileSetImage.flush();
        }
        if (tileImages != null) {
            tileImages.forEach(Image::flush);
            tileImages.clear();
        }else{
            tileImages = new ArrayList<>();
        }
        clear();
        tileSetImage = ImageIO.read(file);
        canvas.setHeight(tileSetImage.getHeight());
        canvas.setWidth(tileSetImage.getWidth());
        tileImages.addAll(PixelParser.parse(tileSetImage, tile_width, tile_height));
        canvas.getGraphicsContext2D().setFill(Paint.valueOf("000000"));
        canvas.getGraphicsContext2D().fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        PixelParser.adjust(canvas, tileImages, tile_width, tile_height);
    }

    @Override
    public void setFile(File file) {
        super.setFile(file);
        try {
            initialize();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void handle(MouseEvent e) {
        if (e.getEventType() == MouseEvent.MOUSE_CLICKED) {
            double spx = e.getX();
            double spy = e.getY();
            int columns = (int) (canvas.getWidth() / tile_width);
            int rows = (int) (canvas.getHeight() / tile_height);
            int index_col = (int) (spx / tile_width);
            int index_row = (int) (spy / tile_height);
            int index = (index_row * columns) + index_col;
            this.selected_index = index;
            System.out.println(index);
            mode = MODE_SELECTED;
            System.out.println("SELECTED");
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

    @Override
    public TileSetCanvasView getReference() {
        return this;
    }

    public int getSelectedIndex() {
        return selected_index;
    }

    public BufferedImage getImage(int index) {
        return tileImages.get(index);
    }

    public BufferedImage getSubimage(int x, int y, int width, int height) {
        return tileSetImage.getSubimage(x, y, width, height);
    }
}
