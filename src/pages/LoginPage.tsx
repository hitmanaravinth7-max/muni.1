import React, { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { Heart, Mail, Lock, Eye, EyeOff, Shield, ArrowRight, CheckCircle2 } from 'lucide-react';
import { useApp } from '../context/AppContext';

export const LoginPage: React.FC = () => {
  const { login } = useApp();
  const navigate = useNavigate();

  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [showPassword, setShowPassword] = useState(false);
  const [rememberMe, setRememberMe] = useState(true);
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setError('');

    if (!email.trim()) {
      setError('Please provide your registered email address.');
      return;
    }
    if (!password) {
      setError('Please enter your password.');
      return;
    }

    setLoading(true);
    try {
      const success = await login(email);
      if (success) {
        if (email.includes('admin')) {
          navigate('/admin-dashboard');
        } else if (email.includes('hospital')) {
          navigate('/hospital-dashboard');
        } else {
          navigate('/donor-dashboard');
        }
      } else {
        setError('Invalid credentials. Please verify your email.');
      }
    } catch (err) {
      setError('An error occurred during authentication.');
    } finally {
      setLoading(false);
    }
  };

  const handleDemoFill = (demoEmail: string, demoPass: string) => {
    setEmail(demoEmail);
    setPassword(demoPass);
    setError('');
  };

  return (
    <div style={{ backgroundColor: '#F8FAFC', minHeight: '85vh', display: 'flex', alignItems: 'center', justifyContent: 'center', padding: '3rem 1rem' }}>
      <div style={{ maxWidth: '440px', width: '100%' }}>
        {/* Card */}
        <div className="card" style={{ padding: '2.25rem 2rem', boxShadow: '0 10px 25px -5px rgba(0,0,0,0.08)' }}>
          {/* Logo */}
          <div style={{ textAlign: 'center', marginBottom: '1.75rem' }}>
            <div style={{ width: '3.25rem', height: '3.25rem', borderRadius: '0.75rem', backgroundColor: '#C62828', display: 'flex', alignItems: 'center', justifyContent: 'center', color: '#FFFFFF', margin: '0 auto 0.75rem', boxShadow: '0 4px 12px rgba(198, 40, 40, 0.3)' }}>
              <Heart size={26} fill="#FFFFFF" />
            </div>
            <h1 style={{ fontSize: '1.5rem', fontWeight: 800, color: '#0F172A' }}>Sign in to BloodBridge</h1>
            <p style={{ color: '#64748B', fontSize: '0.875rem', marginTop: '0.25rem' }}>
              Access donor matching, inventory portals, and emergency dispatches
            </p>
          </div>

          {error && (
            <div style={{ backgroundColor: '#FEF2F2', border: '1px solid #FECACA', borderRadius: '0.5rem', padding: '0.75rem', color: '#B91C1C', fontSize: '0.875rem', marginBottom: '1.25rem' }}>
              {error}
            </div>
          )}

          <form onSubmit={handleSubmit}>
            {/* Email */}
            <div style={{ marginBottom: '1.25rem' }}>
              <label style={{ display: 'block', fontSize: '0.875rem', fontWeight: 600, color: '#334155', marginBottom: '0.375rem' }}>
                Email Address
              </label>
              <div style={{ position: 'relative' }}>
                <Mail size={18} color="#94A3B8" style={{ position: 'absolute', left: '0.75rem', top: '50%', transform: 'translateY(-50%)' }} />
                <input
                  type="email"
                  placeholder="name@organization.com"
                  value={email}
                  onChange={e => setEmail(e.target.value)}
                  style={{ width: '100%', padding: '0.625rem 0.75rem 0.625rem 2.375rem', borderRadius: '0.5rem', border: '1px solid #CBD5E1', fontSize: '0.9375rem', outline: 'none' }}
                />
              </div>
            </div>

            {/* Password */}
            <div style={{ marginBottom: '1.25rem' }}>
              <label style={{ display: 'block', fontSize: '0.875rem', fontWeight: 600, color: '#334155', marginBottom: '0.375rem' }}>
                Password
              </label>
              <div style={{ position: 'relative' }}>
                <Lock size={18} color="#94A3B8" style={{ position: 'absolute', left: '0.75rem', top: '50%', transform: 'translateY(-50%)' }} />
                <input
                  type={showPassword ? 'text' : 'password'}
                  placeholder="••••••••"
                  value={password}
                  onChange={e => setPassword(e.target.value)}
                  style={{ width: '100%', padding: '0.625rem 2.5rem 0.625rem 2.375rem', borderRadius: '0.5rem', border: '1px solid #CBD5E1', fontSize: '0.9375rem', outline: 'none' }}
                />
                <button
                  type="button"
                  onClick={() => setShowPassword(!showPassword)}
                  style={{ position: 'absolute', right: '0.75rem', top: '50%', transform: 'translateY(-50%)', background: 'none', border: 'none', cursor: 'pointer', color: '#94A3B8' }}
                >
                  {showPassword ? <EyeOff size={18} /> : <Eye size={18} />}
                </button>
              </div>
            </div>

            {/* Remember Me */}
            <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '1.5rem', fontSize: '0.875rem' }}>
              <label style={{ display: 'flex', alignItems: 'center', gap: '0.5rem', color: '#475569', cursor: 'pointer' }}>
                <input
                  type="checkbox"
                  checked={rememberMe}
                  onChange={e => setRememberMe(e.target.checked)}
                  style={{ accentColor: '#C62828' }}
                />
                Remember me
              </label>
              <span style={{ color: '#64748B', cursor: 'pointer' }}>Forgot password?</span>
            </div>

            <button
              type="submit"
              disabled={loading}
              className="btn btn-primary"
              style={{ width: '100%', padding: '0.75rem', fontSize: '1rem', borderRadius: '0.5rem' }}
            >
              {loading ? 'Authenticating...' : 'Sign In'}
            </button>
          </form>

          {/* Quick Demo Logins */}
          <div style={{ marginTop: '1.75rem', borderTop: '1px solid #F1F5F9', paddingTop: '1.25rem' }}>
            <span style={{ display: 'block', fontSize: '0.75rem', fontWeight: 700, color: '#64748B', textTransform: 'uppercase', letterSpacing: '0.05em', marginBottom: '0.75rem', textAlign: 'center' }}>
              One-Click Demo Accounts
            </span>

            <div style={{ display: 'flex', flexDirection: 'column', gap: '0.5rem' }}>
              <button
                type="button"
                onClick={() => handleDemoFill('admin@bloodbridge.org', 'Admin2026!')}
                style={{ padding: '0.5rem 0.75rem', borderRadius: '0.375rem', border: '1px solid #E2E8F0', backgroundColor: '#FFFFFF', textAlign: 'left', fontSize: '0.8125rem', cursor: 'pointer', display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}
              >
                <span>🛡️ <strong>System Admin</strong> (admin@bloodbridge.org)</span>
                <span style={{ color: '#C62828', fontWeight: 600 }}>Fill</span>
              </button>

              <button
                type="button"
                onClick={() => handleDemoFill('hospital@metrohealth.org', 'Hospital2026!')}
                style={{ padding: '0.5rem 0.75rem', borderRadius: '0.375rem', border: '1px solid #E2E8F0', backgroundColor: '#FFFFFF', textAlign: 'left', fontSize: '0.8125rem', cursor: 'pointer', display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}
              >
                <span>🏥 <strong>Hospital Coordinator</strong> (hospital@metrohealth.org)</span>
                <span style={{ color: '#C62828', fontWeight: 600 }}>Fill</span>
              </button>

              <button
                type="button"
                onClick={() => handleDemoFill('alex.chen@example.com', 'Donor2026!')}
                style={{ padding: '0.5rem 0.75rem', borderRadius: '0.375rem', border: '1px solid #E2E8F0', backgroundColor: '#FFFFFF', textAlign: 'left', fontSize: '0.8125rem', cursor: 'pointer', display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}
              >
                <span>❤️ <strong>Volunteer Donor</strong> (alex.chen@example.com)</span>
                <span style={{ color: '#C62828', fontWeight: 600 }}>Fill</span>
              </button>
            </div>
          </div>

          {/* Registration link */}
          <div style={{ textAlign: 'center', marginTop: '1.5rem', fontSize: '0.875rem', color: '#64748B' }}>
            Don't have an account?{' '}
            <Link to="/register" style={{ fontWeight: 600, color: '#C62828' }}>
              Register here
            </Link>
          </div>
        </div>
      </div>
    </div>
  );
};
