import React, { useState } from 'react';
import { Link } from 'react-router-dom';
import { Building2, Plus, Minus, AlertTriangle, Droplet, CheckCircle2, Clock, Phone, MapPin } from 'lucide-react';
import { useApp } from '../context/AppContext';
import { BloodGroup, BloodComponent } from '../types';

export const HospitalDashboardPage: React.FC = () => {
  const { bloodBanks, emergencyRequests, updateBloodStock, fulfillRequest } = useApp();
  const bank = bloodBanks[0]; // Active hospital / blood bank

  const [selectedGroup, setSelectedGroup] = useState<BloodGroup>('O-');
  const [selectedComponent, setSelectedComponent] = useState<BloodComponent>('WHOLE_BLOOD');

  const bloodGroups: BloodGroup[] = ['O-', 'O+', 'A-', 'A+', 'B-', 'B+', 'AB-', 'AB+'];
  const components: BloodComponent[] = ['WHOLE_BLOOD', 'RBC', 'PLASMA', 'PLATELETS'];

  return (
    <div style={{ backgroundColor: '#F8FAFC', minHeight: '85vh', padding: '2.5rem 0 4rem' }}>
      <div className="container">
        {/* Header */}
        <div style={{ display: 'flex', flexWrap: 'wrap', justifyContent: 'space-between', alignItems: 'center', gap: '1rem', marginBottom: '2rem' }}>
          <div>
            <span style={{ fontSize: '0.8125rem', fontWeight: 700, color: '#1E40AF', textTransform: 'uppercase', letterSpacing: '0.05em' }}>
              Hospital & Blood Bank Coordinator Portal
            </span>
            <h1 style={{ fontSize: '2rem', color: '#0F172A', marginTop: '0.25rem' }}>
              {bank.name}
            </h1>
            <p style={{ color: '#64748B', fontSize: '0.9375rem' }}>
              License: {bank.licenseNumber} • Direct Hotline: {bank.emergencyHotline}
            </p>
          </div>

          <Link to="/emergency-request" className="btn btn-danger" style={{ padding: '0.625rem 1.25rem' }}>
            <AlertTriangle size={17} />
            + New Emergency Broadcast
          </Link>
        </div>

        {/* Inventory Quick Controls Card */}
        <div className="card" style={{ marginBottom: '2.5rem', padding: '1.75rem' }}>
          <h2 style={{ fontSize: '1.25rem', marginBottom: '0.5rem', color: '#0F172A', display: 'flex', alignItems: 'center', gap: '0.5rem' }}>
            <Droplet size={18} color="#C62828" />
            Live Blood Inventory Management
          </h2>
          <p style={{ color: '#64748B', fontSize: '0.875rem', marginBottom: '1.5rem' }}>
            Update on-hand reserve units. Numbers synchronize instantly with public emergency search.
          </p>

          <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(240px, 1fr))', gap: '1.25rem' }}>
            {bloodGroups.map(bg => {
              const stockList = bank.stock[bg] || [];
              const totalUnits = stockList.reduce((acc, curr) => acc + curr.units, 0);

              return (
                <div key={bg} style={{ border: '1px solid #E2E8F0', borderRadius: '0.5rem', padding: '1rem', backgroundColor: '#FFFFFF' }}>
                  <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '0.75rem' }}>
                    <span style={{ fontSize: '1.375rem', fontWeight: 800, color: '#C62828' }}>{bg}</span>
                    <span style={{ fontSize: '0.875rem', fontWeight: 700, color: totalUnits < 5 ? '#B91C1C' : '#166534' }}>
                      {totalUnits} Units Total
                    </span>
                  </div>

                  <div style={{ display: 'flex', flexDirection: 'column', gap: '0.5rem' }}>
                    {stockList.map(item => (
                      <div key={item.component} style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', fontSize: '0.8125rem' }}>
                        <span style={{ color: '#475569' }}>{item.component.replace('_', ' ')}</span>
                        <div style={{ display: 'flex', alignItems: 'center', gap: '0.5rem' }}>
                          <button
                            onClick={() => updateBloodStock(bank.id, bg, item.component, -1)}
                            style={{ width: '1.5rem', height: '1.5rem', borderRadius: '0.25rem', border: '1px solid #CBD5E1', background: '#F1F5F9', cursor: 'pointer', display: 'flex', alignItems: 'center', justifyContent: 'center' }}
                            title="Decrement unit"
                          >
                            <Minus size={12} />
                          </button>
                          <strong style={{ minWidth: '1.5rem', textAlign: 'center' }}>{item.units}</strong>
                          <button
                            onClick={() => updateBloodStock(bank.id, bg, item.component, 1)}
                            style={{ width: '1.5rem', height: '1.5rem', borderRadius: '0.25rem', border: '1px solid #CBD5E1', background: '#F1F5F9', cursor: 'pointer', display: 'flex', alignItems: 'center', justifyContent: 'center' }}
                            title="Increment unit"
                          >
                            <Plus size={12} />
                          </button>
                        </div>
                      </div>
                    ))}
                  </div>
                </div>
              );
            })}
          </div>
        </div>

        {/* Active Requests Tracker */}
        <div className="card">
          <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '1.25rem' }}>
            <h2 style={{ fontSize: '1.25rem', color: '#0F172A' }}>Active Facility Emergency Requests</h2>
            <span style={{ fontSize: '0.875rem', color: '#64748B' }}>Real-time triage</span>
          </div>

          <div style={{ display: 'flex', flexDirection: 'column', gap: '1rem' }}>
            {emergencyRequests.map(req => (
              <div key={req.id} style={{ border: '1px solid #E2E8F0', borderRadius: '0.5rem', padding: '1rem 1.25rem', display: 'flex', flexWrap: 'wrap', justifyContent: 'space-between', alignItems: 'center', gap: '1rem' }}>
                <div>
                  <div style={{ display: 'flex', alignItems: 'center', gap: '0.5rem', marginBottom: '0.25rem' }}>
                    <span style={{ fontSize: '1.125rem', fontWeight: 800, color: '#C62828' }}>{req.bloodGroup}</span>
                    <span className={`badge ${req.status === 'ACTIVE' ? 'badge-red' : 'badge-green'}`}>
                      {req.status}
                    </span>
                    <span style={{ fontSize: '0.8125rem', color: '#64748B' }}>Needed: {req.unitsNeeded} units</span>
                  </div>
                  <h3 style={{ fontSize: '1rem', color: '#0F172A', marginBottom: '0.25rem' }}>{req.patientName}</h3>
                  <p style={{ fontSize: '0.8125rem', color: '#64748B' }}>
                    Hospital: {req.hospitalName} • Phone: {req.contactPhone}
                  </p>
                </div>

                <div style={{ display: 'flex', alignItems: 'center', gap: '0.75rem' }}>
                  <div style={{ textAlign: 'right' }}>
                    <span style={{ fontSize: '0.75rem', color: '#64748B', display: 'block' }}>Fulfilled</span>
                    <strong style={{ fontSize: '1rem', color: '#166534' }}>{req.unitsFulfilled} / {req.unitsNeeded} units</strong>
                  </div>
                  {req.status === 'ACTIVE' && (
                    <button
                      onClick={() => fulfillRequest(req.id, 1)}
                      className="btn btn-primary"
                      style={{ padding: '0.4rem 0.85rem', fontSize: '0.8125rem' }}
                    >
                      + Log Donor Transfusion
                    </button>
                  )}
                </div>
              </div>
            ))}
          </div>
        </div>
      </div>
    </div>
  );
};
