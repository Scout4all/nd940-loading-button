package com.udacity

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.udacity.databinding.ActivityMainBinding
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.content_main.*


class MainActivity : AppCompatActivity() {




    lateinit var binding: ActivityMainBinding

    lateinit var viewModel: MainActivityViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        setSupportActionBar(toolbar)

        viewModel = ViewModelProvider(this).get(MainActivityViewModel::class.java)
        binding.viewModel = viewModel
        binding.contentMain.customButton.loadingBtnBgColor = getColor(R.color.colorPrimaryDark)
        binding.contentMain.customButton.loadingBtnProgressColor = getColor(R.color.colorPrimary)
        binding.contentMain.customButton.loadingBtnTextColor = getColor(R.color.white)

        binding.contentMain.customButton.setOnClickListener {
            if (viewModel.selectedLibrary.value == -1) {
//                Toast.makeText(this, "No file selected to Download", Toast.LENGTH_SHORT).show()
            } else {
                viewModel.download()
                viewModel.downloaded.observe(this, Observer {
                    if(it){
                        binding.contentMain.customButton.hasCompletedDownload()
                    }
                })
            }
        }

        observers()

    }

    private fun observers() {
        viewModel.selectedLibrary.observe(this) {
            Log.w("act", it.toString())
            if (it != -1) {
                binding.contentMain.customButton.visibility = View.VISIBLE
            }

        }
    }


}
