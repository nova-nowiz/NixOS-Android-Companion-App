package m.v.nixoscompanionapp.ui.option_search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import m.v.nixoscompanionapp.R

class OptionSearchFragment : Fragment() {

    private lateinit var optionSearchViewModel: OptionSearchViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        optionSearchViewModel =
                ViewModelProviders.of(this).get(OptionSearchViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_option_search, container, false)
        val textView: TextView = root.findViewById(R.id.text_option_search)
        optionSearchViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return root
    }
}
