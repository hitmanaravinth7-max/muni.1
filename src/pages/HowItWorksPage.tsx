import React, { useState } from 'react';
import { Link } from 'react-router-dom';
import { CheckCircle2, AlertCircle, Heart, Droplet, UserCheck, Shield, HelpCircle, ArrowRight } from 'lucide-react';
import { COMPATIBILITY_RULES } from '../data/mockData';
import { BloodGroup } from '../types';

export const HowItWorksPage: React.FC = () => {
  const [selectedGroup, setSelectedGroup] = useState<BloodGroup>('O-');

  const bloodGroups: BloodGroup[] = ['O-', 'O+', 'A-', 'A+', 'B-', 'B+', 'AB-', 'AB+'];

  return (
    <div style={{ backgroundColor: '#F8FAFC', minHeight: '80vh', padding: '3rem 0 5rem' }}>
      <div className="container">
        {/* Title */}
        <div style={{ textAlign: 'center', maxWidth: '760px', margin: '0 auto 3.5rem' }}>
          <span style={{ fontSize: '0.8125rem', fontWeight: 700, color: '#C62828', textTransform: 'uppercase', letterSpacing: '0.075em' }}>
            Medical Guidelines & Architecture
          </span>
          <h1 style={{ fontSize: 'clamp(2rem, 4vw, 2.75rem)', marginTop: '0.5rem', marginBottom: '1rem', color: '#0F172A' }}>
            How BloodBridge Coordinates Emergency Transfusions
          </h1>
          <p style={{ color: '#64748B', fontSize: '1.0625rem', lineHeight: 1.6 }}>
            Understanding blood compatibility, donation requirements, and the digital triage pipeline that connects donors with patients in critical condition.
          </p>
        </div>

        {/* Interactive Compatibility Matrix */}
        <div className="card" style={{ marginBottom: '3.5rem', padding: '2rem' }}>
          <div style={{ textAlign: 'center', marginBottom: '2rem' }}>
            <h2 style={{ fontSize: '1.75rem', marginBottom: '0.5rem' }}>Interactive Blood Compatibility Matrix</h2>
            <p style={{ color: '#64748B', fontSize: '0.9375rem' }}>
              Click any blood group to immediately test recipient-donor compatibility rules:
            </p>
          </div>

          {/* Group selector tabs */}
          <div style={{ display: 'flex', flexWrap: 'wrap', justifyContent: 'center', gap: '0.5rem', marginBottom: '2rem' }}>
            {bloodGroups.map(bg => (
              <button
                key={bg}
                onClick={() => setSelectedGroup(bg)}
                style={{
                  padding: '0.625rem 1.25rem',
                  borderRadius: '0.5rem',
                  border: '1.5px solid',
                  borderColor: selectedGroup === bg ? '#C62828' : '#CBD5E1',
                  backgroundColor: selectedGroup === bg ? '#C62828' : '#FFFFFF',
                  color: selectedGroup === bg ? '#FFFFFF' : '#1E293B',
                  fontWeight: 700,
                  fontSize: '1rem',
                  cursor: 'pointer',
                  transition: 'all 0.15s ease',
                }}
              >
                {bg}
              </button>
            ))}
          </div>

          {/* Details for selected group */}
          <div style={{ backgroundColor: '#F8FAFC', borderRadius: '0.75rem', padding: '1.75rem', border: '1px solid #E2E8F0', maxWidth: '800px', margin: '0 auto' }}>
            <div style={{ display: 'flex', alignItems: 'center', gap: '1rem', marginBottom: '1.5rem', borderBottom: '1px solid #E2E8F0', paddingBottom: '1rem' }}>
              <div style={{ width: '4rem', height: '4rem', borderRadius: '50%', backgroundColor: '#FFEBEE', color: '#C62828', display: 'flex', alignItems: 'center', justifyContent: 'center', fontSize: '1.75rem', fontWeight: 800 }}>
                {selectedGroup}
              </div>
              <div>
                <h3 style={{ fontSize: '1.375rem', color: '#0F172A' }}>Blood Group {selectedGroup} Profile</h3>
                {selectedGroup === 'O-' && (
                  <span className="badge badge-red" style={{ marginTop: '0.25rem' }}>
                    Universal Red Cell Donor (Can be given to anyone)
                  </span>
                )}
                {selectedGroup === 'AB+' && (
                  <span className="badge badge-blue" style={{ marginTop: '0.25rem' }}>
                    Universal Red Cell Recipient (Can receive from anyone)
                  </span>
                )}
              </div>
            </div>

            <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(280px, 1fr))', gap: '1.5rem' }}>
              {/* Can Give To */}
              <div>
                <h4 style={{ fontSize: '0.9375rem', fontWeight: 700, color: '#166534', marginBottom: '0.75rem', display: 'flex', alignItems: 'center', gap: '0.375rem' }}>
                  <CheckCircle2 size={18} /> Can Donate Red Blood Cells To:
                </h4>
                <div style={{ display: 'flex', flexWrap: 'wrap', gap: '0.5rem' }}>
                  {COMPATIBILITY_RULES[selectedGroup].canGiveTo.map(g => (
                    <span key={g} style={{ backgroundColor: '#DCFCE7', color: '#166534', padding: '0.35rem 0.75rem', borderRadius: '0.375rem', fontWeight: 700, fontSize: '0.875rem' }}>
                      {g}
                    </span>
                  ))}
                </div>
              </div>

              {/* Can Receive From */}
              <div>
                <h4 style={{ fontSize: '0.9375rem', fontWeight: 700, color: '#1E40AF', marginBottom: '0.75rem', display: 'flex', alignItems: 'center', gap: '0.375rem' }}>
                  <Droplet size={18} /> Can Receive Transfusions From:
                </h4>
                <div style={{ display: 'flex', flexWrap: 'wrap', gap: '0.5rem' }}>
                  {COMPATIBILITY_RULES[selectedGroup].canReceiveFrom.map(g => (
                    <span key={g} style={{ backgroundColor: '#DBEAFE', color: '#1E40AF', padding: '0.35rem 0.75rem', borderRadius: '0.375rem', fontWeight: 700, fontSize: '0.875rem' }}>
                      {g}
                    </span>
                  ))}
                </div>
              </div>
            </div>
          </div>
        </div>

        {/* Eligibility Criteria */}
        <div style={{ marginBottom: '3.5rem' }}>
          <div style={{ textAlign: 'center', maxWidth: '640px', margin: '0 auto 2.5rem' }}>
            <h2 style={{ fontSize: '2rem', marginBottom: '0.5rem' }}>Donor Eligibility Criteria</h2>
            <p style={{ color: '#64748B', fontSize: '1rem' }}>General medical guidelines established by blood transfusion authorities:</p>
          </div>

          <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(280px, 1fr))', gap: '1.5rem' }}>
            <div className="card">
              <div style={{ display: 'flex', alignItems: 'center', gap: '0.75rem', marginBottom: '0.75rem' }}>
                <div style={{ width: '2.5rem', height: '2.5rem', borderRadius: '0.5rem', backgroundColor: '#EFF6FF', color: '#1E40AF', display: 'flex', alignItems: 'center', justifyContent: 'center' }}>
                  <UserCheck size={20} />
                </div>
                <h3 style={{ fontSize: '1.125rem' }}>Age & Weight Requirements</h3>
              </div>
              <ul style={{ paddingLeft: '1.25rem', color: '#64748B', fontSize: '0.9375rem', lineHeight: '1.7' }}>
                <li>Must be between <strong>18 and 65 years old</strong> (16-17 with parental consent in some regions).</li>
                <li>Weigh at least <strong>50 kg (110 lbs)</strong>.</li>
                <li>Good general health feeling well on donation day.</li>
              </ul>
            </div>

            <div className="card">
              <div style={{ display: 'flex', alignItems: 'center', gap: '0.75rem', marginBottom: '0.75rem' }}>
                <div style={{ width: '2.5rem', height: '2.5rem', borderRadius: '0.5rem', backgroundColor: '#FEF3C7', color: '#B45309', display: 'flex', alignItems: 'center', justifyContent: 'center' }}>
                  <Shield size={20} />
                </div>
                <h3 style={{ fontSize: '1.125rem' }}>Donation Frequency Intervals</h3>
              </div>
              <ul style={{ paddingLeft: '1.25rem', color: '#64748B', fontSize: '0.9375rem', lineHeight: '1.7' }}>
                <li>Whole blood: <strong>Every 56 days (8 weeks)</strong> minimum interval.</li>
                <li>Platelet apheresis: <strong>Every 7 days</strong>, up to 24 times/year.</li>
                <li>Plasma: <strong>Every 28 days</strong>, up to 13 times/year.</li>
              </ul>
            </div>

            <div className="card">
              <div style={{ display: 'flex', alignItems: 'center', gap: '0.75rem', marginBottom: '0.75rem' }}>
                <div style={{ width: '2.5rem', height: '2.5rem', borderRadius: '0.5rem', backgroundColor: '#FEE2E2', color: '#C62828', display: 'flex', alignItems: 'center', justifyContent: 'center' }}>
                  <AlertCircle size={20} />
                </div>
                <h3 style={{ fontSize: '1.125rem' }}>Temporary Deferrals</h3>
              </div>
              <ul style={{ paddingLeft: '1.25rem', color: '#64748B', fontSize: '0.9375rem', lineHeight: '1.7' }}>
                <li>Recent antibiotics treatment (wait 24-48 hours after course).</li>
                <li>Recent tattoo or ear piercing (wait 3-6 months depending on facility certification).</li>
                <li>Pregnancy (eligible 6 weeks after delivery).</li>
              </ul>
            </div>
          </div>
        </div>

        {/* Call to action */}
        <div className="card" style={{ backgroundColor: '#1E293B', color: '#FFFFFF', textAlign: 'center', padding: '3rem 1.5rem', border: 'none' }}>
          <h2 style={{ fontSize: '1.875rem', color: '#FFFFFF', marginBottom: '0.75rem' }}>Ready to Make a Life-Saving Impact?</h2>
          <p style={{ color: '#94A3B8', maxWidth: '600px', margin: '0 auto 1.5rem', fontSize: '1rem' }}>
            Register your blood profile now. You can toggle your availability anytime and receive direct emergency requests when someone needs you.
          </p>
          <div style={{ display: 'flex', justifyContent: 'center', gap: '1rem' }}>
            <Link to="/register" className="btn btn-primary" style={{ padding: '0.75rem 1.5rem' }}>
              Register as Donor
            </Link>
            <Link to="/search-donors" className="btn btn-outline" style={{ color: '#FFFFFF', borderColor: '#475569' }}>
              Find Donors
            </Link>
          </div>
        </div>
      </div>
    </div>
  );
};
