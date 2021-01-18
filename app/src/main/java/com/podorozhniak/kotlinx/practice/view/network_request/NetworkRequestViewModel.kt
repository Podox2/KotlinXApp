package com.podorozhniak.kotlinx.practice.view.network_request

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.podorozhniak.kotlinx.practice.base.BaseViewModel
import com.podorozhniak.kotlinx.practice.data.remote.model.Message
import com.podorozhniak.kotlinx.practice.data.remote.repository.MessagesRepo
import com.podorozhniak.kotlinx.practice.extensions.coroutines.launchWithHandlingIO
import com.podorozhniak.kotlinx.practice.extensions.exceptionHandler
import com.podorozhniak.kotlinx.practice.util.retrofit_call_adapter.asFailure
import com.podorozhniak.kotlinx.practice.util.retrofit_call_adapter.asSuccess
import com.podorozhniak.kotlinx.practice.util.retrofit_call_adapter.isSuccess
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NetworkRequestViewModel(private val messagesRepo: MessagesRepo) : BaseViewModel() {

    private val _infoFromRequest = MutableLiveData<String>()
    var infoFromRequest: LiveData<String> = _infoFromRequest

    private val compositeDisposable = CompositeDisposable()
    var requestsCounter = 0

    fun getInfoFromNetworkSimpleWay() {
        viewModelScope.launch {
            try {
                _infoFromRequest.value =
                    "get ${messagesRepo.messages().size} by SimpleWay"
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
                _infoFromRequest.postValue("get $it by CoroutineExtension")
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
                    _infoFromRequest.value =
                        "get ${requestResult.asSuccess().value.size} by CallAdapter"
                }
            } else {
                withContext(Dispatchers.Main) {
                    _infoFromRequest.value = requestResult.asFailure().error?.localizedMessage
                }
            }
        }
    }

    fun getInfoFromNetworkRx() {
        compositeDisposable.add(
            messagesRepo.messagesSingle()
                .subscribe({
                    _infoFromRequest.postValue(
                        "get ${it.size} by Rx"
                    )
                }, {
                    _infoFromRequest.postValue(
                        it.localizedMessage
                    )
                })
        )
    }

    fun getInfoFromNetworkCall() {
        val call = messagesRepo.messagesCall()
        call.enqueue(object : Callback<List<Message>> {
            override fun onResponse(call: Call<List<Message>>, response: Response<List<Message>>) {
                _infoFromRequest.value =
                    "get ${response.body()?.size.toString()} by Call"
            }

            override fun onFailure(call: Call<List<Message>>, t: Throwable) {
                _infoFromRequest.value = t.localizedMessage
            }
        })
    }

    override fun onCleared() {
        compositeDisposable.clear()
        super.onCleared()
    }
}