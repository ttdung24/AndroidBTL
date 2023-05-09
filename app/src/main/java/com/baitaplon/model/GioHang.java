package com.baitaplon.model;

public class GioHang {
    String idsp;
    String tenSP;
    String img;
    int donGia;
    int soluong;

    public GioHang(String idsp, String tenSP, String img, int donGia, int soluong) {
        this.idsp = idsp;
        this.tenSP = tenSP;
        this.img = img;
        this.donGia = donGia;
        this.soluong = soluong;
    }
    public GioHang() {
    }

    public String getIdsp() {
        return idsp;
    }

    public void setIdsp(String idsp) {
        this.idsp = idsp;
    }

    public String getTenSP() {
        return tenSP;
    }

    public void setTenSP(String tenSP) {
        this.tenSP = tenSP;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public int getDonGia() {
        return donGia;
    }

    public void setDonGia(int donGia) {
        this.donGia = donGia;
    }

    public int getSoluong() {
        return soluong;
    }

    public void setSoluong(int soluong) {
        this.soluong = soluong;
    }
}
