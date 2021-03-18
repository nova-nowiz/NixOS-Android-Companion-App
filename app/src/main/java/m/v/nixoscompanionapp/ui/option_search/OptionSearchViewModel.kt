package m.v.nixoscompanionapp.ui.option_search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class OptionSearchViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is the option search Fragment"
    }
    val text: LiveData<String> = _text
}
