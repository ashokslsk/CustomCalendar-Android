package com.example.androidcalendarcheck;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.net.ParseException;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

public class MainActivity extends Activity {

	Button datePickerShowDialogButton = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		// Button to show datepicker
		datePickerShowDialogButton = (Button) findViewById(R.id.activity_datepickertest_datebutton);
		datePickerShowDialogButton
				.setOnClickListener(new View.OnClickListener() {
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
		final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
		View customView = inflater.inflate(R.layout.custom_date_picker, null);
		dialogBuilder.setView(customView);
		final Calendar now = Calendar.getInstance();
		final DatePicker datePicker = (DatePicker) customView
				.findViewById(R.id.dialog_datepicker);
		final TextView dateTextView = (TextView) customView
				.findViewById(R.id.dialog_dateview);
		final SimpleDateFormat dateViewFormatter = new SimpleDateFormat(
				"EEEE, dd.MM.yyyy", Locale.GERMANY);
		final SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy",
				Locale.GERMANY);
		// Minimum date
		Calendar minDate = Calendar.getInstance();
		try {
			try {
				minDate.setTime(formatter.parse("12.12.2010"));
			} catch (java.text.ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
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
			Date choosenDateFromUI = formatter.parse(datePickerShowDialogButton
					.getText().toString());
			choosenDate.setTime(choosenDateFromUI);
			year = choosenDate.get(Calendar.YEAR);
			month = choosenDate.get(Calendar.MONTH);
			day = choosenDate.get(Calendar.DAY_OF_MONTH);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Calendar dateToDisplay = Calendar.getInstance();
		dateToDisplay.set(year, month, day);
		dateTextView.setText(dateViewFormatter.format(dateToDisplay.getTime()));
		// Buttons
		dialogBuilder.setNegativeButton("Go to today",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						datePickerShowDialogButton.setText(formatter.format(now
								.getTime()));
						dialog.dismiss();
					}
				});
		dialogBuilder.setPositiveButton("Choose",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						Calendar choosen = Calendar.getInstance();
						choosen.set(datePicker.getYear(),
								datePicker.getMonth(),
								datePicker.getDayOfMonth());
						datePickerShowDialogButton.setText(dateViewFormatter
								.format(choosen.getTime()));
						dialog.dismiss();
					}
				});
		final AlertDialog dialog = dialogBuilder.create();
		// Initialize datepicker in dialog atepicker
		datePicker.init(year, month, day,
				new DatePicker.OnDateChangedListener() {
					public void onDateChanged(DatePicker view, int year,
							int monthOfYear, int dayOfMonth) {
						Calendar choosenDate = Calendar.getInstance();
						choosenDate.set(year, monthOfYear, dayOfMonth);
						dateTextView.setText(dateViewFormatter
								.format(choosenDate.getTime()));
						if (choosenDate.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY
								|| now.compareTo(choosenDate) < 0) {
							dateTextView.setTextColor(Color
									.parseColor("#ff0000"));
							((Button) dialog
									.getButton(AlertDialog.BUTTON_POSITIVE))
									.setEnabled(false);
						} else {
							dateTextView.setTextColor(Color
									.parseColor("#000000"));
							((Button) dialog
									.getButton(AlertDialog.BUTTON_POSITIVE))
									.setEnabled(true);
						}
					}
				});
		// Finish
		dialog.show();
	}
}