package com.appvantage.stockease.dashboard

import android.content.Context
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import com.appvantage.stockease.authentication.FirebaseAuthManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

object DashboardManager {

    fun createUser(name: String, email:String,password:String,role:String,context: Context){

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val uid = task.result?.user?.uid
                    if (uid != null) {
                        val user = hashMapOf(
                            "name" to name,
                            "email" to email,
                            "role" to role
                        )

                        FirebaseFirestore.getInstance().collection("users").document(uid)
                            .set(user)
                            .addOnSuccessListener {
                                Toast.makeText(context, "User created successfully", Toast.LENGTH_SHORT).show()
                            }
                            .addOnFailureListener {
                                Toast.makeText(context, "Failed to save user data", Toast.LENGTH_SHORT).show()
                            }
                    }
                } else {
                    Toast.makeText(context, "Error: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }
}