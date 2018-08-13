package com.hfad.aisdecoder;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;
import android.os.Handler;


/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions and extra parameters.
 */
public class AISDecodingService extends IntentService {
    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private Handler handler;

    public AISDecodingService() {
        super("AISDecodingService");
    }

    public int onStartCommand(Intent intent, int flags, int startId)
    {
        handler = new Handler();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        synchronized (this){
            try{
                wait(10000);
            }catch (InterruptedException e){
                e.printStackTrace();
            }
        }
        try
        {
            SQLiteOpenHelper aisDecoderDatabaseHelper = new AISDecoderDatabaseHelper(this);
            SQLiteDatabase db = aisDecoderDatabaseHelper.getReadableDatabase();
            Cursor cursor_stnlist = db.query("AISSTATIONLIST",
                    new String[] {"MMSI", "AIS_STATION_NAME"},
                    null,
                    null,
                    null, null, null);

            Cursor cursor_fixedstnlist = db.query("AISFIXEDSTATIONPOSITION",
                    null,
                    null,
                    null,
                    null, null, null);

            Cursor cursor_mobilestnlist = db.query("AISMOBILESTATION",
                    null,
                    null,
                    null,
                    null, null, null);

            if(cursor_stnlist.moveToFirst())
            {
                long mmsi = cursor_stnlist.getLong(0);
                String aisStnName = cursor_stnlist.getString(1);

                //Compared with the MMSI and AISStationName received from the WiFi
                //This needs to be implemented in Wifi Service
                //Decoding logic

                AIVDM aivdmObj = new AIVDM();
                POSTNREPORT posObj = new POSTNREPORT();

                String packet = "!AIVDM,1,1,1,B,15UDQt001aPT136NlWiD93E20<<P,0*30"; //will be received from Wifi Service

                String[] dataExtr = packet.split(",");
                aivdmObj.setData(dataExtr);
                StringBuilder binary = aivdmObj.decodePayload();
                aivdmObj.msgTypedecoding(binary, posObj);
                if(posObj.getMMSI() == mmsi){

                        //Writing to the database table AISFIXEDSTATIONPOSITION
                        //More fields to be included
                        ContentValues decodedValues = new ContentValues();
                        decodedValues.put("LATITUDE", posObj.getLatitude());
                        decodedValues.put("LONGITUDE", posObj.getLongitude());
                        db.insert("AISFIXEDSTATIONPOSITION", null,decodedValues);
                }
                else{

                        //Writing to the database table AISMOBILESTATION
                        ContentValues decodedValues = new ContentValues();
                        decodedValues.put("LATITUDE", posObj.getLatitude());
                        decodedValues.put("LONGITUDE", posObj.getLongitude());
                        db.insert("AISMOBILESTATION", null,decodedValues);

                }
            }

            cursor_stnlist.close();
            cursor_fixedstnlist.close();
            cursor_mobilestnlist.close();
            db.close();
        }catch (SQLException e)
        {
            String text = "Database unavailable";
            showText(text);
        }
    }

    private void showText(final String text) {

        handler.post(new Runnable(){
            public void run()
            {
                Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG).show();
            }
        });

    }
}
