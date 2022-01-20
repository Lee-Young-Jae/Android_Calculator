package com.example.calculator;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

import javax.script.*;

public class MainActivity extends AppCompatActivity {

    Button btn1$, btn2$, btn3$, btn4$, btn5$, btn6$, btn8$, btn7$ ,btn9$, btn0$, btnClear$, btnSign$,
    btnBackSpace$, btnDiv$, btnMul$, btnMinus$, btnPlus$, btnDot$, btnEqual$;
    TextView textView$;
    boolean ExistSign;
    boolean ExistopCode;
    boolean isItFloat;
    boolean backSpaceAfterBracket;
    char lastOpcode;


    // JavaScript의 함수를 사용할 수 있게 하는 라이브러리
    ScriptEngineManager SEM = new ScriptEngineManager();
    ScriptEngine SE = SEM.getEngineByName("javascript");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        TextView test$ = (TextView)findViewById(R.id.textView);

//        View.OnClickListener listener = new View.OnClickListener(){
//            @Override
//            public void onClick(View v){
////                test$.setText(test$.getText()+);
//            }
//        };

        this.InitializeView();
//        Button btn0$ = (Button)findViewById(R.id.btn_0);
//        btn0$.setOnClickListener(listener);
    }
    public void InitializeView()
    {
        btn1$ = (Button)findViewById(R.id.btn_1);
        btn2$ = (Button)findViewById(R.id.btn_2);
        btn3$ = (Button)findViewById(R.id.btn_3);
        btn4$ = (Button)findViewById(R.id.btn_4);
        btn5$ = (Button)findViewById(R.id.btn_5);
        btn6$ = (Button)findViewById(R.id.btn_6);
        btn7$ = (Button)findViewById(R.id.btn_7);
        btn8$ = (Button)findViewById(R.id.btn_8);
        btn9$ = (Button)findViewById(R.id.btn_9);
        btn0$ = (Button)findViewById(R.id.btn_0);
        btnClear$ = (Button)findViewById(R.id.btn_clear);
        btnSign$ = (Button)findViewById(R.id.btn_sign);
        btnBackSpace$ = (Button)findViewById(R.id.btn_backspace);
        btnDiv$ = (Button)findViewById(R.id.btn_div);
        btnMul$ = (Button)findViewById(R.id.btn_mul);
        btnMinus$ = (Button)findViewById(R.id.btn_minus);
        btnPlus$ = (Button)findViewById(R.id.btn_plus);
        btnDot$ = (Button)findViewById(R.id.btn_dot);
        btnEqual$ = (Button)findViewById(R.id.btn_equal);

        textView$ = (TextView)findViewById(R.id.textView);
    }

    public void onClickNumberBtn(View view) {
        String expressionText = textView$.getText().toString();

        // 마지막 글자가 ')' 라면 숫자 입력X
        if (expressionText.length()>0){
            char lastFirstChar = expressionText.charAt(expressionText.length()-1);
            if (lastFirstChar == ')'){ return; }
        }
        switch (view.getId()){
            case R.id.btn_0:
                textView$.setText(textView$.getText()+"0");
                ExistopCode = false;
                break;
            case R.id.btn_9:
                textView$.setText(textView$.getText()+"9");
                ExistopCode = false;
                break;
            case R.id.btn_8:
                textView$.setText(textView$.getText()+"8");
                ExistopCode = false;
                break;
            case R.id.btn_7:
                textView$.setText(textView$.getText()+"7");
                ExistopCode = false;
                break;
            case R.id.btn_6:
                textView$.setText(textView$.getText()+"6");
                ExistopCode = false;
                break;
            case R.id.btn_5:
                textView$.setText(textView$.getText()+"5");
                ExistopCode = false;
                break;
            case R.id.btn_4:
                textView$.setText(textView$.getText()+"4");
                ExistopCode = false;
                break;
            case R.id.btn_3:
                textView$.setText(textView$.getText()+"3");
                ExistopCode = false;
                break;
            case R.id.btn_2:
                textView$.setText(textView$.getText()+"2");
                ExistopCode = false;
                break;
            case R.id.btn_1:
                textView$.setText(textView$.getText()+"1");
                ExistopCode = false;
                break;
        }
    }

    public void onClickOpcode(View view) {

        switch (view.getId()) {
            case R.id.btn_clear:
                textView$.setText("");
                break;
            case R.id.btn_backspace:

                //textView가 공백이라면 리턴
                if (textView$.getText().equals("")){
                    return;
                }

                // infinity 라면
                if (textView$.getText().equals("Infinity") || textView$.getText().equals("-Infinity")){
                    textView$.setText("");
                }

                // 문자열의 길이가 0이 아닌 경우만 backspace
                if (textView$.getText().length() > 0){
                    String expressionText = (String) textView$.getText();
                    char lastFirstChar = expressionText.charAt(expressionText.length()-1);

                    // 문자열의 마지막 문자가 연산자인 경우 연산자 대신 '(- num)' 연산부호를 삭제
//                    if (lastFirstChar == '/' || lastFirstChar == '+' || lastFirstChar == '-' || lastFirstChar == '*'){
//                        char lastSecChar = expressionText.charAt(expressionText.length()-2);
//                        if (lastSecChar == ')'){
//                            expressionText = expressionText.replace("(-", "");
//                            expressionText = expressionText.replace(")", "");
//                            textView$.setText(expressionText);
//                            return;
//                        }
//                    }
//                    expressionText = expressionText.substring(0, expressionText.length()-1);
//                    textView$.setText(expressionText);

                    if (lastFirstChar == ')'){
                        // 지울 문자가 괄호인 경우 앞의 '(' 괄호까지 삭제
                        char lastChar; //textView의 마지막 문자를 저장할 변수
                        int indexOfBracket = 0; //마지막으로 입력한 부호의 index를 저장할 변수
                        for(int i=expressionText.length()-1; i>=0; i--){
                            lastChar = expressionText.charAt(i);

                            // '(' 를 만나면 Bracket의 인덱스를 반환
                            if (lastChar == '('){
                                indexOfBracket = i;
                                ExistSign = false;
                                break;
                            }
                        }

                        // 문자열의 끝에서 부터 처음 만나는 '(-' 와 마지막 문자를 지운 나머지 출력
                        String firstText = expressionText.substring(0, indexOfBracket);
                        String SecondText = expressionText.substring(indexOfBracket+2, expressionText.length()-1);
                        textView$.setText(firstText+SecondText);
                        ExistSign = false;
                    } else if (ExistSign){
                        // 괄호는 없었지만 '(' 가 존재하는 경우
                        char lastChar; //textView의 마지막 문자를 저장할 변수
                        int indexOfBracket = 0; //마지막으로 입력한 부호의 index를 저장할 변수
                        for(int i=expressionText.length()-1; i>=0; i--){
                            lastChar = expressionText.charAt(i);

                            // '(' 를 만나면 Bracket의 인덱스를 반환
                            if (lastChar == '('){
                                indexOfBracket = i;
                                ExistSign = false;
                                break;
                            }
                        }

                        // 문자열의 끝에서 부터 처음 만나는 '(-' 와 마지막 문자를 지운 나머지 출력
                        String firstText = expressionText.substring(0, indexOfBracket);
                        String SecondText = expressionText.substring(indexOfBracket+2, expressionText.length());
                        textView$.setText(firstText+SecondText);
                        ExistSign = false;
                    }
                    else {
                        expressionText = expressionText.substring(0, expressionText.length()-1);
                        textView$.setText(expressionText);
                    }


                    // 문자가 지워진 후 마지막 문자가 연산자인지 아닌지를 체크
                    if (expressionText.length() > 0){
                        lastFirstChar = expressionText.charAt(expressionText.length()-1);
                    }
                    if (lastFirstChar == '/' || lastFirstChar == '+' || lastFirstChar == '-' || lastFirstChar == '*'){
                        ExistopCode = true;
                    }else {
                        ExistopCode = false;
                    }


                    // 문자가 지워진 후 마지막 피연산자가 소수인지 아닌지를 체크
                    expressionText = textView$.getText().toString();
                    boolean checkOp = false;
                    for(int i=expressionText.length(); i>0; i--){
                        lastFirstChar = expressionText.charAt(i-1);

                        if (lastFirstChar == '+' || lastFirstChar == '-' || lastFirstChar == '*' || lastFirstChar == '/' ){
                            checkOp = true;
                            break;
                        }

                        if (lastFirstChar == '.' && checkOp == false){
                            // 피연산자가 소수
                            isItFloat = true;
                        }
                    }
                    isItFloat = false;

                    // 문자가 지워진 후 마지막 글자가 ')' 인지 확인
                    expressionText = textView$.getText().toString();
                    if (expressionText.length() > 0){
                        lastFirstChar = expressionText.charAt(expressionText.length()-1);
                        if (lastFirstChar == ')'){
                            backSpaceAfterBracket = true;
                        } else {
                            backSpaceAfterBracket = false;
                        }
                    }


                } else {
                    textView$.setText("");
                }

                break;

            case R.id.btn_sign:
                boolean IsTheSignNegative;

                // 빈 문자열이면 '(-' 출력
                if (textView$.getText().equals("")){
                    textView$.setText("(-");
                    ExistSign = true;
                    ExistopCode = false;
                    return;
                }

                // 연산 결과가 -일 경우
                if (textView$.getText().toString().charAt(0) == '-'){
                    textView$.setText(textView$.getText().toString().substring(1, textView$.getText().length()));
                    return;
                }

                //backspace 후 마지막 괄호가 ')' 라면
                if (backSpaceAfterBracket){
                    backSpaceAfterBracket = false;
                    String expressionText = (String) textView$.getText();
                    char lastChar; //textView의 마지막 문자를 저장할 변수
                    int indexOfBracket = 0; //마지막으로 입력한 부호의 index를 저장할 변수
                    for(int i=expressionText.length()-1; i>=0; i--){
                        lastChar = expressionText.charAt(i);

                        // '(' 를 만나면 Bracket의 인덱스를 반환
                        if (lastChar == '('){
                            indexOfBracket = i;
                            ExistSign = false;
                            break;
                        }
                    }

                    // 문자열의 끝에서 부터 처음 만나는 '(-' 를 지운 나머지 출력
                    String firstText = expressionText.substring(0, indexOfBracket);
                    String SecondText = expressionText.substring(indexOfBracket+2, expressionText.length()-1);
                    textView$.setText(firstText+SecondText);
                    ExistSign = false;
                    return;
                }

                // 부호가 존재한다면
                if (ExistSign){
                    String expressionText = (String) textView$.getText();
                    char lastChar; //textView의 마지막 문자를 저장할 변수
                    int indexOfBracket = 0; //마지막으로 입력한 부호의 index를 저장할 변수
                    for(int i=expressionText.length()-1; i>=0; i--){
                        lastChar = expressionText.charAt(i);

                        // '(' 를 만나면 Bracket의 인덱스를 반환
                        if (lastChar == '('){
                            indexOfBracket = i;
                            ExistSign = false;
                            break;
                        }
                    }

                    // 문자열의 끝에서 부터 처음 만나는 '(-' 를 지운 나머지 출력
                    String firstText = expressionText.substring(0, indexOfBracket);
                    String SecondText = expressionText.substring(indexOfBracket+2, expressionText.length());
                    textView$.setText(firstText+SecondText);
                    ExistSign = false;
                    return;

                // 부호가 존재하지 않다면
                } else {
                    String expressionText = (String) textView$.getText();
                    char lastChar; //textView의 마지막 문자를 저장할 변수
                    int indexOfOpcode = 0; //마지막으로 입력한 부호의 index를 저장할 변수
                    boolean findedOp = false;

                    // textView의 마지막 글자부터 조회하여 마지막으로 입력한 연산자의 index를 찾는다.
                    for (int i=expressionText.length()-1; i>=0; i--){
                        lastChar = expressionText.charAt(i);

                        // index를 찾았을 경우
                        if (lastChar == lastOpcode){
                            indexOfOpcode = i;
                            findedOp = true;
                            break;
                        }
                    }

                    // index를 찾지 못했을 경우 (연산자가 존재하지 않는 경우)
                    if (!findedOp){
                        textView$.setText("(-"+ expressionText);
                    // index를 찾았을 경우 (연산자가 존재하는 경우) 마지막으로 입력한 수에 '(-' 부호 추가)
                    } else {
                        String firstText = expressionText.substring(0, indexOfOpcode+1);
                        String SecondText = expressionText.substring(indexOfOpcode+1, expressionText.length());
                        textView$.setText(firstText+"(-"+SecondText);

                    }
                    ExistSign = true;
                }
                break;
            case R.id.btn_mul:
                //textView가 공백이라면 리턴
                if (textView$.getText().equals("")){
                    return;
                }

                // TextView 첫문자 '-'인 경우 (연산 후 결과를 backspace로 '-'문자만 남겼을 때)
                if (textView$.getText().toString().charAt(0) == '-'){
                    textView$.setText(textView$.getText().toString().substring(1, textView$.getText().length()));
                    return;
                }

                //연산자가 존재하면 기존 연산자를 지우고 '*' 넣기
                if(ExistopCode){
                    String expressionText = (String) textView$.getText();

                    if (expressionText.charAt(expressionText.length()-2) == '(') {break;}
                    expressionText = expressionText.substring(0, expressionText.length()-1);
                    textView$.setText(expressionText + '*');

                    lastOpcode = '*';
                    isItFloat = false;
                    return;
                }

                // +/- 부호 존재 시 괄호를 닫고 '*' 넣기
                if(ExistSign){
                    // '(-' 라면 입력오류
                    if (textView$.getText().toString().charAt(textView$.getText().length()-1) == '-') break;

                    textView$.setText(textView$.getText() + ")*");
                    ExistSign = false;
                    lastOpcode = '*';
                    isItFloat = false;
                }
                else{
                    textView$.setText(textView$.getText() + "*");
                    lastOpcode = '*';
                    isItFloat = false;
                }
                ExistopCode = true;
                break;

            case R.id.btn_div:
                //textView가 공백이라면 리턴
                if (textView$.getText().equals("")){
                    return;
                }

                // TextView 첫문자 '-'인 경우 (연산 후 결과를 backspace로 '-'문자만 남겼을 때)
                if (textView$.getText().toString().charAt(0) == '-'){
                    textView$.setText(textView$.getText().toString().substring(1, textView$.getText().length()));
                    return;
                }

                //연산자가 존재하면 기존 연산자를 지우고 '/' 넣기
                if(ExistopCode){
                    String expressionText = (String) textView$.getText();

                    if (expressionText.charAt(expressionText.length()-2) == '(') {break;}
                    expressionText = expressionText.substring(0, expressionText.length()-1);
                    textView$.setText(expressionText + '/');
                    lastOpcode = '/';
                    isItFloat = false;
                    return;
                }

                // +/- 부호 존재 시 괄호를 닫고 '/' 넣기
                if(ExistSign){
                    if (textView$.getText().toString().charAt(textView$.getText().length()-1) == '-') break;
                    textView$.setText(textView$.getText() + ")/");
                    ExistSign = false;
                    lastOpcode = '/';
                    isItFloat = false;
                }
                else{
                    textView$.setText(textView$.getText() + "/");
                    lastOpcode = '/';
                    isItFloat = false;
                }
                ExistopCode = true;
                break;

            case R.id.btn_plus:
                //textView가 공백이라면 리턴
                if (textView$.getText().equals("")){
                    return;
                }

                // TextView 첫문자 '-'인 경우 (연산 후 결과를 backspace로 '-'문자만 남겼을 때)
                if (textView$.getText().toString().charAt(0) == '-'){
                    textView$.setText(textView$.getText().toString().substring(1, textView$.getText().length()));
                    return;
                }

                //연산자가 존재하면 기존 연산자를 지우고 '+' 넣기
                if(ExistopCode){
                    String expressionText = (String) textView$.getText();

                    if (expressionText.charAt(expressionText.length()-2) == '(') {break;}
                    expressionText = expressionText.substring(0, expressionText.length()-1);
                    textView$.setText(expressionText + '+');
                    lastOpcode = '+';
                    isItFloat = false;
                    return;
                }

                // +/- 부호 존재 시 괄호를 닫고 '+' 넣기
                if(ExistSign){
                    if (textView$.getText().toString().charAt(textView$.getText().length()-1) == '-') break;

                    textView$.setText(textView$.getText() + ")+");
                    ExistSign = false;
                    lastOpcode = '+';
                    isItFloat = false;
                }
                else{
                    textView$.setText(textView$.getText() + "+");
                    lastOpcode = '+';
                    isItFloat = false;
                }
                ExistopCode = true;
                break;

            case R.id.btn_minus:
                //textView가 공백이라면 리턴
                if (textView$.getText().equals("")){
                    return;
                }

                // TextView 첫문자 '-'인 경우 (연산 후 결과를 backspace로 '-'문자만 남겼을 때)
                if (textView$.getText().toString().charAt(0) == '-'){
                    textView$.setText(textView$.getText().toString().substring(1, textView$.getText().length()));
                    return;
                }

                //연산자가 존재하면 기존 연산자를 지우고 '-' 넣기
                if(ExistopCode){
                    String expressionText = (String) textView$.getText();

                    if (expressionText.charAt(expressionText.length()-2) == '(') {break;}
                    expressionText = expressionText.substring(0, expressionText.length()-1);
                    textView$.setText(expressionText + '-');
                    lastOpcode = '-';
                    isItFloat = false;
                    return;
                }

                // +/- 부호 존재 시 괄호를 닫고 '-' 넣기
                if(ExistSign){
                    if (textView$.getText().toString().charAt(textView$.getText().length()-1) == '-') break;

                    textView$.setText(textView$.getText() + ")-");
                    ExistSign = false;
                    lastOpcode = '-';
                    isItFloat = false;
                }
                else{
                    textView$.setText(textView$.getText() + "-");
                    lastOpcode = '-';
                    isItFloat = false;
                }
                ExistopCode = true;
                break;

            case R.id.btn_dot:

                // textView의 Text가 빈 문자열이 아닐때 마지막 문자가 숫자면 소숫점 입력
                if (textView$.getText().length() > 0 && !isItFloat){
                    // 피연산자가 소수가 아니면 소숫점 입력
                    char lastText = textView$.getText().charAt(textView$.getText().length()-1);
                    if (lastText != '.' && lastText != '+' && lastText != '-' && lastText != '*' && lastText != '/'){
                        textView$.setText(textView$.getText() + ".");
                        isItFloat = true;
                    }
                }
                break;

            case R.id.btn_equal:

                // textView의 Text가 빈 문자열이 아닐때 닫히지 않은 괄호가 있는지 확인 후 연산수행

                if (textView$.getText().length() > 0 ) {
                    char pointing;
                    int openBracket = 0;
                    int closeBracket = 0;
                    for (int i=0; i<textView$.getText().length(); i++){
                        pointing = textView$.getText().charAt(i);

                        if(pointing == '('){
                            openBracket += 1;
                        }
                        if (pointing == ')'){
                            closeBracket += 1;
                        }
                    }
                    if (openBracket > closeBracket){
                        textView$.setText(textView$.getText() + ")");
                        ExistSign = false;
                    }
                    //JavaScript의 eval 함수 사용
                    try{
                        String result = SE.eval( (String)textView$.getText() ).toString();
                        boolean floatTrue = false; // 소숫점이면 true
                        int floatPointIndex = 0; // 소숫점 인덱스

                        char lastChar = result.charAt(result.length()-1);
                        if ( lastChar == '0' ){
                            float test = Float.parseFloat(result);
                            result = Integer.toString((int)test);
                            textView$.setText( result );
                        } else {
                            textView$.setText(result);
                        }

                    } catch (Exception ex){
                        ex.printStackTrace();

                    }
                }

        }
    }

    void drawOnTextView(){

    }



}