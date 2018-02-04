package andromeda.petrochemical;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.lsjwzh.widget.recyclerviewpager.RecyclerViewPager;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

public class HomePage extends AppCompatActivity {


    private FirebaseAuth mAuth;
    private RecyclerViewPager mrecyclerView;
    private DatabaseReference mDatabaseReference;
    private Button rollUp;
    public static int recyclerPos = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_home_page);
        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("news");
        mDatabaseReference.keepSynced(true);// <---- firebase offline feature
        mrecyclerView = (RecyclerViewPager) findViewById(R.id.list);

        final LinearLayoutManager layout = new LinearLayoutManager(HomePage.this, LinearLayoutManager.VERTICAL, false);
        layout.setReverseLayout(true);//<---- to get new posted data on top of list
        layout.setStackFromEnd(true);//<-----
        mrecyclerView.setLayoutManager(layout);
        rollUp = (Button) findViewById(R.id.roll_up);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mAuth = FirebaseAuth.getInstance();

        this.mrecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            int mLastFirstVisibleItem = 0;

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                final int currentFirstVisibleItem = layout.findFirstVisibleItemPosition();

                if (currentFirstVisibleItem < this.mLastFirstVisibleItem) {
                    HomePage.this.getSupportActionBar().hide();

                } else if (currentFirstVisibleItem > this.mLastFirstVisibleItem) {
                    HomePage.this.getSupportActionBar().show();
                }
                this.mLastFirstVisibleItem = currentFirstVisibleItem;
            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            sentToStart();
        }

        final FirebaseRecyclerAdapter<post, postViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<post, postViewHolder>(

                post.class,
                R.layout.post_row_recycle_home,
                postViewHolder.class,
                mDatabaseReference

        ) {
            @Override
            protected void populateViewHolder(postViewHolder viewHolder, final post model, int position) {
                final String post_name = getRef(position).getKey();
                viewHolder.setTitle(model.getTitle());
                viewHolder.setdescription(model.getDescription());
                viewHolder.setimage(getApplicationContext(), model.getImage());
                viewHolder.setsource(model.getSource());
                viewHolder.setRead(model.getRead());
                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(getSupportActionBar().isShowing()){
                            getSupportActionBar().hide();
//                            btnav.setVisibility(View.GONE);
                        }else{
                            getSupportActionBar().show();
//                            btnav.setVisibility(View.VISIBLE);
                        }
                    }
                });
                viewHolder.Read_more.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                            Intent intentweb = new Intent(HomePage.this, webViewNews.class);
                            intentweb.putExtra("posting_name", post_name);
                            startActivity(intentweb);
                    }
                });
            }
        };

        mrecyclerView.setAdapter(firebaseRecyclerAdapter);

    }


    public static class postViewHolder extends RecyclerViewPager.ViewHolder {
        View mView;
        TextView Read_more;

        public postViewHolder(View itemView) {

            super(itemView);
            mView = itemView;
            Read_more = (TextView) mView.findViewById(R.id.Read_more);

        }


        public void setRead( String read){
            Read_more.setText(read);
        }

        public void setTitle( String title) {
            TextView post_title = (TextView) mView.findViewById(R.id.title_cardView);
            post_title.setText(title);
        }

        public void setsource(String source) {
            TextView post_source = (TextView) mView.findViewById(R.id.source_cardView);
            post_source.setText(source);
        }

        public void setdescription(String description) {
            TextView post_description = (TextView) mView.findViewById(R.id.description_cardView);
            post_description.setText(description);
        }


        public void setimage(final Context ctx, final String image) {
            final ImageView post_image = (ImageView) mView.findViewById(R.id.post_image);
            Picasso.with(ctx).load(image).networkPolicy(NetworkPolicy.OFFLINE).into(post_image, new Callback() {

                @Override
                public void onSuccess() {
                }

                @Override
                public void onError() {
                    Picasso.with(ctx).load(image).into(post_image);
                }
            });
        }

    }

        private void sentToStart() {
        Intent intent = new Intent(HomePage.this, LoginPage.class);
        startActivity(intent);
        finish();
    }
    @Override
        public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_home_page, menu);
        return true;
    }
    @Override
        public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        int id = item.getItemId();

        if (id == R.id.logout){
            FirebaseAuth.getInstance().signOut();
            sentToStart();
            return true;
        }
        if (id == R.id.action_settings){
            Intent intentSettings = new Intent(HomePage.this,settings.class);
            startActivity(intentSettings);
            LinearLayoutManager layoutManager = ((LinearLayoutManager) mrecyclerView.getLayoutManager());
            recyclerPos = layoutManager.findFirstVisibleItemPosition();
        }
        if (id == R.id.roll_up){
            LinearLayoutManager layoutManagerroll = ((LinearLayoutManager) mrecyclerView.getLayoutManager());
            layoutManagerroll.scrollToPosition(2);

        }
        if (id == R.id.youtube){
            Intent youint = new Intent(HomePage.this,YoutubeVideos.class);
            startActivity(youint);
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}