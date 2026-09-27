import React from 'react';
import { Link } from 'react-router-dom';
import { Heart, Phone, Mail, MapPin, Shield, Activity, Sparkles } from 'lucide-react';

export const Footer: React.FC = () => {
  return (
    <footer style={{ backgroundColor: '#0F172A', color: '#94A3B8', paddingTop: '4rem', paddingBottom: '2.5rem', borderTop: '1px solid #1E293B' }}>
      <div className="container">
        <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(240px, 1fr))', gap: '2.5rem', marginBottom: '3rem' }}>
          {/* Brand info */}
          <div>
            <div style={{ display: 'flex', alignItems: 'center', gap: '0.75rem', marginBottom: '1.25rem' }}>
              <div style={{ width: '2.25rem', height: '2.25rem', borderRadius: '0.5rem', backgroundColor: '#C62828', display: 'flex', alignItems: 'center', justifyContent: 'center', color: '#FFFFFF' }}>
                <Heart size={20} fill="#FFFFFF" />
              </div>
              <span style={{ fontSize: '1.25rem', fontWeight: 800, color: '#FFFFFF' }}>
                Blood<span style={{ color: '#EF4444' }}>Bridge</span>
              </span>
            </div>
            <p style={{ fontSize: '0.875rem', lineHeight: '1.6', marginBottom: '1.25rem' }}>
              "Connecting Blood Donors With Those Who Need Them." Real-time emergency donor coordination and blood bank availability platform.
            </p>
            <div style={{ display: 'flex', alignItems: 'center', gap: '0.5rem', color: '#EF4444', fontWeight: 700, fontSize: '0.875rem' }}>
              <Activity size={18} />
              <span>National Hotline: 1-800-BLOOD-LINE</span>
            </div>
          </div>

          {/* Quick links */}
          <div>
            <h4 style={{ color: '#FFFFFF', fontSize: '1rem', marginBottom: '1.25rem', fontWeight: 600 }}>Quick Navigation</h4>
            <ul style={{ listStyle: 'none', display: 'flex', flexDirection: 'column', gap: '0.625rem', fontSize: '0.875rem' }}>
              <li><Link to="/search-donors" style={{ color: '#94A3B8' }}>Find Blood Donors</Link></li>
              <li><Link to="/blood-banks" style={{ color: '#94A3B8' }}>Blood Banks & Stock</Link></li>
              <li><Link to="/how-it-works" style={{ color: '#94A3B8' }}>How It Works & Compatibility</Link></li>
              <li><Link to="/emergency-request" style={{ color: '#EF4444', fontWeight: 600 }}>Emergency Blood Request</Link></li>
              <li><Link to="/bizadvisor" style={{ color: '#818CF8', display: 'flex', alignItems: 'center', gap: '0.25rem' }}><Sparkles size={14} /> AI Business Consultant</Link></li>
            </ul>
          </div>

          {/* User Portals */}
          <div>
            <h4 style={{ color: '#FFFFFF', fontSize: '1rem', marginBottom: '1.25rem', fontWeight: 600 }}>Portals & Access</h4>
            <ul style={{ listStyle: 'none', display: 'flex', flexDirection: 'column', gap: '0.625rem', fontSize: '0.875rem' }}>
              <li><Link to="/login" style={{ color: '#94A3B8' }}>Sign In to Portal</Link></li>
              <li><Link to="/register" style={{ color: '#94A3B8' }}>Register as New Donor</Link></li>
              <li><Link to="/donor-dashboard" style={{ color: '#94A3B8' }}>Donor Dashboard</Link></li>
              <li><Link to="/hospital-dashboard" style={{ color: '#94A3B8' }}>Hospital & Blood Bank Portal</Link></li>
              <li><Link to="/admin-dashboard" style={{ color: '#94A3B8' }}>System Administrator</Link></li>
            </ul>
          </div>

          {/* Emergency Notice & Compliance */}
          <div>
            <h4 style={{ color: '#FFFFFF', fontSize: '1rem', marginBottom: '1.25rem', fontWeight: 600 }}>Medical Notice</h4>
            <div style={{ padding: '0.875rem', backgroundColor: '#1E293B', borderRadius: '0.5rem', border: '1px solid #334155', fontSize: '0.8125rem', lineHeight: '1.5' }}>
              <div style={{ display: 'flex', alignItems: 'center', gap: '0.375rem', color: '#F59E0B', fontWeight: 600, marginBottom: '0.375rem' }}>
                <Shield size={16} />
                <span>Life-Threatening Situations</span>
              </div>
              For acute emergencies requiring immediate ambulance dispatch, always call 911 or your local national emergency line first. BloodBridge coordinates donor availability and inventory visibility.
            </div>
          </div>
        </div>

        {/* Bottom copyright */}
        <div style={{ borderTop: '1px solid #1E293B', paddingTop: '1.5rem', display: 'flex', flexWrap: 'wrap', justifyContent: 'space-between', alignItems: 'center', gap: '1rem', fontSize: '0.8125rem' }}>
          <div>
            © {new Date().getFullYear()} BloodBridge System. All rights reserved. HIPAA & Healthcare Protocol Compliant.
          </div>
          <div style={{ display: 'flex', gap: '1.5rem' }}>
            <span style={{ color: '#64748B' }}>Privacy Policy</span>
            <span style={{ color: '#64748B' }}>Terms of Service</span>
            <span style={{ color: '#64748B' }}>Medical Ethics</span>
          </div>
        </div>
      </div>
    </footer>
  );
};
