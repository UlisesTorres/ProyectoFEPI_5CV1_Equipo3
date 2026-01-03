package com.example.myapplication.view.supervisor

import android.os.Bundle
import androidx.activity.ComponentActivity
import com.example.myapplication.R

class SupervisorActivity : ComponentActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Aquí se hace la unión:
        setContentView(R.layout.activity_supervisor)
    }
}