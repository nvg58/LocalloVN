package com.locol.locol;

import android.graphics.Bitmap;

/**
 * Created by GiapNV on 3/9/15.
 * Project LocoL
 */
public class Account {
    private static String userName;
    private static String userEmail;
    private static Bitmap userCover;
    private static Bitmap userAvatar;
    private static String userFBId;

    private static Account sInstance;

    public Account() {
        userEmail = "";
        userName = "";
    }

    public static Account getInstance() {
        if (sInstance == null) {
            sInstance = new Account();
        }
        return sInstance;
    }

    public static String getUserName() {
        return userName;
    }

    public static void setUserName(String userName) {
        Account.userName = userName;
    }

    public static String getUserEmail() {
        return userEmail;
    }

    public static void setUserEmail(String userEmail) {
        Account.userEmail = userEmail;
    }

    public static Bitmap getUserCover() {
        return userCover;
    }

    public static void setUserCover(Bitmap userCover) {
        Account.userCover = userCover;
    }

    public static Bitmap getUserAvatar() {
        return userAvatar;
    }

    public static void setUserAvatar(Bitmap userAvatar) {
        Account.userAvatar = userAvatar;
    }

    public static String getUserFBId() {
        return userFBId;
    }

    public static void setUserFBId(String userFBId) {
        Account.userFBId = userFBId;
    }
}
