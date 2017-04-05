package com.week1.tae.realitivelayoutassignment1;


import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.cocosw.bottomsheet.BottomSheet;
import com.codetroopers.betterpickers.datepicker.DatePickerBuilder;
import com.codetroopers.betterpickers.datepicker.DatePickerDialogFragment;
import com.week1.tae.realitivelayoutassignment1.Database.Handler;
import com.week1.tae.realitivelayoutassignment1.Database.Profile;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class MainActivity extends AppCompatActivity implements DatePickerDialogFragment.DatePickerDialogHandler{
    private TextView forename, surname,date;
    private RadioButton male, female, other;
    private Spinner nationality;
    private static final int CAM_REQUEST = 1;
    private ImageView imageView;
    private Bitmap bitmap;
    private TextView mess;
    private RadioGroup rGroup;
    private int day, month, yr;
    private boolean[] tested= {false, false, false, false, false, false};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Convert XML to Java format.
        forename = (TextView) findViewById(R.id.et_forename);
        surname = (TextView)findViewById(R.id.et_Surname);
        date = (TextView)findViewById(R.id.dt_dob);
        male =(RadioButton)findViewById(R.id.rb_male);
        female =(RadioButton)findViewById((R.id.rb_female));
        other =(RadioButton)findViewById((R.id.rb_Other));
        nationality =(Spinner)findViewById(R.id.sp_Nationality);
        imageView = (ImageView) findViewById(R.id.imageButton);
        mess = (TextView)findViewById(R.id.tv_message);
        rGroup = (RadioGroup)findViewById(R.id.rg_Gender);
        //Create Adapter with array of nationalities.
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,R.array.Country_Array, android.R.layout.simple_spinner_item);
        //Specify layout of spinner.
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Add adapter to spinner.
        nationality.setAdapter(adapter);
        //Wait for user to touch imageView.
        //Display camera GUI and wait for user interaction.
        imageView.setOnClickListener(new btnPhotoClicker());
    }

    //Fetch selected date and display in textfield.
    @Override
    public void onDialogDateSet(int reference, int year, int monthOfYear, int dayOfMonth) {
        day = dayOfMonth;
        month = (monthOfYear+1);
        yr = year;
        String Day = String.valueOf((day));
        String Month = String.valueOf(month);
        String years = String.valueOf((yr));
        date.setText(Day + "-" + Month + "-" + years);
    }

    //Present BetterPickers dialog to user.
    public void viewDatePicker(View v) {
        DatePickerBuilder dpb = new DatePickerBuilder()
                .setFragmentManager(getSupportFragmentManager())
                .setStyleResId(R.style.BetterPickersDialogFragment)
                .setYearOptional(true);
        dpb.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == CAM_REQUEST){
            if(bitmap!=null){
                bitmap.recycle();
            }
            bitmap = (Bitmap)data.getExtras().get("data");
            imageView.setImageBitmap(bitmap);
        }
    }

    //Present BottomSheet dialog, and check button's for user touch.
    public void showDialog(View v){
        new BottomSheet.Builder(this).title("Save Or Cancel").sheet(R.menu.bottomsheet).
                listener(new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case R.id.save:
                               saveBtn();
                                break;
                            case R.id.clear:
                                cancelBtn();
                                break;
                        }
                    }
                }).show();
    }

    public void saveBtn(){
        checkFinalResponse();
    }

    //Check radio buttons to find out, which has been selected.
    //Once a selection has been determined call function to save data, passing selected gender.
    private void checkFinalResponse() {
        if (verifyEntries() == true) {
            if(male.isChecked()==true) {
                saveData("Male");
            }else if(female.isChecked()==true){
                saveData("Female");
            }else if(other.isChecked()==true){
                saveData("Other");
            }
        }
    }

    //Check all fields; if any entry has not been populated with user data.
    //Fetch calculated age and return error, if isn't realistic.
    //display error and return false.
    private boolean verifyEntries() {
        StringBuilder sb = new StringBuilder();

        boolean status = false;
        if ((forename.getText().toString().equals("Forename"))||(forename.getText().toString().equals(""))
                ||(forename.getText().toString()==null)) {
            forename.setError("Forename not entered");
            tested[0] = false;
        }else{
            forename.setError(null);
            tested[0] = true;
        }
            if((surname.getText().toString().equals("Surname"))||(surname.getText().toString().equals(""))
                    ||(surname.getText().toString()==null)) {
               surname.setError("Surname not entered");
                tested[1] = false;
            }else{
                surname.setError(null);
                tested[1] = true;
            }
        if((date.getText().toString().equals("Date Of Birth"))||(date.getText().toString().equals(""))) {
            date.setError("Date of birth not entered");
            tested[2] = false;
        }else{
            date.setError(null);
            if(calcAge() >= 140){
                sb.append("Your age is: "+calcAge()+" \n");
                sb.append("Please enter a sensible Date Of Birth \n");
                tested[2] = false;
            }
            else if((calcAge() > 0) && (calcAge() < 14)){
                sb.append("Your age is: "+calcAge()+" \n");
                sb.append("Minimum age is 14 \n");
                tested[2] = false;
            }
            else if(calcAge() < 1){
                sb.append("Your age is: "+calcAge()+" \n");
                sb.append("Enter a sensible Date Of Birth \n");
                tested[2] = false;
            }
            else{
                tested[2] = true;
                mess.setText(null);
            }
        }
            if ((nationality.getSelectedItem().toString().equals("Nationality"))||
                    (nationality.getSelectedItem().toString().equals(""))
                    ||(nationality.getSelectedItem().toString()==null)) {
                sb.append("Nationality not selected \n");
                tested[3] = false;
            }else{
                mess.setError(null);
               tested[3] = true;
            }
        if((male.isChecked()==false)&&(female.isChecked()==false)&&(other.isChecked()==false)){
            sb.append("Gender not selected \n");
            tested[4] = false;
        }else{
           mess.setError(null);
            tested[4] = true;
        }

        if(bitmap==null){
            sb.append("Picture not added \n");
            tested[5] = false;
        }else{
            tested[5] = true;
        }
        mess.setText(sb.toString());
        //Test if we've got everything required and return true
        //Anything has been missed out, stop and return false;
        for(boolean tmp: tested){
            if(tmp == false){
                break;
            }else{
                status = true;
            }
        }
        return status;
    }

    //Calculate age and return value.
    //Examples: 18-10-2016 - 03-06-1882 = 34,
    //18-10-2016 - 03-06-3026 = -1011.
    private int calcAge(){

        GregorianCalendar gc = new GregorianCalendar();
        int d, m, y, a;
        d = gc.get(Calendar.DAY_OF_MONTH);
        m = gc.get(Calendar.MONTH);
        y = gc.get(Calendar.YEAR);
        gc.set(yr, month, day);
        a = y - gc.get(Calendar.YEAR);

        //check for realistic Date Of Birth
        if((m < gc.get(Calendar.MONTH) || (m == gc.get(Calendar.MONTH)
                && (d < gc.get(Calendar.DAY_OF_MONTH))))){
            --a;
        }
        //Check for unrealistic Date Of Birth Entry.
        else if((m > gc.get(Calendar.MONTH) || (m == gc.get(Calendar.MONTH)
                && (d > gc.get(Calendar.DAY_OF_MONTH))))){
            ++a;
        }
        return a;
    }

    private void saveData(String gender){
        Profile holdData = new Profile();
        holdData.setForename(forename.getText().toString());
        holdData.setSurname(surname.getText().toString());
        holdData.setNationality(nationality.getSelectedItem().toString());
        holdData.setDob(date.getText().toString());
        holdData.setGender(gender);
        holdData.setImg(bitmap);
        Handler addProfile = new Handler(this);
        addProfile.Add_Contact(new Profile(forename.getText().toString(),
                surname.getText().toString(),
                nationality.getSelectedItem().toString(), date.getText().toString(), gender, bitmap));
        if(holdData.getStatus()>-1){
            mess.setText("Your profile has been saved");
        }else{
            mess.setText("Profile could not be saved");
        }
    }

    //clear all interface fields, and errors etc.
    public void cancelBtn(){
        forename.setText("Forename");
        forename.setError(null);
        surname.setText("Surname");
        surname.setError(null);
        date.setText("Date Of Birth");
        date.setError(null);
        nationality.setSelection(0);
        rGroup.clearCheck();
        imageView.setImageDrawable(null);
        mess.setText(null);
        for(int i = 0;i<tested.length;i++){
            tested[i] = false;
        }
    }

    //Inner class used to manage camera.
    class btnPhotoClicker implements Button.OnClickListener{

        @Override
        public void onClick(View v) {//Show camera interface.
        Intent camIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(camIntent, CAM_REQUEST);
        }
    }
}

//BottomSheet reference: https://github.com/soarcn/BottomSheet
//Camera Interface reference: https://www.youtube.com/watch?v=s7lo2wSE0zM
//Date Picker: https://github.com/code-troopers/android-betterpickers