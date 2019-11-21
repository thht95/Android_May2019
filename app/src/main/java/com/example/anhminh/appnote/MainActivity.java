package com.example.anhminh.appnote;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.Blob;
import java.text.SimpleDateFormat;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.jar.Manifest;

public class MainActivity extends AppCompatActivity {
    public static Database database;
    ListView lvCongViec;
    EditText edtsearch;
    ArrayList<CongViec> congViecArrayList;
    AdapterCongViec adapter;

    ImageView imageHinhanh;
    ImageView imagecamera;
    ImageView imagefolder;
    ImageView imageHinhanh2;
    TextView txtTime;
    Button btnthem;
    final int REQUEST_CODE_CAMERA = 123;
    final int REQUEST_CODE_FOLDER = 456;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initControls();
        congViecArrayList = new ArrayList<>();
        adapter = new AdapterCongViec(this, R.layout.dong_cong_viec, congViecArrayList);
        lvCongViec.setAdapter(adapter);
        // tao file
        database = new Database(this, "ghichu.sqlite", null, 1);
        // tao bang cong viec
        database.QueryData("CREATE TABLE IF NOT EXISTS CongViec(Id INTEGER PRIMARY KEY AUTOINCREMENT,TenCV varchar(200),Hinhanh BLOB,Ngaygio varchar(50))");
        GetDataCongViec();
        edtsearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                GetDataCongViec();
                lvCongViec.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (edtsearch.getText().toString().equals("")) {
                    adapter.notifyDataSetChanged();
                } else {
                    search(edtsearch.getText().toString());
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void initControls() {
        txtTime = (TextView) findViewById(R.id.text_ngaygio);
        lvCongViec = (ListView) findViewById(R.id.listview);
        edtsearch = (EditText) findViewById(R.id.edt_timkiem);
    }
    private String getTCurrentTime() {
        String datetime;
        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat dingdangngay = new SimpleDateFormat(" HH:mm:ss dd/MM/yyyy");
        datetime = dingdangngay.format(calendar.getTime());
        return datetime;
    }
    private void GetDataCongViec() {
        // select data doc du lieu
        try {
            Cursor cursor = database.GetData("SELECT * FROM CongViec");
            congViecArrayList.clear();
            while (cursor.moveToNext()) {
                congViecArrayList.add(new CongViec(cursor.getInt(0), cursor.getString(1), cursor.getBlob(2), cursor.getString(3)));
            }
            adapter.notifyDataSetChanged();
        } catch (Exception ex) {
            Toast.makeText(this, "Chưa có dữ liệu!", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_congviec, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.menu_add) {
            DialogThem();
        }
//        if (item.getItemId() == R.id.item_sapxeptang) {
//            congViecArrayList =  Sapxeptang(congViecArrayList);
//            adapter.notifyDataSetChanged();
//        }
//        if (item.getItemId() == R.id.item_sapxepgiam) {
//            congViecArrayList =  Sapxepgiam(congViecArrayList);
//            adapter.notifyDataSetChanged();
//        }

        return super.onOptionsItemSelected(item);
    }
// // Sap xếp
//    public ArrayList<CongViec> Sapxeptang(ArrayList<CongViec> arr) {
//        for (int i = 0; i<arr.size()-1;i++){
//            for (int j = i+1; j<arr.size();j++){
//                SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss MM/dd/yyyy");
//                Date d1, d2;
//                int x =0;
//                try {
//                    d1 = df.parse(arr.get(i).getNgaygio());
//                    d2 = df.parse(arr.get(j).getNgaygio());
//                    if (d1.compareTo(d2) > 0) {
//                        CongViec cv = arr.get(i);
//                        arr.set(i,arr.get(j));
//                        arr.set(j,cv);
//                    }
//                } catch (Exception exx) {
//
//                }
//            }
//        }
//        return arr;
//
//    }
//
//    public ArrayList<CongViec> Sapxepgiam(ArrayList<CongViec> arr) {
//        for (int i = 0; i<arr.size()-1;i++){
//            for (int j = i+1; j<arr.size();j++){
//                SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss MM/dd/yyyy");
//                Date d1, d2;
//                int x =0;
//                try {
//                    d1 = df.parse(arr.get(i).getNgaygio());
//                    d2 = df.parse(arr.get(j).getNgaygio());
//                    if (d1.compareTo(d2) < 0) {
//                           CongViec cv = arr.get(i);
//                         arr.set(i,arr.get(j));
//                         arr.set(j,cv);
//                    }
//                } catch (Exception exx) {
//
//                }
//            }
//        }
//       return arr;
//
//    }


    // dialog thêm
    private void DialogThem() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); //loại bỏ title
        dialog.setContentView(R.layout.dialog_themcv);
        dialog.show();
        final EditText edtten = (EditText) dialog.findViewById(R.id.edt_tencv);
        btnthem = (Button) dialog.findViewById(R.id.btn_them);
        Button btnhuy = (Button) dialog.findViewById(R.id.btn_huy);
        imagecamera = (ImageView) dialog.findViewById(R.id.image_camera);
        imagefolder = (ImageView) dialog.findViewById(R.id.image_folder);
        imageHinhanh = (ImageView) dialog.findViewById(R.id.image_anh);
        imagecamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, REQUEST_CODE_CAMERA);
                //quyen camera
//                ActivityCompat.requestPermissions(MainActivity.this
//                ,new String[]{android.Manifest.permission.CAMERA},
//                        REQUEST_CODE_CAMERA
//                        );
            }
        });
        imagefolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_CODE_FOLDER);
