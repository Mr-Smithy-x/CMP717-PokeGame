package com.charlton.tilemap3d.utils;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.WritableImage;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class PixelParser {

    public static List<BufferedImage> parse(BufferedImage image, int pixel_width, int pixel_height) {
        int columns = image.getWidth() / pixel_width;
        int rows = image.getHeight() / pixel_height;
        List<BufferedImage> images = new ArrayList<>();
        for (int row = 0; row < rows; row++) {
            for (int column = 0; column < columns; column++) {
                images.add(image.getSubimage(column * pixel_width, row * pixel_height, pixel_width, pixel_height));
            }
        }
        return images;
    }

    public static void apply(Canvas tile_canvas, List<BufferedImage> tileImages, int pixel_height, int pixel_width) {
        double width = tile_canvas.getWidth();
        double height = tile_canvas.getHeight();
        int columns = (int) (width / pixel_width);
        int rows = (int) (height / pixel_height);
        for (int row = 0; row < rows; row++) {
            for (int column = 0; column < columns; column++) {
                int index = (row * columns) + column;
                BufferedImage bufferedImage = tileImages.get(index);
                tile_canvas.getGraphicsContext2D().drawImage(SwingFXUtils.toFXImage(bufferedImage, null), column * pixel_width, row * pixel_height);
            }
        }
    }

    public static void adjust(Canvas tile_canvas, List<BufferedImage> image, int pixel_width, int pixel_height) {
        double width = tile_canvas.getWidth();
        double height = tile_canvas.getHeight();
        System.out.printf("%sx%s", width, height);
        int columns = (int) (width / pixel_width);// + ((width % tile_canvas.getWidth() > 0) ? 1 : 0);
        int rows = (int) (height / pixel_height);// + ((height % tile_canvas.getHeight() > 0) ? 1 : 0);
        for (int row = 0; row < rows; row++) {
            for (int column = 0; column < columns; column++) {
                int index = (row * columns) + column;
                BufferedImage subimage = image.get(index);
                int px = column * pixel_width;
                int py = row * pixel_height;
                WritableImage img = SwingFXUtils.toFXImage(subimage, null);
                //tile_canvas.getGraphicsContext2D().drawImage(img, px, py, pixel_width, pixel_height);
                tile_canvas.getGraphicsContext2D().drawImage(img,0, 0, pixel_width, pixel_height, px, py, pixel_width-1, pixel_height-1);

            }
        }
    }
}
