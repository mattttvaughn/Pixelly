package io.github.mattpvaughn.pixelly.features.library

import android.graphics.Color
import android.util.Log
import androidx.annotation.ColorInt
import androidx.lifecycle.*
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import io.github.mattpvaughn.pixelly.application.APP_NAME
import io.github.mattpvaughn.pixelly.util.Event
import io.github.mattpvaughn.pixelly.util.NetworkState
import io.github.mattpvaughn.pixelly.util.postEvent
import io.github.mattpvaughn.pixelly.view.PixelCanvas
import io.github.mattpvaughn.pixelly.view.colorpicker.Utils


class EditorViewModel() : ViewModel() {

    private var _color = MutableLiveData(Color.BLACK)
    val color : LiveData<Int>
        get() = _color

    private var _pickColorEvent = MutableLiveData<Event<Int>>()
    val pickColorEvent: LiveData<Event<Int>>
        get() = _pickColorEvent

    private var _tool = MutableLiveData<PixelCanvas.Companion.Tool>()
    val tool : LiveData<PixelCanvas.Companion.Tool>
        get() = _tool

    fun setTool(tool: PixelCanvas.Companion.Tool) {
        _tool.postValue(tool)
    }

    fun pickColor(){
        _pickColorEvent.postEvent(color.value as Int)
    }

    fun setColor(color: Int) {
        _color.postValue(color)
        Log.i(APP_NAME, "Viewmodel color set to: ${Utils.parseColorToString(color)}")
    }


}
