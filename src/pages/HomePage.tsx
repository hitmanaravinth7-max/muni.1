import React from 'react';
import { Link } from 'react-router-dom';
import { Heart, Search, UserPlus, ShieldAlert, CheckCircle, Clock, MapPin, ArrowRight, Activity, Users, Building, Droplets, AlertTriangle, Phone } from 'lucide-react';
import { useApp } from '../context/AppContext';
import { BloodGroup } from '../types';

export const HomePage: React.FC = () => {
  const { donors, bloodBanks, emergencyRequests } = useApp();

  const activeDonorsCount = donors.filter(d => d.isAvailable).length;
  const verifiedBanksCount = bloodBanks.length;
  const activeRequests = emergencyRequests.filter(r => r.status === 'ACTIVE');
  const fulfilledRequestsCount = emergencyRequests.filter(r => r.status === 'FULFILLED').length;

  const bloodGroups: BloodGroup[] = ['A+', 'A-', 'B+', 'B-', 'AB+', 'AB-', 'O+', 'O-'];

  return (
    <div>
      {/* Hero Section */}
      <section style={{ background: 'linear-gradient(180deg, #FFFFFF 0%, #FFF5F5 100%)', paddingTop: '4rem', paddingBottom: '4.5rem', borderBottom: '1px solid #FEE2E2' }}>
        <div className="container" style={{ textAlign: 'center', maxWidth: '900px' }}>
          {/* Badge */}
          <div style={{ display: 'inline-flex', alignItems: 'center', gap: '0.5rem', backgroundColor: '#FEE2E2', color: '#991B1B', padding: '0.35rem 0.875rem', borderRadius: '9999px', fontSize: '0.875rem', fontWeight: 600, marginBottom: '1.5rem' }}>
            <Activity size={16} />
            Emergency Blood Availability & Rapid Donor Matching
          </div>

          <h1 style={{ fontSize: 'clamp(2.5rem, 5vw, 3.75rem)', fontWeight: 800, lineHeight: 1.15, color: '#0F172A', marginBottom: '1.25rem', letterSpacing: '-0.03em' }}>
            Find Blood. Find Hope. <span style={{ color: '#C62828' }}>Save Lives.</span>
          </h1>

          <p style={{ fontSize: 'clamp(1.125rem, 2vw, 1.25rem)', color: '#475569', lineHeight: 1.6, marginBottom: '2.5rem', maxWidth: '720px', margin: '0 auto 2.5rem' }}>
            Quickly connect with available blood donors and blood banks during emergencies.
          </p>

          {/* Action Buttons */}
          <div style={{ display: 'flex', flexWrap: 'wrap', justifyContent: 'center', gap: '1rem', marginBottom: '2.5rem' }}>
            <Link
              to="/search-donors"
              className="btn btn-primary"
              style={{ padding: '0.875rem 1.75rem', fontSize: '1.0625rem', borderRadius: '0.625rem' }}
            >
              <Search size={20} />
              Find Blood Donors
            </Link>

            <Link
              to="/register"
              className="btn btn-outline"
              style={{ padding: '0.875rem 1.75rem', fontSize: '1.0625rem', borderRadius: '0.625rem', borderColor: '#CBD5E1', backgroundColor: '#FFFFFF' }}
            >
              <UserPlus size={20} color="#C62828" />
              Become a Donor
            </Link>
          </div>

          {/* Emergency Notice */}
          <div style={{ backgroundColor: '#FEF2F2', border: '1px solid #FECACA', borderRadius: '0.75rem', padding: '1rem 1.25rem', display: 'flex', alignItems: 'flex-start', gap: '0.75rem', textAlign: 'left', maxWidth: '760px', margin: '0 auto' }}>
            <ShieldAlert size={22} color="#DC2626" style={{ flexShrink: 0, marginTop: '0.125rem' }} />
            <p style={{ fontSize: '0.875rem', color: '#991B1B', lineHeight: 1.5, margin: 0 }}>
              <strong>Emergency Notice:</strong> For life-threatening emergencies, contact local emergency services first. BloodBridge helps coordinate blood availability and donor connections.
            </p>
          </div>
        </div>
      </section>

      {/* Live Statistics Section */}
      <section style={{ backgroundColor: '#FFFFFF', padding: '3.5rem 0', borderBottom: '1px solid #E2E8F0' }}>
        <div className="container">
          <div style={{ textAlign: 'center', marginBottom: '2.5rem' }}>
            <h2 style={{ fontSize: '1.75rem', marginBottom: '0.5rem' }}>Live Platform Telemetry</h2>
            <p style={{ color: '#64748B', fontSize: '0.9375rem' }}>Real-time updates across registered blood centers and verified active volunteer donors</p>
          </div>

          <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(220px, 1fr))', gap: '1.5rem' }}>
            <div className="card" style={{ textAlign: 'center', borderTop: '4px solid #C62828' }}>
              <div style={{ width: '3rem', height: '3rem', borderRadius: '50%', backgroundColor: '#FFEBEE', display: 'flex', alignItems: 'center', justifyContent: 'center', margin: '0 auto 1rem', color: '#C62828' }}>
                <Users size={24} />
              </div>
              <div style={{ fontSize: '2.25rem', fontWeight: 800, color: '#0F172A', lineHeight: 1 }}>{activeDonorsCount}</div>
              <div style={{ fontSize: '0.875rem', fontWeight: 600, color: '#64748B', marginTop: '0.5rem' }}>Available Donors Ready</div>
            </div>

            <div className="card" style={{ textAlign: 'center', borderTop: '4px solid #1E40AF' }}>
              <div style={{ width: '3rem', height: '3rem', borderRadius: '50%', backgroundColor: '#EFF6FF', display: 'flex', alignItems: 'center', justifyContent: 'center', margin: '0 auto 1rem', color: '#1E40AF' }}>
                <Building size={24} />
              </div>
              <div style={{ fontSize: '2.25rem', fontWeight: 800, color: '#0F172A', lineHeight: 1 }}>{verifiedBanksCount}</div>
              <div style={{ fontSize: '0.875rem', fontWeight: 600, color: '#64748B', marginTop: '0.5rem' }}>Registered Blood Banks</div>
            </div>

            <div className="card" style={{ textAlign: 'center', borderTop: '4px solid #DC2626' }}>
              <div style={{ width: '3rem', height: '3rem', borderRadius: '50%', backgroundColor: '#FEF2F2', display: 'flex', alignItems: 'center', justifyContent: 'center', margin: '0 auto 1rem', color: '#DC2626' }}>
                <Droplets size={24} />
              </div>
              <div style={{ fontSize: '2.25rem', fontWeight: 800, color: '#DC2626', lineHeight: 1 }}>{activeRequests.length}</div>
              <div style={{ fontSize: '0.875rem', fontWeight: 600, color: '#64748B', marginTop: '0.5rem' }}>Active Blood Requests</div>
            </div>

            <div className="card" style={{ textAlign: 'center', borderTop: '4px solid #166534' }}>
              <div style={{ width: '3rem', height: '3rem', borderRadius: '50%', backgroundColor: '#F0FDF4', display: 'flex', alignItems: 'center', justifyContent: 'center', margin: '0 auto 1rem', color: '#166534' }}>
                <CheckCircle size={24} />
              </div>
              <div style={{ fontSize: '2.25rem', fontWeight: 800, color: '#0F172A', lineHeight: 1 }}>{fulfilledRequestsCount + 48}</div>
              <div style={{ fontSize: '0.875rem', fontWeight: 600, color: '#64748B', marginTop: '0.5rem' }}>Requests Fulfilled</div>
            </div>
          </div>
        </div>
      </section>

      {/* Urgent Emergency Requests Banner/Feed */}
      {activeRequests.length > 0 && (
        <section style={{ backgroundColor: '#FEF2F2', padding: '3rem 0', borderBottom: '1px solid #FECACA' }}>
          <div className="container">
            <div style={{ display: 'flex', flexWrap: 'wrap', justifyContent: 'space-between', alignItems: 'center', gap: '1rem', marginBottom: '1.5rem' }}>
              <div style={{ display: 'flex', alignItems: 'center', gap: '0.75rem' }}>
                <div style={{ width: '2.5rem', height: '2.5rem', borderRadius: '0.5rem', backgroundColor: '#DC2626', display: 'flex', alignItems: 'center', justifyContent: 'center', color: '#FFFFFF' }}>
                  <AlertTriangle size={22} />
                </div>
                <div>
                  <h2 style={{ fontSize: '1.5rem', color: '#991B1B' }}>Urgent Emergency Requests</h2>
                  <p style={{ color: '#B91C1C', fontSize: '0.875rem' }}>Direct emergency transfusions required right now</p>
                </div>
              </div>
              <Link to="/emergency-request" className="btn btn-danger" style={{ padding: '0.5rem 1rem', fontSize: '0.875rem' }}>
                + Post Emergency Request
              </Link>
            </div>

            <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(320px, 1fr))', gap: '1.25rem' }}>
              {activeRequests.map(req => (
                <div key={req.id} style={{ backgroundColor: '#FFFFFF', borderRadius: '0.75rem', border: '1px solid #FCA5A5', padding: '1.25rem', boxShadow: '0 2px 4px rgba(220, 38, 38, 0.08)' }}>
                  <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start', marginBottom: '0.75rem' }}>
                    <span style={{ fontSize: '1.5rem', fontWeight: 800, color: '#B91C1C', backgroundColor: '#FEE2E2', padding: '0.2rem 0.6rem', borderRadius: '0.375rem', display: 'inline-block' }}>
                      {req.bloodGroup}
                    </span>
                    <span className={`badge ${req.urgency === 'CRITICAL' ? 'badge-red' : 'badge-amber'}`}>
                      {req.urgency} PRIORITY
                    </span>
                  </div>
                  <h3 style={{ fontSize: '1.125rem', marginBottom: '0.25rem', color: '#0F172A' }}>{req.patientName}</h3>
                  <p style={{ fontSize: '0.875rem', color: '#64748B', display: 'flex', alignItems: 'center', gap: '0.375rem', marginBottom: '0.5rem' }}>
                    <Building size={15} /> {req.hospitalName}, {req.city}
                  </p>
                  <p style={{ fontSize: '0.875rem', color: '#475569', marginBottom: '1rem', fontStyle: 'italic' }}>
                    "{req.notes}"
                  </p>
                  <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', borderTop: '1px solid #F1F5F9', paddingTop: '0.75rem' }}>
                    <span style={{ fontSize: '0.8125rem', color: '#64748B' }}>Needed: <strong>{req.unitsNeeded} units</strong> ({req.component})</span>
                    <a
                      href={`tel:${req.contactPhone}`}
                      className="btn btn-primary"
                      style={{ padding: '0.4rem 0.85rem', fontSize: '0.8125rem' }}
                    >
                      <Phone size={14} /> Call Hotline
                    </a>
                  </div>
                </div>
              ))}
            </div>
          </div>
        </section>
      )}

      {/* Blood Group Section */}
      <section style={{ backgroundColor: '#FFFFFF', padding: '4rem 0', borderBottom: '1px solid #E2E8F0' }}>
        <div className="container">
          <div style={{ textAlign: 'center', maxWidth: '640px', margin: '0 auto 3rem' }}>
            <h2 style={{ fontSize: '2rem', marginBottom: '0.5rem' }}>Blood Group Directory</h2>
            <p style={{ color: '#64748B', fontSize: '1rem' }}>
              Select your blood group to immediately inspect available registered donors and compatibility profiles.
            </p>
          </div>

          <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(130px, 1fr))', gap: '1rem' }}>
            {bloodGroups.map(bg => {
              const count = donors.filter(d => d.bloodGroup === bg && d.isAvailable).length;
              const isUniversalDonor = bg === 'O-';
              const isUniversalRecipient = bg === 'AB+';

              return (
                <Link
                  key={bg}
                  to={`/search-donors?group=${encodeURIComponent(bg)}`}
                  style={{
                    backgroundColor: '#FFFFFF',
                    border: '1.5px solid #E2E8F0',
                    borderRadius: '0.75rem',
                    padding: '1.25rem 0.75rem',
                    textAlign: 'center',
                    textDecoration: 'none',
                    transition: 'all 0.2s ease',
                    boxShadow: '0 1px 3px rgba(0,0,0,0.04)',
                    display: 'flex',
                    flexDirection: 'column',
                    alignItems: 'center',
                    justifyContent: 'center',
                  }}
                  onMouseEnter={e => {
                    e.currentTarget.style.borderColor = '#C62828';
                    e.currentTarget.style.transform = 'translateY(-3px)';
                    e.currentTarget.style.boxShadow = '0 6px 16px rgba(198, 40, 40, 0.15)';
                  }}
                  onMouseLeave={e => {
                    e.currentTarget.style.borderColor = '#E2E8F0';
                    e.currentTarget.style.transform = 'none';
                    e.currentTarget.style.boxShadow = '0 1px 3px rgba(0,0,0,0.04)';
                  }}
                >
                  <div style={{ width: '3.25rem', height: '3.25rem', borderRadius: '50%', backgroundColor: '#FFEBEE', color: '#C62828', display: 'flex', alignItems: 'center', justifyContent: 'center', fontSize: '1.375rem', fontWeight: 800, marginBottom: '0.75rem' }}>
                    {bg}
                  </div>
                  <div style={{ fontSize: '0.8125rem', fontWeight: 600, color: '#1E293B' }}>
                    {count} Available
                  </div>
                  {isUniversalDonor && (
                    <span style={{ fontSize: '0.6875rem', color: '#C62828', fontWeight: 700, marginTop: '0.35rem' }}>Universal Donor</span>
                  )}
                  {isUniversalRecipient && (
                    <span style={{ fontSize: '0.6875rem', color: '#1E40AF', fontWeight: 700, marginTop: '0.35rem' }}>Universal Recipient</span>
                  )}
                </Link>
              );
            })}
          </div>
        </div>
      </section>

      {/* How BloodBridge Works */}
      <section style={{ backgroundColor: '#F8FAFC', padding: '4.5rem 0', borderBottom: '1px solid #E2E8F0' }}>
        <div className="container">
          <div style={{ textAlign: 'center', maxWidth: '640px', margin: '0 auto 3.5rem' }}>
            <span style={{ fontSize: '0.8125rem', fontWeight: 700, color: '#C62828', textTransform: 'uppercase', letterSpacing: '0.075em' }}>Simplified Protocol</span>
            <h2 style={{ fontSize: '2.25rem', marginTop: '0.5rem', marginBottom: '0.75rem' }}>How BloodBridge Works</h2>
            <p style={{ color: '#64748B', fontSize: '1.0625rem' }}>Every second counts in medical emergencies. Our streamlined workflow connects patients with donors in minutes.</p>
          </div>

          <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(240px, 1fr))', gap: '2rem' }}>
            {/* Step 1 */}
            <div className="card" style={{ position: 'relative', overflow: 'hidden' }}>
              <div style={{ fontSize: '3rem', fontWeight: 900, color: '#F1F5F9', position: 'absolute', top: '0.5rem', right: '1rem', lineHeight: 1 }}>01</div>
              <div style={{ width: '3rem', height: '3rem', borderRadius: '0.5rem', backgroundColor: '#FFEBEE', color: '#C62828', display: 'flex', alignItems: 'center', justifyContent: 'center', marginBottom: '1.25rem' }}>
                <Search size={22} />
              </div>
              <h3 style={{ fontSize: '1.25rem', marginBottom: '0.5rem' }}>Step 1: Search</h3>
              <p style={{ color: '#64748B', fontSize: '0.9375rem', lineHeight: 1.6 }}>
                Locate verified donors or check blood bank inventory by blood group, specific component, and proximity radius.
              </p>
            </div>

            {/* Step 2 */}
            <div className="card" style={{ position: 'relative', overflow: 'hidden' }}>
              <div style={{ fontSize: '3rem', fontWeight: 900, color: '#F1F5F9', position: 'absolute', top: '0.5rem', right: '1rem', lineHeight: 1 }}>02</div>
              <div style={{ width: '3rem', height: '3rem', borderRadius: '0.5rem', backgroundColor: '#EFF6FF', color: '#1E40AF', display: 'flex', alignItems: 'center', justifyContent: 'center', marginBottom: '1.25rem' }}>
                <Droplets size={22} />
              </div>
              <h3 style={{ fontSize: '1.25rem', marginBottom: '0.5rem' }}>Step 2: Find a Match</h3>
              <p style={{ color: '#64748B', fontSize: '0.9375rem', lineHeight: 1.6 }}>
                Our medical compatibility engine flags exact and compatible blood matches, highlighting verified donors.
              </p>
            </div>

            {/* Step 3 */}
            <div className="card" style={{ position: 'relative', overflow: 'hidden' }}>
              <div style={{ fontSize: '3rem', fontWeight: 900, color: '#F1F5F9', position: 'absolute', top: '0.5rem', right: '1rem', lineHeight: 1 }}>03</div>
              <div style={{ width: '3rem', height: '3rem', borderRadius: '0.5rem', backgroundColor: '#FEF3C7', color: '#B45309', display: 'flex', alignItems: 'center', justifyContent: 'center', marginBottom: '1.25rem' }}>
                <Phone size={22} />
              </div>
              <h3 style={{ fontSize: '1.25rem', marginBottom: '0.5rem' }}>Step 3: Contact / Respond</h3>
              <p style={{ color: '#64748B', fontSize: '0.9375rem', lineHeight: 1.6 }}>
                Instantly connect via direct phone call, SMS, or broadcast urgent emergency notifications to local matched donors.
              </p>
            </div>

            {/* Step 4 */}
            <div className="card" style={{ position: 'relative', overflow: 'hidden' }}>
              <div style={{ fontSize: '3rem', fontWeight: 900, color: '#F1F5F9', position: 'absolute', top: '0.5rem', right: '1rem', lineHeight: 1 }}>04</div>
              <div style={{ width: '3rem', height: '3rem', borderRadius: '0.5rem', backgroundColor: '#DCFCE7', color: '#166534', display: 'flex', alignItems: 'center', justifyContent: 'center', marginBottom: '1.25rem' }}>
                <Heart size={22} />
              </div>
              <h3 style={{ fontSize: '1.25rem', marginBottom: '0.5rem' }}>Step 4: Save a Life</h3>
              <p style={{ color: '#64748B', fontSize: '0.9375rem', lineHeight: 1.6 }}>
                Donor arrives at the designated certified trauma center or blood bank, enabling critical life-saving transfusions.
              </p>
            </div>
          </div>
        </div>
      </section>

      {/* Call to Action Section */}
      <section style={{ backgroundColor: '#991B1B', color: '#FFFFFF', padding: '5rem 0', position: 'relative', overflow: 'hidden' }}>
        <div className="container" style={{ textAlign: 'center', maxWidth: '780px', position: 'relative', zIndex: 2 }}>
          <div style={{ width: '4rem', height: '4rem', borderRadius: '50%', backgroundColor: 'rgba(255,255,255,0.15)', display: 'flex', alignItems: 'center', justifyContent: 'center', margin: '0 auto 1.5rem', color: '#FFFFFF' }}>
            <Heart size={32} fill="#FFFFFF" />
          </div>
          <h2 style={{ fontSize: 'clamp(2rem, 4vw, 2.75rem)', fontWeight: 800, color: '#FFFFFF', marginBottom: '1rem', lineHeight: 1.2 }}>
            Become a blood donor and help someone in need.
          </h2>
          <p style={{ fontSize: '1.125rem', color: '#FEE2E2', lineHeight: 1.6, marginBottom: '2.5rem' }}>
            One single donation can save up to three lives. Join our nationwide network of voluntary emergency donors today.
          </p>
          <div style={{ display: 'flex', flexWrap: 'wrap', justifyContent: 'center', gap: '1rem' }}>
            <Link
              to="/register"
              className="btn"
              style={{ backgroundColor: '#FFFFFF', color: '#991B1B', padding: '0.875rem 2rem', fontSize: '1.0625rem', fontWeight: 700, borderRadius: '0.625rem' }}
            >
              Become a Donor
            </Link>
            <Link
              to="/how-it-works"
              className="btn btn-outline"
              style={{ borderColor: 'rgba(255,255,255,0.4)', color: '#FFFFFF', padding: '0.875rem 1.75rem', fontSize: '1.0625rem', borderRadius: '0.625rem' }}
            >
              Learn More
            </Link>
          </div>
        </div>
      </section>
    </div>
  );
};
