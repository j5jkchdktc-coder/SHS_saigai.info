package jp.shsit.shsinfo2025.ui.sonaeru.screenshot;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.text.SpannableStringBuilder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import jp.shsit.shsinfo2025.MainActivity;
import jp.shsit.shsinfo2025.R;
import jp.shsit.shsinfo2025.hinan.MapsFragmenrt2;


public class ScreenShotFragment extends Fragment {
    private final static int RESULT_CAMERA = 1001;
    private ImageView imageView;
    private Uri cameraUri;

    private GridView gridView;
    private GridAdapter gridAdapter;
    private  Boolean pictureFlag = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_screen_shot, container, false);

        /*************言語選択*******************/
        String language= PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("lang", "日本語");
        MainActivity main = new MainActivity();
        /*****************************************/


   /*     final ImageButton cameraButton = view.findViewById(R.id.button22);

       if(language.equals("English")){
            cameraButton.setImageResource(R.drawable.save_en);
        }


        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ScreenShotIntent();
            }
        });
*/


        /** GridViewの取得 */
        gridView = (GridView) view.findViewById(R.id.gridView);

        /** 画像フォルダから画像パスの一覧を取得する。 */
        List<String> imgFilePaths = new ArrayList<String>();
        List<String> imgFileNames = new ArrayList<String>();
        File cFolder = getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        //val path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
        //val folder = File(path, "subDirectoryName")
        File[] imgFiles = new File(cFolder.toString()).listFiles();
        //初期値にcapture.jpegが設定されるために削除する
       // String fileN ="capture.jpeg";
      //  String fileName = String.format(fileN);
      //  File cameraFile = new File(cFolder, fileName);
      //  cameraFile.delete();
        for(File imgFile : imgFiles){
            String filename= imgFile.getName();
            if(filename.equals("CameraIntent1.jpg")||filename.equals("CameraIntent2.jpg")||filename.equals("CameraIntent3.jpg")||filename.equals("CameraIntent4.jpg")) {
                imgFilePaths.add(imgFile.getAbsolutePath());
                imgFileNames.add(filename);
            }
            Log.i("test",imgFile.getName());
        }

        /** アダプターをGridViewに設定。 */
        gridAdapter = new GridAdapter(getContext(), R.layout.grid_item, imgFilePaths);
        gridView.setAdapter(gridAdapter);


        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i("test","クリック押されました"+position);
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                // もどるボタンで戻ってこれるように
                transaction.addToBackStack(null);
                String name = imgFileNames.get(position);
                if(name.equals("CameraIntent1.jpg")) {
                    transaction.replace(R.id.nav_host_fragment, new java1());
                }else if(name.equals("CameraIntent2.jpg")) {
                    transaction.replace(R.id.nav_host_fragment, new java2());
                }else if(name.equals("CameraIntent3.jpg")) {
                    transaction.replace(R.id.nav_host_fragment, new java3());
                }else if(name.equals("CameraIntent4.jpg")) {
                    transaction.replace(R.id.nav_host_fragment, new java4());
                }


                transaction.commit();
            }
        });


        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

                new AlertDialog.Builder(getActivity())

                        .setTitle(main.LangReader("hinanSave",3,language))
                        .setMessage(main.LangReader("hinanSave",4,language))
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                                // 保存先のフォルダー
                                               File cFolder = getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                                                Log.i("test","ロングクリック押されました"+cFolder);
                                               // ファイル名
                                                String fileN = imgFileNames.get(position);
                                                //String fileN ="capture.jpeg";
                                                String fileName = String.format(fileN);
                                                File cameraFile = new File(cFolder, fileName);

                                                SharedPreferences prefer = getActivity().getPreferences(MODE_PRIVATE);

                                                SharedPreferences.Editor editor = prefer.edit();
                                                String filePass = "file"+(position+1);
                                                editor.putBoolean(filePass, false);
                                                editor.commit();


                                                cameraFile.delete();
                                                //リストの更新
                                                gridAdapter.notifyDataSetChanged();
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .show();


                //trueの場合、長押し後にイベントを発生させない
                return true;
            }
        });

        TextView tv = view.findViewById(R.id.textView22);
        //"避難所や避難所までのルートを画像として保存できます。\n" +
        //      "画像をタップすると、拡大することができます。\n" +
        //    "画像を長押しすると、削除できます。");
        String s1 = main.LangReader("hinanSave",0,language);
        String s2 = main.LangReader("hinanSave",1,language);
        SpannableStringBuilder sb = new SpannableStringBuilder();
        sb.append(s1);
        sb.append("\n");
        sb.append(s2);
        tv.setText(sb);

        //もどる
        Button modBtn = view.findViewById(R.id.mo1Button);
        modBtn.setText(main.LangReader("hinan",5,language));
        modBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentFragmentManager().popBackStack();
            }
        });


        return view;

    }

    private void ScreenShotIntent() {
        Context context = getContext();
        // 保存先のフォルダー
        File cFolder = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        Log.d("log", "path: " + String.valueOf(cFolder));

        boolean fileflag1 = PreferenceManager.getDefaultSharedPreferences(getContext()).getBoolean("file1",false);
        boolean fileflag2 = PreferenceManager.getDefaultSharedPreferences(getContext()).getBoolean("file2",false);
        boolean fileflag3 = PreferenceManager.getDefaultSharedPreferences(getContext()).getBoolean("file3",false);
        boolean fileflag4 = PreferenceManager.getDefaultSharedPreferences(getContext()).getBoolean("file4",false);

        String fileN = "CameraIntent1.jpg";


        pictureFlag = false;
        if (fileflag1 == false){
            fileN="CameraIntent1.jpg";

        }else if(fileflag2 == false){
            fileN="CameraIntent2.jpg";

        }else if(fileflag3 == false){
            fileN="CameraIntent3.jpg";

        }else if(fileflag4 == false){
            fileN="CameraIntent4.jpg";

        }
        else{
            pictureFlag = true;
        }
        if(pictureFlag == false) {
            // ファイル名
            String fileName = String.format(fileN);
            File cameraFile = new File(cFolder, fileName);
            cameraUri = FileProvider.getUriForFile(
                    context,
                    context.getPackageName() + ".fileprovider",
                    cameraFile);

            FragmentManager manager = getActivity().getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            /* もどるボタンで戻ってこれるように */
            transaction.addToBackStack(null);
            /*ファイル名を渡す*/
            MapsFragmenrt2 fragment = new MapsFragmenrt2();
            Bundle bundle = new Bundle();
            bundle.putString("name",fileN);
            fragment.setArguments(bundle);
            transaction.replace(R.id.nav_host_fragment, fragment);
            transaction.commit();

          //  Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
           // intent.putExtra(MediaStore.EXTRA_OUTPUT, cameraUri);
           // startActivityForResult(intent, RESULT_CAMERA);
        } else{
            /*************言語選択*******************/
            String language= PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("lang", "日本語");
            MainActivity main = new MainActivity();
            String s1 = main.LangReader("hinanSave",5,language);
            String s2 = main.LangReader("hinanSave",6,language);
            String s3 = main.LangReader("hinanSave",7,language);
            SpannableStringBuilder sb = new SpannableStringBuilder();
            sb.append(s1);
            sb.append("\n");
            sb.append(s2);
            sb.append("\n");
            sb.append(s3);
            Toast.makeText(getContext(),
                    sb,
                    Toast.LENGTH_LONG).show();
        }
    }
/*
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("test", resultCode+"中止"+requestCode+","+data);

        if ( requestCode == this.RESULT_CAMERA) {
            switch (resultCode) {
                case RESULT_OK:    //撮影完了
                    try {
                        Toast.makeText(getContext(),
                                "画像保存パス = " + cameraUri.getPath(),
                                Toast.LENGTH_LONG).show();
                        Fragment frg = null;
                        frg = getActivity().getSupportFragmentManager().findFragmentByTag("java22");
                        final FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                        ft.detach(frg);
                        ft.attach(frg);
                        ft.commit();
                        //}
                    } catch (NullPointerException err) {
                        //画像URIがnullだった時の処理
                    }


                    Log.d("test", "ok");
                    break;
                case RESULT_CANCELED:    //撮影が途中で中止
                    Toast.makeText(getContext(), "中止 " , Toast.LENGTH_LONG).show();
                    Log.d("test", "中止");
                    break;
                default:
                    break;
            }
        }

    }
*/
    /* Checks if external storage is available for read and write */
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        Log.i("test","koko" );
        return (Environment.MEDIA_MOUNTED.equals(state));

    }

    /* Checks if external storage is available to at least read */
    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        return (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state));
    }
}
