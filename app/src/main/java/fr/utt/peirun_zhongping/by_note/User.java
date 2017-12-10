package fr.utt.peirun_zhongping.by_note;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by PEIRUN on 11/28/2017.
 */

public class User {
    private int userID;
    private String userName;
    private String userEmail;
    private static User instance = null;

    public static User getInstance() {
        if(instance == null) {
            instance = new User();
        }
        return instance;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
