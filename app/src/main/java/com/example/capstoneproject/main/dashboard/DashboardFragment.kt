package com.example.capstoneproject.main.dashboard

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import androidx.fragment.app.Fragment
import android.os.Bundle
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.text.Editable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.lifecycleScope
import com.airbnb.lottie.LottieAnimationView
import com.example.capstoneproject.R
import com.example.capstoneproject.databinding.FragmentDashboardBinding
import com.example.capstoneproject.utils.customSpinner.CustomSpinner
import com.example.capstoneproject.viewmodel.ClusterViewModel
import com.example.capstoneproject.viewmodel.DisasterViewModel
import com.example.capstoneproject.viewmodel.MarkerViewModel
import com.example.capstoneproject.viewmodel.UserViewModel
import com.example.core.domain.model.Disaster
import com.example.core.domain.model.Marker
import com.example.core.domain.model.User
import com.example.core.presentation.SpinnerDisasterAdapter
import com.example.core.valueobject.Status
import com.google.android.gms.location.*
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.coroutines.*
import org.koin.android.viewmodel.ext.android.viewModel
import java.io.IOException
import java.util.*

class DashboardFragment : Fragment(), OnMapReadyCallback, CustomSpinner.OnSpinnerEventsListener {
    private lateinit var binding: FragmentDashboardBinding
    private lateinit var googleMap: GoogleMap
    private lateinit var locationClient: FusedLocationProviderClient
    private lateinit var bottomSheetView: View
    private lateinit var user : User
    private var latitudeCurrent: Double = 0.00
    private var longitudeCurrent: Double = 0.00
    private val userViewModel: UserViewModel by viewModel()
    private val markerViewModel: MarkerViewModel by viewModel()
    private val clusterViewModel: ClusterViewModel by viewModel()
    private val disasterViewModel: DisasterViewModel by viewModel()

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        binding = FragmentDashboardBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (ContextCompat.checkSelfPermission(requireActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION) !=
            PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                ActivityCompat.requestPermissions(requireActivity(),
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), FINE_LOCATION_ACCESS_REQUEST_CODE)
            } else {
                ActivityCompat.requestPermissions(requireActivity(),arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), FINE_LOCATION_ACCESS_REQUEST_CODE)
            }
        }
        setCurrentUser()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        locationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        binding.mapView.onCreate(savedInstanceState)
        binding.mapView.onResume()
        binding.mapView.getMapAsync(this)

        lifecycleScope.launch {
            setReportButton()
        }
    }

    override fun onMapReady(map: GoogleMap?) {
        googleMap = map!!
        getLastLocation()
        setClusters()
        setSearchView()
    }

    private fun setCurrentUser(){
        userViewModel.getUserReference().observe(requireActivity(), {
            if(it != null) {
                user = it
            }
        })
    }

    private fun addMarker(latLng: LatLng, title: String) {
        val marker = MarkerOptions()
        marker.icon(bitmapDescriptorFromVector(requireActivity(),R.drawable.ic_baseline_location_on_24))
        marker.position(latLng)
        marker.title(title)
        googleMap.addMarker(marker)
    }

    private fun addCircle(latLng: LatLng, radius: Double) {
        val circleOptions = CircleOptions()
        circleOptions.center(latLng)
        circleOptions.radius(radius)
        circleOptions.fillColor(R.color.placeholder_text_color)
        circleOptions.strokeWidth(4f)
        googleMap.addCircle(circleOptions)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == FINE_LOCATION_ACCESS_REQUEST_CODE) {
            when (grantResults[0]) {
                PackageManager.PERMISSION_GRANTED -> getLastLocation()
            }
        }
    }

    private fun getLastLocation() {
        if (ActivityCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), arrayOf("Manifest.permission.ACCESS_FINE_LOCATION"), FINE_LOCATION_ACCESS_REQUEST_CODE)
            return
        }
        googleMap.isMyLocationEnabled = true
        val locationManager = requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val locationListener = object : android.location.LocationListener{
            override fun onLocationChanged(location: Location) {
                val latitute = location.latitude
                val longitute = location.longitude
                latitudeCurrent = location.latitude
                longitudeCurrent = location.longitude
                Log.i("test", "Latitute: $latitute ; Longitute: $longitute")
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(latitute, longitute), 15F))
                addMarker(LatLng(latitute, longitute), requireActivity().resources.getString(R.string.title_your_location))
                addCircle(LatLng(latitute, longitute), GEOFENCE_RADIUS)
            }

            override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
            }
        }
        val providers = locationManager.getProviders(true)
        for (provider in providers) {
            locationManager.requestLocationUpdates(provider, 10000, 0F, locationListener)
        }
    }

    private fun setClusters(){
        clusterViewModel.getAllClusters().observe(requireActivity(), { clusters ->
            if (clusters != null && clusters.status == Status.SUCCESS) {
                if (clusters.data != null) {
                    val listClusters = clusters.data!!
                    for (cluster in listClusters) {
                        val radiusInMeters = cluster.radius * 111000
                        Log.i("testAllClusters","Latitute: $cluster.latitude ; Longitute: $cluster.longitude")
                        addMarker(
                            LatLng(cluster.latitude, cluster.longitude),
                            "Latitute: ${cluster.latitude} ; Longitute: ${cluster.longitude}"
                        )
                        addCircle(LatLng(cluster.latitude, cluster.longitude), radiusInMeters)
                    }
                }
            }
        })
    }

    private fun setSearchView() {
        binding.svLocation.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                val location = binding.svLocation.query.toString()
                var listAddress: MutableList<Address> = mutableListOf()

                if (location != null || !location.equals("")) {
                    val geoCoder = Geocoder(requireActivity())
                    try {
                        listAddress = geoCoder.getFromLocationName(location, 1)
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                    val address = listAddress[0]
                    val latLng = LatLng(address.latitude, address.longitude)
                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15F))
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }

        })
    }

    private suspend fun setReportButton() {
        binding.btnReport.setOnClickListener {
            val listDisaster = setContentDisasterSpinner()

            val sheetDialog = BottomSheetDialog(requireContext(), R.style.BottomSheetDialogTheme)
            bottomSheetView = LayoutInflater.from(requireContext()).inflate(R.layout.item_modal_panic_button, null)
            bottomSheetView.findViewById<ImageButton>(R.id.imgBtn_mic).setOnClickListener{
                askSpeechInput()
            }
            bottomSheetView.findViewById<Button>(R.id.btn_send).setOnClickListener {
                val selectedDisaster = bottomSheetView.findViewById<CustomSpinner>(R.id.spinner_disaster).selectedItem
                val idDisaster = listDisaster[selectedDisaster.toString().toInt()].id
                lifecycleScope.launch {
                    insertMarker(idDisaster)
                    sheetDialog.dismiss()
                }
            }
            sheetDialog.setContentView(bottomSheetView)
            sheetDialog.show()
        }
    }

    private fun setContentDisasterSpinner() : List<Disaster> {
        val list = arrayListOf<Disaster>()
        disasterViewModel.getAllDisasters().observe(requireActivity(), { disaster ->
            if (disaster != null && disaster.status == Status.SUCCESS) {
                if (disaster.data != null) {
                    list.addAll(disaster.data!!)
                    val adapter = SpinnerDisasterAdapter(requireActivity(), disaster.data!!)
                    bottomSheetView.findViewById<CustomSpinner>(R.id.spinner_disaster).adapter = adapter
                }
            }
        })
        return list
    }

    //Custom Spinner
    override fun onPopupWindowOpened(spinner: Spinner?) {
        bottomSheetView.findViewById<CustomSpinner>(R.id.spinner_disaster).background = ResourcesCompat.getDrawable(resources, R.drawable.spinner_bg_up, null)
    }

    override fun onPopupWindowClosed(spinner: Spinner?) {
        bottomSheetView.findViewById<CustomSpinner>(R.id.spinner_disaster).background = ResourcesCompat.getDrawable(resources, R.drawable.spinner_bg, null)
    }

    private fun askSpeechInput(){
        if(!SpeechRecognizer.isRecognitionAvailable(requireActivity())){
            Toast.makeText(requireActivity(), requireActivity().resources.getString(R.string.speech_recognation_not_available), Toast.LENGTH_SHORT).show()
        }
        else{
            val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
            intent.putExtra(RecognizerIntent.EXTRA_PROMPT, requireActivity().resources.getString(R.string.say_something_string))
            startActivityForResult(intent, RQ_SPEECH_REC)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == RQ_SPEECH_REC && resultCode == Activity.RESULT_OK) {
            val result = data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            bottomSheetView.findViewById<EditText>(R.id.edit_description).text = Editable.Factory.getInstance().newEditable(result?.get(0).toString())
        }
    }

    private suspend fun insertMarker(idDisaster: String){
        val descriptionDisaster = bottomSheetView.findViewById<EditText>(R.id.edit_description).text.toString()
        val marker =  Marker(
                userId = user.userId,
                disasterId = idDisaster,
                latitude = latitudeCurrent,
                longitude = longitudeCurrent,
                message = descriptionDisaster,
                voiceMessagePath = descriptionDisaster,
        )

        markerViewModel.insertMarker(marker).observe(requireActivity(),{ marker ->
            if (marker != null) {
                when (marker.status) {
                    Status.SUCCESS -> if (marker.data != null && marker.data!!.latitude.toString() != "") {
                        showFeedbackDialog(true, resources.getString(R.string.send_information_success))
                    }
                    Status.LOADING -> showFeedbackDialog(true, null)
                    Status.ERROR -> {
                        showFeedbackDialog(false, resources.getString(R.string.send_information_failed))
                    }
                }
            }
        })
    }

    private fun showFeedbackDialog(isSuccess : Boolean, msg : String?){
        val dialog = Dialog(requireActivity())
        dialog.setContentView(R.layout.feedback_dialog)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        if(msg == null){
            dialog.findViewById<LottieAnimationView>(R.id.success_logo).visibility = View.INVISIBLE
            dialog.findViewById<LottieAnimationView>(R.id.failed_logo).visibility = View.INVISIBLE
            dialog.findViewById<TextView>(R.id.tv_subtitle_dialog).visibility = View.INVISIBLE
            dialog.findViewById<TextView>(R.id.tv_title_dialog).text = resources.getString(R.string.loading_striing)
        }
        else{
            if(isSuccess){
                dialog.findViewById<LottieAnimationView>(R.id.success_logo).visibility = View.VISIBLE
                dialog.findViewById<LottieAnimationView>(R.id.failed_logo).visibility = View.INVISIBLE
                dialog.findViewById<TextView>(R.id.tv_title_dialog).text = resources.getString(R.string.success_string)
            }
            else{
                dialog.findViewById<LottieAnimationView>(R.id.success_logo).visibility = View.INVISIBLE
                dialog.findViewById<LottieAnimationView>(R.id.failed_logo).visibility = View.VISIBLE
                dialog.findViewById<TextView>(R.id.tv_title_dialog).text = resources.getString(R.string.failed_string)
            }
            dialog.findViewById<TextView>(R.id.tv_subtitle_dialog).text = msg
        }

        dialog.findViewById<Button>(R.id.btn_okay).setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun bitmapDescriptorFromVector(
        context: Context,
        vectorResId: Int
    ): BitmapDescriptor? {
        val vectorDrawable = ContextCompat.getDrawable(context, vectorResId)
        vectorDrawable!!.setBounds(
            0,
            0,
            vectorDrawable.intrinsicWidth,
            vectorDrawable.intrinsicHeight
        )
        val bitmap = Bitmap.createBitmap(
            vectorDrawable.intrinsicWidth,
            vectorDrawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        vectorDrawable.draw(canvas)
        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }

    companion object {
        private const val FINE_LOCATION_ACCESS_REQUEST_CODE = 10001
        private const val GEOFENCE_RADIUS = 200.00
        private const val RQ_SPEECH_REC = 102
    }
}