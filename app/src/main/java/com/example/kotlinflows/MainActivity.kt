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
                /******    Flow Operators  *****/

                /*****
                Terminal Operator --> These operators start the flows
                Non Terminal Operator --> These operators, plays with data
                 *****/
                DummyData().getNotes().apply {
                    // Non Terminal Operator
                    map {
                        FormattedNote(it.isActive, it.title.toUpperCase(), it.description.toUpperCase())
                    }
                    filter {
                        it.isActive
                    }
                    collect{
                        binding.startFlowBTN.append(" $it \n ")
                    }
                }

            }
        }
    }
}