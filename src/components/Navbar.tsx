import React, { useState } from 'react';
import { Link, useNavigate, useLocation } from 'react-router-dom';
import { Heart, Search, Building2, BookOpen, AlertCircle, User, LogOut, Menu, X, ShieldAlert, Sparkles } from 'lucide-react';
import { useApp } from '../context/AppContext';

export const Navbar: React.FC = () => {
  const { currentUser, logout } = useApp();
  const [mobileMenuOpen, setMobileMenuOpen] = useState(false);
  const navigate = useNavigate();
  const location = useLocation();

  const handleLogout = () => {
    logout();
    navigate('/');
    setMobileMenuOpen(false);
  };

  const isActive = (path: string) => location.pathname === path;

  return (
    <header style={{ position: 'sticky', top: 0, zIndex: 50, backgroundColor: '#FFFFFF', borderBottom: '1px solid #E2E8F0', boxShadow: '0 1px 3px rgba(0,0,0,0.05)' }}>
      {/* Top emergency ticker */}
      <div style={{ backgroundColor: '#B71C1C', color: '#FFFFFF', padding: '0.4rem 1rem', fontSize: '0.8125rem', textAlign: 'center', display: 'flex', alignItems: 'center', justifyContent: 'center', gap: '0.5rem' }}>
        <ShieldAlert size={16} />
        <span><strong>CRITICAL NEED:</strong> O-Negative and B-Negative blood units are currently low. Available donors requested to respond immediately.</span>
      </div>

      <div className="container" style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', height: '4.5rem' }}>
        {/* Brand Logo */}
        <Link to="/" style={{ display: 'flex', alignItems: 'center', gap: '0.75rem', textDecoration: 'none' }}>
          <div style={{ width: '2.5rem', height: '2.5rem', borderRadius: '0.625rem', backgroundColor: '#C62828', display: 'flex', alignItems: 'center', justifyContent: 'center', color: '#FFFFFF', boxShadow: '0 4px 10px rgba(198, 40, 40, 0.3)' }}>
            <Heart size={22} fill="#FFFFFF" />
          </div>
          <div>
            <span style={{ fontSize: '1.375rem', fontWeight: 800, color: '#0F172A', letterSpacing: '-0.025em', display: 'block', lineHeight: 1.1 }}>
              Blood<span style={{ color: '#C62828' }}>Bridge</span>
            </span>
            <span style={{ fontSize: '0.6875rem', color: '#64748B', fontWeight: 600, textTransform: 'uppercase', letterSpacing: '0.05em' }}>
              Emergency Blood Network
            </span>
          </div>
        </Link>

        {/* Desktop Nav Links */}
        <nav style={{ display: 'flex', alignItems: 'center', gap: '1.75rem' }} className="desktop-nav">
          <Link
            to="/search-donors"
            style={{
              display: 'flex',
              alignItems: 'center',
              gap: '0.375rem',
              fontSize: '0.9375rem',
              fontWeight: 600,
              color: isActive('/search-donors') ? '#C62828' : '#334155',
              padding: '0.375rem 0.5rem',
              borderRadius: '0.375rem',
              backgroundColor: isActive('/search-donors') ? '#FFEBEE' : 'transparent',
            }}
          >
            <Search size={17} />
            Find Donors
          </Link>

          <Link
            to="/blood-banks"
            style={{
              display: 'flex',
              alignItems: 'center',
              gap: '0.375rem',
              fontSize: '0.9375rem',
              fontWeight: 600,
              color: isActive('/blood-banks') ? '#C62828' : '#334155',
              padding: '0.375rem 0.5rem',
              borderRadius: '0.375rem',
              backgroundColor: isActive('/blood-banks') ? '#FFEBEE' : 'transparent',
            }}
          >
            <Building2 size={17} />
            Blood Banks
          </Link>

          <Link
            to="/how-it-works"
            style={{
              display: 'flex',
              alignItems: 'center',
              gap: '0.375rem',
              fontSize: '0.9375rem',
              fontWeight: 600,
              color: isActive('/how-it-works') ? '#C62828' : '#334155',
              padding: '0.375rem 0.5rem',
              borderRadius: '0.375rem',
              backgroundColor: isActive('/how-it-works') ? '#FFEBEE' : 'transparent',
            }}
          >
            <BookOpen size={17} />
            How It Works
          </Link>

          <Link
            to="/emergency-request"
            style={{
              display: 'flex',
              alignItems: 'center',
              gap: '0.375rem',
              fontSize: '0.875rem',
              fontWeight: 700,
              color: '#DC2626',
              backgroundColor: '#FEE2E2',
              padding: '0.45rem 0.85rem',
              borderRadius: '9999px',
              border: '1px solid #FCA5A5',
            }}
          >
            <AlertCircle size={16} />
            Emergency Request
          </Link>
        </nav>

        {/* Right CTA / User State */}
        <div style={{ display: 'flex', alignItems: 'center', gap: '0.75rem' }}>
          {currentUser ? (
            <div style={{ display: 'flex', alignItems: 'center', gap: '0.75rem' }}>
              <Link
                to={
                  currentUser.role === 'ADMIN'
                    ? '/admin-dashboard'
                    : currentUser.role === 'HOSPITAL'
                    ? '/hospital-dashboard'
                    : '/donor-dashboard'
                }
                className="btn btn-outline"
                style={{ padding: '0.5rem 0.875rem', fontSize: '0.875rem' }}
              >
                <User size={16} />
                <span>{currentUser.fullName} ({currentUser.role})</span>
              </Link>
              <button
                onClick={handleLogout}
                className="btn btn-outline"
                style={{ padding: '0.5rem', color: '#64748B' }}
                title="Log Out"
              >
                <LogOut size={16} />
              </button>
            </div>
          ) : (
            <div style={{ display: 'flex', alignItems: 'center', gap: '0.625rem' }}>
              <Link to="/login" className="btn btn-outline" style={{ padding: '0.5rem 1rem' }}>
                Sign In
              </Link>
              <Link to="/register" className="btn btn-primary" style={{ padding: '0.5rem 1rem' }}>
                Register as Donor
              </Link>
            </div>
          )}

          {/* Mobile menu toggle */}
          <button
            onClick={() => setMobileMenuOpen(!mobileMenuOpen)}
            style={{
              display: 'none',
              background: 'none',
              border: 'none',
              cursor: 'pointer',
              padding: '0.5rem',
              color: '#334155',
            }}
            className="mobile-menu-btn"
          >
            {mobileMenuOpen ? <X size={24} /> : <Menu size={24} />}
          </button>
        </div>
      </div>

      {/* Mobile Menu Dropdown */}
      {mobileMenuOpen && (
        <div style={{ backgroundColor: '#FFFFFF', borderTop: '1px solid #E2E8F0', padding: '1rem 1.25rem', display: 'flex', flexDirection: 'column', gap: '0.75rem' }}>
          <Link
            to="/search-donors"
            onClick={() => setMobileMenuOpen(false)}
            style={{ display: 'flex', alignItems: 'center', gap: '0.5rem', padding: '0.625rem', fontWeight: 600, color: '#1E293B' }}
          >
            <Search size={18} /> Find Donors
          </Link>
          <Link
            to="/blood-banks"
            onClick={() => setMobileMenuOpen(false)}
            style={{ display: 'flex', alignItems: 'center', gap: '0.5rem', padding: '0.625rem', fontWeight: 600, color: '#1E293B' }}
          >
            <Building2 size={18} /> Blood Banks
          </Link>
          <Link
            to="/how-it-works"
            onClick={() => setMobileMenuOpen(false)}
            style={{ display: 'flex', alignItems: 'center', gap: '0.5rem', padding: '0.625rem', fontWeight: 600, color: '#1E293B' }}
          >
            <BookOpen size={18} /> How It Works
          </Link>
          <Link
            to="/emergency-request"
            onClick={() => setMobileMenuOpen(false)}
            style={{ display: 'flex', alignItems: 'center', gap: '0.5rem', padding: '0.625rem', fontWeight: 700, color: '#DC2626', backgroundColor: '#FEE2E2', borderRadius: '0.5rem' }}
          >
            <AlertCircle size={18} /> Broadcast Emergency Request
          </Link>
          <Link
            to="/bizadvisor"
            onClick={() => setMobileMenuOpen(false)}
            style={{ display: 'flex', alignItems: 'center', gap: '0.5rem', padding: '0.625rem', fontWeight: 600, color: '#4338CA' }}
          >
            <Sparkles size={18} /> AI Business Consultant
          </Link>
        </div>
      )}

      <style>{`
        @media (max-width: 900px) {
          .desktop-nav { display: none !important; }
          .mobile-menu-btn { display: block !important; }
        }
      `}</style>
    </header>
  );
};
