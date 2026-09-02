package com.podorozhniak.kotlinx.theory.rx

import io.reactivex.Single
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class RxK {

    private val disposables = CompositeDisposable()

    val singleObserver = object : SingleObserver<String> {
        override fun onSubscribe(d: Disposable) {
            disposables.add(d)
        }

        override fun onSuccess(value: String) {

        }

        override fun onError(e: Throwable) {
        }
    }

    val disposableSingleObserver = object : DisposableSingleObserver<String>() {

        override fun onSuccess(value: String) {

        }

        override fun onError(e: Throwable) {
        }
    }

    // observer додає disposable в onSubscribe(d: Disposable)
    private fun getDataFromSingle() {
        Single
            .just("Data from Single")
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {  }
            .doAfterTerminate {  }
            .doFinally {  }
            .subscribe(singleObserver)
    }

    // disposable.add() при створенні
    private fun getDataFromSingle2() {
        disposables.add(
            Single
                .just("Data from Single 2")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(disposableSingleObserver)
        )
    }

    // реалізація підписника через Consumer
    private fun getDataFromSingle3() {
        disposables.add(
            Single
                .just("Data from Single 3")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    /*object : Consumer<String> {
                        override fun accept(s: String) {
                        }
                    }, object : Consumer<Throwable> {
                        override fun accept(throwable: Throwable) {
                        }
                    }*/
                    /*Consumer { value ->
                    },*/
                    MyConsumer(),
                    Consumer { }
                )
        )
    }

    // реалізація підписника лямбдами
    private fun getDataFromSingle4() {
        disposables.add(
            Single
                .just("Data from Single 2")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ value ->
                }, { })
        )
    }

    private fun getDataFromFlow() {
        CoroutineScope(Dispatchers.IO).launch {
            flow {
                emit("Data from Flow")
            }
                // перемикає поітк для коду вище
                .flowOn(Dispatchers.IO)
                .onEach { }
                .catch { e -> }
                .collect { value -> }
        }
    }

}

class MyConsumer : Consumer<String> {
    override fun accept(t: String) { }
}