package jp.shsit.shsinfo2025.AR;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.google.ar.core.Anchor;
import com.google.ar.core.ArCoreApk;
import com.google.ar.core.Pose;
import com.google.ar.core.Session;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.rendering.ViewRenderable;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.ar.sceneform.ux.TransformableNode;

import jp.shsit.shsinfo2025.R;
import jp.shsit.shsinfo2025.hinan.ListData2;

import static java.lang.Math.atan2;
import static java.lang.Math.cos;
import static java.lang.Math.sin;

public class AR_Fragment extends Fragment {

    ListData2[] list;

    private BroadcastReceiver mReceiver = null;
    private IntentFilter mIntentFilter = null;
    float genKakudo,hinanKakudo1,hinanKakudo2,hinanKakudo3,hinanKakudo4,hinanKakudo5;
    float hinanKyori1,hinanKyori2,hinanKyori3,hinanKyori4,hinanKyori5;

    private ViewRenderable textViewRenderable,textViewRenderable1,textViewRenderable2,textViewRenderable3,
            textViewRenderable4, textViewRenderable5, textViewRenderable6;
    TextView tv,tv1,tv2,tv3,tv4,tv5,tv6;
    ArFragment arFragment;
    boolean flag = false;

    LinearLayout linearLayout1,linearLayout11,linearLayout111;
    LinearLayout linearLayout2,linearLayout22,linearLayout222;
    LinearLayout linearLayout3,linearLayout33,linearLayout333;
    LinearLayout linearLayout4,linearLayout44,linearLayout444;
    LinearLayout linearLayout5,linearLayout55,linearLayout555;

