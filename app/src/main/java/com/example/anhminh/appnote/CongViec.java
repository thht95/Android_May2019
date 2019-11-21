package com.example.anhminh.appnote;

import java.util.Date;

/**
 * Created by Anh Minh on 10/31/2017.
 */

public class CongViec {

    private int IdCV;
    private String TenCV;
    private byte[] Hinhanh;
    private String Ngaygio;

    public CongViec(int idCV, String tenCV, byte[] hinhanh, String ngaygio) {
        IdCV = idCV;
        TenCV = tenCV;
        Hinhanh = hinhanh;
        Ngaygio = ngaygio;

    }

    public int getIdCV() {
        return IdCV;
    }

    public void setIdCV(int idCV) {
        IdCV = idCV;
    }

    public String getTenCV() {
        return TenCV;
    }

    public void setTenCV(String tenCV) {
        TenCV = tenCV;
    }

    public byte[] getHinhanh() {
        return Hinhanh;
    }

    public void setHinhanh(byte[] hinhanh) {
        Hinhanh = hinhanh;
    }

    public String getNgaygio() {
        return Ngaygio;
    }

    public void setNgaygio(String ngaygio) {
        Ngaygio = ngaygio;
    }
}