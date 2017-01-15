package com.catholicuniversity.mydraw;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.ViewFlipper;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by ccw on 2016-12-25.
 */

public class DrawMainActivity extends AppCompatActivity implements View.OnTouchListener{
    private SingleTouchView drawView;
    int pos=0;
    private ImageButton currPaint;
    private ImageView LL;
    private LinearLayout    DrawerLinear;


    private final String[] navItems = {"당뇨", "감기", "암", "뇌출혈", "몰르겟다"};
    int gallery_grid_Images[]={R.drawable.ribs,R.drawable.eraser,R.drawable.left,
            R.drawable.new_pic, R.drawable.brush, R.drawable.save };
    private DrawerLayout dlDrawer;
    private ListView lvLeft;
    private GridView L_Right;
    private ListView lvRight;
    private FrameLayout flContainerLeft;
    private FrameLayout flContainerRight;
    private ImageButton btnLeft;
    private ImageButton btnRight;
    private LinearLayout mDrawer;
    private ViewFlipper viewFlipper;
    float xAtDown;
    float xAtUp;

    private File file=null;
    private File file2= null;
    private List myList;
    private File list[];
    ImageButton btn = null;
    ImageView iv = null;


// 그리드 뿌리기
    private int count;
    private Bitmap[] thumbnails;
    private boolean[] thumbnailsselection;
    private String[] arrPath;
    private gridAdapter GridAdapter;
    private ArrayList<String> f = new ArrayList<String>();// list of file paths //mylist 로한다
    File[] listFile;

    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;




    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw_main);
        setup();
        viewFlipper = (ViewFlipper) findViewById(R.id.flipper);
        for(int i=0;i<gallery_grid_Images.length;i++)
        {
            //  This will create dynamic image view and add them to ViewFlipper
            setFlipperImage(gallery_grid_Images[i]);
            drawView = (SingleTouchView) findViewById(R.id.drawing);
        }
        //drawView = (SingleTouchView) findViewById(R.id.drawing);
        LinearLayout paintLayout = (LinearLayout) findViewById(R.id.paint_colors);
        currPaint = (ImageButton) paintLayout.getChildAt(0);

        lvLeft = (ExpandableListView)findViewById(R.id.lv_left1);
        //L_Right =(GridView)findViewById(R.id.lv_left2);

        //lvRight = (ListView)drawView.findViewById(R.id.lv_right);

        flContainerLeft = (FrameLayout)findViewById(R.id.slideLeft);
        //flContainerRight = (FrameLayout)findViewById(R.id.slideRight);

        btnLeft = (ImageButton)findViewById(R.id.btnLeft);
        btnLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "open", Toast.LENGTH_SHORT).show();
                dlDrawer.openDrawer(mDrawer);
                mDrawer.bringToFront();
                mDrawer.requestLayout();
            }
        });

        btnRight = (ImageButton)findViewById(R.id.btnRight);
        btnRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "open", Toast.LENGTH_SHORT).show();
               // dlDrawer.openDrawer(lvRight);
            }

        //setContentView(R.layout.drawer);


        //DrawerLinear =(LinearLayout)findViewById(R.id.dl_activity_main_drawer2);

        //MainView mainView =new MainView(this);
        //setContentView(mainView);
    });

        dlDrawer = (DrawerLayout)findViewById(R.id.drawer_layout);
        mDrawer = (LinearLayout)findViewById((R.id.RL));
        //L_Right.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, navItems));


        Animation showIn= AnimationUtils.loadAnimation(this, android.R.anim.slide_in_left);
        viewFlipper.setInAnimation(showIn);
        viewFlipper.setOutAnimation(this, android.R.anim.slide_out_right);


        //lvRight.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, navItems));
        //lvRight.setOnItemClickListener(new DrawerItemClickListenerLeft());


         myList=new ArrayList<String>(); // 태블릿의 /sdcard/DCIM 폴더 내 파일들의 이름을 불러오는 코드

         String rootSD =  System.getenv("EXTERNAL_STORAGE").toString();
         file =new File(rootSD+"/DCIM");
         //file2 = new File(rootSD+"/DCIM/Camera");
         list = file.listFiles();

        for(int i=0;i<list.length; i++){
            myList.add(list[i].getName());
        }

        expListView = (ExpandableListView) findViewById(R.id.lv_left1);

        // preparing list data
        prepareListData();

        listAdapter = new ExpandableList(this, myList, listDataChild);

        // setting list adapter
        expListView.setAdapter(listAdapter);
        //lvLeft.setOnItemClickListener(new DrawerItemClickListenerLeft());
