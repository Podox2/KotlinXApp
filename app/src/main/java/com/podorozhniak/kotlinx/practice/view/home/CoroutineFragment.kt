package com.podorozhniak.kotlinx.practice.view.home

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.podorozhniak.kotlinx.R
import com.podorozhniak.kotlinx.databinding.FragmentCoroutinesBinding
import com.podorozhniak.kotlinx.practice.base.BaseFragment
import com.podorozhniak.kotlinx.practice.di.appContext
import com.podorozhniak.kotlinx.practice.extensions.onClick
import com.podorozhniak.kotlinx.practice.view.MainActivity
import com.podorozhniak.kotlinx.practice.view.fragment_result_api.SecondActivity
import kotlinx.coroutines.*
import kotlin.system.measureTimeMillis

class CoroutineFragment : BaseFragment<FragmentCoroutinesBinding>() {
    override val layoutId: Int
        get() = R.layout.fragment_coroutines

    override fun onCreateViewBinding(view: View) = FragmentCoroutinesBinding.bind(view)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            btnCoroutine.onClick {
                customScope()
                startInGlobalScope()

                GlobalScope.launch {
                    runSuspends()
                    switchContext()
                }

                runBlocking()
                jobEtc()
                asyncAwait()
                scopes()
            }
            btnNetworkRequests.onClick {
                findNavController().navigate(CoroutineFragmentDirections.actionCoroutineFragmentToNetworkRequestFragment())
            }
        }
    }

    private val uiJob = Job()
    //Scope = context в якому працює курутина
    private val uiScope = CoroutineScope(context = Dispatchers.Main + uiJob)
    private fun customScope() {
        uiScope.launch {
            log("uiScope")
        }
    }

    //треба самому відміняти job, якщо кастомний скоуп
    override fun onDestroyView() {
        super.onDestroyView()
        uiJob.cancel()
    }

    //https://www.youtube.com/watch?v=kvfpuzSwVZ8&list=PLQkwcJG4YTCQcFEPuYGuv54nYai_lwil_&index=2
    //якщо закрити аплікуху, то знищиться і мейн тред, і курутина в ній -> не виведетсья лог
    private fun startInGlobalScope() {
        GlobalScope.launch {
            delay(5_000L)
            log("Coroutine says from thread  ${Thread.currentThread().name}")
        }
        log("Coroutine says from thread  ${Thread.currentThread().name}")
    }

    //https://www.youtube.com/watch?v=yc_WfBp-PdE&list=PLQkwcJG4YTCQcFEPuYGuv54nYai_lwil_&index=3
    //suspend ф-ції запускаються в курутинах, або інших suspend ф-ціях
    //два ділея будуть по черзі виконані. вивід відбудеться через 4 секунди
    private suspend fun runSuspends() {
        log("start coroutines")
        val response = suspendFunc()
        val response2 = suspendFunc2()
        log(response)
        log(response2)
    }

    //https://www.youtube.com/watch?v=71NrkkRNXG4&list=PLQkwcJG4YTCQcFEPuYGuv54nYai_lwil_&index=4
    private suspend fun switchContext() {
        log("Coroutine says from thread  ${Thread.currentThread().name}")
        GlobalScope.launch(Dispatchers.IO) {
            //binding.btnCoroutine.text = "some text" //causes crash cause not main thread
            log("Coroutine says from thread  ${Thread.currentThread().name}")
            withContext(Dispatchers.Main) {
                log("Coroutine says from thread  ${Thread.currentThread().name}")
            }
        }
    }

    //https://www.youtube.com/watch?v=k9yisEEPC8g&list=PLQkwcJG4YTCQcFEPuYGuv54nYai_lwil_&index=5
    private fun runBlocking() {
        //не заблокує потік
        GlobalScope.launch(Dispatchers.Main) {
            delay(5_000L)
        }

        //заблокує потік
        //можна юзати замість lifecycleScope.launch { delay(1_000L) }
        runBlocking {
            //можна запускати інші корутини
            launch {
                delay(1_000L)
                log("another coroutine")
            }

            log("runBlocking started")
            delay(5_000)
            log("runBlocking finished")
        }

        //варіант без корутин
        /*log("runBlocking started")
        Thread.sleep(5_000L)
        log("runBlocking finished")*/
    }

    //https://www.youtube.com/watch?v=55W60o9uzVc&list=PLQkwcJG4YTCQcFEPuYGuv54nYai_lwil_&index=6
    private fun jobEtc() {
        val job = GlobalScope.launch(Dispatchers.Default) {
            for (i in 0..10) {
                //без перевірки незважаючи на cancel, курутина буде виконувати роботу
                if (isActive) {
                    log(longWork())
                }
            }
        }
        runBlocking {
            delay(2_000L)
            job.cancel()
            log("Main Thread is continuing...")
        }

        //job'а зупиниться після 3 секунд
        /*val jobTimeOut = GlobalScope.launch(Dispatchers.Default) {
            withTimeout(3_000L) {
                repeat(5) {
                    log("Coroutine is still working $it")
                    delay(1500L)
                }
            }
        }*/
    }

    //https://www.youtube.com/watch?v=t-3TOke8tq8&list=PLQkwcJG4YTCQcFEPuYGuv54nYai_lwil_&index=7
    private fun asyncAwait() {
        //два запити йдуть один за одним. два ділея "додаються"
        GlobalScope.launch(Dispatchers.IO) {
            val time = measureTimeMillis {
                val ans1 = suspendFunc()
                val ans2 = suspendFunc2()
            }
            log("requests took $time ms")
        }


        //запити виконаютсья паралельно. launch варіант (поганий)
        GlobalScope.launch(Dispatchers.IO) {
            val time = measureTimeMillis {
                var answer1: String? = null
                var answer2: String? = null
                val job1 = launch { answer1 = suspendFunc() }
                val job2 = launch { answer2 = suspendFunc2() }
                //без join робота програми продовжиться і в логах виведуться null'и
                job1.join()
                job2.join()
                log("answer1 - $answer1")
                log("answer2 - $answer2")
            }
            log("requests took $time ms")
        }

        //запити виконаютсья паралельно. async варіант (нормальний)
        GlobalScope.launch(Dispatchers.IO) {
            val time = measureTimeMillis {
                val answer1 = async { suspendFunc() }
                val answer2 = async { suspendFunc2() }.await()
                log("answer1 - ${answer1.await()}")
                log("answer2 - $answer2")
            }
            log("requests took $time ms")
        }
    }

    //https://www.youtube.com/watch?v=uiPYcSSjNTI&list=PLQkwcJG4YTCQcFEPuYGuv54nYai_lwil_&index=8
    //курутина запущена в глобал скоупі, навіть після знищення актівіті, буде жива
    private fun scopes() {
        GlobalScope.launch {
            while (true) {
                log("GlobalScope coroutine still alive!")
                delay(1_000L)
            }
        }
        lifecycleScope.launch {
            while (true) {
                log("lifecycleScope coroutine still alive!")
                delay(1_000L)
            }
        }
        GlobalScope.launch {
            delay(3_000L)
            startActivity(Intent(appContext, SecondActivity::class.java))
            delay(2_000L)
            (requireActivity() as MainActivity).finish()
        }
    }

    private suspend fun suspendFunc(): String {
        delay(2_000L)
        return "response"
    }

    private suspend fun suspendFunc2(): String {
        delay(2_000L)
        return "response2"
    }

    private fun longWork(): String {
        Thread.sleep(500L)
        return "longWork done"
    }
}

