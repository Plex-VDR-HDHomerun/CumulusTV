package com.felkertech.cumulustv.tv;

import android.media.tv.TvInputService;

public class LeanbackTvInputService extends TvInputService {

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public final Session onCreateSession(String inputId) {
        return new CumulusTvTifService(this, inputId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}
