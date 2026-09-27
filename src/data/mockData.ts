import { Donor, BloodBank, EmergencyRequest, User, BloodGroup } from '../types';

export const COMPATIBILITY_RULES: Record<BloodGroup, { canGiveTo: BloodGroup[]; canReceiveFrom: BloodGroup[] }> = {
  'O-': {
    canGiveTo: ['O-', 'O+', 'A-', 'A+', 'B-', 'B+', 'AB-', 'AB+'],
    canReceiveFrom: ['O-']
  },
  'O+': {
    canGiveTo: ['O+', 'A+', 'B+', 'AB+'],
    canReceiveFrom: ['O+', 'O-']
  },
  'A-': {
    canGiveTo: ['A-', 'A+', 'AB-', 'AB+'],
    canReceiveFrom: ['A-', 'O-']
  },
  'A+': {
    canGiveTo: ['A+', 'AB+'],
    canReceiveFrom: ['A+', 'A-', 'O+', 'O-']
  },
  'B-': {
    canGiveTo: ['B-', 'B+', 'AB-', 'AB+'],
    canReceiveFrom: ['B-', 'O-']
  },
  'B+': {
    canGiveTo: ['B+', 'AB+'],
    canReceiveFrom: ['B+', 'B-', 'O+', 'O-']
  },
  'AB-': {
    canGiveTo: ['AB-', 'AB+'],
    canReceiveFrom: ['AB-', 'A-', 'B-', 'O-']
  },
  'AB+': {
    canGiveTo: ['AB+'],
    canReceiveFrom: ['AB+', 'AB-', 'A+', 'A-', 'B+', 'B-', 'O+', 'O-']
  }
};

export const INITIAL_USERS: User[] = [
  {
    id: 'user_admin',
    fullName: 'Dr. Sarah Mitchell',
    email: 'admin@bloodbridge.org',
    role: 'ADMIN',
    phone: '+1 (800) 555-0199',
    organizationName: 'BloodBridge National Operations'
  },
  {
    id: 'user_hospital',
    fullName: 'Robert Hayes (Coordinator)',
    email: 'hospital@metrohealth.org',
    role: 'HOSPITAL',
    phone: '+1 (555) 321-7788',
    organizationName: 'Metropolitan General Trauma Center'
  },
  {
    id: 'user_donor_1',
    fullName: 'Alexander Chen',
    email: 'alex.chen@example.com',
    role: 'DONOR',
    phone: '+1 (555) 439-8821'
  }
];

