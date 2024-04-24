package com.example.socialmediaapp.tests
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertNull
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when` as whenever


class loginUnitTests {

    private lateinit var firebaseAuthService: FirebaseAuthService
    private lateinit var firebaseAuth: FirebaseAuth

    @Before
    fun setup() {
        firebaseAuth = mock(FirebaseAuth::class.java)
        firebaseAuthService = FirebaseAuthService(firebaseAuth)
    }


    @Test
    fun testLoginWithValidCredentials() = runBlocking {
        val email = "testuser@example.com"
        val password = "password"

        val mockAuthResult = mock(AuthResult::class.java)
        val mockUser = mock(FirebaseUser::class.java)
        whenever(mockAuthResult.user).thenReturn(mockUser)
        whenever(firebaseAuth.signInWithEmailAndPassword(email, password))
            .thenReturn(Tasks.forResult(mockAuthResult))

        val user = firebaseAuthService.login(email, password)
        assertNotNull(user)
        assertEquals(mockUser, user)
    }

    @Test
    fun testLoginWithInvalidCredentials() = runBlocking {
        val email = "invalid@example.com"
        val password = "wrongpassword"

        whenever(firebaseAuth.signInWithEmailAndPassword(email, password))
            .thenAnswer { throw Exception("INVALID_CREDENTIALS") }

        val user = firebaseAuthService.login(email, password)
        assertNull(user)
    }
}

class FirebaseAuthService(private val firebaseAuth: FirebaseAuth) {
    suspend fun login(email: String, password: String): FirebaseUser? {
        return try {
            val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            result.user
        } catch (e: Exception) {
            // Handle the exception by returning null
            null
        }
    }
}