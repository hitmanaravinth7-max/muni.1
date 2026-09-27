# BloodBridge – Emergency Blood Availability & Donor Matching System

> **"Connecting Blood Donors With Those Who Need Them."**

BloodBridge is a mission-critical emergency blood availability and voluntary donor matching platform designed for rapid response during healthcare crises. The system seamlessly connects patients, hospitals, authorized blood banks, and verified voluntary donors while enforcing strict privacy rules, donor cooldown periods, and medical criteria.

---

## 1. Project Overview

During life-threatening emergencies, every second counts. BloodBridge delivers:
- **Instant Donor Matching**: Real-time matching by ABO/Rh blood groups and universal donor/recipient rules (e.g., O- universal donor, AB+ universal recipient).
- **Automated Donor Cooldown Enforcement**: Enforces a strict 90-day cooldown between whole blood donations. If a donor donated within 90 days, their availability is automatically disabled and marked with remaining cooldown days.
- **Privacy-First Donor Protection**: Guest and public users never see exact residential addresses, raw phone numbers, or private health details. Contact info is unlocked only when an authorized donor accepts an emergency alert.
- **Blood Bank & Multi-Component Stock**: Real-time tracking of blood inventory across 8 blood groups (`A+`, `A-`, `B+`, `B-`, `AB+`, `AB-`, `O+`, `O-`) and 4 components (`WHOLE_BLOOD`, `PLASMA`, `PLATELETS`, `RBC`). Stock levels can never be negative.
- **Emergency Broadcast Workflow**: When an emergency request is approved by an administrator, automated donation alerts are dispatched to matching compatible donors in the area without duplicates.
- **Read-Only Audit Activity Log**: Immutable log recording logins, registrations, status updates, request approvals, and stock changes.

---

## 2. Technology Stack

### Mobile Client (Android Jetpack Compose)
- **Language**: Kotlin 2.2.10
- **UI Toolkit**: Jetpack Compose with Material Design 3 (M3)
- **Architecture**: MVVM (Model-View-ViewModel) + Repository Pattern
- **Persistence**: Room Database with Kotlin Coroutines & Flow
- **Navigation**: BackHandler-backed stateful navigation with deep route protection
- **Intent Services**: Native Google Maps directions (`geo:lat,lng` intent) & Android Dialer (`tel:`)

### Backend Architecture Reference (Spring Boot 3 + MySQL 8)
- **Framework**: Java 17+, Spring Boot 3.2+
- **Security**: Spring Security 6 with JWT & BCrypt password hashing
- **ORM / Persistence**: Spring Data JPA / Hibernate
- **Database**: MySQL 8+ / PostgreSQL

---

## 3. Project & Folder Structure

```
├── app/
│   ├── src/main/
│   │   ├── AndroidManifest.xml
│   │   ├── java/com/example/
│   │   │   ├── BloodBridgeApp.kt
│   │   │   ├── MainActivity.kt
│   │   │   ├── data/
│   │   │   │   ├── dao/
│   │   │   │   │   └── BloodBridgeDao.kt
│   │   │   │   ├── database/
│   │   │   │   │   └── AppDatabase.kt
│   │   │   │   ├── entity/
│   │   │   │   │   └── Entities.kt
│   │   │   │   └── repository/
│   │   │   │       └── BloodBridgeRepository.kt
│   │   │   ├── ui/
│   │   │   │   ├── components/
│   │   │   │   │   └── BloodBridgeComponents.kt
│   │   │   │   ├── screens/
│   │   │   │   │   ├── HomeScreen.kt
│   │   │   │   │   ├── SearchDonorsScreen.kt
│   │   │   │   │   ├── BloodBanksScreen.kt
│   │   │   │   │   ├── HowItWorksScreen.kt
│   │   │   │   │   ├── LoginScreen.kt
│   │   │   │   │   ├── RegisterScreen.kt
│   │   │   │   │   ├── UserDashboardScreen.kt
│   │   │   │   │   ├── DonorProfileScreen.kt
│   │   │   │   │   ├── EmergencyRequestScreen.kt
│   │   │   │   │   ├── MyRequestsScreen.kt
│   │   │   │   │   ├── DonorAlertsScreen.kt
│   │   │   │   │   └── admin/
│   │   │   │   │       ├── AdminDashboardScreen.kt
│   │   │   │   │       ├── AdminUsersScreen.kt
│   │   │   │   │       ├── AdminDonorsScreen.kt
│   │   │   │   │       ├── AdminBloodBanksScreen.kt
│   │   │   │   │       ├── AdminRequestsScreen.kt
│   │   │   │   │       └── AdminActivityLogScreen.kt
│   │   │   │   ├── theme/
│   │   │   │   │   ├── Color.kt
│   │   │   │   │   ├── Theme.kt
│   │   │   │   │   └── Type.kt
│   │   │   │   └── viewmodel/
│   │   │   │       └── BloodBridgeViewModel.kt
│   │   │   └── util/
│   │   │       ├── AppIntents.kt
│   │   │       ├── BloodCompatibility.kt
│   │   │       └── SecurityUtil.kt
│   │   └── res/
│   │       ├── drawable/
│   │       │   ├── ic_launcher_background.xml
│   │       │   └── ic_launcher_foreground.xml
│   │       ├── mipmap-*/
│   │       └── values/
│   │           ├── colors.xml
│   │           ├── strings.xml
│   │           └── themes.xml
│   └── build.gradle.kts
├── metadata.json
├── settings.gradle.kts
└── README.md
```

