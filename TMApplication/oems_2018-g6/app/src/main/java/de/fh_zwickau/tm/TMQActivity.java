package de.fh_zwickau.tm;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import de.fh_zwickau.tm.db_helper.DB_Helper;
import de.fh_zwickau.tm.domain.TMQuestion;
import de.fh_zwickau.tm.fragments.TMQResultDialogFragment;
import de.fh_zwickau.tm.fragments.TMQStartDialogFragment;

public class TMQActivity extends AppCompatActivity {
    private TMQResultDialogFragment tmqResultDialogFragment;
    private TMQStartDialogFragment tmqStartDialogFragment;
    private TextView categoRyTextView, questionTextView;
    private Button nextButton, previousButton, checkButton;
    private RadioButton neverRadBut, seldomRadBut, sometimesRadBut, oftenRadBut, alwaysRadBut;
    private List<RadioButton> answerRadioButtons = new ArrayList<>();
    private List<TMQuestion> questions = new ArrayList<>();
    private int maxQustionCount = 17;
    private TMQuestion currentQuestion;
    private int never = 1;
    private int seldom = 2;
    private int sometimes = 3;
    private int often = 4;
    private int always = 5;
    private int currenQuestionNumber = 0;
    private OnRadioButtonClicked onRadioButtonClicked;
    private OnNextClickedListener onNextClickedListener;
    private OnPreviousClickedListener onPreviousClickedListener;
    private OnCheckClickedListener onCheckClickedListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tmq);
        initializeTMQGUI();
    }

    private void initializeTMQGUI() {
        tmqResultDialogFragment = new TMQResultDialogFragment();
        tmqResultDialogFragment.setTmqscore(0);
        tmqStartDialogFragment = new TMQStartDialogFragment();
        onNextClickedListener = new OnNextClickedListener();
        onPreviousClickedListener = new OnPreviousClickedListener();
        onCheckClickedListener = new OnCheckClickedListener();
        categoRyTextView = findViewById(R.id.category_text_view);
        questionTextView = findViewById(R.id.question_text_view);
        nextButton = findViewById(R.id.next_button);
        nextButton.setOnClickListener(onNextClickedListener);
        previousButton = findViewById(R.id.previous_button);
        previousButton.setOnClickListener(onPreviousClickedListener);
        previousButton.setEnabled(false);
        checkButton = findViewById(R.id.check_button);
        checkButton.setOnClickListener(onCheckClickedListener);
        onRadioButtonClicked = new OnRadioButtonClicked();
        fillQuestionList(questions);

        neverRadBut = findViewById(R.id.never_rad_but);

        seldomRadBut = findViewById(R.id.seldom_rad_but);
        sometimesRadBut = findViewById(R.id.sometimes_rad_but);
        oftenRadBut = findViewById(R.id.often_rad_but);
        alwaysRadBut = findViewById(R.id.always_rab_but);

        answerRadioButtons.add(neverRadBut);
        answerRadioButtons.add(seldomRadBut);
        answerRadioButtons.add(sometimesRadBut);
        answerRadioButtons.add(oftenRadBut);
        answerRadioButtons.add(alwaysRadBut);

        for (RadioButton r : answerRadioButtons) {
            r.setOnClickListener(onRadioButtonClicked);
        }
        setActivityViews(currentQuestion);
        tmqStartDialogFragment.show(getSupportFragmentManager(), "Start");
    }


    private void disableAllRadioButtonsExceptGiven(RadioButton checkedRadioButton) {
        for (RadioButton r : answerRadioButtons) {
            if (!r.equals(checkedRadioButton)) {
                r.setChecked(false);
            }
        }
    }

    private void disableAllRadioButtons() {
        for (RadioButton r : answerRadioButtons) {
                r.setChecked(false);
        }
    }

    private void fillQuestionList(List<TMQuestion> questions) {
        questions.add(new TMQuestion(getStringById(R.string.tmq_short_planing_title), getStringById(R.string.q1)));
        questions.add(new TMQuestion(getStringById(R.string.tmq_short_planing_title), getStringById(R.string.q2)));
        questions.add(new TMQuestion(getStringById(R.string.tmq_short_planing_title), getStringById(R.string.q3)));
        questions.add(new TMQuestion(getStringById(R.string.tmq_short_planing_title), getStringById(R.string.q4)));
        questions.add(new TMQuestion(getStringById(R.string.tmq_short_planing_title), getStringById(R.string.q5)));
        questions.add(new TMQuestion(getStringById(R.string.tmq_short_planing_title), getStringById(R.string.q6)));
        questions.add(new TMQuestion(getStringById(R.string.tmq_short_planing_title), getStringById(R.string.q7)));
        questions.add(new TMQuestion(getStringById(R.string.tmq_title), getStringById(R.string.q8)));
        questions.add(new TMQuestion(getStringById(R.string.tmq_title), getStringById(R.string.q9)));
        questions.add(new TMQuestion(getStringById(R.string.tmq_title), getStringById(R.string.q10)));
        questions.add(new TMQuestion(getStringById(R.string.tmq_title), getStringById(R.string.q11)));
        questions.add(new TMQuestion(getStringById(R.string.tmq_title), getStringById(R.string.q12)));
        questions.add(new TMQuestion(getStringById(R.string.tmq_title_long), getStringById(R.string.q13)));
        questions.add(new TMQuestion(getStringById(R.string.tmq_title_long), getStringById(R.string.q14)));
        questions.add(new TMQuestion(getStringById(R.string.tmq_title_long), getStringById(R.string.q15)));
        questions.add(new TMQuestion(getStringById(R.string.tmq_title_long), getStringById(R.string.q16)));
        questions.add(new TMQuestion(getStringById(R.string.tmq_title_long), getStringById(R.string.q17)));
        questions.add(new TMQuestion(getStringById(R.string.tmq_title_long), getStringById(R.string.q18)));

        currentQuestion = questions.get(currenQuestionNumber);
    }

    class OnRadioButtonClicked implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            RadioButton clickedButton = (RadioButton) v;
            clickedButton.setChecked(true);
            disableAllRadioButtonsExceptGiven(clickedButton);
            setQuestionValue(clickedButton);
        }
    };

    class OnNextClickedListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            if(currenQuestionNumber == maxQustionCount-1){
                nextButton.setEnabled(false);
            }
            currenQuestionNumber++;
            if(!previousButton.isEnabled()){
                previousButton.setEnabled(true);
            }

            currentQuestion = questions.get(currenQuestionNumber);
            setActivityViews(currentQuestion);
        }
    }

    class OnPreviousClickedListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            if(currenQuestionNumber == 1){
                previousButton.setEnabled(false);
            }
            currenQuestionNumber--;
            if(!nextButton.isEnabled()){
                nextButton.setEnabled(true);
            }

            currentQuestion = questions.get(currenQuestionNumber);
            setActivityViews(currentQuestion);
        }
    }

    class OnCheckClickedListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            DB_Helper db_helper = MainActivity.db_helper;
            int tmqscore = getPercentOfAnswers();
            db_helper.updateScore(tmqscore);
            tmqResultDialogFragment.setTmqscore(tmqscore);
            tmqResultDialogFragment.show(getSupportFragmentManager(), "TMQ Score result");
        }
    }

    private int getPercentOfAnswers(){
        int maxValue = always*(maxQustionCount+1);

        double sumOfAnswers = 0;
        double percent = 100;
        int tmqscore ;
        for (TMQuestion q : questions){
            sumOfAnswers += q.getAnswerValue();
        }
        Log.d("sumOfAns", sumOfAnswers+"");

        tmqscore = (int)Math.round((sumOfAnswers/maxValue)*100);
        return tmqscore;
    }

    private void setActivityViews(TMQuestion currentQuestion){
        categoRyTextView.setText(currentQuestion.getCategory());
        questionTextView.setText(currentQuestion.getQuestion());
        setRadButStates(currentQuestion.getAnswerValue());
    }

    private void setRadButStates(int answerValue){
        switch (answerValue){
            case 1:
                neverRadBut.setChecked(true);
                disableAllRadioButtonsExceptGiven(neverRadBut);
                break;
            case 2:
                seldomRadBut.setChecked(true);
                disableAllRadioButtonsExceptGiven(seldomRadBut);
                break;
            case 3:
                sometimesRadBut.setChecked(true);
                disableAllRadioButtonsExceptGiven(sometimesRadBut);
                break;
            case 4:
                oftenRadBut.setChecked(true);
                disableAllRadioButtonsExceptGiven(oftenRadBut);
                break;
            case 5:
                alwaysRadBut.setChecked(true);
                disableAllRadioButtonsExceptGiven(alwaysRadBut);
                break;

        }
    }

    private void setQuestionValue(RadioButton clickedRadBut){
        Log.d("setQuestionValue","YES");
        if(clickedRadBut.equals(neverRadBut)){
            currentQuestion.setAnswerValue(never);
        }else if(clickedRadBut.equals(seldomRadBut)){
            currentQuestion.setAnswerValue(seldom);
        }else if(clickedRadBut.equals(seldomRadBut)){
            currentQuestion.setAnswerValue(sometimes);
        }else if(clickedRadBut.equals(oftenRadBut)){
            currentQuestion.setAnswerValue(often);
        }else if(clickedRadBut.equals(alwaysRadBut)){
            currentQuestion.setAnswerValue(always);
        }
    }

    private String getStringById(int id) {
        return getResources().getString(id);
    }

}
