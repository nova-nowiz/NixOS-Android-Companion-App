package m.v.nixoscompanionapp.ui.package_search

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_package_search.*
import m.v.nixoscompanionapp.R
import org.json.JSONObject

class PackageSearchFragment : Fragment() {

    private lateinit var packageSearchViewModel: PackageSearchViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        packageSearchViewModel = ViewModelProviders
            .of(this)
            .get(PackageSearchViewModel::class.java)

        val root = inflater.inflate(R.layout.fragment_package_search, container, false)

        val searchField: EditText = root.findViewById(R.id.packageSearchField)
        val searchButton: Button = root.findViewById(R.id.packageSearchButton)
        val recyclerView: RecyclerView = root.findViewById(R.id.packageRecyclerView)

        val adapter = PackageSearchHitsAdapter()

        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter

        packageSearchViewModel.hits.observe(viewLifecycleOwner, {
            adapter.packageSearchHits = it
        })

        val searchAction = {
            val query = searchField.text.toString()

            if (query.length > 1) {
                val formattedString = String.format(
                    resources.getString(R.string.package_search_json_body),
                    query
                )
                Log.d("Pkg Search: String", formattedString)
                val jsonBody = JSONObject(formattedString)
                Log.d("Pkg Search: Json", jsonBody.toString(2))
                packageSearchViewModel.makeApiRequest(jsonBody)
            }
        }

        searchButton.setOnClickListener {
            searchAction()
        }

        searchField.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                searchAction()
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }

        return root
    }
}
