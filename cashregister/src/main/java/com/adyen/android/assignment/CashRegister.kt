package com.adyen.android.assignment

import com.adyen.android.assignment.money.Bill
import com.adyen.android.assignment.money.Change
import com.adyen.android.assignment.money.Coin

/**
 * The CashRegister class holds the logic for performing transactions.
 *
 * @param change The change that the CashRegister is holding.
 */
class CashRegister(private val change: Change) {
    /**
     * Performs a transaction for a product/products with a certain price and a given amount.
     *
     * @param price The price of the product(s).
     * @param amountPaid The amount paid by the shopper.
     *
     * @return The change for the transaction.
     *
     * @throws TransactionException If the transaction cannot be performed.
     */
    fun performTransaction(price: Long, amountPaid: Change): Change =
        when {
            change.total == 0L -> {
                //Change cannot be given if there is no change currently in the register
                throw TransactionException(message = "Register does not contain any change to settle customer")
            }
            price < amountPaid.total -> {
                evaluateChange(price, amountPaid)
            }
            price == amountPaid.total -> {
                //if shopper pays exact amount no change should be given
                Change.none()
            }
            else -> {
                //If shopper pays less than price of product(s) an exception has to be thrown
                throw TransactionException(message = "Shopper has paid less than the minimum required amount")
            }
        }


    private fun evaluateChange(price: Long, amountPaid: Change): Change {
        //determine value of change
        val balance = amountPaid.total - (price)

        //put money into cash register
        change.addToRegister(amountPaid)

        //to match modified notation structure
        return matchBalance(balance)
    }

    private fun arrangeBills(): List<Bill> {
        val largestCount = change.getCount(Bill.FIVE_HUNDRED_EURO)
        val smallerChange = change.remove(Bill.FIVE_HUNDRED_EURO, largestCount)
        return smallerChange.getElements().filterIsInstance<Bill>()
            .sortedByDescending { it.minorValue }
    }

    private fun arrangeCoins(): List<Coin> =
        change.getElements().filterIsInstance<Coin>().sortedByDescending { it.minorValue }

    private fun matchBalance(balance: Long): Change {
        //find if balance is smaller than smallest available bill
        //change to lowest denomination of bill in register
        if (balance < Bill.FIVE_EURO.minorValue) {
            return makeChangeFromCoins(balance)
        } else {
            val billChange = makeChangeFromBills(balance)
            if (billChange.total != balance) {
                return makeChangeFromCoins(balance - billChange.total, billChange)
            } else {
                return billChange
            }
        }
    }

    private fun makeChangeFromBills(balance: Long): Change {
        var matchingBalance = balance
        val result = Change.none()
        val bills = arrangeBills()
        for (bill in bills) {
            if (matchingBalance >= bill.minorValue){
                val requiredPieces = matchingBalance / bill.minorValue
                val actualPieces = change.getCount(bill)
                val count = if (actualPieces >= requiredPieces) requiredPieces.toInt() else actualPieces
                result.add(bill, count)
                matchingBalance -= (bill.minorValue) * count
            }
        }
        return result
    }

    private fun makeChangeFromCoins(balance: Long, billChange: Change = Change.none()): Change {
        var matchingBalance = balance
        val coins = arrangeCoins()
        //add coins to bills and return
        for (coin in coins) {
            if (matchingBalance >= coin.minorValue){
                val requiredPieces = matchingBalance / coin.minorValue
                val actualPieces = change.getCount(coin)
                val count = if (actualPieces >= requiredPieces) requiredPieces.toInt() else actualPieces
                billChange.add(coin, count)
                matchingBalance -= (coin.minorValue) * count
            }
        }
        return billChange
    }


    class TransactionException(message: String, cause: Throwable? = null) :
        Exception(message, cause)
}
