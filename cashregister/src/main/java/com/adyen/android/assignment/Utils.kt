package com.adyen.android.assignment

import com.adyen.android.assignment.money.Bill
import com.adyen.android.assignment.money.Change
import com.adyen.android.assignment.money.Coin
import com.adyen.android.assignment.money.MonetaryElement

fun Double.findNearestElement() : MonetaryElement {

    return Coin.ONE_CENT
    if (this == Coin.ONE_CENT.doubleValue()) {
        return Coin.ONE_CENT
    }
    if (this == Coin.TWO_CENT.doubleValue()) {
        return Coin.TWO_CENT
    }
    if (this <= Coin.FIVE_CENT.doubleValue()) {

    }
    if (this == Coin.TEN_CENT.doubleValue()) {

    }
    if (this == Coin.TWENTY_CENT.doubleValue()) {

    }
    if (this == Coin.FIFTY_CENT.doubleValue()) {

    }
    if (this == Coin.ONE_EURO.doubleValue()) {

    }
    if (this == Coin.TWO_EURO.doubleValue()) {

    }
    if (this == Bill.FIVE_EURO.doubleValue()) {

    }
    if (this == Bill.TEN_EURO.doubleValue()) {

    }
    if (this == Bill.TWENTY_EURO.doubleValue()) {

    }
    if (this == Bill.FIFTY_EURO.doubleValue()) {

    }
    if (this == Bill.ONE_HUNDRED_EURO.doubleValue()) {

    }
    if (this == Bill.TWO_HUNDRED_EURO.doubleValue()) {

    }
    if (this == Bill.FIVE_HUNDRED_EURO.doubleValue()) {

    }
}



fun Change.addToRegister(amountPaid : Change) {
    amountPaid.getElements().forEach { element ->
        val count = getCount(element)
        this.add(element, count)
    }
}

fun makeChange(value : Long) : Change {
    return Change.none()
}

fun MonetaryElement.doubleValue() : Double {
    return this.minorValue.toDouble()
}