package com.podorozhniak.kotlinx.practice.flow

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import com.podorozhniak.kotlinx.practice.base.Event
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.random.Random

/*
hot stream - емітить значення без колекторів (підписників).
працює як ТБ канал, його може ніхто і не дивитись, але він і надалі буде надсилати "івенти".
This is good when you want values computed in the background fast, preparing them
for multiple observers you already have waiting. But if you’re going to add observers
after the fact, you could lose the data. Additionally, if the producer of values is hot, it can keep producing values even
though there are no consumers. This effectively wastes resources, and you have to
close the stream manually if you stop using it.
Приклад гарячих потоків - Channels

cold - не емітить значення без колекторів.
Приклад - Flow.

гарячий - створюється 1 раз і при підписці нового не буде перестворюватись, а розішле значення підписникам
емітить значення навіть якщо немає підписників
має буфер значень, які будуть розіслані
 */
class FlowViewModel : ViewModel() {

    private var counter = 0

    // медіатор лайв дата
    // тригериться на кожне оновлення source-лайвдат
    // тобто, навіть якщо одна з лайв дат без значення - підписникам прийде оновлення
    val liveDataSource1 = MutableLiveData<String>()
    val liveDataSource2 = MutableLiveData<String>()
    val mediatorLiveData = MediatorLiveData<String>()

    init {
        mediatorLiveData.addSource(liveDataSource1) {
            mediatorLiveData.value = "$it is from livedata1, ${liveDataSource2.value} is from livedata2"
        }
        mediatorLiveData.addSource(liveDataSource2) {
            mediatorLiveData.value = "${liveDataSource1.value} is from livedata1, $it is from livedata2"
        }
    }

    // live data ~== state flow
    // LiveData is an lifecycle aware observable data holder (means it knows the lifecycle of the activity or an fragment)
    // use it when you play with UI elements (views).
    private val _textLiveData = MutableLiveData("Hello, world")
    val textLiveData: LiveData<String> = _textLiveData

    // верхні поля можна замінити таким. в сетері (якщо він потрібний) треба змінювати тип до Mutable (80-й рядок)
    private val textLiveDataLaconic = liveData {
        emit("Hello, world")
    }

    fun updateLiveData() {
        _textLiveData.value = "Live Data!"
        (textLiveDataLaconic as MutableLiveData).value = "Live Data!"
    }

    // Kotlin functions
    val mappedTextLiveData: LiveData<String> = _textLiveData.map { text ->
        "$text mapped Kotlin"
    }

    val switchMappedTextLiveData: LiveData<String> = _textLiveData.switchMap { text ->
        liveData {
            emit("$text switch mapped Kotlin")
        }
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

    init {
        viewModelScope.launch {
            delay(2_000)
            Log.d("COR_TEST", "coroutine")
        }
        Log.d("COR_TEST", "thread")
    }

    //event live data ~== shared flow
    private val _textEventLiveData = MutableLiveData<Event<String>>()
    val textEventLiveData = _textEventLiveData

    fun updateEventLiveData() {
        _textEventLiveData.value = Event("Event!")
    }

    // StateFlow (hot stream) does similar things like LiveData but it is made using flow by kotlin guys and
    // only difference compare to LiveData is its not lifecycle aware but this is also been solved using repeatOnLifecycle api's,
    // So whatever LiveData can do StateFlow can do much better with power of flow's api.
    // ~== live data

    // StateFlow окрема реалізація SharedFlow
    // state flow можна порівняти з лайв датою, але має більший функціонал (функціонал flow (map, filter і т.д.))
    // використовується для зберігання значення або стану (state) як і лайв дата
    // гарячий стрім
    // має поле value, з якого можна отримати поточне значення. Тому при створенні потрібно обов'язково вказати якесь значення
    // якщо нове значення == старому значенню (equals == true), то не буде емітитись новий івент
    // при повороті екрану заемітить значення і воно буде оброблене, тому для тостів і т.д. не підходить
    // It’s recommended to change the state of the StateFlow using functions like emit
    // and tryEmit rather than using the value accessor directly (e.g. stateFlow.value = "Author: Luka" )
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
        _textStateFlow.update {
            "$counter State Flow!"
        }
    }

    // SharedFlow (hot stream) - name itself says it is shared, this flow can be shared by multiple consumers,
    // I mean if multiple collect calls happening on the sharedflow there will be a single flow which will get shared across all the consumers unlike normal flow.
    // ~== event live data

    // SharedFlow - не має поля value і не потрібне початкове значення
    // не зберігає стейт
    // для one-time івентів (тости, снекбари)
    // One key aspect of a SharedFlow is that it never completes. Because it represents a
    // possibly infinite stream of new information shared across multiple subscribers
    // you have to make sure you close the Flow as soon as you don’t need it
    // it can replay the last 1 event it processed and emitted to the rest of the subscribers
    // private val _textSharedFlow = MutableSharedFlow<String>(replay = 1)
    private val _textSharedFlow = MutableSharedFlow<String>()
    val textSharedFlow: SharedFlow<String> = _textSharedFlow.asSharedFlow()

    fun updateSharedFlow() {
        counter = counter++
        viewModelScope.launch {
            _textSharedFlow.emit("$counter Shared Flow!")
        }
    }

    // With the channel, each event is delivered to a single subscriber.
    // An attempt to post an event without subscribers will suspend as soon as the channel buffer becomes full,
    // waiting for a subscriber to appear. Posted events are never dropped by default.
    // не зберігає стейт
    // для one-time івентів (тости, снекбари)
    private val _channel: Channel<String> = Channel()
    val channel = _channel.receiveAsFlow()

    fun updateChannel() {
        counter = counter++
        viewModelScope.launch {
            _channel.send("$counter Channel")
        }
    }

    fun setEffect(data: String) {
        viewModelScope.launch {
            _channel.send(data)
        }
    }

    // Flow (cold stream) - In general think of it like a stream of data flowing in a pipe
    // with both ends having a producer and consumer running on a coroutine.
    // flow не зберігає стейт. при повороті екрану не буде заемічене останнє значення
    // відробить роботу і зупиниться
    // курутина, яка емітить значення
    fun triggerFlow(): Flow<String> {
        return flow {
            repeat(5) {
                emit("emit ${it + 1}")
                delay(1_000)
            }
        }
    }

    fun endlessFLow(): Flow<Int> {
        return flow {
            while (true) {
                emit(Random.nextInt())
                println("FLOW: Emitting item")
                delay(500)
            }
        }.flowOn(Dispatchers.Default)
    }}