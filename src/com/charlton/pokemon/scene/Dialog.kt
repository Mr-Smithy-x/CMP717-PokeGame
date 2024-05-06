package com.charlton.pokemon.scene

import com.charlton.pokemon.Global
import javax.swing.JOptionPane

object Dialog {

    fun questionWindow(
        title: String = "Dialog",
        message: String = "Question",
        onYes: () -> Unit,
        onNo: () -> Unit,
        onCancel: () -> Unit
    ) = when (JOptionPane.showConfirmDialog(
        Global.game.container, message, title,
        JOptionPane.YES_NO_OPTION,
        JOptionPane.QUESTION_MESSAGE
    )) {
        JOptionPane.YES_OPTION -> onYes()
        JOptionPane.NO_OPTION -> onNo()
        else -> onCancel()
    }


}