package com.charlton.gameengine

interface Debuggable {

    fun inDebuggingMode(): Boolean {
        return System.getProperty("DEBUG", "false") == "true"
    }

    fun setDebug(debug: Boolean) {
        println("Debug set to: $debug")
        System.setProperty("DEBUG", debug.toString())
    }

}
