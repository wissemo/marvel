import com.miled.marvel.BuildConfig
import com.miled.marvel.utils.Constants
import java.math.BigInteger
import java.net.InetAddress
import java.security.MessageDigest

/**
 *  This method actually checks if device is connected to internet
 *  (There is a possibility it's connected to a network but not to internet).
 *  @author Miled
 */
fun isInternetAvailable(): Boolean {
    return try {
        val ipAddress: InetAddress = InetAddress.getByName("google.com")
        !ipAddress.equals("")
    } catch (e: Exception) {
        false
    }
}

/**
 * generate hash code for the api call
 * MD5 algorithm
 * @author Miled
 * @return String
 */
fun generateHash(
    timeStump: Long
): String {
    val keyToHash =
        "${timeStump}${BuildConfig.API_PRIVATE_KEY}${BuildConfig.API_PUBLIC_KEY}"
    val md = MessageDigest.getInstance("MD5")
    return BigInteger(1, md.digest(keyToHash.toByteArray())).toString(16).padStart(32, '0')
}

fun gridReachedEnd(firstVisibleIndex: Int, screenMaxDisplay: Int, listComicsSize: Int): Boolean =
    firstVisibleIndex >= listComicsSize.div(Constants.CELL_COUNT) - screenMaxDisplay