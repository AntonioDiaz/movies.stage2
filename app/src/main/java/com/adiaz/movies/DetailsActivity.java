package com.adiaz.movies;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class DetailsActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_details);
		Uri uriMovie = getIntent().getData();
		Cursor cursor = getContentResolver().query(uriMovie, null, null, null, null);

		Toast.makeText(this, cursor.getCount() + "", Toast.LENGTH_SHORT).show();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_details, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.action_settings:
				Intent intentSettings = new Intent(this, SettingsActivity.class);
				startActivity(intentSettings);
				break;
		}
		return super.onOptionsItemSelected(item);
	}
}
