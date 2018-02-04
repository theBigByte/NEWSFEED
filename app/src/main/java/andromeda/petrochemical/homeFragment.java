//package andromeda.petrochemical;
//
//import android.content.Context;
//import android.content.Intent;
//import android.os.Bundle;
//import android.support.annotation.Nullable;
//import android.support.v4.app.Fragment;
//import android.support.v7.widget.LinearLayoutManager;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import com.firebase.ui.database.FirebaseRecyclerAdapter;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseUser;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.lsjwzh.widget.recyclerviewpager.RecyclerViewPager;
//import com.squareup.picasso.Callback;
//import com.squareup.picasso.NetworkPolicy;
//import com.squareup.picasso.Picasso;
//
//import static com.facebook.FacebookSdk.getApplicationContext;
//
//
//public class homeFragment extends Fragment {
//    private FirebaseAuth mAuth;
//    private RecyclerViewPager mrecyclerView;
//    private DatabaseReference mDatabaseReference;
//
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//
//        View v = inflater.inflate(R.layout.activity_home_page, container, false);
//
//        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("game");
//        mDatabaseReference.keepSynced(true);// <---- firebase offline feature
//        mrecyclerView = (RecyclerViewPager) getActivity().findViewById(R.id.list);
//        final LinearLayoutManager layout = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
//        layout.setReverseLayout(true);//<---- to get new posted data on top of list
//        layout.setStackFromEnd(true);//<-----
//        mrecyclerView.setLayoutManager(layout);
//        mAuth = FirebaseAuth.getInstance();
//        return v;
//    }
//    @Override
//    public void onStart() {
//        super.onStart();
//
//        final FirebaseRecyclerAdapter<post, postViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<post,postViewHolder>(
//
//                post.class,
//                R.layout.post_row_recycle_home,
//                postViewHolder.class,
//                mDatabaseReference
//
//        ) {
//            @Override
//            protected void populateViewHolder(postViewHolder viewHolder, final post model, int position) {
//                final String post_name = getRef(position).getKey();
//                viewHolder.setTitle(model.getTitle());
//                viewHolder.setdescription(model.getDescription());
//                viewHolder.setimage(getApplicationContext(), model.getImage());
//                viewHolder.setsource(model.getSource());
//                viewHolder.setRead(model.getRead());
////                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
////                    @Override
////                    public void onClick(View v) {
////                        if(getSupportActionBar().isShowing()){
////                            getSupportActionBar().hide();
//////                            btnav.setVisibility(View.GONE);
////                        }else{
////                            getSupportActionBar().show();
//////                            btnav.setVisibility(View.VISIBLE);
////                        }
////                    }
////                });
//                viewHolder.Read_more.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//
//                        Intent intentweb = new Intent(getActivity(), webViewNews.class);
//                        intentweb.putExtra("posting_name", post_name);
//                        startActivity(intentweb);
//                    }
//                });
//            }
//        };
//
//        mrecyclerView.setAdapter(firebaseRecyclerAdapter);
//
//    }
//
//
//    public static class postViewHolder extends RecyclerViewPager.ViewHolder {
//        View mView;
//        TextView Read_more;
//
//        public postViewHolder(View itemView) {
//
//            super(itemView);
//            mView = itemView;
//            Read_more = (TextView) mView.findViewById(R.id.Read_more);
//
//        }
//
//
//        public void setRead( String read){
//            Read_more.setText(read);
//        }
//
//        public void setTitle( String title) {
//            TextView post_title = (TextView) mView.findViewById(R.id.title_cardView);
//            post_title.setText(title);
//        }
//
//        public void setsource(String source) {
//            TextView post_source = (TextView) mView.findViewById(R.id.source_cardView);
//            post_source.setText(source);
//        }
//
//        public void setdescription(String description) {
//            TextView post_description = (TextView) mView.findViewById(R.id.description_cardView);
//            post_description.setText(description);
//        }
//
//
//        public void setimage(final Context ctx, final String image) {
//            final ImageView post_image = (ImageView) mView.findViewById(R.id.post_image);
//            Picasso.with(ctx).load(image).networkPolicy(NetworkPolicy.OFFLINE).into(post_image, new Callback() {
//
//                @Override
//                public void onSuccess() {
//                }
//
//                @Override
//                public void onError() {
//                    Picasso.with(ctx).load(image).into(post_image);
//                }
//            });
//        }
//
//    }
//}
