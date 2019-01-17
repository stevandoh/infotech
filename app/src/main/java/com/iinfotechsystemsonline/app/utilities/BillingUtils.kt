

open class BillingUtils {

    companion object {
        @JvmStatic
        fun setAmountToDecimal(amount: String): Double {
            return amount.toDouble()
        }

        @JvmStatic
        fun calculatePermAmount(amount: Double, lines: Int): Double {
            return amount.times(lines)
        }

        @JvmStatic
        fun calculateCommissionAmount(flatFee: Double, commissionPercentage: Double): Double {
            return flatFee.times(commissionPercentage)
        }

        @JvmStatic
        fun calculateTotalAmount(flatFee: Double, commission: Double):Double {
            return flatFee.plus(commission)
        }
    }
}

