package ro.ub.fmi.xChat.ro.ub.fmi.xChat.Utils;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;
import ro.ub.fmi.xChat.R;

public class UserViewHolder extends RecyclerView.ViewHolder {


    private TextView tv_grupa;
    private CircleImageView circleImageView;
    private TextView tv_displayName;
    private ImageView onlineImageView;




    public ImageView getOnlineImageView() {
        return onlineImageView;
    }

    public void setOnlineImageView(ImageView onlineImageView) {
        this.onlineImageView = onlineImageView;
    }

    public UserViewHolder(View view){
        super(view);
        tv_displayName = view.findViewById(R.id.users_displayName);
        tv_grupa = view.findViewById(R.id.users_status);
        circleImageView = view.findViewById(R.id.users_avatar);
        onlineImageView = view.findViewById(R.id.user_single_online_icon);



    }

    public TextView getTv_grupa() {
        return tv_grupa;
    }

    public void setTv_grupa(TextView tv_grupa) {
        this.tv_grupa = tv_grupa;
    }

    public CircleImageView getCircleImageView() {
        return circleImageView;
    }

    public void setCircleImageView(CircleImageView circleImageView) {
        this.circleImageView = circleImageView;
    }

    public TextView getTv_displayName() {
        return tv_displayName;
    }

    public void setTv_displayName(TextView tv_displayName) {
        this.tv_displayName = tv_displayName;
    }




}
