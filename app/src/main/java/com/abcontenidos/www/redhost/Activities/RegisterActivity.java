package com.abcontenidos.www.redhost.Activities;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.util.Patterns;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.abcontenidos.www.redhost.Fragments.DatePickerFragment;
import com.abcontenidos.www.redhost.Objets.User;
import com.abcontenidos.www.redhost.R;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    Toolbar myToolbar;
    EditText name, mail, pass, repass, address, birthday;
    Spinner spinner;
    ImageView imageProfile;
    Button buttonRegister;
    static final int REQUEST_TAKE_PHOTO = 1;
    String mCurrentPhotoPath;
    File photoFile;
    Boolean flag_take_picture = false;
    TextInputLayout tilName, tilMail, tilPass, tilRePass, tilAddress, tilBirthday;
    ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        myToolbar = findViewById(R.id.my_toolbar_register);
        setSupportActionBar(myToolbar);

        name = findViewById(R.id.et_register_fullname);
        mail = findViewById(R.id.et_register_mail);
        pass = findViewById(R.id.et_register_pass);
        repass = findViewById(R.id.et_register_repass);
        address = findViewById(R.id.et_register_address);
        birthday = findViewById(R.id.et_register_birthday);
        imageProfile = findViewById(R.id.image_register);
        buttonRegister = findViewById(R.id.save_register);
        spinner = findViewById(R.id.et_register_gender);

        tilName = findViewById(R.id.tilName);
        tilMail = findViewById(R.id.tilMail);
        tilPass = findViewById(R.id.tilPass);
        tilRePass = findViewById(R.id.tilRePass);
        tilAddress = findViewById(R.id.tilAddress);
        tilBirthday = findViewById(R.id.tilBirthday);


        progressBar = findViewById(R.id.progressBar);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource (this,
                R.array.gender_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        imageProfile.setOnClickListener(this);
        buttonRegister.setOnClickListener(this);
        birthday.setOnClickListener(this);
        spinner.setOnItemSelectedListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.save_register:
                // PRIMERO VALIDAR LOS DATOS
                if(validarDatos()) {
                    String birthdayString = null;
                    // formatear la fecha
                    try {
                        final String inputFormat = "dd/MM/yyyy";
                        final String outputFormat = "yyyy-MM-dd";
                        birthdayString = TimeStampConverter(inputFormat, birthday.getText().toString(),
                                outputFormat);
                    } catch (ParseException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    User user = new User();
                    user.setPass(pass.getText().toString());
                    user.setGender(spinner.getSelectedItem().toString());
                    user.setBirthday(birthdayString);
                    user.setAddress(address.getText().toString());
                    user.setMail(mail.getText().toString());
                    user.setName(name.getText().toString());

                    Bitmap bm = ((BitmapDrawable) imageProfile.getDrawable()).getBitmap();
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    bm.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                    byte[] encodedString = byteArrayOutputStream.toByteArray();
                    String toBase64 = Base64.encodeToString(encodedString, Base64.DEFAULT);
                    user.setImage(toBase64);

                    Gson gson = new Gson();
                    final String json = gson.toJson(user);
                    RequestQueue queue = Volley.newRequestQueue(this);
                    String url = "http://redoff.bithive.cloud/ws/register";

                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    Log.d("registrando", response);
                                    if (response.equals("Ok")) {
                                        progressBar.setVisibility(View.GONE);
                                        AlertDialog alertDialog = new AlertDialog.Builder(RegisterActivity.this).create();
                                        alertDialog.setCanceledOnTouchOutside(false);
                                        //alertDialog.onTouchEvent();
                                        alertDialog.setTitle("Usuario creado");
                                        alertDialog.setMessage("Ingrese a su cuenta de correo para activar el usuario");
                                        alertDialog.setIcon(R.drawable.ic_launcher_background);

                                        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                finish();
                                            }
                                        });

                                        alertDialog.show();
                                    } else {
                                        progressBar.setVisibility(View.GONE);
                                        AlertDialog alertDialog = new AlertDialog.Builder(getApplicationContext()).create();
                                        alertDialog.setTitle("Hubo un error en la creacion del usuario");
                                        alertDialog.setMessage("Reintente crear el usuario");
                                        alertDialog.setIcon(R.drawable.ic_launcher_background);

                                        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                finish();
                                            }
                                        });

                                        alertDialog.show();
                                    }
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            showToastMessage("That didn't work! -- " + error.toString());
                        }
                    }) {
                        @Override
                        public byte[] getBody() {
                            return json.toString().getBytes();
                        }

                        @Override
                        public String getBodyContentType() {
                            return "application/json; charset=utf-8";
                        }
                    };
                    stringRequest.setRetryPolicy(new DefaultRetryPolicy(0,
                            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

                    // Add the request to the RequestQueue.
                    queue.add(stringRequest);
                    progressBar.setVisibility(View.VISIBLE);
                    buttonRegister.setEnabled(false);
                }
                break;
            case R.id.et_register_birthday:
                showDatePickerDialog(birthday);
                break;

            case R.id.image_register:
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                // Ensure that there's a camera activity to handle the intent
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    // Create the File where the photo should go
                    photoFile = null;
                    try {
                        photoFile = createImageFile();
                    } catch (IOException ex) {
                        // Error occurred while creating the File
                    }
                    // Continue only if the File was successfully created
                    if (photoFile != null) {
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                        startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
                    }
                }
                break;

        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    private static String TimeStampConverter(final String inputFormat, String inputTimeStamp, final String outputFormat) throws ParseException {
        return new SimpleDateFormat(outputFormat).format(new SimpleDateFormat(inputFormat).parse(inputTimeStamp));
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void showDatePickerDialog(final EditText editText) {
        DatePickerFragment newFragment = DatePickerFragment.newInstance(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                // +1 because january is zero
                final String selectedDate = twoDigits(day) + "/" + twoDigits(month+1) + "/" + year;
                editText.setText(selectedDate);
            }
        });
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    private String twoDigits(int n) {
        return (n<=9) ? ("0"+n) : String.valueOf(n);
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "MAG_JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        if (requestCode == REQUEST_TAKE_PHOTO) {
            //imageProfile.setImageBitmap(mImageBitmap);
            //imageProfile.setImageURI(Uri.parse(mCurrentPhotoPath));
            //Picasso.get().load(mCurrentPhotoPath).into(imageProfile);
            flag_take_picture = true;
            setPic();
        }
    }

    private void setPic() {
        // Get the dimensions of the View
        int targetW = imageProfile.getWidth();
        int targetH = imageProfile.getHeight();

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        ByteArrayOutputStream blob = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 0 /* Ignored for PNGs */, blob);
        byte[] arrayImagen = blob.toByteArray();

        Bitmap bitmap1 = BitmapFactory.decodeByteArray(arrayImagen, 0, arrayImagen.length);
        imageProfile.setImageBitmap(bitmap);
    }

    private String getImage() {
        // Get the dimensions of the View
        int targetW = imageProfile.getWidth();
        int targetH = imageProfile.getHeight();

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        ByteArrayOutputStream blob = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 0 /* Ignored for PNGs */, blob);
        String image = blob.toString();

        return image;
    }

    private boolean esNombreValido(String nombre) {
        Pattern patron = Pattern.compile("^[a-zA-Z ]+$");
        if (!patron.matcher(nombre).matches() || nombre.length() > 30) {
            tilName.setError("Nombre inválido");
            return false;
        } else {
            tilName.setError(null);
        }

        return true;
    }

    private boolean esPassValido(String repass) {
        if (!repass.equals(tilPass.getEditText().getText().toString())) {
            tilRePass.setError("Las contraseñas no coinciden");
            return false;
        } else {
            tilRePass.setError(null);
        }

        return true;
    }

    private boolean esCorreoValido(String correo) {
        if (!Patterns.EMAIL_ADDRESS.matcher(correo).matches()) {
            tilMail.setError("Correo electrónico inválido");
            return false;
        } else {
            tilMail.setError(null);
        }

        return true;
    }

    private boolean esFechaValida(String fecha) {
        if (fecha.equals("")) {
            tilBirthday.setError("Ingrese una fecha");
            return false;
        } else {
            tilBirthday.setError(null);
        }

        return true;
    }

    private boolean validarDatos() {
        String nombre = tilName.getEditText().getText().toString();
        String pass = tilRePass.getEditText().getText().toString();
        String correo = tilMail.getEditText().getText().toString();

        String fecha = birthday.getText().toString();

        boolean a = esNombreValido(nombre);
        boolean b = esPassValido(pass);
        boolean c = esCorreoValido(correo);
        boolean d = esFechaValida(fecha);

        if (a && b && c && d) {
            return true;
        }else {
            return false;
        }

    }

    private void showToastMessage (String message) {
        Toast toast = Toast.makeText(this, message, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }


}
