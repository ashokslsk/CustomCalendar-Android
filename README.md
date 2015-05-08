# CustomCalendar-Android
Calendar for enabling and disabling date chooser for specific days in week


 Smart and fabulous introduction on using a custom dialog, especially a datepicker dialog in Android.   
 The example provides a possible solution to exlude specific days from users selection, e.g.   
 you can prevent the user to choose a date of a sunday.  
 
![Screen](https://github.com/ashokslsk/CustomCalendar-Android/blob/master/AndroidCalendarcheck/screens/allowed.png)
![Screen](https://github.com/ashokslsk/CustomCalendar-Android/blob/master/AndroidCalendarcheck/screens/notAllowed.png)

**STEPS**  
- Define custom layout with all UI elements, that you want to be included in your custom dialog.
  The following example provides a layout for a datepicker as shown in the screenshots above.
```sh 
<!-- res/layout/datepicker_layout.xml -->
<?xml version="1.0" encoding="utf-8"?>  
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
              android:layout_width="fill_parent"
              android:layout_height="wrap_content"
              android:orientation="vertical">
 
    <TextView
            android:id="@+id/dialog_dateview"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:textAppearance="?android:attr/textAppearanceLarge"
            android:layout_margin="12dp"
            android:text=""
            android:layout_gravity="center"/>
 
    <DatePicker
            android:id="@+id/dialog_datepicker"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_gravity="center"
            android:calendarViewShown="false"/>
 
</LinearLayout>
```
  
- Create a datepicker dialog based on a custom layout.

```sh 
public class DatePickerTestActivity extends Activity {
 
Button datePickerShowDialogButton = null;
 
@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datepickertest);
        // Button to show datepicker
        datePickerShowDialogButton = 
        (Button) actionBar.getCustomView().findViewById(
           R.id.activity_datepickertest_datebutton
        );
        datePickerShowDialogButton.setOnClickListener(
          new View.OnClickListener() {
             public void onClick(View v) {
                 showDatePicker();
             }
          });
    }
 
    /**
     * Builds a custom dialog based on the defined layout 
     * 'res/layout/datepicker_layout.xml' and shows it
     */
    public void showDatePicker() {
        // Initializiation
        LayoutInflater inflater = (LayoutInflater) getLayoutInflater();
        final AlertDialog.Builder dialogBuilder = 
        new AlertDialog.Builder(this);
        View customView = inflater.inflate(R.layout.datepicker_layout, null);
        dialogBuilder.setView(customView);
        Calendar now = Calendar.getInstance();
        final DatePicker datePicker = 
            (DatePicker) customView.findViewById(R.id.dialog_datepicker);
        final TextView dateTextView = 
            (TextView) customView.findViewById(R.id.dialog_dateview);
        final SimpleDateFormat dateViewFormatter = 
            new SimpleDateFormat("EEEE, dd.MM.yyyy", Locale.GERMANY);
        final SimpleDateFormat formatter = 
            new SimpleDateFormat("dd.MM.yyyy", Locale.GERMANY);
        // Minimum date
        Calendar minDate = Calendar.getInstance();
        try {
            minDate.setTime(formatter.parse("12.12.2010"));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        datePicker.setMinDate(minDate.getTimeInMillis());
        // View settings
        dialogBuilder.setTitle("Choose a date");
        Calendar choosenDate = Calendar.getInstance();
        int year = choosenDate.get(Calendar.YEAR);
        int month = choosenDate.get(Calendar.MONTH);
        int day = choosenDate.get(Calendar.DAY_OF_MONTH);
        try {
            Date choosenDateFromUI = formatter.parse(
                datePickerShowDialogButton.getText().toString()
            );
            choosenDate.setTime(choosenDateFromUI);
            year = choosenDate.get(Calendar.YEAR);
            month = choosenDate.get(Calendar.MONTH);
            day = choosenDate.get(Calendar.DAY_OF_MONTH);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Calendar dateToDisplay = Calendar.getInstance();
        dateToDisplay.set(year, month, day);
        dateTextView.setText(
            dateViewFormatter.format(dateToDisplay.getTime())
        );
        // Buttons
        dialogBuilder.setNegativeButton(
            "Go to today", 
            new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    datePickerShowDialogButton.setText(
                        formatter.format(now.getTime())
                    );
                    dialog.dismiss();
                }
            }
        );
        dialogBuilder.setPositiveButton(
            "Choose", 
            new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Calendar choosen = Calendar.getInstance();
                    choosen.set(
                        datePicker.getYear(), 
                        datePicker.getMonth(), 
                        datePicker.getDayOfMonth()
                    );
                    datePickerShowDialogButton.setText(
                        dateViewFormatter.format(choosen.getTime())
                    );
                    dialog.dismiss();
                }
            }
        );
        final AlertDialog dialog = dialogBuilder.create();
        // Initialize datepicker in dialog atepicker
        datePicker.init(
            year, 
            month, 
            day, 
            new DatePicker.OnDateChangedListener() {
                public void onDateChanged(DatePicker view, int year, 
                    int monthOfYear, int dayOfMonth) {
                    Calendar choosenDate = Calendar.getInstance();
                    choosenDate.set(year, monthOfYear, dayOfMonth);
                    dateTextView.setText(
                        dateViewFormatter.format(choosenDate.getTime())
                    );
                    if (choosenDate.get(Calendar.DAY_OF_WEEK) == 
                        Calendar.SUNDAY || 
                        now.compareTo(choosenDate) < 0) {
                        dateTextView.setTextColor(
                            Color.parseColor("#ff0000")
                        );
                        ((Button) dialog.getButton(
                        AlertDialog.BUTTON_POSITIVE))
                            .setEnabled(false);
                    } else {
                        dateTextView.setTextColor(
                            Color.parseColor("#000000")
                        );
                        ((Button) dialog.getButton(
                        AlertDialog.BUTTON_POSITIVE))
                            .setEnabled(true);
                    }
                }
            }
        );
        // Finish
        dialog.show();
    }
}
```

- You can use your usual layouts with a simple button to call this custom calendar
```sh
<!-- res/layout/activity_datepickertest.xml -->
<RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:paddingLeft="@dimen/activity_horizontal_margin"
    android:paddingRight="@dimen/activity_horizontal_margin"
    android:paddingTop="@dimen/activity_vertical_margin"
    android:paddingBottom="@dimen/activity_vertical_margin"
    tools:context=".DatePickerTestActvity">
 
    <Button
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:text="@string/button_initialLabel"
            android:id="@+id/activity_datepickertest_datebutton"
            android:layout_centerVertical="true"
            android:layout_centerHorizontal="true"/>
</RelativeLayout>
```
- Execute it as simple as it is 

* [For more codes, funs and for queries be in touch with @ashokslsk ](https://github.com/ashokslsk)