export const INITIAL_DONORS: Donor[] = [
  {
    id: 'donor-1',
    userId: 'user_donor_1',
    fullName: 'Alexander Chen',
    email: 'alex.chen@example.com',
    phone: '+1 (555) 439-8821',
    bloodGroup: 'O-',
    city: 'Downtown Metro',
    state: 'NY',
    distanceKm: 2.4,
    isAvailable: true,
    isVerified: true,
    lastDonationDate: '2026-05-14',
    totalDonations: 8,
    latitude: 40.7128,
    longitude: -74.0060
  },
  {
    id: 'donor-2',
    userId: 'user_donor_2',
    fullName: 'Elena Rostova',
    email: 'elena.rostova@example.com',
    phone: '+1 (555) 892-1144',
    bloodGroup: 'A+',
    city: 'Westside Medical District',
    state: 'NY',
    distanceKm: 4.8,
    isAvailable: true,
    isVerified: true,
    lastDonationDate: '2026-06-20',
    totalDonations: 5,
    latitude: 40.7306,
    longitude: -73.9352
  },
  {
    id: 'donor-3',
    userId: 'user_donor_3',
    fullName: 'Marcus Vance',
    email: 'marcus.v@example.com',
    phone: '+1 (555) 671-9902',
    bloodGroup: 'B+',
    city: 'North Hills',
    state: 'NY',
    distanceKm: 7.1,
    isAvailable: true,
    isVerified: false,
    lastDonationDate: '2026-04-10',
    totalDonations: 3,
    latitude: 40.7829,
    longitude: -73.9654
  },
  {
    id: 'donor-4',
    userId: 'user_donor_4',
    fullName: 'Priya Sharma',
    email: 'priya.s@example.com',
    phone: '+1 (555) 234-5678',
    bloodGroup: 'AB+',
    city: 'Downtown Metro',
    state: 'NY',
    distanceKm: 1.8,
    isAvailable: true,
    isVerified: true,
    lastDonationDate: '2026-07-02',
    totalDonations: 12,
    latitude: 40.7150,
    longitude: -74.0010
  },
  {
    id: 'donor-5',
    userId: 'user_donor_5',
    fullName: 'David K. O’Connor',
    email: 'david.oc@example.com',
    phone: '+1 (555) 876-5432',
    bloodGroup: 'O+',
    city: 'Harbor Point',
    state: 'NY',
    distanceKm: 5.5,
    isAvailable: false,
    isVerified: true,
    lastDonationDate: '2026-08-15',
    totalDonations: 4,
    latitude: 40.7020,
    longitude: -74.0150
  },
  {
    id: 'donor-6',
    userId: 'user_donor_6',
    fullName: 'Sophia Martinez',
    email: 'sophia.m@example.com',
    phone: '+1 (555) 345-6789',
    bloodGroup: 'A-',
    city: 'Westside Medical District',
    state: 'NY',
    distanceKm: 3.2,
    isAvailable: true,
    isVerified: true,
    lastDonationDate: '2026-05-30',
    totalDonations: 7,
    latitude: 40.7410,
    longitude: -73.9890
  },
  {
    id: 'donor-7',
    userId: 'user_donor_7',
    fullName: 'Liam Washington',
    email: 'liam.w@example.com',
    phone: '+1 (555) 901-2345',
    bloodGroup: 'AB-',
    city: 'North Hills',
    state: 'NY',
    distanceKm: 9.0,
    isAvailable: true,
    isVerified: false,
    lastDonationDate: '2026-03-25',
    totalDonations: 2,
    latitude: 40.7910,
    longitude: -73.9520
  },
  {
    id: 'donor-8',
    userId: 'user_donor_8',
    fullName: 'Grace Kim',
    email: 'grace.kim@example.com',
    phone: '+1 (555) 789-0123',
    bloodGroup: 'B-',
    city: 'Downtown Metro',
    state: 'NY',
    distanceKm: 2.1,
    isAvailable: true,
    isVerified: true,
    lastDonationDate: '2026-06-11',
    totalDonations: 6,
    latitude: 40.7200,
    longitude: -74.0040
  }
];

