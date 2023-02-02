/*
 * Copyright (c) 2023.
 *
 *  Developed by : Bigad Aboubakr
 *  Developer website : http://bigad.me
 *  Developer github : https://github.com/Scout4all
 *  Developer Email : bigad@bigad.me
 */

package com.udacity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.udacity.databinding.ActivityMainBinding
import com.udacity.uicomponents.ButtonState
import kotlinx.android.synthetic.main.activity_detail.*


class MainActivity : AppCompatActivity() {


    lateinit var binding: ActivityMainBinding

    lateinit var viewModel: MainActivityViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        setSupportActionBar(toolbar)

        viewModel = ViewModelProvider(this).get(MainActivityViewModel::class.java)
        binding.viewModel = viewModel

        //Testing change custom button attributes from activity
        binding.contentMain.customButton.loadingBtnBgColor = getColor(R.color.colorPrimaryDark)
        binding.contentMain.customButton.loadingBtnProgressColor = getColor(R.color.colorPrimary)
        binding.contentMain.customButton.loadingBtnTextColor = getColor(R.color.white)


        binding.contentMain.customButton.setOnClickListener {
            //check button state if completed move to detail activity
            if (binding.contentMain.customButton.buttonState == ButtonState.Completed) {
                val intent = Intent(this, DetailActivity::class.java)
                intent.putExtra("status", viewModel.downloadStatus)
                intent.putExtra("fileName", viewModel.selectedFile.value!!.fileName)
                startActivity(intent)
            } else {
                //check if file selected in view model or not
                if (viewModel.selectedFile.value?.fileIndex == -1) {
                    Toast.makeText(this, "No file selected to Download", Toast.LENGTH_SHORT).show()
                } else {
                    //if file selected change button state to loading to animate
                    binding.contentMain.customButton.buttonState = ButtonState.Loading
                    // start download file
                    viewModel.download()
                }

            }

        }

        observers()

    }

    private fun observers() {

        // observe if file downloaded in view model to change button state
        viewModel.fileDownloadedState.observe(this, Observer { downloadedStatus ->
            if (downloadedStatus) {
                binding.contentMain.customButton.hasCompletedDownload()
            }
        })

    }


}
