import React, { useState } from 'react';
import { Building2, MapPin, Phone, ExternalLink, ShieldCheck, Search, Filter, Droplet, Clock } from 'lucide-react';
import { useApp } from '../context/AppContext';
import { BloodGroup, BloodComponent } from '../types';

export const BloodBanksPage: React.FC = () => {
  const { bloodBanks } = useApp();
  const [searchCity, setSearchCity] = useState('');
  const [selectedGroup, setSelectedGroup] = useState<string>('ALL');
  const [selectedComponent, setSelectedComponent] = useState<string>('ALL');

  const bloodGroups: BloodGroup[] = ['O-', 'O+', 'A-', 'A+', 'B-', 'B+', 'AB-', 'AB+'];
  const components: BloodComponent[] = ['WHOLE_BLOOD', 'RBC', 'PLASMA', 'PLATELETS'];

  const filteredBanks = bloodBanks.filter(bank => {
    if (searchCity.trim()) {
      const q = searchCity.toLowerCase();
      const matchCity = bank.city.toLowerCase().includes(q) || bank.name.toLowerCase().includes(q);
      if (!matchCity) return false;
    }
    return true;
  });

  const getStockStatusColor = (units: number) => {
    if (units === 0) return { bg: '#FEE2E2', text: '#991B1B', label: 'OUT OF STOCK' };
    if (units <= 3) return { bg: '#FEF3C7', text: '#92400E', label: 'CRITICAL (LOW)' };
    return { bg: '#DCFCE7', text: '#166534', label: 'AVAILABLE' };
  };

  return (
    <div style={{ backgroundColor: '#F8FAFC', minHeight: '80vh', padding: '2.5rem 0 4rem' }}>
      <div className="container">
        {/* Header */}
        <div style={{ marginBottom: '2rem' }}>
          <h1 style={{ fontSize: '2.25rem', marginBottom: '0.5rem', color: '#0F172A' }}>
            Blood Banks & Live Inventory
          </h1>
          <p style={{ color: '#64748B', fontSize: '1.0625rem' }}>
            Check verified clinical blood bank supplies, unit availability across whole blood and components, and direct emergency dispatch.
          </p>
        </div>

        {/* Filter controls */}
        <div className="card" style={{ marginBottom: '2rem', padding: '1.25rem 1.5rem' }}>
          <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(220px, 1fr))', gap: '1.25rem' }}>
            <div>
              <label style={{ display: 'block', fontSize: '0.875rem', fontWeight: 600, color: '#334155', marginBottom: '0.5rem' }}>
                Search Blood Bank / City
              </label>
              <div style={{ position: 'relative' }}>
                <Search size={18} color="#94A3B8" style={{ position: 'absolute', left: '0.75rem', top: '50%', transform: 'translateY(-50%)' }} />
                <input
                  type="text"
                  placeholder="e.g. Metro Red Cross or Downtown"
                  value={searchCity}
                  onChange={e => setSearchCity(e.target.value)}
                  style={{ width: '100%', padding: '0.625rem 0.75rem 0.625rem 2.375rem', borderRadius: '0.5rem', border: '1px solid #CBD5E1', fontSize: '0.9375rem', outline: 'none' }}
                />
              </div>
            </div>

            <div>
              <label style={{ display: 'block', fontSize: '0.875rem', fontWeight: 600, color: '#334155', marginBottom: '0.5rem' }}>
                Filter Blood Group Stock
              </label>
              <select
                value={selectedGroup}
                onChange={e => setSelectedGroup(e.target.value)}
                style={{ width: '100%', padding: '0.625rem 0.75rem', borderRadius: '0.5rem', border: '1px solid #CBD5E1', fontSize: '0.9375rem', backgroundColor: '#FFFFFF' }}
              >
                <option value="ALL">All Blood Groups</option>
                {bloodGroups.map(bg => (
                  <option key={bg} value={bg}>{bg}</option>
                ))}
              </select>
            </div>

            <div>
              <label style={{ display: 'block', fontSize: '0.875rem', fontWeight: 600, color: '#334155', marginBottom: '0.5rem' }}>
                Blood Component
              </label>
              <select
                value={selectedComponent}
                onChange={e => setSelectedComponent(e.target.value)}
                style={{ width: '100%', padding: '0.625rem 0.75rem', borderRadius: '0.5rem', border: '1px solid #CBD5E1', fontSize: '0.9375rem', backgroundColor: '#FFFFFF' }}
              >
                <option value="ALL">All Components</option>
                <option value="WHOLE_BLOOD">Whole Blood</option>
                <option value="RBC">Packed Red Blood Cells (RBC)</option>
                <option value="PLASMA">Fresh Frozen Plasma</option>
                <option value="PLATELETS">Platelets</option>
              </select>
            </div>
          </div>
        </div>

        {/* Banks List */}
        <div style={{ display: 'flex', flexDirection: 'column', gap: '2rem' }}>
          {filteredBanks.map(bank => {
            const groupsToDisplay = selectedGroup === 'ALL' ? bloodGroups : [selectedGroup as BloodGroup];

            return (
              <div key={bank.id} className="card" style={{ padding: '1.75rem' }}>
                {/* Header row */}
                <div style={{ display: 'flex', flexWrap: 'wrap', justifyContent: 'space-between', alignItems: 'flex-start', gap: '1rem', marginBottom: '1.5rem', borderBottom: '1px solid #F1F5F9', paddingBottom: '1.25rem' }}>
                  <div>
                    <div style={{ display: 'flex', alignItems: 'center', gap: '0.5rem', marginBottom: '0.375rem' }}>
                      <h2 style={{ fontSize: '1.375rem', color: '#0F172A' }}>{bank.name}</h2>
                      {bank.isVerified && (
                        <span title="FDA / Certified Blood Center">
                          <ShieldCheck size={20} color="#166534" />
                        </span>
                      )}
                    </div>
                    <p style={{ display: 'flex', alignItems: 'center', gap: '0.375rem', fontSize: '0.875rem', color: '#64748B', marginBottom: '0.25rem' }}>
                      <MapPin size={15} /> {bank.address}, {bank.city}, {bank.state}
                    </p>
                    <span style={{ fontSize: '0.75rem', color: '#94A3B8' }}>License: {bank.licenseNumber}</span>
                  </div>

                  <div style={{ display: 'flex', flexWrap: 'wrap', gap: '0.75rem' }}>
                    <a
                      href={`tel:${bank.emergencyHotline}`}
                      className="btn btn-primary"
                      style={{ padding: '0.5rem 1rem', fontSize: '0.875rem' }}
                    >
                      <Phone size={15} /> Hotline: {bank.emergencyHotline}
                    </a>
                    <a
                      href={`https://www.google.com/maps/search/?api=1&query=${bank.latitude},${bank.longitude}`}
                      target="_blank"
                      rel="noopener noreferrer"
                      className="btn btn-outline"
                      style={{ padding: '0.5rem 1rem', fontSize: '0.875rem' }}
                    >
                      <ExternalLink size={15} /> Directions
                    </a>
                  </div>
                </div>

                {/* Stock Table / Matrix */}
                <div>
                  <h3 style={{ fontSize: '1rem', color: '#334155', marginBottom: '1rem', fontWeight: 600, display: 'flex', alignItems: 'center', gap: '0.375rem' }}>
                    <Droplet size={16} color="#C62828" />
                    Live Component Inventory Status
                  </h3>

                  <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(230px, 1fr))', gap: '1rem' }}>
                    {groupsToDisplay.map(bg => {
                      const stockItems = bank.stock[bg] || [];
                      const displayItems = selectedComponent === 'ALL'
                        ? stockItems
                        : stockItems.filter(item => item.component === selectedComponent);

                      return (
                        <div
                          key={bg}
                          style={{
                            border: '1px solid #E2E8F0',
                            borderRadius: '0.5rem',
                            padding: '0.875rem',
                            backgroundColor: '#FFFFFF',
                          }}
                        >
                          <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '0.625rem', borderBottom: '1px solid #F1F5F9', paddingBottom: '0.375rem' }}>
                            <span style={{ fontSize: '1.25rem', fontWeight: 800, color: '#C62828' }}>{bg}</span>
                            <span style={{ fontSize: '0.75rem', color: '#94A3B8' }}>Live</span>
                          </div>

                          <div style={{ display: 'flex', flexDirection: 'column', gap: '0.5rem', fontSize: '0.8125rem' }}>
                            {displayItems.map(item => {
                              const status = getStockStatusColor(item.units);
                              return (
                                <div key={item.component} style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
                                  <span style={{ color: '#475569' }}>
                                    {item.component.replace('_', ' ')}
                                  </span>
                                  <span
                                    style={{
                                      backgroundColor: status.bg,
                                      color: status.text,
                                      padding: '0.15rem 0.45rem',
                                      borderRadius: '0.25rem',
                                      fontWeight: 700,
                                      fontSize: '0.75rem',
                                    }}
                                  >
                                    {item.units} units
                                  </span>
                                </div>
                              );
                            })}
                          </div>
                        </div>
                      );
                    })}
                  </div>
                </div>
              </div>
            );
          })}
        </div>
      </div>
    </div>
  );
};
