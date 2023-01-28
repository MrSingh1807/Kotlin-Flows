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
            /********  Cold Flows   *******/

            /*       ******  Multiple Consumers  *******
            In Cold Streams --> all consumers are independent; ie -> all consumer receive data in Start
                no matter when they start to receive data
             */
            CoroutineScope(Dispatchers.Main).launch {
            val data: Flow<Int> = producer()
            data.collect {
                // ie: Consumer 1; Here we collect data,
                Log.d(TAG, "Consumer 1 -> $it \n  ")
                binding.flowDataTV.append("Consumer 1 -> $it \n  ")
            }
        }
            CoroutineScope(Dispatchers.Main).launch {
                val data: Flow<Int> = producer()
                data.collect {
                    // ie: Consumer 2 ; Here we collect data,

                    delay(2000)  // Consumer 2; is slow to receive data
                    Log.d(TAG, "Consumer 2 -> $it \n ")
                    binding.flowDataTV.append("Consumer 2 -> $it \n  ")
                }
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