package com.xtremelabs.robolectric.shadows;

import android.location.Location;
import com.xtremelabs.robolectric.WithTestDefaultsRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

@RunWith(WithTestDefaultsRunner.class)
public class LocationTest {

    @Test
    public void testDistanceBetween_preventNPE() {
        Location.distanceBetween(1.0, 1.0, 1.0, 1.0, new float[2]);
    }

    @Test
    public void gettersAndSetters_shouldWork() {
        Location l = new Location("gps");
        l.setLatitude(1.0);
        l.setLongitude(2.0);
        l.setAltitude(3.0);
        l.setAccuracy(4.0f);
        l.setBearing(5.0f);
        l.setSpeed(6.0f);
        assertEquals(1.0, l.getLatitude(), 0.0);
        assertEquals(2.0, l.getLongitude(), 0.0);
        assertEquals(3.0, l.getAltitude(), 0.0);
        assertEquals(4.0f, l.getAccuracy(), 0.0);
        assertEquals(5.0f, l.getBearing(), 0.0);
        assertEquals(6.0f, l.getSpeed(), 0.0);
    }


}
