package com.oh.uidesigntest;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {
    public static Context context;
    FloatingActionButton fab;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    static public CircleImageView profile_cv;
    static public CircleImageView header;
    RecyclerView recyclerView;
    Recycler_Adapter adapter;
    ArrayList<Item>items=new ArrayList<>();
    Random random=new Random();
    int Number=1;
    int notifyID=2;
    int notifyID2=3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fullwindows();
        context=getApplicationContext();
        fab=findViewById(R.id.fab);
        fabonclick();
        header=findViewById(R.id.header_ic);
        profile_cv=findViewById(R.id.profile);
        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        drawerLayout=findViewById(R.id.drawer);
        navigationView=findViewById(R.id.navi);
        recyclerView=findViewById(R.id.recycler_main);
        navigationView.setItemIconTintList(null);
        header=navigationView.getHeaderView(0).findViewById(R.id.header_ic);
        header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,Getimg.class);
                startActivity(intent);
            }
        });
        SharedPreferences spf=getSharedPreferences("pic",MODE_PRIVATE);
        String pic=spf.getString("pic1",null);
        if (pic!=null){
            Glide.with(this).load(pic).into(header);
            Glide.with(this).load(pic).into(profile_cv);

        } else {
            header.setImageResource(R.drawable.emoticon_01);
            profile_cv.setImageResource(R.drawable.emoticon_01);
        }
        circleclick();

        items.add(new Item(R.drawable.emoticon_01,R.drawable.img01,"익명1","@_ic","야호"));
        adapter=new Recycler_Adapter(this,items);
        recyclerView.setAdapter(adapter);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.timeline:
                        break;
                    case R.id.news:
                        Intent intent=new Intent(MainActivity.this,News_Activity.class);
                        startActivity(intent);
                        break;
                }
                return true;
            }
        });
        adapter.notifyDataSetChanged();
        check();
    }

    public void fabonclick(){
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu=new PopupMenu(MainActivity.this,fab);
                popupMenu.getMenuInflater().inflate(R.menu.popup,popupMenu.getMenu());
                popupMenu.show();
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.wr:
                                int rndImg=random.nextInt(6);
                                String[] names={"익명1","익명2","익명3","익명4","익명5"};
                                String[] acc={"@acc","@mon","@joker","@gu","@monster"};
                                String[] tneyong={"Hi","Hello","안녕","おはようございます","こんにちは"};

                                int rndname=random.nextInt(names.length);
                                items.add(new Item(R.drawable.emoticon_01+rndImg,R.drawable.img01+rndImg,names[rndname],acc[rndname],tneyong[rndname]));
                                adapter.notifyDataSetChanged();
                                break;
                            case R.id.noti:
                                Alarmnotify();
                                break;
                        }
                        return true;
                    }
                });
            }
        });

    }
    public void circleclick(){
        profile_cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(navigationView);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }
    public void Alarmnotify(){
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            NotificationManager manager= (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            NotificationChannel channel=new NotificationChannel("channel1","채널이름",NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("channel description");
            channel.enableLights(true);
            channel.setLightColor(Color.RED);
            channel.enableVibration(true);
            channel.setShowBadge(true);
            channel.setVibrationPattern(new long[]{100,200,300,400});
            channel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
            manager.createNotificationChannel(channel);
            final Notification.Builder builder=new Notification.Builder(this,"channel1");
            PendingIntent pendingIntent=PendingIntent.getActivity(this,0,new Intent(this,MainActivity.class),PendingIntent.FLAG_UPDATE_CURRENT);
            builder.setContentTitle("알림");
            builder.setContentText("테스트");
            builder.setNumber(Number++);
            builder.setAutoCancel(true);
            builder.setSmallIcon(R.drawable.tuh);
            builder.setChannelId("channel1");
            builder.setContentIntent(pendingIntent);
            Uri notif=Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.persona5noti);
            Ringtone ringtone= RingtoneManager.getRingtone(getApplicationContext(),notif);
            ringtone.play();
            Notification notification=builder.build();
            manager.notify(notifyID,notification);

        }else {
            NotificationManager manager= (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            final Notification.Builder builder=new Notification.Builder(this);
            PendingIntent pendingIntent=PendingIntent.getActivity(this,0,new Intent(getApplicationContext(),MainActivity.class),PendingIntent.FLAG_UPDATE_CURRENT);
            builder.setVibrate(new long[]{100,200,300,400});
            builder.setContentText("테스트");
            builder.setContentTitle("알림");
            builder.setSmallIcon(R.drawable.tuh);
            builder.setAutoCancel(true);
            builder.setNumber(Number++);
            Uri notif=Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.persona5noti);
            Ringtone ringtone= RingtoneManager.getRingtone(getApplicationContext(),notif);
            ringtone.play();
            builder.setContentIntent(pendingIntent);
            Notification notification=builder.build();
            manager.notify(notifyID2,notification);
        }
    }
  private void check(){
        String state= Environment.getExternalStorageState();
        if (!state.equals(Environment.MEDIA_MOUNTED)){
            Toast.makeText(this, "Sd card is not munted", Toast.LENGTH_SHORT).show();
            return;
        }
      if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.N) {
          int checkSelfPermission = checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
          int checkW = checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE);
          int checkC = checkSelfPermission(Manifest.permission.CAMERA);
          int checkV = checkSelfPermission(Manifest.permission.VIBRATE);
          int checkI=checkSelfPermission(Manifest.permission.INTERNET);
          //퍼미션이 허용되어 있지 않다면
          if (checkSelfPermission == PackageManager.PERMISSION_DENIED) {
              //퍼미션을 요청하는 다이얼로그를 보여주는 메소드
              String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
              requestPermissions(permissions, 100);
          }

          if (checkW == PackageManager.PERMISSION_DENIED) {
              String[] permissionsW = {Manifest.permission.READ_EXTERNAL_STORAGE};
              requestPermissions(permissionsW, 101);
          }

          if (checkC == PackageManager.PERMISSION_DENIED) {
              String[] permissionsC = {Manifest.permission.CAMERA};
              requestPermissions(permissionsC, 103);
          }

          if (checkV == PackageManager.PERMISSION_DENIED) {
              String[] permissionsV = {Manifest.permission.VIBRATE};
              requestPermissions(permissionsV, 102);
          }
          if (checkI==PackageManager.PERMISSION_DENIED){
              String[] permissionsI={Manifest.permission.INTERNET};
              requestPermissions(permissionsI,104);
          }
      }
  }//check

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }
    public void fullwindows(){
        View decorView;
        int uiOption;
        decorView=getWindow().getDecorView();
        uiOption=getWindow().getDecorView().getSystemUiVisibility();
        if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH )
            uiOption |= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN )
            uiOption |= View.SYSTEM_UI_FLAG_FULLSCREEN;
        if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT )
            uiOption |= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        getWindow().getDecorView().setSystemUiVisibility(uiOption);
    }

}
