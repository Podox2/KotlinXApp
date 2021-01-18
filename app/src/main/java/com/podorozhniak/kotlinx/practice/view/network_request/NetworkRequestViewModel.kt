package com.podorozhniak.kotlinx.practice.view.network_request

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.podorozhniak.kotlinx.practice.base.BaseViewModel
import com.podorozhniak.kotlinx.practice.data.remote.repository.MessagesRepo
import com.podorozhniak.kotlinx.practice.extensions.coroutines.launchWithHandlingIO
import com.podorozhniak.kotlinx.practice.extensions.exceptionHandler
import com.podorozhniak.kotlinx.practice.util.retrofit_call_adapter.asFailure
import com.podorozhniak.kotlinx.practice.util.retrofit_call_adapter.asSuccess
import com.podorozhniak.kotlinx.practice.util.retrofit_call_adapter.isSuccess
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class NetworkRequestViewModel(private val messagesRepo: MessagesRepo) : BaseViewModel() {

    private val _infoFromRequest = MutableLiveData<String>()
    var infoFromRequest: LiveData<String> = _infoFromRequest

    var requestsCounter = 0

    fun getInfoFromNetworkSimpleWay() {
        viewModelScope.launch {
            try {
                _infoFromRequest.value = messagesRepo.messages().size.toString()
            } catch (ex: Exception) {
                ex.exceptionHandler()
                _infoFromRequest.value = ex.localizedMessage
            }
        }
    }

    fun getInfoFromNetworkCoroutineExtension() {
        viewModelScope.launchWithHandlingIO(
            doBlock = {
                messagesRepo.messages().size.toString()
            },
            onSuccess = {
                _infoFromRequest.postValue(it)
            },
            onError = {
                _infoFromRequest.postValue(it.localizedMessage)
            }
        )
    }

    fun getInfoFromNetworkCallAdapter() {
        viewModelScope.launch {
            val requestResult = messagesRepo.messagesResult()
            if (requestResult.isSuccess()) {
                withContext(Dispatchers.Main) {
                    _infoFromRequest.value = requestResult.asSuccess().value.size.toString()
                }
            } else {
                withContext(Dispatchers.Main) {
                    _infoFromRequest.value = requestResult.asFailure().error?.localizedMessage
                }
            }
        }
    }

}