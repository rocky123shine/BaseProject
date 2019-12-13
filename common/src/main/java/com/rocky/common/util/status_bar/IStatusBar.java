package com.rocky.common.util.status_bar;

import android.view.Window;

interface IStatusBar {
    void setStatusBarColor(Window window, int color, boolean lightStatusBar);
}
