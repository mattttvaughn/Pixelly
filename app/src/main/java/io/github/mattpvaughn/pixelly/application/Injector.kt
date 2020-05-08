package io.github.mattpvaughn.pixelly.application

import io.github.mattpvaughn.pixelly.injection.components.AppComponent

class Injector private constructor() {
    companion object {
        fun get() : AppComponent = CustomApplication.get().appComponent
    }
}