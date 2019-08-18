package tw.brad.apps.brad28;
//查詢手機資料庫
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.View;

import java.net.URI;

public class MainActivity extends AppCompatActivity {
    private ContentResolver cr;
    private Uri uriSettings = Settings.System.CONTENT_URI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //使用電話簿權限
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_CALL_LOG) //READ_CALL_LOG電話簿紀錄
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_CALL_LOG},//READ_CONTACTS 電話簿權限
                    123);

        } else {
            // Permission has already been granted
        }

        cr = getContentResolver();

        // content://database/table
        // content://ContactsContract 聯絡人
        // content://CallLog 通話紀錄
        // content://MediaStore 媒體資料
        // content://Settings 設定資料


    }

    //查詢uri的全部資料
    public void test1(View view) {
        // select * from Settings
        Cursor c = cr.query(uriSettings,
                null, null, null, null);

        int colCount = c.getColumnCount();
        while (c.moveToNext()) {
            for (int i = 0; i < colCount; i++) {
                Log.v("brad", c.getColumnName(i) + " => " + c.getString(i));
            }


        }
        c.close();
    }

    public void test2(View view) {
//        Cursor c = cr.query(uriSettings, //cr表
//                null, //位置
//                "name = ?", //查詢條件
//                new String[]{Settings.System.SCREEN_BRIGHTNESS},//String{}[位置]
//                null);//order
//        c.moveToNext();
//        //要抓欄位方式一
//        String value = c.getString(c.getColumnIndexOrThrow("value"));
//        Log.v("brad", "v = " + value);
//
//        //抓欄位方式二用getInt//(1.cr,2.欄位字串)
//        try {
//            int v2 = Settings.System.getInt(cr, Settings.System.SCREEN_BRIGHTNESS);//(1.cr,2.欄位字串)
//            Log.v("brad", "v2 = " + v2);
//        } catch (Exception e) {
//            Log.v("brad", e.toString());
//        }
        Log.v("brad", getSystemSetting(Settings.System.ANDROID_ID)); //抓id
    }
        //自己寫的更好讀取方法
        private String getSystemSetting(String settingName){
            String ret = null;
            Cursor c = cr.query(uriSettings,
                    null,
                    "name = ?",new String[]{settingName},null);
            try {
                ret =  Settings.System.getString(cr, settingName);
            }catch (Exception e){
                Log.v("brad", e.toString());
                //return null;
            }
            c.close();
            return ret;
        }

    public void test3(View view) {
        //   ContactsContract.CommonDataKinds.Phone; //找聯絡人裡面,的電話
//        ContactsContract.CommonDataKinds.Email; //找聯絡人的email
//        ContactsContract.CommonDataKinds.Photo; //找聯絡人的照片
//          ContactsContract.Contacts.CONTENT_URI 全部欄位資料

        // ContactsContract.Contacts._ID => key => phone
        Log.v("brad", getPhoneNumber("2"));//抓取id1的資料,跑到下面程式寫式電話
    }

        //輸入id查詢手機電話號碼
    private String getPhoneNumber(String id){
        // where ContactsContract.CommonDataKinds.Photo.CONTACT_ID+"=?"
        String ret = null;
        Cursor c = cr.query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI, //一開始查表
                null,
                ContactsContract.CommonDataKinds.Photo.CONTACT_ID+"=?", //這個查詢位值參數為多少
                new String[]{id},//這邊id帶進去上面參數理
                null
        );
        int count = c.getCount();
        if (count>0) {
            c.moveToNext();
            int col = c.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER); //抓到欄位式:聯絡人的電話號碼
            ret =  c.getString(col);
        }

        c.close();

        return ret;
    }
    //查詢電話紀錄
    public void test4(View view) {
// CallLog.Calls.CONTENT_URI
        // CallLog.Calls.CACHED_NAME
        // CallLog.Calls.NUMBER
        // CallLog.Calls.TYPE => CallLog.Calls.INCOMING_TYPE, OUTGOING_TYPE, MISSED_TYPE
        // CallLog.Calls.DATE
        // CallLog.Calls.DURATION (second)

        Cursor c = cr.query(CallLog.Calls.CONTENT_URI,
                null,null,null,null);

        while (c.moveToNext()){
            String name = c.getString(c.getColumnIndex(CallLog.Calls.CACHED_NAME));//取得欄位(使用者的名字)
            String number = c.getString(c.getColumnIndex(CallLog.Calls.NUMBER));  //取得欄位(使用者的電話)
            Log.v("brad", name + ":" + number);

        }
        c.close();



    }


    public void test5(View view){
        // MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        // MediaStore.Images.Media.DATA
    }

}
