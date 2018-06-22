package com.oh.uidesigntest;

import android.content.CursorLoader;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;

import com.bumptech.glide.Glide;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class Getimg extends AppCompatActivity {
    Uri photouri;
    String Photopath;
    String imgcapname;
    private final int GALLERY_CODE=1111;
    private final int CAMER_CODE=1112;
    CircleImageView ic;
    CircleImageView ci;
    SharedPreferences sp;
    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_getimg);

        ic=MainActivity.header;
        ci=MainActivity.profile_cv;
    }

    public void btnpic(View view) {
        selectPhoto();
    }

    public void btnalbum(View view) {
        selectGallery();
    }

    public void btncancel(View view) {
        finish();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return false;
    }
    private void selectPhoto(){
        String state=Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)){
            Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (intent.resolveActivity(getPackageManager())!=null){
                File photoFile=null;
                try {
                    photoFile=creatImgFile();
                }catch (Exception e){}
                if (photoFile!=null){
                    photouri= FileProvider.getUriForFile(this,"com.oh.uidesigntest",photoFile);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT,photouri);
                    startActivityForResult(intent,CAMER_CODE);
                }
            }
        }
    }//selectphoto
    private File creatImgFile() throws IOException{
        File dir=new File(Environment.getExternalStorageDirectory()+"/test/");
        if (!dir.exists()){
            dir.mkdirs();
        }
        String imgtime=new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        imgcapname=imgtime+".jpg";
        File storageDir=new File(Environment.getExternalStorageDirectory().getAbsoluteFile()+"/test/"+imgcapname);
        Intent intent=new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        intent.setData(Uri.parse("file://"+storageDir));
        sendBroadcast(intent);
        Photopath=storageDir.getAbsolutePath();
        return storageDir;
    }
    private void getPictureForPhoto(){
        sp=getSharedPreferences("pic",MODE_PRIVATE);
        editor=sp.edit();
        editor.putString("pic1",Photopath);
        editor.commit();

        String s=sp.getString("pic1",Photopath);
        Glide.with(MainActivity.context).load(s).into(ci);
        Glide.with(MainActivity.context).load(s).into(ic);
        finish();
    }

    private void selectGallery(){
        Intent intent=new Intent(Intent.ACTION_PICK);
        intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent,GALLERY_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK){
            switch (requestCode){
                case GALLERY_CODE:
                    sendPic(data.getData());
                    break;
                case CAMER_CODE:
                    getPictureForPhoto();
                    break;
            }
        }
    }//onActivityResult
    private void sendPic(Uri uri){
        String imgpath=getPathFromURI(uri);
        sp=getSharedPreferences("pic",MODE_PRIVATE);
        editor=sp.edit();
        editor.putString("pic1",imgpath);
        editor.commit();

        String s=sp.getString("pic1",imgpath);
        Glide.with(MainActivity.context).load(s).into(ci);
        Glide.with(MainActivity.context).load(s).into(ic);
        finish();
    }
    private String getPathFromURI(Uri contentUri){
        String[] proj={MediaStore.Images.Media.DATA};
        CursorLoader loader=new CursorLoader(this,contentUri,proj,null,null,null);
        Cursor cursor=loader.loadInBackground();
        int colum_index=cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result=cursor.getString(colum_index);
        cursor.close();
        return result;
    }


}