//                    ActivityCompat.requestPermissions(MainActivity.this
//                            ,new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},
//                            REQUEST_CODE_FOLDER
//                    );
            }
        });
        //  su kien huy
        btnhuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        //GetDataCongViec();
        //su kien them
        btnthem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tencv = edtten.getText().toString();
                if (tencv.equals("")) {
                    Toast.makeText(MainActivity.this, " Vui lòng nhập tên ghi chú!", Toast.LENGTH_SHORT).show();
                } else {
                    if (imageHinhanh.getDrawable() != null) {
                        //chuyuen data imageview thanh byte
                        BitmapDrawable bitmapDrawable = (BitmapDrawable) imageHinhanh.getDrawable();
                        Bitmap bitmap = bitmapDrawable.getBitmap();
                        ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArray);
                        byte[] Hinhanh = byteArray.toByteArray();
                        MainActivity.database.INSERT_CONGVIEC(edtten.getText().toString(),
                                Hinhanh, getTCurrentTime());
                        Toast.makeText(MainActivity.this, "Đã thêm!", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                        GetDataCongViec();
                        adapter.notifyDataSetChanged();
                    } else {
                        byte[] Hinhanh = null;
                        database.QueryData("INSERT INTO CongViec VALUES (null,'" + tencv + "','" + Hinhanh + "','" + getTCurrentTime() + "')");
                        Toast.makeText(MainActivity.this, "Đã thêm!", Toast.LENGTH_SHORT).show();
                        //database.QueryData("INSERT INTO CongViec VALUES (null,'" + Hinhanh + "')");
                        dialog.dismiss();
                        GetDataCongViec();
                        adapter.notifyDataSetChanged();
                    }
                }
            }
        });
    }
    // CHO PHEP QUYEN DUNG CAMERA

    //    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//
