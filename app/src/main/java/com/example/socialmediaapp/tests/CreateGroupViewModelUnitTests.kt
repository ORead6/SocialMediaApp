package com.example.socialmediaapp.tests
import com.example.socialmediaapp.viewModels.createGroupViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import org.junit.Test
import org.junit.Before
import org.mockito.Mock
import org.mockito.MockitoAnnotations


class CreateGroupViewModelUnitTests {

    // Mock FirebaseAuth and FirebaseFirestore
    @Mock
    lateinit var mockAuth: FirebaseAuth

    @Mock
    lateinit var mockFirestore: FirebaseFirestore

    // Initialize ViewModel
    private lateinit var viewModel: createGroupViewModel

    @Before
    fun setup() {
        // Initialize Mockito annotations
        MockitoAnnotations.initMocks(this)

        // Create ViewModel with mocked dependencies
        viewModel = createGroupViewModel(mockAuth, mockFirestore)
    }

    @Test
    fun testSetGroupName() {
        val testName = "Test Group Name"
        viewModel.setGroupName(testName)
        assert(viewModel.groupName.value == testName)
    }

    @Test
    fun testSetPrivacyState() {
        val testState = true
        viewModel.setPrivacyState(testState)
        assert(viewModel.privacyState.value == testState)
    }

    @Test
    fun testGenerateRandomCode() {
        val length = 6
        val randomCode = viewModel.generateRandomCode(length)
        assert(randomCode.length == length)
    }

}