package com.appvantage.stockease.authentication

import android.annotation.SuppressLint
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

object FirebaseAuthManager {

    private val auth = FirebaseAuth.getInstance()
    @SuppressLint("StaticFieldLeak")
    private val firestore = FirebaseFirestore.getInstance()

    fun signUpAdmin(
        adminName: String,
        businessName: String,
        email: String,
        password: String,
        onSuccess: ()->Unit,
        onFailure: (Exception)->Unit
    ){
        auth.createUserWithEmailAndPassword(email,password)
            .addOnSuccessListener { authResult->
                val uid = authResult.user?.uid ?: return@addOnSuccessListener
                val userMap = hashMapOf(
                    "adminName" to adminName,
                    "businessName" to businessName,
                    "email" to email,
                    "role" to "Admin" //Default it set to admin because of Admin only can signup
                )
                firestore.collection("users").document(uid).set(userMap)
                    .addOnSuccessListener { onSuccess() }
                    .addOnFailureListener{ onFailure(it) }
            }
            .addOnFailureListener { onFailure(it) }
    }

    fun login(
        email: String,
        password: String,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ){
        auth.signInWithEmailAndPassword(email,password)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onFailure(it) }
    }



}