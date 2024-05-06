package com.charlton.tilemap3d

import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.stage.Stage

class TileMapEditor : Application() {
    @Throws(Exception::class)
    override fun start(primaryStage: Stage) {
        val root = FXMLLoader.load<Parent>(javaClass.getResource("views/mainwindow.fxml"))
        primaryStage.title = "TileMap Editor"
        primaryStage.scene = Scene(root)
        primaryStage.show()
    }
}



fun main() {
    Application.launch(TileMapEditor::class.java, "Welcome")
}