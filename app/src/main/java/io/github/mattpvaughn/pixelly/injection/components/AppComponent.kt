package io.github.mattpvaughn.pixelly.injection.components

import android.content.Context
import dagger.Component
import io.github.mattpvaughn.pixelly.application.CustomApplication
import io.github.mattpvaughn.pixelly.data.local.*
import io.github.mattpvaughn.pixelly.features.library.EditorViewModelFactory
import io.github.mattpvaughn.pixelly.injection.modules.AppModule
import retrofit2.Retrofit
import java.io.File
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
interface AppComponent {
    fun applicationContext(): Context
    fun internalFilesDir(): File
    fun externalDeviceDirs(): List<File>
    fun prefsRepo(): PrefsRepo
    fun editorViewModelFactory(): EditorViewModelFactory

    // Inject
    fun inject(customApplication: CustomApplication)
}