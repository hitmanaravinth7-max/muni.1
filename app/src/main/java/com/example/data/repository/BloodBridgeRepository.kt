package com.example.data.repository

import android.content.Context
import com.example.data.dao.BloodBridgeDao
import com.example.data.database.AppDatabase
import com.example.data.entity.ActivityLogEntity
import com.example.data.entity.BloodBankEntity
import com.example.data.entity.BloodStockEntity
import com.example.data.entity.DonationAlertEntity
import com.example.data.entity.DonorProfileEntity
import com.example.data.entity.EmergencyRequestEntity
import com.example.data.entity.UserEntity
import com.example.util.BloodCompatibility
import com.example.util.SecurityUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

data class DonorDisplayItem(
    val profile: DonorProfileEntity,
    val firstName: String,
    val fullPhone: String,
    val isUserActive: Boolean
)

data class AlertDisplayItem(
    val alert: DonationAlertEntity,
    val request: EmergencyRequestEntity,
    val requesterPhone: String,
    val requesterName: String
)

class BloodBridgeRepository(private val dao: BloodBridgeDao) {

    companion object {
        @Volatile
        private var INSTANCE: BloodBridgeRepository? = null

        fun getInstance(context: Context): BloodBridgeRepository {
            return INSTANCE ?: synchronized(this) {
                val db = AppDatabase.getDatabase(context)
                val repo = BloodBridgeRepository(db.bloodBridgeDao())
                INSTANCE = repo
                // Seed initial data in background
                CoroutineScope(Dispatchers.IO).launch {
                    repo.seedInitialDataIfEmpty()
                }
                repo
            }
        }
    }

