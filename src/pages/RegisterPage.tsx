import React, { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { Heart, Building2, User, Phone, MapPin, Mail, Lock, ShieldCheck } from 'lucide-react';
import { useApp } from '../context/AppContext';
import { BloodGroup } from '../types';

export const RegisterPage: React.FC = () => {
  const { registerDonor, registerBloodBank } = useApp();
  const navigate = useNavigate();

  const [regType, setRegType] = useState<'DONOR' | 'HOSPITAL'>('DONOR');

  // Donor state
  const [fullName, setFullName] = useState('');
  const [email, setEmail] = useState('');
  const [phone, setPhone] = useState('');
  const [bloodGroup, setBloodGroup] = useState<BloodGroup>('O+');
  const [city, setCity] = useState('');
  const [state, setState] = useState('NY');
  const [password, setPassword] = useState('');

  // Hospital state
  const [orgName, setOrgName] = useState('');
  const [licenseNumber, setLicenseNumber] = useState('');
  const [address, setAddress] = useState('');
  const [hotline, setHotline] = useState('');

  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');

  const bloodGroups: BloodGroup[] = ['A+', 'A-', 'B+', 'B-', 'AB+', 'AB-', 'O+', 'O-'];

  const handleRegister = async (e: React.FormEvent) => {
    e.preventDefault();
    setError('');

    if (regType === 'DONOR') {
      if (!fullName.trim() || !email.trim() || !phone.trim() || !city.trim()) {
        setError('Please fill out all mandatory fields.');
        return;
      }

      setLoading(true);
      try {
        await registerDonor({
          userId: `user_${Date.now()}`,
          fullName,
          email,
          phone,
          bloodGroup,
          city,
          state,
          isAvailable: true,
          isVerified: true,
          latitude: 40.7128,
          longitude: -74.0060,
        });
        navigate('/donor-dashboard');
      } catch (err) {
        setError('Registration failed. Please try again.');
      } finally {
        setLoading(false);
      }
    } else {
      if (!orgName.trim() || !licenseNumber.trim() || !address.trim() || !city.trim() || !hotline.trim()) {
        setError('Please complete all facility registration credentials.');
        return;
      }

      setLoading(true);
      try {
        await registerBloodBank({
          name: orgName,
          licenseNumber,
          address,
          city,
          state,
          phone: hotline,
          emergencyHotline: hotline,
          latitude: 40.7128,
          longitude: -74.0060,
          isVerified: true,
        });
        navigate('/hospital-dashboard');
      } catch (err) {
        setError('Facility registration failed. Please try again.');
      } finally {
        setLoading(false);
      }
    }
  };

  return (
    <div style={{ backgroundColor: '#F8FAFC', minHeight: '85vh', padding: '3.5rem 1rem' }}>
      <div style={{ maxWidth: '560px', width: '100%', margin: '0 auto' }}>
        <div className="card" style={{ padding: '2.5rem 2rem', boxShadow: '0 10px 25px -5px rgba(0,0,0,0.08)' }}>
          <div style={{ textAlign: 'center', marginBottom: '2rem' }}>
            <div style={{ width: '3.25rem', height: '3.25rem', borderRadius: '0.75rem', backgroundColor: '#C62828', display: 'flex', alignItems: 'center', justifyContent: 'center', color: '#FFFFFF', margin: '0 auto 0.75rem', boxShadow: '0 4px 12px rgba(198, 40, 40, 0.3)' }}>
              <Heart size={26} fill="#FFFFFF" />
            </div>
            <h1 style={{ fontSize: '1.625rem', fontWeight: 800, color: '#0F172A' }}>Register with BloodBridge</h1>
            <p style={{ color: '#64748B', fontSize: '0.875rem', marginTop: '0.25rem' }}>
              Connect with thousands of patients and certified emergency centers
            </p>
          </div>

          {/* Toggle Type */}
          <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '0.5rem', backgroundColor: '#F1F5F9', padding: '0.375rem', borderRadius: '0.5rem', marginBottom: '1.75rem' }}>
            <button
              type="button"
              onClick={() => setRegType('DONOR')}
              style={{
                padding: '0.625rem',
                borderRadius: '0.375rem',
                border: 'none',
                backgroundColor: regType === 'DONOR' ? '#FFFFFF' : 'transparent',
                color: regType === 'DONOR' ? '#C62828' : '#64748B',
                fontWeight: 700,
                fontSize: '0.875rem',
                cursor: 'pointer',
                display: 'flex',
                alignItems: 'center',
                justifyContent: 'center',
                gap: '0.375rem',
                boxShadow: regType === 'DONOR' ? '0 1px 3px rgba(0,0,0,0.1)' : 'none',
              }}
            >
              <Heart size={16} /> Volunteer Donor
            </button>

            <button
              type="button"
              onClick={() => setRegType('HOSPITAL')}
              style={{
                padding: '0.625rem',
                borderRadius: '0.375rem',
                border: 'none',
                backgroundColor: regType === 'HOSPITAL' ? '#FFFFFF' : 'transparent',
                color: regType === 'HOSPITAL' ? '#1E40AF' : '#64748B',
                fontWeight: 700,
                fontSize: '0.875rem',
                cursor: 'pointer',
                display: 'flex',
                alignItems: 'center',
                justifyContent: 'center',
                gap: '0.375rem',
                boxShadow: regType === 'HOSPITAL' ? '0 1px 3px rgba(0,0,0,0.1)' : 'none',
              }}
            >
              <Building2 size={16} /> Blood Bank / Center
            </button>
          </div>

          {error && (
            <div style={{ backgroundColor: '#FEF2F2', border: '1px solid #FECACA', borderRadius: '0.5rem', padding: '0.75rem', color: '#B91C1C', fontSize: '0.875rem', marginBottom: '1.25rem' }}>
              {error}
            </div>
          )}

          <form onSubmit={handleRegister}>
            {regType === 'DONOR' ? (
              <>
                <div style={{ marginBottom: '1.25rem' }}>
                  <label style={{ display: 'block', fontSize: '0.875rem', fontWeight: 600, color: '#334155', marginBottom: '0.375rem' }}>
                    Full Legal Name
                  </label>
                  <input
                    type="text"
                    required
                    placeholder="e.g. Alexander Chen"
                    value={fullName}
                    onChange={e => setFullName(e.target.value)}
                    style={{ width: '100%', padding: '0.625rem 0.75rem', borderRadius: '0.5rem', border: '1px solid #CBD5E1', fontSize: '0.9375rem' }}
                  />
                </div>

                <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '1rem', marginBottom: '1.25rem' }}>
                  <div>
                    <label style={{ display: 'block', fontSize: '0.875rem', fontWeight: 600, color: '#334155', marginBottom: '0.375rem' }}>
                      Blood Group
                    </label>
                    <select
                      value={bloodGroup}
                      onChange={e => setBloodGroup(e.target.value as BloodGroup)}
                      style={{ width: '100%', padding: '0.625rem 0.75rem', borderRadius: '0.5rem', border: '1px solid #CBD5E1', fontSize: '0.9375rem', backgroundColor: '#FFFFFF' }}
                    >
                      {bloodGroups.map(bg => (
                        <option key={bg} value={bg}>{bg}</option>
                      ))}
                    </select>
                  </div>

                  <div>
                    <label style={{ display: 'block', fontSize: '0.875rem', fontWeight: 600, color: '#334155', marginBottom: '0.375rem' }}>
                      Phone (Direct/SMS)
                    </label>
                    <input
                      type="tel"
                      required
                      placeholder="+1 (555) 000-0000"
                      value={phone}
                      onChange={e => setPhone(e.target.value)}
                      style={{ width: '100%', padding: '0.625rem 0.75rem', borderRadius: '0.5rem', border: '1px solid #CBD5E1', fontSize: '0.9375rem' }}
                    />
                  </div>
                </div>

                <div style={{ marginBottom: '1.25rem' }}>
                  <label style={{ display: 'block', fontSize: '0.875rem', fontWeight: 600, color: '#334155', marginBottom: '0.375rem' }}>
                    Email Address
                  </label>
                  <input
                    type="email"
                    required
                    placeholder="alex@example.com"
                    value={email}
                    onChange={e => setEmail(e.target.value)}
                    style={{ width: '100%', padding: '0.625rem 0.75rem', borderRadius: '0.5rem', border: '1px solid #CBD5E1', fontSize: '0.9375rem' }}
                  />
                </div>

                <div style={{ display: 'grid', gridTemplateColumns: '2fr 1fr', gap: '1rem', marginBottom: '1.5rem' }}>
                  <div>
                    <label style={{ display: 'block', fontSize: '0.875rem', fontWeight: 600, color: '#334155', marginBottom: '0.375rem' }}>
                      City / Area
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
                      State
                    </label>
                    <input
                      type="text"
                      value={state}
                      onChange={e => setState(e.target.value)}
                      style={{ width: '100%', padding: '0.625rem 0.75rem', borderRadius: '0.5rem', border: '1px solid #CBD5E1', fontSize: '0.9375rem' }}
                    />
                  </div>
                </div>
              </>
            ) : (
              <>
                <div style={{ marginBottom: '1.25rem' }}>
                  <label style={{ display: 'block', fontSize: '0.875rem', fontWeight: 600, color: '#334155', marginBottom: '0.375rem' }}>
                    Facility / Blood Bank Name
                  </label>
                  <input
                    type="text"
                    required
                    placeholder="e.g. City General Trauma Center Blood Bank"
                    value={orgName}
                    onChange={e => setOrgName(e.target.value)}
                    style={{ width: '100%', padding: '0.625rem 0.75rem', borderRadius: '0.5rem', border: '1px solid #CBD5E1', fontSize: '0.9375rem' }}
                  />
                </div>

                <div style={{ marginBottom: '1.25rem' }}>
                  <label style={{ display: 'block', fontSize: '0.875rem', fontWeight: 600, color: '#334155', marginBottom: '0.375rem' }}>
                    Clinical License / FDA Registration ID
                  </label>
                  <input
                    type="text"
                    required
                    placeholder="FDA-BB-XXXXX"
                    value={licenseNumber}
                    onChange={e => setLicenseNumber(e.target.value)}
                    style={{ width: '100%', padding: '0.625rem 0.75rem', borderRadius: '0.5rem', border: '1px solid #CBD5E1', fontSize: '0.9375rem' }}
                  />
                </div>

                <div style={{ marginBottom: '1.25rem' }}>
                  <label style={{ display: 'block', fontSize: '0.875rem', fontWeight: 600, color: '#334155', marginBottom: '0.375rem' }}>
                    Street Address
                  </label>
                  <input
                    type="text"
                    required
                    placeholder="100 Medical Center Parkway"
                    value={address}
                    onChange={e => setAddress(e.target.value)}
                    style={{ width: '100%', padding: '0.625rem 0.75rem', borderRadius: '0.5rem', border: '1px solid #CBD5E1', fontSize: '0.9375rem' }}
                  />
                </div>

                <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '1rem', marginBottom: '1.5rem' }}>
                  <div>
                    <label style={{ display: 'block', fontSize: '0.875rem', fontWeight: 600, color: '#334155', marginBottom: '0.375rem' }}>
                      City
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
                      24/7 Emergency Hotline
                    </label>
                    <input
                      type="tel"
                      required
                      placeholder="+1 (800) 555-0199"
                      value={hotline}
                      onChange={e => setHotline(e.target.value)}
                      style={{ width: '100%', padding: '0.625rem 0.75rem', borderRadius: '0.5rem', border: '1px solid #CBD5E1', fontSize: '0.9375rem' }}
                    />
                  </div>
                </div>
              </>
            )}

            <button
              type="submit"
              disabled={loading}
              className="btn btn-primary"
              style={{ width: '100%', padding: '0.75rem', fontSize: '1rem', borderRadius: '0.5rem' }}
            >
              {loading ? 'Creating Profile...' : regType === 'DONOR' ? 'Complete Donor Registration' : 'Register Blood Facility'}
            </button>
          </form>

          <div style={{ textAlign: 'center', marginTop: '1.5rem', fontSize: '0.875rem', color: '#64748B' }}>
            Already registered?{' '}
            <Link to="/login" style={{ fontWeight: 600, color: '#C62828' }}>
              Sign in to your account
            </Link>
          </div>
        </div>
      </div>
    </div>
  );
};
