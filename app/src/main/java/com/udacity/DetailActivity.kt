package com.udacity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.udacity.databinding.ActivityDetailBinding
import kotlinx.android.synthetic.main.activity_detail.*
import java.util.*
import kotlin.concurrent.schedule

class DetailActivity : AppCompatActivity() {
    lateinit var binding: ActivityDetailBinding
    lateinit var viewModel: DetailActivityViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail)
        setSupportActionBar(toolbar)
        viewModel = ViewModelProvider(this).get(DetailActivityViewModel::class.java)
        binding.viewModel = viewModel

        val intent = getIntent()
        if (intent.extras != null) {
            if (!intent.getStringExtra("status").isNullOrEmpty()) {
                val fileStatus = intent.getStringExtra("status")
                viewModel.fileStatus.value = fileStatus
                when (fileStatus) {
                    "Paused" -> binding.contentDetail.fileStatus.setTextColor(resources.getColor(R.color.download_paused))
                    "Pending" -> binding.contentDetail.fileStatus.setTextColor(resources.getColor(R.color.download_pending))
                    "Running" -> binding.contentDetail.fileStatus.setTextColor(resources.getColor(R.color.download_running))
                    "Success" -> binding.contentDetail.fileStatus.setTextColor(resources.getColor(R.color.download_success))
                    "Fail" -> binding.contentDetail.fileStatus.setTextColor(resources.getColor(R.color.download_fail))
                }

            }
            if (!intent.getStringExtra("fileName").isNullOrEmpty()) {
                val filename = intent.getStringExtra("fileName")
                viewModel.fileName.value = filename

            }
        }

        binding.contentDetail.backBtn.setOnClickListener {
            startActivity(Intent(this,MainActivity::class.java))
        }

    }

    override fun onResume() {
        super.onResume()
//start animation
        // Use
        Timer().schedule(500){
            binding.contentDetail.contentDetailMotionLayout.transitionToEnd()
        }


    }


}
