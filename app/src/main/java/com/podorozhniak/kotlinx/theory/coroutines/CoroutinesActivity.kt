package com.podorozhniak.kotlinx.theory.coroutines

import android.os.Bundle
import android.os.SystemClock
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.podorozhniak.kotlinx.R
import com.podorozhniak.kotlinx.practice.data.remote.model.Message
import com.podorozhniak.kotlinx.practice.data.remote.service.MessagesService
import io.reactivex.Single
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.*
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


class CoroutinesActivity : AppCompatActivity() {

    private lateinit var api: MessagesService
    private lateinit var single: Single<Int>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_coroutines)

        val retrofit = Retrofit.Builder()
            .baseUrl("https://rawgit.com/startandroid/data/master/messages/")
            .addConverterFactory(GsonConverterFactory.create())
            //.addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build()

        api = retrofit.create(MessagesService::class.java)

        val compositeDisposable = CompositeDisposable()

        findViewById<Button>(R.id.btn_net).setOnClickListener {
            compositeDisposable.add(
                api.messagesSingle()
                    .subscribeOn(Schedulers.io())
                    .delay(3, TimeUnit.SECONDS)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        Toast.makeText(applicationContext, "Size = ${it.size}", Toast.LENGTH_SHORT).show()
                    },
                        { Toast.makeText(applicationContext, it.localizedMessage, Toast.LENGTH_SHORT).show() })
            )
        }
        compositeDisposable.add(
            api.messagesSingle()
                .subscribeOn(Schedulers.io())
                .delay(3, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object: Consumer<List<Message>> {
                    override fun accept(t: List<Message>?) {
                        TODO("Not yet implemented")
                    }
                }, object: Consumer<Throwable>{
                    override fun accept(t: Throwable?) {
                        TODO("Not yet implemented")
                    }
                }))

        val l = api.messagesSingle()
        val s = l.subscribe()
        single = Single.fromCallable{work2()}
        val l2 = RxJ.createSingle().subscribe(object: SingleObserver<String>{
            override fun onSubscribe(d: Disposable) {
                TODO("Not yet implemented")
            }

            override fun onSuccess(t: String) {
                TODO("Not yet implemented")
            }

            override fun onError(e: Throwable) {
                TODO("Not yet implemented")
            }
        })

        val l3 = RxJ.createSingle().subscribe(object: DisposableSingleObserver<String>(){
            override fun onSuccess(t: String) {
            }

            override fun onError(e: Throwable) {
            }
        })

        val l4 = RxJ.createSingle().subscribe(object: Consumer<String> {
            override fun accept(t: String?) {
                TODO("Not yet implemented")
            }
        }, object: Consumer<Throwable>{
            override fun accept(t: Throwable?) {
                TODO("Not yet implemented")
            }
        })


        val l6 = RxJ.createSingle().subscribe({

        }, {

        })
        compositeDisposable.add(l6)

        findViewById<Button>(R.id.btn_loc).setOnClickListener {
            compositeDisposable.add(
                single
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        Toast.makeText(applicationContext, "Size = ${it}", Toast.LENGTH_SHORT).show()
                    },
                        { Toast.makeText(applicationContext, it.localizedMessage, Toast.LENGTH_SHORT).show() })
            )
        }

        findViewById<Button>(R.id.btn_loc_freeze).setOnClickListener {
            compositeDisposable.add(
                work()
                    .subscribeOn(Schedulers.io())
                    .delay(2, TimeUnit.SECONDS)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        Toast.makeText(applicationContext, "Size = ${it}", Toast.LENGTH_SHORT).show()
                    },
                        { Toast.makeText(applicationContext, it.localizedMessage, Toast.LENGTH_SHORT).show() })
            )
        }

/*        api.messagesCall().enqueue(object: Callback<List<Message>>{
            override fun onFailure(call: Call<List<Message>>, t: Throwable) {
                Toast.makeText(applicationContext, t.localizedMessage, Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<List<Message>>, response: Response<List<Message>>) {
                Toast.makeText(applicationContext, "Size = ${response.body()?.size}", Toast.LENGTH_SHORT).show()
            }
        })*/


        CoroutineScope(Dispatchers.IO).launch {
            val request = api.messagesDeferred()
            withContext(Dispatchers.Main) {
                try {
                    val response = request.await()
                    if (response.isNotEmpty()) {
                        Toast.makeText(applicationContext, "Coroutine!!! Size = ${response.size}", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(applicationContext, "${response}", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: HttpException) {
                    Toast.makeText(applicationContext, e.localizedMessage, Toast.LENGTH_SHORT).show()
                } catch (e: Throwable) {
                    Toast.makeText(applicationContext, e.localizedMessage, Toast.LENGTH_SHORT).show()
                }
            }
        }

        GlobalScope.launch (Dispatchers.Main){
            //toast("Hello it has started")
            delay(2000)
            //textView.text = "I have changed after 10 seconds"
        }
    }

    fun work(): Single<Int>{
        return Single.just(2)
    }

    fun work2(): Int {
        SystemClock.sleep(3000)
        return 2
    }

    suspend fun workCor(){
        val list: Deferred<List<Message>> = api.messagesDeferred()
        list.toString()
    }
}
