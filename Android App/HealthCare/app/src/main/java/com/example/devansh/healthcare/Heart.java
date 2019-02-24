package com.example.devansh.healthcare;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class Heart extends AppCompatActivity {

    public EditText age, restbp, chol, thalach, oldpeak, ca;
    public RadioGroup sex, cp, fbs, restecg, exang, slope, thal;
    public TextView result, show_accuracy, cvd;
    public Button predict, reset;

    //public String handshakeAddress = "https://intense-citadel-71493.herokuapp.com/";
    public String PredictionAddress = "https://intense-citadel-71493.herokuapp.com/heart/predict/";

    public String params;
    RequestQueue queue;

    //return params
    String generateParams(){

        String sex_value, cp_value, fbs_value, restecg_value, exang_vlue, slope_value, thal_value,t1;

        String age_value = age.getText().toString();

        sex_value = ((((RadioButton)findViewById(sex.getCheckedRadioButtonId())).getText().toString()).equals("Male") ? "1" : "0");

        t1 = ((RadioButton)findViewById(cp.getCheckedRadioButtonId())).getText().toString();
        cp_value = (t1.equals("Asymptomatic"))?"0":(t1.equals("Atypical Angina"))?"1":(t1.equals("Non-Anginal Pain"))?"2":"3";

        String restbp_value = restbp.getText().toString();

        String chol_value = chol.getText().toString();

        fbs_value = ((((RadioButton)findViewById(fbs.getCheckedRadioButtonId())).getText().toString()).equals("Yes") ? "1" : "0");

        t1 = ((RadioButton)findViewById(restecg.getCheckedRadioButtonId())).getText().toString();
        restecg_value = (t1.equals("Type 0"))?"0":(t1.equals("Type 1"))?"1": "2";

        String thalach_value = thalach.getText().toString();

        exang_vlue = ((((RadioButton)findViewById(exang.getCheckedRadioButtonId())).getText().toString()).equals("Yes") ? "1" : "0");

        String oldpeak_value = oldpeak.getText().toString();

        t1 = ((RadioButton)findViewById(slope.getCheckedRadioButtonId())).getText().toString();
        slope_value = (t1.equals("Downsloping"))?"0":(t1.equals("Flat"))?"1": "2";

        String ca_value = ca.getText().toString();

        t1 = ((RadioButton)findViewById(thal.getCheckedRadioButtonId())).getText().toString();
        thal_value = (t1.equals("Unknown"))?"0":(t1.equals("Fixed Defect"))?"1":(t1.equals("Normal"))?"2":"3";

        //returning..
        if(age_value.equals("") || restbp_value.equals("") || chol_value.equals("") || thalach_value.equals("") || oldpeak_value.equals("") || ca_value.equals(""))
            return "error";
        else
            return (age_value + "-" + sex_value + "-" + cp_value + "-" + restbp_value + "-" + chol_value + "-" + fbs_value + "-" + restecg_value + "-" + thalach_value + "-" + exang_vlue + "-" + oldpeak_value + "-" + slope_value + "-" + ca_value + "-" + thal_value);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_heart);

        /*
        * initialise features
        * init request queue
        * handshake
        * add in queue
        * click event
        * */

        //Features
        age = (EditText)findViewById(R.id.ed_age);
        sex = (RadioGroup)findViewById(R.id.sex);
        cp = (RadioGroup)findViewById(R.id.cp);
        restbp = (EditText)findViewById(R.id.ed_restbp);
        chol = (EditText)findViewById(R.id.ed_chol);
        fbs = (RadioGroup)findViewById(R.id.fbs);
        restecg = (RadioGroup)findViewById(R.id.restecg);
        thalach = (EditText)findViewById(R.id.ed_thalach);
        exang = (RadioGroup)findViewById(R.id.exang);
        oldpeak = (EditText)findViewById(R.id.ed_oldpeak);
        slope = (RadioGroup)findViewById(R.id.slope);
        ca = (EditText)findViewById(R.id.ed_ca);
        thal = (RadioGroup)findViewById(R.id.thal);

        //Buttons
        predict = (Button)findViewById(R.id.predictbtn);
        reset = (Button)findViewById(R.id.resetbtn);

        //result show
        result = (TextView)findViewById(R.id.result);
        show_accuracy = (TextView)findViewById(R.id.show_accuracy);
        cvd = (TextView)findViewById(R.id.cvd);

        //Queue of requests
        queue = Volley.newRequestQueue(getApplicationContext());

        //To check connection
        /*JsonObjectRequest handShaker = new JsonObjectRequest(
                Request.Method.GET,

                handshakeAddress,

                null,

                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        //Toast.makeText(getApplicationContext(),"In onResponse",Toast.LENGTH_SHORT).show();
                        try {
                            //JSONObject jsonObject = response.getJSONObject();
                            String message = response.getString("message");
                            Toast.makeText(getApplicationContext(),"Servers are up and running!",Toast.LENGTH_SHORT).show();
                            //String msg = jsonObject.getString("");
                        } catch (JSONException e) {
                            Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_SHORT).show();
                        }
                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(),"We dont seem to connect to servers right now!",Toast.LENGTH_LONG).show();
                    }
                }
        );

        queue.add(handShaker);*/

        //String finalAddress = PredictionAddress+params;

        cvd.setVisibility(View.GONE);
        show_accuracy.setVisibility(View.GONE);

        //if predict is clicked.
        predict.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /*
                * empty the edit text
                * get params
                * checking params
                * making calls to api -> predictor
                * adding the request
                * */

                result.setText("");
                show_accuracy.setText("");


                params = generateParams();
                if(params.equals("error")){
                   Toast.makeText(getApplicationContext(),"Enter missing values",Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(getApplicationContext(), "You are good to go!", Toast.LENGTH_SHORT).show();

                    final JsonObjectRequest predictor = new JsonObjectRequest(
                            Request.Method.GET,

                            PredictionAddress+params,

                            null,

                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    //Toast.makeText(getApplicationContext(),"in on response",Toast.LENGTH_SHORT).show();

                                    try {
                                        String resultstr = ((response.getString("Result")).equals("0"))?"You might not":"You might";
                                        //String for_dev = response.getString("for_dev");
                                        result.setText(resultstr);
                                        if ((resultstr.equals("You might"))) {
                                            result.setTextColor(Color.RED);
                                        } else {
                                            result.setTextColor(Color.BLUE);
                                        }
                                        cvd.setVisibility(View.VISIBLE);

                                        JSONArray accuracy = response.getJSONArray("Accuracy");
                                        String ann="",knn="",lr="",dtree="",nb="",svm="";

                                        show_accuracy.setVisibility(View.VISIBLE);

                                        for (int i=0; i<accuracy.length(); i++) {
                                            JSONObject jsonObject = accuracy.getJSONObject(i);

                                            switch (i) {
                                                case 0:
                                                    ann = (jsonObject.getString("ann")).substring(0, 5) + " %";
                                                    break;

                                                case 1:
                                                    knn = (jsonObject.getString("knn")).substring(0, 5) + " %";
                                                    break;

                                                case 2:
                                                    dtree = (jsonObject.getString("decisionTree")).substring(0, 5) + " %";
                                                    break;

                                                case 3:
                                                    lr = (jsonObject.getString("logisticRegression")).substring(0, 5) + " %";
                                                    break;

                                                case 4:
                                                    nb = (jsonObject.getString("naiveBayes")).substring(0, 5) + " %";
                                                    break;

                                                case 5:
                                                    svm = (jsonObject.getString("svm")).substring(0, 5) + " %";
                                                    break;

                                            }
                                        }

                                            String final_accuracy = "\n\nAccuracy" + getLine() + "\nANN : " + ann + "\nKNN : " + knn + "\nLogistic Regression : " + lr +
                                                    "\nDecision Tree : " + dtree + "\nNaive Bayes : " + nb + "\nSVM : " + svm;

                                            show_accuracy.append(final_accuracy);




                                        //Toast.makeText(getApplicationContext(),for_dev,Toast.LENGTH_LONG).show();

                                    } catch (Exception e) {
                                        Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_LONG).show();
                                    }
                                }
                            },

                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(getApplicationContext(),"Something went wrong",Toast.LENGTH_LONG).show();
                                }
                            }
                    );


                    queue.add(predictor);
                }

            }
        });

        //if reset is clicked.
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //clearing the values
                age.setText("");
                restbp.setText("");
                chol.setText("");
                thalach.setText("");
                oldpeak.setText("");
                ca.setText("");
                result.setText("");

                cvd.setVisibility(View.GONE);
                show_accuracy.setVisibility(View.GONE);

                Toast.makeText(getApplicationContext(),"Ready to predict again!",Toast.LENGTH_SHORT).show();

            }
        });


    }

    static public String getLine() {
        return "\n--------------------------------------------------";
    }
}
