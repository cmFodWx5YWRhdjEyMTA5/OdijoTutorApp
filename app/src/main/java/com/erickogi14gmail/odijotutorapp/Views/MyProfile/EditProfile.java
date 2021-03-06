package com.erickogi14gmail.odijotutorapp.Views.MyProfile;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.erickogi14gmail.odijotutorapp.Adapters.SubjectAdapter;
import com.erickogi14gmail.odijotutorapp.Configs;
import com.erickogi14gmail.odijotutorapp.Data.Models.Levels;
import com.erickogi14gmail.odijotutorapp.Data.Models.Subjects;
import com.erickogi14gmail.odijotutorapp.Data.Parsers.SubjectParser;
import com.erickogi14gmail.odijotutorapp.Helper.PrefManager;
import com.erickogi14gmail.odijotutorapp.Interfaces.clickListenerSubject;
import com.erickogi14gmail.odijotutorapp.MyApplication;
import com.erickogi14gmail.odijotutorapp.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class EditProfile extends AppCompatActivity {
    private static ArrayList<Subjects> s1 = new ArrayList<>();
    private final int CAMERA_REQUEST = 1888;
    PrefManager prefManager;
    ArrayList<Subjects> subjectsUnadded = new ArrayList<>();
    LinearLayout linearLayoutEmpty;
    private View view;
    private Subjects subjects;
    private ArrayList<Subjects> subjectses = new ArrayList<>();
    private SubjectAdapter subjectAdapter;
    private ArrayList<Subjects> arrayList;
    private RecyclerView recyclerView;
    private StaggeredGridLayoutManager mStaggeredLayoutManager;
    private TextView txtName, txtEmail;
    private Spinner mSpinnerZone;
    private Button btnAddSubjects, btnImageView, btnSave;
    private ImageView img;
    private String imagePath = "1";
    private Bitmap bitmap = null;
    private TextInputEditText edtDescription;
    private TextInputEditText edtWorkHours;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        ActionBar bar = getActionBar();
//        bar.setBackgroundDrawable(new ColorDrawable(this.getResources().getColor(R.color.colorPrimaryDark)));
//        setContentView(R.layout.activity_edit_profile);
        prefManager = new PrefManager(EditProfile.this);
//        bar.setDisplayHomeAsUpEnabled(true);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
       // ((AppCompatActivity) getActivity()).getSupportActionBar().hide();

        txtName = findViewById(R.id.txt_name);
        txtEmail = findViewById(R.id.txt_emailAdress);
        mSpinnerZone = findViewById(R.id.spinnerZone);
        btnAddSubjects = findViewById(R.id.btn_add_subject);

        edtDescription = findViewById(R.id.edt_description);
        edtWorkHours = findViewById(R.id.edt_workhours);


        btnImageView = findViewById(R.id.btn_imageview);
        img = findViewById(R.id.imageView);
        btnSave = findViewById(R.id.btn_save);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateDetails();
            }
        });
        btnImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            }
        });


        btnAddSubjects.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getSubjects("");


            }
        });
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            }
        });

        intent=getIntent();


        txtName.setText(intent.getStringExtra("Name"));
        txtEmail.setText(intent.getStringExtra("Email"));
        edtWorkHours.setText(intent.getStringExtra("work_hours"));
        edtDescription.setText(intent.getStringExtra("description"));

        subjectses = (ArrayList<Subjects>) intent.getSerializableExtra("data");
        String[] Zones =intent.getStringArrayExtra("Zones");
        int zone = intent.getIntExtra("zone",1);

        ArrayAdapter<String> simpleAdapterCourse = new ArrayAdapter<String>(EditProfile.this, android.R.layout.simple_spinner_dropdown_item, Zones);
        mSpinnerZone.setAdapter(simpleAdapterCourse);
        mSpinnerZone.setSelection(zone);


        String image = prefManager.getImg();
        //Toast.makeText(getContext(), ""+image, Toast.LENGTH_SHORT).show();

        if (!image.equals("null")) {
            imagePath = image;
            bitmap = getThumbnail(image);
            img.setImageBitmap(getThumbnail(image));
        } else {

        }


        initViews();
    }
    private void UpdateDetails() {
        if(bitmap!=null&&!txtName.getText().toString().equals("")&&!txtEmail.getText().toString().equals("")
                &&!edtDescription.getText().toString().equals("")&&!edtDescription.getText().toString().equals("")) {
            String image = getStringImage(bitmap);

            String mobile = intent.getStringExtra("mobile");
            String name = txtName.getText().toString();
            String email = txtEmail.getText().toString();
            String img = imagePath;
            String zone = mSpinnerZone.getSelectedItem().toString();
            String json = prefManager.getSubjectstring();
            String workhours = edtWorkHours.getText().toString();
            String des = edtDescription.getText().toString();

            if (edtDescription.getText().toString().equals("")) {
                des = "ww";
            }

            update(name, email, mobile, zone, image, json, des, workhours);
            //  prefManager.updateUser(name,email,img,zone);
            //fragmentManager.popBackStack();
            //popOutFragments();
        }else {
            Toast.makeText(this, "Some Fields Are Missing", Toast.LENGTH_SHORT).show();
        }




    }

    private void initViews() {
        subjectses = prefManager.getSubjects();
        recyclerView = findViewById(R.id.recycler_view);
        subjectAdapter = new SubjectAdapter(EditProfile.this, 0, subjectses, new clickListenerSubject() {
            @Override
            public void onBtnDeleteClicked(int pos) {
                subjectses.remove(pos);
                prefManager.saveSubjects(subjectses);
                initViews();
            }

            @Override
            public void onBtnAddClicked(int adapterPosition) {

            }

        });
        subjectAdapter.notifyDataSetChanged();
        mStaggeredLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(mStaggeredLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(subjectAdapter);


    }

    private void getSubjects(final String mobile) {
        final ProgressDialog loading = ProgressDialog.show(EditProfile.this, "Fetching Subjects...", "Please wait...", false, false);






        StringRequest strReq = new StringRequest(Request.Method.POST,
                Configs.GET_SUBJECTS_URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                // Log.d("response", response.toString());
                //progressDialog.dismiss();
                // dialoge.show();
                try {
                    Log.d("erfh","errrrr"+response);

                    JSONObject responseObj = new JSONObject(response);

                    // Parsing json object response
                    // response will be a json object
                    boolean error = responseObj.getBoolean("error");
                    //  String message = responseObj.getString("message");

                    // checking for error, if not error SMS is initiated
                    // device should receive it shortly
                    if (!error) {
                        Log.d("erf","errrrr");
                        loading.dismiss();

//                        String a = null;
//
//                        try {
//                            JSONObject jObj = new JSONObject(response);
//                            JSONArray jsonArray = jObj.getJSONArray("subjects");
//                            a = jObj.getString("subjects");
//
//
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//
//                        Gson gson = new Gson();
//                        Type collectionType = new TypeToken<Collection<Subjects>>() {
//
//                        }.getType();
//                        ArrayList<Subjects> subjectses1 = new ArrayList<>();
//                        subjectses1 = gson.fromJson(a, collectionType);
                        ArrayList<Subjects> subjectses1 = new ArrayList<>();
                        //subjectses1 = gson.fromJson(a, collectionType);

                        subjectses1 = SubjectParser.parseData(response);
                        Log.d("response",response);



                        popup(subjectses1);


                    } else {
                        loading.dismiss();
                        String message = responseObj.getString("message");
                        //relativeLayoutSignup.setVisibility(View.VISIBLE);
                        //relativeLayoutOtp.setVisibility(View.GONE);
                        Toast.makeText(EditProfile.this,
                                "Error: " + message,
                                Toast.LENGTH_LONG).show();
                        // dialoge.dismiss();

                    }

                    // hiding the progress bar
                    // progressDialog.dismiss();


                } catch (JSONException e) {
                    loading.dismiss();
                    e.printStackTrace();
                    Log.d("ERRRRR", e.toString());
                    Toast.makeText(EditProfile.this,
                            "Error: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                    // dialoge.dismiss();
                    //relativeLayoutSignup.setVisibility(View.VISIBLE);
                    // relativeLayoutOtp.setVisibility(View.GONE);
                    // progressDialog.dismiss();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                loading.dismiss();
                //relativeLayoutSignup.setVisibility(View.VISIBLE);
                //relativeLayoutOtp.setVisibility(View.GONE);
                Log.e("error", "Error: " + error.getMessage());
                Toast.makeText(EditProfile.this,
                        error.getMessage(), Toast.LENGTH_SHORT).show();
                //progressDialog.dismiss();
                // dialoge.dismiss();
                //progressBar.setVisibility(View.GONE);
            }
        }) {

            /**
             * Passing user parameters to our server
             * @return
             */
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("all", "all");
                params.put("user", "TUTOR");

                Log.e("posting params", "Posting params: " + params.toString());

                return params;
            }


        };

        // Adding request to request queue
        MyApplication.getInstance().addToRequestQueue(strReq);
    }

    private void update(final String name, final String email, final String mobile, final String zone,
                        final String image, final String subjects, final String description, final String work_hours) {
        final ProgressDialog loading = ProgressDialog.show(EditProfile.this, "Uploading...", "Please wait...", false, false);

        StringRequest strReq = new StringRequest(Request.Method.POST,
                Configs.UPDATE_PROFILE_URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                // Log.d("response", response.toString());
                //progressDialog.dismiss();
                // dialoge.show();
                try {
                    JSONObject responseObj = new JSONObject(response);

                    // Parsing json object response
                    // response will be a json object
                    boolean error = responseObj.getBoolean("error");
                    //  String message = responseObj.getString("message");

                    // checking for error, if not error SMS is initiated
                    // device should receive it shortly
                    if (!error) {

                        loading.dismiss();
                        prefManager.updateUser(name, email, imagePath, zone, description, work_hours);
                        Toast.makeText(EditProfile.this, "Profile Updated Successfully", Toast.LENGTH_LONG).show();
                      //  popOutFragments();

                        // boolean flag saying device is waiting for sms

                        //pref.setIsWaitingForSms(true);
                        // progressDialog.dismiss();
                        // moving the screen to next pager item i.e otp screen
                        //dialoge.show();


                        // Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();

                    } else {
                        loading.dismiss();
                        String message = responseObj.getString("message");
                        //relativeLayoutSignup.setVisibility(View.VISIBLE);
                        //relativeLayoutOtp.setVisibility(View.GONE);
                        Toast.makeText(EditProfile.this,
                                "Error: " + message,
                                Toast.LENGTH_LONG).show();
                        // dialoge.dismiss();

                    }

                    // hiding the progress bar
                    // progressDialog.dismiss();


                } catch (JSONException e) {
                    loading.dismiss();
                    e.printStackTrace();
                    Log.d("ERRRRR", e.toString());
                    Toast.makeText(EditProfile.this,
                            "Error: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                    // dialoge.dismiss();
                    //relativeLayoutSignup.setVisibility(View.VISIBLE);
                    // relativeLayoutOtp.setVisibility(View.GONE);
                    // progressDialog.dismiss();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                loading.dismiss();
                //relativeLayoutSignup.setVisibility(View.VISIBLE);
                //relativeLayoutOtp.setVisibility(View.GONE);
                Log.e("error", "Error: " + error.getMessage());
                Toast.makeText(EditProfile.this,
                        error.getMessage(), Toast.LENGTH_SHORT).show();
                //progressDialog.dismiss();
                // dialoge.dismiss();
                //progressBar.setVisibility(View.GONE);
            }
        }) {

            /**
             * Passing user parameters to our server
             * @return
             */
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("name", name);
                params.put("email", email);
                params.put("mobile", mobile);
                params.put("zone", zone);
                params.put("image", image);
                params.put("json", subjects);
                params.put("description", description);
                params.put("work_hours", work_hours);
                params.put("user", "TUTOR");

                Log.e("posting params", "Posting params: " + params.toString());

                return params;
            }

        };

        // Adding request to request queue
        MyApplication.getInstance().addToRequestQueue(strReq);
    }

    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    void setSubjectsView() {

    }

    void popup(final ArrayList<Subjects> subjGects) {
        final View popupview = LayoutInflater.from(EditProfile.this).inflate(R.layout.subjects_pop_up_view, null);

        final PopupWindow popupWindow = new PopupWindow(popupview, WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT / 2);
        subjectsUnadded.clear();
        if (prefManager.getSubjects() != null) {
            ArrayList<Subjects> ps = prefManager.getSubjects();
            HashMap<String, String> subjects = new HashMap<>();

            for (int s = 0; s < ps.size(); s++) {
                subjects.put("sub" + String.valueOf(s), ps.get(s).getSubject_name());
            }
            for (int a = 0; a < subjGects.size(); a++) {
                if (subjects.containsValue(subjGects.get(a).getSubject_name())) {

                } else {
                    subjectsUnadded.add(subjGects.get(a));
                }
            }

        } else {
            subjectsUnadded = subjGects;
        }


        recyclerView = popupview.findViewById(R.id.recycler_view);
        linearLayoutEmpty = popupview.findViewById(R.id.empty_layout);

        arrayList = new ArrayList<>();
        subjectAdapter = new SubjectAdapter(EditProfile.this, 1, subjectsUnadded, new clickListenerSubject() {




            @Override
            public void onBtnDeleteClicked(int pos) {

            }

            @Override
            public void onBtnAddClicked(final int adapterPosition) {
               // String[] level = {"All", "8:4:4", "Diploma", "Degree", "Higher Diploma", "Certificate", "KSCE", "KCPE", "IGCSE"};
                ArrayList<Levels> levelses=subjectsUnadded.get(adapterPosition).getLevelses();


                String[] level = new String[levelses.size()];
                for(int a=0;a<levelses.size();a++){
                    level[a]=levelses.get(a).getLevel();
                }


                final Dialog dialog = new Dialog(EditProfile.this);
                dialog.setContentView(R.layout.dialog_subject_details);
                dialog.setTitle("Edit");
                dialog.setCanceledOnTouchOutside(false);
                final TextInputEditText edtRate = dialog.findViewById(R.id.edt_rate);
                final TextInputEditText edtQualifications = dialog.findViewById(R.id.edt_qualifications);
                final Spinner spinnerLevel = dialog.findViewById(R.id.spinner_level);

                final ArrayAdapter<String> simpleAdapterCourse = new ArrayAdapter<String>(EditProfile.this, android.R.layout.simple_spinner_dropdown_item, level);
                spinnerLevel.setAdapter(simpleAdapterCourse);
                //spinnerLevel.setSelection(zone);

                Button btnCancel = dialog.findViewById(R.id.btn_cancel);
                Button btnProceed = dialog.findViewById(R.id.btn_add);


                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                btnProceed.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (edtQualifications.getText().toString() == "" || edtRate.getText().toString().equals("") || edtQualifications.getText().toString().isEmpty()) {
                            if (edtQualifications.getText().toString().isEmpty() || edtQualifications.getText().toString().equals("")) {
                                edtQualifications.setError("Required");
                            } else {
                                edtRate.setError("Required");
                            }

                        } else {

                            String rate = edtRate.getText().toString();
                            String qualifications = edtQualifications.getText().toString();
                            String level = spinnerLevel.getSelectedItem().toString();

                            try {
                                if (prefManager.getSubjects().isEmpty()) {

                                    subjectsUnadded.get(adapterPosition).setSubject_rate(rate);
                                    subjectsUnadded.get(adapterPosition).setLevel(level);
                                    subjectsUnadded.get(adapterPosition).setQualifications(qualifications);
                                    arrayList.add(subjectsUnadded.get(adapterPosition));
                                } else {
                                    subjectsUnadded.get(adapterPosition).setLevel(level);
                                    subjectsUnadded.get(adapterPosition).setQualifications(qualifications);

                                    subjectsUnadded.get(adapterPosition).setSubject_rate(rate);

                                    arrayList = prefManager.getSubjects();
                                    arrayList.add(subjectsUnadded.get(adapterPosition));
                                }
                            } catch (Exception nm) {
                                subjectsUnadded.get(adapterPosition).setLevel(level);
                                subjectsUnadded.get(adapterPosition).setQualifications(qualifications);

                                subjectsUnadded.get(adapterPosition).setSubject_rate(rate);

                                //arrayList = prefManager.getSubjects();
                                s1.add(subjectsUnadded.get(adapterPosition));
                                arrayList = s1;
                            }


                            prefManager.saveSubjects(arrayList);
                            // subjectAdapter.notifyDataSetChanged();
                            // subjectAdapter.notifyItemChanged(adapterPosition);
                            // subjectAdapter.updateList();

                            initViews();
                            dialog.dismiss();
                            popupWindow.dismiss();


                        }
                    }
                });
                dialog.show();


            }
        });
        subjectAdapter.notifyDataSetChanged();
        mStaggeredLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(mStaggeredLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(subjectAdapter);
        if (subjectAdapter.getItemCount() > 0) {
            linearLayoutEmpty.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);

        } else {
            linearLayoutEmpty.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }

        Button buttonBack = popupview.findViewById(R.id.btn_back);
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });


        popupWindow.showAtLocation(popupview, Gravity.CENTER, 0, 0);


    }


    public void onActivityResult(int resquestCode, int resultCode, Intent data) {
        if (resquestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            Bitmap sb = Bitmap.createScaledBitmap(photo, 100, 100, false);

            img.setImageBitmap(sb);
            bitmap = sb;
            if (storeImage(sb)) {
                // controller.toast("Image Retrieved Successfully",ItemDetails.this,R.drawable.ic_done_black_24dp);
                //Toast.makeText(getContext(), "stored", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public Bitmap getThumbnail(String filename) {
        Bitmap thumnail = BitmapFactory.decodeResource(EditProfile.this.getResources(), R.drawable.ic_person_black_24dp);
        try {
            File filepath = getApplicationContext().getFileStreamPath(filename);
            FileInputStream fi = new FileInputStream(filepath);
            thumnail = BitmapFactory.decodeStream(fi);

        } catch (Exception m) {
            m.printStackTrace();
        }
        return thumnail;
    }

    private boolean storeImage(Bitmap b) {
        try {
            String name = txtName.getText().toString();
            imagePath = name + ".png";
            FileOutputStream fos = getApplicationContext().openFileOutput(name + ".png", Context.MODE_PRIVATE);
            b.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.close();


            prefManager.storeImg(imagePath);
            return true;
        } catch (Exception m) {
            //controller.toast("Error Storing Image",ItemDetails.this,R.drawable.ic_error_outline_black_24dp);
            Toast.makeText(EditProfile.this, "not sac" + m.getMessage(), Toast.LENGTH_SHORT).show();
            m.printStackTrace();
            return false;
        }
    }

    void popOutFragments() {
       // FragmentManager fragmentManager = getApplicationContext().getSupportFragmentManager();
        //for (int i = 0; i < 1; i++) {
       // fragmentManager.popBackStack();
        // }
    }


}
