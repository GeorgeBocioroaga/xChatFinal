package ro.ub.fmi.xChat.ro.ub.fmi.xChat.Utils;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import ro.ub.fmi.xChat.ChatsFragment;
import ro.ub.fmi.xChat.FriendsFragment;

public class SectionPageAdaptor  extends FragmentPagerAdapter {

    public SectionPageAdaptor(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        switch (position){

            case 0:
                ChatsFragment chatsFragment = new ChatsFragment();
                return chatsFragment;
            case 1:
                FriendsFragment friendsFragment = new FriendsFragment();
                return  friendsFragment;

            default:
                return null;

        }


    }

    @Override
    public int getCount() {
        return 2;
    }


    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "CHATS";
            case 1:
                return "FRIENDS";


            default:
                return null;
        }
    }
}
