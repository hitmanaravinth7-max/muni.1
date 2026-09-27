import React, { createContext, useContext, useState, useEffect } from 'react';
import { User, Donor, BloodBank, EmergencyRequest, ActivityLog, BloodGroup, BloodComponent, UrgencyLevel } from '../types';
import { INITIAL_USERS, INITIAL_DONORS, INITIAL_BLOOD_BANKS, INITIAL_EMERGENCY_REQUESTS } from '../data/mockData';

interface AppContextType {
  currentUser: User | null;
  donors: Donor[];
  bloodBanks: BloodBank[];
  emergencyRequests: EmergencyRequest[];
  activityLogs: ActivityLog[];
  login: (email: string, role?: string) => Promise<boolean>;
  logout: () => void;
  registerDonor: (donorData: Omit<Donor, 'id' | 'distanceKm' | 'totalDonations' | 'lastDonationDate'>) => Promise<boolean>;
  registerBloodBank: (bankData: Omit<BloodBank, 'id' | 'stock'>) => Promise<boolean>;
  createEmergencyRequest: (reqData: Omit<EmergencyRequest, 'id' | 'createdAt' | 'unitsFulfilled' | 'status'>) => void;
  fulfillRequest: (id: string, unitsAdded?: number) => void;
  toggleDonorAvailability: (donorId: string) => void;
  updateBloodStock: (bankId: string, group: BloodGroup, component: BloodComponent, delta: number) => void;
  verifyDonor: (donorId: string) => void;
}

const AppContext = createContext<AppContextType | undefined>(undefined);

