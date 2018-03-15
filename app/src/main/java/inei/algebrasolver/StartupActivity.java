package inei.algebrasolver;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import inei.algebrasolver.calculator.CalculatorActivity;

public class StartupActivity extends AppCompatActivity {

    Button algebraButton;
    Button calculatorButton;
    Button optionsButton;
    Button exitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startup);
    }


   /* public void startAlgebraActivity(View view) {
        algebraButton = (Button) findViewById(R.id.algebra);
        Intent algebra = new Intent(this, AlgebraActivity.class);
        startActivity(algebra);
    }
    */

    public void startCalculatorActivity(View view) {
        calculatorButton = (Button) findViewById(R.id.calculator);
        Intent calculator = new Intent(this, CalculatorActivity.class);
        startActivity(calculator);
    }

    public void startOptionsActivity(View view) {
        optionsButton = (Button) findViewById(R.id.options);
        Intent options = new Intent(this, OptionsActivity.class);
        startActivity(options);
    }

    public void startExitActivity(View view) {
        onDestroy();
    }
}
