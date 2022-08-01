package com.podorozhniak.kotlinx.practice.view.network_request

import android.util.Log
import androidx.lifecycle.*
import com.podorozhniak.kotlinx.practice.base.BaseViewModel
import com.podorozhniak.kotlinx.practice.base.ViewModelExecutor
import com.podorozhniak.kotlinx.practice.data.remote.model.Message
import com.podorozhniak.kotlinx.practice.data.remote.repository.MessagesRepo
import com.podorozhniak.kotlinx.practice.extensions.coroutines.launchWithHandlingIO
import com.podorozhniak.kotlinx.practice.extensions.exceptionHandler
import com.podorozhniak.kotlinx.practice.util.retrofit_call_adapter.asFailure
import com.podorozhniak.kotlinx.practice.util.retrofit_call_adapter.asSuccess
import com.podorozhniak.kotlinx.practice.util.retrofit_call_adapter.isSuccess
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@ExperimentalCoroutinesApi
class NetworkRequestViewModel(
    private val messagesRepo: MessagesRepo,
    viewModelExecutor: ViewModelExecutor
) : BaseViewModel(viewModelExecutor) {
    private val refreshIntervalMs: Long = 2000
    var periodicRequestsCounter = 0
    var requestsCounter = 0
    private val compositeDisposable = CompositeDisposable()

    //реалізація через live data
    private val _infoFromRequest = MutableLiveData<String>()
    val infoFromRequest: LiveData<String>
        get() = _infoFromRequest

    val stringToStringTrans: LiveData<String> =
        Transformations.map(_infoFromRequest) {
            "transformed $it"
        }

    //реалізація через flow
    private val _infoFromRequestFlow = MutableStateFlow("")
    val infoFromRequestFlow: StateFlow<String> = _infoFromRequestFlow

    //проста реалізація flow через flow builder
    val latestNews: Flow<String> = flow {
        while(true) {
            val latestNews = fetchSomeInfo()
            emit(latestNews) // Emits the result of the request to the flow
            delay(refreshIntervalMs) // Suspends the coroutine for some time
        }
    }

    //можна зробити аналогічну реалізацію через лайв дату
    //різницю не побачив. flow надає механізм операторів для зміни об'єктів, які приходять
    val latestNewsLiveData: LiveData<String> = liveData (timeoutInMs = 5000L) {
        while(true) {
            val latestNews = fetchSomeInfo()
            emit(latestNews) // Emits the result of the request to the flow
            delay(refreshIntervalMs) // Suspends the coroutine for some time
        }
    }

    private fun fetchSomeInfo(): String {
        periodicRequestsCounter++
        val range = (1..30)
        return "${range.random()}, counter = $periodicRequestsCounter"
    }

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

    fun getInfoFromNetworkViewModelExecutor() {
        viewModelScope.launch {
            runSafe(
                operation = {
                    messagesRepo.messages().size.toString()
                },
                onSuccess = {
                    _infoFromRequest.postValue("get $it by ViewModelExecutor")
                },
                onError = {
                    _infoFromRequest.postValue(it.localizedMessage)
                }
            )
        }
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
                Log.d(
                    "Cache",
                    "onResponse response -> ${response.raw().networkResponse}, cache -> ${response.raw().cacheResponse}"
                )
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