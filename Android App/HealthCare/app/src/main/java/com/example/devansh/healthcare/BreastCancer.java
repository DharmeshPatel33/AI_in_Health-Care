package com.example.devansh.healthcare;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

public class BreastCancer extends AppCompatActivity {

    public EditText radius, texture, perimeter, area, smoothness, compactness, concavity, concave_points, symmetry, fractal_dimension;
    public Button predict, reset;
    public TextView result, show_accuracy, cvd;

    //public String HandshakeAddress = "";
    public String PredictionAddress = "https://intense-citadel-71493.herokuapp.com/cancer/predict/";

    public String params;
    RequestQueue queue;

    // Return Params
    String generateParams() {

        //Fetch values
        String rad = radius.getText().toString();
        String text = texture.getText().toString();
        String peri = perimeter.getText().toString();
        String ar = area.getText().toString();
        String smooth = smoothness.getText().toString();
        String compact = compactness.getText().toString();
        String concave = concavity.getText().toString();
        String con_pts = concave_points.getText().toString();
        String symm = symmetry.getText().toString();
        String fd = fractal_dimension.getText().toString();

        //returning..
        if(rad.equals("") || text.equals("") || peri.equals("") || ar.equals("") || smooth.equals("") || compact.equals("") || concave.equals("") || con_pts.equals("") || symm.equals("") || fd.equals(""))
            return "error";
        else
            return (rad + "-" + text + "-" + peri + "-" + ar + "-" + smooth + "-" + compact + "-" + concave + "-" + con_pts + "-" + symm + "-" + fd);
    }

    static public String getLine() {
        return "\n--------------------------------------------------";
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_breast_cancer);


        //Features
        radius = (EditText)findViewById(R.id.rMean);
        texture = (EditText)findViewById(R.id.tMean);
        perimeter = (EditText)findViewById(R.id.pMean);
        area = (EditText)findViewById(R.id.aMean);
        smoothness = (EditText)findViewById(R.id.sMean);
        compactness = (EditText)findViewById(R.id.CoMean);
        concavity = (EditText)findViewById(R.id.concMean);
        concave_points = (EditText)findViewById(R.id.conCavMean);
        symmetry = (EditText)findViewById(R.id.SymMean);
        fractal_dimension = (EditText)findViewById(R.id.fdMean);

        //Buttons
        predict = (Button)findViewById(R.id.predictbtn);
        reset = (Button)findViewById(R.id.resetbtn);

        //result show
        result = (TextView)findViewById(R.id.result);
        show_accuracy = (TextView)findViewById(R.id.show_accuracy);
        cvd = (TextView)findViewById(R.id.cvd);

        //Queue of requests
        queue = Volley.newRequestQueue(getApplicationContext());

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
                                        String resultstr = ((response.getString("Result")).equals("M"))?"Malignant":"Benign";
                                        //String for_dev = response.getString("for_dev");
                                        result.setText("You have " + resultstr);
                                        if ((resultstr.equals("Malignant"))) {
                                            result.setTextColor(Color.RED);
                                        } else {
                                            result.setTextColor(Color.BLUE);
                                        }
                                        cvd.setVisibility(View.VISIBLE);

                                        JSONArray accuracy = response.getJSONArray("Accuracy");
                                        String knn="",lr="",nb="",svm="";

                                        show_accuracy.setVisibility(View.VISIBLE);

                                        for (int i=0; i<accuracy.length(); i++) {
                                            JSONObject jsonObject = accuracy.getJSONObject(i);

                                            switch (i) {

                                                case 0:
                                                    knn = (jsonObject.getString("knn")).substring(0, 5) + " %";
                                                    break;

                                                case 1:
                                                    lr = (jsonObject.getString("lr")).substring(0, 5) + " %";
                                                    break;

                                                case 2:
                                                    nb = (jsonObject.getString("nb")).substring(0, 5) + " %";
                                                    break;

                                                case 3:
                                                    svm = (jsonObject.getString("svm")).substring(0, 5) + " %";
                                                    break;

                                            }
                                        }

                                        String final_accuracy = "\n\nAccuracy" + getLine() + "\nKNN : " + knn + "\nLogistic Regression : " + lr +
                                                "\nNaive Bayes : " + nb + "\nSVM : " + svm;

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
                radius.setText("");
                texture.setText("");
                perimeter.setText("");
                area.setText("");
                smoothness.setText("");
                compactness.setText("");
                concavity.setText("");
                concave_points.setText("");
                symmetry.setText("");
                fractal_dimension.setText("");

                result.setText("");

                cvd.setVisibility(View.GONE);
                show_accuracy.setVisibility(View.GONE);

                Toast.makeText(getApplicationContext(),"Ready to predict again!",Toast.LENGTH_SHORT).show();

            }
        });






    }
}
