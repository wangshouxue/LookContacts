package com.example.mycalldemo;

import android.graphics.Bitmap;

import java.util.List;

public class ContactsBean {
    String name;
    Bitmap bm;
    List<phoneBean> phoneBeans;
    List<emailBean> emailBeans;
    List<addBean> addressBeans;
    String company;
    String job;

    public void setBm(Bitmap bm) {
        this.bm = bm;
    }

    public Bitmap getBm() {
        return bm;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setPhoneBeans(List<phoneBean> phoneBeans) {
        this.phoneBeans = phoneBeans;
    }

    public List<phoneBean> getPhoneBeans() {
        return phoneBeans;
    }

    public void setEmailBeans(List<emailBean> emailBeans) {
        this.emailBeans = emailBeans;
    }

    public List<emailBean> getEmailBeans() {
        return emailBeans;
    }

    public void setAddressBeans(List<addBean> addressBeans) {
        this.addressBeans = addressBeans;
    }

    public List<addBean> getAddressBeans() {
        return addressBeans;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getCompany() {
        return company;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getJob() {
        return job;
    }

    public static class phoneBean{
        String phone;

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getPhone() {
            return phone;
        }
    }
    public static class emailBean{
        String email;

        public void setEmail(String email) {
            this.email = email;
        }

        public String getEmail() {
            return email;
        }
    }
    public static class addBean{
        String address;

        public void setAddress(String address) {
            this.address = address;
        }

        public String getAddress() {
            return address;
        }
    }
}