---

## 4. Database Schema (Entities & Relationships)

### `users`
| Column | Type | Description |
|---|---|---|
| `id` | BIGINT PK AUTO_INCREMENT | Unique User identifier |
| `full_name` | VARCHAR(100) | Full Name |
| `email` | VARCHAR(100) UNIQUE | Email Address |
| `password_hash` | VARCHAR(255) | Salted Hash of Password |
| `phone_number` | VARCHAR(20) | Phone Number |
| `role` | VARCHAR(20) | `USER` or `ADMIN` |
| `account_status` | VARCHAR(20) | `ACTIVE` or `BLOCKED` |
| `created_at` | BIGINT | Timestamp millis |
| `updated_at` | BIGINT | Timestamp millis |

### `donor_profiles`
| Column | Type | Description |
|---|---|---|
| `id` | BIGINT PK AUTO_INCREMENT | Donor Profile ID |
| `user_id` | BIGINT FK (`users.id`) | Linked User ID (1-to-1) |
| `blood_group` | VARCHAR(5) | `A+`, `A-`, `B+`, `B-`, `AB+`, `AB-`, `O+`, `O-` |
| `date_of_birth` | VARCHAR(10) | Date of Birth (YYYY-MM-DD) |
| `gender` | VARCHAR(10) | `MALE`, `FEMALE`, `OTHER` |
| `weight_kg` | DOUBLE | Weight in Kilograms (>= 50 kg) |
| `address_line` | VARCHAR(255) | Residential street / area |
| `city` | VARCHAR(100) | City / District |
| `state` | VARCHAR(100) | State / Province |
| `pincode` | VARCHAR(10) | Postal PIN code |
| `latitude` | DOUBLE | GPS Latitude |
| `longitude` | DOUBLE | GPS Longitude |
| `last_donation_date` | BIGINT NULL | Epoch millis of last donation |
| `is_available` | BOOLEAN | Availability toggle (cooldown locks to false) |
| `has_chronic_illness` | BOOLEAN | Chronic disease disclosure |
| `on_medication` | BOOLEAN | Prescription medication disclosure |
| `recent_surgery` | BOOLEAN | Surgery/tattoo within 6 months |
| `total_donations` | INT | Lifetime donation count |
| `consent_given` | BOOLEAN | Required voluntary consent |
| `verified_by_admin` | BOOLEAN | Verification status by admin |

### `blood_banks`
| Column | Type | Description |
|---|---|---|
| `id` | BIGINT PK AUTO_INCREMENT | Blood Bank Center ID |
| `name` | VARCHAR(150) | Hospital or Red Cross Center Name |
| `address` | VARCHAR(255) | Street address |
| `city` | VARCHAR(100) | City |
| `state` | VARCHAR(100) | State |
| `pincode` | VARCHAR(10) | PIN Code |
| `latitude` | DOUBLE | Coordinates |
| `longitude` | DOUBLE | Coordinates |
| `contact_phone` | VARCHAR(25) | Emergency direct phone |
| `license_number` | VARCHAR(50) | Medical blood license number |

### `blood_stock`
| Column | Type | Description |
|---|---|---|
| `id` | BIGINT PK AUTO_INCREMENT | Stock Item ID |
| `blood_bank_id` | BIGINT FK | Linked Blood Bank |
| `blood_group` | VARCHAR(5) | Blood group |
| `component` | VARCHAR(20) | `WHOLE_BLOOD`, `PLASMA`, `PLATELETS`, `RBC` |
| `units_available` | INT | Current inventory (>= 0) |
| `last_updated_by` | VARCHAR(100) | Auditor identity |
| `updated_at` | BIGINT | Timestamp |

### `emergency_requests`
| Column | Type | Description |
|---|---|---|
| `id` | BIGINT PK AUTO_INCREMENT | Request ID |
| `requested_by_user_id` | BIGINT FK | Requester user ID |
| `patient_name` | VARCHAR(100) | Patient Name |
| `blood_group` | VARCHAR(5) | Required Blood Group |
| `component` | VARCHAR(20) | Component needed |
| `units_needed` | INT | Quantity |
| `blood_bank_id` | BIGINT NULL | Optional targeted blood center |
| `hospital_name` | VARCHAR(150) | Hospital, Ward & Room details |
| `urgency_level` | VARCHAR(20) | `CRITICAL`, `URGENT`, `NORMAL` |
| `contact_phone` | VARCHAR(25) | Verified contact phone |
| `status` | VARCHAR(20) | `PENDING`, `APPROVED`, `REJECTED`, `FULFILLED`, `CANCELLED` |

