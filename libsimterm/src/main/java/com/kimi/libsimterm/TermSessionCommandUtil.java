package com.kimi.libsimterm;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.io.IOException;

import jackpal.androidterm.ShellTermSession;
import jackpal.androidterm.emulatorview.TermKeyListener;
import jackpal.androidterm.util.TermSettings;

/**
 * Created by kerence on 2016/11/23.
 */
public class TermSessionCommandUtil {
    static TermSessionCommandUtil util;

    public static TermSessionCommandUtil getInstance(Context c) {
        if (util == null) {
            util = new TermSessionCommandUtil();
        }
        return util;
    }

    public void init() {
        final SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(SystemUtil.getApplicationUsingReflection());
        TermSettings settings = new TermSettings(SystemUtil.getApplicationUsingReflection().getResources(), mPrefs);
        try {
            session = new ShellTermSession(settings, settings.getInitialCommand());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public TermSessionCommandUtil() {
        super();
        init();
    }
    ShellTermSession session;
    public void exec(String command) {
        run("su");
        //the second command will not be executed if haven't slept for 100 millseconds.
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        run(command);
    }

    private void run(CharSequence text) {
        StringBuilder sb = new StringBuilder();
        sb.append(text);
        if (sb.charAt(sb.length() - 1) != '\r') {
            sb.append('\r');
        }
        text = sb.toString();

        int n = text.length();

        char c;
        try {
            for (int i = 0; i < n; i++) {
                c = text.charAt(i);
                if (Character.isHighSurrogate(c)) {
                    int codePoint;
                    if (++i < n) {
                        codePoint = Character.toCodePoint(c, text.charAt(i));
                    } else {
                        // Unicode Replacement Glyph, aka white question mark in black diamond.
                        codePoint = '\ufffd';
                    }
                    mapAndSend(codePoint);
                } else {
                    mapAndSend(c);
                }
            }
            session.updateSize(80, 64);
        } catch (IOException e) {
        }
    }

    private void mapAndSend(int c) throws IOException {
        int result = new TermKeyListener(null).mapControlChar(c);
        if (result < TermKeyListener.KEYCODE_OFFSET) {
            session.write(result);
        }
    }

}
