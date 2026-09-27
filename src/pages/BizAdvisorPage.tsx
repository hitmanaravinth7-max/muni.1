import React, { useState } from 'react';
import { Sparkles, TrendingUp, Users, DollarSign, Calculator, ArrowRight, Brain, CheckCircle, BarChart3, AlertCircle } from 'lucide-react';

interface BusinessProfile {
  name: string;
  industry: string;
  mrr: number;
  grossMargin: number;
  arpu: number;
  monthlyChurn: number;
  cac: number;
  cashReserve: number;
  monthlyBurn: number;
}

const PRESET_PROFILES: Record<string, BusinessProfile> = {
  apex: {
    name: 'Apex AI Systems',
    industry: 'Enterprise B2B SaaS',
    mrr: 85000,
    grossMargin: 82,
    arpu: 2500,
    monthlyChurn: 1.8,
    cac: 6200,
    cashReserve: 950000,
    monthlyBurn: 65000,
  },
  greenroots: {
    name: 'GreenRoots Organics',
    industry: 'D2C Sustainable Commerce',
    mrr: 42000,
    grossMargin: 58,
    arpu: 85,
    monthlyChurn: 4.5,
    cac: 45,
    cashReserve: 210000,
    monthlyBurn: 28000,
  },
  healthpulse: {
    name: 'HealthPulse Network',
    industry: 'Concierge Healthcare & Diagnostics',
    mrr: 115000,
    grossMargin: 74,
    arpu: 420,
    monthlyChurn: 2.1,
    cac: 750,
    cashReserve: 1400000,
    monthlyBurn: 82000,
  },
};

