package com.example.socialmediaapp.tests
import com.example.socialmediaapp.viewModels.createGroupViewModel
import com.example.socialmediaapp.viewModels.editprofileViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import org.junit.Test
import org.junit.Before
import org.mockito.Mock
import org.mockito.MockitoAnnotations


class EditProfileViewModelUnitTests {

    // Initialize ViewModel
    private lateinit var viewModel: editprofileViewModel

    @Before
    fun setup() {
        // Initialize Mockito annotations
        MockitoAnnotations.initMocks(this)

        // Create ViewModel with mocked dependencies
        viewModel = editprofileViewModel()
    }

    @Test
    fun testSetBio() {
        val testBio = "Test Bio"
        viewModel.setBio(testBio)
        assert(viewModel.bio.value == testBio)
    }

    @Test
    fun testSetUsername() {
        val testuserName = "TestUserName"
        viewModel.setUsername(testuserName)
        assert(viewModel.username.value == testuserName)
    }

}