package com.bagas.tanganicovid_19.adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.text.format.DateFormat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bagas.tanganicovid_19.AddPostActivity;
import com.bagas.tanganicovid_19.R;
import com.bagas.tanganicovid_19.ThereProfileActivity;
import com.bagas.tanganicovid_19.models.ModelPost;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;


import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class AdapterPosts extends RecyclerView.Adapter<AdapterPosts.Myholder> {
    private Context context;
    private List<ModelPost> postList;
    ProgressDialog pd;

    String myUid;

    private DatabaseReference likesRef;
    private DatabaseReference postRef;

    boolean mProcessLike = false;


    public AdapterPosts(Context context, List<ModelPost> postList) {
        this.context = context;
        this.postList = postList;
        myUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        likesRef = FirebaseDatabase.getInstance().getReference().child("Likes");
        postRef = FirebaseDatabase.getInstance().getReference().child("Posts");
    }

    @NonNull
    @Override
    public Myholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflate layout row_posts.xml
        View view = LayoutInflater.from(context).inflate(R.layout.row_posts, parent, false);
        return new Myholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final Myholder myHolder, final int position) {
        final String uid = postList.get(position).getUid();
        String uEmail = postList.get(position).getuEmail();
        String uName = postList.get(position).getuName();
        String uDp = postList.get(position).getuDp();
        final String pid = postList.get(position).getpId();
        String pTitle = postList.get(position).getpTitle();
        String pDescription = postList.get(position).getpDescr();
        final String pImage = postList.get(position).getpImage();
        String pTimeStamp = postList.get(position).getpTime();
        String pLikes = postList.get(position).getpLikes();

        //convert timestamp to dd/mm/yyyy hh:mm am/pm
        Calendar calendar = Calendar.getInstance(Locale.getDefault());
        calendar.setTimeInMillis(Long.parseLong(pTimeStamp));
        String pTime = DateFormat.format("dd/MM/yyyy hh:mm aa", calendar).toString();

        //set data
        myHolder.uNameTv.setText(uName);
        myHolder.pTimeTv.setText(pTime);
        myHolder.pTitleTv.setText(pTitle);
        myHolder.pDescriptionTv.setText(pDescription);
        myHolder.pLikesTv.setText(pLikes + "Likes");
        //set Likes for each post
        setLikes(myHolder, pid);


        //setUser Dp
        try {
            Picasso.get().load(uDp).placeholder(R.drawable.ic_face_diskusi).into(myHolder.uPictureIv);
        } catch (Exception e) {

        }

        //set post image
        //if there is no image i.e pImage.equals("noImage") then hide ImageView
        if(pImage.equals("noImage")) {
            //hide imageview
            myHolder.pImageIv.setVisibility(View.GONE);
        } else {

            //show imageview
            myHolder.pImageIv.setVisibility(View.VISIBLE); //make sure to correct this

            try {
                Picasso.get().load(pImage).into(myHolder.pImageIv);
            } catch (Exception e) {

            }
        }

        //handle button click,
        myHolder.moreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMoreOptions(myHolder.moreButton, uid, myUid, pid, pImage);
            }
        });

        myHolder.likeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               final int pLikes = Integer.parseInt(postList.get(position).getpLikes());
               mProcessLike = true;
               //get id of the post clicked
                final String postIde = postList.get(position).getpId();
                likesRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(mProcessLike) {
                            if(dataSnapshot.child(postIde).hasChild(myUid)){
                                //already liked, so remove like
                                postRef.child(postIde).child("pLikes").setValue(""+(pLikes-1));
                                likesRef.child(postIde).child(myUid).removeValue();
                                mProcessLike = false;
                            } else {
                                //not likid, then like
                                postRef.child(postIde).child("pLikes").setValue(""+(pLikes+1));
                                likesRef.child(postIde).child(myUid).setValue("Liked");
                                mProcessLike = false;
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

        myHolder.commentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Comment", Toast.LENGTH_SHORT).show();
            }
        });

        myHolder.shareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Share", Toast.LENGTH_SHORT).show();
            }
        });




    }

    private void setLikes(final Myholder myHolder, final String postKey) {
        likesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child(postKey).hasChild(myUid)) {
                    //user has liked post
                    myHolder.likeBtn.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_thumb_up_black_24dpp, 0,0,0);

                } else {
                    //user hasn't like post
                    myHolder.likeBtn.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_thumb_up_black_24dp, 0,0,0);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void showMoreOptions(ImageButton moreButton, String uid, String myUid, final String pid, final String pImage) {
        //creating popup menu
        PopupMenu popupMenu = new PopupMenu(context, moreButton, Gravity.END);


        //show delete option in only post(S) of currently signed-in user
        if(uid.equals(myUid)){
            //add item in menu
            popupMenu.getMenu().add(Menu.NONE, 0, 0, "Hapus postingan");
            popupMenu.getMenu().add(Menu.NONE, 1, 0, "Edit postingan");
        }

        //item click listener
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();
                if(id == 0) {
                    //delete is clicked
                    beginDelete(pid, pImage);
                } else if (id == 1) {
                    //edit is clicked
                    //start AddpostActivity with any "editPost" and id of the post clicked
                    Intent intent = new Intent(context, AddPostActivity.class);
                    intent.putExtra("key", "editPost");
                    intent.putExtra("editPostId", pid);
                    context.startActivity(intent);

                }
                return false;
            }
        });

        //show menu
        popupMenu.show();
    }

    private void beginDelete(String pid, String pImage) {
        //post can be with or without image
        if (pImage.equals("noImage")) {
            deleteWithoutImage(pid);
        } else {
            deleteWithImage(pid, pImage);
        }
    }

    private void showProgress() {
        pd.show();
        pd.setContentView(R.layout.progress_dialog);
        pd.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
    }

    private void deleteWithoutImage(String pid) {
        pd = new ProgressDialog(context);
        showProgress();

        Query fquery = FirebaseDatabase.getInstance().getReference("Posts")
                .orderByChild("pId").equalTo(pid);
        fquery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds: dataSnapshot.getChildren()) {
                    ds.getRef().removeValue(); //remove value from firebase pid matches
                }
                //deleted
                pd.dismiss();
                Toast.makeText(context, "Berhasil Menghapus Postingan", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void deleteWithImage(final String pid, String pImage) {
        pd = new ProgressDialog(context);
        showProgress();

        //steps:
//        1) Deleting image using uri
//        2) Delete from database using post id

        StorageReference picRef = FirebaseStorage.getInstance().getReferenceFromUrl(pImage);
        picRef.delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Query fquery = FirebaseDatabase.getInstance().getReference("Posts")
                                .orderByChild("pId").equalTo(pid);
                        fquery.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for(DataSnapshot ds: dataSnapshot.getChildren()) {
                                    ds.getRef().removeValue(); //remove value from firebase pid matches
                                }
                                //deleted
                                pd.dismiss();
                                Toast.makeText(context, "Berhasil Menghapus Postingan", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        pd.dismiss();
                        Toast.makeText(context, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    //view holder class
    class Myholder extends RecyclerView.ViewHolder {

        //view from row_post.xml
        ImageView uPictureIv, pImageIv;
        TextView uNameTv, pTimeTv, pTitleTv, pDescriptionTv, pLikesTv;
        ImageButton moreButton;
        Button likeBtn, commentBtn, shareBtn;
        LinearLayout profileLayout;


        public Myholder(@NonNull View itemView) {
            super(itemView);

            //init views
            uPictureIv = itemView.findViewById(R.id.uPictureIv);
            pImageIv = itemView.findViewById(R.id.pImageIv);
            uNameTv = itemView.findViewById(R.id.uNameTv);
            pTimeTv = itemView.findViewById(R.id.pTimeTv);
            pTitleTv = itemView.findViewById(R.id.pTitleTv);
            pDescriptionTv = itemView.findViewById(R.id.pDescriptionTv);
            pLikesTv = itemView.findViewById(R.id.pLikesTv);
            moreButton = itemView.findViewById(R.id.moreBtn);
            likeBtn = itemView.findViewById(R.id.likeBtn);
            commentBtn = itemView.findViewById(R.id.commentBtn);
            shareBtn = itemView.findViewById(R.id.shareBtn);
            profileLayout = itemView.findViewById(R.id.profileLayout);



        }
    }
}
