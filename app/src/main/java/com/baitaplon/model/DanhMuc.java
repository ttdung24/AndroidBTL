package com.baitaplon.model;

public class DanhMuc {
    String maDanhMuc;
    String tenDanhMuc;
    String imgDanhmuc;
//{ "maDanhMuc": "dm02",  "tenDanhMuc": "Quần", "imgDanhmuc": ""}
    public DanhMuc(String maDanhMuc, String tenDanhMuc, String imgDanhmuc) {
        this.maDanhMuc = maDanhMuc;
        this.tenDanhMuc = tenDanhMuc;
        this.imgDanhmuc = imgDanhmuc;
    }

    public DanhMuc() {
    }

    public String getMaDanhMuc() {
        return maDanhMuc;
    }

    public void setMaDanhMuc(String maDanhMuc) {
        this.maDanhMuc = maDanhMuc;
    }

    public String getTenDanhMuc() {
        return tenDanhMuc;
    }

    public void setTenDanhMuc(String tenDanhMuc) {
        this.tenDanhMuc = tenDanhMuc;
    }

    public String getImgDanhmuc() {
        return imgDanhmuc;
    }

    public void setImgDanhmuc(String imgDanhmuc) {
        this.imgDanhmuc = imgDanhmuc;
    }

    @Override
    public String toString() {
        return tenDanhMuc;
    }
}
