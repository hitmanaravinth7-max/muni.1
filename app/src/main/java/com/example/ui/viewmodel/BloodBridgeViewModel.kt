package com.example.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.entity.ActivityLogEntity
import com.example.data.entity.BloodBankEntity
import com.example.data.entity.BloodStockEntity
import com.example.data.entity.DonorProfileEntity
import com.example.data.entity.EmergencyRequestEntity
import com.example.data.entity.UserEntity
import com.example.data.repository.AlertDisplayItem
import com.example.data.repository.BloodBridgeRepository
import com.example.data.repository.DonorDisplayItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class AuthUiState(
    val currentUser: UserEntity? = null,
    val donorProfile: DonorProfileEntity? = null,
    val isLoading: Boolean = false,
    val loginError: String? = null,
    val registerError: String? = null,
    val registerSuccess: Boolean = false
) {
    val isLoggedIn: Boolean get() = currentUser != null
    val isAdmin: Boolean get() = currentUser?.role == "ADMIN"
    val isDonor: Boolean get() = donorProfile != null
}

class BloodBridgeViewModel(private val repository: BloodBridgeRepository) : ViewModel() {

    private val _authState = MutableStateFlow(AuthUiState())
    val authState: StateFlow<AuthUiState> = _authState.asStateFlow()

    // Public & Live Flows
    val allDonors: StateFlow<List<DonorDisplayItem>> = repository.getAllDonorsFlow()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allBloodBanks: StateFlow<List<BloodBankEntity>> = repository.getAllBloodBanksFlow()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allBloodStock: StateFlow<List<BloodStockEntity>> = repository.getAllBloodStockFlow()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allEmergencyRequests: StateFlow<List<EmergencyRequestEntity>> = repository.getAllEmergencyRequestsFlow()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allUsers: StateFlow<List<UserEntity>> = repository.getAllUsersFlow()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allActivityLogs: StateFlow<List<ActivityLogEntity>> = repository.getAllActivityLogsFlow()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _myRequests = MutableStateFlow<List<EmergencyRequestEntity>>(emptyList())
    val myRequests: StateFlow<List<EmergencyRequestEntity>> = _myRequests.asStateFlow()

    private val _donorAlerts = MutableStateFlow<List<AlertDisplayItem>>(emptyList())
    val donorAlerts: StateFlow<List<AlertDisplayItem>> = _donorAlerts.asStateFlow()

    private val _userMessage = MutableStateFlow<String?>(null)
    val userMessage: StateFlow<String?> = _userMessage.asStateFlow()

    fun clearUserMessage() {
        _userMessage.value = null
    }

    fun clearAuthErrors() {
        _authState.value = _authState.value.copy(loginError = null, registerError = null)
    }

    fun resetRegisterSuccess() {
        _authState.value = _authState.value.copy(registerSuccess = false)
    }

    fun login(email: String, passwordRaw: String, onResult: (Boolean, String?) -> Unit) {
        if (email.isBlank() || passwordRaw.isBlank()) {
            _authState.value = _authState.value.copy(loginError = "Please enter both email and password.")
            onResult(false, "Please enter both email and password.")
            return
        }

        if (passwordRaw.length < 8) {
            _authState.value = _authState.value.copy(loginError = "Password must be at least 8 characters.")
            onResult(false, "Password must be at least 8 characters.")
            return
        }

        viewModelScope.launch {
            _authState.value = _authState.value.copy(isLoading = true, loginError = null)
            val result = repository.login(email, passwordRaw)
            result.onSuccess { user ->
                // Check if user has donor profile
                val donorProfile = repository.getDonorProfileByUserId(user.id)
                _authState.value = _authState.value.copy(
                    currentUser = user,
                    donorProfile = donorProfile,
                    isLoading = false,
                    loginError = null
                )
                observeUserData(user.id, donorProfile?.id)
                onResult(true, null)
            }.onFailure { ex ->
                val errorMsg = ex.message ?: "Invalid email or password."
                _authState.value = _authState.value.copy(
                    isLoading = false,
                    loginError = errorMsg
                )
                onResult(false, errorMsg)
            }
        }
    }

