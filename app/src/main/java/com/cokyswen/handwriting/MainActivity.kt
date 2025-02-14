package com.cokyswen.handwriting

import android.os.Bundle
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import com.cokyswen.handwriting.databinding.ActivityMainBinding

import androidx.databinding.DataBindingUtil

class MainActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
        mBinding = DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)

        mBinding.btnClear.setOnClickListener {
            mBinding.handwritingView.clearView()
        }

        mBinding.alphaSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                var alpha = progress * 1.0f / mBinding.alphaSeekBar.max
                mBinding.tvAlpha.text = "Alpha:$alpha"
                mBinding.handwritingView.pointManager.changeAlpha(alpha)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        })
        mBinding.widthSeekbar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                mBinding.tvWidth.text = "Width:$progress"
                mBinding.handwritingView.pointManager.setPenWidth(progress * 1f)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        })

    }
}