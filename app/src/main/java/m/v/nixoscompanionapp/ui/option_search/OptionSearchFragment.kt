package m.v.nixoscompanionapp.ui.option_search

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import m.v.nixoscompanionapp.R
import org.json.JSONObject

class OptionSearchFragment : Fragment() {

    private lateinit var optionSearchViewModel: OptionSearchViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        optionSearchViewModel = ViewModelProviders
            .of(this)
            .get(OptionSearchViewModel::class.java)

        val root = inflater.inflate(R.layout.fragment_option_search, container, false)

        val searchField: EditText = root.findViewById(R.id.optionSearchField)
        val searchButton: Button = root.findViewById(R.id.optionSearchButton)
        val recyclerView: RecyclerView = root.findViewById(R.id.optionRecyclerView)

        val adapter = OptionSearchHitsAdapter()

        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter

        optionSearchViewModel.hits.observe(viewLifecycleOwner, {
            adapter.optionSearchHits = it
        })

        val searchAction = {
            val query = searchField.text.toString()

            if (query.length > 1) {
                val formattedString = String.format(
                    resources.getString(R.string.option_search_json_body),
                    query
                )
                Log.d("Pkg Search: String", formattedString)
                val jsonBody = JSONObject(formattedString)
                Log.d("Pkg Search: Json", jsonBody.toString(2))
                optionSearchViewModel.makeApiRequest(jsonBody)
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
