package org.kotlang.freelancerfinance.domain.logic

import org.kotlang.freelancerfinance.domain.model.IndianState
import kotlin.math.roundToInt

data class TaxResult(
    val taxableAmount: Double,
    val cgst: Double = 0.0,
    val sgst: Double = 0.0,
    val igst: Double = 0.0,
    val totalAmount: Double
)

object TaxCalculator {
    
    fun calculate(
        amount: Double,
        gstRatePercent: Double, // e.g., 18.0
        supplierState: IndianState,
        placeOfSupply: IndianState
    ): TaxResult {
        
        val taxAmount = amount * (gstRatePercent / 100.0)

        val isInterState = supplierState != placeOfSupply
        
        return if (isInterState) {
            // IGST Case
            TaxResult(
                taxableAmount = amount,
                igst = taxAmount,
                totalAmount = amount + taxAmount
            )
        } else {
            // CGST + SGST Case
            val halfTax = taxAmount / 2.0
            TaxResult(
                taxableAmount = amount,
                cgst = halfTax,
                sgst = halfTax,
                totalAmount = amount + taxAmount
            )
        }
    }
    
    fun Double.roundTwoDecimals(): Double {
        return (this * 100.0).roundToInt() / 100.0
    }
}