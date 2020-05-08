package io.github.mattpvaughn.pixelly.injection.components

import dagger.Component
import io.github.mattpvaughn.pixelly.application.MainActivity
import io.github.mattpvaughn.pixelly.application.MainActivityViewModelFactory
import io.github.mattpvaughn.pixelly.features.library.EditorFragment
import io.github.mattpvaughn.pixelly.injection.modules.ActivityModule
import io.github.mattpvaughn.pixelly.injection.scopes.ActivityScope
import io.github.mattpvaughn.pixelly.navigation.Navigator

@ActivityScope
@Component(dependencies = [AppComponent::class], modules = [ActivityModule::class])
interface ActivityComponent {
    fun mainActivityViewModelFactory(): MainActivityViewModelFactory
    fun navigator(): Navigator

    fun inject(activity: MainActivity)
    fun inject(editorFragment: EditorFragment)
}

