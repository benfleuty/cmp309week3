package uk.ac.abertay.intentsandactivities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {
    /** Gets the selected contact's phone number and put it into the EditText field */
    private void getContactNumber(Intent intent){
        // 1. Get data from Intent
        Uri contactUri = intent.getData();
        // 2. Create a search filter for all values we need by putting their types into an array.
        String[] projection = new String[]{
                ContactsContract.CommonDataKinds.Phone.NUMBER // we only use 1 here - the phone number
        };
        // 3. Run a query on the SQLite Database containing all contacts using the parameters from above
        // 	  ignore the last 3 nulls for now.
        Cursor cursor = getContentResolver().query(contactUri, projection, null, null, null);
        // 4. If the search returned a valid result in the cursor object, get the phone number.
        if (cursor != null && cursor.moveToFirst()) {
            // Since we will only have 1 entry for the phone number,
            // no need to loop through the entire table, just get the FIRST row.
            int numberIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER); // find out what the index for the phone number column is
            String number = cursor.getString(numberIndex); // use this index to get the string value from the column in the current row
            /** Don't forget to change the "inputFieldID" to the id of your EditText field */
//            ((EditText)findViewById(R.id.inputFieldID)).setText(number); // set the text of the input field to the phone number string
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}