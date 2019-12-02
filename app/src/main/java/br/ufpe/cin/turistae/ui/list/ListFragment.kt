package br.ufpe.cin.turistae.ui.list

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import br.ufpe.cin.turistae.R
import br.ufpe.cin.turistae.data.TouristSpot
import com.google.firebase.database.*
import com.google.firebase.database.GenericTypeIndicator
import androidx.recyclerview.widget.LinearLayoutManager
import br.ufpe.cin.turistae.data.Address
import br.ufpe.cin.turistae.data.Location
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.item_tourist_spot.*

class ListFragment : Fragment() {

    private lateinit var database: DatabaseReference
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager
    private lateinit var recyclerView: RecyclerView
    private var touristSpots = ArrayList<TouristSpot>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        initTouristSpots()

        val view = inflater.inflate(R.layout.fragment_list, container, false)

        viewManager = LinearLayoutManager(this.context)
        viewAdapter = touristSpots.let { ListAdapter(it) }

//        database = FirebaseDatabase.getInstance().reference
//        database.addListenerForSingleValueEvent(menuListener)

        recyclerView = view.findViewById<RecyclerView>(R.id.mRecyclerView).apply {
            layoutManager = viewManager
            adapter = viewAdapter
        }

        return view
    }

    private fun initTouristSpots() {
        val t1 = TouristSpot(
            "Caixa Cultural Recife",
            Address("Av. Alfredo Lisboa", 505, "(81) 3425-1915", Location(-8.062598, -34.8734924)),
            "Caixa Cultural",
            "https://visit.recife.br/wp-content/uploads/2017/08/o-que-fazer-caixa-cultural-topo-mobile.jpg")

        val t2 = TouristSpot(
            "Marco Zero",
            Address("Bairro do Recife", 0, "sem telefone", Location(-8.063169, -34.871139)),
            "Marco zero do Recife",
            "http://transladorecife.com.br/wp-content/uploads/2015/09/marco-zero.jpg")

        touristSpots.add(t1)
        touristSpots.add(t2)
    }

    val menuListener = object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            val tasks = dataSnapshot.children.iterator()

            //Check if current database contains any collection
            if (tasks.hasNext()) {
                val listIndex = tasks.next()
                val itemsIterator = listIndex.children.iterator()

                //check if the collection has any task or not
                while (itemsIterator.hasNext()) {

                    //get current task
                    val currentItem = itemsIterator.next()

                    //get current data in a map
                    val map = currentItem.value as HashMap<*, *>

                    val name = map.get("name") as String
                    val description = map.get("description") as String
                    val photoUrl = map.get("photoUrl") as String

                    val addresJSON = map.get("") as HashMap<*, *>
                    val addressNumber = addresJSON.get("number") as Int
                    val addressPhone = addresJSON.get("phone") as String
                    val addressStreet = addresJSON.get("street") as String

                    val locationJSON = addresJSON.get("location") as HashMap<*, *>
                    val lat = locationJSON.get("lat") as Double
                    val lng = locationJSON.get("lng") as Double

                    val touristSpot = TouristSpot(
                        name,
                        Address(addressStreet, addressNumber, addressPhone, Location(lat, lng)),
                        description,
                        photoUrl)

                    touristSpots.add(touristSpot)
                }
            }


        }

        override fun onCancelled(databaseError: DatabaseError) {
            println("loadPost:onCancelled ${databaseError.toException()}")
        }
    }


}