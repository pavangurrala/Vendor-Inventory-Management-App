package ie.setu.vendorinventorymanagement.ui.screens.maps

import android.content.Context
import android.graphics.BitmapFactory
import android.view.ViewGroup
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.MapView
import com.mapbox.maps.Style
import com.mapbox.maps.plugin.Plugin
import com.mapbox.maps.plugin.annotation.generated.createPointAnnotationManager
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationOptions
import com.mapbox.maps.plugin.annotation.annotations
import ie.setu.vendorinventorymanagement.R
import ie.setu.vendorinventorymanagement.data.models.ProductsModel
import ie.setu.vendorinventorymanagement.navigation.Home
import ie.setu.vendorinventorymanagement.navigation.allDestinations
import ie.setu.vendorinventorymanagement.navigation.listOfHomeTiles
import ie.setu.vendorinventorymanagement.ui.components.general.BottomAppBarProvider
import ie.setu.vendorinventorymanagement.ui.components.general.TopAppBarProvider
import ie.setu.vendorinventorymanagement.ui.screens.productmanagement.ProductManagementViewModel
import ie.setu.vendorinventorymanagement.firebase.services.Product

@Composable
fun ProductMapScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    productviewModel: ProductManagementViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val products = productviewModel.uiProducts.collectAsState().value

    val currentNavBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = currentNavBackStackEntry?.destination
    val currentBottomScreen = allDestinations.find { it.route == currentDestination?.route } ?: Home
    val currentTileScreen = listOfHomeTiles.find { it.route == currentDestination?.route } ?: Home

    var mapView: MapView? by remember { mutableStateOf(null) }

    Scaffold(
        topBar = {
            TopAppBarProvider(
                currentScreen = currentTileScreen,
                canNavigateBack = navController.previousBackStackEntry != null
            ) {
                navController.navigateUp()
            }
        },
        bottomBar = {
            BottomAppBarProvider(
                navController = navController,
                currentScreen = currentBottomScreen,
            )
        }
    ) { padding ->
        AndroidView(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            factory = { ctx ->
                MapView(ctx).apply {
                    layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                }.also {
                    mapView = it
                }
            }
        )

        // Load the style asynchronously using LaunchedEffect
        LaunchedEffect(mapView, products) {
            mapView?.mapboxMap?.loadStyle(Style.MAPBOX_STREETS) {
                mapView?.mapboxMap?.setCamera(
                    CameraOptions.Builder()
                        .center(Point.fromLngLat(-7.6921, 53.1424)) // Longitude, Latitude
                        .zoom(6.5) // Adjust zoom level as needed
                        .build()
                )
                addProductMarkers(context, mapView!!, products)
            }
        }
    }
}


private fun addProductMarkers(
    context: Context,
    mapView: MapView,
    products: List<ProductsModel>
) {
    val annotationApi = mapView.annotations
    val pointAnnotationManager = annotationApi.createPointAnnotationManager()

    val icon = BitmapFactory.decodeResource(
        context.resources,
        R.drawable.custom_marker
    )

    products.forEach { product ->
        if (product.latitude != 0.0 && product.longitude != 0.0) {
            val point = Point.fromLngLat(product.longitude, product.latitude)

            val pointAnnotationOptions = PointAnnotationOptions()
                .withPoint(point)
                .withIconImage(icon)
                .withTextField(product.productName)
                .withTextOffset(listOf(0.0, -1.5))

            pointAnnotationManager.create(pointAnnotationOptions)
        }
    }
}