    fun register(
        fullName: String,
        email: String,
        phone: String,
        passwordRaw: String,
        confirmPasswordRaw: String,
        agreeTerms: Boolean,
        onResult: (Boolean, String?) -> Unit
    ) {
        if (fullName.isBlank()) {
            _authState.value = _authState.value.copy(registerError = "Full name is required.")
            onResult(false, "Full name is required.")
            return
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email.trim()).matches()) {
            _authState.value = _authState.value.copy(registerError = "Please enter a valid email address.")
            onResult(false, "Please enter a valid email address.")
            return
        }
        val cleanPhone = phone.trim().replace(Regex("[^0-9+]"), "")
        if (cleanPhone.length < 10) {
            _authState.value = _authState.value.copy(registerError = "Please enter a valid phone number (at least 10 digits).")
            onResult(false, "Please enter a valid phone number.")
            return
        }
        if (passwordRaw.length < 8) {
            _authState.value = _authState.value.copy(registerError = "Password must be at least 8 characters.")
            onResult(false, "Password must be at least 8 characters.")
            return
        }
        if (passwordRaw != confirmPasswordRaw) {
            _authState.value = _authState.value.copy(registerError = "Passwords do not match.")
            onResult(false, "Passwords do not match.")
            return
        }
        if (!agreeTerms) {
            _authState.value = _authState.value.copy(registerError = "You must agree to the Terms & Privacy Policy.")
            onResult(false, "You must agree to terms.")
            return
        }