### `donation_alerts`
| Column | Type | Description |
|---|---|---|
| `id` | BIGINT PK AUTO_INCREMENT | Alert ID |
| `emergency_request_id` | BIGINT FK | Approved request |
| `donor_id` | BIGINT FK | Target eligible donor |
| `status` | VARCHAR(20) | `PENDING`, `ACCEPTED`, `DECLINED` |
| `responded_at` | BIGINT NULL | Timestamp when donor responded |

### `activity_log`
| Column | Type | Description |
|---|---|---|
| `id` | BIGINT PK AUTO_INCREMENT | Log ID |
| `user_id` | BIGINT NULL | Operating user |
| `action` | VARCHAR(50) | Operation type |
| `details` | TEXT | Audit description |
| `ip_address` | VARCHAR(45) | Client IP |
| `created_at` | BIGINT | Timestamp (read-only) |

---

## 5. REST API Specifications

### Authentication Endpoints
- `POST /api/auth/register` — Register a new USER account.
- `POST /api/auth/login` — Sign in and receive JWT token.
- `POST /api/auth/logout` — Terminate session.
- `GET  /api/auth/me` — Return current authenticated profile.

### Public Endpoints
- `GET  /api/blood-banks` — List all registered blood banks.
- `GET  /api/blood-banks/{id}` — Retrieve bank details.
- `GET  /api/blood-banks/{id}/stock` — Retrieve current inventory by group and component.
- `GET  /api/donors/search` — Search available donors with privacy masking (no raw phone/exact address).

### Authenticated User Endpoints
- `GET  /api/user/profile` — Fetch user profile.
- `PUT  /api/user/profile` — Update name or phone number.
- `POST /api/user/donor-profile` — Register or update donor health criteria.
- `GET  /api/user/donor-profile` — Fetch user's donor status.
- `PUT  /api/user/donor-profile/availability` — Toggle donor availability (cooldown validated).
- `POST /api/requests` — Submit emergency request (Status = `PENDING`).
- `GET  /api/requests/my` — List submitted requests.
- `GET  /api/user/alerts` — List emergency alerts for user's donor profile.
- `PUT  /api/user/alerts/{id}/respond` — Accept or decline an alert (unlocks requester contact info).

### Admin Endpoints
- `GET  /api/admin/users` — Search and list all user accounts.
- `PUT  /api/admin/users/{id}/status` — Block or unblock a user.
- `GET  /api/admin/donors` — List donors with health disclosures.
- `PUT  /api/admin/donors/{id}/verify` — Verify or unverify a donor.
- `POST /api/admin/blood-banks` — Add a new blood bank.
- `PUT  /api/admin/blood-banks/{id}` — Edit blood bank details.
- `PUT  /api/admin/blood-stock/{id}` — Update inventory units (never below zero).
- `GET  /api/admin/requests` — List emergency requests.
- `PUT  /api/admin/requests/{id}/approve` — Approve request and auto-broadcast alerts to compatible donors.
- `PUT  /api/admin/requests/{id}/reject` — Reject request.
- `GET  /api/admin/activity-log` — Read-only system audit log.

---

## 6. Demo Credentials & Seed Data

The database is pre-seeded on first launch with real sample data:

### Default Administrator Account
- **Email**: `admin@bloodbridge.local`
- **Password**: `Admin@12345`
- **Role**: `ADMIN`
- *(Quick fill button available on Login screen)*

### Demo Donor & User Accounts
- **Rahul Sharma** (O+ Donor, Coimbatore): `rahul.sharma@example.com` / `User@12345`
- **Priya Patel** (A+ Donor, Chennai - in 90-day cooldown): `priya.patel@example.com` / `User@12345`
- **Arun Kumar** (B+ Donor, Coimbatore): `arun.kumar@example.com` / `User@12345`
- **Deepak Singh** (O- Universal Donor, Delhi): `deepak.singh@example.com` / `User@12345`

---

## 7. Security & Privacy Rules

1. **Password Security**: Passwords are never stored in plain text. Salting and hashing are enforced prior to persistence.
2. **Role Authorization**: Admin screens (`/admin/*`) are strictly guarded against normal user or guest access.
3. **Protected Donor Details**: Public searches mask raw phone numbers and exact street addresses. Only authorized, verified donation flows reveal contact details.
4. **90-Day Donation Cooldown**: Strictly verified both on the client and in the database layer.
5. **Immutable Audit Trail**: Activity logs cannot be edited or deleted through any UI affordance.
