package com.example.finalprojectandroid

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapsFragment : Fragment(),OnMapReadyCallback {
    private var map: GoogleMap? =null;
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val view = inflater.inflate(R.layout.fragment_list, container, false)

        val mapFragment = childFragmentManager.findFragmentById(R.id.map_id) as? SupportMapFragment
        mapFragment?.getMapAsync(this)

        return view
    }


    override fun onMapReady(googlemap: GoogleMap) {
       map = googlemap
        map!!.mapType = GoogleMap.MAP_TYPE_NORMAL

        val lebanonLatLng = LatLng(33.8547, 35.8623)

        map!!.moveCamera(CameraUpdateFactory.newLatLngZoom(lebanonLatLng, 10f))

        val markerLatLng = LatLng(33.8889, 35.4955) // Example: Beirut's latitude and longitude
        map!!.addMarker(MarkerOptions().position(markerLatLng).title("Talia's Catering"))

        // Optionally, enable map UI settings
        map!!.uiSettings.isZoomControlsEnabled = true
        map!!.uiSettings.isMyLocationButtonEnabled = true
    }

}