        viewModelScope.launch {
            _authState.value = _authState.value.copy(isLoading = true, registerError = null)
            val result = repository.register(fullName, email, cleanPhone, passwordRaw)
            result.onSuccess {
                _authState.value = _authState.value.copy(
                    isLoading = false,
                    registerError = null,
                    registerSuccess = true
                )
                onResult(true, null)
            }.onFailure { ex ->
                val errorMsg = ex.message ?: "Registration failed. Please try again."
                _authState.value = _authState.value.copy(
                    isLoading = false,
                    registerError = errorMsg
                )
                onResult(false, errorMsg)
            }
        }
    }

    fun logout() {
        _authState.value = AuthUiState()
        _myRequests.value = emptyList()
        _donorAlerts.value = emptyList()
    }

    private fun observeUserData(userId: Long, donorId: Long?) {
        viewModelScope.launch {
            repository.getRequestsByUserFlow(userId).collect {
                _myRequests.value = it
            }
        }
        viewModelScope.launch {
            repository.getDonorProfileForUserFlow(userId).collect { profile ->
                _authState.value = _authState.value.copy(donorProfile = profile)
            }
        }
        if (donorId != null) {
            viewModelScope.launch {
                repository.getAlertsForDonorFlow(donorId).collect {
                    _donorAlerts.value = it
                }
            }
        }
    }

    fun saveDonorProfile(profile: DonorProfileEntity, onComplete: (Boolean, String?) -> Unit) {
        viewModelScope.launch {
            val result = repository.saveDonorProfile(profile)
            result.onSuccess { id ->
                val updatedProfile = repository.getDonorProfileByUserId(profile.userId)
                _authState.value = _authState.value.copy(donorProfile = updatedProfile)
                if (updatedProfile != null) {
                    observeUserData(profile.userId, updatedProfile.id)
                }
                _userMessage.value = "Donor profile registered successfully!"
                onComplete(true, null)
            }.onFailure { ex ->
                onComplete(false, ex.message)
            }
        }
    }

    fun toggleAvailability(isAvailable: Boolean) {
        val user = _authState.value.currentUser ?: return
        viewModelScope.launch {
            val res = repository.toggleDonorAvailability(user.id, isAvailable)
            res.onSuccess {
                _userMessage.value = if (it) "Availability set to Available" else "Availability set to Not Available"
            }.onFailure { ex ->
                _userMessage.value = ex.message ?: "Failed to update availability."
            }
        }
    }

    fun createEmergencyRequest(request: EmergencyRequestEntity, onComplete: (Boolean, String?) -> Unit) {
        viewModelScope.launch {
            val res = repository.createEmergencyRequest(request)
            res.onSuccess { reqId ->
                _userMessage.value = "Emergency request submitted! Request ID: #REQ-$reqId"
                onComplete(true, null)
            }.onFailure { ex ->
                onComplete(false, ex.message)
            }
        }
    }

    fun respondToAlert(alertId: Long, isAccepted: Boolean) {
        val user = _authState.value.currentUser ?: return
        viewModelScope.launch {
            val res = repository.respondToAlert(alertId, isAccepted, user.id)
            res.onSuccess {
                _userMessage.value = if (isAccepted) "Alert accepted! Contact details now visible." else "Alert declined."
            }.onFailure {
                _userMessage.value = "Failed to update alert."
            }
        }
    }

    // --- Admin Operations ---
    fun adminApproveRequest(requestId: Long) {
        val admin = _authState.value.currentUser ?: return
        if (admin.role != "ADMIN") return
        viewModelScope.launch {
            val res = repository.approveEmergencyRequest(requestId, admin.id)
            res.onSuccess { count ->
                _userMessage.value = "Request approved! Broadcasted $count donor alerts."
            }.onFailure {
                _userMessage.value = it.message ?: "Failed to approve request."
            }
        }
    }

    fun adminRejectRequest(requestId: Long) {
        val admin = _authState.value.currentUser ?: return
        if (admin.role != "ADMIN") return
        viewModelScope.launch {
            repository.updateRequestStatus(requestId, "REJECTED", admin.id)
            _userMessage.value = "Emergency request #$requestId rejected."
        }
    }

    fun adminFulfillRequest(requestId: Long) {
        val admin = _authState.value.currentUser ?: return
        if (admin.role != "ADMIN") return
        viewModelScope.launch {
            repository.updateRequestStatus(requestId, "FULFILLED", admin.id)
            _userMessage.value = "Emergency request #$requestId marked as FULFILLED."
        }
    }

    fun adminVerifyDonor(donorId: Long, isVerified: Boolean) {
        val admin = _authState.value.currentUser ?: return
        if (admin.role != "ADMIN") return
        viewModelScope.launch {
            repository.verifyDonor(donorId, isVerified, admin.id)
            _userMessage.value = if (isVerified) "Donor #$donorId verified successfully!" else "Donor #$donorId unverified."
        }
    }

    fun adminToggleUserBlock(userId: Long, currentBlocked: Boolean) {
        val admin = _authState.value.currentUser ?: return
        if (admin.role != "ADMIN") return
        viewModelScope.launch {
            val shouldBlock = !currentBlocked
            repository.toggleUserBlocked(userId, shouldBlock, admin.id)
            _userMessage.value = if (shouldBlock) "User #$userId blocked." else "User #$userId unblocked."
        }
    }

    fun adminUpdateStock(stockId: Long, newUnits: Int) {
        val admin = _authState.value.currentUser ?: return
        if (admin.role != "ADMIN") return
        viewModelScope.launch {
            val res = repository.updateStockUnits(stockId, newUnits, admin.fullName)
            res.onSuccess {
                _userMessage.value = "Stock updated to $newUnits units."
            }.onFailure {
                _userMessage.value = it.message ?: "Stock error."
            }
        }
    }

    fun adminAddBloodBank(bank: BloodBankEntity, onComplete: (Boolean) -> Unit) {
        val admin = _authState.value.currentUser ?: return
        if (admin.role != "ADMIN") return
        viewModelScope.launch {
            val res = repository.addBloodBank(bank, admin.id)
            res.onSuccess {
                _userMessage.value = "Blood Bank '${bank.name}' added successfully!"
                onComplete(true)
            }.onFailure {
                _userMessage.value = it.message ?: "Failed to add blood bank."
                onComplete(false)
            }
        }
    }
}

class BloodBridgeViewModelFactory(private val repository: BloodBridgeRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BloodBridgeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return BloodBridgeViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
