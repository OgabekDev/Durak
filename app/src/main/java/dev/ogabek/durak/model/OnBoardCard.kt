package dev.ogabek.durak.model

data class OnBoardCard (
    val firstCard: Card?,
    var secondCard: Card? = null
) {
    fun cards(): Collection<Card> {
        val list = arrayListOf<Card>()
        list.add(firstCard!!)
        if (secondCard != null) {
            list.add(secondCard!!)
        }
        return list
    }

}