    suspend fun seedInitialDataIfEmpty() = withContext(Dispatchers.IO) {
        val existingAdmin = dao.getUserByEmail("admin@bloodbridge.local")
        if (existingAdmin != null) return@withContext

        val now = System.currentTimeMillis()

        // 1. Seed Admin
        val admin = UserEntity(
            fullName = "BloodBridge Admin",
            email = "admin@bloodbridge.local",
            passwordHash = SecurityUtil.hashPassword("Admin@12345"),
            phoneNumber = "+91 9876543210",
            role = "ADMIN",
            accountStatus = "ACTIVE",
            createdAt = now,
            updatedAt = now
        )
        val adminId = dao.insertUser(admin)

        dao.insertActivityLog(
            ActivityLogEntity(
                userId = adminId,
                action = "SYSTEM_INITIALIZED",
                details = "BloodBridge emergency system database seeded with default admin and demo records.",
                ipAddress = "127.0.0.1",
                createdAt = now
            )
        )

        // 2. Seed Demo Regular Users & Donors
        val usersSeed = listOf(
            Triple("Rahul Sharma", "rahul.sharma@example.com", "+91 9845012345"),
            Triple("Priya Patel", "priya.patel@example.com", "+91 9876123456"),
            Triple("Arun Kumar", "arun.kumar@example.com", "+91 9789012345"),
            Triple("Kavitha Ramesh", "kavitha.ramesh@example.com", "+91 9944012345"),
            Triple("Deepak Singh", "deepak.singh@example.com", "+91 9811012345")
        )

        val userIds = mutableListOf<Long>()
        for ((name, email, phone) in usersSeed) {
            val uId = dao.insertUser(
                UserEntity(
                    fullName = name,
                    email = email,
                    passwordHash = SecurityUtil.hashPassword("User@12345"),
                    phoneNumber = phone,
                    role = "USER",
                    accountStatus = "ACTIVE",
                    createdAt = now - 86400000L * 10,
                    updatedAt = now - 86400000L * 10
                )
            )
            userIds.add(uId)
        }

        // Seed Donor Profiles
        // Rahul: O+, Coimbatore, Available, Verified
        val donor1Id = dao.insertDonorProfile(
            DonorProfileEntity(
                userId = userIds[0],
                bloodGroup = "O+",
                dateOfBirth = "1994-06-15",
                gender = "MALE",
                weightKg = 72.0,
                addressLine = "Gandhipuram 4th Cross",
                city = "Coimbatore",
                state = "Tamil Nadu",
                pincode = "641012",
                latitude = 11.0168,
                longitude = 76.9558,
                lastDonationDate = now - (120L * 86400000L), // 120 days ago (eligible)
                isAvailable = true,
                hasChronicIllness = false,
                onMedication = false,
                recentSurgery = false,
                totalDonations = 4,
                consentGiven = true,
                verifiedByAdmin = true,
                createdAt = now - 86400000L * 10
            )
        )

        // Priya: A+, Chennai, in cooldown (donated 30 days ago)
        dao.insertDonorProfile(
            DonorProfileEntity(
                userId = userIds[1],
                bloodGroup = "A+",
                dateOfBirth = "1998-02-20",
                gender = "FEMALE",
                weightKg = 58.0,
                addressLine = "T. Nagar Main Road",
                city = "Chennai",
                state = "Tamil Nadu",
                pincode = "600017",
                latitude = 13.0418,
                longitude = 80.2341,
                lastDonationDate = now - (30L * 86400000L), // 30 days ago (cooldown active!)
                isAvailable = false,
                hasChronicIllness = false,
                onMedication = false,
                recentSurgery = false,
                totalDonations = 2,
                consentGiven = true,
                verifiedByAdmin = true,
                createdAt = now - 86400000L * 10
            )
        )

        // Arun: B+, Coimbatore, Available, Verified
        dao.insertDonorProfile(
            DonorProfileEntity(
                userId = userIds[2],
                bloodGroup = "B+",
                dateOfBirth = "1991-11-10",
                gender = "MALE",
                weightKg = 68.0,
                addressLine = "RS Puram West",
                city = "Coimbatore",
                state = "Tamil Nadu",
                pincode = "641002",
                latitude = 11.0084,
                longitude = 76.9489,
                lastDonationDate = now - (105L * 86400000L),
                isAvailable = true,
                hasChronicIllness = false,
                onMedication = false,
                recentSurgery = false,
                totalDonations = 6,
                consentGiven = true,
                verifiedByAdmin = true,
                createdAt = now - 86400000L * 10
            )
        )

        // Kavitha: AB+, Bengaluru, Available, Not Verified yet
        dao.insertDonorProfile(
            DonorProfileEntity(
                userId = userIds[3],
                bloodGroup = "AB+",
                dateOfBirth = "1996-08-05",
                gender = "FEMALE",
                weightKg = 54.0,
                addressLine = "Indiranagar 100ft Road",
                city = "Bengaluru",
                state = "Karnataka",
                pincode = "560038",
                latitude = 12.9784,
                longitude = 77.6408,
                lastDonationDate = null,
                isAvailable = true,
                hasChronicIllness = false,
                onMedication = false,
                recentSurgery = false,
                totalDonations = 0,
                consentGiven = true,
                verifiedByAdmin = false,
                createdAt = now - 86400000L * 10
            )
        )

        // Deepak: O-, Delhi, Available, Verified (Universal Donor!)
        dao.insertDonorProfile(
            DonorProfileEntity(
                userId = userIds[4],
                bloodGroup = "O-",
                dateOfBirth = "1990-04-12",
                gender = "MALE",
                weightKg = 75.0,
                addressLine = "Connaught Place Sector B",
                city = "New Delhi",
                state = "Delhi",
                pincode = "110001",
                latitude = 28.6315,
                longitude = 77.2167,
                lastDonationDate = now - (180L * 86400000L),
                isAvailable = true,
                hasChronicIllness = false,
                onMedication = false,
                recentSurgery = false,
                totalDonations = 8,
                consentGiven = true,
                verifiedByAdmin = true,
                createdAt = now - 86400000L * 10
            )
        )

        // 3. Seed Blood Banks
        val bank1 = dao.insertBloodBank(
            BloodBankEntity(
                name = "Coimbatore Central Red Cross Blood Bank",
                address = "Huzur Road, Near Collectorate",
                city = "Coimbatore",
                state = "Tamil Nadu",
                pincode = "641018",
                latitude = 11.0016,
                longitude = 76.9628,
                contactPhone = "+91 422 2212345",
                licenseNumber = "TN-CBE-BB-2018-092",
                createdAt = now,
                updatedAt = now
            )
        )

        val bank2 = dao.insertBloodBank(
            BloodBankEntity(
                name = "Apollo Blood Centre",
                address = "21 Greams Lane, Off Greams Road",
                city = "Chennai",
                state = "Tamil Nadu",
                pincode = "600006",
                latitude = 13.0604,
                longitude = 80.2496,
                contactPhone = "+91 44 28290200",
                licenseNumber = "TN-CHN-BB-2015-110",
                createdAt = now,
                updatedAt = now
            )
        )

        val bank3 = dao.insertBloodBank(
            BloodBankEntity(
                name = "Manipal Rotary Blood Center",
                address = "98 HAL Old Airport Road, Kodihalli",
                city = "Bengaluru",
                state = "Karnataka",
                pincode = "560017",
                latitude = 12.9592,
                longitude = 77.6534,
                contactPhone = "+91 80 25024444",
                licenseNumber = "KA-BLR-BB-2019-331",
                createdAt = now,
                updatedAt = now
            )
        )

        // 4. Seed Stock for Bank 1 (Coimbatore)
        val bloodGroups = listOf("A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-")
        val components = listOf("WHOLE_BLOOD", "PLASMA", "PLATELETS", "RBC")
        val stockList = mutableListOf<BloodStockEntity>()

        for (bankId in listOf(bank1, bank2, bank3)) {
            for (bg in bloodGroups) {
                for (comp in components) {
                    val units = when {
                        bg == "O+" && comp == "WHOLE_BLOOD" -> 14
                        bg == "O-" && comp == "RBC" -> 3 // Low stock
                        bg == "AB-" -> 2
                        comp == "PLATELETS" -> 5
                        else -> 8
                    }
                    stockList.add(
                        BloodStockEntity(
                            bloodBankId = bankId,
                            bloodGroup = bg,
                            component = comp,
                            unitsAvailable = units,
                            lastUpdatedBy = "Seed System",
                            updatedAt = now
                        )
                    )
                }
            }
        }
        dao.insertAllBloodStock(stockList)

        // 5. Seed Emergency Request
        val req1Id = dao.insertEmergencyRequest(
            EmergencyRequestEntity(
                requestedByUserId = userIds[0],
                patientName = "Suresh Rao",
                bloodGroup = "O+",
                component = "WHOLE_BLOOD",
                unitsNeeded = 2,
                bloodBankId = bank1,
                hospitalName = "GKNM Hospital, Coimbatore",
                urgencyLevel = "CRITICAL",
                contactPhone = "+91 9845012345",
                additionalNotes = "Patient scheduled for urgent cardiac bypass surgery. Requires whole blood by 3 PM.",
                status = "APPROVED",
                reviewedByAdminId = adminId,
                createdAt = now - 3600000L * 4,
                updatedAt = now - 3600000L * 2
            )
        )

        // Seed Alert for Donor 1 (Rahul)
        dao.insertDonationAlert(
            DonationAlertEntity(
                emergencyRequestId = req1Id,
                donorId = donor1Id,
                status = "PENDING",
                createdAt = now - 3600000L * 2
            )
        )

        // Seed another pending emergency request
        dao.insertEmergencyRequest(
            EmergencyRequestEntity(
                requestedByUserId = userIds[2],
                patientName = "Meenakshi Sundaram",
                bloodGroup = "B+",
                component = "PLATELETS",
                unitsNeeded = 3,
                bloodBankId = bank1,
                hospitalName = "PSG Hospitals, Peelamedu",
                urgencyLevel = "URGENT",
                contactPhone = "+91 9789012345",
                additionalNotes = "Dengue patient with dropping platelet count. Immediate assistance needed.",
                status = "PENDING",
                reviewedByAdminId = null,
                createdAt = now - 1800000L,
                updatedAt = now - 1800000L
            )
        )
    }

