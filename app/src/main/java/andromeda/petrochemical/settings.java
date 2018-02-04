package andromeda.petrochemical;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

import com.facebook.FacebookSdk;
import com.facebook.share.model.AppInviteContent;
import com.facebook.share.widget.AppInviteDialog;

public class settings extends AppCompatActivity {

    private ToggleButton DayNightt;
    private static Bundle bundle = new Bundle();
    String appLinkUrl, previewImageUrl;
    public Button fbInvites;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.settings_page);

        fbInvites = (Button)findViewById(R.id.facebookIN);
        fbInvites.setOnClickListener(new View.OnClickListener() {
                                         @Override
                                         public void onClick(View v) {
                                             appLinkUrl = " https://fb.me/162525674340646";
                                             AppInviteContent content = new AppInviteContent.Builder()
                                                     .setApplinkUrl(appLinkUrl)
                                                     .build();
                                             AppInviteDialog.show(settings.this, content);
                                         }
                                     }
        );

        DayNightt = (ToggleButton) findViewById(R.id.dayNight_Switch);
        DayNightt.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked){
                if (isChecked){
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                }else{
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

                }
            }
        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }
    @Override
    public void onPause() {
        super.onPause();
        bundle.putBoolean("ToggleButtonState", DayNightt.isChecked());
    }
    @Override
    public void onResume() {
        super.onResume();
        DayNightt.setChecked(bundle.getBoolean("ToggleButtonState",false));
    }
}
