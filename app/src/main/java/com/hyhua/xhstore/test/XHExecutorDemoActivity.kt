package com.hyhua.xhstore.test

import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.hyhua.xhlibrary.executor.XHExecutor
import com.hyhua.xhstore.R

class XHExecutorDemoActivity : AppCompatActivity() {

    private var isPaused: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_xhexecutor_demo)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val btn1 = findViewById<Button>(R.id.btn_1)
        btn1.setOnClickListener {
            for (priority in 0..9) {
                val finalPriority = priority
                XHExecutor.execute(finalPriority) {
                    try {
                        Thread.sleep(10000) // 10秒长时间任务
                    } catch (e: InterruptedException) {
                        e.printStackTrace()
                    }
                }
            }
        }

        val btn2 = findViewById<Button>(R.id.btn_2)
        btn2.setOnClickListener {
            if (isPaused) {
                XHExecutor.resume()
            } else {
                XHExecutor.pause()
            }
            isPaused = !isPaused
        }

        val btn3 = findViewById<Button>(R.id.btn_3)
        btn3.setOnClickListener {
            XHExecutor.execute(
                runnable = object : XHExecutor.Callable<String>() {

                    override fun onBackground(): String {
                        Log.e("XHExecutorDemoActivity", "onBackground-当前线程是：" + Thread.currentThread().name)
                        return "我是异步任务的结果"
                    }

                    override fun onCompleted(result: String) {
                        Log.e("XHExecutorDemoActivity", "onCompleted-当前线程是：" + Thread.currentThread().name)
                        Log.e("XHExecutorDemoActivity", "onCompleted-任务结果是：$result")
                    }

                })
        }
    }
}