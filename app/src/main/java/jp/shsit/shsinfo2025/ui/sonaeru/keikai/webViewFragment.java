package jp.shsit.shsinfo2025.ui.sonaeru.keikai;

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

import jp.shsit.shsinfo2025.MainActivity;
import jp.shsit.shsinfo2025.R;
import jp.shsit.shsinfo2025.hazardmap.gpsFragmrnt;

public class webViewFragment extends Fragment {

    @Nullable
    @Override

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_webview, container, false);

        WebView webview = view.findViewById(R.id.webview1);
        webview.setWebViewClient(new WebViewClient());
        webview.getSettings().setJavaScriptEnabled(true);




        webview.requestFocus();
        //拡大縮小
        webview.getSettings().setBuiltInZoomControls(true);

        webview.setWebViewClient(new gpsFragmrnt.ViewClient(this.getContext(),"テータ取得中"));
        String language= PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("lang", "日本語");
        webview.loadUrl("https://www.jma.go.jp/jma/kishou/know/bosai/alertlevel.html");
        if(!language.equals("Japanese")){
            webview.loadUrl("https://www.bousai.go.jp/oukyu/hinanjouhou/r3_hinanjouhou_guideline/evacuation_en.html");
        }


        MainActivity main = new MainActivity();
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
}
