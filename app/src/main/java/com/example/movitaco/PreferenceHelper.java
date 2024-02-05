package com.example.movitaco;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceHelper {
        private final String INTRO = "intro";
        private final String idTaqueria = "idTaqueria";
        private final String idMesero = "idMesero";
        private SharedPreferences app_prefs;
        private Context context;

        public PreferenceHelper(Context context) {
                app_prefs = context.getSharedPreferences("shared",
                        Context.MODE_PRIVATE);
                this.context = context;
        }

        public void putIsLogin(boolean loginorout) {
                SharedPreferences.Editor edit = app_prefs.edit();
                edit.putBoolean(INTRO, loginorout);
                edit.commit();
        }
        public boolean getIsLogin() {
                return app_prefs.getBoolean(INTRO, false);
        }

        public void putID(String loginorout) {
                SharedPreferences.Editor edit = app_prefs.edit();
                edit.putString(idTaqueria, loginorout);
                edit.commit();
        }
        public String getIdTaqueria() {
                return app_prefs.getString(idTaqueria, "");
        }

        public void putIDM(String login) {
                SharedPreferences.Editor edit = app_prefs.edit();
                edit.putString(idMesero, login);
                edit.commit();
        }
        public String getIdMesero() {
                return app_prefs.getString(idMesero, "");
        }
}