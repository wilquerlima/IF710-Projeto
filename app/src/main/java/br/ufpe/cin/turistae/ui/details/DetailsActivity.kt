package br.ufpe.cin.turistae.ui.details

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import br.ufpe.cin.turistae.R
import br.ufpe.cin.turistae.data.*
import kotlinx.android.synthetic.main.activity_details.*
import leakcanary.AppWatcher

class DetailsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        AppWatcher.objectWatcher.watch(this)
        val spot = intent.getSerializableExtra("spot") as BaseItem
        rvDetails.adapter = DetailsAdapter(spot)
    }
}