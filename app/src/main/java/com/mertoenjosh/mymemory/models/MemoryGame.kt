package com.mertoenjosh.mymemory.models

import com.mertoenjosh.mymemory.utils.DEFAULT_ICONS

class MemoryGame(private val boardSize: BoardSize, private val customImages: List<String>?) {
    val cards: List<MemoryCard>
    var numPairsFound = 0

    var numCardFlips = 0

    private var indexOfSingleSelectedCard: Int? = null

    init {

        if (customImages == null) {
            val chosenImages: List<Int> = DEFAULT_ICONS.shuffled().take(boardSize.getNumPairs())
            val randomizedImages: List<Int> = (chosenImages + chosenImages).shuffled()

            cards = randomizedImages.map { MemoryCard(it) }
        } else {
            val randomizedImages = (customImages + customImages).shuffled()
            cards = randomizedImages.map { MemoryCard(it.hashCode(), it) }
        }

    }

    fun flipCard(position: Int): Boolean {
        numCardFlips++
        val card: MemoryCard = cards[position]
        // Three cases
        // 0 cards flipped over => flip the selected card

        // 1 cards flipped over => flip over the selected card + check if images match
        // 2 cards flipped over => restore cards + flip over the selected card
        var foundMatch = false
        if (indexOfSingleSelectedCard == null) {
            // 0 or 2 cards previously flipped over
            restoreCard()
            indexOfSingleSelectedCard = position
        } else {
            foundMatch = checkMatch(indexOfSingleSelectedCard!!, position)
            indexOfSingleSelectedCard = null
        }
        card.isFaceUp = !card.isFaceUp
        return foundMatch
    }

    private fun checkMatch(position1: Int, position2: Int): Boolean {

        if(cards[position1].identifier != cards[position2].identifier) {
            return false
        }
        cards[position1].isMatched = true
        cards[position2].isMatched = true
        numPairsFound++

        return true
    }

    private fun restoreCard() {
        for (card in cards)
            if (!card.isMatched) {
                card.isFaceUp = false
            }
    }

    fun haveWonGame(): Boolean {
        return numPairsFound == boardSize.getNumPairs()
    }

    fun isCardFaceUP(position: Int): Boolean {
        return cards[position].isFaceUp
    }

    fun getNumMoves(): Int {
        return numCardFlips / 2
    }

}