    // --- Authentication ---
    suspend fun login(email: String, passwordRaw: String): Result<UserEntity> = withContext(Dispatchers.IO) {
        val user = dao.getUserByEmail(email.trim().lowercase())
            ?: return@withContext Result.failure(Exception("Invalid email or password."))

        if (user.accountStatus == "BLOCKED") {
            return@withContext Result.failure(Exception("This account has been suspended by administration."))
        }

        if (!SecurityUtil.verifyPassword(passwordRaw, user.passwordHash)) {
            return@withContext Result.failure(Exception("Invalid email or password."))
        }

        // Log successful login
        dao.insertActivityLog(
            ActivityLogEntity(
                userId = user.id,
                action = "USER_LOGIN",
                details = "User ${user.email} (${user.role}) logged in successfully.",
                ipAddress = "127.0.0.1"
            )
        )

        Result.success(user)
    }

    suspend fun register(
        fullName: String,
        email: String,
        phone: String,
        passwordRaw: String
    ): Result<UserEntity> = withContext(Dispatchers.IO) {
        val cleanEmail = email.trim().lowercase()
        val existing = dao.getUserByEmail(cleanEmail)
        if (existing != null) {
            return@withContext Result.failure(Exception("An account with this email address already exists."))
        }

        val now = System.currentTimeMillis()
        val user = UserEntity(
            fullName = fullName.trim(),
            email = cleanEmail,
            passwordHash = SecurityUtil.hashPassword(passwordRaw),
            phoneNumber = phone.trim(),
            role = "USER",
            accountStatus = "ACTIVE",
            createdAt = now,
            updatedAt = now
        )
        val id = dao.insertUser(user)
        val createdUser = user.copy(id = id)

        dao.insertActivityLog(
            ActivityLogEntity(
                userId = id,
                action = "USER_REGISTERED",
                details = "New user registered: ${createdUser.email}",
                ipAddress = "127.0.0.1"
            )
        )

        Result.success(createdUser)
    }

