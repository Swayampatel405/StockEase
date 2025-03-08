package com.appvantage.stockease.authentication

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.appvantage.stockease.R

@Composable
fun AuthenticationScreen(
    modifier: Modifier = Modifier,
    navController: NavController
){

    //Keep Details of all textFields
    var adminName by remember { mutableStateOf("") }
    var businessName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    var isLogin by remember { mutableStateOf(true) }  // Toggle between Login & Signup
    val gradient = Brush.verticalGradient(
        colors = listOf(Color(0xFF00C6FF), Color(0xFF0072FF)) // Gradient background
    )

    Box(
        modifier = modifier.fillMaxSize().background(gradient),
        contentAlignment = Alignment.Center
    ){
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(Color.White.copy(alpha = 0.95f))
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ){

            Text(
                text = if (isLogin) "Welcome Back!" else "Create Account",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0072FF)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Show "Admin Name" & "Business Name" fields only in Signup mode
            AnimatedVisibility(visible = !isLogin) {
                Column {
                    OutlinedTextField(
                        value = adminName,
                        onValueChange = { adminName = it},
                        label = { Text("Admin Name") },
                        leadingIcon = { Icon(Icons.Default.Person , contentDescription = null) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = businessName,
                        onValueChange = { businessName = it },
                        label = { Text("Business Name") },
                        leadingIcon = { Icon(painter = painterResource(R.drawable.baseline_business_24), contentDescription = null) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true
                    )
                }
            }

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text(text = "Email Address") },
                leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                singleLine = true
            )

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text(text = "Password") },
                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                singleLine = true,
                visualTransformation = PasswordVisualTransformation()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    if(!isLogin){
                        FirebaseAuthManager.signUpAdmin(
                            adminName = adminName,
                            businessName = businessName,
                            email = email,
                            password = password,
                            onSuccess = {navController.navigate("dashboard"){popUpTo("authenticate"){inclusive = true} } },
                            onFailure = {error -> Log.e("Signup", "Failed: ${error.message}")},
                        )
                    }else{
                        FirebaseAuthManager.login(
                            email = email,
                            password = password,
                            onSuccess = {navController.navigate("dashboard"){popUpTo("authenticate"){inclusive = true} }},
                            onFailure = {error -> Log.e("Login", "Failed: ${error.message}")},
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0072FF))
            ){
                Text(text = if(isLogin) "Login" else "Signup", fontSize = 18.sp)
            }

            TextButton(
                onClick = { isLogin = !isLogin}
            ) {
                Text(
                    text = if(isLogin)"Don't have an account? Signup" else "Already have an account? Login",
                    color = Color(0xFF0072FF)
                )
            }

        }
    }


}