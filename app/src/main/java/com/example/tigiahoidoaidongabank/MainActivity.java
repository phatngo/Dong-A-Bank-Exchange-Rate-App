package com.example.tigiahoidoaidongabank;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.example.model.TiGia;
import com.example.model.TiGiaAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
ArrayList<TiGia>listTiGia;
TiGiaAdapter tiGiaAdapter;
ListView lvTiGia;
ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addControls();
    }

    private void addControls() {
        listTiGia=new ArrayList<>();
        tiGiaAdapter=new TiGiaAdapter(MainActivity.this,R.layout.item,listTiGia);
        lvTiGia=findViewById(R.id.lvTiGia);
        lvTiGia.setAdapter(tiGiaAdapter);
        progressDialog=new ProgressDialog(MainActivity.this);
        progressDialog.setTitle("Thông Báo");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMessage("Đang Xử Lý...");
        TiGiaTask tiGiaTask=new TiGiaTask();
        tiGiaTask.execute();
    }
    class TiGiaTask extends AsyncTask<Void,Void,ArrayList<TiGia>>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            tiGiaAdapter.clear();
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(ArrayList<TiGia> tiGias) {
            super.onPostExecute(tiGias);
            tiGiaAdapter.clear();
            tiGiaAdapter.addAll(tiGias);
            progressDialog.dismiss();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected ArrayList<TiGia> doInBackground(Void... voids) {
            ArrayList<TiGia>dsTiGia=new ArrayList<>();
            try{
                URL url=new URL("https://www.dongabank.com.vn/exchange/export");
                HttpURLConnection connection= (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setRequestProperty("Content-type", "application/json; charset=utf-8");
                connection.setRequestProperty("User-Agent","Mozilla/5.0 ( compatible ) ");
                connection.setRequestProperty("Accept", "*/*");       //lấy dữ liệu mà server trả về
                InputStream is= connection.getInputStream();
                InputStreamReader isr=new InputStreamReader(is,"UTF-8");
                BufferedReader br=new BufferedReader(isr);
                String line=br.readLine();
                StringBuilder builder=new StringBuilder();
                while (line!=null){
                    builder.append(line);
                    line=br.readLine();}
                String json=builder.toString();
                json=json.replace("(", "");
                json=json.replace(")","");

                //Tách 1 tập các JSON ra thành các JSON Object
                JSONObject jsonObject=new JSONObject(json);
                JSONArray jsonArray= jsonObject.getJSONArray("items");
                for (int i = 0; i<jsonArray.length(); i++) {
                    JSONObject item = jsonArray.getJSONObject(i);
                    TiGia tiGia = new TiGia();
                    if (item.has("type"))
                        tiGia.setType(item.getString("type"));
                    if (item.has("imageurl")) {
                        tiGia.setImageurl(item.getString("imageurl"));
                        url=new URL(tiGia.getImageurl());
                        connection= (HttpURLConnection) url.openConnection();
                        connection.setRequestMethod("GET");
                        connection.setRequestProperty("User-Agent","Mozilla/5.0 ( compatible ) ");
                        connection.setRequestProperty("Accept", "*/*");
                        Bitmap bitmap = BitmapFactory.decodeStream(connection.getInputStream());
                        tiGia.setBitmap(bitmap);
                    }
                    if (item.has("muatienmat")) {
                        tiGia.setMuatienmat(item.getString("muatienmat"));
                    }
                    if (item.has("muack")) {
                        tiGia.setMuack(item.getString("muack"));
                    }
                    if (item.has("bantienmat")) {
                        tiGia.setBantienmat(item.getString("bantienmat"));
                    }
                    if (item.has("banck")) {
                        tiGia.setBanck(item.getString("banck"));
                    }
                    dsTiGia.add(tiGia);
                }
                return dsTiGia;
            }catch (Exception e){
                Log.e("Retrieve Data Failed",e.toString() );
            }
            return null;
        }
    }
}
