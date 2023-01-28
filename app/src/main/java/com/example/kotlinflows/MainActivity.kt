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
            /********  Cold Flows   *******/

            CoroutineScope(Dispatchers.Main).launch {
                /******    Flow Operators  *****/

                /*****
                Terminal Operator --> These operators start the flows
                Non Terminal Operator --> These operators, plays with data
                 *****/

                val result = producer()
                result.apply {
                    // Terminal Operator ; Some Common Operators
                    toList()  // First Consume all data & then update
                    first()
                    collect {
                        Log.d(TAG, "Consumer -> $it \n  ")
                        binding.flowDataTV.append("Consumer -> $it \n  ")
                    }
                }

                result.apply {
                    // Non Terminal Operator; Some Common Operators
                    map {
                        // Convert My data
                        it * 2
                    }
                    filter {
                        it < 8
                    }
                    /***** We need a terminal Operator for start flows
                    bcz Non terminal Operator can n't start the flow *****/
                    collect()
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