export const INITIAL_BLOOD_BANKS: BloodBank[] = [
  {
    id: 'bb-1',
    name: 'Metropolitan Red Cross Blood Institute',
    licenseNumber: 'FDA-BB-89211-NY',
    address: '450 East 29th Street, Suite 400',
    city: 'Downtown Metro',
    state: 'NY',
    phone: '+1 (212) 555-0144',
    emergencyHotline: '+1 (800) 988-7711',
    latitude: 40.7405,
    longitude: -73.9745,
    isVerified: true,
    stock: {
      'O-': [
        { component: 'WHOLE_BLOOD', units: 4, lastUpdated: '10 mins ago' },
        { component: 'RBC', units: 3, lastUpdated: '15 mins ago' },
        { component: 'PLASMA', units: 6, lastUpdated: '1 hour ago' },
        { component: 'PLATELETS', units: 2, lastUpdated: '30 mins ago' }
      ],
      'O+': [
        { component: 'WHOLE_BLOOD', units: 14, lastUpdated: '10 mins ago' },
        { component: 'RBC', units: 18, lastUpdated: '15 mins ago' },
        { component: 'PLASMA', units: 12, lastUpdated: '1 hour ago' },
        { component: 'PLATELETS', units: 7, lastUpdated: '30 mins ago' }
      ],
      'A+': [
        { component: 'WHOLE_BLOOD', units: 22, lastUpdated: '20 mins ago' },
        { component: 'RBC', units: 25, lastUpdated: '20 mins ago' },
        { component: 'PLASMA', units: 14, lastUpdated: '1 hour ago' },
        { component: 'PLATELETS', units: 9, lastUpdated: '45 mins ago' }
      ],
      'A-': [
        { component: 'WHOLE_BLOOD', units: 5, lastUpdated: '15 mins ago' },
        { component: 'RBC', units: 4, lastUpdated: '15 mins ago' },
        { component: 'PLASMA', units: 5, lastUpdated: '2 hours ago' },
        { component: 'PLATELETS', units: 3, lastUpdated: '1 hour ago' }
      ],
      'B+': [
        { component: 'WHOLE_BLOOD', units: 16, lastUpdated: '35 mins ago' },
        { component: 'RBC', units: 19, lastUpdated: '35 mins ago' },
        { component: 'PLASMA', units: 11, lastUpdated: '3 hours ago' },
        { component: 'PLATELETS', units: 6, lastUpdated: '1 hour ago' }
      ],
      'B-': [
        { component: 'WHOLE_BLOOD', units: 3, lastUpdated: '25 mins ago' },
        { component: 'RBC', units: 2, lastUpdated: '25 mins ago' },
        { component: 'PLASMA', units: 4, lastUpdated: '2 hours ago' },
        { component: 'PLATELETS', units: 1, lastUpdated: '15 mins ago' }
      ],
      'AB+': [
        { component: 'WHOLE_BLOOD', units: 12, lastUpdated: '40 mins ago' },
        { component: 'RBC', units: 15, lastUpdated: '40 mins ago' },
        { component: 'PLASMA', units: 18, lastUpdated: '2 hours ago' },
        { component: 'PLATELETS', units: 5, lastUpdated: '1 hour ago' }
      ],
      'AB-': [
        { component: 'WHOLE_BLOOD', units: 2, lastUpdated: '10 mins ago' },
        { component: 'RBC', units: 1, lastUpdated: '10 mins ago' },
        { component: 'PLASMA', units: 4, lastUpdated: '3 hours ago' },
        { component: 'PLATELETS', units: 2, lastUpdated: '20 mins ago' }
      ]
    }
  },
  {
    id: 'bb-2',
    name: 'St. Jude University Trauma Blood Center',
    licenseNumber: 'FDA-BB-55201-NY',
    address: '1300 York Avenue, Pavilion Wing 2',
    city: 'Westside Medical District',
    state: 'NY',
    phone: '+1 (212) 746-5454',
    emergencyHotline: '+1 (800) 441-2299',
    latitude: 40.7645,
    longitude: -73.9535,
    isVerified: true,
    stock: {
      'O-': [
        { component: 'WHOLE_BLOOD', units: 1, lastUpdated: '5 mins ago' },
        { component: 'RBC', units: 2, lastUpdated: '15 mins ago' },
        { component: 'PLASMA', units: 3, lastUpdated: '1 hour ago' },
        { component: 'PLATELETS', units: 1, lastUpdated: '25 mins ago' }
      ],
      'O+': [
        { component: 'WHOLE_BLOOD', units: 9, lastUpdated: '15 mins ago' },
        { component: 'RBC', units: 11, lastUpdated: '15 mins ago' },
        { component: 'PLASMA', units: 8, lastUpdated: '2 hours ago' },
        { component: 'PLATELETS', units: 4, lastUpdated: '35 mins ago' }
      ],
      'A+': [
        { component: 'WHOLE_BLOOD', units: 18, lastUpdated: '30 mins ago' },
        { component: 'RBC', units: 21, lastUpdated: '30 mins ago' },
        { component: 'PLASMA', units: 10, lastUpdated: '1 hour ago' },
        { component: 'PLATELETS', units: 6, lastUpdated: '40 mins ago' }
      ],
      'A-': [
        { component: 'WHOLE_BLOOD', units: 2, lastUpdated: '20 mins ago' },
        { component: 'RBC', units: 3, lastUpdated: '20 mins ago' },
        { component: 'PLASMA', units: 4, lastUpdated: '3 hours ago' },
        { component: 'PLATELETS', units: 2, lastUpdated: '1 hour ago' }
      ],
      'B+': [
        { component: 'WHOLE_BLOOD', units: 13, lastUpdated: '10 mins ago' },
        { component: 'RBC', units: 14, lastUpdated: '10 mins ago' },
        { component: 'PLASMA', units: 7, lastUpdated: '2 hours ago' },
        { component: 'PLATELETS', units: 3, lastUpdated: '50 mins ago' }
      ],
      'B-': [
        { component: 'WHOLE_BLOOD', units: 2, lastUpdated: '45 mins ago' },
        { component: 'RBC', units: 1, lastUpdated: '45 mins ago' },
        { component: 'PLASMA', units: 3, lastUpdated: '4 hours ago' },
        { component: 'PLATELETS', units: 1, lastUpdated: '30 mins ago' }
      ],
      'AB+': [
        { component: 'WHOLE_BLOOD', units: 8, lastUpdated: '25 mins ago' },
        { component: 'RBC', units: 9, lastUpdated: '25 mins ago' },
        { component: 'PLASMA', units: 12, lastUpdated: '1 hour ago' },
        { component: 'PLATELETS', units: 4, lastUpdated: '45 mins ago' }
      ],
      'AB-': [
        { component: 'WHOLE_BLOOD', units: 1, lastUpdated: '10 mins ago' },
        { component: 'RBC', units: 1, lastUpdated: '10 mins ago' },
        { component: 'PLASMA', units: 2, lastUpdated: '2 hours ago' },
        { component: 'PLATELETS', units: 1, lastUpdated: '15 mins ago' }
      ]
    }
  }
];

