package m.v.nixoscompanionapp.ui.package_search

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.*
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.BufferedInputStream
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream
import java.net.MalformedURLException
import java.net.URL
import javax.net.ssl.HttpsURLConnection

class PackageSearchViewModel(application: Application) : AndroidViewModel(application) {

    private val _text = MutableLiveData<String>().apply {
        value = "This is the package search Fragment"
    }
    val text: LiveData<String> = _text

    fun makeApiRequest(jsonBody: String) {
        viewModelScope.launch {
            VolleySingleton
                .getInstance(getApplication<Application>().baseContext)
                .addToRequestQueue(
                    object : JsonObjectRequest(
                        Request.Method.GET,
                        "https://nixos-search-5886075189.us-east-1.bonsaisearch.net/latest-19-20.09/_search",
                        JSONObject(jsonBody),
                        { response: JSONObject ->
                            _text.value = response.toString(2)
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

class VolleySingleton constructor(context: Context) {
    companion object {
        @Volatile
        private var INSTANCE: VolleySingleton? = null
        fun getInstance(context: Context) =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: VolleySingleton(context).also {
                    INSTANCE = it
                }
            }
    }
    val requestQueue: RequestQueue by lazy {
        // applicationContext is key, it keeps you from leaking the
        // Activity or BroadcastReceiver if someone passes one in.
        Volley.newRequestQueue(context.applicationContext)
    }
    fun <T> addToRequestQueue(req: Request<T>) {
        requestQueue.add(req)
    }
}
