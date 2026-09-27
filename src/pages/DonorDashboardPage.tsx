import React, { useState } from 'react';
import { Heart, Activity, CheckCircle2, AlertTriangle, ShieldCheck, Phone, MapPin, Calendar, Clock, Bell } from 'lucide-react';
import { useApp } from '../context/AppContext';
import { COMPATIBILITY_RULES } from '../data/mockData';

export const DonorDashboardPage: React.FC = () => {
  const { currentUser, donors, emergencyRequests, toggleDonorAvailability, fulfillRequest } = useApp();

  // Find donor corresponding to current user
  const donor = donors.find(d => d.email.toLowerCase() === currentUser?.email.toLowerCase()) || donors[0];

  const [respondedReqs, setRespondedReqs] = useState<string[]>([]);

  // Find incoming requests matching this donor's blood group
  const compatibleRequests = emergencyRequests.filter(req => {
    if (req.status !== 'ACTIVE') return false;
    const canGiveList = COMPATIBILITY_RULES[donor.bloodGroup]?.canGiveTo || [];
    return canGiveList.includes(req.bloodGroup);
  });

  const handleRespond = (reqId: string) => {
    fulfillRequest(reqId, 1);
    setRespondedReqs(prev => [...prev, reqId]);
  };

  return (
    <div style={{ backgroundColor: '#F8FAFC', minHeight: '85vh', padding: '2.5rem 0 4rem' }}>
      <div className="container">
        {/* Welcome Header */}
        <div style={{ display: 'flex', flexWrap: 'wrap', justifyContent: 'space-between', alignItems: 'center', gap: '1rem', marginBottom: '2rem' }}>
          <div>
            <span style={{ fontSize: '0.8125rem', fontWeight: 700, color: '#C62828', textTransform: 'uppercase', letterSpacing: '0.05em' }}>
              Volunteer Donor Portal
            </span>
            <h1 style={{ fontSize: '2rem', color: '#0F172A', marginTop: '0.25rem' }}>
              Welcome back, {donor.fullName}
            </h1>
            <p style={{ color: '#64748B', fontSize: '0.9375rem' }}>
              Your blood donations have helped save estimated {donor.totalDonations * 3} lives.
            </p>
          </div>

          {/* Availability Toggle */}
          <div className="card" style={{ padding: '0.75rem 1.25rem', display: 'flex', alignItems: 'center', gap: '1rem' }}>
            <div>
              <span style={{ fontSize: '0.75rem', color: '#64748B', display: 'block' }}>Emergency Status</span>
              <strong style={{ fontSize: '0.9375rem', color: donor.isAvailable ? '#166534' : '#92400E' }}>
                {donor.isAvailable ? '🟢 Available for Calls' : '⏸️ Temporarily Offline'}
              </strong>
            </div>
            <button
              onClick={() => toggleDonorAvailability(donor.id)}
              className={donor.isAvailable ? 'btn btn-outline' : 'btn btn-primary'}
              style={{ padding: '0.4rem 0.85rem', fontSize: '0.8125rem' }}
            >
              {donor.isAvailable ? 'Go Offline' : 'Set Available'}
            </button>
          </div>
        </div>

        {/* Profile and Quick Metrics */}
        <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(220px, 1fr))', gap: '1.25rem', marginBottom: '2rem' }}>
          <div className="card" style={{ display: 'flex', alignItems: 'center', gap: '1rem' }}>
            <div style={{ width: '3.5rem', height: '3.5rem', borderRadius: '50%', backgroundColor: '#FFEBEE', color: '#C62828', display: 'flex', alignItems: 'center', justifyContent: 'center', fontSize: '1.5rem', fontWeight: 800 }}>
              {donor.bloodGroup}
            </div>
            <div>
              <span style={{ fontSize: '0.75rem', color: '#64748B' }}>Registered Group</span>
              <div style={{ fontSize: '1.125rem', fontWeight: 700, color: '#0F172A' }}>{donor.bloodGroup} Positive</div>
              {donor.isVerified && (
                <span style={{ fontSize: '0.6875rem', color: '#166534', fontWeight: 600, display: 'flex', alignItems: 'center', gap: '0.2rem' }}>
                  <ShieldCheck size={13} /> Verified Donor
                </span>
              )}
            </div>
          </div>

          <div className="card">
            <span style={{ fontSize: '0.75rem', color: '#64748B' }}>Total Donations</span>
            <div style={{ fontSize: '1.75rem', fontWeight: 800, color: '#0F172A' }}>{donor.totalDonations}</div>
            <span style={{ fontSize: '0.75rem', color: '#166534', fontWeight: 600 }}>Active donor certificate</span>
          </div>

          <div className="card">
            <span style={{ fontSize: '0.75rem', color: '#64748B' }}>Last Donated</span>
            <div style={{ fontSize: '1.25rem', fontWeight: 700, color: '#0F172A' }}>{donor.lastDonationDate || 'Available Now'}</div>
            <span style={{ fontSize: '0.75rem', color: '#166534', fontWeight: 600 }}>Eligible to donate today</span>
          </div>

          <div className="card">
            <span style={{ fontSize: '0.75rem', color: '#64748B' }}>Registered Location</span>
            <div style={{ fontSize: '1.125rem', fontWeight: 700, color: '#0F172A' }}>{donor.city}, {donor.state}</div>
            <span style={{ fontSize: '0.75rem', color: '#64748B' }}>Radius ~15 km alert</span>
          </div>
        </div>

        {/* Incoming Emergency Alerts for this Donor */}
        <div style={{ marginBottom: '2.5rem' }}>
          <div style={{ display: 'flex', alignItems: 'center', gap: '0.5rem', marginBottom: '1rem' }}>
            <Bell size={20} color="#DC2626" />
            <h2 style={{ fontSize: '1.375rem', color: '#0F172A' }}>
              Compatible Emergency Alerts ({compatibleRequests.length})
            </h2>
          </div>

          {compatibleRequests.length === 0 ? (
            <div className="card" style={{ textAlign: 'center', padding: '2.5rem 1rem' }}>
              <CheckCircle2 size={36} color="#166534" style={{ margin: '0 auto 0.75rem' }} />
              <h3 style={{ fontSize: '1.125rem', marginBottom: '0.25rem' }}>No Active Emergencies in Your Area</h3>
              <p style={{ color: '#64748B', fontSize: '0.875rem' }}>
                We will send an immediate notification whenever an urgent transfusion match is requested for your blood group.
              </p>
            </div>
          ) : (
            <div style={{ display: 'flex', flexDirection: 'column', gap: '1rem' }}>
              {compatibleRequests.map(req => {
                const hasResponded = respondedReqs.includes(req.id);

                return (
                  <div key={req.id} className="card" style={{ borderLeft: '4px solid #DC2626', padding: '1.25rem 1.5rem' }}>
                    <div style={{ display: 'flex', flexWrap: 'wrap', justifyContent: 'space-between', alignItems: 'flex-start', gap: '1rem' }}>
                      <div>
                        <div style={{ display: 'flex', alignItems: 'center', gap: '0.5rem', marginBottom: '0.375rem' }}>
                          <span style={{ fontSize: '1.25rem', fontWeight: 800, color: '#DC2626', backgroundColor: '#FEE2E2', padding: '0.15rem 0.5rem', borderRadius: '0.25rem' }}>
                            {req.bloodGroup}
                          </span>
                          <span className={`badge ${req.urgency === 'CRITICAL' ? 'badge-red' : 'badge-amber'}`}>
                            {req.urgency}
                          </span>
                          <span style={{ fontSize: '0.75rem', color: '#64748B' }}>Posted {req.createdAt}</span>
                        </div>

                        <h3 style={{ fontSize: '1.125rem', color: '#0F172A' }}>{req.patientName}</h3>
                        <p style={{ fontSize: '0.875rem', color: '#475569', marginBottom: '0.375rem' }}>
                          <strong>Hospital:</strong> {req.hospitalName} ({req.city})
                        </p>
                        <p style={{ fontSize: '0.875rem', color: '#64748B', fontStyle: 'italic' }}>
                          "{req.notes}"
                        </p>
                      </div>

                      <div style={{ display: 'flex', flexDirection: 'column', gap: '0.5rem', minWidth: '180px' }}>
                        <a
                          href={`tel:${req.contactPhone}`}
                          className="btn btn-outline"
                          style={{ padding: '0.5rem', fontSize: '0.8125rem', width: '100%' }}
                        >
                          <Phone size={14} color="#C62828" /> Call: {req.contactPhone}
                        </a>

                        {hasResponded ? (
                          <div style={{ textAlign: 'center', color: '#166534', fontWeight: 600, fontSize: '0.8125rem', padding: '0.4rem', backgroundColor: '#DCFCE7', borderRadius: '0.375rem' }}>
                            ✓ Response Logged!
                          </div>
                        ) : (
                          <button
                            onClick={() => handleRespond(req.id)}
                            className="btn btn-primary"
                            style={{ padding: '0.5rem', fontSize: '0.8125rem', width: '100%' }}
                          >
                            <Heart size={14} /> I Can Donate
                          </button>
                        )}
                      </div>
                    </div>
                  </div>
                );
              })}
            </div>
          )}
        </div>

        {/* Donation History Table */}
        <div className="card">
          <h2 style={{ fontSize: '1.25rem', marginBottom: '1rem', color: '#0F172A' }}>Donation Record History</h2>
          <div style={{ overflowX: 'auto' }}>
            <table style={{ width: '100%', borderCollapse: 'collapse', textAlign: 'left', fontSize: '0.875rem' }}>
              <thead>
                <tr style={{ borderBottom: '1px solid #E2E8F0', color: '#64748B', fontWeight: 600 }}>
                  <th style={{ padding: '0.75rem 0.5rem' }}>Date</th>
                  <th style={{ padding: '0.75rem 0.5rem' }}>Location</th>
                  <th style={{ padding: '0.75rem 0.5rem' }}>Units</th>
                  <th style={{ padding: '0.75rem 0.5rem' }}>Component</th>
                  <th style={{ padding: '0.75rem 0.5rem' }}>Status</th>
                </tr>
              </thead>
              <tbody>
                <tr style={{ borderBottom: '1px solid #F1F5F9' }}>
                  <td style={{ padding: '0.75rem 0.5rem' }}>May 14, 2026</td>
                  <td style={{ padding: '0.75rem 0.5rem' }}>Metropolitan Red Cross Blood Institute</td>
                  <td style={{ padding: '0.75rem 0.5rem' }}>1 Pint (450ml)</td>
                  <td style={{ padding: '0.75rem 0.5rem' }}>Whole Blood</td>
                  <td style={{ padding: '0.75rem 0.5rem' }}><span className="badge badge-green">Completed</span></td>
                </tr>
                <tr style={{ borderBottom: '1px solid #F1F5F9' }}>
                  <td style={{ padding: '0.75rem 0.5rem' }}>Jan 10, 2026</td>
                  <td style={{ padding: '0.75rem 0.5rem' }}>St. Jude University Trauma Blood Center</td>
                  <td style={{ padding: '0.75rem 0.5rem' }}>1 Pint (450ml)</td>
                  <td style={{ padding: '0.75rem 0.5rem' }}>Whole Blood</td>
                  <td style={{ padding: '0.75rem 0.5rem' }}><span className="badge badge-green">Completed</span></td>
                </tr>
                <tr>
                  <td style={{ padding: '0.75rem 0.5rem' }}>Sep 18, 2025</td>
                  <td style={{ padding: '0.75rem 0.5rem' }}>Metropolitan Red Cross Blood Institute</td>
                  <td style={{ padding: '0.75rem 0.5rem' }}>1 Unit Apheresis</td>
                  <td style={{ padding: '0.75rem 0.5rem' }}>Platelets</td>
                  <td style={{ padding: '0.75rem 0.5rem' }}><span className="badge badge-green">Completed</span></td>
                </tr>
              </tbody>
            </table>
          </div>
        </div>
      </div>
    </div>
  );
};
