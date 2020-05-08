package io.github.mattpvaughn.pixelly.injection.modules

import dagger.Module
import dagger.Provides
import io.github.mattpvaughn.pixelly.application.MainActivity
import io.github.mattpvaughn.pixelly.injection.scopes.ActivityScope
import io.github.mattpvaughn.pixelly.navigation.Navigator

@Module
class ActivityModule(private val activity: MainActivity) {

    @Provides
    @ActivityScope
    fun navigator(): Navigator = Navigator(activity.supportFragmentManager)

}


