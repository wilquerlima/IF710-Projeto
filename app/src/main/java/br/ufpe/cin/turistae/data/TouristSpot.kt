package br.ufpe.cin.turistae.data

import java.io.Serializable

sealed class BaseItem : Serializable

data class ShoppingType(
    val nome: String = "",
    val descricao: String = "",
    val bairro: String = "",
    val logradouro: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val telefone: String = "",
    val site: String = "",
    val funcionamento: String = "",
    val funcionamentoDomingo: String = ""
) : BaseItem(), Serializable

data class TeatroType(
    val nome: String = "",
    val descricao: String = "",
    val bairro: String = "",
    val logradouro: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val telefone: String = ""
) : BaseItem(), Serializable

data class MuseuType(
    val nome: String = "",
    val descricao: String = "",
    val bairro: String = "",
    val logradouro: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val telefone: String = "",
    val site: String = ""
) : BaseItem(), Serializable

data class FeiraLivreType(
    val nome: String = "",
    val localizacao: String = "",
    val dias: String = "",
    val horario: String = "",
    val observacao: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0
) : BaseItem(), Serializable

data class PonteType(
    val nome: String = "",
    val descricao: String = "",
    val bairro: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0
) : BaseItem(), Serializable


data class MercadoType(
    val nome: String = "",
    val descricao: String = "",
    val bairro: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0
) : BaseItem(), Serializable