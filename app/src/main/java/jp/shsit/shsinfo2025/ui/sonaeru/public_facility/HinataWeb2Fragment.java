package jp.shsit.shsinfo2025.ui.sonaeru.public_facility;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.github.barteksc.pdfviewer.PDFView;

import jp.shsit.shsinfo2025.MainActivity;
import jp.shsit.shsinfo2025.R;

public class HinataWeb2Fragment extends Fragment {

    String pdf;
    public HinataWeb2Fragment(String pdf){
        this.pdf=pdf;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.hinata_web, container, false);

        ((PDFView)view.findViewById(R.id.pdfviewer)).fromAsset(this.pdf).load();


        /*************言語選択*******************/
        String language= PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("lang", "日本語");
        MainActivity main = new MainActivity();
        /*****************************************/
        Button bt1 = view.findViewById(R.id.back_btn);
        /*************言語選択*******************/
        bt1.setText(main.LangReader("hinan",5,language));
        /***********************************************************/
        bt1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });


        return view;
    }
    public static final class ViewClient extends WebViewClient {
        private ProgressDialog progressDialog;

        public ViewClient(Context context, String message){
            super();
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage(message);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            progressDialog.show();

        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            progressDialog.dismiss();




        }
    }
}
