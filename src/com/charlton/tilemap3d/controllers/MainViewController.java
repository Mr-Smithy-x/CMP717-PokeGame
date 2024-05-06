package com.charlton.tilemap3d.controllers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import com.charlton.tilemap3d.contracts.CanvasImageReference;
import com.charlton.tilemap3d.helpers.TileMapCanvasView;
import com.charlton.tilemap3d.helpers.TileSetCanvasView;
import com.charlton.tilemap3d.models.Point;
import com.charlton.tilemap3d.models.Tile;
import com.charlton.tilemap3d.models.TileSet;

import java.awt.image.BufferedImage;
import java.io.*;

public class MainViewController implements PixelDialogController.PixelDialogHandler, CanvasImageReference, TileMapCanvasView.TileMapCallback {

    public Canvas map_canvas, tile_canvas;
    public MenuItem newMenuItem, openMenuItem, closeMenuItem, retileMenuItem, saveMenuItem;
    public ComboBox jCollisionComboBox, jObjectComboBox, jForegroundComboBox;
    public Label jTileOptions;
    private FileChooser.ExtensionFilter
            images = new FileChooser.ExtensionFilter("Image", "*.jpg", "*.png"),
            formats = new FileChooser.ExtensionFilter("Formats", "*.json");
    private FileChooser fileChooser = new FileChooser();
    private File fileImage, fileJson;
    private Stage stage;
    private Scene dialogScene;
    private TileSetCanvasView tileSetHandler;
    private TileMapCanvasView tileMapHandler;

    @FXML
    public void initialize() {
        newMenuItem.setOnAction(onMenuItemClicked);
        openMenuItem.setOnAction(onMenuItemClicked);
        closeMenuItem.setOnAction(onMenuItemClicked);
        retileMenuItem.setOnAction(onMenuItemClicked);
        saveMenuItem.setOnAction(onMenuItemClicked);
    }

    protected void showDialog() throws IOException {
        if (stage == null) {
            stage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../views/pixeldialog.fxml"));
            Parent root = loader.load();
            PixelDialogController controller = loader.getController();
            dialogScene = new Scene(root);
            stage.setScene(dialogScene);
            controller.setCloseHandler(this);
        }
        if (!stage.isShowing()) {
            stage.showAndWait();
        }
    }

    private EventHandler<ActionEvent> onMenuItemClicked = event -> {
        MenuItem source = (MenuItem) event.getSource();
        if (source == newMenuItem) {
            fileJson = null;
            fileChooser.setSelectedExtensionFilter(images);
            fileImage = fileChooser.showOpenDialog(null);
            if (fileImage != null) {
                try {
                    showDialog();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else if (source == openMenuItem) {
            fileChooser.setSelectedExtensionFilter(formats);
            File file = fileChooser.showOpenDialog(null);
            if (file != null) {
                try {
                    loadTemplate(file);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else if (source == closeMenuItem) {

        } else if (source == retileMenuItem) {
            if (fileImage != null) {
                try {
                    showDialog();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else if (source == saveMenuItem) {
            fileChooser.setTitle("Save");
            fileChooser.getExtensionFilters().addAll(formats);
            File jsonFile = fileChooser.showSaveDialog(stage);
            try {
                FileWriter fw = new FileWriter(jsonFile);
                fw.write(saveJson());
                fw.flush();
                fw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };

    protected String saveJson() {
        return new GsonBuilder().setPrettyPrinting().create().toJson(tileMapHandler.getTiles());
    }

    @Override
    public void close() {
        stage.close();
    }

    public void loadTemplate(File json) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(json));
        String stub;
        StringBuilder sb = new StringBuilder();
        while ((stub = br.readLine()) != null) {
            sb.append(stub);
        }
        br.close();
        TileSet tileSet = new Gson().fromJson(sb.toString(), TileSet.class);
        int tile_width = tileSet.getTileWidth();
        int tile_height = tileSet.getTileHeight();
        String imageFileString = String.format("%s/assets/sets/%s", System.getProperty("user.dir"), tileSet.getMapImage());
        fileImage = new File(imageFileString);
        fileJson = json;
        initialize_sets(tile_width, tile_height);
        tileMapHandler.setTiles(tileSet);
    }


    public void initialize_sets(int width, int height) {
        if (tileSetHandler == null) {
            tileSetHandler = new TileSetCanvasView(tile_canvas, fileImage, width, height);
        } else {
            tileSetHandler.clear();
            tileSetHandler.setPixelDimension(width, height);
            tileSetHandler.setFile(fileImage);
        }
        if (tileMapHandler == null) {
            tileMapHandler = new TileMapCanvasView(map_canvas, fileImage, width, height, this);
            tileMapHandler.setCanvasHandlerReference(tileSetHandler);
            tileMapHandler.setImageReference(this);
        } else {
            tileMapHandler.clear();
            tileMapHandler.setPixelDimension(width, height);
            tileMapHandler.setFile(fileImage);
        }
    }

    @Override
    public void submit(int width, int height) {
        initialize_sets(width, height);
        if (stage != null) {
            stage.close();
        }

    }

    @Override
    public BufferedImage getImage(int index) {
        return tileSetHandler.getImage(index);
    }

    @Override
    public BufferedImage getSubImage(int x, int y, int width, int height) {
        System.out.printf("X: %s, Y: %s, WIDTH: %s, HEIGHT: %s\n", x, y, width, height);
        return tileSetHandler.getSubimage(x, y, width, height);
    }

    @Override
    public BufferedImage getSubImageAtAddress(long address) {
        return getSubImageFromTile(Tile.create(address));
    }

    @Override
    public BufferedImage getSubImageFromTile(Tile tile) {
        return getSubImage(tile.getPositionX(), tile.getPositionY(), tile.getPixelW(), tile.getPixelH());
    }

    @Override
    public void onTileClicked(Point point, Tile tile) {
        jTileOptions.setText(String.format("Tile: (%s, %s)\nCol,Object: (%s,%s)\n",
                point.getX(), point.getY(),
                tile.isCollision(), tile.isObject()
        ));
    }
}
