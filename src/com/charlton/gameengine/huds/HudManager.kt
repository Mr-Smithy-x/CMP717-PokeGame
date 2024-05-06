package com.charlton.gameengine.huds

import com.charlton.gameengine.contracts.Renderable
import java.awt.Graphics
import kotlin.reflect.KClass

object HudManager : Renderable {

    private val manager: HashMap<KClass<HudComponent>, HudComponent> = hashMapOf()

    fun addHud(component: HudComponent) {
        manager[component.javaClass.kotlin] = component
    }

    fun setVisible(component: HudComponent) {
        if (manager.containsKey(component.javaClass.kotlin)) {
            manager[component.javaClass.kotlin]?.visible = true
        }
    }

    fun setHidden(component: HudComponent) {
        if (manager.containsKey(component.javaClass.kotlin)) {
            manager[component.javaClass.kotlin]!!.visible = false
        }
    }

    override fun render(g: Graphics) {
        manager.forEach {
            it.value.render(g)
        }
    }

}