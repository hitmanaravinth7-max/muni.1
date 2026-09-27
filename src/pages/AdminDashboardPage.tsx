import React from 'react';
import { Shield, Users, Building, Droplets, CheckCircle, Clock, ShieldCheck, Activity } from 'lucide-react';
import { useApp } from '../context/AppContext';

export const AdminDashboardPage: React.FC = () => {
  const { donors, bloodBanks, emergencyRequests, activityLogs, verifyDonor, fulfillRequest } = useApp();

  const unverifiedDonors = donors.filter(d => !d.isVerified);
  const totalDonors = donors.length;
  const activeEmergencies = emergencyRequests.filter(r => r.status === 'ACTIVE').length;

  return (
    <div style={{ backgroundColor: '#F8FAFC', minHeight: '85vh', padding: '2.5rem 0 4rem' }}>
      <div className="container">
        {/* Header */}
        <div style={{ marginBottom: '2rem' }}>
          <span style={{ fontSize: '0.8125rem', fontWeight: 700, color: '#991B1B', textTransform: 'uppercase', letterSpacing: '0.05em' }}>
            System Administration
          </span>
          <h1 style={{ fontSize: '2rem', color: '#0F172A', marginTop: '0.25rem' }}>
            National BloodBridge Control Center
          </h1>
          <p style={{ color: '#64748B', fontSize: '0.9375rem' }}>
            Platform governance, donor credential audits, hospital certification, and system security telemetry.
          </p>
        </div>

        {/* Quick Metrics */}
        <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(200px, 1fr))', gap: '1.25rem', marginBottom: '2.5rem' }}>
          <div className="card">
            <span style={{ fontSize: '0.75rem', color: '#64748B' }}>Total Registered Donors</span>
            <div style={{ fontSize: '1.875rem', fontWeight: 800, color: '#0F172A' }}>{totalDonors}</div>
            <span style={{ fontSize: '0.75rem', color: '#166534', fontWeight: 600 }}>{unverifiedDonors.length} awaiting verification</span>
          </div>

          <div className="card">
            <span style={{ fontSize: '0.75rem', color: '#64748B' }}>Registered Blood Centers</span>
            <div style={{ fontSize: '1.875rem', fontWeight: 800, color: '#0F172A' }}>{bloodBanks.length}</div>
            <span style={{ fontSize: '0.75rem', color: '#166534', fontWeight: 600 }}>All FDA certified</span>
          </div>

          <div className="card">
            <span style={{ fontSize: '0.75rem', color: '#64748B' }}>Active Emergencies</span>
            <div style={{ fontSize: '1.875rem', fontWeight: 800, color: '#DC2626' }}>{activeEmergencies}</div>
            <span style={{ fontSize: '0.75rem', color: '#DC2626', fontWeight: 600 }}>Immediate attention required</span>
          </div>

          <div className="card">
            <span style={{ fontSize: '0.75rem', color: '#64748B' }}>System Uptime</span>
            <div style={{ fontSize: '1.875rem', fontWeight: 800, color: '#166534' }}>99.98%</div>
            <span style={{ fontSize: '0.75rem', color: '#64748B' }}>Encrypted & Audited</span>
          </div>
        </div>

        <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(340px, 1fr))', gap: '2rem' }}>
          {/* Donor Verification Queue */}
          <div className="card">
            <h2 style={{ fontSize: '1.25rem', color: '#0F172A', marginBottom: '0.5rem', display: 'flex', alignItems: 'center', gap: '0.5rem' }}>
              <ShieldCheck size={20} color="#166534" />
              Donor Verification Queue ({unverifiedDonors.length})
            </h2>
            <p style={{ color: '#64748B', fontSize: '0.875rem', marginBottom: '1.25rem' }}>
              Review volunteer credentials to award the verified healthcare donor badge.
            </p>

            {unverifiedDonors.length === 0 ? (
              <div style={{ textAlign: 'center', padding: '2rem 1rem', color: '#166534' }}>
                <CheckCircle size={32} style={{ margin: '0 auto 0.5rem' }} />
                <p style={{ fontSize: '0.875rem', fontWeight: 600 }}>All donors currently verified!</p>
              </div>
            ) : (
              <div style={{ display: 'flex', flexDirection: 'column', gap: '0.75rem' }}>
                {unverifiedDonors.map(d => (
                  <div key={d.id} style={{ border: '1px solid #E2E8F0', borderRadius: '0.5rem', padding: '0.875rem', display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
                    <div>
                      <div style={{ fontWeight: 700, color: '#0F172A', fontSize: '0.9375rem' }}>
                        {d.fullName} ({d.bloodGroup})
                      </div>
                      <div style={{ fontSize: '0.75rem', color: '#64748B' }}>
                        {d.city}, {d.state} • {d.phone}
                      </div>
                    </div>
                    <button
                      onClick={() => verifyDonor(d.id)}
                      className="btn btn-outline"
                      style={{ padding: '0.35rem 0.75rem', fontSize: '0.75rem', borderColor: '#166534', color: '#166534' }}
                    >
                      Verify Badge
                    </button>
                  </div>
                ))}
              </div>
            )}
          </div>

          {/* Real-time System Audit Activity Log */}
          <div className="card">
            <h2 style={{ fontSize: '1.25rem', color: '#0F172A', marginBottom: '0.5rem', display: 'flex', alignItems: 'center', gap: '0.5rem' }}>
              <Activity size={20} color="#C62828" />
              Live Security & Activity Telemetry
            </h2>
            <p style={{ color: '#64748B', fontSize: '0.875rem', marginBottom: '1.25rem' }}>
              Chronological log of logins, inventory updates, and emergency broadcasts.
            </p>

            <div style={{ display: 'flex', flexDirection: 'column', gap: '0.75rem', maxHeight: '360px', overflowY: 'auto' }}>
              {activityLogs.map(log => (
                <div key={log.id} style={{ display: 'flex', alignItems: 'flex-start', gap: '0.75rem', fontSize: '0.8125rem', borderBottom: '1px solid #F1F5F9', paddingBottom: '0.5rem' }}>
                  <span className={`badge ${log.type === 'ADMIN' ? 'badge-blue' : log.type === 'HOSPITAL' ? 'badge-amber' : log.type === 'DONOR' ? 'badge-green' : 'badge-red'}`} style={{ fontSize: '0.6875rem' }}>
                    {log.type}
                  </span>
                  <div style={{ flex: 1 }}>
                    <div style={{ color: '#1E293B', fontWeight: 500 }}>{log.action}</div>
                    <div style={{ color: '#94A3B8', fontSize: '0.75rem' }}>By: {log.actor} • {log.timestamp}</div>
                  </div>
                </div>
              ))}
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};