export const AppProvider: React.FC<{ children: React.ReactNode }> = ({ children }) => {
  const [currentUser, setCurrentUser] = useState<User | null>(() => {
    const saved = localStorage.getItem('bloodbridge_user');
    return saved ? JSON.parse(saved) : null;
  });

  const [donors, setDonors] = useState<Donor[]>(() => {
    const saved = localStorage.getItem('bloodbridge_donors');
    return saved ? JSON.parse(saved) : INITIAL_DONORS;
  });

  const [bloodBanks, setBloodBanks] = useState<BloodBank[]>(() => {
    const saved = localStorage.getItem('bloodbridge_blood_banks');
    return saved ? JSON.parse(saved) : INITIAL_BLOOD_BANKS;
  });

  const [emergencyRequests, setEmergencyRequests] = useState<EmergencyRequest[]>(() => {
    const saved = localStorage.getItem('bloodbridge_requests');
    return saved ? JSON.parse(saved) : INITIAL_EMERGENCY_REQUESTS;
  });

  const [activityLogs, setActivityLogs] = useState<ActivityLog[]>(() => {
    const saved = localStorage.getItem('bloodbridge_logs');
    return saved ? JSON.parse(saved) : [
      { id: '1', action: 'System online & monitoring emergency feeds', actor: 'System', timestamp: 'Just now', type: 'SYSTEM' },
      { id: '2', action: 'Emergency request req-101 broadcasted for O- units', actor: 'Metropolitan General', timestamp: '15 mins ago', type: 'HOSPITAL' },
      { id: '3', action: 'Donor Alexander Chen verified', actor: 'Admin', timestamp: '2 hours ago', type: 'ADMIN' },
    ];
  });

  useEffect(() => {
    localStorage.setItem('bloodbridge_donors', JSON.stringify(donors));
  }, [donors]);

  useEffect(() => {
    localStorage.setItem('bloodbridge_blood_banks', JSON.stringify(bloodBanks));
  }, [bloodBanks]);

  useEffect(() => {
    localStorage.setItem('bloodbridge_requests', JSON.stringify(emergencyRequests));
  }, [emergencyRequests]);

  useEffect(() => {
    localStorage.setItem('bloodbridge_logs', JSON.stringify(activityLogs));
  }, [activityLogs]);

  useEffect(() => {
    if (currentUser) {
      localStorage.setItem('bloodbridge_user', JSON.stringify(currentUser));
    } else {
      localStorage.removeItem('bloodbridge_user');
    }
  }, [currentUser]);

  const addLog = (action: string, actor: string, type: 'DONOR' | 'HOSPITAL' | 'ADMIN' | 'SYSTEM') => {
    const newLog: ActivityLog = {
      id: Date.now().toString(),
      action,
      actor,
      timestamp: 'Just now',
      type,
    };
    setActivityLogs(prev => [newLog, ...prev.slice(0, 40)]);
  };

  const login = async (email: string, _role?: string): Promise<boolean> => {
    const foundUser = INITIAL_USERS.find(u => u.email.toLowerCase() === email.toLowerCase());
    if (foundUser) {
      setCurrentUser(foundUser);
      addLog(`User ${foundUser.fullName} logged in as ${foundUser.role}`, foundUser.fullName, foundUser.role);
      return true;
    }
    // Also check if any donor matches email
    const donorUser = donors.find(d => d.email.toLowerCase() === email.toLowerCase());
    if (donorUser) {
      const user: User = {
        id: donorUser.userId,
        fullName: donorUser.fullName,
        email: donorUser.email,
        phone: donorUser.phone,
        role: 'DONOR',
      };
      setCurrentUser(user);
      addLog(`Donor ${user.fullName} logged in`, user.fullName, 'DONOR');
      return true;
    }

    // Dynamic login for demo purposes
    const role: 'DONOR' | 'HOSPITAL' | 'ADMIN' = email.includes('admin') ? 'ADMIN' : email.includes('hospital') ? 'HOSPITAL' : 'DONOR';
    const genericUser: User = {
      id: `user_${Date.now()}`,
      fullName: email.split('@')[0].replace('.', ' '),
      email,
      phone: '+1 (555) 000-0000',
      role,
    };
    setCurrentUser(genericUser);
    addLog(`User ${genericUser.fullName} signed in as ${genericUser.role}`, genericUser.fullName, genericUser.role);
    return true;
  };

  const logout = () => {
    if (currentUser) {
      addLog(`User ${currentUser.fullName} logged out`, currentUser.fullName, currentUser.role);
    }
    setCurrentUser(null);
  };

  const registerDonor = async (donorData: Omit<Donor, 'id' | 'distanceKm' | 'totalDonations' | 'lastDonationDate'>): Promise<boolean> => {
    const newDonor: Donor = {
      ...donorData,
      id: `donor-${Date.now()}`,
      distanceKm: 2.5,
      totalDonations: 0,
      lastDonationDate: null,
    };
    setDonors(prev => [newDonor, ...prev]);

    const user: User = {
      id: donorData.userId,
      fullName: donorData.fullName,
      email: donorData.email,
      phone: donorData.phone,
      role: 'DONOR',
    };
    setCurrentUser(user);
    addLog(`New donor ${newDonor.fullName} (${newDonor.bloodGroup}) registered`, newDonor.fullName, 'DONOR');
    return true;
  };

  const registerBloodBank = async (bankData: Omit<BloodBank, 'id' | 'stock'>): Promise<boolean> => {
    const defaultStock: Record<BloodGroup, any[]> = {
      'O-': [{ component: 'WHOLE_BLOOD', units: 5, lastUpdated: 'Just now' }, { component: 'RBC', units: 4, lastUpdated: 'Just now' }, { component: 'PLASMA', units: 3, lastUpdated: 'Just now' }, { component: 'PLATELETS', units: 2, lastUpdated: 'Just now' }],
      'O+': [{ component: 'WHOLE_BLOOD', units: 10, lastUpdated: 'Just now' }, { component: 'RBC', units: 12, lastUpdated: 'Just now' }, { component: 'PLASMA', units: 8, lastUpdated: 'Just now' }, { component: 'PLATELETS', units: 5, lastUpdated: 'Just now' }],
      'A+': [{ component: 'WHOLE_BLOOD', units: 15, lastUpdated: 'Just now' }, { component: 'RBC', units: 18, lastUpdated: 'Just now' }, { component: 'PLASMA', units: 10, lastUpdated: 'Just now' }, { component: 'PLATELETS', units: 6, lastUpdated: 'Just now' }],
      'A-': [{ component: 'WHOLE_BLOOD', units: 4, lastUpdated: 'Just now' }, { component: 'RBC', units: 3, lastUpdated: 'Just now' }, { component: 'PLASMA', units: 4, lastUpdated: 'Just now' }, { component: 'PLATELETS', units: 2, lastUpdated: 'Just now' }],
      'B+': [{ component: 'WHOLE_BLOOD', units: 12, lastUpdated: 'Just now' }, { component: 'RBC', units: 15, lastUpdated: 'Just now' }, { component: 'PLASMA', units: 7, lastUpdated: 'Just now' }, { component: 'PLATELETS', units: 4, lastUpdated: 'Just now' }],
      'B-': [{ component: 'WHOLE_BLOOD', units: 3, lastUpdated: 'Just now' }, { component: 'RBC', units: 2, lastUpdated: 'Just now' }, { component: 'PLASMA', units: 3, lastUpdated: 'Just now' }, { component: 'PLATELETS', units: 1, lastUpdated: 'Just now' }],
      'AB+': [{ component: 'WHOLE_BLOOD', units: 8, lastUpdated: 'Just now' }, { component: 'RBC', units: 10, lastUpdated: 'Just now' }, { component: 'PLASMA', units: 12, lastUpdated: 'Just now' }, { component: 'PLATELETS', units: 4, lastUpdated: 'Just now' }],
      'AB-': [{ component: 'WHOLE_BLOOD', units: 2, lastUpdated: 'Just now' }, { component: 'RBC', units: 1, lastUpdated: 'Just now' }, { component: 'PLASMA', units: 2, lastUpdated: 'Just now' }, { component: 'PLATELETS', units: 1, lastUpdated: 'Just now' }]
    };

    const newBank: BloodBank = {
      ...bankData,
      id: `bb-${Date.now()}`,
      stock: defaultStock,
    };
    setBloodBanks(prev => [newBank, ...prev]);

    const user: User = {
      id: `user_bb_${Date.now()}`,
      fullName: bankData.name,
      email: `${bankData.name.toLowerCase().replace(/[^a-z]/g, '')}@bloodbank.org`,
      phone: bankData.emergencyHotline,
      role: 'HOSPITAL',
      organizationName: bankData.name,
    };
    setCurrentUser(user);
    addLog(`Blood Bank ${newBank.name} registered`, newBank.name, 'HOSPITAL');
    return true;
  };

  const createEmergencyRequest = (reqData: Omit<EmergencyRequest, 'id' | 'createdAt' | 'unitsFulfilled' | 'status'>) => {
    const newReq: EmergencyRequest = {
      ...reqData,
      id: `req-${Date.now().toString().slice(-4)}`,
      createdAt: 'Just now',
      unitsFulfilled: 0,
      status: 'ACTIVE',
    };
    setEmergencyRequests(prev => [newReq, ...prev]);
    addLog(`CRITICAL BROADCAST: ${newReq.unitsNeeded} units of ${newReq.bloodGroup} requested at ${newReq.hospitalName}`, newReq.contactPerson, 'HOSPITAL');
  };

  const fulfillRequest = (id: string, unitsAdded: number = 1) => {
    setEmergencyRequests(prev => prev.map(req => {
      if (req.id === id) {
        const nextFulfilled = Math.min(req.unitsNeeded, req.unitsFulfilled + unitsAdded);
        const nextStatus = nextFulfilled >= req.unitsNeeded ? 'FULFILLED' : 'ACTIVE';
        return {
          ...req,
          unitsFulfilled: nextFulfilled,
          status: nextStatus,
        };
      }
      return req;
    }));
    addLog(`Blood donation fulfilled for request ${id}`, 'Donor', 'DONOR');
  };

  const toggleDonorAvailability = (donorId: string) => {
    setDonors(prev => prev.map(d => {
      if (d.id === donorId) {
        const updated = !d.isAvailable;
        addLog(`Donor ${d.fullName} availability set to ${updated ? 'AVAILABLE' : 'OFFLINE'}`, d.fullName, 'DONOR');
        return { ...d, isAvailable: updated };
      }
      return d;
    }));
  };

  const updateBloodStock = (bankId: string, group: BloodGroup, component: BloodComponent, delta: number) => {
    setBloodBanks(prev => prev.map(bank => {
      if (bank.id === bankId) {
        const currentGroupStock = bank.stock[group] || [];
        const updatedGroupStock = currentGroupStock.map(item => {
          if (item.component === component) {
            return { ...item, units: Math.max(0, item.units + delta), lastUpdated: 'Just now' };
          }
          return item;
        });
        return {
          ...bank,
          stock: {
            ...bank.stock,
            [group]: updatedGroupStock,
          },
        };
      }
      return bank;
    }));
    addLog(`Stock updated for ${group} (${component}) delta: ${delta > 0 ? '+' : ''}${delta}`, 'Inventory Desk', 'HOSPITAL');
  };

  const verifyDonor = (donorId: string) => {
    setDonors(prev => prev.map(d => {
      if (d.id === donorId) {
        addLog(`Donor ${d.fullName} verified by Admin`, 'Admin', 'ADMIN');
        return { ...d, isVerified: true };
      }
      return d;
    }));
  };

  return (
    <AppContext.Provider
      value={{
        currentUser,
        donors,
        bloodBanks,
        emergencyRequests,
        activityLogs,
        login,
        logout,
        registerDonor,
        registerBloodBank,
        createEmergencyRequest,
        fulfillRequest,
        toggleDonorAvailability,
        updateBloodStock,
        verifyDonor,
      }}
    >
      {children}
    </AppContext.Provider>
  );
};

export const useApp = () => {
  const context = useContext(AppContext);
  if (!context) throw new Error('useApp must be used within AppProvider');
  return context;
};
