package br.ufpe.cin.turistae.ui.list

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.ufpe.cin.turistae.R
import br.ufpe.cin.turistae.data.*
import br.ufpe.cin.turistae.ui.details.DetailsActivity
import kotlinx.android.synthetic.main.item_feira.view.*
import kotlinx.android.synthetic.main.item_ponte_mercado.view.*
import kotlinx.android.synthetic.main.item_shopping.view.*
import kotlinx.android.synthetic.main.item_shopping.view.name

class ListAdapter(private val myDataset: List<BaseItem>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun getItemViewType(position: Int): Int {
        return when (myDataset[position]) {
            is ShoppingType -> SHOPPING_TYPE
            is TeatroType -> TEATRO_TYPE
            is MuseuType -> MUSEU_TYPE
            is FeiraLivreType -> FEIRA_LIVRE_TYPE
            is PonteType -> PONTE_TYPE
            else -> MERCADO_TYPE
        }
    }

    override fun getItemCount() = myDataset.size

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        when (viewType) {
            SHOPPING_TYPE -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_shopping, parent, false)
                return ShoppingViewHolder(view)
            }
            TEATRO_TYPE -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_shopping, parent, false)
                return TeatroViewHolder(view)
            }
            MUSEU_TYPE -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_shopping, parent, false)
                return MuseuViewHolder(view)
            }
            FEIRA_LIVRE_TYPE -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_feira, parent, false)
                return FeiraViewHolder(view)
            }
            PONTE_TYPE -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_ponte_mercado, parent, false)
                return PonteViewHolder(view)
            }
            else -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_ponte_mercado, parent, false)
                return MercadoViewHolder(view)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            SHOPPING_TYPE -> initShoppingVH(holder as ShoppingViewHolder, position)
            TEATRO_TYPE -> initTeatroVH(holder as TeatroViewHolder, position)
            MUSEU_TYPE -> initMuseuVH(holder as MuseuViewHolder, position)
            FEIRA_LIVRE_TYPE -> initFeiraVH(holder as FeiraViewHolder, position)
            PONTE_TYPE -> initPonteMercadoVH(holder as PonteViewHolder, position)
            MERCADO_TYPE -> initMercadoMercadoVH(holder as MercadoViewHolder, position)
        }
    }

    private fun initShoppingVH(holder: ShoppingViewHolder, pos: Int) {
        val item = myDataset[pos] as ShoppingType
        holder.bind(item)
    }

    private fun initTeatroVH(holder: TeatroViewHolder, pos: Int) {
        val item = myDataset[pos] as TeatroType
        holder.bind(item)
    }

    private fun initMuseuVH(holder: MuseuViewHolder, pos: Int) {
        val item = myDataset[pos] as MuseuType
        holder.bind(item)
    }

    private fun initFeiraVH(holder: FeiraViewHolder, pos: Int) {
        val item = myDataset[pos] as FeiraLivreType
        holder.bind(item)
    }

    private fun initPonteMercadoVH(holder: PonteViewHolder, pos: Int) {
        val item = myDataset[pos] as PonteType
        holder.bind(item)
    }

    private fun initMercadoMercadoVH(holder: MercadoViewHolder, pos: Int) {
        val item = myDataset[pos] as MercadoType
        holder.bind(item)
    }

    inner class ShoppingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: ShoppingType) {
            with(itemView) {
                name.text = item.nome
                description.text = item.descricao
                telefone.text = item.telefone

                setOnClickListener {
                    context.startActivity(
                        Intent(
                            context,
                            DetailsActivity::class.java
                        ).putExtra("spot", item)
                    )
                }
            }
        }
    }

    inner class TeatroViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: TeatroType) {
            with(itemView) {
                name.text = item.nome
                description.text = item.descricao
                telefone.text = item.telefone

                setOnClickListener {
                    context.startActivity(
                        Intent(
                            context,
                            DetailsActivity::class.java
                        ).putExtra("spot", item)
                    )
                }
            }
        }
    }

    inner class MuseuViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: MuseuType) {
            with(itemView) {
                name.text = item.nome
                description.text = item.descricao
                telefone.text = item.telefone

                setOnClickListener {
                    context.startActivity(
                        Intent(
                            context,
                            DetailsActivity::class.java
                        ).putExtra("spot", item)
                    )
                }
            }
        }
    }

    inner class FeiraViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: FeiraLivreType) {
            with(itemView) {
                name.text = item.nome
                localizacao.text = item.localizacao
                horario.text = item.horario

                setOnClickListener {
                    context.startActivity(
                        Intent(
                            context,
                            DetailsActivity::class.java
                        ).putExtra("spot", item)
                    )
                }
            }
        }
    }

    inner class PonteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: PonteType) {
            with(itemView) {
                name.text = item.nome
                descricao.text = item.descricao
                bairro.text = item.bairro

                setOnClickListener {
                    context.startActivity(
                        Intent(
                            context,
                            DetailsActivity::class.java
                        ).putExtra("spot", item)
                    )
                }
            }
        }
    }

    inner class MercadoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: MercadoType) {
            with(itemView) {
                name.text = item.nome
                descricao.text = item.descricao
                bairro.text = item.bairro

                setOnClickListener {
                    context.startActivity(
                        Intent(
                            context,
                            DetailsActivity::class.java
                        ).putExtra("spot", item)
                    )
                }
            }
        }
    }

    companion object {
        const val SHOPPING_TYPE = 0
        const val TEATRO_TYPE = 1
        const val MUSEU_TYPE = 2
        const val FEIRA_LIVRE_TYPE = 3
        const val PONTE_TYPE = 4
        const val MERCADO_TYPE = 5
    }
}