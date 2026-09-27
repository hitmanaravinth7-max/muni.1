import React, { useState, useMemo } from 'react';
import { useSearchParams } from 'react-router-dom';
import { Search, Filter, Phone, MapPin, CheckCircle2, ShieldCheck, Heart, AlertCircle, Clock, Calendar, Check } from 'lucide-react';
import { useApp } from '../context/AppContext';
import { BloodGroup } from '../types';
import { COMPATIBILITY_RULES } from '../data/mockData';

export const SearchDonorsPage: React.FC = () => {
  const [searchParams] = useSearchParams();
  const initialGroup = searchParams.get('group') as BloodGroup | null;

  const { donors, createEmergencyRequest } = useApp();
  const [selectedGroup, setSelectedGroup] = useState<string>(initialGroup || 'ALL');
  const [searchQuery, setSearchQuery] = useState('');
  const [availableOnly, setAvailableOnly] = useState(true);
  const [recipientMatchGroup, setRecipientMatchGroup] = useState<string>('NONE');

  // Contact Modal State
  const [contactingDonor, setContactingDonor] = useState<any | null>(null);
  const [requestSentSuccess, setRequestSentSuccess] = useState(false);
  const [unitsNeeded, setUnitsNeeded] = useState(1);
  const [patientNotes, setPatientNotes] = useState('');

  const bloodGroups: (BloodGroup | 'ALL')[] = ['ALL', 'A+', 'A-', 'B+', 'B-', 'AB+', 'AB-', 'O+', 'O-'];

  const filteredDonors = useMemo(() => {
    return donors.filter(donor => {
      // 1. Group filter
      if (selectedGroup !== 'ALL' && donor.bloodGroup !== selectedGroup) {
        return false;
      }

      // 2. Compatibility match filter
      if (recipientMatchGroup !== 'NONE') {
        const acceptableGroups = COMPATIBILITY_RULES[recipientMatchGroup as BloodGroup]?.canReceiveFrom || [];
        if (!acceptableGroups.includes(donor.bloodGroup)) {
          return false;
        }
      }

      // 3. Availability filter
      if (availableOnly && !donor.isAvailable) {
        return false;
      }

      // 4. City / Name search query
      if (searchQuery.trim()) {
        const q = searchQuery.toLowerCase();
        const matchesCity = donor.city.toLowerCase().includes(q) || donor.state.toLowerCase().includes(q);
        const matchesName = donor.fullName.toLowerCase().includes(q);
        if (!matchesCity && !matchesName) return false;
      }

      return true;
    });
  }, [donors, selectedGroup, recipientMatchGroup, availableOnly, searchQuery]);

  const handleSendRequest = (e: React.FormEvent) => {
    e.preventDefault();
    if (!contactingDonor) return;

    createEmergencyRequest({
      patientName: `Urgent Request for ${contactingDonor.bloodGroup}`,
      hospitalName: 'Local Emergency Clinic / Hospital',
      bloodGroup: contactingDonor.bloodGroup,
      unitsNeeded,
      component: 'WHOLE_BLOOD',
      urgency: 'URGENT',
      contactPerson: 'Attending Caregiver',
      contactPhone: '+1 (555) 911-3322',
      city: contactingDonor.city,
      requiredDate: 'Within 2 hours',
      notes: patientNotes || `Direct donor matching requested for ${contactingDonor.fullName}`,
    });

    setRequestSentSuccess(true);
    setTimeout(() => {
      setRequestSentSuccess(false);
      setContactingDonor(null);
      setPatientNotes('');
    }, 2000);
  };

  return (
    <div style={{ backgroundColor: '#F8FAFC', minHeight: '80vh', padding: '2.5rem 0 4rem' }}>
      <div className="container">
        {/* Page Header */}
        <div style={{ marginBottom: '2rem' }}>
          <h1 style={{ fontSize: '2.25rem', marginBottom: '0.5rem', color: '#0F172A' }}>Find Available Blood Donors</h1>
          <p style={{ color: '#64748B', fontSize: '1.0625rem' }}>
            Direct access to verified voluntary blood donors. Connect immediately for emergency requirements.
          </p>
        </div>

        {/* Filter Controls Bar */}
        <div className="card" style={{ marginBottom: '2rem', padding: '1.5rem' }}>
          <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(220px, 1fr))', gap: '1.25rem', alignItems: 'flex-end' }}>
            {/* Search Input */}
            <div>
              <label style={{ display: 'block', fontSize: '0.875rem', fontWeight: 600, color: '#334155', marginBottom: '0.5rem' }}>
                Search City or Donor Name
              </label>
              <div style={{ position: 'relative' }}>
                <Search size={18} color="#94A3B8" style={{ position: 'absolute', left: '0.75rem', top: '50%', transform: 'translateY(-50%)' }} />
                <input
                  type="text"
                  placeholder="e.g. Downtown Metro or New York"
                  value={searchQuery}
                  onChange={e => setSearchQuery(e.target.value)}
                  style={{ width: '100%', padding: '0.625rem 0.75rem 0.625rem 2.375rem', borderRadius: '0.5rem', border: '1px solid #CBD5E1', fontSize: '0.9375rem', outline: 'none' }}
                />
              </div>
            </div>

            {/* Blood Group Filter */}
            <div>
              <label style={{ display: 'block', fontSize: '0.875rem', fontWeight: 600, color: '#334155', marginBottom: '0.5rem' }}>
                Donor Blood Group
              </label>
              <select
                value={selectedGroup}
                onChange={e => setSelectedGroup(e.target.value)}
                style={{ width: '100%', padding: '0.625rem 0.75rem', borderRadius: '0.5rem', border: '1px solid #CBD5E1', fontSize: '0.9375rem', backgroundColor: '#FFFFFF', outline: 'none' }}
              >
                {bloodGroups.map(bg => (
                  <option key={bg} value={bg}>
                    {bg === 'ALL' ? 'All Blood Groups' : bg}
                  </option>
                ))}
              </select>
            </div>

            {/* Recipient Compatibility Matcher */}
            <div>
              <label style={{ display: 'block', fontSize: '0.875rem', fontWeight: 600, color: '#334155', marginBottom: '0.5rem' }}>
                Recipient Blood Group (Compatible Match)
              </label>
              <select
                value={recipientMatchGroup}
                onChange={e => setRecipientMatchGroup(e.target.value)}
                style={{ width: '100%', padding: '0.625rem 0.75rem', borderRadius: '0.5rem', border: '1px solid #CBD5E1', fontSize: '0.9375rem', backgroundColor: '#FFFFFF', outline: 'none' }}
              >
                <option value="NONE">Any / Direct selection</option>
                <option value="A+">Patient is A+ (Can receive A+, A-, O+, O-)</option>
                <option value="A-">Patient is A- (Can receive A-, O-)</option>
                <option value="B+">Patient is B+ (Can receive B+, B-, O+, O-)</option>
                <option value="B-">Patient is B- (Can receive B-, O-)</option>
                <option value="AB+">Patient is AB+ (Universal Recipient - all groups)</option>
                <option value="AB-">Patient is AB- (Can receive AB-, A-, B-, O-)</option>
                <option value="O+">Patient is O+ (Can receive O+, O-)</option>
                <option value="O-">Patient is O- (Can receive O- only)</option>
              </select>
            </div>

            {/* Availability Checkbox Toggle */}
            <div style={{ display: 'flex', alignItems: 'center', height: '2.75rem' }}>
              <label style={{ display: 'flex', alignItems: 'center', gap: '0.625rem', cursor: 'pointer', fontSize: '0.9375rem', fontWeight: 600, color: '#1E293B', userSelect: 'none' }}>
                <input
                  type="checkbox"
                  checked={availableOnly}
                  onChange={e => setAvailableOnly(e.target.checked)}
                  style={{ width: '1.125rem', height: '1.125rem', accentColor: '#C62828', cursor: 'pointer' }}
                />
                Available Donors Only
              </label>
            </div>
          </div>
        </div>

        {/* Results Counter */}
        <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '1.25rem' }}>
          <div style={{ fontSize: '0.9375rem', color: '#64748B' }}>
            Showing <strong>{filteredDonors.length}</strong> verified donors matching your criteria
          </div>
        </div>

        {/* Donors Grid */}
        {filteredDonors.length === 0 ? (
          <div className="card" style={{ textAlign: 'center', padding: '4rem 1.5rem' }}>
            <div style={{ width: '4rem', height: '4rem', borderRadius: '50%', backgroundColor: '#FEE2E2', color: '#DC2626', display: 'flex', alignItems: 'center', justifyContent: 'center', margin: '0 auto 1.25rem' }}>
              <AlertCircle size={32} />
            </div>
            <h3 style={{ fontSize: '1.25rem', marginBottom: '0.5rem' }}>No Donors Found</h3>
            <p style={{ color: '#64748B', maxWidth: '480px', margin: '0 auto 1.5rem' }}>
              We could not find any donors matching your specific combination of filters. Try broadening the search location or switching to compatible blood groups.
            </p>
            <button
              onClick={() => { setSelectedGroup('ALL'); setSearchQuery(''); setRecipientMatchGroup('NONE'); setAvailableOnly(false); }}
              className="btn btn-outline"
            >
              Reset All Filters
            </button>
          </div>
        ) : (
          <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(320px, 1fr))', gap: '1.5rem' }}>
            {filteredDonors.map(donor => (
              <div key={donor.id} className="card" style={{ display: 'flex', flexDirection: 'column', justifyContent: 'space-between' }}>
                <div>
                  {/* Top card header */}
                  <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start', marginBottom: '1rem' }}>
                    <div style={{ display: 'flex', alignItems: 'center', gap: '0.75rem' }}>
                      <div style={{ width: '3.25rem', height: '3.25rem', borderRadius: '0.625rem', backgroundColor: '#FFEBEE', color: '#C62828', display: 'flex', alignItems: 'center', justifyContent: 'center', fontSize: '1.375rem', fontWeight: 800 }}>
                        {donor.bloodGroup}
                      </div>
                      <div>
                        <div style={{ display: 'flex', alignItems: 'center', gap: '0.375rem' }}>
                          <h3 style={{ fontSize: '1.125rem', color: '#0F172A', fontWeight: 700 }}>{donor.fullName}</h3>
                          {donor.isVerified && (
                            <span title="Verified Medical Donor">
                              <ShieldCheck size={18} color="#166534" />
                            </span>
                          )}
                        </div>
                        <div style={{ display: 'flex', alignItems: 'center', gap: '0.375rem', fontSize: '0.8125rem', color: '#64748B', marginTop: '0.125rem' }}>
                          <MapPin size={14} /> {donor.city}, {donor.state} (~{donor.distanceKm} km away)
                        </div>
                      </div>
                    </div>

                    <span className={`badge ${donor.isAvailable ? 'badge-green' : 'badge-amber'}`}>
                      {donor.isAvailable ? 'AVAILABLE' : 'OFFLINE'}
                    </span>
                  </div>

                  {/* Donor Stats */}
                  <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '0.5rem', backgroundColor: '#F8FAFC', borderRadius: '0.5rem', padding: '0.75rem', marginBottom: '1.25rem', fontSize: '0.8125rem' }}>
                    <div>
                      <span style={{ color: '#64748B', display: 'block' }}>Total Donations</span>
                      <strong style={{ color: '#0F172A' }}>{donor.totalDonations} times</strong>
                    </div>
                    <div>
                      <span style={{ color: '#64748B', display: 'block' }}>Last Donated</span>
                      <strong style={{ color: '#0F172A' }}>{donor.lastDonationDate || 'Eligible Now'}</strong>
                    </div>
                  </div>
                </div>

                {/* Card Actions */}
                <div style={{ display: 'flex', gap: '0.75rem', borderTop: '1px solid #F1F5F9', paddingTop: '1rem' }}>
                  <a
                    href={`tel:${donor.phone}`}
                    className="btn btn-outline"
                    style={{ flex: 1, padding: '0.5rem', fontSize: '0.875rem' }}
                  >
                    <Phone size={15} color="#C62828" />
                    Call Direct
                  </a>

                  <button
                    onClick={() => setContactingDonor(donor)}
                    className="btn btn-primary"
                    style={{ flex: 1.2, padding: '0.5rem', fontSize: '0.875rem' }}
                  >
                    <Heart size={15} />
                    Request Blood
                  </button>
                </div>
              </div>
            ))}
          </div>
        )}
      </div>

      {/* Request Modal */}
      {contactingDonor && (
        <div style={{ position: 'fixed', inset: 0, backgroundColor: 'rgba(15, 23, 42, 0.65)', display: 'flex', alignItems: 'center', justifyContent: 'center', zIndex: 100, padding: '1rem' }}>
          <div className="card" style={{ maxWidth: '480px', width: '100%', boxShadow: '0 20px 25px -5px rgba(0,0,0,0.2)' }}>
            <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '1rem' }}>
              <h3 style={{ fontSize: '1.25rem' }}>Request Blood from {contactingDonor.fullName}</h3>
              <button onClick={() => setContactingDonor(null)} style={{ background: 'none', border: 'none', fontSize: '1.5rem', cursor: 'pointer', color: '#64748B' }}>×</button>
            </div>

            {requestSentSuccess ? (
              <div style={{ textAlign: 'center', padding: '2rem 1rem' }}>
                <div style={{ width: '3.5rem', height: '3.5rem', borderRadius: '50%', backgroundColor: '#DCFCE7', color: '#166534', display: 'flex', alignItems: 'center', justifyContent: 'center', margin: '0 auto 1rem' }}>
                  <Check size={28} />
                </div>
                <h4 style={{ fontSize: '1.25rem', marginBottom: '0.5rem' }}>Urgent Request Broadcasted!</h4>
                <p style={{ color: '#64748B', fontSize: '0.9375rem' }}>
                  Donor has been alerted and coordinator hotline has been opened.
                </p>
              </div>
            ) : (
              <form onSubmit={handleSendRequest}>
                <div style={{ backgroundColor: '#FEE2E2', borderRadius: '0.5rem', padding: '0.75rem', marginBottom: '1.25rem', fontSize: '0.875rem', color: '#991B1B' }}>
                  Matching Blood Group: <strong>{contactingDonor.bloodGroup}</strong> • Phone: <strong>{contactingDonor.phone}</strong>
                </div>

                <div style={{ marginBottom: '1rem' }}>
                  <label style={{ display: 'block', fontSize: '0.875rem', fontWeight: 600, marginBottom: '0.375rem' }}>Units Needed</label>
                  <input
                    type="number"
                    min="1"
                    max="6"
                    value={unitsNeeded}
                    onChange={e => setUnitsNeeded(parseInt(e.target.value) || 1)}
                    style={{ width: '100%', padding: '0.625rem', borderRadius: '0.375rem', border: '1px solid #CBD5E1' }}
                  />
                </div>

                <div style={{ marginBottom: '1.5rem' }}>
                  <label style={{ display: 'block', fontSize: '0.875rem', fontWeight: 600, marginBottom: '0.375rem' }}>Urgency & Patient Notes</label>
                  <textarea
                    rows={3}
                    placeholder="e.g. ICU patient scheduled for emergency surgery at General Hospital..."
                    value={patientNotes}
                    onChange={e => setPatientNotes(e.target.value)}
                    style={{ width: '100%', padding: '0.625rem', borderRadius: '0.375rem', border: '1px solid #CBD5E1', resize: 'vertical' }}
                  />
                </div>

                <div style={{ display: 'flex', gap: '0.75rem' }}>
                  <button type="button" onClick={() => setContactingDonor(null)} className="btn btn-outline" style={{ flex: 1 }}>
                    Cancel
                  </button>
                  <button type="submit" className="btn btn-primary" style={{ flex: 1.5 }}>
                    Send Emergency Alert
                  </button>
                </div>
              </form>
            )}
          </div>
        </div>
      )}
    </div>
  );
};
