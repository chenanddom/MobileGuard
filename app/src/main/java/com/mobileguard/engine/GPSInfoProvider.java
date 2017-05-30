package com.mobileguard.engine;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.v4.app.ActivityCompat;

import com.mobileguard.activitys.MainActivity;

/**
 * 实现定位的功能
 * Created by chendom on 2017/4/11 0011.
 */
public class GPSInfoProvider {
    private static final GPSInfoProvider gpsInfoProvider = new GPSInfoProvider();
    private LocationManager locateManager;
    private Location location;
    private SharedPreferences preferences;

    public static GPSInfoProvider getGPSInfoProvider() {
        return gpsInfoProvider;
    }

    public void initialLocation(Context context) {
        //通过系统服务得到LocationManager
        String contextService = Context.LOCATION_SERVICE;
        locateManager = (LocationManager) context.getSystemService(contextService);
        //根据位置提供器获取的位置
        String provider = locateManager.GPS_PROVIDER;//获取位置提供器
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        location = locateManager.getLastKnownLocation(provider);
        //在标准集合的的使用下，使用最佳的位置提供器，获取位置
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);//高精度
        criteria.setAltitudeRequired(false);//不要求海拔
        criteria.setBearingRequired(false);//不要求方位
        criteria.setCostAllowed(true);//允许有花费
        criteria.setPowerRequirement(Criteria.POWER_LOW);//低功耗
        //从可用的位置提供器中，匹配以上标准的最佳提供器
        String provider2 = locateManager.getBestProvider(criteria, true);
        //获得最后一次变化的位置
        location = locateManager.getLastKnownLocation(provider2);
        preferences = context.getSharedPreferences("appdata", Context.MODE_PRIVATE);
        //监听位置变化，2秒一次，距离10米以上
        locateManager.requestLocationUpdates(provider, 2000, 10, new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                String latitude = "纬度:"+location.getLatitude(); // 纬度
                String longitude = "经度:"+location.getLongitude(); // 经度
                String meter = "精确度:"+location.getAccuracy();// 精确度
                System.out.println(latitude + "-" + longitude + "-" + meter);
                preferences.edit().putString("location", latitude + "-" + longitude + "-" + meter).commit();

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        });
    }

    public String getLocation() {
        return preferences.getString("location", "");
    }
}
