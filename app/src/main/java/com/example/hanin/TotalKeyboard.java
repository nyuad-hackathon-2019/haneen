package com.example.hanin;

import android.inputmethodservice.InputMethodService;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputConnection;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class TotalKeyboard extends InputMethodService
    implements KeyboardView.OnKeyboardActionListener, View.OnClickListener {
        private LinearLayout mInputView;
        private TextView mText;
        private TextView mText1;
        private TextView mText2;
        String message = "";
        static String something = "";
        static String sentence = "";
        static boolean bool = false;

        private String[] suggestions = {

            "سلام" ,
                " كيف",
                " حالك",
            "السلام" ,
                "عليكم " ,
                "كيف" ,
                    "الحال",
            "صباح" ,
                "الخير" ,
                " كيف " ,
                    "حالك",
            "الحمد" ,
                "لله " ,
                "عالسلامة",
            "اسف" ,
                "عالتأخير" ,
                "كان" ,
                "في" ,
                    "زحمة" ,
                "عالطريق",
                "طمني" ,
                "  عنك إن " ,
                "شاء",
                    "الله" ,
                "صرت",
                    "أحسن",
            "وين",
                "السهرة" ,
                "اليوم"
        };

        @Override public void onCreate() {
            super.onCreate();
        }

        @Override public View onCreateInputView() {
            mInputView = (LinearLayout) getLayoutInflater().inflate(R.layout.key_text, null);
            mText = mInputView.findViewById(R.id.textguy);
            mText1 = mInputView.findViewById(R.id.textguy1);
            mText2 = mInputView.findViewById(R.id.textguy2);
            KeyboardView keyboardView = (KeyboardView) mInputView.findViewById(R.id.keyboard);
            Keyboard keyboard = new Keyboard(this, R.xml.number_pad);
            keyboardView.setKeyboard(keyboard);
            keyboardView.setOnKeyboardActionListener(this);
            mText.setOnClickListener(this);
            mText1.setOnClickListener(this);
            mText2.setOnClickListener(this);

            return mInputView;
        }

        @Override
        public void onPress(int primaryCode) {

        }

        @Override
        public void onRelease(int primaryCode) {

        }

        @Override
        public void onKey(int primaryCode, int[] keyCodes) {


            final InputConnection inputConnection = getCurrentInputConnection();

            if (inputConnection != null) switch (primaryCode) {
                case Keyboard.KEYCODE_DELETE:
                    CharSequence selectedText = inputConnection.getSelectedText(0);
                    if (TextUtils.isEmpty(selectedText)) {
                        inputConnection.deleteSurroundingText(1, 0);
                    } else {
                        inputConnection.commitText("", 1);
                    }
//
                    if(message.length() > 0)
                    {
                        message = message.substring(0, message.length()-1);
                    }
                    else
                    {
                        Log.d("Space", "jiro");
                    }
                    bool = false;
                    break;
                default:
                    final char code = (char) primaryCode;
                    if(code == 10)
                    {
                        if(something.length() > 1)
                        {
                            repustateConnect();
                            sentence = "";
                        }
                        else
                        {
                            Log.d("Done", "jiro");
                        }
                        bool =false;
                    }
                    if(code == 32 && bool == false)
                    {
                        bool = true;
                        Log.d("Space", message);
                        String URL =  "https://api.rosette.com:443/rest/v1/transliteration/";
                        final String key = "e3a9f84d7586b4489486602c2394c081";

                        String transliterationData = message;

                        JSONObject jsonBody = new JSONObject();
                        try {
                            jsonBody.put("content", transliterationData);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                        RequestQueue requestQueue = Volley.newRequestQueue(this);

                        JsonObjectRequest objectRequest = new JsonObjectRequest(
                            Request.Method.POST,
                            URL,
                            jsonBody,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {

                                    Log.d("ROSETTE", response.toString());
                                    try {
                                        Random random = new Random();
                                        int randomNum = random.nextInt(((suggestions.length-2) - 0) + 1) + 0;
                                        String arabicreply = response.getString("transliteration");
                                        mText.setText(suggestions[randomNum]);
                                        randomNum = random.nextInt(((suggestions.length-2) - 0) + 1) + 0;
                                        mText1.setText(suggestions[randomNum]);
                                        randomNum = random.nextInt(((suggestions.length-2) - 0) + 1) + 0;
                                        mText2.setText(suggestions[randomNum]);
                                        inputConnection.deleteSurroundingText(message.length(), 0);
                                        inputConnection.commitText(String.valueOf(code), 1);
                                        inputConnection.commitText(arabicreply, 1);
                                        inputConnection.commitText(String.valueOf(code), 1);
                                        something = arabicreply;
                                        sentence = something + sentence;
                                        message = "";
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Log.d("ROSETTE", error.toString());
                                }
                            }
                        ){
                            @Override
                            public Map<String, String> getHeaders() throws AuthFailureError {
                                Map<String,String> params = new HashMap<>();
                                params.put("Content-Type","application/json");
                                params.put("X-RosetteAPI-Key",key);
                                //..add other headers
                                return params;
                            }
                        };

                        requestQueue.add(objectRequest);

                        message = message + String.valueOf(code);
//                        inputConnection.comm

                    }
                    else
                    {
                        inputConnection.commitText(String.valueOf(code), 1);
                        message = message + String.valueOf(code);
                        bool = false;

                    }

            }
        }

        @Override
        public void onText(CharSequence text) {


        }

        @Override
        public void swipeLeft() {

        }

        @Override
        public void swipeRight() {

        }

        @Override
        public void swipeDown() {

        }

        @Override
        public void swipeUp() {

        }

        public void repustateConnect(){
            String URL =  "https://api.repustate.com/v4/f08fba212fbf0e1029acd365c9943ca57cde5dac/score.json?text=";




            RequestQueue requestQueue = Volley.newRequestQueue(this);

            JsonObjectRequest objectRequest = new JsonObjectRequest(
                Request.Method.POST,
                URL + sentence + "&lang=ar",
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.d("REPUSTATE", response.toString());
                        try {
                            String arabicreply = response.getString("score");
                            Toast.makeText(getApplicationContext(), arabicreply, Toast.LENGTH_LONG).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("ROSETTE", error.toString());
                    }
                }
            ){
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String,String> params = new HashMap<>();
                    params.put("Content-Type","application/json");
                    //..add other headers
                    return params;
                }
            };

            requestQueue.add(objectRequest);

        }

    @Override
    public void onClick(View v) {

        final InputConnection inputConnection = getCurrentInputConnection();
     if(v.getId() == R.id.textguy)
     {
         inputConnection.commitText(mText.getText().toString(), 1);
         inputConnection.commitText(String.valueOf((char)32), 1);
     }
     else  if(v.getId() == R.id.textguy1)
     {
         inputConnection.commitText(mText1.getText().toString(), 1);
         inputConnection.commitText(String.valueOf((char)32), 1);
     }
     else  if(v.getId() == R.id.textguy2)
     {
         inputConnection.commitText(mText2.getText().toString(), 1);
         inputConnection.commitText(String.valueOf((char)32), 1);
     }

        Random random = new Random();
        int randomNum = random.nextInt(((suggestions.length-2) - 0) + 1) + 0;
        mText.setText(suggestions[randomNum]);
        randomNum = random.nextInt(((suggestions.length-2) - 0) + 1) + 0;
        mText1.setText(suggestions[randomNum]);
        randomNum = random.nextInt(((suggestions.length-2) - 0) + 1) + 0;
        mText2.setText(suggestions[randomNum]);
    }
}
