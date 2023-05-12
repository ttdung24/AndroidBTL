package com.baitaplon.model;

import java.io.Serializable;

public class SanPham implements Serializable {
    public static int id = 1;
    String idSp, nhasx, namsx, img, maDanhMuc, mota, tenSp;
    int gia, slCon;
    float saoDanhgia;
    // { "idSp": "sp01", "nhasx": "", "namsx": "", "maDanhMuc": "", "mota": "", "tenSp": "San pham 1", "gia": 55000, "slCon": 5, "saoDanhgia": 4.5, "img": ""}
    public SanPham(String nhasx, String namsx, String img, String maDanhMuc, String mota, String tenSp, int gia, int slCon, float saoDanhgia) {
        this.idSp = "sp" + id ;
        id = id + 1;
        this.nhasx = nhasx;
        this.namsx = namsx;
        this.img = img;
        this.maDanhMuc = maDanhMuc;
        this.mota = mota;
        this.tenSp = tenSp;
        this.gia = gia;
        this.slCon = slCon;
        this.saoDanhgia = saoDanhgia;
    }

    public SanPham(String idSp, String nhasx, String namsx, String img, String maDanhMuc, String mota, String tenSp, int gia, int slCon, float saoDanhgia) {
        this.idSp = idSp;
        this.nhasx = nhasx;
        this.namsx = namsx;
        this.img = img;
        this.maDanhMuc = maDanhMuc;
        this.mota = mota;
        this.tenSp = tenSp;
        this.gia = gia;
        this.slCon = slCon;
        this.saoDanhgia = saoDanhgia;
    }

    public SanPham() {
    }

    public String getIdSp() {
        return idSp;
    }

    public void setIdSp(String idSp) {
        this.idSp = idSp;
    }

    public String getNhasx() {
        return nhasx;
    }

    public void setNhasx(String nhasx) {
        this.nhasx = nhasx;
    }

    public String getNamsx() {
        return namsx;
    }

    public void setNamsx(String namsx) {
        this.namsx = namsx;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getMota() {
        return mota;
    }

    public void setMota(String mota) {
        this.mota = mota;
    }

    public String getTenSp() {
        return tenSp;
    }

    public void setTenSp(String tenSp) {
        this.tenSp = tenSp;
    }

    public int getGia() {
        return gia;
    }

    public void setGia(int gia) {
        this.gia = gia;
    }

    public int getSlCon() {
        return slCon;
    }

    public void setSlCon(int slCon) {
        this.slCon = slCon;
    }

    public float getSaoDanhgia() {
        return saoDanhgia;
    }

    public void setSaoDanhgia(float saoDanhgia) {
        this.saoDanhgia = saoDanhgia;
    }

    public String getMaDanhMuc() {
        return maDanhMuc;
    }

    public void setMaDanhMuc(String maDanhMuc) {
        this.maDanhMuc = maDanhMuc;
    }
}
