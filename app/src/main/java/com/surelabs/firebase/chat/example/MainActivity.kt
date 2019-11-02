package com.surelabs.firebase.chat.example

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        register.setOnClickListener {
            val model = UserModel()
            model.email = username.text.toString()
            model.password = password.text.toString()
            val db = FirebaseFirestore.getInstance()
            //this is means
            // INSERT INTO account (email, password) VALULES ('username.text.toString()', 'password.text.toString()');
            // email and password colom we got from the class UserModel below
            db.collection("account")
                .add(model)
                .addOnSuccessListener {
                    Toast.makeText(this@MainActivity, it.id, Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener {
                    Toast.makeText(this@MainActivity, it.message.toString(), Toast.LENGTH_SHORT)
                        .show()

                }
        }

        getData.setOnClickListener {
            val db = FirebaseFirestore.getInstance()
            //grab the data with the condition email=username.text.tostring and password=password.text.tostring
            // IN SQL : SELECT * FROM account WHERE email='username.text.toString' AND password='password.text.toString()';
            db.collection("account").whereEqualTo("email", username.text.toString())
                .whereEqualTo("password", password.text.toString())
                .get()
                .addOnSuccessListener { documents ->
                    for (doc in documents) {
                        Log.i("id", doc.id)
                        Log.i("id", doc.data["email"].toString())
                    }
                }
        }

        getDataAsList.setOnClickListener {
            val db = FirebaseFirestore.getInstance()
            //grab all data without condition
            // IN SQL SELECT * FROM account
            db.collection("account").get()
                .addOnSuccessListener {
                    for (doc in it.documents) {
                        Log.d("id", doc.id)
                        Log.d("email", doc.get("email").toString())
                        Log.d("password", doc.get("password").toString())
                    }
                }
                .addOnFailureListener {

                }
        }

        getDataOrderBy.setOnClickListener {
            val db = FirebaseFirestore.getInstance()
            //u can chage on DESCENDING (Z-A) into ASCENDING if you want to sort A-Z
            // IN SQL : SELECT * FROM account ORDDER BY email DESC
            db.collection("account").orderBy("email", Query.Direction.DESCENDING).get()
                .addOnSuccessListener {
                    for (doc in it.documents) {
                        Log.d("id", doc.id)
                        Log.d("email", doc.get("email").toString())
                        Log.d("password", doc.get("password").toString())
                    }
                }
                .addOnFailureListener {

                }
        }

        getDataWithLimit2.setOnClickListener {
            val db = FirebaseFirestore.getInstance()
            //change 2L with the limit you want. Why 2L, coz limit need Long type
            // IN SQL : SELECT * FROM account LIMIT 2
            db.collection("account").limit(2L).get()
                .addOnSuccessListener {
                    for (doc in it.documents) {
                        Log.d("id", doc.id)
                        Log.d("email", doc.get("email").toString())
                        Log.d("password", doc.get("password").toString())
                    }
                }
                .addOnFailureListener {

                }
        }

        getDataRealtime.setOnClickListener {
            val db = FirebaseFirestore.getInstance()
            //here get realtime data from all document at account collections
            db.collection("account")
                .addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                    if(firebaseFirestoreException != null)
                        Log.d("exception", firebaseFirestoreException.message.toString())

                    if(querySnapshot != null && !querySnapshot.isEmpty()){
                        val data = querySnapshot.documents
                        data.forEachIndexed { index, documentSnapshot ->
                            Log.d("data", documentSnapshot.get("email").toString())
                        }
                    }else{
                        Log.d("No", "No Daaata")
                    }
                }
        }
    }


    class UserModel {
        var email: String? = null
        var password: String? = null
    }
}
