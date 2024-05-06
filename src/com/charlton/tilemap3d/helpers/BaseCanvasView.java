package com.charlton.tilemap3d.helpers;

import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.ScrollBar;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

import java.io.File;

public class BaseCanvasView implements EventHandler<MouseEvent> {

    public static final int MODE_DEFAULT = 0;
    public static final int MODE_SELECTED = 1;
    public static final int MODE_ENTERED = 2;
    public static final int MODE_EXITED = 3;
    public static final int MODE_RELEASED = 4;

    public ScrollBar createScrollBar(Orientation orientation, double fullSize, double canvasSize) {
        ScrollBar sb = new ScrollBar();
        sb.setOrientation(orientation);
        sb.setMax(fullSize - canvasSize);
        sb.setVisibleAmount(canvasSize);
        return sb;
    }

    public static final int UP   = KeyCode.UP.ordinal();
    public static final int DN   = KeyCode.DOWN.ordinal();
    public static final int LT   = KeyCode.LEFT.ordinal();
    public static final int RT   = KeyCode.RIGHT.ordinal();

    public static final int _A   = KeyCode.A.ordinal();
    public static final int _B   = KeyCode.B.ordinal();
    public static final int _C   = KeyCode.C.ordinal();
    public static final int _D   = KeyCode.D.ordinal();
    public static final int _E   = KeyCode.E.ordinal();
    public static final int _F   = KeyCode.F.ordinal();
    public static final int _G   = KeyCode.G.ordinal();
    public static final int _H   = KeyCode.H.ordinal();
    public static final int _I   = KeyCode.I.ordinal();
    public static final int _J   = KeyCode.J.ordinal();
    public static final int _K   = KeyCode.K.ordinal();
    public static final int _L   = KeyCode.L.ordinal();
    public static final int _M   = KeyCode.M.ordinal();
    public static final int _N   = KeyCode.N.ordinal();
    public static final int _O   = KeyCode.O.ordinal();
    public static final int _P   = KeyCode.P.ordinal();
    public static final int _Q   = KeyCode.Q.ordinal();
    public static final int _R   = KeyCode.R.ordinal();
    public static final int _S   = KeyCode.S.ordinal();
    public static final int _T   = KeyCode.T.ordinal();
    public static final int _U   = KeyCode.U.ordinal();
    public static final int _V   = KeyCode.V.ordinal();
    public static final int _W   = KeyCode.W.ordinal();
    public static final int _X   = KeyCode.X.ordinal();
    public static final int _Y   = KeyCode.Y.ordinal();
    public static final int _Z   = KeyCode.Z.ordinal();

    public static final int _1 = KeyCode.DIGIT1.ordinal();
    public static final int _2 = KeyCode.DIGIT2.ordinal();
    public static final int _3 = KeyCode.DIGIT3.ordinal();
    public static final int _4 = KeyCode.DIGIT4.ordinal();
    public static final int _5 = KeyCode.DIGIT5.ordinal();
    public static final int _6 = KeyCode.DIGIT6.ordinal();
    public static final int _7 = KeyCode.DIGIT7.ordinal();
    public static final int _8 = KeyCode.DIGIT8.ordinal();
    public static final int _9 = KeyCode.DIGIT9.ordinal();

    public static final int CTRL = KeyCode.CONTROL.ordinal();
    public static final int SHFT = KeyCode.SHIFT.ordinal();
    public static final int ALT  = KeyCode.ALT.ordinal();

    public static final int SPACE = KeyCode.SPACE.ordinal();

    public static final int COMMA      = KeyCode.COMMA.ordinal();
    public static final int PERIOD     = KeyCode.PERIOD.ordinal();
    public static final int SLASH      = KeyCode.SLASH.ordinal();
    public static final int SEMICOLON  = KeyCode.SEMICOLON.ordinal();
    public static final int COLON      = KeyCode.COLON.ordinal();
    public static final int QUOTE      = KeyCode.QUOTE.ordinal();
    
    boolean[] pressing = new boolean[1024];
    protected int mode = MODE_DEFAULT;

    protected Canvas canvas;
    protected File file;
    protected double last_y;
    protected double last_x;
    protected int tile_width;
    protected int tile_height;


    public void draw(WritableImage toFXImage) {
        canvas.getGraphicsContext2D().drawImage(toFXImage, last_x, last_y);
    }

    public void clear() {
        canvas.getGraphicsContext2D().clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }

    public BaseCanvasView(Canvas canvas, File file) {
        this.canvas = canvas;
        this.file = file;
        this.canvas.setOnMouseClicked(this);
        this.canvas.setOnMouseDragEntered(this);
        this.canvas.setOnMouseDragReleased(this);
        this.canvas.setOnMouseDragged(this);
        this.canvas.setOnMouseDragEntered(this);
        this.canvas.setOnMouseDragExited(this);
        this.canvas.setOnMouseEntered(this);
        this.canvas.setOnMouseExited(this);
        this.canvas.setOnKeyPressed(this::keyPressed);
        this.canvas.setOnKeyReleased(this::keyReleased);
        this.canvas.setFocusTraversable(true);
    }


    public void setFile(File file) {
        this.file = file;
    }

    public File getFile() {
        return file;
    }

    @Override
    public void handle(MouseEvent event) {

    }


    public void setPixelDimension(int tile_width, int tile_height) {
        this.tile_width = tile_width;
        this.tile_height = tile_height;
    }


    public void keyPressed(KeyEvent e)
    {
        System.out.printf("IS SPACE PRESSED", e.getCode().ordinal() == SPACE);
        pressing[e.getCode().ordinal()] = true;
    }

    public void keyReleased(KeyEvent e)
    {
        pressing[e.getCode().ordinal()] = false;
    }


}
