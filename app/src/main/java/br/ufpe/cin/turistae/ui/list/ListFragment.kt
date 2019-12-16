package br.ufpe.cin.turistae.ui.list

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.ufpe.cin.turistae.R
import br.ufpe.cin.turistae.data.*
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_list.*
import leakcanary.AppWatcher

class ListFragment : Fragment(), AdapterView.OnItemSelectedListener {

    private lateinit var contextFragment: Context
    private var touristSpot = listOf<String>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_list, container, false)

        AppWatcher.objectWatcher.watch(this)
        touristSpot = resources.getStringArray(R.array.touristSpot).toList()
        val spinner = view.findViewById<Spinner>(R.id.spinnerFilter)
        if (spinner != null) {
            val adapter = ArrayAdapter(
                contextFragment,
                android.R.layout.simple_spinner_item, touristSpot
            )
            spinner.adapter = adapter
        }
        spinner.onItemSelectedListener = this

        return view
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        contextFragment = context!!
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {

    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        when (p2) {
            0 -> {
                val jsonArray =
                    Gson().fromJson(readJson(p2), Array<MercadoType>::class.java).toList()
                mRecyclerView.adapter = ListAdapter(jsonArray)
            }
            1 -> {
                val jsonArray = Gson().fromJson(readJson(p2), Array<PonteType>::class.java).toList()
                mRecyclerView.adapter = ListAdapter(jsonArray)
            }
            2 -> {
                val jsonArray =
                    Gson().fromJson(readJson(p2), Array<FeiraLivreType>::class.java).toList()
                mRecyclerView.adapter = ListAdapter(jsonArray)
            }
            3 -> {
                val jsonArray = Gson().fromJson(readJson(p2), Array<MuseuType>::class.java).toList()
                mRecyclerView.adapter = ListAdapter(jsonArray)
            }
            4 -> {
                val jsonArray =
                    Gson().fromJson(readJson(p2), Array<TeatroType>::class.java).toList()
                mRecyclerView.adapter = ListAdapter(jsonArray)
            }
            5 -> {
                val jsonArray =
                    Gson().fromJson(readJson(p2), Array<ShoppingType>::class.java).toList()
                mRecyclerView.adapter = ListAdapter(jsonArray)
            }
        }
    }

    private fun readJson(pos: Int): String? =
        activity?.applicationContext?.assets?.open("${getJsonFileName(pos)}.json")?.bufferedReader()
            .use { it?.readText() }

    private fun getJsonFileName(pos: Int): String {
        return when (pos) {
            0 -> "mercadospublicos"
            1 -> "pontesdorecife"
            2 -> "feiraslivre"
            3 -> "museus"
            4 -> "teatros"
            else -> "shopping"
        }
    }
}