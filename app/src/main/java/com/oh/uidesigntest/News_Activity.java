package com.oh.uidesigntest;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class News_Activity extends AppCompatActivity {
    ArrayList<News_item>items=new ArrayList<>();
    RecyclerView recyclerView;
    SwipeRefreshLayout refreshLayout;
    NewsitemAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_);
        recyclerView=findViewById(R.id.rv_news);
        adapter=new NewsitemAdapter(this,items);
        recyclerView.setAdapter(adapter);
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);
        readRss();
        refreshLayout=findViewById(R.id.isr);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                items.clear();
                readRss();
            }
        });
    }

    private void readRss() {
        try {
            URL url= new URL("http://rss.hankyung.com/new/news_industry.xml");
            RssFeedTask task=new RssFeedTask();
            task.execute(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }
    class RssFeedTask extends AsyncTask<URL,Void,String> {

        @Override
        protected String doInBackground(URL... urls) {
            URL url = urls[0];
            try {
                InputStream is = url.openStream();
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                XmlPullParser xpp = factory.newPullParser();
                xpp.setInput(is, "utf-8");   //euc-jp
                int eventType = xpp.next();
                News_item item = null;
                String tagname = null;
                while (eventType != XmlPullParser.END_DOCUMENT) {
                    eventType = xpp.next();
                    switch (eventType) {
                        case XmlPullParser.START_DOCUMENT:
                            break;
                        case XmlPullParser.START_TAG:
                            tagname = xpp.getName();
                            if (tagname.equals("item")) {
                                item = new News_item();
                            } else if (tagname.equals("title")) {
                                xpp.next();
                                if (item != null) {
                                    item.setTitle(xpp.getText());
                                }
                            } else if (tagname.equals("link")) {
                                xpp.next();
                                if (item != null) {
                                    item.setLink(xpp.getText());
                                }
                            } else if (tagname.equals("description")) {
                                xpp.next();
                                if (item != null) {
                                    item.setDesc(xpp.getText());
                                }
                            } else if (tagname.equals("image")) {
                                xpp.next();
                                if (item != null) {
                                    item.setImgUrl(xpp.getText());
                                }
                            } else if (tagname.equals("pubDate")) {
                                xpp.next();
                                if (item != null) {
                                    item.setDate(xpp.getText());
                                }
                            }
                            break;
                        case XmlPullParser.TEXT:
                            break;
                        case XmlPullParser.END_TAG:
                            tagname = xpp.getName();
                            if (tagname.equals("item")) {
                                //logcat에 기록 남기기
                                Log.i("Ab", item.getTitle());
                                items.add(item);

                                item = null;

                                //리사이클러뷰의 아답터에게 데이터가 갱신되었다는 걸 통지
                                publishProgress();//작업중 UI변경 요청

                            }
                            break;
                        case XmlPullParser.END_DOCUMENT:
                            break;
                    }
                }
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return "파싱 종료";
        }

            @Override
            protected void onProgressUpdate (Void...values){
                super.onProgressUpdate(values);
                //리사이클러뷰의 갱신요청...
                adapter.notifyDataSetChanged();
            }

            @Override
            protected void onPostExecute (String s){
                super.onPostExecute(s);
                //스와이프 리프레시 로딩 아이콘 모양 지우기
                refreshLayout.setRefreshing(false);
                Snackbar.make(recyclerView, s, Snackbar.LENGTH_SHORT).show();
            }
        }
    }

