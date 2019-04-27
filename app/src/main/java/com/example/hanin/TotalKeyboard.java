package com.example.hanin;

import android.inputmethodservice.InputMethodService;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputConnection;
import android.widget.LinearLayout;
import android.widget.TextView;

public class TotalKeyboard extends InputMethodService
    implements KeyboardView.OnKeyboardActionListener
    {
        private LinearLayout mInputView;
        private TextView mText;

        @Override public void onCreate() {
            super.onCreate();
        }

        @Override public View onCreateInputView() {
            mInputView = (LinearLayout) getLayoutInflater().inflate(R.layout.key_text, null);
            mText = mInputView.findViewById(R.id.textguy);
            KeyboardView keyboardView = (KeyboardView) mInputView.findViewById(R.id.keyboard);
            Keyboard keyboard = new Keyboard(this, R.xml.number_pad);
            keyboardView.setKeyboard(keyboard);
            keyboardView.setOnKeyboardActionListener(this);
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

            InputConnection inputConnection = getCurrentInputConnection();

            if (inputConnection != null) {
                switch(primaryCode) {
                    case Keyboard.KEYCODE_DELETE :
                        CharSequence selectedText = inputConnection.getSelectedText(0);

                        if (TextUtils.isEmpty(selectedText)) {
                            inputConnection.deleteSurroundingText(1, 0);
                        } else {
                            inputConnection.commitText("", 1);
                        }

                        break;
                    default :
                        char code = (char) primaryCode;
                        inputConnection.commitText(String.valueOf(code), 1);

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

        public void rosettaConnect(String message){



        }
    }
