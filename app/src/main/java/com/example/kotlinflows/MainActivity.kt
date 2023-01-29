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
                 */

                try {
                    producer()
                        .collect {
                            Log.d(TAG, "Consumer " + Thread.currentThread().name) // Main Thread
                            binding.flowDataTV.append("Consumer -> $it \n ")
//                            throw Exception("Error in Collector")
                        }

                } catch (e: Exception){
                    // You can easily handel the both errors in one try catch
                    Log.d(TAG, e.message.toString())
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
    }
}