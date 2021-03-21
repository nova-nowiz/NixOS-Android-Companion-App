package m.v.nixoscompanionapp.ui.package_search

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.*
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import kotlinx.coroutines.launch
import m.v.nixoscompanionapp.VolleySingleton
import org.json.JSONArray
import org.json.JSONObject

class PackageSearchViewModel(application: Application) : AndroidViewModel(application) {

    private val _hits = MutableLiveData<JSONArray>().apply {
        value = JSONArray()
    }
    val hits: LiveData<JSONArray> = _hits

    fun makeApiRequest(jsonBody: JSONObject) {
        viewModelScope.launch {
            VolleySingleton
                .getInstance(getApplication<Application>().baseContext)
                .addToRequestQueue(
                    object : JsonObjectRequest(
                        Method.POST,
                        "https://nixos-search-5886075189.us-east-1.bonsaisearch.net/latest-19-20.09/_search",
                        jsonBody,
                        { response: JSONObject ->
                            _hits.value = response.getJSONObject("hits").getJSONArray("hits")
                            Log.d("Pkg Search: Response", _hits.value?.toString(2) ?: "null")
                        },
                        null
                    ) {
                        override fun getHeaders(): MutableMap<String, String> {
                            val headers = HashMap<String, String>(super.getHeaders())
                            headers["Authorization"] = "Bearer ejNaRko2eTJtUjpkczhDRXZBTFBmOXB1aTdYRw=="
                            headers["Content-Type"] = "application/json"
                            return headers
                        }
                    }
                )
        }
    }
}