//        switch (requestCode){
//            case REQUEST_CODE_CAMERA:
//                if(grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
//                           Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                           startActivityForResult(intent,REQUEST_CODE_CAMERA);
//
//                } else{
//                    Toast.makeText(MainActivity.this, "Bạn không cho phép mở camera!", Toast.LENGTH_SHORT).show();
//                }
//                break;
//            case REQUEST_CODE_FOLDER:
//
//                if(grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
//                    Intent intent = new Intent(Intent.ACTION_PICK);
//                    intent.setType("image/*");
//                    startActivityForResult(intent,REQUEST_CODE_FOLDER);
//                } else{
//                    Toast.makeText(MainActivity.this, "Bạn không cho phép mở thư viện ảnh!", Toast.LENGTH_SHORT).show();
//                }
//                break;
//        }
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//    }
    // đổ hình ảnh ra
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_CAMERA && resultCode == RESULT_OK && data != null) {
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            try {
                imageHinhanh.setImageBitmap(bitmap);
            } catch (Exception e) {

            }
            //Bitmap bitmap1 = (Bitmap) data.getExtras().get("data");
            try {
                imageHinhanh2.setImageBitmap(bitmap);
            } catch (Exception e) {
            }
        }
        if (requestCode == REQUEST_CODE_FOLDER && resultCode == RESULT_OK && data != null) {
            Uri uri = data.getData();
            try {
                InputStream inputStream = getContentResolver().openInputStream(uri);// mo doc du lieu ra
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                try {
                    imageHinhanh.setImageBitmap(bitmap);
                } catch (Exception exx) {

                }

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            try {
                InputStream inputStream = getContentResolver().openInputStream(uri);// mo doc du lieu ra
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                imageHinhanh2.setImageBitmap(bitmap);
            } catch (Exception e) {

            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    // Dialog sửa
    public void DialogSuacongviec(String ten, final int id, final Bitmap anh) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);// bo tieu de
        dialog.setContentView(R.layout.dialog_sua);
        final EditText editTencv = (EditText) dialog.findViewById(R.id.edtten_sua);
        Button buttonxacnhan = (Button) dialog.findViewById(R.id.btnxacnhan);
        Button buttonhuy = (Button) dialog.findViewById(R.id.btnhuy);
        ImageView imagecamera2 = (ImageView) dialog.findViewById(R.id.image_camera1);
        ImageView imagefolder2 = (ImageView) dialog.findViewById(R.id.image_folder1);
        imageHinhanh2 = (ImageView) dialog.findViewById(R.id.image_sua1);
        editTencv.setText(ten);
        imageHinhanh2.setImageBitmap(anh);

        imagecamera2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, REQUEST_CODE_CAMERA);
                //quyen camera
//                ActivityCompat.requestPermissions(MainActivity.this
//                ,new String[]{android.Manifest.permission.CAMERA},
//                        REQUEST_CODE_CAMERA
//                        );
            }
        });
        imagefolder2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_CODE_FOLDER);
//                    ActivityCompat.requestPermissions(MainActivity.this
//                            ,new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},
//                            REQUEST_CODE_FOLDER
//                    );
            }
        });
        buttonxacnhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tenmoi = editTencv.getText().toString().trim();
                BitmapDrawable bitmapDrawable = (BitmapDrawable) imageHinhanh2.getDrawable();
                Bitmap bitmap = bitmapDrawable.getBitmap();

                try {

                    ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArray);
                    byte[] Hinhanh = byteArray.toByteArray();

                    database.INSERT_CONGVIEC(tenmoi,
                            Hinhanh, getTCurrentTime());
                    database.QueryData("DELETE FROM CongViec WHERE Id='" + id + "'");
                    // database.QueryData("UPDATE CongViec SET Hinhanh ='"+ha +"' WHERE Id="+id+"");
                    Toast.makeText(MainActivity.this, "Đã cập nhật!", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                    GetDataCongViec();
                    adapter.notifyDataSetChanged();
                } catch (Exception ex) {
                    //Toast.makeText(MainActivity.this, "lỗi ảnh", Toast.LENGTH_SHORT).show();
                }
            }
        });
        buttonhuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
    //Dialog xóa
    public void DialogXoaCV(final String tencv, final int id) {
        final AlertDialog.Builder dialogxoa = new AlertDialog.Builder(this);
        dialogxoa.setMessage("Bạn có muốn xóa ghi chú này không?");
        dialogxoa.setPositiveButton("Có", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                database.QueryData("DELETE FROM CongViec WHERE Id='" + id + "'");
                Toast.makeText(MainActivity.this, "Đã xóa : " + tencv, Toast.LENGTH_SHORT).show();
                GetDataCongViec();
                adapter.notifyDataSetChanged();
            }
        });
        dialogxoa.setNegativeButton("Không", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        dialogxoa.show();
    }
    public void search(String tenCV) {
        ArrayList<CongViec> arr = new ArrayList<>();
        for (int i = 0; i < congViecArrayList.size(); i++) {
            if (congViecArrayList.get(i).getTenCV().contains(tenCV)) {
                arr.add(congViecArrayList.get(i));
            }
        }
        AdapterCongViec adapter2 = new AdapterCongViec(this, R.layout.dong_cong_viec, arr);
        lvCongViec.setAdapter(adapter2);
        //GetDataCongViec();
        //adapter.notifyDataSetChanged();
    }
}
