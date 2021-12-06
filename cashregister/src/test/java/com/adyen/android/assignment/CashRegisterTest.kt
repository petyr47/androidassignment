package com.adyen.android.assignment

import com.adyen.android.assignment.money.Bill
import com.adyen.android.assignment.money.Change
import com.adyen.android.assignment.money.Coin
import org.junit.Assert.*
import org.junit.Test

class CashRegisterTest {
    private val emptyRegister = CashRegister(Change.none())

    private val fiftyEuro = Change()
        .add(Bill.FIFTY_EURO, 1)

    private val tenEuro = Change()
        .add(Bill.TEN_EURO, 1)

    private val register = CashRegister(fiftyEuro)

        @Test
    fun testNoChangeInRegisterTransaction() {
        assertEquals(emptyRegister.performTransaction(10L, fiftyEuro), Change.none())
    }

    @Test
    fun testLessAmountPaidTransaction() {
        assertThrows(CashRegister.TransactionException::class.java
        ) { register.performTransaction(1000L, tenEuro) }
    }

    @Test
    fun testTransactionWithBills() {
        val amountPaid = Change.none().apply {
            add(Bill.FIVE_HUNDRED_EURO, 2)
            add(Bill.TWO_HUNDRED_EURO, 2)
        }
        val change = Change.none().apply {
            add(Bill.FIVE_HUNDRED_EURO, 4)
            add(Bill.TWO_HUNDRED_EURO, 4)
            add(Bill.FIFTY_EURO, 3)
        }
        val balance = CashRegister(change).performTransaction(1300_00, amountPaid)
        println(balance)
        assertEquals(100_00, balance.total)
    }


    @Test
    fun testTransactionWithBillsAndCoins() {
        val amountPaid = Change.none().apply {
            add(Bill.FIVE_HUNDRED_EURO, 2)
            add(Bill.TWO_HUNDRED_EURO, 2)
        }
        val change = Change.none().apply {
            add(Bill.FIVE_HUNDRED_EURO, 4)
            add(Bill.TWO_HUNDRED_EURO, 4)
            add(Bill.FIFTY_EURO, 5)
            add(Coin.TWO_EURO, 10)
            add(Coin.ONE_EURO, 10)
            add(Coin.FIFTY_CENT, 8)

        }
        val balance = CashRegister(change).performTransaction(1020_50, amountPaid)
        println(balance)
        assertEquals(379_50, balance.total)
    }

}