//android.R.layout.simple_list_item_1
        //lvLeft.setAdapter(new ExpandableList(this));// 왼쪽 드로우의 첫 번째 view에 폴더 이름 넣기
        /////// girdview////////
        expListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {
                Toast.makeText(getApplicationContext(),
                        listDataHeader.get(groupPosition) + " Expanded",
                        Toast.LENGTH_SHORT).show();
            }
        });

        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                Toast.makeText(
                        getApplicationContext(),
                        listDataHeader.get(groupPosition)
                                + " : "
                                + listDataChild.get(
                                listDataHeader.get(groupPosition)).get(
                                childPosition), Toast.LENGTH_SHORT)
                        .show();
                return false;
            }
        });

        // Listview Group collasped listener
        expListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {
                Toast.makeText(getApplicationContext(),
                        listDataHeader.get(groupPosition) + " Collapsed",
                        Toast.LENGTH_SHORT).show();

            }
        });

        // Listview on child click listener
        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                // TODO Auto-generated method stub
                Toast.makeText(
                        getApplicationContext(),
                        listDataHeader.get(groupPosition)
                                + " : "
                                + listDataChild.get(
                                listDataHeader.get(groupPosition)).get(
                                childPosition), Toast.LENGTH_SHORT)
                        .show();
                return false;
            }
        });

    }


    private void prepareListData() {
        myList=new ArrayList<String>(); // 태블릿의 /sdcard/DCIM 폴더 내 파일들의 이름을 불러오는 코드

        String rootSD =  System.getenv("EXTERNAL_STORAGE").toString();
        file =new File(rootSD+"/DCIM");
        //file2 = new File(rootSD+"/DCIM/Camera");
        list = file.listFiles();

        for(int i=0;i<list.length; i++){
            myList.add(list[i].getName());
        }

        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        // Adding child data
        listDataHeader.add("Top 250");
        listDataHeader.add("Now Showing");
        listDataHeader.add("Coming Soon..");

        // Adding child data
        List<String> top250 = new ArrayList<String>();
        top250.add("The Shawshank Redemption");
        top250.add("The Godfather");
        top250.add("The Godfather: Part II");
        top250.add("Pulp Fiction");
        top250.add("The Good, the Bad and the Ugly");
        top250.add("The Dark Knight");
        top250.add("12 Angry Men");

        List<String> nowShowing = new ArrayList<String>();
        nowShowing.add("The Conjuring");
        nowShowing.add("Despicable Me 2");
        nowShowing.add("Turbo");
        nowShowing.add("Grown Ups 2");
        nowShowing.add("Red 2");
        nowShowing.add("The Wolverine");

        List<String> comingSoon = new ArrayList<String>();
        comingSoon.add("2 Guns");
        comingSoon.add("The Smurfs 2");
        comingSoon.add("The Spectacular Now");
        comingSoon.add("The Canyons");
        comingSoon.add("Europa Report");

        listDataChild.put(listDataHeader.get(0), top250); // Header, Child data
        listDataChild.put(listDataHeader.get(1), nowShowing);
        listDataChild.put(listDataHeader.get(2), comingSoon);
    }

    // Listview Group expanded listener


public class gridAdapter extends BaseAdapter {
        LayoutInflater inflater;
    public gridAdapter(){
        inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public final int getCount(){
        return f.size();
    }

    public final Object getItem(int position){
        return position;
    }

    public final long getItemId(int position){
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent){
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.gallery_item, null);
            holder.imageview = (ImageView) convertView.findViewById(R.id.thumbImage);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }

