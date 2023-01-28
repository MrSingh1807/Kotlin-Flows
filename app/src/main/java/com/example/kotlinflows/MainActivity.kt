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
                /*    Some Events , Which we can handle */
                // we can manually emit values for conditions
                producer()
                    .onStart {
                        emit( -1)
                        binding.flowDataTV.append("Consumer Start Receiving ")
                    }
                    .onCompletion {
                        emit( 10)
                        binding.flowDataTV.append("Consumer Completed  his receiving")
                    }
                    .onEach {
                        binding.flowDataTV.append("Consumer $it Received ")
                    }
                    .onEmpty {
                        binding.flowDataTV.append("Data is Empty ")
                    }
                    .collect {
                        Log.d(TAG, "Consumer -> $it \n  ")
                        binding.flowDataTV.append("Consumer -> $it \n  ")
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