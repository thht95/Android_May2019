package com.example.anhminh.appnote;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.zip.Inflater;

import static android.os.Build.VERSION_CODES.O;

/**
 * Created by Anh Minh on 10/31/2017.
 */

public class AdapterCongViec extends BaseAdapter {

    private MainActivity context;
    private  int layout;
    private List<CongViec> congViecList;

    public AdapterCongViec(MainActivity context, int layout, List<CongViec> congViecList) {
        this.context = context;
        this.layout = layout;
        this.congViecList = congViecList;
    }

    @Override
    public int getCount() {
        return congViecList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }
    private class ViewHolder{
        TextView txtten;
        ImageView imgdelete;
        ImageView imageEdit;
        ImageView imagehinh;
        ImageView imagehinhsua;
        TextView txttime;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(layout, null);
            holder.txtten = (TextView) convertView.findViewById(R.id.txtview);
            holder.imgdelete = (ImageView) convertView.findViewById(R.id.imgDelete);
            holder.imageEdit = (ImageView) convertView.findViewById(R.id.imgeEditt);
            holder.imagehinh=(ImageView) convertView.findViewById(R.id.customhinh);
            holder.imagehinhsua=(ImageView) convertView.findViewById(R.id.customhinh);
            holder.txttime=(TextView) convertView.findViewById(R.id.text_ngaygio);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();

        }

        final CongViec congViec = congViecList.get(position);


        holder.txtten.setText(congViec.getTenCV());
        holder.txttime.setText(congViec.getNgaygio());


        // chuyen byte anh sua bang bitmap

        byte [] ha = congViec.getHinhanh();

        // Bắt sự kiện sửa
        holder.imageEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                byte[] Hinhanh = congViec.getHinhanh();
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inMutable = true;
                Bitmap bmp = BitmapFactory.decodeByteArray(Hinhanh, 0, Hinhanh.length, options);
                holder.imagehinhsua.setImageBitmap(bmp);
                context.DialogSuacongviec(congViec.getTenCV(),congViec.getIdCV(),bmp);
            }
        });

        // Bắt sự kiện xóa
        holder.imgdelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.DialogXoaCV(congViec.getTenCV(),congViec.getIdCV());
            }
        });

        // CHuyen byte hinh anh thanh bitmap
        byte[] Hinhanh = congViec.getHinhanh();
        Bitmap bitmap = BitmapFactory.decodeByteArray(Hinhanh,0,Hinhanh.length);
        holder.imagehinh.setImageBitmap(bitmap);
        //holder.imagehinhsua.setImageBitmap(bitmap);
        return convertView;


    }

}

