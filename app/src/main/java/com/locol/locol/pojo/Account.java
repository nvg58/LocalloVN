package com.locol.locol.pojo;

import android.app.Application;
import android.graphics.Bitmap;
import android.util.Log;
import android.widget.Toast;

import com.locol.locol.MainApplication;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import java.util.UUID;

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

//    public static void updateUserDataToParse() {
//        ParseUser user = new ParseUser();
//        user.setEmail(getUserEmail());
//        user.setUsername(getUserName());
//        user.setPassword(UUID.randomUUID().toString());
//        user.put("fbId", getUserFBId());
//        user.signUpInBackground(new SignUpCallback() {
//            public void done(ParseException e) {
//                if (e == null) {
//                    // Hooray! Let them use the app now.
//                    Toast.makeText(MainApplication.getAppContext(), "Login successfully!", Toast.LENGTH_SHORT).show();
//                } else {
//                    // Sign up didn't succeed. Look at the ParseException
//                    // to figure out what went wrong
//                    Toast.makeText(MainApplication.getAppContext(), "Something wrong happened!", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//        Log.wtf("updateUserDataToParse", "updateUserDataToParse");
//    }
}
