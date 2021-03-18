package m.v.nixoscompanionapp.ui.package_search

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import m.v.nixoscompanionapp.R

class PackageSearchFragment : Fragment() {

    private lateinit var packageSearchViewModel: PackageSearchViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        packageSearchViewModel =
                ViewModelProviders.of(this).get(PackageSearchViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_package_search, container, false)
        val textView: TextView = root.findViewById(R.id.text_package_search)
        packageSearchViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        val jsonBody: String =
            String.format(
                resources.getString(R.string.nixos_elastic_search_json_body),
                "package",
                "gnu"
            )
        Log.i("NixOS Package Search", jsonBody)
        packageSearchViewModel.makeApiRequest(jsonBody)
        return root
    }
}
