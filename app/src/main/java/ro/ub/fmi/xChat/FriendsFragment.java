package ro.ub.fmi.xChat;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import ro.ub.fmi.xChat.ro.ub.fmi.xChat.Utils.Constants;
import ro.ub.fmi.xChat.ro.ub.fmi.xChat.Utils.User;
import ro.ub.fmi.xChat.ro.ub.fmi.xChat.Utils.UserViewHolder;


public class FriendsFragment extends Fragment {

    private View mMainView;
    private RecyclerView listOfUsers;
    private DatabaseReference mUsersDatabase;
    private String mCurrent_user_id;
    private FirebaseRecyclerOptions<User> options;
    private FirebaseRecyclerAdapter<User,UserViewHolder> adapter;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mMainView = inflater.inflate(R.layout.fragment_friends, container, false);
        mCurrent_user_id = FirebaseAuth.getInstance().getCurrentUser().getUid();


        listOfUsers = (RecyclerView) mMainView.findViewById(R.id.users_list);



        mUsersDatabase = FirebaseDatabase.getInstance().getReference().child("Users");

        options = new FirebaseRecyclerOptions.Builder<User>().setQuery(
                mUsersDatabase, User.class).build();



        adapter = new FirebaseRecyclerAdapter<User, UserViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull UserViewHolder holder, int position, @NonNull final User model) {

                String currentUid = getRef(position).getKey();
                model.setUid(currentUid);




                    if(model.getOnline().equals("true")){
                        holder.getOnlineImageView().setVisibility(View.VISIBLE);
                    }

                    Picasso.get().load(model.getThumb_image()).placeholder(R.drawable.default_user).into(holder.getCircleImageView());
                    holder.getTv_displayName().setText(model.getName());
                    holder.getTv_grupa().setText(model.getAn()+" "+model.getGrupa());








                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent clickIntent = new Intent(getContext(), MessageActivity.class);
                            clickIntent.putExtra(Constants.MESSAGE_UID, model.getUid());
                            clickIntent.putExtra(Constants.MESSAGE_DISPLAY,model.getName());
                            clickIntent.putExtra(Constants.MESSAGE_AVATAR,model.getThumb_image());

                            if(!(model.getUid().equals(mCurrent_user_id))){
                                startActivity(clickIntent);
                            }else {
                                Toast.makeText(getContext(),getString(R.string.txt_selectOtherUser),Toast.LENGTH_LONG).show();
                            }


                        }
                    });



            }

            @NonNull
            @Override
            public UserViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {


                    View userView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.user_layout,
                            viewGroup,false);

                    return new UserViewHolder(userView);


            }
        };

        listOfUsers.setHasFixedSize(true);


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());

        listOfUsers.setLayoutManager(linearLayoutManager);

        adapter.startListening();

        listOfUsers.setAdapter(adapter);



        return mMainView;

    }





    @Override
    public void onStart() {
        super.onStart();
        if(adapter!=null)
            adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        if(adapter!=null)
            adapter.stopListening();

    }

    @Override
    public void onResume() {
        super.onResume();
        if(adapter!=null)
            adapter.startListening();

    }


}
