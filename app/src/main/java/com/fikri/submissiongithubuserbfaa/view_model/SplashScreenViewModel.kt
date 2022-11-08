package com.fikri.submissiongithubuserbfaa.view_model

import androidx.lifecycle.*
import com.fikri.submissiongithubuserbfaa.other_class.SettingPreferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SplashScreenViewModel(private val pref: SettingPreferences) : ViewModel() {

    private val _isTimeOut = MutableLiveData<Boolean>()
    val isTimeOut: LiveData<Boolean> = _isTimeOut

    init {
        waitAndMove()
    }

    private fun waitAndMove() {
        viewModelScope.launch(Dispatchers.Default) {
            delay(2500)
            withContext(Dispatchers.Main) {
                _isTimeOut.value = true
            }
        }
    }

    fun getThemeSettings(): LiveData<Boolean> {
        return pref.getThemeSetting().asLiveData()
    }
}