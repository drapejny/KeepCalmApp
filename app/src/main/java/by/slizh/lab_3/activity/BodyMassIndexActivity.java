package by.slizh.lab_3.activity;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import by.slizh.lab_3.R;

public class BodyMassIndexActivity extends AppCompatActivity {

    private LinearLayout formulaLayout;

    private TextView
            weightTextView,
            heightTextView1,
            heightTextView2,
            imtTextView,
            verdictTextView;

    private void getRidOfTopBar() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
    }

    private void findFormulaLayout(){
        formulaLayout = findViewById(R.id.formulaLayout);
    }

    private void findAllTextViews(){
        weightTextView  = findViewById(R.id.weightTextView);
        heightTextView1 = findViewById(R.id.heightTextView1);
        heightTextView2 = findViewById(R.id.heightTextView2);
        imtTextView = findViewById(R.id.imtTextView);
        verdictTextView = findViewById(R.id.verdictTextView);
    }

    private String getVerdict(double imt){
        if (imt < 18) return "Сильный дефицит";
        if (imt >= 18 && imt < 21) return "Дефицит";
        if (imt >= 21 && imt < 24.5) return "Норма";
        if (imt >= 24.5 && imt < 26.5 ) return "Лишний вес";
        if (imt >= 26.5 && imt < 31.5) return "Ожирение";
        if (imt >= 31.5 && imt < 37) return "Сильное ожирение";
        if (imt >= 37) return "Нереально большое ожирение";
        return "Самфинг вронг";
    }

    private void fillAllTextViews(){
        int weight = MainActivity.CURR_USER_DB_INFO.WEIGHT;
        int height_cm = MainActivity.CURR_USER_DB_INFO.HEIGHT;

        String warning_message = "Для расчета необходимы данные:\n";
        boolean cannotCalculate = false;

        if (weight == -1){
            warning_message += "Вес\n";
            cannotCalculate = true;
        }

        if (height_cm == -1){
            warning_message += "Рост\n";
            cannotCalculate = true;
        }

        if (cannotCalculate){
            formulaLayout.setVisibility(View.GONE);
            verdictTextView.setText(warning_message);
        } else {
            double height_m = height_cm / 100.0;
            weightTextView.setText(String.valueOf(MainActivity.CURR_USER_DB_INFO.WEIGHT));
            heightTextView1.setText(String.valueOf(height_m));
            heightTextView2.setText(String.valueOf(height_m));

            double imt = weight / height_m / height_m;
            imt = Math.round(imt * 100) / 100.0;

            imtTextView.setText(String.valueOf(imt));
            verdictTextView.setText(getVerdict(imt));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getRidOfTopBar();

        setContentView(R.layout.activity_body_mass_index);

        findFormulaLayout();
        findAllTextViews();
        fillAllTextViews();
    }
}