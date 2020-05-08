package io.github.mattpvaughn.pixelly.navigation

import android.os.Bundle
import androidx.fragment.app.FragmentManager
import io.github.mattpvaughn.pixelly.R
import io.github.mattpvaughn.pixelly.features.library.EditorFragment

class Navigator(private val fragmentManager: FragmentManager) {

    fun openLibrary() {
        val libraryFragment = EditorFragment.newInstance()

        fragmentManager.beginTransaction().replace(R.id.fragNavHost, libraryFragment)
            .addToBackStack(null).commit()
    }
}