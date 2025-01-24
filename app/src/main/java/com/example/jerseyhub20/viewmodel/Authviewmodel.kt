package com.example.jerseyhub20.viewmodel


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.jerseyhub20.util.Status
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow



//
class AuthViewModel : ViewModel() {

    private val _auth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }
    private val userRef by lazy { FirebaseDatabase.getInstance().getReference("user") }

    private val _currentUser = MutableStateFlow<FirebaseUser?>(_auth.currentUser)
    val currentUser: StateFlow<FirebaseUser?> = _currentUser

    private val _userData = MutableStateFlow<UserModel?>(null)
    val userData: StateFlow<UserModel?> = _userData

    private val _authState = MutableLiveData<Status>()
    val authState: LiveData<Status> = _authState

    private val passwordRegex = "^.{8,}$".toRegex()
    private val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$".toRegex()

    init {
        checkAuthenticationStatus()
    }

    fun checkAuthenticationStatus() {
        val user = _auth.currentUser
        if (user != null && user.isEmailVerified) {
            _authState.value = Status.Authenticated
            fetchUserData(user.uid) { data ->
                _userData.value = data
            }
        } else {
            _authState.value = Status.NotAuthenticated
        }
    }

    fun logIn(email: String, password: String) {
        if (email.isBlank() || password.isBlank()) {
            _authState.value = Status.Error("Email and password cannot be empty")
            return
        }

        _authState.value = Status.Loading
        _auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = _auth.currentUser
                    if (user?.isEmailVerified == true) {
                        _authState.value = Status.Authenticated
                        _currentUser.value = user
                    } else {
                        _authState.value =
                            Status.Error("Please verify your email before logging in")
                    }
                } else {
                    _authState.value = Status.Error(task.exception?.message ?: "Login failed")
                }
            }
            .addOnFailureListener { exception ->
                _authState.value = Status.Error(exception.message ?: "An error occurred")
            }
    }

    private fun registerToRealtime(
        email: String, phone: String, userName: String, uid: String, onSuccess: () -> Unit
    ) {
        val userData = UserModel(email = email, phone = phone, username = userName, uid = uid)
        userRef.child(uid).setValue(userData)
            .addOnSuccessListener {
                onSuccess()
                Log.d("AuthViewModel", "User data saved successfully")
            }
            .addOnFailureListener { exception ->
                _authState.value = Status.Error(exception.message ?: "Failed to save user data")
            }
    }

    fun register(
        email: String, password: String, userName: String, phone: String, onClick: () -> Unit
    ) {
        if (email.isBlank() || password.isBlank() || phone.isBlank()) {
            _authState.value = Status.Error("Email, password, or phone cannot be empty")
            return
        }
        if (!email.matches(emailRegex)) {
            _authState.value = Status.Error("Invalid email format")
            return
        }
        if (!password.matches(passwordRegex)) {
            _authState.value = Status.Error("Password must meet requirements")
            return
        }

        _authState.value = Status.Loading
        _auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = _auth.currentUser
                    user?.sendEmailVerification()?.addOnCompleteListener { emailTask ->
                        if (emailTask.isSuccessful) {
                            registerToRealtime(email, phone, userName, user.uid) {
                                _currentUser.value = user
                                onClick()
                            }
                        } else {
                            _authState.value = Status.Error("Failed to send verification email")
                        }
                    }
                } else {
                    _authState.value =
                        Status.Error(task.exception?.message ?: "Registration failed")
                }
            }
            .addOnFailureListener { exception ->
                _authState.value = Status.Error(exception.message ?: "An error occurred")
            }
    }

    fun signOut() {
        _auth.signOut()
        _userData.value = null
        _currentUser.value = null
        _authState.value = Status.NotAuthenticated
    }

    private fun fetchUserData(uid: String, onResult: (UserModel?) -> Unit) {
        userRef.child(uid).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val userData = snapshot.getValue(UserModel::class.java)
                    onResult(userData)
                } else {
                    onResult(null)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                onResult(null)
            }
        })
    }
}

data class UserModel(
    val email: String = "",
    val phone: String = "",
    val uid: String = "",
    val username: String = ""
)


//