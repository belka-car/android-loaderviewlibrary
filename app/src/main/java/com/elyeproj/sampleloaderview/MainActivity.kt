package com.elyeproj.sampleloaderview

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.elyeproj.sampleloaderview.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val vm: MainViewModel by viewModels()
    private lateinit var vb: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        vb = ActivityMainBinding.inflate(layoutInflater)
        setContentView(vb.root)

        vm.data.observe(this, ::postLoadData)

        vb.reset.setOnClickListener { resetLoader() }
    }

    private fun postLoadData(data: Data) {
        vb.name.text = data.name
        vb.title.text = data.title
        vb.phone.text = data.phone
        vb.email.text = data.email
        vb.icon.setImageResource(data.image)
    }

    private fun resetLoader() {
        vb.name.resetLoader()
        vb.title.resetLoader()
        vb.phone.resetLoader()
        vb.email.resetLoader()
        vb.icon.resetLoader()
        vm.loadData()
    }
}