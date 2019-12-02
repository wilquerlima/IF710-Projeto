package br.ufpe.cin.turistae.data

data class Address(val street: String, val number: Int, val phone: String, val location: Location) {
    override fun toString(): String = "$street, $number $phone"
}