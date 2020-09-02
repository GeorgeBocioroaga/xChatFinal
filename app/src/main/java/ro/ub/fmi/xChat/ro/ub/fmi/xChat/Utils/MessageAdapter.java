package ro.ub.fmi.xChat.ro.ub.fmi.xChat.Utils;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import ro.ub.fmi.xChat.R;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder>{


    private List<Messages> mMessageList;
    private DatabaseReference mUserDatabase;
    private FirebaseAuth mAuth;

    public MessageAdapter(List<Messages> mMessageList) {

        this.mMessageList = mMessageList;

    }



    public static class MessageViewHolder extends RecyclerView.ViewHolder {

        public TextView messageText;
        public CircleImageView profileImage;
        public TextView displayName;
        public ImageView messageImage;
        public TextView timeText;

        public TextView messageSelf;
        public TextView timeSelf;
        public TextView nameSelf;

        public RelativeLayout to_message;
        public RelativeLayout from_mesaage;



        public MessageViewHolder(View view) {
            super(view);


            messageText = (TextView) view.findViewById(R.id.message_text_layout);
            profileImage = (CircleImageView) view.findViewById(R.id.message_profile_layout);
            displayName = (TextView) view.findViewById(R.id.name_text_layout);

            timeText = (TextView)  view.findViewById(R.id.time_text_layout);
            timeSelf = (TextView)  view.findViewById(R.id.time_text_layout_self);
            messageSelf = (TextView) view.findViewById(R.id.message_text_layout_self);


            to_message = view.findViewById(R.id.message_to);
            from_mesaage = view.findViewById(R.id.message_from);


        }
    }


    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.message_single_layout ,viewGroup, false);


        mAuth = FirebaseAuth.getInstance();
        return new MessageViewHolder(v);
    }










    @Override
    public void onBindViewHolder(final MessageViewHolder viewHolder, int i) {

        String currentUserId = mAuth.getCurrentUser().getUid();

        Messages c = mMessageList.get(i);

        final String from_user = c.getFrom();
        String message_type = c.getType();


        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(from_user);

        mUserDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String name = dataSnapshot.child("name").getValue().toString();
                String image = dataSnapshot.child("thumb_image").getValue().toString();

                viewHolder.displayName.setText(name);


                Picasso.get().load(image).networkPolicy(NetworkPolicy.OFFLINE)
                        .placeholder(R.drawable.default_user).into(viewHolder.profileImage);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


//.            viewHolder.messageImage.setVisibility(View.INVISIBLE);

            if(from_user.equals(currentUserId)){

                viewHolder.from_mesaage.setVisibility(View.INVISIBLE);
                viewHolder.to_message.setVisibility(View.VISIBLE);

                viewHolder.displayName.setVisibility(View.INVISIBLE);
                viewHolder.profileImage.setVisibility(View.INVISIBLE);




                viewHolder.messageText.setVisibility(View.INVISIBLE);
                viewHolder.timeText.setVisibility(View.INVISIBLE);

                viewHolder.timeSelf.setVisibility(View.VISIBLE);
                viewHolder.messageSelf.setVisibility(View.VISIBLE);



                viewHolder.timeSelf.setText(GetTimeAgo.getTimeAgo(c.getTime()));
                viewHolder.messageSelf.setText(c.getMessage());
                viewHolder.messageSelf.setTextColor(Color.BLACK);



            }else {

                viewHolder.from_mesaage.setVisibility(View.VISIBLE);
                viewHolder.to_message.setVisibility(View.INVISIBLE);


                viewHolder.displayName.setVisibility(View.VISIBLE);
                viewHolder.profileImage.setVisibility(View.VISIBLE);
                viewHolder.messageText.setVisibility(View.VISIBLE);
                viewHolder.timeText.setVisibility(View.VISIBLE);

                viewHolder.timeSelf.setVisibility(View.INVISIBLE);
                viewHolder.messageSelf.setVisibility(View.INVISIBLE);


                viewHolder.timeText.setText(GetTimeAgo.getTimeAgo(c.getTime()));
                viewHolder.messageText.setTextColor(Color.BLACK);
                viewHolder.messageText.setText(c.getMessage());
            }








    }

    @Override
    public int getItemCount() {
        return mMessageList.size();
    }






}