export const BizAdvisorPage: React.FC = () => {
  const [selectedPreset, setSelectedPreset] = useState<string>('apex');
  const [profile, setProfile] = useState<BusinessProfile>(PRESET_PROFILES.apex);

  // Diagnostics calculations
  const clv = (profile.arpu * (profile.grossMargin / 100)) / (profile.monthlyChurn / 100);
  const ltvCac = profile.cac > 0 ? (clv / profile.cac) : 0;
  const cacPaybackMonths = profile.arpu > 0 ? (profile.cac / (profile.arpu * (profile.grossMargin / 100))) : 0;
  const runwayMonths = profile.monthlyBurn > 0 ? (profile.cashReserve / profile.monthlyBurn) : 0;

  // Advisory Chat State
  const [chatMessages, setChatMessages] = useState<Array<{ role: 'user' | 'assistant'; text: string }>>([
    {
      role: 'assistant',
      text: `Hello! I am your AI Strategic Business Consultant. I've loaded ${profile.name}'s telemetry (${profile.industry}). Your LTV/CAC ratio is ${ltvCac.toFixed(1)}x with ${runwayMonths.toFixed(1)} months of cash runway. What strategic priority shall we tackle today?`,
    },
  ]);
  const [inputQuery, setInputQuery] = useState('');
  const [consulting, setConsulting] = useState(false);

  const handleSelectPreset = (key: string) => {
    setSelectedPreset(key);
    const p = PRESET_PROFILES[key];
    setProfile(p);
    const calcLtvCac = (p.arpu * (p.grossMargin / 100)) / (p.monthlyChurn / 100) / p.cac;
    setChatMessages([
      {
        role: 'assistant',
        text: `Loaded ${p.name} (${p.industry}). Current MRR: $${p.mrr.toLocaleString()}, Gross Margin: ${p.grossMargin}%, LTV/CAC: ${calcLtvCac.toFixed(1)}x. How can I assist with growth, pricing, or retention?`,
      },
    ]);
  };

  const handleSendMessage = (e: React.FormEvent) => {
    e.preventDefault();
    if (!inputQuery.trim()) return;

    const userText = inputQuery;
    setChatMessages(prev => [...prev, { role: 'user', text: userText }]);
    setInputQuery('');
    setConsulting(true);

    setTimeout(() => {
      let response = '';
      const q = userText.toLowerCase();

      if (q.includes('churn') || q.includes('retention')) {
        response = `For ${profile.name}, your churn rate is ${profile.monthlyChurn}%. To reduce this: 1) Implement automated early warning triggers when product engagement drops by >25% in a 14-day window. 2) Conduct executive QBRs for top 20% MRR accounts. 3) Align net revenue retention (NRR) with account expansion tiers. Target: sub-${(profile.monthlyChurn * 0.7).toFixed(1)}% monthly churn.`;
      } else if (q.includes('growth') || q.includes('scale') || q.includes('expand')) {
        response = `Given your LTV/CAC of ${ltvCac.toFixed(1)}x (benchmark is ≥3.0x), ${ltvCac >= 3.0 ? 'you have strong unit economics to aggressively scale acquisition spend by 25-35%' : 'you should focus on optimizing CAC and gross margins before accelerating top-of-funnel spend'}. Prioritize channel diversification and referral loops.`;
      } else if (q.includes('runway') || q.includes('fund') || q.includes('capital') || q.includes('burn')) {
        response = `Current Cash Runway is ${runwayMonths.toFixed(1)} months ($${profile.cashReserve.toLocaleString()} reserves with $${profile.monthlyBurn.toLocaleString()} monthly burn). Recommended action: Begin capital expansion conversations when runway reaches 9-12 months. Extend runway by optimizing vendor SaaS spend and shortening receivables cycles to <30 days.`;
      } else {
        response = `Strategic Analysis for ${profile.name}: Focusing on ${userText}. In the ${profile.industry} sector, sustainable advantage requires clear differentiation (IP/data moats), pricing power (value-based packaging rather than cost-plus), and disciplined payback periods under ${Math.ceil(cacPaybackMonths)} months.`;
      }

      setChatMessages(prev => [...prev, { role: 'assistant', text: response }]);
      setConsulting(false);
    }, 800);
  };

  return (
    <div style={{ backgroundColor: '#F8FAFC', minHeight: '85vh', padding: '2.5rem 0 4rem' }}>
      <div className="container">
        {/* Header */}
        <div style={{ marginBottom: '2rem' }}>
          <div style={{ display: 'inline-flex', alignItems: 'center', gap: '0.375rem', backgroundColor: '#EDE9FE', color: '#6D28D9', padding: '0.25rem 0.75rem', borderRadius: '9999px', fontSize: '0.8125rem', fontWeight: 700, marginBottom: '0.75rem' }}>
            <Sparkles size={15} /> Machine Learning & Strategic AI Consultant
          </div>
          <h1 style={{ fontSize: '2.25rem', color: '#0F172A', fontWeight: 800 }}>
            BizAdvisor AI Strategic Command
          </h1>
          <p style={{ color: '#64748B', fontSize: '1rem' }}>
            Predictive machine learning diagnostics, unit economics modeling, and multi-turn executive strategic advisory.
          </p>
        </div>

        {/* Profile Preset Switcher */}
        <div className="card" style={{ marginBottom: '2rem', padding: '1.25rem 1.5rem' }}>
          <div style={{ display: 'flex', flexWrap: 'wrap', justifyContent: 'space-between', alignItems: 'center', gap: '1rem' }}>
            <div>
              <span style={{ fontSize: '0.875rem', fontWeight: 700, color: '#334155' }}>Select Benchmark Corporate Profile:</span>
            </div>
            <div style={{ display: 'flex', flexWrap: 'wrap', gap: '0.75rem' }}>
              {Object.entries(PRESET_PROFILES).map(([key, item]) => (
                <button
                  key={key}
                  onClick={() => handleSelectPreset(key)}
                  style={{
                    padding: '0.5rem 1rem',
                    borderRadius: '0.5rem',
                    border: '1.5px solid',
                    borderColor: selectedPreset === key ? '#6D28D9' : '#CBD5E1',
                    backgroundColor: selectedPreset === key ? '#EDE9FE' : '#FFFFFF',
                    color: selectedPreset === key ? '#6D28D9' : '#334155',
                    fontWeight: 600,
                    fontSize: '0.875rem',
                    cursor: 'pointer',
                  }}
                >
                  {item.name} ({item.industry})
                </button>
              ))}
            </div>
          </div>
        </div>

        {/* Financial Diagnostics Metrics */}
        <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(220px, 1fr))', gap: '1.25rem', marginBottom: '2.5rem' }}>
          <div className="card">
            <span style={{ fontSize: '0.75rem', color: '#64748B' }}>Customer Lifetime Value (CLV)</span>
            <div style={{ fontSize: '1.75rem', fontWeight: 800, color: '#0F172A' }}>
              ${Math.round(clv).toLocaleString()}
            </div>
            <span style={{ fontSize: '0.75rem', color: '#166534', fontWeight: 600 }}>Gross margin adjusted</span>
          </div>

          <div className="card">
            <span style={{ fontSize: '0.75rem', color: '#64748B' }}>LTV / CAC Ratio</span>
            <div style={{ fontSize: '1.75rem', fontWeight: 800, color: ltvCac >= 3.0 ? '#166534' : '#B45309' }}>
              {ltvCac.toFixed(1)}x
            </div>
            <span style={{ fontSize: '0.75rem', color: ltvCac >= 3.0 ? '#166534' : '#B45309', fontWeight: 600 }}>
              {ltvCac >= 3.0 ? 'Optimal (≥ 3.0x)' : 'Sub-optimal (Target ≥ 3.0x)'}
            </span>
          </div>

          <div className="card">
            <span style={{ fontSize: '0.75rem', color: '#64748B' }}>CAC Payback Period</span>
            <div style={{ fontSize: '1.75rem', fontWeight: 800, color: '#0F172A' }}>
              {cacPaybackMonths.toFixed(1)} mo
            </div>
            <span style={{ fontSize: '0.75rem', color: '#64748B' }}>Months to recoup acquisition cost</span>
          </div>

          <div className="card">
            <span style={{ fontSize: '0.75rem', color: '#64748B' }}>Cash Runway</span>
            <div style={{ fontSize: '1.75rem', fontWeight: 800, color: runwayMonths >= 12 ? '#166534' : '#B91C1C' }}>
              {runwayMonths.toFixed(1)} mo
            </div>
            <span style={{ fontSize: '0.75rem', color: '#64748B' }}>Based on net burn</span>
          </div>
        </div>

        {/* AI Strategic Consultation Terminal */}
        <div className="card" style={{ padding: '2rem' }}>
          <div style={{ display: 'flex', alignItems: 'center', gap: '0.75rem', marginBottom: '1.5rem', borderBottom: '1px solid #E2E8F0', paddingBottom: '1rem' }}>
            <div style={{ width: '2.5rem', height: '2.5rem', borderRadius: '0.5rem', backgroundColor: '#EDE9FE', color: '#6D28D9', display: 'flex', alignItems: 'center', justifyContent: 'center' }}>
              <Brain size={22} />
            </div>
            <div>
              <h2 style={{ fontSize: '1.25rem', color: '#0F172A' }}>Executive Strategic AI Consultant</h2>
              <p style={{ color: '#64748B', fontSize: '0.8125rem' }}>Trained in competitive strategy, unit economics optimization, and go-to-market scaling</p>
            </div>
          </div>

          {/* Conversation history */}
          <div style={{ display: 'flex', flexDirection: 'column', gap: '1rem', minHeight: '280px', maxHeight: '420px', overflowY: 'auto', marginBottom: '1.5rem', paddingRight: '0.5rem' }}>
            {chatMessages.map((msg, idx) => (
              <div
                key={idx}
                style={{
                  display: 'flex',
                  justifyContent: msg.role === 'user' ? 'flex-end' : 'flex-start',
                }}
              >
                <div
                  style={{
                    maxWidth: '80%',
                    padding: '0.875rem 1.25rem',
                    borderRadius: '0.75rem',
                    fontSize: '0.9375rem',
                    lineHeight: '1.6',
                    backgroundColor: msg.role === 'user' ? '#6D28D9' : '#F1F5F9',
                    color: msg.role === 'user' ? '#FFFFFF' : '#1E293B',
                    borderTopRightRadius: msg.role === 'user' ? '0.125rem' : '0.75rem',
                    borderTopLeftRadius: msg.role === 'assistant' ? '0.125rem' : '0.75rem',
                  }}
                >
                  {msg.text}
                </div>
              </div>
            ))}
            {consulting && (
              <div style={{ display: 'flex', justifyContent: 'flex-start' }}>
                <div style={{ backgroundColor: '#F1F5F9', color: '#64748B', padding: '0.75rem 1rem', borderRadius: '0.75rem', fontSize: '0.875rem', fontStyle: 'italic' }}>
                  Synthesizing strategy with predictive models...
                </div>
              </div>
            )}
          </div>

          {/* Input form */}
          <form onSubmit={handleSendMessage} style={{ display: 'flex', gap: '0.75rem' }}>
            <input
              type="text"
              placeholder="Ask about retention strategy, pricing restructuring, runway extensions, or sales hiring..."
              value={inputQuery}
              onChange={e => setInputQuery(e.target.value)}
              style={{ flex: 1, padding: '0.75rem 1rem', borderRadius: '0.5rem', border: '1px solid #CBD5E1', fontSize: '0.9375rem', outline: 'none' }}
            />
            <button
              type="submit"
              disabled={consulting}
              className="btn btn-primary"
              style={{ backgroundColor: '#6D28D9', padding: '0.75rem 1.5rem' }}
            >
              Ask Advisor
            </button>
          </form>
        </div>
      </div>
    </div>
  );
};
