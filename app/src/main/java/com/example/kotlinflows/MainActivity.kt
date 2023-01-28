package com.example.kotlinflows

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.kotlinflows.databinding.ActivityMainBinding
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val TAG = "MyTaG"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.startFlowBTN.setOnClickListener {
            val job = CoroutineScope(Dispatchers.Main).launch {
                val data: Flow<Int> = producer()
                /*
                Flows by default are Cold, until consumer not present
                 */

                data.collect {
                    // ie: Consumer; Here we collect data,
                    Log.d(TAG, "$it , ->  ")

                    binding.flowDataTV.append( "$it \n  ")
                }
            }
             /*
             if you wanna stop the flow, simply remove the consumer flow automatically closed
              */
           GlobalScope.launch {
               // We cancel our Job(coroutine) for stop flow
               delay(5000)
               job.cancel()
           }
        }

    }


    private fun producer() = flow<Int> {
        val list = listOf(1, 2, 3, 4, 5, 6, 7, 8, 9)
        // Assume that, this is a data which flows consistently

        list.forEach {
            delay(1000)
            emit(it)

            // Here, Producer produce the for collector
        }


    }
}