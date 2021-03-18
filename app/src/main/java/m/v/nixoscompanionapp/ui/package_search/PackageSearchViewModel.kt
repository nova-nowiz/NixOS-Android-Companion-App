package m.v.nixoscompanionapp.ui.package_search

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

class PackageSearchViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is the package search Fragment"
    }
    val text: LiveData<String> = _text

    fun makeApiRequest(jsonBody: String) {
        viewModelScope.launch {
            val bearerAuth = "Bearer ejNaRko2eTJtUjpkczhDRXZBTFBmOXB1aTdYRw=="
            val response = makeNixOSElasticSearchRequest(ElasticSearchType.Package, jsonBody, bearerAuth)
            _text.value = response?.toString(2)
        }
    }
}

enum class ElasticSearchType {
    Package,
    Option
}

suspend fun makeNixOSElasticSearchRequest(searchType: ElasticSearchType, jsonBody: String, auth: String): JSONObject? =
    withContext(Dispatchers.IO) {
        try {
            val url = URL("https://nixos-search-5886075189.us-east-1.bonsaisearch.net/latest-19-20.09/_search")
            (url.openConnection() as? HttpsURLConnection)?.run {
                requestMethod = "GET"
                setRequestProperty("Content-Type", "application/json")
                setRequestProperty("Authorization", auth)
                //doOutput = true
                //outputStream.write(jsonBody.toByteArray())
                val stream = BufferedInputStream(inputStream)
                val result: String = readStream(stream)
                Log.i("NixOS Package Search", result)
                return@withContext JSONObject(result)
            }
        } catch (e: MalformedURLException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return@withContext null
    }

private fun readStream(inputStream: InputStream): String {
    return try {
        val bo = ByteArrayOutputStream()
        var i = inputStream.read()
        while (i != -1) {
            bo.write(i)
            i = inputStream.read()
        }
        bo.toString()
    } catch (e: IOException) {
        ""
    }
}
