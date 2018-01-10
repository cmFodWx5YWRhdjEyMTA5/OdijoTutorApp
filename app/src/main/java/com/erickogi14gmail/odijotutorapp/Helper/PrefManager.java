package com.erickogi14gmail.odijotutorapp.Helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.erickogi14gmail.odijotutorapp.Data.Models.Subjects;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

/**
 * Created by Eric on 10/8/2017.
 */

public class PrefManager {
    // Shared preferences file name
    private static final String PREF_NAME = "OdijoTutor";
    // All Shared Preferences Keys
    private static final String KEY_IS_WAITING_FOR_SMS = "IsWaitingForSms";
    private static final String KEY_MOBILE_NUMBER = "mobile_number";
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";
    private static final String KEY_NAME = "name";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_MOBILE = "mobile";
    private static final String KEY_ZONE = "zone";
    private static final String KEY_IMAGE = "image";
    private static final int KEY_ZONE_TEST = 1;
    private static final String KEY_PROFILE_SET = "profile_set";
    private static final String KEY_SUBJECTS = "subjects";
    private static final String KEY_DESCRIPTION = "description";
    private static final String KEY_WORK_HRS = "work_hours";
    private static final String KEY_ID = "id";
    // Shared Preferences
    SharedPreferences pref;
    // Editor for Shared preferences
    SharedPreferences.Editor editor;
    // Context
    Context _context;
    // Shared pref mode
    int PRIVATE_MODE = 0;


    public PrefManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }
    public void setIsWaitingForSms(boolean isWaiting) {
        editor.putBoolean(KEY_IS_WAITING_FOR_SMS, isWaiting);
        editor.commit();
    }

    public boolean isWaitingForSms() {
        return pref.getBoolean(KEY_IS_WAITING_FOR_SMS, false);
    }

    public String getMobileNumber() {
        return pref.getString(KEY_MOBILE, null);
    }

    public void setMobileNumber(String mobileNumber) {
        editor.putString(KEY_MOBILE, mobileNumber);
        editor.commit();
    }

    public void createLogin(String name, String email, String mobile, String zone, String des, String workhrs, String id) {
        editor.putString(KEY_NAME, name);
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_MOBILE, mobile);
        editor.putString(KEY_ZONE, zone);
        editor.putString(KEY_DESCRIPTION, des);
        editor.putString(KEY_WORK_HRS, workhrs);
        editor.putBoolean(KEY_IS_LOGGED_IN, true);
        editor.putString(KEY_ID, id);
        editor.commit();
    }

    public boolean isLoggedIn() {
        return pref.getBoolean(KEY_IS_LOGGED_IN, false);
    }

    public void clearSession() {
        editor.clear();
        editor.commit();
    }

    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> profile = new HashMap<>();
        profile.put("name", pref.getString(KEY_NAME, null));
        profile.put("email", pref.getString(KEY_EMAIL, null));
        profile.put("mobile", pref.getString(KEY_MOBILE, null));
        profile.put("zone", pref.getString(KEY_ZONE, "null"));
        profile.put("description", pref.getString(KEY_DESCRIPTION, "Please add a short description of yourself"));
        profile.put("work_hours", pref.getString(KEY_WORK_HRS, "null"));
        profile.put("id", pref.getString(KEY_ID, "0"));
        return profile;
    }

    public void storeImg(String imagePath) {
        editor.putString(KEY_IMAGE, imagePath);
        editor.commit();
    }

    public String getImg() {
        return pref.getString(KEY_IMAGE, "null");
    }

    public String getId() {
        return pref.getString(KEY_ID, "0");
    }


    public void storeZone(String zone) {
        editor.putString(KEY_ZONE, zone);
        editor.commit();
    }

    public String getZone() {
        return pref.getString(KEY_ZONE, "null");
    }

    public void updateUser(String name, String email, String img, String zone, String des, String workhours) {
        editor.putString(KEY_NAME, name);
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_IMAGE, img);
        editor.putString(KEY_ZONE, zone);
        editor.putString(KEY_DESCRIPTION, des);
        editor.putString(KEY_WORK_HRS, workhours);
        editor.putBoolean(KEY_PROFILE_SET, true);


        editor.commit();
    }
    public boolean isProfileSet(){
        return pref.getBoolean(KEY_PROFILE_SET,false);
    }

    public void saveSubjects(ArrayList<Subjects> subjectses) {
        Gson gson = new Gson();
        String json = gson.toJson(subjectses);
        Log.d("gsonSa", json);
        editor.putString(KEY_SUBJECTS, json);
        editor.commit();

    }

    public ArrayList<Subjects> getSubjects() {
        ArrayList<Subjects> subjectses = new ArrayList<>();
        String gsong = pref.getString(KEY_SUBJECTS, "");
        Log.d("gsonSa", gsong);
        Gson gson = new Gson();
        Type collectionType = new TypeToken<Collection<Subjects>>() {

        }.getType();
        ArrayList<Subjects> subjectses1 = new ArrayList<>();
        Subjects s = new Subjects();
        s.setQualifications("Introduction");
        s.setSubject_rate("Introduction");
        s.setSubject_name("Introduction");
        s.setLevel("Introduction");
        s.setSubject_id(111);

        // subjectses1.add(s);

        subjectses1 = gson.fromJson(gsong, collectionType);
        // subjectses1.add(s);


        return subjectses1;
    }

    public String getSubjectstring() {
        String gsong = pref.getString(KEY_SUBJECTS, "");


        return gsong;
    }
}
