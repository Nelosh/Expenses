package app

import app.ui.UIWindow

object Main {
    def main(args: Array[String]) = {
        val ui = new UIWindow
        ui.visible = true
    }
}
