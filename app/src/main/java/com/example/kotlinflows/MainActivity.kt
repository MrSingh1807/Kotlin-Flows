package com.example.kotlinflows

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.kotlinflows.databinding.ActivityMainBinding
import com.example.kotlinflows.model.FormattedNote
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val TAG = "MyTaG"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.startFlowBTN.setOnClickListener {
            /********  Cold Flows   *******/

            CoroutineScope(Dispatchers.Main).launch {
                /******    Context Preservation ( Flow On )  *****/

                /* Flow Always preserve his context,
                     they assume that emit() & collect() are in same context
                Producer always produce your data on that context in which context you consume data

                 Case -> I want to produce data in IO thread & consume it in Main Thread ; Use FlowON
                 */

                producer()
                    .map {
                        Log.d(TAG, "Consumer " + Thread.currentThread().name) // IO thread
                        it * 2
                    }
                    .flowOn(Dispatchers.IO)   // Before this, all code run in passed Thread Pools
                    .collect {
                        Log.d(TAG, "Consumer " + Thread.currentThread().name) // Main
                        binding.flowDataTV.append("Consumer -> $it \n ")
                    }

            }
        }
    }

    private fun producer() = flow {
            val list = listOf(1, 2, 3, 4, 5, 6, 7, 8)
            list.forEach {
                delay(1000)
                Log.d(TAG, "Producer " + Thread.currentThread().name)
                emit(it)
            }
    }
}