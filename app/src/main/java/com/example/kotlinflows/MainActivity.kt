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
            /********  State Flow ( Hot Flow )  *******/

            /*
            In Hot Streams --> all consumers are not independent; ie -> all consumer receive same data
                no matter when they start to receive data

             After flow started;  If anyOne start to receive data, it missed the previous data

             Shared Flow --> not maintain state
             State Flow --> maintain his state; latest (last) value maintain as a state
                    like a single buffer
                 */

            GlobalScope.launch(Dispatchers.Main) {
                val consumer = producer()
                delay(5000)
                /*
                consumer was added after 5 second but data produced completely in 4 seconds .
                   in Shared Flow case -> consume didn't receive any data
                   but in State Flow case  -> State Flow maintain his state, so consumer get the last value
                 */
                consumer.collect {
                    Log.d(TAG, "Consumer, Item - $it " + Thread.currentThread().name) // Main Thread
                    binding.flowDataTV.append("Consumer -> $it \n ")
                }
            }
        }
    }

    private fun producer(): Flow<Int> {   // You can also return -> MutableSharedFlow / SharedFlow / Flow
        val mutableStatedFlow = MutableStateFlow(0)
        // replay --> same as buffer(); scenario -> may be producer is slow or late
        GlobalScope.launch {
            val list = listOf(1, 2, 3, 4)
            list.forEach { value ->
                mutableStatedFlow.apply {
                    delay(1000)
                    emit(value)
                }
            }
        }
        return mutableStatedFlow
    }
}