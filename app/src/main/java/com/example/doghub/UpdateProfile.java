package com.example.doghub;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Transaction;


public class UpdateProfile extends AppCompatActivity {


    //declaring variables
    EditText etName, etBio, etDogName, etBreed, etDogAge;
    Button button;
    ImageView imageView;
    Button char1, char2, char3, char4, char5;
    TextView char1_up_tv, char2_up_tv, char3_up_tv, char4_up_tv, char5_up_tv;

    //getting instance of database
    FirebaseDatabase database = FirebaseDatabase.getInstance();

    //for also saving the data in the firebase database, since we are saving in two different places
    DatabaseReference reference;
    DocumentReference documentReference;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String currentUid = user.getUid();


    //initialize activity
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);

        //get user if from Firebase
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String currentUid = user.getUid();

        documentReference = db.collection("user").document(currentUid);

        //giving the reference for the data entered by the user
        etBio = findViewById(R.id.et_bio_up);
        //etName = findViewById(R.id.et_name_up);
        etDogAge = findViewById(R.id.et_dog_age_up);
        etDogName = findViewById(R.id.et_dog_name_up);
        etBreed = findViewById(R.id.et_breed_up);
        button = findViewById(R.id.btn_up);


        //  imageView = findViewById(R.id.imageView_dog_up)
        // char2 = findViewById(R.id.char2_btn_up);
        //  char1 = findViewById(R.id.char1_btn_up);
        //   char3 = findViewById(R.id.char3_btn_up);
        //   char4 = findViewById(R.id.char4_btn_up);
        //   char5 = findViewById(R.id.char5_btn_up);


        //setting the button method

        //listens for user to click on Update Profile button
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateProfile();

            }
        });
    }

    //this onStart method will show the user the previous data that they had entered prior to updating
    //this way users can see what they want to change.
    //whenever opened, we will see the data that was already loaded
    @Override
    protected void onStart() {
        super.onStart();


        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if (task.getResult().exists()) {


                    String nameResult = task.getResult().getString("name");
                    String dogNameResult = task.getResult().getString("dogs_name");
                    String breedResult = task.getResult().getString("breed");
                    String bioResult = task.getResult().getString("bio");
                    String dogAgeResult = task.getResult().getString("dogs_age");
                    String url = task.getResult().getString("url");
                    //  String char1_result = task.getResult().getString("char1");
                    //   String char2_result= task.getResult().getString("char2");
                    //   String char3_result = task.getResult().getString("char3");
                    //   String char4_result = task.getResult().getString("char4");
                    //   String char5_result = task.getResult().getString("char5");

                    //   Picasso.get().load(url).into(imageView);


                    etBio.setText(bioResult);
                    //    etName.setText(nameResult);
                    etDogAge.setText(dogAgeResult);
                    etDogName.setText(dogNameResult);
                    etBreed.setText(breedResult);

            /*        char1_up_tv.setText(char1_result.toUpperCase());
                    char2_up_tv.setText(char2_result.toUpperCase());
                    char3_up_tv.setText(char3_result.toUpperCase());
                    char4_up_tv.setText(char4_result.toUpperCase());
                    char5_up_tv.setText(char5_result.toUpperCase());
*/

                } else {
                    Toast.makeText(UpdateProfile.this, "No Profile", Toast.LENGTH_SHORT).show();
                }

            }
        });


    }

    //update the profile data
    private void updateProfile() {
        String bio = etBio.getText().toString();
//        String name = etName.getText().toString();
        String age = etDogAge.getText().toString();
        String dogname = etDogName.getText().toString();
        String breed = etBreed.getText().toString();
        //      String char1 = char1_up_tv.getText().toString();
        //    String char2 = char2_up_tv.getText().toString();
        //     String char3 = char3_up_tv.getText().toString();
        //    String char4 = char4_up_tv.getText().toString();
        //     String char5 = char5_up_tv.getText().toString();


        final DocumentReference sDoc = db.collection("user").document(currentUid);

        //update the attributes on Firebase
        db.runTransaction(new Transaction.Function<Void>() {
            @Override
            public Void apply(Transaction transaction) throws FirebaseFirestoreException {
                // DocumentSnapshot snapshot = transaction.get(sfDocRef);


                transaction.update(sDoc, "bio", bio);
                //transaction.update(sDoc, "name", name);
                transaction.update(sDoc, "dogs_age", age);
                transaction.update(sDoc, "dogs_name", dogname);
                transaction.update(sDoc, "breed", breed);


                return null;
            }

            //creates toast message if profile is updated
        }).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(UpdateProfile.this, "updated", Toast.LENGTH_SHORT).show();

            }
        })
                //creates toast message if profile fails to update
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(UpdateProfile.this, "failed", Toast.LENGTH_SHORT).show();
                    }
                });


    }
}