    suspend fun getUserById(userId: Long): UserEntity? = withContext(Dispatchers.IO) {
        dao.getUserById(userId)
    }

    suspend fun updateUserProfile(user: UserEntity): Result<Unit> = withContext(Dispatchers.IO) {
        dao.updateUser(user.copy(updatedAt = System.currentTimeMillis()))
        dao.insertActivityLog(
            ActivityLogEntity(
                userId = user.id,
                action = "PROFILE_UPDATED",
                details = "User updated profile info: ${user.fullName}",
                ipAddress = "127.0.0.1"
            )
        )
        Result.success(Unit)
    }

    // --- Donors ---
    fun getAllDonorsFlow(): Flow<List<DonorDisplayItem>> {
        return combine(dao.getAllDonorProfilesFlow(), dao.getAllUsersFlow()) { profiles, users ->
            val userMap = users.associateBy { it.id }
            profiles.map { profile ->
                val user = userMap[profile.userId]
                DonorDisplayItem(
                    profile = profile,
                    firstName = user?.let { SecurityUtil.getFirstName(it.fullName) } ?: "Donor",
                    fullPhone = user?.phoneNumber ?: "",
                    isUserActive = user?.accountStatus == "ACTIVE"
                )
            }
        }
    }

    fun getDonorProfileForUserFlow(userId: Long): Flow<DonorProfileEntity?> {
        return dao.getDonorProfileByUserIdFlow(userId)
    }

    suspend fun getDonorProfileByUserId(userId: Long): DonorProfileEntity? = withContext(Dispatchers.IO) {
        dao.getDonorProfileByUserId(userId)
    }

    suspend fun saveDonorProfile(profile: DonorProfileEntity): Result<Long> = withContext(Dispatchers.IO) {
        // Enforce cooldown rule: If last donation within 90 days, force availability false
        val cooldownActive = BloodCompatibility.isCooldownActive(profile.lastDonationDate)
        val effectiveAvailability = if (cooldownActive) false else profile.isAvailable

        val entityToSave = profile.copy(
            isAvailable = effectiveAvailability,
            updatedAt = System.currentTimeMillis()
        )

        val existing = dao.getDonorProfileByUserId(profile.userId)
        val savedId = if (existing == null) {
            dao.insertDonorProfile(entityToSave)
        } else {
            val updated = entityToSave.copy(id = existing.id)
            dao.updateDonorProfile(updated)
            existing.id
        }

        dao.insertActivityLog(
            ActivityLogEntity(
                userId = profile.userId,
                action = "DONOR_PROFILE_SAVED",
                details = "Donor profile saved. Blood group: ${profile.bloodGroup}, City: ${profile.city}, Available: $effectiveAvailability",
                ipAddress = "127.0.0.1"
            )
        )

        Result.success(savedId)
    }

    suspend fun toggleDonorAvailability(userId: Long, desiredAvailable: Boolean): Result<Boolean> = withContext(Dispatchers.IO) {
        val donor = dao.getDonorProfileByUserId(userId)
            ?: return@withContext Result.failure(Exception("Donor profile not found."))

        // Check 90 day cooldown
        if (desiredAvailable && BloodCompatibility.isCooldownActive(donor.lastDonationDate)) {
            val remainingDays = BloodCompatibility.getDonorCooldownRemainingDays(donor.lastDonationDate)
            return@withContext Result.failure(Exception("Donation cooldown active ($remainingDays days remaining). Cannot set to available."))
        }

        dao.updateDonorAvailability(userId, desiredAvailable, System.currentTimeMillis())
        dao.insertActivityLog(
            ActivityLogEntity(
                userId = userId,
                action = "DONOR_AVAILABILITY_CHANGED",
                details = "Donor availability toggled to $desiredAvailable",
                ipAddress = "127.0.0.1"
            )
        )
        Result.success(desiredAvailable)
    }

