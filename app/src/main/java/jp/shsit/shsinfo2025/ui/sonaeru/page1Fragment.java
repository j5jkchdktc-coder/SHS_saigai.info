package jp.shsit.shsinfo2025.ui.sonaeru;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import jp.shsit.shsinfo2025.MainActivity;
import jp.shsit.shsinfo2025.R;

public class page1Fragment extends Fragment {
    int position;
    int csvIndex;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_page1, container, false);
        Bundle args = getArguments();
        if (args != null) {
            position = args.getInt("pos");
            csvIndex = args.getInt("csvIndex");
        }

        ImageView imageView = root.findViewById(R.id.imageView);
        TextView text = root.findViewById(R.id.textView2);
        TextView text2 = root.findViewById(R.id.textView3);
        TextView text3 = root.findViewById(R.id.textView4);

        Button moBtn = root.findViewById(R.id.mo2Button);
        moBtn.setOnClickListener(v -> getParentFragmentManager().popBackStack());

        String language = PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("lang", "日本語");
        MainActivity main = new MainActivity();

        // Common setup
        text2.setText("説明");
        String name = main.LangReader("mochi", csvIndex, language);
        text.setText(name);

        StringBuilder details = new StringBuilder();

        switch (position) {
            case 0:
                imageView.setImageResource(R.drawable.kaityuu);
                details.append(main.LangReader("mochi", 15, language));
                break;
            case 1:
                imageView.setImageResource(R.drawable.mobairu);
                details.append(main.LangReader("mochi", 16, language));
                break;
            case 2:
                imageView.setImageResource(R.drawable.denti2);
                details.append(main.LangReader("mochi", 17, language));
                break;
            case 3:
                imageView.setImageResource(R.drawable.radio);
                details.append(main.LangReader("mochi", 18, language));
                break;
            case 4:
                imageView.setImageResource(R.drawable.kityouhin);
                for (int i = 19; i <= 24; i++) {
                    details.append(main.LangReader("mochi", i, language)).append("\n");
                }
                break;
            case 5:
                imageView.setImageResource(R.drawable.fuku);
                details.append(main.LangReader("mochi", 25, language));
                break;
            case 6:
                imageView.setImageResource(R.drawable.hozon);
                for (int i = 26; i <= 30; i++) {
                    details.append(main.LangReader("mochi", i, language)).append("\n");
                }
                break;
            case 7:
                imageView.setImageResource(R.drawable.raita);
                details.append(main.LangReader("mochi", 31, language));
                break;
            case 8:
                imageView.setImageResource(R.drawable.mafuro);
                for (int i = 32; i <= 35; i++) {
                    details.append(main.LangReader("mochi", i, language)).append("\n");
                }
                break;
            case 9:
                imageView.setImageResource(R.drawable.kyuukyuu);
                details.append(main.LangReader("mochi", 36, language));
                break;
            case 10:
                imageView.setImageResource(R.drawable.keitai);
                details.append(main.LangReader("mochi", 37, language));
                break;
            case 11:
                imageView.setImageResource(R.drawable.kami);
                for (int i = 38; i <= 43; i++) {
                    details.append(main.LangReader("mochi", i, language)).append("\n");
                }
                break;
            case 12:
                imageView.setImageResource(R.drawable.pfudebako);
                details.append(main.LangReader("mochi", 44, language));
                break;
            case 13: // Portable Toilet
                imageView.setImageResource(R.drawable.kami);
                details.append(main.LangReader("mochi", 64, language));
                break;
            case 14: // Baby
                imageView.setImageResource(R.drawable.baby);
                for (int i = 45; i <= 49; i++) {
                    details.append(main.LangReader("mochi", i, language)).append("\n");
                }
                break;
            case 15: // Elderly
                imageView.setImageResource(R.drawable.noimage2);
                details.append(main.LangReader("mochi", 60, language)).append("\n\n");
                details.append(main.LangReader("mochi", 61, language));
                break;
            case 16: // Woman
                imageView.setImageResource(R.drawable.noimage2);
                details.append(main.LangReader("mochi", 62, language)).append("\n\n");
                details.append(main.LangReader("mochi", 63, language));
                break;
            case 17: // Pet
                imageView.setImageResource(R.drawable.inu);
                // Old dog items + new pet items
                for (int i = 50; i <= 53; i++) {
                    details.append(main.LangReader("mochi", i, language)).append("\n");
                }
                details.append(main.LangReader("mochi", 65, language)).append("\n\n");
                details.append(main.LangReader("mochi", 66, language));
                break;
            default:
                imageView.setImageResource(R.drawable.noimage2);
                details.append("Details not available.");
        }

        text3.setText(details.toString().trim());

        return root;
    }
}
