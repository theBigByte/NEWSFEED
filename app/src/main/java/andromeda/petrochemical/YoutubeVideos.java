package andromeda.petrochemical;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.vision.text.Text;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeStandalonePlayer;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static android.R.attr.path;


public class YoutubeVideos extends AppCompatActivity {

    private RecyclerView YouRecycler;
    private DatabaseReference YDatabaseReference;
    public static String DEVELOPER_KEY = "YOUR DEVELOPER KEY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.youtube_videos);
        YDatabaseReference = FirebaseDatabase.getInstance().getReference().child("yolo");
        YDatabaseReference.keepSynced(true);// <---- firebase offline feature
        YouRecycler = (RecyclerView) findViewById(R.id.listyou);
        YouRecycler.setHasFixedSize(true);
        YouRecycler.setLayoutManager(new LinearLayoutManager(this));


    }

    @Override
    public void onStart() {
        super.onStart();


       final FirebaseRecyclerAdapter<post2youtube, youpostHolder> fireYouRecyclerHolder = new FirebaseRecyclerAdapter<post2youtube, youpostHolder>(

                post2youtube.class,
                R.layout.youtube_card_view,
                youpostHolder.class,
                YDatabaseReference


        ) {
            @Override
            protected void populateViewHolder(youpostHolder viewHolder, final post2youtube model, int position) {
                viewHolder.setYoutube(model.getYoutubed());
                viewHolder.setTitle(model.getTitle());
                viewHolder.setSource(model.getSource());
                viewHolder.play.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = YouTubeStandalonePlayer.createVideoIntent(YoutubeVideos.this, DEVELOPER_KEY, model.getYoutubed());
                        startActivity(intent);
                    }
                });
                viewHolder.youfab.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent shareIntent = new Intent(Intent.ACTION_SEND);
                        shareIntent.setType("text/plain");
                        shareIntent.putExtra(Intent.EXTRA_TEXT, model.getShare() + model.getYoutubed());
                        startActivity(Intent.createChooser(shareIntent, "Share link using"));

                    }
                });
            }
        };
        YouRecycler.setAdapter(fireYouRecyclerHolder);

    }

    public static class youpostHolder extends RecyclerView.ViewHolder {
        View yView;
        Button play;
         FloatingActionButton youfab;
         TextView Title;
        TextView Source;


        public youpostHolder(View itemView) {
            super(itemView);
            yView = itemView;
            play = (Button)yView.findViewById(R.id.playBtn);
            youfab = (FloatingActionButton)yView.findViewById(R.id.youtubeFloatingbtn);
            Title = (TextView)yView.findViewById(R.id.titleText);
            Source = (TextView)yView.findViewById(R.id.sourceTextYoutube);
        }

        public void setSource(String source){
            Source.setText(source);
        }

        public void setYoutube(final String youtubed) {

            final YouTubeThumbnailView youPlay = (YouTubeThumbnailView) yView.findViewById(R.id.youtuber);
            youPlay.initialize(DEVELOPER_KEY, new YouTubeThumbnailView.OnInitializedListener() {
                @Override
                public void onInitializationSuccess(YouTubeThumbnailView youTubeThumbnailView, final YouTubeThumbnailLoader youTubeThumbnailLoader) {
                    youTubeThumbnailLoader.setVideo(youtubed);
                    youTubeThumbnailLoader.setOnThumbnailLoadedListener(new YouTubeThumbnailLoader.OnThumbnailLoadedListener() {
                        @Override
                        public void onThumbnailLoaded(YouTubeThumbnailView youTubeThumbnailView, String s) {
                            youTubeThumbnailLoader.release();
                        }

                        @Override
                        public void onThumbnailError(YouTubeThumbnailView youTubeThumbnailView, YouTubeThumbnailLoader.ErrorReason errorReason) {

                        }
                    });
                }

                @Override
                public void onInitializationFailure(YouTubeThumbnailView youTubeThumbnailView, YouTubeInitializationResult youTubeInitializationResult) {
                }
            });

        }
        public void setTitle(String title){
            Title.setText(title);
        }
    }
}