    suspend fun verifyDonor(donorId: Long, isVerified: Boolean, adminId: Long): Result<Unit> = withContext(Dispatchers.IO) {
        dao.updateDonorVerification(donorId, isVerified, System.currentTimeMillis())
        val statusText = if (isVerified) "VERIFIED" else "UNVERIFIED"
        dao.insertActivityLog(
            ActivityLogEntity(
                userId = adminId,
                action = "DONOR_$statusText",
                details = "Admin set donor verification for Donor ID #$donorId to $isVerified",
                ipAddress = "127.0.0.1"
            )
        )
        Result.success(Unit)
    }

    // --- Blood Banks & Stock ---
    fun getAllBloodBanksFlow(): Flow<List<BloodBankEntity>> = dao.getAllBloodBanksFlow()
    fun getAllBloodStockFlow(): Flow<List<BloodStockEntity>> = dao.getAllBloodStockFlow()
    fun getStockForBankFlow(bankId: Long): Flow<List<BloodStockEntity>> = dao.getStockForBankFlow(bankId)

    suspend fun updateStockUnits(stockId: Long, newUnits: Int, updatedBy: String): Result<Unit> = withContext(Dispatchers.IO) {
        if (newUnits < 0) {
            return@withContext Result.failure(Exception("Stock cannot be negative."))
        }
        dao.updateStockUnits(stockId, newUnits, updatedBy, System.currentTimeMillis())
        dao.insertActivityLog(
            ActivityLogEntity(
                userId = null,
                action = "BLOOD_STOCK_UPDATED",
                details = "$updatedBy updated stock ID #$stockId to $newUnits units",
                ipAddress = "127.0.0.1"
            )
        )
        Result.success(Unit)
    }

    suspend fun addBloodBank(bank: BloodBankEntity, adminId: Long): Result<Long> = withContext(Dispatchers.IO) {
        val id = dao.insertBloodBank(bank)
        // Seed stock for new bank
        val bloodGroups = BloodCompatibility.ALL_BLOOD_GROUPS
        val components = BloodCompatibility.ALL_COMPONENTS
        val stockList = mutableListOf<BloodStockEntity>()
        val now = System.currentTimeMillis()
        for (bg in bloodGroups) {
            for (comp in components) {
                stockList.add(
                    BloodStockEntity(
                        bloodBankId = id,
                        bloodGroup = bg,
                        component = comp,
                        unitsAvailable = 5,
                        lastUpdatedBy = "Admin",
                        updatedAt = now
                    )
                )
            }
        }
        dao.insertAllBloodStock(stockList)

        dao.insertActivityLog(
            ActivityLogEntity(
                userId = adminId,
                action = "BLOOD_BANK_ADDED",
                details = "Admin added blood bank: ${bank.name} in ${bank.city}",
                ipAddress = "127.0.0.1"
            )
        )
        Result.success(id)
    }

    // --- Emergency Requests ---
    fun getAllEmergencyRequestsFlow(): Flow<List<EmergencyRequestEntity>> = dao.getAllEmergencyRequestsFlow()
    fun getRequestsByUserFlow(userId: Long): Flow<List<EmergencyRequestEntity>> = dao.getRequestsByUserFlow(userId)

    suspend fun createEmergencyRequest(request: EmergencyRequestEntity): Result<Long> = withContext(Dispatchers.IO) {
        val id = dao.insertEmergencyRequest(request.copy(status = "PENDING", createdAt = System.currentTimeMillis()))
        dao.insertActivityLog(
            ActivityLogEntity(
                userId = request.requestedByUserId,
                action = "EMERGENCY_REQUEST_CREATED",
                details = "Urgent blood request created: ${request.patientName}, ${request.bloodGroup}, ${request.component}, ${request.unitsNeeded} units at ${request.hospitalName} (${request.urgencyLevel})",
                ipAddress = "127.0.0.1"
            )
        )
        Result.success(id)
    }

