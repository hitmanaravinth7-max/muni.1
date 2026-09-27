export type BloodGroup = 'A+' | 'A-' | 'B+' | 'B-' | 'AB+' | 'AB-' | 'O+' | 'O-';

export type BloodComponent = 'WHOLE_BLOOD' | 'PLASMA' | 'PLATELETS' | 'RBC';

export type UserRole = 'DONOR' | 'HOSPITAL' | 'ADMIN';

export type UrgencyLevel = 'CRITICAL' | 'URGENT' | 'NORMAL';

export type RequestStatus = 'ACTIVE' | 'FULFILLED' | 'CANCELLED';

export interface User {
  id: string;
  fullName: string;
  email: string;
  role: UserRole;
  phone: string;
  token?: string;
  organizationName?: string;
}

export interface Donor {
  id: string;
  userId: string;
  fullName: string;
  email: string;
  phone: string;
  bloodGroup: BloodGroup;
  city: string;
  state: string;
  distanceKm: number;
  isAvailable: boolean;
  isVerified: boolean;
  lastDonationDate: string | null;
  totalDonations: number;
  latitude: number;
  longitude: number;
}

export interface BloodStockItem {
  component: BloodComponent;
  units: number;
  lastUpdated: string;
}

export interface BloodBank {
  id: string;
  name: string;
  licenseNumber: string;
  address: string;
  city: string;
  state: string;
  phone: string;
  emergencyHotline: string;
  latitude: number;
  longitude: number;
  isVerified: boolean;
  stock: Record<BloodGroup, BloodStockItem[]>;
}

export interface EmergencyRequest {
  id: string;
  patientName: string;
  hospitalName: string;
  bloodGroup: BloodGroup;
  unitsNeeded: number;
  unitsFulfilled: number;
  component: BloodComponent;
  urgency: UrgencyLevel;
  contactPerson: string;
  contactPhone: string;
  city: string;
  requiredDate: string;
  status: RequestStatus;
  createdAt: string;
  notes?: string;
}

export interface DonationAlert {
  id: string;
  requestId: string;
  patientName: string;
  hospitalName: string;
  bloodGroup: BloodGroup;
  unitsNeeded: number;
  urgency: UrgencyLevel;
  timestamp: string;
  city: string;
}

export interface ActivityLog {
  id: string;
  action: string;
  actor: string;
  timestamp: string;
  type: 'DONOR' | 'HOSPITAL' | 'ADMIN' | 'SYSTEM';
}
