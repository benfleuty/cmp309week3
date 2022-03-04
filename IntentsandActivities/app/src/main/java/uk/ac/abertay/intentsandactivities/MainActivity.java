package uk.ac.abertay.intentsandactivities;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Patterns;
import android.view.View;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private static final int GET_CONTACT_REQUEST = 1;
    Toast lastToast;

    public void Toaster(String text) {
        Toaster(text, true);
    }

    public void Toaster(String text, boolean cancel) {
        Toast nextToast = Toast.makeText(this, text, Toast.LENGTH_SHORT);
        if (cancel && lastToast != null) {
            lastToast.cancel();
        }
        nextToast.show();
        lastToast = nextToast;
    }

    /**
     * Gets the selected contact's phone number and put it into the EditText field
     */
    private void getContactNumber(Intent intent) {
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
            /* Don't forget to change the "inputFieldID" to the id of your EditText field */
            ((EditText) findViewById(R.id.etInputField)).setText(number); // set the text of the input field to the phone number string
        }
    }

    public void handleClick(View view) {
        int action = view.getId();
        int btnDialNumberId = findViewById(R.id.btnDialNumber).getId();
        int btnGetContactId = findViewById(R.id.btnGetContact).getId();
        int btnGoToWebId = findViewById(R.id.btnGoToWeb).getId();

        EditText inputField = findViewById(R.id.etInputField);

        if (action == btnDialNumberId) {
            btnDialNumberClicked(inputField);
        } else if (action == btnGetContactId) {
            btnGetContactClicked(inputField);
        } else if (action == btnGoToWebId) {
            btnGoToWebClicked(inputField);
        } else {
            Toaster("error");
        }
    }

    private void btnDialNumberClicked(EditText inputField) {
        String contents = inputField.getText().toString();
        if (contents.length() == 0) {
            Toaster("Please enter a phone number");
            return;
        }

        if(!contents.matches("[0-9#*+]*")){
            Toaster("The entered value is not a valid phone number!");
            return;
        }

        String uri = "tel:" + contents;

        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse(uri));
        startActivity(intent);
    }

    private void btnGetContactClicked(EditText inputField) {
        Toaster("Getting contacts");
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
        startActivityForResult(intent, GET_CONTACT_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) return;
        if (requestCode == GET_CONTACT_REQUEST && resultCode == RESULT_OK) {
            getContactNumber(data);
        }
    }

    private void btnGoToWebClicked(EditText inputField) {
        String contents = inputField.getText().toString();
        if (contents.length() == 0) {
            Toaster("Please enter a web address");
            return;
        }

        if (!Patterns.WEB_URL.matcher(contents).matches()) {
            Toaster("The address entered is not valid.");
            return;
        }

        Toaster("Opening the browser to: " + contents);
        Intent webBrowserIntent = new Intent(MainActivity.this, BrowserActivity.class);
        webBrowserIntent.putExtra("webAddressToLoad",contents);
        startActivity(webBrowserIntent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}