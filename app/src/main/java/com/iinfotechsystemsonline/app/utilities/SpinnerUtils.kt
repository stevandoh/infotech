

import java.util.*

class SpinnerUtils {
    companion object {

        @JvmStatic
        public fun getNetwork(): Map<String, String> {
            val networkDict = TreeMap<String, String>()
            networkDict[Constants.NETWORK_AIRTEL] = Constants.KEY_NETWORK_AIRTEL
            networkDict[Constants.NETWORK_MTN] = Constants.KEY_NETWORK_MTN
            networkDict[Constants.NETWORK_TIGO] = Constants.KEY_NETWORK_TIGO
            networkDict[Constants.NETWORK_VODAFONE] = Constants.KEY_NETWORK_VODAFONE
            return networkDict
        }
    }
}