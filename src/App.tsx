import React, { useEffect } from 'react';
import { BrowserRouter, Routes, Route, useLocation } from 'react-router-dom';
import { AppProvider } from './context/AppContext';
import { Navbar } from './components/Navbar';
import { Footer } from './components/Footer';

// Pages
import { HomePage } from './pages/HomePage';
import { SearchDonorsPage } from './pages/SearchDonorsPage';
import { BloodBanksPage } from './pages/BloodBanksPage';
import { HowItWorksPage } from './pages/HowItWorksPage';
import { LoginPage } from './pages/LoginPage';
import { RegisterPage } from './pages/RegisterPage';
import { EmergencyRequestPage } from './pages/EmergencyRequestPage';
import { DonorDashboardPage } from './pages/DonorDashboardPage';
import { HospitalDashboardPage } from './pages/HospitalDashboardPage';
import { AdminDashboardPage } from './pages/AdminDashboardPage';
import { BizAdvisorPage } from './pages/BizAdvisorPage';
import { NotFoundPage } from './pages/NotFoundPage';

// Scroll to top on navigation
const ScrollToTop: React.FC = () => {
  const { pathname } = useLocation();
  useEffect(() => {
    window.scrollTo(0, 0);
  }, [pathname]);
  return null;
};

export const App: React.FC = () => {
  return (
    <AppProvider>
      <BrowserRouter>
        <ScrollToTop />
        <div style={{ display: 'flex', flexDirection: 'column', minHeight: '100vh' }}>
          <Navbar />
          <main style={{ flex: 1 }}>
            <Routes>
              <Route path="/" element={<HomePage />} />
              <Route path="/search-donors" element={<SearchDonorsPage />} />
              <Route path="/blood-banks" element={<BloodBanksPage />} />
              <Route path="/how-it-works" element={<HowItWorksPage />} />
              <Route path="/login" element={<LoginPage />} />
              <Route path="/register" element={<RegisterPage />} />
              <Route path="/emergency-request" element={<EmergencyRequestPage />} />
              <Route path="/donor-dashboard" element={<DonorDashboardPage />} />
              <Route path="/hospital-dashboard" element={<HospitalDashboardPage />} />
              <Route path="/admin-dashboard" element={<AdminDashboardPage />} />
              <Route path="/bizadvisor" element={<BizAdvisorPage />} />
              <Route path="*" element={<NotFoundPage />} />
            </Routes>
          </main>
          <Footer />
        </div>
      </BrowserRouter>
    </AppProvider>
  );
};

export default App;
