package andromeda.petrochemical;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class webViewNews extends AppCompatActivity {

    private WebView webviewthis;
    private DatabaseReference mdataRef;
    private String mPostW = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webview_page);
        mPostW = getIntent().getExtras().getString("posting_name");
        mdataRef = FirebaseDatabase.getInstance().getReference().child("news");
        webviewthis = (WebView) findViewById(R.id.webView_news);
        webviewthis.setWebViewClient(new WebViewClient());
        webviewthis.getSettings().setJavaScriptEnabled(true);
        webviewthis.getSettings().setLoadsImagesAutomatically(true);
        mdataRef.child(mPostW).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String webViewPost = (String)dataSnapshot.child("webViewPost").getValue();
                webviewthis.loadUrl(webViewPost);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
//                throw databaseError.toException();
            }
        });

    }

}
