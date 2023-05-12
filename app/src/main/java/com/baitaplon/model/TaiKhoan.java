package com.baitaplon.model;

public class TaiKhoan {
    private String username, ten, diachi, id_user, sdt, email, ava;
    private boolean permisson;

    public TaiKhoan(String username, String ten, String diachi, String id_user, String sdt, String email, String ava, boolean permisson) {
        this.username = username;
        this.ten = ten;
        this.diachi = diachi;
        this.id_user = id_user;
        this.sdt = sdt;
        this.email = email;
        this.ava = ava;
        this.permisson = permisson;
    }

    public TaiKhoan() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTen() {
        return ten;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }

    public String getDiachi() {
        return diachi;
    }

    public void setDiachi(String diachi) {
        this.diachi = diachi;
    }

    public String getId_user() {
        return id_user;
    }

    public void setId_user(String id_user) {
        this.id_user = id_user;
    }

    public String getSdt() {
        return sdt;
    }

    public void setSdt(String sdt) {
        this.sdt = sdt;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAva() {
        return ava;
    }

    public void setAva(String ava) {
        this.ava = ava;
    }

    public boolean isPermisson() {
        return permisson;
    }

    public void setPermisson(boolean permisson) {
        this.permisson = permisson;
    }
}
