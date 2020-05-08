package io.github.mattpvaughn.pixelly.features.library

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import io.github.mattpvaughn.pixelly.application.MainActivity
import io.github.mattpvaughn.pixelly.data.local.PrefsRepo
import io.github.mattpvaughn.pixelly.databinding.FragmentEditorBinding
import io.github.mattpvaughn.pixelly.navigation.Navigator
import io.github.mattpvaughn.pixelly.util.Event
import io.github.mattpvaughn.pixelly.view.PixelCanvas
import io.github.mattpvaughn.pixelly.view.colorpicker.ColorPickerDialog
import javax.inject.Inject

class EditorFragment : Fragment() {

    companion object {
        fun newInstance() = EditorFragment()
    }

    @Inject
    lateinit var prefsRepo: PrefsRepo

    @Inject
    lateinit var navigator: Navigator

    @Inject
    lateinit var viewModelFactory: EditorViewModelFactory

    override fun onAttach(context: Context) {
        (activity as MainActivity).activityComponent.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentEditorBinding.inflate(inflater, container, false)

        val viewModel = ViewModelProvider(this, viewModelFactory).get(EditorViewModel::class.java)

        binding.palette.setOnClickListener {
            viewModel.pickColor()
        }

        binding.penTool.setOnClickListener {
            binding.penTool.alpha = 1F
            binding.fillTool.alpha = 0.5F
            viewModel.setTool(PixelCanvas.Companion.Tool.PAINT_BRUSH)
        }

        binding.fillTool.setOnClickListener {
            binding.penTool.alpha = 0.5F
            binding.fillTool.alpha = 1F
            viewModel.setTool(PixelCanvas.Companion.Tool.FILL)
        }

        viewModel.tool.observe(viewLifecycleOwner, Observer {
            binding.pixelCanvas.tool = it
        })

        viewModel.pickColorEvent.observe(
            viewLifecycleOwner,
            Observer { pickColorEvent: Event<Int> ->
                if (!pickColorEvent.hasBeenHandled) {
                    ColorPickerDialog.newInstance(pickColorEvent.getContentIfNotHandled() ?: 0)
                        .setOnPositiveButtonClickListener { color ->
                            viewModel.setColor(color)
                        }.show(parentFragmentManager, "Some tag")
                }
            })

        viewModel.color.observe(viewLifecycleOwner, Observer { color ->
            binding.pixelCanvas.setColor(color)
        })

        (activity as MainActivity).setSupportActionBar(binding.toolbar)

        return binding.root
    }

}