    Double lat_current,lon_current;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.activity_main3, container, false);

        //ARcore利用できるか？チェック
        ArCoreApk.Availability availability= ArCoreApk.getInstance().checkAvailability(getContext());
        if(availability.isSupported()){
            Toast.makeText(getContext(),"AR機能が利用できます", Toast.LENGTH_LONG).show();
            // btn4.setEnabled(true);
        }else{
            Toast.makeText(getContext(),"AR機能を利用することができません", Toast.LENGTH_LONG).show();
            getFragmentManager().popBackStack();
            // btn4.setEnabled(false);
        }


        /**************配列の受け取り******************/
        //Mapsfragmrnt2から受け取り
        list = new ListData2[5];
        // 各要素ごとにインスタンス化
        for(int i = 0; i < 5; i++) {
            list[i] = new ListData2();
        }
        list= ( ListData2[])getArguments().getSerializable("INT_KEY");

        Log.i("list",list[1].getName());
        /*************************************************/

        lat_current = getArguments().getDouble("lat");
        lon_current = getArguments().getDouble("lng");



        //非同期処理の並列処理 //方角の取得
        new Direction(getContext()).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        /******Activityが持っているTextViewをService側で発生したデータで更新*****/
        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                // このonReceiveでMainServiceからのIntentを受信する。
                Bundle bundle = intent.getExtras();
                String direction = bundle.getString("message");
                genKakudo = Float.parseFloat(direction);
                if(genKakudo<0){
                    genKakudo += 360;
                }
                // TextViewへ文字列をセット
                TextView tv = (TextView)view.findViewById(R.id.act3_text1);
                tv.setText(direction);
            }
        };
        // "direction" Intentフィルターをセット
        mIntentFilter = new IntentFilter();
        mIntentFilter.addAction("direction");
        getActivity().registerReceiver(mReceiver, mIntentFilter);

        Intent intent1 = new Intent(getActivity(), Direction.class);
        getActivity().startService(intent1);


        /*******************************************************************/
        /*****AR関係**************************************************************/
        //AR_init();



        ImageButton btn1 = (ImageButton)view.findViewById(R.id.imagebtn1);
        btn1.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                Log.i("test","押された");


                AR_etc();
                if(flag==true){
                    flag=false;
                    btn1.setImageResource(R.drawable.play_button);
                }else{
                    flag=true;
                    btn1.setImageResource(R.drawable.reload_button);
                }

            }
        });
        AR_init();
        dis_bear();


        Button modoru =(Button)view.findViewById(R.id.modorubtn);
        modoru.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });

        return view;
    }

    //球面三角法
    public double  distance(double lat1, double lon1, double lat2, double lon2) {
        double radLat1 = Math.toRadians(lat1);
        double radLon1 = Math.toRadians(lon1);
        double radLat2 = Math.toRadians(lat2);
        double radLon2 = Math.toRadians(lon2);
        double r = 6378137.0; // equatorial radius
        double averageLat = (radLat1 - radLat2) / 2;
        double averageLon = (radLon1 - radLon2) / 2;
        return 2 * r * Math.asin(Math.sqrt(sin(averageLat)*sin(averageLat) + cos(radLat1) * cos(radLat2) * sin(averageLon)*sin(averageLon)));
    }
    //方角を計算　現在地->lat1,lon1 目的地->lat2,lon2
    public double bearing(double lat1, double lon1, double lat2, double lon2){
        double radLat1 = Math.toRadians(lat1);
        double  radLat2 = Math.toRadians(lat2);
        double diffLon = Math.toRadians(lon2 - lon1);
        double  x = cos(radLat1) * sin(radLat2) - sin(radLat1) * cos(radLat2) * cos(diffLon);
        double y = cos(radLat2) * sin(diffLon);
        return (Math.toDegrees(atan2(y, x)) + 360) % 360;
    }

    public void dis_bear(){
        for(int i=0;i<5;i++) {
            double lat1 = Double.parseDouble(list[i].getLat());
            double long1 = Double.parseDouble(list[i].getLon());
            double kyori = distance(lat_current, lon_current, lat1, long1);
            double kakudo=bearing(lat_current, lon_current, lat1, long1);
            if(i==0) {
                hinanKyori1 = (float) kyori;
                hinanKakudo1=(float)kakudo;
                Log.i("test","kokoko"+hinanKakudo1+","+genKakudo );
            }
            else if(i==1){
                hinanKyori2=(float)kyori;
                hinanKakudo2=(float)kakudo;
            }
            else if(i==2){
                hinanKyori3=(float)kyori;
                hinanKakudo3=(float)kakudo;
            }
            else if(i==3){
                hinanKyori4=(float)kyori;
                hinanKakudo4=(float)kakudo;
            }
            else if(i==4){
                hinanKyori5=(float)kyori;
                hinanKakudo5=(float)kakudo;
            }
        }
    }
    /****AR関係の初期設定*********************************/
    public void AR_init(){
        tv = new TextView(getContext());
        tv.setTextColor(Color.rgb(0x0,0x0,0xaa));
        tv.setBackgroundColor(Color.rgb(0xff,0xff,0xff));
        tv.setText("基準の位置");

        tv1 = new TextView(getContext());
        tv1.setTextColor(Color.rgb(0x0,0xff,0x00));
        tv1.setBackgroundColor(Color.rgb(0xff,0x00,0x00));
        tv1.setText("北");

        tv2 = new TextView(getContext());
        tv2.setTextColor(Color.rgb(0xff,0xff,0xff));
        //tv2.setBackgroundColor(Color.rgb(0x00,0xff,0x00));
        tv2.setTextSize(30.0f);

        tv3 = new TextView(getContext());
        tv3.setTextColor(Color.rgb(0xff,0xff,0xff));
        //tv3.setBackgroundColor(Color.rgb(0x00,0xff,0x00));
        tv3.setTextSize(30.0f);

        tv4 = new TextView(getContext());
        tv4.setTextColor(Color.rgb(0xff,0xff,0xff));
        //tv4.setBackgroundColor(Color.rgb(0x00,0xff,0x00));
        tv4.setTextSize(30.0f);

        tv5 = new TextView(getContext());
        tv5.setTextColor(Color.rgb(0xff,0xff,0xff));
        //tv5.setBackgroundColor(Color.rgb(0x00,0xff,0x00));
        tv5.setTextSize(30.0f);

        tv6 = new TextView(getContext());
        tv6.setTextColor(Color.rgb(0xff,0xff,0xff));
        //tv6.setBackgroundColor(Color.rgb(0x00,0xff,0x00));
        tv6.setTextSize(30.0f);

        arFragment = (ArFragment)getChildFragmentManager().findFragmentById(R.id.ar_fragment);
                //getSupportFragmentManager().findFragmentById(R.id.ar_fragment);

        //標識の作成
        marker();


    }
    private void AR_etc() {

        Session session = arFragment.getArSceneView().getSession();
        if (flag == true) {
            //session.close();
            getFragmentManager().popBackStack();
        } else {

            float[] pos = {0, -0.2f, 0.0f};
            float[] rotation = {0, 0, 0, 1};
            Anchor anchor = session.createAnchor(new Pose(pos, rotation));
            AnchorNode anchorNode = new AnchorNode(anchor);
            anchorNode.setParent(arFragment.getArSceneView().getScene());
            TransformableNode model = new TransformableNode(arFragment.getTransformationSystem());
            model.setRenderable(textViewRenderable);
            model.setParent(anchorNode);
            model.select();
            //2m先　高さは-40cm
           /*float[] pos1 = {0, -0.2f, -2.0f};
            float[] rotation1 = {0, 0, 0, 1};
            anchor = session.createAnchor(new Pose(pos1, rotation1));
            anchorNode = new AnchorNode(anchor);
            anchorNode.setParent(arFragment.getArSceneView().getScene());
            model = new TransformableNode(arFragment.getTransformationSystem());
            model.setRenderable(textViewRenderable1);
            model.setParent(anchorNode);
            model.select();*/
            float z1 = 0.0f, x1 = 0.0f;
            float kaku1 = 0.0f;

            z1 = (float) (1 * cos(Math.toRadians(0 - genKakudo)));
            x1 = (float) (1 * sin(Math.toRadians(0 - genKakudo)));
            kaku1 = genKakudo - 0;

            float[] pos1 = {x1, 0.0f, -(z1)};
            float[] rotation1 = {0, (float) sin(Math.toRadians(kaku1 / 2.0)), 0, (float) cos(Math.toRadians(kaku1 / 2.0))};
            anchor = session.createAnchor(new Pose(pos1, rotation1));
            anchorNode = new AnchorNode(anchor);
            anchorNode.setParent(arFragment.getArSceneView().getScene());
            model = new TransformableNode(arFragment.getTransformationSystem());
            model.setRenderable(textViewRenderable1);
            model.setParent(anchorNode);
            model.select();


            //2m後　高さは＋40cm

            for (int i = 0; i < 5; i++) {
                String name = "", kyori = "";

                 if (i == 0) {
                    name = list[0].getName();
                    kyori = String.valueOf(hinanKyori1);
                    z1 = (float) (3 * cos(Math.toRadians(hinanKakudo1 - genKakudo)));
                    x1 = (float) (3 * sin(Math.toRadians(hinanKakudo1 - genKakudo)));
                    kaku1 = genKakudo - hinanKakudo1;
                    Log.i("test", name + kyori + "," + hinanKakudo1 + "," + genKakudo);
                    tv2.setText(name + kyori + "[m]");
                    float[] pos2 = {x1, 0.0f, -(z1)};
                    float[] rotation2 = {0, (float) sin(Math.toRadians(kaku1 / 2.0)), 0, (float) cos(Math.toRadians(kaku1 / 2.0))};
                    anchor = session.createAnchor(new Pose(pos2, rotation2));
                    anchorNode = new AnchorNode(anchor);
                    anchorNode.setParent(arFragment.getArSceneView().getScene());
                    model = new TransformableNode(arFragment.getTransformationSystem());
                    model.setRenderable(textViewRenderable2);
                    model.setParent(anchorNode);
                    model.select();
                } else if (i == 1) {
                    name = list[1].getName();
                    kyori = String.valueOf(hinanKyori2);
                    z1 = (float) (3.5 * cos(Math.toRadians(hinanKakudo2 - genKakudo)));
                    x1 = (float) (3.5 * sin(Math.toRadians(hinanKakudo2 - genKakudo)));
                    kaku1 = genKakudo - hinanKakudo2;
                    Log.i("test", name + kyori + "2");
                    tv3.setText(name + kyori + "[m]");
                    float[] pos2 = {x1, 0.5f, -(z1)};
                    float[] rotation2 = {0, (float) sin(Math.toRadians(kaku1 / 2.0)), 0, (float) cos(Math.toRadians(kaku1 / 2.0))};
                    anchor = session.createAnchor(new Pose(pos2, rotation2));
                    anchorNode = new AnchorNode(anchor);
                    anchorNode.setParent(arFragment.getArSceneView().getScene());
                    model = new TransformableNode(arFragment.getTransformationSystem());
                    model.setRenderable(textViewRenderable3);
                    model.setParent(anchorNode);
                    model.select();
                } else if (i == 2) {
                    name = list[2].getName();
                    kyori = String.valueOf(hinanKyori3);
                    z1 = (float) (4 * cos(Math.toRadians(hinanKakudo3 - genKakudo)));
                    x1 = (float) (4 * sin(Math.toRadians(hinanKakudo3 - genKakudo)));
                    kaku1 = genKakudo - hinanKakudo3;
                    Log.i("test", name + kyori + "3");
                    tv4.setText(name + kyori + "[m]");
                    float[] pos2 = {x1, 1.5f, -(z1)};
                    float[] rotation2 = {0, (float) sin(Math.toRadians(kaku1 / 2.0)), 0, (float) cos(Math.toRadians(kaku1 / 2.0))};
                    anchor = session.createAnchor(new Pose(pos2, rotation2));
                    anchorNode = new AnchorNode(anchor);
                    anchorNode.setParent(arFragment.getArSceneView().getScene());
                    model = new TransformableNode(arFragment.getTransformationSystem());
                    model.setRenderable(textViewRenderable4);
                    model.setParent(anchorNode);
                    model.select();
                } else if (i == 3) {
                    name = list[3].getName();
                    kyori = String.valueOf(hinanKyori4);
                    z1 = (float) (4.5 * cos(Math.toRadians(hinanKakudo4 - genKakudo)));
                    x1 = (float) (4.5 * sin(Math.toRadians(hinanKakudo4 - genKakudo)));
                    kaku1 = genKakudo - hinanKakudo4;
                    Log.i("test", name + kyori + "4");
                    tv5.setText(name + kyori + "[m]");
                    float[] pos2 = {x1, 2.0f, -(z1)};
                    float[] rotation2 = {0, (float) sin(Math.toRadians(kaku1 / 2.0)), 0, (float) cos(Math.toRadians(kaku1 / 2.0))};
                    anchor = session.createAnchor(new Pose(pos2, rotation2));
                    anchorNode = new AnchorNode(anchor);
                    anchorNode.setParent(arFragment.getArSceneView().getScene());
                    model = new TransformableNode(arFragment.getTransformationSystem());
                    model.setRenderable(textViewRenderable5);
                    model.setParent(anchorNode);
                    model.select();
                } else if (i == 4) {
                    name = list[4].getName();
                    kyori = String.valueOf(hinanKyori5);
                    z1 = (float) (5 * cos(Math.toRadians(hinanKakudo5 - genKakudo)));
                    x1 = (float) (5 * sin(Math.toRadians(hinanKakudo5 - genKakudo)));
                    kaku1 = genKakudo - hinanKakudo5;
                    Log.i("test", name + kyori + "5");
                    tv6.setText(name + kyori + "[m]");
                    float[] pos2 = {x1, 2.5f, -(z1)};
                    float[] rotation2 = {0, (float) sin(Math.toRadians(kaku1 / 2.0)), 0, (float) cos(Math.toRadians(kaku1 / 2.0))};
                    anchor = session.createAnchor(new Pose(pos2, rotation2));
                    anchorNode = new AnchorNode(anchor);
                    anchorNode.setParent(arFragment.getArSceneView().getScene());
                    model = new TransformableNode(arFragment.getTransformationSystem());
                    model.setRenderable(textViewRenderable6);
                    model.setParent(anchorNode);
                    model.select();
                }

            }
        }

    }
        public void reload() {
            //Intent intent = getIntent();
            //overridePendingTransition(0, 0);
            //intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            getActivity().finish();
          //  overridePendingTransition(0, 0);
            startActivity(getActivity().getIntent());

        }

        public void marker(){
            /******************************************************/
            linearLayout1 = new LinearLayout(getContext());
            linearLayout1.setOrientation(LinearLayout.VERTICAL);
            linearLayout11 = new LinearLayout(getContext());
            linearLayout11.setOrientation(LinearLayout.HORIZONTAL);
            linearLayout111= new LinearLayout(getContext());
            linearLayout111.setOrientation(LinearLayout.HORIZONTAL);

            ImageView imageView11 = new ImageView(getContext());
            imageView11.setImageResource(R.drawable.jishin2);
            ImageView imageView12 = new ImageView(getContext());
            imageView12.setImageResource(R.drawable.kouzui);
            ImageView imageView13 = new ImageView(getContext());
            imageView13.setImageResource(R.drawable.tunami);
            ImageView imageView14 = new ImageView(getContext());
            imageView14.setImageResource(R.drawable.jyo2);
            Log.i("test",list[0].getName()+","+list[0].getJishin()+list[1].getName()+","+list[1].getJishin());
            if(list[0].getJishin().equals("1")) {
                linearLayout11.addView(imageView11, new LinearLayout.LayoutParams(100, 100));
            }
            if(list[0].getKouzui().equals("1")) {
                linearLayout11.addView(imageView12, new LinearLayout.LayoutParams(100, 100));
            }
            if(list[0].getTsunami().equals("1")) {
                linearLayout11.addView(imageView13, new LinearLayout.LayoutParams(100, 100));
            }
            if(list[0].getJyou().equals("1")) {
                linearLayout11.addView(imageView14, new LinearLayout.LayoutParams(200, 100));
            }
            linearLayout1.setBackgroundColor(Color.rgb(25,170,7));
            linearLayout1.setPadding(20,20,20,20);

            //白の枠線を引く
            linearLayout111.setBackgroundColor(Color.WHITE);
            linearLayout111.setPadding(5,5,5,5);

            linearLayout1.addView(linearLayout11,new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            linearLayout1.addView(tv2,new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            linearLayout111.addView(linearLayout1,new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            /******************************************************/

            /******************************************************/
            linearLayout2 = new LinearLayout(getContext());
            linearLayout2.setOrientation(LinearLayout.VERTICAL);
            linearLayout22 = new LinearLayout(getContext());
            linearLayout22.setOrientation(LinearLayout.HORIZONTAL);
            linearLayout222= new LinearLayout(getContext());
            linearLayout222.setOrientation(LinearLayout.HORIZONTAL);

            ImageView imageView21 = new ImageView(getContext());
            imageView21.setImageResource(R.drawable.jishin2);
            ImageView imageView22 = new ImageView(getContext());
            imageView22.setImageResource(R.drawable.kouzui);
            ImageView imageView23 = new ImageView(getContext());
            imageView23.setImageResource(R.drawable.tunami);
            ImageView imageView24 = new ImageView(getContext());
            imageView24.setImageResource(R.drawable.jyo2);

            if(list[1].getJishin().equals("1")) {
                linearLayout22.addView(imageView21, new LinearLayout.LayoutParams(   100, 100));
            }
            if(list[1].getKouzui().equals("1")) {
                linearLayout22.addView(imageView22, new LinearLayout.LayoutParams(100, 100));
            }
            if(list[1].getTsunami().equals("1")) {
                linearLayout22.addView(imageView23, new LinearLayout.LayoutParams(100, 100));
            }
            if(list[1].getJyou().equals("1")) {
                linearLayout22.addView(imageView24, new LinearLayout.LayoutParams(200, 100));
            }

            linearLayout2.setBackgroundColor(Color.rgb(25,170,7));
            linearLayout2.setPadding(20,20,20,20);

            //白の枠線を引く
            linearLayout222.setBackgroundColor(Color.WHITE);
            linearLayout222.setPadding(5,5,5,5);

            linearLayout2.addView(linearLayout22,new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            linearLayout2.addView(tv3,new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            linearLayout222.addView(linearLayout2,new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            /******************************************************/
            /******************************************************/
            linearLayout3 = new LinearLayout(getContext());
            linearLayout3.setOrientation(LinearLayout.VERTICAL);
            linearLayout33 = new LinearLayout(getContext());
            linearLayout33.setOrientation(LinearLayout.HORIZONTAL);
            linearLayout333 = new LinearLayout(getContext());
            linearLayout333.setOrientation(LinearLayout.HORIZONTAL);

            ImageView imageView31 = new ImageView(getContext());
            imageView31.setImageResource(R.drawable.jishin2);
            ImageView imageView32 = new ImageView(getContext());
            imageView32.setImageResource(R.drawable.kouzui);
            ImageView imageView33 = new ImageView(getContext());
            imageView33.setImageResource(R.drawable.tunami);
            ImageView imageView34 = new ImageView(getContext());
            imageView34.setImageResource(R.drawable.jyo2);
            if(list[2].getJishin().equals("1")) {
                linearLayout33.addView(imageView31, new LinearLayout.LayoutParams(   100, 100));
            }
            if(list[2].getKouzui().equals("1")) {
                linearLayout33.addView(imageView32, new LinearLayout.LayoutParams(100, 100));
            }
            if(list[2].getTsunami().equals("1")) {
                linearLayout33.addView(imageView33, new LinearLayout.LayoutParams(100, 100));
            }
            if(list[2].getJyou().equals("1")) {
                linearLayout33.addView(imageView34, new LinearLayout.LayoutParams(200, 100));
            }
            linearLayout3.setBackgroundColor(Color.rgb(25,170,7));
            linearLayout3.setPadding(20,20,20,20);

            //白の枠線を引く
            linearLayout333.setBackgroundColor(Color.WHITE);
            linearLayout333.setPadding(5,5,5,5);

            linearLayout3.addView(linearLayout33,new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            linearLayout3.addView(tv4,new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            linearLayout333.addView(linearLayout3,new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            /******************************************************/
            /******************************************************/
            linearLayout4 = new LinearLayout(getContext());
            linearLayout4.setOrientation(LinearLayout.VERTICAL);
            linearLayout44 = new LinearLayout(getContext());
            linearLayout44.setOrientation(LinearLayout.HORIZONTAL);
            linearLayout444 = new LinearLayout(getContext());
            linearLayout444.setOrientation(LinearLayout.HORIZONTAL);

            ImageView imageView41 = new ImageView(getContext());
            imageView41.setImageResource(R.drawable.jishin2);
            ImageView imageView42 = new ImageView(getContext());
            imageView42.setImageResource(R.drawable.kouzui);
            ImageView imageView43 = new ImageView(getContext());
            imageView43.setImageResource(R.drawable.tunami);
            ImageView imageView44 = new ImageView(getContext());
            imageView44.setImageResource(R.drawable.jyo2);
            if(list[3].getJishin().equals("1")) {
                linearLayout44.addView(imageView41, new LinearLayout.LayoutParams(   100, 100));
            }
            if(list[3].getKouzui().equals("1")) {
                linearLayout44.addView(imageView42, new LinearLayout.LayoutParams(100, 100));
            }
            if(list[3].getTsunami().equals("1")) {
                linearLayout44.addView(imageView43, new LinearLayout.LayoutParams(100, 100));
            }
            if(list[3].getJyou().equals("1")) {
                linearLayout44.addView(imageView44, new LinearLayout.LayoutParams(200, 100));
            }
            linearLayout4.setBackgroundColor(Color.rgb(25,170,7));
            linearLayout4.setPadding(20,20,20,20);

            //白の枠線を引く
            linearLayout444.setBackgroundColor(Color.WHITE);
            linearLayout444.setPadding(5,5,5,5);

            linearLayout4.addView(linearLayout44,new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            linearLayout4.addView(tv5,new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            linearLayout444.addView(linearLayout4,new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            /******************************************************/
            /******************************************************/
            linearLayout5 = new LinearLayout(getContext());
            linearLayout5.setOrientation(LinearLayout.VERTICAL);
            linearLayout55 = new LinearLayout(getContext());
            linearLayout55.setOrientation(LinearLayout.HORIZONTAL);
            linearLayout555 = new LinearLayout(getContext());
            linearLayout555.setOrientation(LinearLayout.HORIZONTAL);

            ImageView imageView51 = new ImageView(getContext());
            imageView51.setImageResource(R.drawable.jishin2);
            ImageView imageView52 = new ImageView(getContext());
            imageView52.setImageResource(R.drawable.kouzui);
            ImageView imageView53 = new ImageView(getContext());
            imageView53.setImageResource(R.drawable.tunami);
            ImageView imageView54 = new ImageView(getContext());
            imageView54.setImageResource(R.drawable.jyo2);
            if(list[4].getJishin().equals("1")) {
                linearLayout55.addView(imageView51, new LinearLayout.LayoutParams(   100, 100));
            }
            if(list[4].getKouzui().equals("1")) {
                linearLayout55.addView(imageView52, new LinearLayout.LayoutParams(100, 100));
            }
            if(list[4].getTsunami().equals("1")) {
                linearLayout55.addView(imageView53, new LinearLayout.LayoutParams(100, 100));
            }
            if(list[4].getJyou().equals("1")) {
                linearLayout55.addView(imageView54, new LinearLayout.LayoutParams(200, 100));
            }
            linearLayout5.setBackgroundColor(Color.rgb(25,170,7));
            linearLayout5.setPadding(20,20,20,20);

            //白の枠線を引く
            linearLayout555.setBackgroundColor(Color.WHITE);
            linearLayout555.setPadding(5,5,5,5);

            linearLayout5.addView(linearLayout55,new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            linearLayout5.addView(tv6,new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            linearLayout555.addView(linearLayout5,new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            /******************************************************/

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                //現在地
                ViewRenderable.builder().setView(getContext(), tv).build().thenAccept(renderable -> textViewRenderable = renderable);
                //現在地から１m
                ViewRenderable.builder().setView(getContext(), tv1).build().thenAccept(renderable -> textViewRenderable1 = renderable);
                ViewRenderable.builder().setView(getContext(), linearLayout111).build().thenAccept(renderable -> textViewRenderable2 = renderable);
                ViewRenderable.builder().setView(getContext(), linearLayout222).build().thenAccept(renderable -> textViewRenderable3 = renderable);
                ViewRenderable.builder().setView(getContext(), linearLayout333).build().thenAccept(renderable -> textViewRenderable4 = renderable);
                ViewRenderable.builder().setView(getContext(), linearLayout444).build().thenAccept(renderable -> textViewRenderable5 = renderable);
                ViewRenderable.builder().setView(getContext(), linearLayout555).build().thenAccept(renderable -> textViewRenderable6 = renderable);
            }


        }

}
