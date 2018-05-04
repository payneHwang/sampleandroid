package ui.com;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.pep.dubsdk.view.activity.DubActivity;


public class MainActivity extends AppCompatActivity {
    private static final int VIDEO_FROM_NET = 0;
    private static final int VIDEO_FROM_LOCAL = 1;
    private static final int VIDEO_FROM_SDCARD = 2;
    private static final int VIDEO_FROM_ASSETS = 3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void loadNetVideo(View view) {
        Intent intent = new Intent(this, DubActivity.class);
        intent.putExtra("source", VIDEO_FROM_NET);
        startActivity(intent);
    }

    public void loadLocalVideo(View view) {
        Intent intent = new Intent(this, DubActivity.class);
        intent.putExtra("source", VIDEO_FROM_LOCAL);
        startActivity(intent);
    }

    public void loadSdcardVideo(View view) {
        Intent intent = new Intent(this, DubActivity.class);
        intent.putExtra("source", VIDEO_FROM_SDCARD);
        startActivity(intent);
    }

    public void loadAssetsVideo(View view) {
        Intent intent = new Intent(this, DubActivity.class);
        intent.putExtra("source", VIDEO_FROM_ASSETS);
        startActivity(intent);
    }
}
