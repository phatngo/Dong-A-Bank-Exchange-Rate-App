package com.example.model;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.tigiahoidoaidongabank.R;

import java.io.IOException;
import java.net.URL;
import java.util.List;

public class TiGiaAdapter extends ArrayAdapter<TiGia> {
    Activity context;
    int resource;
    List<TiGia> objects;
    public TiGiaAdapter(@NonNull Activity context, int resource, @NonNull List<TiGia> objects) {
        super(context, resource, objects);
        this.context=context;
        this.resource=resource;
        this.objects=objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater=this.context.getLayoutInflater();
        View view=layoutInflater.inflate(this.resource,null);
        TiGia tiGia=objects.get(position);

        TextView txtType=view.findViewById(R.id.txtType);
        TextView txtMuatm=view.findViewById(R.id.txtMuatm);
        TextView txtMuaCk=view.findViewById(R.id.txtMuaCk);
        TextView txtBanTM=view.findViewById(R.id.txtBanTM);
        TextView txtBanCK=view.findViewById(R.id.txtBanCK);
        ImageView imgHinh=view.findViewById(R.id.imgHinh);

        txtType.setText(tiGia.getType());
        txtBanCK.setText(tiGia.getBanck());
        txtBanTM.setText(tiGia.getBantienmat());
        txtMuatm.setText(tiGia.getMuatienmat());
        txtMuaCk.setText(tiGia.getMuack());
        imgHinh.setImageBitmap(tiGia.getBitmap());
        return view;
    }
}
