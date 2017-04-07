package com.example.estudiante.vistaspdam;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.TextView;

import com.example.estudiante.vistaspdam.mapas.AddressResultListener;
import com.example.estudiante.vistaspdam.mapas.AddressResultReceiver;
import com.example.estudiante.vistaspdam.mapas.FetchAddressIntentService;
import com.example.estudiante.vistaspdam.mapas.GMapV2Direction;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.w3c.dom.Document;

import java.util.ArrayList;


public class MapActivity
        extends FragmentActivity
        implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,GoogleMap.OnMarkerClickListener
{

    private static final int ACCESS_LOCATION_PERMISSION_CODE = 10;

    private final LocationRequest locationRequest = new LocationRequest();

    private GoogleMap googleMap;

    private GoogleApiClient googleApiClient;

    private TextView address;

    private LatLng sourcePosition = null;
    private LatLng destPosition = null;
    private boolean ok = false;

    public static boolean hasPermissions( Context context, String[] permissions )
    {
        for ( String permission : permissions )
        {
            if ( ContextCompat.checkSelfPermission( context, permission ) == PackageManager.PERMISSION_DENIED )
            {
                return false;
            }
        }
        return true;
    }

    public static void requestPermissions( Activity activity, String[] permissions, int requestCode )
    {
        ActivityCompat.requestPermissions( activity, permissions, requestCode );
    }

    @Override
    protected void onCreate( Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.mapa );
        address = (TextView) findViewById( R.id.address );
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById( R.id.map );
        mapFragment.getMapAsync( this );

        //Configure Google Maps API Objects
        googleApiClient =
                new GoogleApiClient.Builder( this ).addConnectionCallbacks( this ).addOnConnectionFailedListener(
                        this ).addApi( LocationServices.API ).build();
        locationRequest.setInterval( 10000 );
        locationRequest.setFastestInterval( 5000 );
        locationRequest.setPriority( LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY );
        googleApiClient.connect();

    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady( GoogleMap googleMap )
    {

        this.googleMap = googleMap;
        this.googleMap.setOnMarkerClickListener(this);
        this.googleMap.addMarker(new MarkerOptions().position(new LatLng(4.7460666,-74.0363919)).title("Casa Vega"));
        this.googleMap.addMarker(new MarkerOptions().position(new LatLng(4.6160164,-74.1207628)).title("Casa 1 Tatiana"));
        this.googleMap.addMarker(new MarkerOptions().position(new LatLng(4.6669766,-74.1259004)).title("Casa 2 Tatiana"));

    }

    public void setRoute(View v){

        System.out.println("Destino"+destPosition);
        System.out.println("Origen"+sourcePosition);

        GMapV2Direction md = new GMapV2Direction();

        Document doc = md.getDocument(sourcePosition, destPosition,
                GMapV2Direction.MODE_DRIVING);

        ArrayList<LatLng> directionPoint = md.getDirection(doc);
        PolylineOptions rectLine = new PolylineOptions().width(3).color(
                Color.RED);

        for (int i = 0; i < directionPoint.size(); i++) {
            rectLine.add(directionPoint.get(i));
        }
        Polyline polylin = googleMap.addPolyline(rectLine);

    }



    @SuppressWarnings( "MissingPermission" )
    public void showMyLocation()
    {
        if ( googleMap != null )
        {
            String[] permissions = { android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION };
            if ( hasPermissions( this, permissions ) )
            {
                googleMap.setMyLocationEnabled( true );

                Location lastLocation = LocationServices.FusedLocationApi.getLastLocation( googleApiClient );
                destPosition = new LatLng(lastLocation.getLatitude(),lastLocation.getLongitude());
                System.out.println("sapo");
                if ( lastLocation != null )
                {
                    if(!ok){
                        addMarkerAndZoom( lastLocation, "My Location", 15 );
                        ok = true;
                    }

                }
            }
            else
            {
                requestPermissions( this, permissions, ACCESS_LOCATION_PERMISSION_CODE );
            }
        }
    }

    @Override
    public void onRequestPermissionsResult( int requestCode, @NonNull String[] permissions,
                                            @NonNull int[] grantResults )
    {
        for ( int grantResult : grantResults )
        {
            if ( grantResult == -1 )
            {
                return;
            }
        }
        switch ( requestCode )
        {
            case ACCESS_LOCATION_PERMISSION_CODE:
                showMyLocation();
                break;
            default:
                super.onRequestPermissionsResult( requestCode, permissions, grantResults );
        }
    }

    @Override
    public void onConnected( @Nullable Bundle bundle )
    {
        startLocationUpdates();
    }

    @Override
    public void onConnectionSuspended( int i )
    {
        stopLocationUpdates();
    }

    @Override
    public void onConnectionFailed( @NonNull ConnectionResult connectionResult )
    {
        startLocationUpdates();
    }

    public void stopLocationUpdates()
    {
        LocationServices.FusedLocationApi.removeLocationUpdates( googleApiClient, new LocationListener()
        {
            @Override
            public void onLocationChanged( Location location )
            {

            }
        } );
    }

    @SuppressWarnings( "MissingPermission" )
    public void startLocationUpdates()
    {
        LocationServices.FusedLocationApi.requestLocationUpdates( googleApiClient, locationRequest,
                new LocationListener()
                {
                    @Override
                    public void onLocationChanged( Location location )
                    {
                        showMyLocation();
                        stopLocationUpdates();
                    }
                } );
    }


    public void addMarkerAndZoom( Location location, String title, int zoom )
    {
        LatLng myLocation = new LatLng( location.getLatitude(), location.getLongitude() );
        googleMap.addMarker( new MarkerOptions().position( myLocation ).title( title ) );
        googleMap.moveCamera( CameraUpdateFactory.newLatLngZoom( myLocation, zoom ) );
    }


    public void onFindAddressClicked( View view )
    {
        startFetchAddressIntentService();
    }


    @SuppressWarnings( "MissingPermission" )
    public void startFetchAddressIntentService()
    {

        Location lastLocation = LocationServices.FusedLocationApi.getLastLocation( googleApiClient );
        if ( lastLocation != null )
        {

            AddressResultReceiver addressResultReceiver = new AddressResultReceiver( new Handler() );
            addressResultReceiver.setAddressResultListener( new AddressResultListener()
            {
                @Override
                public void onAddressFound( final String address )
                {
                    runOnUiThread( new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            MapActivity.this.address.setText( address );
                            MapActivity.this.address.setVisibility( View.VISIBLE );
                        }
                    } );


                }
            } );
            Intent intent = new Intent( this, FetchAddressIntentService.class );
            intent.putExtra( FetchAddressIntentService.RECEIVER, addressResultReceiver );
            intent.putExtra( FetchAddressIntentService.LOCATION_DATA_EXTRA, lastLocation );
            startService( intent );
        }
    }



    @Override
    public boolean onMarkerClick(Marker marker) {
        sourcePosition = marker.getPosition();
        return false;
    }
}
