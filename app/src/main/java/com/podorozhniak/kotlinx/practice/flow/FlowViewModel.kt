package com.podorozhniak.kotlinx.practice.flow

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.podorozhniak.kotlinx.practice.base.Event
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/*
hot stream - емітить значення без колекторів (підписників)
cold - не емітить

гарячий - створюється 1 раз і при підписці нового не буде перестворюватись, а розішле значення підписникам
емітить значення навіть якщо немає підписників
має буфер значень, які будуть розіслані
 */
class FlowViewModel : ViewModel() {

    //live data ~== state flow
    //LiveData is an lifecycle aware observable data holder (means it knows the lifecycle of the activity or an fragment) use it when you play with UI elements(views).
    private val _textLiveData = MutableLiveData("Hello, world")
    val textLiveData: LiveData<String> = _textLiveData

    fun updateLiveData() {
        _textLiveData.value = "Live Data!"
    }

    //event live data ~== shared flow
    private val _textEventLiveData = MutableLiveData<Event<String>>()
    val textEventLivaData = _textEventLiveData

    fun updateEventLiveData() {
        _textEventLiveData.value = Event("Event!")
    }

    //~== live data
    //StateFlow (hot stream)  does similar things like LiveData but it is made using flow by kotlin guys and
    // only difference compare to LiveData is its not lifecycle aware but this is also been solved using repeatOnLifecycle api's,
    // So whatever LiveData can do StateFlow can do much better with power of flow's api.

    //state flow можна порівняти з лайв датою, але має більший функціонал (функціонал flow (map, filter і т.д.))
    //використовується для зберігання значення або стану (state) як і лайв дата
    //гарячий стрім
    //має поле value, з якого можна отримати поточне значення. Тому при створенні потрібно обов'язково вказати якесь значення
    //якщо нове значення == старому значенню (equals == true), то не буде емітитись новий івент
    //при повороті екрану заемітить значення і воно буде оброблене, тому для тостів і т.д. не підходить
    private val _textStateFlow = MutableStateFlow("Hello, world")
    val textStateFlow: StateFlow<String> =
        _textStateFlow.asStateFlow() //asStateFlow щоб повернути read only тип. без цього можна змінити тип на MutableStateFlow
    /*
    вертати можна по-різному
    val messageVisible: StateFlow<Boolean>
        get() {
            return _messageVisible.asStateFlow()
        }
    get() = _messageVisible.asStateFlow()
    */

    fun updateStateFlow() {
        _textStateFlow.value = "State Flow!"
    }

    //~== event live data
    //SharedFlow (hot stream) - name itself says it is shared, this flow can be shared by multiple consumers,
    // I mean if multiple collect calls happening on the sharedflow there will be a single flow which will get shared across all the consumers unlike normal flow.

    //SharedFlow - окрема реалізація StateFlow, не має поля value і не потрібне початкове значення
    //не зберігає стейт
    //для one-time івентів (тости, снекбари)
    private val _textSharedFlow = MutableSharedFlow<String>()
    val textSharedFlow: SharedFlow<String> = _textSharedFlow.asSharedFlow()

    fun updateSharedFlow() {
        viewModelScope.launch {
            _textSharedFlow.emit("Shared Flow!")
        }
    }

    //Flow (cold stream) - In general think of it like a stream of data flowing in a pipe with both ends having a producer and consumer running on a coroutine.
    //flow не зберігає стейт. при повороті екрану не буде заемічене останнє значення
    //відробить роботу і зупиниться
    //курутина, яка емітить значення
    fun triggerFlow(): Flow<String> {
        return flow {
            repeat(5) {
                emit("emit ${it + 1}")
                delay(1_000)
            }
        }
    }
}