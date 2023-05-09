package com.baitaplon.model;

import java.util.List;

public class DonHang {
    public static int id = 1;
    private String maDh;
    private List<GioHang> listGh;
    private String tongTien;
    private boolean ptThanhtoan;

    public DonHang(List<GioHang> listGh, String tongTien, boolean ptThanhtoan) {
        this.maDh = "dh" + id;
        id = id + 1;
        this.listGh = listGh;
        this.tongTien = tongTien;
        this.ptThanhtoan = ptThanhtoan;
    }

    public DonHang() {
    }

    public String getMaDh() {
        return maDh;
    }

    public void setMaDh(String maDh) {
        this.maDh = maDh;
    }

    public List<GioHang> getListGh() {
        return listGh;
    }

    public void setListGh(List<GioHang> listGh) {
        this.listGh = listGh;
    }

    public String getTongTien() {
        return tongTien;
    }

    public void setTongTien(String tongTien) {
        this.tongTien = tongTien;
    }

    public boolean isPtThanhtoan() {
        return ptThanhtoan;
    }

    public void setPtThanhtoan(boolean ptThanhtoan) {
        this.ptThanhtoan = ptThanhtoan;
    }
}
