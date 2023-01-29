package com.example.kotlinflows

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.kotlinflows.databinding.ActivityMainBinding
import com.example.kotlinflows.model.FormattedNote
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import java.lang.Exception

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
                /******    Exception Handling  *****/

                /*
                There are two Specific point where error can be occur;
                       either -> producing data
                       or -> collecting data

                     => You can Segregate  errors
                     => You can use multiple catches as flowOn
                 */

                try {
                    producer()
                        .collect {
                            Log.d(TAG, "Consumer " + Thread.currentThread().name) // Main Thread
                            binding.flowDataTV.append("Consumer -> $it \n ")
                            throw Exception("Error in Consumer")
                        }

                } catch (e: Exception) {
                    Log.d(TAG, "Producers ->  " + e.message.toString())
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
            throw Exception("Error in Producer")
        }
    }.catch {
        // here, you receive all producer's errors
        // Additional Benefit - you can pass additional callback elements (emit something) according yourself

        Log.d(TAG, "Producer -> ${it.message}")
        emit(-1)  // add addition callback
    }
}