export const INITIAL_EMERGENCY_REQUESTS: EmergencyRequest[] = [
  {
    id: 'req-101',
    patientName: 'Emma Richardson (ICU Bed 4)',
    hospitalName: 'Metropolitan General Trauma Center',
    bloodGroup: 'O-',
    unitsNeeded: 3,
    unitsFulfilled: 1,
    component: 'RBC',
    urgency: 'CRITICAL',
    contactPerson: 'Dr. Aaron Vance, ER Chief',
    contactPhone: '+1 (555) 911-0422',
    city: 'Downtown Metro',
    requiredDate: 'IMMEDIATE',
    status: 'ACTIVE',
    createdAt: '15 mins ago',
    notes: 'Emergency surgical intervention following multi-vehicle accident. Universal donor required urgently.'
  },
  {
    id: 'req-102',
    patientName: 'Lucas Morales',
    hospitalName: 'St. Jude University Trauma Center',
    bloodGroup: 'B-',
    unitsNeeded: 2,
    unitsFulfilled: 0,
    component: 'PLATELETS',
    urgency: 'URGENT',
    contactPerson: 'Nurse Supervisor Karen Diaz',
    contactPhone: '+1 (555) 746-5510',
    city: 'Westside Medical District',
    requiredDate: 'Within 4 hours',
    status: 'ACTIVE',
    createdAt: '45 mins ago',
    notes: 'Oncology patient requiring platelet transfusion before chemotherapy cycle.'
  },
  {
    id: 'req-103',
    patientName: 'Grace Sterling',
    hospitalName: 'Mercy Children’s Hospital',
    bloodGroup: 'A+',
    unitsNeeded: 4,
    unitsFulfilled: 4,
    component: 'WHOLE_BLOOD',
    urgency: 'NORMAL',
    contactPerson: 'Blood Bank Desk',
    contactPhone: '+1 (555) 321-9988',
    city: 'North Hills',
    requiredDate: 'Tomorrow 9:00 AM',
    status: 'FULFILLED',
    createdAt: '3 hours ago',
    notes: 'Scheduled pediatric cardiac reconstruction procedure.'
  }
];
