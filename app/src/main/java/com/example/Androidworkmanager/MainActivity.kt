package com.example.Androidworkmanager

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.work.*
import com.douglasstarnes.basicworkmanager.WorkStatusSingleton
import com.example.Androidworkmanager.databinding.ActivityMainBinding
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    //instance of the WorkManager
    private val workManager = WorkManager.getInstance(this)

    lateinit var btnStartWork: Button
    lateinit var btnWorkStatus: Button
    lateinit var btnResetStatus: Button
    lateinit var btnWorkUIThread: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        btnStartWork = binding.btnStartWork
        btnWorkStatus = binding.btnWorkStatus
        btnResetStatus = binding.btnResetStatus
        btnWorkUIThread = binding.btnWorkUIThread

        btnStartWork.setOnClickListener {
            //creating a one time work request
            //Java
//            val workRequest = OneTimeWorkRequest.Builder(SimpleWorker::class.java).build()

            //Java
//            val data = Data.Builder().putString("WORK_MESSAGE", "Work Completed!")

            //Work Constraint
            val constraints = Constraints.Builder()
                .setRequiresCharging(true)
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()

            val data = workDataOf("WORK_MESSAGE" to "Work Completed!")

            //Kotlin
            val workRequest = OneTimeWorkRequestBuilder<SimpleWorker>()
                    //input data to the worker
                .setInputData(data)
                    //set constraint
                .setConstraints(constraints)
                .build()
            //to repeat tasks periodically
            val periodicWorkRequest = PeriodicWorkRequestBuilder<SimpleWorker>(
                5, TimeUnit.MINUTES,
            1, TimeUnit.MINUTES).build()
            //enqueuing the workRequest in the workManager
            workManager.enqueue(workRequest)
        }

        btnWorkStatus.setOnClickListener {
            val toast = Toast.makeText(this, "The work status is: ${WorkStatusSingleton.workMessage}", Toast.LENGTH_SHORT)
            toast.show()
        }

        btnResetStatus.setOnClickListener {
            WorkStatusSingleton.workComplete = false
        }

        btnWorkUIThread.setOnClickListener {
            Thread.sleep(10000)
            WorkStatusSingleton.workComplete = true
        }

    }
}