import React, { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { AlertTriangle, ShieldAlert, Heart, Building, Phone, Calendar, Clock, CheckCircle2 } from 'lucide-react';
import { useApp } from '../context/AppContext';
import { BloodGroup, BloodComponent, UrgencyLevel } from '../types';

export const EmergencyRequestPage: React.FC = () => {
  const { createEmergencyRequest } = useApp();
  const navigate = useNavigate();

  const [patientName, setPatientName] = useState('');
  const [hospitalName, setHospitalName] = useState('');
  const [bloodGroup, setBloodGroup] = useState<BloodGroup>('O-');
  const [unitsNeeded, setUnitsNeeded] = useState(2);
  const [component, setComponent] = useState<BloodComponent>('WHOLE_BLOOD');
  const [urgency, setUrgency] = useState<UrgencyLevel>('CRITICAL');
  const [contactPerson, setContactPerson] = useState('');
  const [contactPhone, setContactPhone] = useState('');
  const [city, setCity] = useState('');
  const [requiredDate, setRequiredDate] = useState('IMMEDIATE (< 2 Hours)');
  const [notes, setNotes] = useState('');
  const [submitted, setSubmitted] = useState(false);

  const bloodGroups: BloodGroup[] = ['O-', 'O+', 'A-', 'A+', 'B-', 'B+', 'AB-', 'AB+'];
  const components: BloodComponent[] = ['WHOLE_BLOOD', 'RBC', 'PLASMA', 'PLATELETS'];

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();

    createEmergencyRequest({
      patientName,
      hospitalName,
      bloodGroup,
      unitsNeeded,
      component,
      urgency,
      contactPerson,
      contactPhone,
      city,
      requiredDate,
      notes,
    });

    setSubmitted(true);
  };

  return (
    <div style={{ backgroundColor: '#F8FAFC', minHeight: '85vh', padding: '3rem 1rem' }}>
      <div style={{ maxWidth: '680px', width: '100%', margin: '0 auto' }}>
        <div className="card" style={{ padding: '2.5rem 2rem', boxShadow: '0 10px 25px -5px rgba(0,0,0,0.08)', borderTop: '5px solid #DC2626' }}>
          {/* Header */}
          <div style={{ display: 'flex', alignItems: 'center', gap: '0.875rem', marginBottom: '1.5rem' }}>
            <div style={{ width: '3rem', height: '3rem', borderRadius: '0.625rem', backgroundColor: '#FEE2E2', color: '#DC2626', display: 'flex', alignItems: 'center', justifyContent: 'center' }}>
              <AlertTriangle size={24} />
            </div>
            <div>
              <h1 style={{ fontSize: '1.625rem', color: '#0F172A', fontWeight: 800 }}>Broadcast Emergency Blood Request</h1>
              <p style={{ color: '#64748B', fontSize: '0.875rem' }}>
                Broadcast real-time alerts across our network of registered donors and blood centers.
              </p>
            </div>
          </div>

          {/* Urgent warning */}
          <div style={{ backgroundColor: '#FEF2F2', border: '1px solid #FECACA', borderRadius: '0.5rem', padding: '0.875rem', marginBottom: '1.75rem', fontSize: '0.8125rem', color: '#991B1B', display: 'flex', alignItems: 'center', gap: '0.5rem' }}>
            <ShieldAlert size={18} style={{ flexShrink: 0 }} />
            <span>Verify patient details and room coordinates accurately. Attending physicians and coordinators will be connected immediately.</span>
          </div>

          {submitted ? (
            <div style={{ textAlign: 'center', padding: '2.5rem 1rem' }}>
              <div style={{ width: '4rem', height: '4rem', borderRadius: '50%', backgroundColor: '#DCFCE7', color: '#166534', display: 'flex', alignItems: 'center', justifyContent: 'center', margin: '0 auto 1.25rem' }}>
                <CheckCircle2 size={32} />
              </div>
              <h2 style={{ fontSize: '1.5rem', marginBottom: '0.5rem', color: '#0F172A' }}>Emergency Alert Dispatched!</h2>
              <p style={{ color: '#64748B', maxWidth: '440px', margin: '0 auto 2rem', fontSize: '0.9375rem' }}>
                Your request for <strong>{unitsNeeded} units of {bloodGroup}</strong> at {hospitalName} has been broadcasted to all matching local donors.
              </p>
              <div style={{ display: 'flex', justifyContent: 'center', gap: '1rem' }}>
                <Link to="/search-donors" className="btn btn-primary">
                  View Matching Donors
                </Link>
                <Link to="/" className="btn btn-outline">
                  Return to Home
                </Link>
              </div>
            </div>
          ) : (
            <form onSubmit={handleSubmit}>
              <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '1rem', marginBottom: '1.25rem' }}>
                <div>
                  <label style={{ display: 'block', fontSize: '0.875rem', fontWeight: 600, color: '#334155', marginBottom: '0.375rem' }}>
                    Required Blood Group
                  </label>
                  <select
                    value={bloodGroup}
                    onChange={e => setBloodGroup(e.target.value as BloodGroup)}
                    style={{ width: '100%', padding: '0.625rem 0.75rem', borderRadius: '0.5rem', border: '1px solid #CBD5E1', fontSize: '0.9375rem', backgroundColor: '#FFFFFF', fontWeight: 700, color: '#C62828' }}
                  >
                    {bloodGroups.map(bg => (
                      <option key={bg} value={bg}>{bg}</option>
                    ))}
                  </select>
                </div>

                <div>
                  <label style={{ display: 'block', fontSize: '0.875rem', fontWeight: 600, color: '#334155', marginBottom: '0.375rem' }}>
                    Units Needed
                  </label>
                  <input
                    type="number"
                    min="1"
                    max="10"
                    required
                    value={unitsNeeded}
                    onChange={e => setUnitsNeeded(parseInt(e.target.value) || 1)}
                    style={{ width: '100%', padding: '0.625rem 0.75rem', borderRadius: '0.5rem', border: '1px solid #CBD5E1', fontSize: '0.9375rem' }}
                  />
                </div>
              </div>

              <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '1rem', marginBottom: '1.25rem' }}>
                <div>
                  <label style={{ display: 'block', fontSize: '0.875rem', fontWeight: 600, color: '#334155', marginBottom: '0.375rem' }}>
                    Component Type
                  </label>
                  <select
                    value={component}
                    onChange={e => setComponent(e.target.value as BloodComponent)}
                    style={{ width: '100%', padding: '0.625rem 0.75rem', borderRadius: '0.5rem', border: '1px solid #CBD5E1', fontSize: '0.9375rem', backgroundColor: '#FFFFFF' }}
                  >
                    {components.map(c => (
                      <option key={c} value={c}>{c.replace('_', ' ')}</option>
                    ))}
                  </select>
                </div>

                <div>
                  <label style={{ display: 'block', fontSize: '0.875rem', fontWeight: 600, color: '#334155', marginBottom: '0.375rem' }}>
                    Urgency Level
                  </label>
                  <select
                    value={urgency}
                    onChange={e => setUrgency(e.target.value as UrgencyLevel)}
                    style={{ width: '100%', padding: '0.625rem 0.75rem', borderRadius: '0.5rem', border: '1px solid #CBD5E1', fontSize: '0.9375rem', backgroundColor: '#FFFFFF', fontWeight: 600, color: urgency === 'CRITICAL' ? '#DC2626' : '#B45309' }}
                  >
                    <option value="CRITICAL">CRITICAL (Immediate Transfusion)</option>
                    <option value="URGENT">URGENT (Within 4-6 Hours)</option>
                    <option value="NORMAL">NORMAL (Scheduled Surgery)</option>
                  </select>
                </div>
              </div>

              <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '1rem', marginBottom: '1.25rem' }}>
                <div>
                  <label style={{ display: 'block', fontSize: '0.875rem', fontWeight: 600, color: '#334155', marginBottom: '0.375rem' }}>
                    Patient Name / Room Identifier
                  </label>
                  <input
                    type="text"
                    required
                    placeholder="e.g. John Doe (ICU Bed 2)"
                    value={patientName}
                    onChange={e => setPatientName(e.target.value)}
                    style={{ width: '100%', padding: '0.625rem 0.75rem', borderRadius: '0.5rem', border: '1px solid #CBD5E1', fontSize: '0.9375rem' }}
                  />
                </div>

                <div>
                  <label style={{ display: 'block', fontSize: '0.875rem', fontWeight: 600, color: '#334155', marginBottom: '0.375rem' }}>
                    Hospital / Trauma Facility Name
                  </label>
                  <input
                    type="text"
                    required
                    placeholder="e.g. Metropolitan General Trauma"
                    value={hospitalName}
                    onChange={e => setHospitalName(e.target.value)}
                    style={{ width: '100%', padding: '0.625rem 0.75rem', borderRadius: '0.5rem', border: '1px solid #CBD5E1', fontSize: '0.9375rem' }}
                  />
                </div>
              </div>

              <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '1rem', marginBottom: '1.25rem' }}>
                <div>
                  <label style={{ display: 'block', fontSize: '0.875rem', fontWeight: 600, color: '#334155', marginBottom: '0.375rem' }}>
                    Contact Person / Physician
                  </label>
                  <input
                    type="text"
                    required
                    placeholder="Dr. Smith / Nurse Supervisor"
                    value={contactPerson}
                    onChange={e => setContactPerson(e.target.value)}
                    style={{ width: '100%', padding: '0.625rem 0.75rem', borderRadius: '0.5rem', border: '1px solid #CBD5E1', fontSize: '0.9375rem' }}
                  />
                </div>

                <div>
                  <label style={{ display: 'block', fontSize: '0.875rem', fontWeight: 600, color: '#334155', marginBottom: '0.375rem' }}>
                    Contact Phone (Direct Hotline)
                  </label>
                  <input
                    type="tel"
                    required
                    placeholder="+1 (555) 911-0000"
                    value={contactPhone}
                    onChange={e => setContactPhone(e.target.value)}
                    style={{ width: '100%', padding: '0.625rem 0.75rem', borderRadius: '0.5rem', border: '1px solid #CBD5E1', fontSize: '0.9375rem' }}
                  />
                </div>
              </div>

              <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '1rem', marginBottom: '1.25rem' }}>
                <div>
                  <label style={{ display: 'block', fontSize: '0.875rem', fontWeight: 600, color: '#334155', marginBottom: '0.375rem' }}>
                    City / District
                  </label>
                  <input
                    type="text"
                    required
                    placeholder="Downtown Metro"
                    value={city}
                    onChange={e => setCity(e.target.value)}
                    style={{ width: '100%', padding: '0.625rem 0.75rem', borderRadius: '0.5rem', border: '1px solid #CBD5E1', fontSize: '0.9375rem' }}
                  />
                </div>

                <div>
                  <label style={{ display: 'block', fontSize: '0.875rem', fontWeight: 600, color: '#334155', marginBottom: '0.375rem' }}>
                    Timeline
                  </label>
                  <input
                    type="text"
                    required
                    placeholder="IMMEDIATE (< 2 Hours)"
                    value={requiredDate}
                    onChange={e => setRequiredDate(e.target.value)}
                    style={{ width: '100%', padding: '0.625rem 0.75rem', borderRadius: '0.5rem', border: '1px solid #CBD5E1', fontSize: '0.9375rem' }}
                  />
                </div>
              </div>

              <div style={{ marginBottom: '1.75rem' }}>
                <label style={{ display: 'block', fontSize: '0.875rem', fontWeight: 600, color: '#334155', marginBottom: '0.375rem' }}>
                  Clinical Case Notes / Specific Instructions
                </label>
                <textarea
                  rows={3}
                  placeholder="Detail the case severity, blood bank drop-off location, or special donor conditions..."
                  value={notes}
                  onChange={e => setNotes(e.target.value)}
                  style={{ width: '100%', padding: '0.625rem 0.75rem', borderRadius: '0.5rem', border: '1px solid #CBD5E1', fontSize: '0.9375rem', resize: 'vertical' }}
                />
              </div>

              <div style={{ display: 'flex', gap: '1rem' }}>
                <button type="button" onClick={() => navigate(-1)} className="btn btn-outline" style={{ flex: 1 }}>
                  Cancel
                </button>
                <button type="submit" className="btn btn-danger" style={{ flex: 2, padding: '0.75rem' }}>
                  🚨 Broadcast Emergency Request
                </button>
              </div>
            </form>
          )}
        </div>
      </div>
    </div>
  );
};
