package br.ufpe.cin.turistae.ui.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import br.ufpe.cin.turistae.R
import br.ufpe.cin.turistae.data.TouristSpot
import com.squareup.picasso.Picasso

class ListAdapter(private val myDataset: List<TouristSpot>) :
    RecyclerView.Adapter<ListAdapter.MyViewHolder>() {

    class MyViewHolder(val cardView: CardView) : RecyclerView.ViewHolder(cardView)


    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): MyViewHolder {
        val textView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_tourist_spot, parent, false) as CardView

        return MyViewHolder(textView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val imageView = holder.cardView.findViewById<AppCompatImageView>(R.id.photo)
        val nameView = holder.cardView.findViewById<AppCompatTextView>(R.id.name)
        val descriptionView = holder.cardView.findViewById<AppCompatTextView>(R.id.description)
        val addressView = holder.cardView.findViewById<AppCompatTextView>(R.id.address)

        val touristSpot = myDataset[position]

        Picasso.get().load(touristSpot.photoUrl).resize(150, 150).into(imageView)
        nameView.text = touristSpot.name
        descriptionView.text = touristSpot.description
        addressView.text = touristSpot.address.toString()
    }

    override fun getItemCount() = myDataset.size
}