package br.ufpe.cin.turistae.ui.details

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.ufpe.cin.turistae.R
import br.ufpe.cin.turistae.data.*
import kotlinx.android.synthetic.main.item_feira_details.view.*
import kotlinx.android.synthetic.main.item_museu_details.view.*
import kotlinx.android.synthetic.main.item_ponte_mercado_details.view.*
import kotlinx.android.synthetic.main.item_shopping_details.view.*
import kotlinx.android.synthetic.main.item_shopping_details.view.bairro
import kotlinx.android.synthetic.main.item_teatro_details.view.*

class DetailsAdapter(private val dataType: BaseItem) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun getItemViewType(position: Int): Int {
        return when (dataType) {
            is ShoppingType -> SHOPPING_TYPE
            is TeatroType -> TEATRO_TYPE
            is MuseuType -> MUSEU_TYPE
            is FeiraLivreType -> FEIRA_LIVRE_TYPE
            is PonteType -> PONTE_TYPE
            else -> MERCADO_TYPE
        }
    }

    override fun getItemCount() = 1

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        when (viewType) {
            SHOPPING_TYPE -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_shopping_details, parent, false)
                return ShoppingViewHolder(view)
            }
            TEATRO_TYPE -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_teatro_details, parent, false)
                return TeatroViewHolder(view)
            }
            MUSEU_TYPE -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_museu_details, parent, false)
                return MuseuViewHolder(view)
            }
            FEIRA_LIVRE_TYPE -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_feira_details, parent, false)
                return FeiraViewHolder(view)
            }
            PONTE_TYPE -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_ponte_mercado_details, parent, false)
                return PonteViewHolder(view)
            }
            else -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_ponte_mercado_details, parent, false)
                return MercadoViewHolder(view)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            SHOPPING_TYPE -> initShoppingVH(holder as ShoppingViewHolder)
            TEATRO_TYPE -> initTeatroVH(holder as TeatroViewHolder)
            MUSEU_TYPE -> initMuseuVH(holder as MuseuViewHolder)
            FEIRA_LIVRE_TYPE -> initFeiraVH(holder as FeiraViewHolder)
            PONTE_TYPE -> initPonteVH(holder as PonteViewHolder)
            MERCADO_TYPE -> initMercadoVH(holder as MercadoViewHolder)
        }
    }

    private fun initShoppingVH(holder: ShoppingViewHolder) {
        val item = dataType as ShoppingType
        holder.bind(item)
    }

    private fun initTeatroVH(holder: TeatroViewHolder) {
        val item = dataType as TeatroType
        holder.bind(item)
    }

    private fun initMuseuVH(holder: MuseuViewHolder) {
        val item = dataType as MuseuType
        holder.bind(item)
    }

    private fun initFeiraVH(holder: FeiraViewHolder) {
        val item = dataType as FeiraLivreType
        holder.bind(item)
    }

    private fun initPonteVH(holder: PonteViewHolder) {
        val item = dataType as PonteType
        holder.bind(item)
    }

    private fun initMercadoVH(holder: MercadoViewHolder) {
        val item = dataType as MercadoType
        holder.bind(item)
    }

    inner class ShoppingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: ShoppingType) {
            with(itemView) {
                nameShopping.text = item.nome
                descricao.text = item.descricao
                val bairroShopping = item.bairro + " - " + item.logradouro
                bairro.text = bairroShopping
                telefone.text = item.telefone
                site.text = item.site
                funcionamento.text = item.funcionamento
                funcionamentoDomingo.text = item.funcionamentoDomingo
            }
        }
    }

    inner class TeatroViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: TeatroType) {
            with(itemView) {
                nameTeatro.text = item.nome
                descricaoTeatro.text = item.descricao
                val bairro = item.bairro + " - " + item.logradouro
                bairroTeatro.text = bairro
                telefoneTeatro.text = item.telefone
            }
        }
    }

    inner class MuseuViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: MuseuType) {
            with(itemView) {
                nameMuseu.text = item.nome
                descricaoMuseu.text = item.descricao
                val bairro = item.bairro + " - " + item.logradouro
                bairroMuseu.text = bairro
                telefoneMuseu.text = item.telefone
                siteMuseu.text = item.site
            }
        }
    }

    inner class FeiraViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: FeiraLivreType) {
            with(itemView) {
                nameFeira.text = item.nome
                localizacaoFeira.text = item.localizacao
                diasFeira.text = item.dias
                horarioFeira.text = item.horario
                if (observacaoFeira.text != "") observacaoFeira.visibility =
                    View.INVISIBLE else observacaoFeira.text = item.observacao
            }
        }
    }

    inner class PonteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: PonteType) {
            with(itemView) {
                imagePonte.background = resources.getDrawable(R.drawable.ic_bridge)
                name.text = item.nome
                desc.text = item.descricao
                bairro.text = item.bairro
            }
        }
    }

    inner class MercadoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: MercadoType) {
            with(itemView) {
                imagePonte.background = resources.getDrawable(R.drawable.ic_market)
                name.text = item.nome
                desc.text = item.descricao
                bairro.text = item.bairro
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