        Bitmap myBitmap = BitmapFactory.decodeFile(f.get(position));
        holder.imageview.setImageBitmap(myBitmap);
        return convertView;
    }
    class ViewHolder {
        ImageView imageview;
    }

}

    private class DrawerItemClickListenerLeft implements ListView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {
            for(int i=0;i<list.length; i++){
                f.add(i,list[i].getAbsolutePath().toString());
            }
            switch (position) {
                case 0:
                    String path = f.get(0);
                    file2 = new File(path);
                    list = file2.listFiles();
                    //file2 = new File(f.get(0));
                    //File list[] = file.listFiles();
                    GridAdapter = new gridAdapter();
                    L_Right.setAdapter(GridAdapter);
                    break;
                case 1:
                    flContainerLeft.setBackgroundColor(Color.parseColor("#5F9EA0"));
                    break;
                case 2:
                    flContainerLeft.setBackgroundColor(Color.parseColor("#556B2F"));
                    break;
                case 3:
                    flContainerLeft.setBackgroundColor(Color.parseColor("#FF8C00"));
                    break;
                case 4:
                    flContainerLeft.setBackgroundColor(Color.parseColor("#DAA520"));
                    break;
            }
            //dlDrawer.closeDrawer(lvLeft);

        }
    }

    private class DrawerItemClickListenerRight implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {
            switch (position) {
                case 0:
                    flContainerRight.setBackgroundColor(Color.parseColor("#A52A2A"));
                    break;
                case 1:
                    flContainerRight.setBackgroundColor(Color.parseColor("#5F9EA0"));
                    break;
                case 2:
                    flContainerRight.setBackgroundColor(Color.parseColor("#556B2F"));
                    break;
                case 3:
                    flContainerRight.setBackgroundColor(Color.parseColor("#FF8C00"));
                    break;
                case 4:
                    flContainerRight.setBackgroundColor(Color.parseColor("#DAA520"));
                    break;
            }
            dlDrawer.closeDrawer(lvRight);

        }
    }
    private void setFlipperImage(int res) { // 이미지 저장 배열에 사진을 넣어주는 메서드
        //Log.i("Set Filpper Called", res+"");
        ImageView image = new ImageView(getApplicationContext());
        image.setBackgroundResource(res);
        viewFlipper.addView(image);
    }

        public boolean onTouch(View v, MotionEvent event)
        {
            if (v != viewFlipper)
            {return false;}

                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        xAtDown = event.getX(); // 터치 시작지점 x좌표 저장
                    }
                    else if (event.getAction() == MotionEvent.ACTION_UP) {
                        xAtUp = event.getX(); // 터치 끝난지점 x좌표 저장

                        if (xAtUp < xAtDown) {
                    // 왼쪽 방향 에니메이션 지정
                    viewFlipper.setInAnimation(AnimationUtils.loadAnimation(this,
                            R.anim.push_left_in));
                    viewFlipper.setOutAnimation(AnimationUtils.loadAnimation(this,
                            R.anim.push_left_out));

                    // 다음 view 보여줌
                    viewFlipper.showNext();
                } else if (xAtUp > xAtDown) {
                    // 오른쪽 방향 에니메이션 지정
                    viewFlipper.setInAnimation(AnimationUtils.loadAnimation(this,
                            R.anim.push_right_in));
                    viewFlipper.setOutAnimation(AnimationUtils.loadAnimation(this,
                            R.anim.push_right_out));
                    // 전 view 보여줌
                    viewFlipper.showPrevious();
                }
            }
            return true;
        }

    @Override
    public void onBackPressed() {
        if (dlDrawer.isDrawerOpen(lvLeft)) {
            dlDrawer.closeDrawer(lvLeft);
        } else {
            super.onBackPressed();
        }

    }

    public void clicked(View view) {
        if (view != currPaint) {
            String color = view.getTag().toString();
            this.drawView.setColor(color);
            currPaint = (ImageButton) view;
        }
    }

    public void mOnClick(View v){ //previous, next button 클릭시 실행

        switch( v.getId() ){
            case R.id.btn_previous:
                viewFlipper.showPrevious();//이전 View로 교체
                pos--;
                break;
            case R.id.btn_next:
                viewFlipper.showNext();//다음 View로 교체
                pos++;
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //iv = data.getData();
        iv.setImageURI(data.getData());
    }

    private void setup() {
        btn = (ImageButton) findViewById(R.id.new_btn);
        //iv = (ImageView) findViewById(R.id.iv);

        btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick (View v)
            {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
                startActivityForResult(intent,1);
                //Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                //startActivityForResult(intent,1);
            }
        });

    }

/*
    public void newImage(View view) {//갤러리 들어가는 기능
        if (view.getId() == R.id.new_btn) {
            drawView.doTakeAlbumAction();
        }
    }
*/

}