    suspend fun approveEmergencyRequest(requestId: Long, adminId: Long): Result<Int> = withContext(Dispatchers.IO) {
        val req = dao.getEmergencyRequestById(requestId)
            ?: return@withContext Result.failure(Exception("Request not found."))

        dao.updateEmergencyRequestStatus(requestId, "APPROVED", adminId, System.currentTimeMillis())

        // Find eligible nearby donors and generate donation alerts
        val compatibleGroups = BloodCompatibility.getCompatibleDonorsForRecipient(req.bloodGroup)
        val allDonors = dao.getAllDonorProfilesList()

        var alertsCreated = 0
        for (donor in allDonors) {
            // Check blood group compatibility
            if (!compatibleGroups.contains(donor.bloodGroup)) continue

            // Check availability & 90-day cooldown
            if (!donor.isAvailable || BloodCompatibility.isCooldownActive(donor.lastDonationDate)) continue

            // Check medical suitability
            if (donor.hasChronicIllness || donor.recentSurgery || donor.onMedication || !donor.consentGiven) continue

            // Check if alert already exists
            val count = dao.countAlertsForRequestAndDonor(requestId, donor.id)
            if (count == 0) {
                dao.insertDonationAlert(
                    DonationAlertEntity(
                        emergencyRequestId = requestId,
                        donorId = donor.id,
                        status = "PENDING",
                        createdAt = System.currentTimeMillis()
                    )
                )
                alertsCreated++
            }
        }

        dao.insertActivityLog(
            ActivityLogEntity(
                userId = adminId,
                action = "REQUEST_APPROVED",
                details = "Admin approved Emergency Request #$requestId (${req.bloodGroup}). Broadcasted $alertsCreated donation alerts.",
                ipAddress = "127.0.0.1"
            )
        )

        Result.success(alertsCreated)
    }

    suspend fun updateRequestStatus(requestId: Long, status: String, adminId: Long): Result<Unit> = withContext(Dispatchers.IO) {
        dao.updateEmergencyRequestStatus(requestId, status, adminId, System.currentTimeMillis())
        dao.insertActivityLog(
            ActivityLogEntity(
                userId = adminId,
                action = "REQUEST_STATUS_UPDATED",
                details = "Emergency Request #$requestId status changed to $status",
                ipAddress = "127.0.0.1"
            )
        )
        Result.success(Unit)
    }

    // --- Alerts ---
    fun getAlertsForDonorFlow(donorId: Long): Flow<List<AlertDisplayItem>> {
        return combine(
            dao.getAlertsForDonorFlow(donorId),
            dao.getAllEmergencyRequestsFlow(),
            dao.getAllUsersFlow()
        ) { alerts, requests, users ->
            val reqMap = requests.associateBy { it.id }
            val userMap = users.associateBy { it.id }

            alerts.mapNotNull { alert ->
                val request = reqMap[alert.emergencyRequestId] ?: return@mapNotNull null
                val requester = userMap[request.requestedByUserId]
                AlertDisplayItem(
                    alert = alert,
                    request = request,
                    requesterPhone = requester?.phoneNumber ?: request.contactPhone,
                    requesterName = requester?.fullName ?: "Emergency Requester"
                )
            }
        }
    }

    suspend fun respondToAlert(alertId: Long, isAccepted: Boolean, donorUserId: Long): Result<Unit> = withContext(Dispatchers.IO) {
        val newStatus = if (isAccepted) "ACCEPTED" else "DECLINED"
        dao.updateAlertStatus(alertId, newStatus, System.currentTimeMillis())
        dao.insertActivityLog(
            ActivityLogEntity(
                userId = donorUserId,
                action = "DONOR_ALERT_RESPONSE",
                details = "Donor responded $newStatus to alert #$alertId",
                ipAddress = "127.0.0.1"
            )
        )
        Result.success(Unit)
    }

    // --- Admin User Management ---
    fun getAllUsersFlow(): Flow<List<UserEntity>> = dao.getAllUsersFlow()

    suspend fun toggleUserBlocked(userId: Long, shouldBlock: Boolean, adminId: Long): Result<Unit> = withContext(Dispatchers.IO) {
        val newStatus = if (shouldBlock) "BLOCKED" else "ACTIVE"
        dao.updateUserStatus(userId, newStatus, System.currentTimeMillis())
        dao.insertActivityLog(
            ActivityLogEntity(
                userId = adminId,
                action = if (shouldBlock) "USER_BLOCKED" else "USER_UNBLOCKED",
                details = "Admin updated status for User #$userId to $newStatus",
                ipAddress = "127.0.0.1"
            )
        )
        Result.success(Unit)
    }

    // --- Activity Log ---
    fun getAllActivityLogsFlow(): Flow<List<ActivityLogEntity>> = dao.getAllActivityLogsFlow()
}
