package com.example.jiro.awesomemap;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.media.ExifInterface;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.File;

/**
 * Created by jiro on 26.05.17.
 */
public class ImagedMarker {
    File mImage = null;

    Float Latitude, Longitude;

    Bitmap bmp =  null;

    public ImagedMarker(File image){
        mImage = image;
    }

    public MarkerOptions getMarker(){
        if(bmp == null) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            Bitmap bitmap = BitmapFactory.decodeFile(mImage.getAbsolutePath(), options);

            bmp = Bitmap.createScaledBitmap(bitmap, 80, 80, false);

            try {
                ExifInterface exif = new ExifInterface(mImage.getAbsolutePath());
                String LATITUDE = exif.getAttribute(ExifInterface.TAG_GPS_LATITUDE);
                String LATITUDE_REF = exif.getAttribute(ExifInterface.TAG_GPS_LATITUDE_REF);
                String LONGITUDE = exif.getAttribute(ExifInterface.TAG_GPS_LONGITUDE);
                String LONGITUDE_REF = exif.getAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF);

                if((LATITUDE !=null)
                        && (LATITUDE_REF !=null)
                        && (LONGITUDE != null)
                        && (LONGITUDE_REF !=null))
                {

                    if(LATITUDE_REF.equals("N")){
                        Latitude = convertToDegree(LATITUDE);
                    }
                    else{
                        Latitude = 0 - convertToDegree(LATITUDE);
                    }

                    if(LONGITUDE_REF.equals("E")){
                        Longitude = convertToDegree(LONGITUDE);
                    }
                    else{
                        Longitude = 0 - convertToDegree(LONGITUDE);
                    }

                }

            } catch (Exception e){

            }
        }
        if(Longitude == null || Latitude == null){
            Longitude = new Float(0);
            Latitude = new Float(0);
        }

        //TODO: get lat lon from Image
        return new MarkerOptions().position(new LatLng(Latitude,Longitude)).icon(BitmapDescriptorFactory.fromBitmap(bmp));//.anchor(0.5f,0.5f);
    }

    private Float convertToDegree(String stringDMS){
        Float result = null;
        String[] DMS = stringDMS.split(",", 3);

        String[] stringD = DMS[0].split("/", 2);
        Double D0 = new Double(stringD[0]);
        Double D1 = new Double(stringD[1]);
        Double FloatD = D0/D1;

        String[] stringM = DMS[1].split("/", 2);
        Double M0 = new Double(stringM[0]);
        Double M1 = new Double(stringM[1]);
        Double FloatM = M0/M1;

        String[] stringS = DMS[2].split("/", 2);
        Double S0 = new Double(stringS[0]);
        Double S1 = new Double(stringS[1]);
        Double FloatS = S0/S1;

        result = new Float(FloatD + (FloatM/60) + (FloatS/3600));

        return result;


    };
    public boolean isSame(File checkImage) {
        return mImage.getName().equals(checkImage.getName());
    }

}
