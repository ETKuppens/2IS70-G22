package com.example.cardhub.map;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cardhub.BuildConfig;
import com.example.cardhub.CardRecyclerViewAdapter;
import com.example.cardhub.PairingModeActivity;
import com.example.cardhub.R;
import com.example.cardhub.collector_navigation.CollectorBaseActivity;
import com.example.cardhub.inventory.Card;
import com.example.cardhub.inventory.InventoryActivity;
import com.example.cardhub.user_profile.ProfileActivity;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.PlaceLikelihood;
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest;
import com.google.android.libraries.places.api.net.FindCurrentPlaceResponse;
import com.google.android.libraries.places.api.net.PlacesClient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MapActivity extends CollectorBaseActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    // Navigation part
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.action_inventory) {
            startActivity(new Intent(this, InventoryActivity.class));
        } else if (itemId == R.id.action_map) {
            startActivity(new Intent(this, MapActivity.class));
        } else if (itemId == R.id.action_trading) {
            startActivity(new Intent(this, PairingModeActivity.class));
        } else if (itemId == R.id.action_profile) {
            startActivity(new Intent(this, ProfileActivity.class));
        }
        return true;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_map_l;
    }

    @Override
    public int getBottomNavigationMenuItemId() {
        return R.id.action_map;
    }


    // Map part
    LatLng onMarkerClickLatLng = null;

    public List<LatLng> geoMarkersLocations = new ArrayList<LatLng>();

    private static final String TAG = MapActivity.class.getSimpleName();
    private GoogleMap map;
    private CameraPosition cameraPosition;

    // The entry point to the Places API.
    private PlacesClient placesClient;

    // The entry point to the Fused Location Provider.
    private FusedLocationProviderClient fusedLocationProviderClient;

    // A default location (Sydney, Australia) and default zoom to use when location permission is
    // not granted.
    private final LatLng defaultLocation = new LatLng(51.447782, 5.485958);
    private static final int DEFAULT_ZOOM = 15;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private boolean locationPermissionGranted = false;

    // The geographical location where the device is currently located. That is, the last-known
    // location retrieved by the Fused Location Provider.
    private Location lastKnownLocation;

    // Keys for storing activity state.
    private static final String KEY_CAMERA_POSITION = "camera_position";
    private static final String KEY_LOCATION = "location";

    // Used for selecting the current place.
    private static final int M_MAX_ENTRIES = 5;
    private String[] likelyPlaceNames;
    private String[] likelyPlaceAddresses;
    private List[] likelyPlaceAttributions;
    private LatLng[] likelyPlaceLatLngs;

    private View cardBanner;

    PopupWindow cardpackPreviewWindow = null;

    MapState state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupNav();

        state = new MapState(this);

        state.requestPacks();

        // Retrieve location and camera position from saved instance state.
        if (savedInstanceState != null) {
            lastKnownLocation = savedInstanceState.getParcelable(KEY_LOCATION);
            cameraPosition = savedInstanceState.getParcelable(KEY_CAMERA_POSITION);
        }


        // Construct a PlacesClient
        Places.initialize(getApplicationContext(), BuildConfig.MAPS_API_KEY);
        placesClient = Places.createClient(this);

        // Construct a FusedLocationProviderClient.
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        // Build the map.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    /**
     * Saves the state of the map when the activity is paused.
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (map != null) {
            outState.putParcelable(KEY_CAMERA_POSITION, map.getCameraPosition());
            outState.putParcelable(KEY_LOCATION, lastKnownLocation);
        }
        super.onSaveInstanceState(outState);
    }

    /**
     * Manipulates the map when it's available.
     * This callback is triggered when the map is ready to be used.
     */
    @Override
    public void onMapReady(GoogleMap map) {
        this.map = map;
        // Use a custom info window adapter to handle multiple lines of text in the
        // info window contents.
        this.map.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

            @Override
            // Return null here, so that getInfoContents() is called next.
            public View getInfoWindow(Marker arg0) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {
                // Inflate the layouts for the info window, title and snippet.
                View infoWindow = getLayoutInflater().inflate(R.layout.custom_info_contents,
                        (FrameLayout) findViewById(R.id.map), false);

                TextView title = infoWindow.findViewById(R.id.title);
                title.setText(marker.getTitle());

                TextView snippet = infoWindow.findViewById(R.id.snippet);
                snippet.setText(marker.getSnippet());

                // NO CUSTOM INFO

                return null;
            }
        });
        this.map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(@NonNull LatLng latLng) {
                hideCardBannerIfActive();
            }
        });

        // Prompt the user for permission.
        getLocationPermission();

        // Turn on the My Location layer and the related control on the map.
        updateLocationUI();

        // Get the current location of the device and set the position of the map.
        getDeviceLocation();
    }

    public void addPointsOnTheMap() {
        if (!locationPermissionGranted) {
            return;
        }

        List<CardPack> cardPacks = state.packs;
        for (int i = 0; i < cardPacks.size(); i++) {
            map.addMarker(new MarkerOptions()
                    .position(cardPacks.get(i).position)).setTag(i);
            // Circle the area around geotag
            Integer geotagRingFill = Color.argb(100, 0, 0, 100);
            Integer geotagRingStroke = Color.argb(100, 200, 0, 0);
            CircleOptions circleOptions = new CircleOptions()
                    .center(cardPacks.get(i).position)
                    .radius(30) // in meters
                    .strokeWidth(2)
                    .strokeColor(geotagRingStroke)
                    .fillColor(geotagRingFill);

            Circle circle = map.addCircle(circleOptions);
        }
        map.setOnMarkerClickListener(this);
    }

    /**
     * Gets the current location of the device, and positions the map's camera.
     */
    /** Called when the user clicks a marker. */
    @Override
    public boolean onMarkerClick(final Marker marker) {

        onMarkerClickLatLng = marker.getPosition();

        hideCardBannerIfActive();

        getLayoutInflater().inflate(R.layout.card_banner, findViewById(R.id.root), true);
        cardBanner = findViewById(R.id.card_banner);


        ImageView imageView = cardBanner.findViewById(R.id.card_image);
        TextView titleView = cardBanner.findViewById(R.id.card_title);
        TextView descriptionView = cardBanner.findViewById(R.id.card_description);

        CardPack pack = state.packs.get((Integer)marker.getTag());

        switch (pack.rarity) {
            case COMMON:
                imageView.setImageResource(R.drawable.common_pack);
                break;
            case RARE:
                imageView.setImageResource(R.drawable.rare_pack);
                break;
            case LEGENDARY:
                imageView.setImageResource(R.drawable.legendary_pack);
                break;
            case ULTRA_RARE:
                imageView.setImageResource(R.drawable.ultra_rare_pack);
                break;
        }

        titleView.setText(state.packs.get((Integer)marker.getTag()).name);
        descriptionView.setText(state.packs.get((Integer)marker.getTag()).description);

        Button collectCardButton = findViewById(R.id.buttonCollectCard);
        collectCardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                destroyCardpackPreviewWindow();
                buttonCollectCardPackClicked(pack.rarity);
            }
        });

        // Return false to indicate that we have not consumed the event and that we wish
        // for the default behavior to occur (which is for the camera to move such that the
        // marker is centered and for the marker's info window to open, if it has one).
        return false;
    }


    private void getDeviceLocation() {
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        try {
            if (locationPermissionGranted) {
                Task<Location> locationResult = fusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(this, new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful()) {
                            // Set the map's camera position to the current location of the device.
                            lastKnownLocation = task.getResult();
                            if (lastKnownLocation != null) {
                                map.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                        new LatLng(lastKnownLocation.getLatitude(),
                                                lastKnownLocation.getLongitude()), DEFAULT_ZOOM));
                            }
                        } else {
                            Log.d(TAG, "Current location is null. Using defaults.");
                            Log.e(TAG, "Exception: %s", task.getException());
                            map.moveCamera(CameraUpdateFactory
                                    .newLatLngZoom(defaultLocation, DEFAULT_ZOOM));
                            map.getUiSettings().setMyLocationButtonEnabled(false);
                        }
                    }
                });
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage(), e);
        }
    }

    /**
     * Prompts the user for permission to use the device location.
     */
    private void getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            locationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
            locationPermissionGranted = false;
        }
    }

    /**
     * Handles the result of the request for location permissions.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        locationPermissionGranted = false;
        if (requestCode
                == PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION) {// If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                locationPermissionGranted = true;
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
        updateLocationUI();
    }

    /**
     * Prompts the user to select the current place from a list of likely places, and shows the
     * current place on the map - provided the user has granted location permission.
     */
    private void showCurrentPlace() {
        if (map == null) {
            return;
        }

        if (locationPermissionGranted) {
            // Use fields to define the data types to return.
            List<Place.Field> placeFields = Arrays.asList(Place.Field.NAME, Place.Field.ADDRESS,
                    Place.Field.LAT_LNG);

            // Use the builder to create a FindCurrentPlaceRequest.
            FindCurrentPlaceRequest request =
                    FindCurrentPlaceRequest.newInstance(placeFields);

            // Get the likely places - that is, the businesses and other points of interest that
            // are the best match for the device's current location.
            @SuppressWarnings("MissingPermission") final
            Task<FindCurrentPlaceResponse> placeResult =
                    placesClient.findCurrentPlace(request);
            placeResult.addOnCompleteListener (new OnCompleteListener<FindCurrentPlaceResponse>() {
                @Override
                public void onComplete(@NonNull Task<FindCurrentPlaceResponse> task) {
                    if (task.isSuccessful() && task.getResult() != null) {
                        FindCurrentPlaceResponse likelyPlaces = task.getResult();

                        // Set the count, handling cases where less than 5 entries are returned.
                        int count;
                        if (likelyPlaces.getPlaceLikelihoods().size() < M_MAX_ENTRIES) {
                            count = likelyPlaces.getPlaceLikelihoods().size();
                        } else {
                            count = M_MAX_ENTRIES;
                        }

                        int i = 0;
                        likelyPlaceNames = new String[count];
                        likelyPlaceAddresses = new String[count];
                        likelyPlaceAttributions = new List[count];
                        likelyPlaceLatLngs = new LatLng[count];

                        for (PlaceLikelihood placeLikelihood : likelyPlaces.getPlaceLikelihoods()) {
                            // Build a list of likely places to show the user.
                            likelyPlaceNames[i] = placeLikelihood.getPlace().getName();
                            likelyPlaceAddresses[i] = placeLikelihood.getPlace().getAddress();
                            likelyPlaceAttributions[i] = placeLikelihood.getPlace()
                                    .getAttributions();
                            likelyPlaceLatLngs[i] = placeLikelihood.getPlace().getLatLng();

                            i++;
                            if (i > (count - 1)) {
                                break;
                            }
                        }

                        // Show a dialog offering the user the list of likely places, and add a
                        // marker at the selected place.
                        MapActivity.this.openPlacesDialog();
                    }
                    else {
                        Log.e(TAG, "Exception: %s", task.getException());
                    }
                }
            });
        } else {
            // The user has not granted permission.
            Log.i(TAG, "The user did not grant location permission.");

            // Add a default marker, because the user hasn't selected a place.
            map.addMarker(new MarkerOptions()
                    .title(getString(R.string.default_info_title))
                    .position(defaultLocation)
                    .snippet(getString(R.string.default_info_snippet)));

            // Prompt the user for permission.
            getLocationPermission();
        }
    }

    /**
     * Displays a form allowing the user to select a place from a list of likely places.
     */
    private void openPlacesDialog() {
        // Ask the user to choose the place where they are now.
        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // The "which" argument contains the position of the selected item.
                LatLng markerLatLng = likelyPlaceLatLngs[which];
                String markerSnippet = likelyPlaceAddresses[which];
                if (likelyPlaceAttributions[which] != null) {
                    markerSnippet = markerSnippet + "\n" + likelyPlaceAttributions[which];
                }

                // Add a marker for the selected place, with an info window
                // showing information about that place.
                map.addMarker(new MarkerOptions()
                        .title(likelyPlaceNames[which])
                        .position(markerLatLng)
                        .snippet(markerSnippet));

                // Position the map's camera at the location of the marker.
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(markerLatLng,
                        DEFAULT_ZOOM));
            }
        };

        // Display the dialog.
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle(R.string.pick_place)
                .setItems(likelyPlaceNames, listener)
                .show();
    }

    /**
     * Updates the map's UI settings based on whether the user has granted location permission.
     */
    private void updateLocationUI() {
        if (map == null) {
            return;
        }
        try {
            if (locationPermissionGranted) {
                map.setMyLocationEnabled(true);
                map.getUiSettings().setMyLocationButtonEnabled(true);
            } else {
                map.setMyLocationEnabled(false);
                map.getUiSettings().setMyLocationButtonEnabled(false);
                lastKnownLocation = null;
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    public void onCompassClicked(View view) {
        float currentBearing = map.getCameraPosition().bearing;
        map.animateCamera(CameraUpdateFactory.newCameraPosition(
                new CameraPosition.Builder()
                        .target(map.getCameraPosition().target)
                        .zoom(map.getCameraPosition().zoom)
                        .bearing(0)
                        .tilt(map.getCameraPosition().tilt)
                        .build()), 500, null);
    }

    private void hideCardBannerIfActive() {
        if (cardBanner != null) {
            ((ViewGroup)findViewById(R.id.root)).removeView(cardBanner);
        }
    }

    protected void buttonCollectCardPackClicked(Card.Rarity rarity) {
        // Get user location (update lastKnownLocation)
        getDeviceLocation();
        // Get marker location
        LatLng MarkerLatLng = onMarkerClickLatLng;
        // Make sure that the distance is <30m. Distance is calculated in meters
        Double distance =
                distanceBetween(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude(),
                        MarkerLatLng.latitude, MarkerLatLng.longitude);
        Toast.makeText(getApplicationContext(),Double.toString(distance),Toast.LENGTH_SHORT).show();
        // Init card collection
        if (distance <= 30) {
            state.acquireRandomCard(rarity);
        } else {
            String errMessage = String.format("%.2f", distance);
            Toast.makeText(getApplicationContext(), "You cannot collect the card. Distance " +
                    errMessage + " is >30m.",Toast.LENGTH_SHORT).show();
        }
    }

    public static final double AVERAGE_RADIUS_OF_EARTH_KM = 6371;

    public static double distanceBetween(double startLat, double startLng, double endLat, double endLng) {
        double latDistance = Math.toRadians(endLat - startLat);
        double lngDistance = Math.toRadians(endLng - startLng);

        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(startLat)) * Math.cos(Math.toRadians(endLat))
                * Math.sin(lngDistance / 2) * Math.sin(lngDistance / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return AVERAGE_RADIUS_OF_EARTH_KM * c * 1000;
    }

    public void cardsResponse(List<CardPack> packs) {
        Thread waitForMapToBeReadyThread = new Thread (() -> {
            while (map == null) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    // might cause an error if the map is not ready
                    // add pinpoints on the map
                    addPointsOnTheMap();
                }
            });
        });
        waitForMapToBeReadyThread.start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        destroyCardpackPreviewWindow();
    }

    private void destroyCardpackPreviewWindow() {
        if (cardpackPreviewWindow != null) {
            cardpackPreviewWindow.dismiss();
            cardpackPreviewWindow = null;
        }
    }

    public void showCardpackPreviewWindow(List<Card> cardPackCards) {
        if (cardpackPreviewWindow != null || cardPackCards == null) {
            return;
        }

        LayoutInflater inflater = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
        View cardpackPreviewView = inflater.inflate(R.layout.cardpack_preview, null);

        cardpackPreviewWindow = new PopupWindow(cardpackPreviewView,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);

        ImageButton cardpackPreviewCloseButton = (ImageButton)cardpackPreviewView
                .findViewById(R.id.cardpack_preview_close_button);

        cardpackPreviewCloseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                destroyCardpackPreviewWindow();
            }
        });

        RecyclerView cardpackRecyclerView = (RecyclerView)cardpackPreviewView
                .findViewById(R.id.cardpack_preview_cards_recyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this,
                LinearLayoutManager.HORIZONTAL,
                false);
        cardpackRecyclerView.setLayoutManager(layoutManager);
        CardRecyclerViewAdapter adapter = new CardRecyclerViewAdapter(cardpackRecyclerView.getContext(),
                cardPackCards);
        cardpackRecyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        cardpackPreviewWindow.showAtLocation(findViewById(R.id.map), Gravity.CENTER, 0, 0);
    }
}
