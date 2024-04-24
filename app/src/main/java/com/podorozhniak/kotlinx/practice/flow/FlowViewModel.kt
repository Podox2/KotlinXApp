package com.podorozhniak.kotlinx.practice.flow

import android.util.Log
import androidx.lifecycle.*
import com.podorozhniak.kotlinx.practice.base.Event
import kotlinx.coroutines.channels.Channel
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

    private var counter = 0

    //live data ~== state flow
    //LiveData is an lifecycle aware observable data holder (means it knows the lifecycle of the activity or an fragment) use it when you play with UI elements (views).
    private val _textLiveData = MutableLiveData("Hello, world")
    val textLiveData: LiveData<String> = _textLiveData

    //верхні поля можна замінити таким. в сетері (якщо він потрібний) треба змінювати тип до Mutable (53-й рядок)
    val textLiveDataLaconic = liveData {
        emit("Hello, world")
    }

    // Kotlin functions
    val mappedTextLiveData = _textLiveData.map { text ->
        "$text mapped Kotlin"
    }

    val switchMappedTextLiveData = _textLiveData.switchMap { text ->
        liveData {
            emit("$text switch mapped Kotlin")
        }
    }

    init {
        viewModelScope.launch {
            delay(2_000)
            Log.d("COR_TEST", "coroutine")
        }
        Log.d("COR_TEST", "thread")
    }

    // old Java methods
    /*val mappedTextLiveDataByJava = Transformations.map(_textLiveData) { text ->
        "$text mapped Java"
    }

    val switchMappedTextLiveDataByJava = Transformations.switchMap(_textLiveData) { text ->
        liveData {
            emit("$text switch mapped Java")
        }
    }*/

    fun updateLiveData() {
        _textLiveData.value = "Live Data!"
        (textLiveDataLaconic as MutableLiveData).value = "Live Data!"
    }

    //event live data ~== shared flow
    private val _textEventLiveData = MutableLiveData<Event<String>>()
    val textEventLiveData = _textEventLiveData

    fun updateEventLiveData() {
        _textEventLiveData.value = Event("Event!")
    }


    //StateFlow (hot stream) does similar things like LiveData but it is made using flow by kotlin guys and
    // only difference compare to LiveData is its not lifecycle aware but this is also been solved using repeatOnLifecycle api's,
    // So whatever LiveData can do StateFlow can do much better with power of flow's api.
    // ~== live data

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
        counter += 1
        _textStateFlow.value = "${counter} State Flow!"
        // via update
        /*_textStateFlow.update {
            "State Flow!"
        }*/
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
        counter = counter++
        viewModelScope.launch {
            _textSharedFlow.emit("${counter} Shared Flow!")
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

    private val _channel: Channel<String> = Channel()
    val channel = _channel.receiveAsFlow()

    fun updateChannel() {
        counter = counter++
        viewModelScope.launch {
            _channel.send("$counter Channel")
        }
    }
}