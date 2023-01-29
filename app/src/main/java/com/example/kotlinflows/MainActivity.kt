package com.example.kotlinflows

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.kotlinflows.databinding.ActivityMainBinding
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
            /********  Shared Flow ( Hot Flow )  *******/

            /*
            In Hot Streams --> all consumers are not independent; ie -> all consumer receive same data
                no matter when they start to receive data

                After flow started;  If anyOne start to receive data, it missed the previous data
                 */

            GlobalScope.launch(Dispatchers.Main) {
                val consumer = producer()
                consumer.collect {
                    Log.d(TAG, "Consumer 1, Item - $it " + Thread.currentThread().name) // Main Thread
                    binding.flowDataTV.append("Consumer 1 -> $it \n ")
                }
            }

            GlobalScope.launch(Dispatchers.Main){
                val consumer = producer()
                delay(2500)
                consumer.collect {
                    Log.d(TAG, "Consumer 2, Item - $it " + Thread.currentThread().name) // Main Thread
                    binding.flowDataTV.append("Consumer 2 -> $it \n ")

                }
            }
        }
    }

    private fun producer(): Flow<Int> {
        val mutableSharedFlow = MutableSharedFlow<Int>()
        GlobalScope.launch {
            val list = listOf(1, 2, 3, 4, 5, 6, 7, 8)
            list.forEach { value ->
                mutableSharedFlow.apply {
                    delay(1000)
                    Log.d(TAG, "Producer " + Thread.currentThread().name)
                    emit(value)
                }
            }
        }
        return mutableSharedFlow
    }
}