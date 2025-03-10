package timisongdev.deliverytasks

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.webkit.*
import android.widget.Toast
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.location.*
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.mapview.MapView

class YandexMap {
    companion object {
        val mapMode = mutableStateOf("map")
        @SuppressLint("SetJavaScriptEnabled")
        @Composable
        fun Mapa() {
            val localContext = LocalContext.current

            val configuration = LocalConfiguration.current
            val screenHeight = configuration.screenHeightDp.dp

            if (mapMode.value == "route") {
                Button(onClick = {
                    mapMode.value = "map"
                }) {
                    Text(
                        "Return to map"
                    )
                }
            }

            if (mapMode.value == "map") {
                AndroidView(
                    factory = {
                        Workspace.getMap(localContext).apply {
                            map.isRotateGesturesEnabled = true
                            map.isTiltGesturesEnabled = true
                            map.isScrollGesturesEnabled = true
                            showUserLocation()
                        }
                    },
                    Modifier
                        .padding(8.dp)
                        .clip(RoundedCornerShape(24.dp))
                        .height(screenHeight)
                )
            } else {
                if (mapMode.value == "route") {
                    AndroidView(
                        factory = { context ->
                            WebView(context).apply {
                                settings.javaScriptEnabled = true
                                webViewClient = WebViewClient()
                                settings.useWideViewPort = true
                                settings.loadWithOverviewMode = true
                                settings.javaScriptCanOpenWindowsAutomatically = true
                                webChromeClient = object : WebChromeClient() {
                                    override fun onGeolocationPermissionsShowPrompt(
                                        origin: String?,
                                        callback: GeolocationPermissions.Callback?
                                    ) {
                                        callback?.invoke(origin, true, false)
                                    }
                                }
                                webViewClient = object : WebViewClient() {
                                    @SuppressLint("QueryPermissionsNeeded")
                                    @Deprecated("Deprecated in Java")
                                    override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                                        url?.let {
                                            val uri = Uri.parse(it)
                                            if (it.startsWith("intent://")) {
                                                try {
                                                    val intent = Intent.parseUri(it, Intent.URI_INTENT_SCHEME)
                                                    val appPackageName = intent.data?.schemeSpecificPart

                                                    val packageManager = view?.context?.packageManager
                                                    val resolveInfo = packageManager?.resolveActivity(intent, PackageManager.MATCH_ALL)

                                                    if (resolveInfo != null) {
                                                        context.startActivity(intent)
                                                    } else {
                                                        val fallbackUrl = intent.getStringExtra("browser_fallback_url")
                                                        if (fallbackUrl != null) {
                                                            view?.loadUrl(fallbackUrl)
                                                        } else {
                                                            try {
                                                                val playStoreIntent = Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$appPackageName"))
                                                                view?.context?.startActivity(playStoreIntent)
                                                            } catch (e: Exception) {
                                                                e.printStackTrace()
                                                            }
                                                        }
                                                    }
                                                    return true
                                                } catch (e: Exception) {
                                                    e.printStackTrace()
                                                }
                                            } else {
                                                try {
                                                    val intent = Intent(Intent.ACTION_VIEW, uri)
                                                    view?.context?.startActivity(intent)
                                                    return true
                                                } catch (e: ActivityNotFoundException) {
                                                    return false
                                                }
                                            }
                                        }
                                        return false
                                    }
                                }
                                if (Working.order == null) {
                                    val url =
                                        "https://yandex.ru/maps/?rtext=${Workspace.startLocation}~55.755814,37.617635&rtt=pd"
                                    loadUrl(url)
                                } else {
                                    val orderString = Working.order
                                    if (orderString != null) {
                                        val (lat, long) = orderString
                                        val url =
                                            "https://yandex.ru/maps/?rtext=${Workspace.startLocation}~$lat,$long&rtt=pd"
                                        loadUrl(url)
                                    }
                                }
                            }
                        },
                        Modifier
                            .padding(8.dp)
                            .clip(RoundedCornerShape(24.dp))
                            .height(screenHeight)
                    )
                } else {
                    Text(
                        "Map. Closed",
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.surface
                    )
                }
            }
        }

        private fun MapView.showUserLocation() {
            if (Workspace.isInit.value) {

                val locationManager: LocationManager = MapKitFactory.getInstance().createLocationManager()

                locationManager.requestSingleUpdate(object : LocationListener {
                    override fun onLocationUpdated(location: com.yandex.mapkit.location.Location) {

                        val userLocation = Point(location.position.latitude, location.position.longitude)

                        val circle = com.yandex.mapkit.geometry.Circle(
                            Point(location.position.latitude, location.position.longitude),
                            20f
                        )

                        map.mapObjects.addCircle(circle).apply {
                            strokeWidth = 2f
                            strokeColor = R.color.teal_200
                            fillColor = R.color.teal_700
                        }

                        map.move(
                            CameraPosition(
                                userLocation,
                                /* zoom = */ 17.0f,
                                /* azimuth = */ 150.0f,
                                /* tilt = */ 30.0f
                            )
                        )
                    }

                    override fun onLocationStatusUpdated(p0: LocationStatus) {
                        Toast.makeText(context, "$p0", Toast.LENGTH_SHORT).show()
                    }
                })
            }
        }
    }
}