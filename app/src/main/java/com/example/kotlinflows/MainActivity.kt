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

                 Case -> I want to produce data in IO thread & consume it in Main Thread ;
                    Use FlowON ; in multiple flowOn: flowOn always work upstreams,
                                 code b/w ABC to XYZ flowOn is run in XYZ flowOn 
                 */

                producer()
                    .map {
                        Log.d(TAG, "Consumer map " + Thread.currentThread().name) // Default thread
                        it * 2
                    }
                    .buffer(2)
                    .flowOn(Dispatchers.Unconfined)  // Before this or b/w last to this flowOn; all code run in passed Thread Pools
                    .filter {
                        Log.d(TAG, "Consumer filter " + Thread.currentThread().name) // IO thread
                        it <= 10
                    }
                    .flowOn(Dispatchers.IO)   // Before this or b/w last to this flowOn; all code run in passed Thread Pools
                    .collect {
                        Log.d(TAG, "Consumer " + Thread.currentThread().name) // Main Thread
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