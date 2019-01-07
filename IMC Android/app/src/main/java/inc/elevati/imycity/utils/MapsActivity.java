package inc.elevati.imycity.utils;

import androidx.fragment.app.FragmentActivity;
import inc.elevati.imycity.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private static final LatLng dummyPosition = new LatLng(45.646457, 9.607563);
    private Button bnSelect;
    private LatLng position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        position = getIntent().getParcelableExtra("position");
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_fragment);
        mapFragment.getMapAsync(this);
        bnSelect = findViewById(R.id.bn_position);
        bnSelect.setEnabled(false);
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        bnSelect.setEnabled(true);
        EspressoIdlingResource.decrement();
        if (position == null)
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(dummyPosition, 17f));
        else
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 17f));

        bnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EspressoIdlingResource.increment();
                position = googleMap.getCameraPosition().target;
                Intent result = new Intent();
                result.putExtra("position", position);
                setResult(Activity.RESULT_OK, result);
                finish();
            }
        });
    }
}
