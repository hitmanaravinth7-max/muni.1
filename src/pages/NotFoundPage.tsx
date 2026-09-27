import React from 'react';
import { Link } from 'react-router-dom';
import { AlertCircle, Home, Search } from 'lucide-react';

export const NotFoundPage: React.FC = () => {
  return (
    <div style={{ backgroundColor: '#F8FAFC', minHeight: '75vh', display: 'flex', alignItems: 'center', justifyContent: 'center', padding: '3rem 1rem' }}>
      <div style={{ textAlign: 'center', maxWidth: '500px' }}>
        <div style={{ width: '4.5rem', height: '4.5rem', borderRadius: '50%', backgroundColor: '#FEE2E2', color: '#DC2626', display: 'flex', alignItems: 'center', justifyContent: 'center', margin: '0 auto 1.5rem' }}>
          <AlertCircle size={40} />
        </div>
        <h1 style={{ fontSize: '2.5rem', fontWeight: 800, color: '#0F172A', marginBottom: '0.75rem' }}>
          Page Not Found
        </h1>
        <p style={{ color: '#64748B', fontSize: '1rem', lineHeight: '1.6', marginBottom: '2rem' }}>
          The requested URL does not exist or has been relocated within the BloodBridge network.
        </p>
        <div style={{ display: 'flex', justifyContent: 'center', gap: '1rem' }}>
          <Link to="/" className="btn btn-primary">
            <Home size={18} /> Return to Homepage
          </Link>
          <Link to="/search-donors" className="btn btn-outline">
            <Search size={18} /> Search Donors
          </Link>
        </div>
      </div>
    </div>
  );
};
