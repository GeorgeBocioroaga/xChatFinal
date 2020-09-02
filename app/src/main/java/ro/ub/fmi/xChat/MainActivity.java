package ro.ub.fmi.xChat;

import android.content.Intent;
import android.graphics.Color;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;

import ro.ub.fmi.xChat.ro.ub.fmi.xChat.Utils.SectionPageAdaptor;

public class MainActivity extends AppCompatActivity {



    private FirebaseAuth mAuth;
    private Toolbar toolbar;
    private ViewPager viewPager;
    private SectionPageAdaptor sectionPageAdaptor;

    private DatabaseReference mUserRef;
    private TabLayout mainTabLayout;


    private  void initComponents(){
        toolbar = findViewById(R.id.main_page_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getString(R.string.app_name));
        viewPager = findViewById(R.id.main_pager);
        sectionPageAdaptor =  new SectionPageAdaptor(getSupportFragmentManager());
        viewPager.setAdapter(sectionPageAdaptor);
        mainTabLayout =  findViewById(R.id.main_tabs);
        mainTabLayout.setupWithViewPager(viewPager);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initComponents();
        mAuth =  FirebaseAuth.getInstance();

        if (mAuth.getCurrentUser() != null) {


            mUserRef = FirebaseDatabase.getInstance().getReference().child("Users").child(mAuth.getCurrentUser().getUid());

        }

    }

    @Override
    public void onStart() {
        super.onStart();



        FirebaseUser currentUser = mAuth.getCurrentUser();

        if(currentUser == null){
            startLogin();
        }else {
            mUserRef.child("online").setValue("true");
        }

    }

    @Override
    protected void onStop() {
        super.onStop();

        FirebaseUser currentUser = mAuth.getCurrentUser();

        if(currentUser != null) {

            mUserRef.child("online").setValue("false");
            mUserRef.child("lastOnline").setValue(ServerValue.TIMESTAMP);

        }

    }


    private void startLogin(){
        Intent intentLogin = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intentLogin);
        finish();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
         super.onCreateOptionsMenu(menu);
         getMenuInflater().inflate(R.menu.main_menu,menu);
        for(int i = 0; i < menu.size(); i++) {
            MenuItem item = menu.getItem(i);
            SpannableString spanString = new SpannableString(menu.getItem(i).getTitle().toString());
            spanString.setSpan(new ForegroundColorSpan(Color.BLACK), 0,     spanString.length(), 0);
            item.setTitle(spanString);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
         super.onOptionsItemSelected(item);
            if(item.getItemId() == R.id.main_menu_logout){


                    mUserRef.child("online").setValue("false");
                    mUserRef.child("lastOnline").setValue(ServerValue.TIMESTAMP);


                FirebaseAuth.getInstance().signOut();
                startLogin();
            }

            if(item.getItemId()== R.id.main_menu_profile){
                Intent settingsIntent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(settingsIntent);
            }


        return true;
    }



}
