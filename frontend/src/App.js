import React from 'react';
import { Routes, Route } from 'react-router-dom';
import { AuthProvider } from './context/AuthContext';
import Layout from './components/layout/Layout';
import ProtectedRoute from './components/auth/ProtectedRoute';
import HomePage from './pages/HomePage';
import CryptoPage from './pages/CryptoPage';
import TradingPage from './pages/TradingPage';
import PortfolioPage from './pages/PortfolioPage';
import AboutPage from './pages/AboutPage';
import LoginPage from './pages/LoginPage';
import RegisterPage from './pages/RegisterPage';
import UserProfilePage from './pages/UserProfilePage';
import NotFoundPage from './pages/NotFoundPage';
import './styles/App.css';

function App() {
  return (
    <AuthProvider>
      <div className="app">
        <Routes>
          <Route path="/login" element={<LoginPage />} />
          <Route path="/register" element={<RegisterPage />} />
          
          <Route path="/" element={<Layout />}>
            <Route index element={<HomePage />} />
            <Route path="about" element={<AboutPage />} />
            
            <Route path="crypto" element={
              <ProtectedRoute>
                <CryptoPage />
              </ProtectedRoute>
            } />
            <Route path="trading" element={
              <ProtectedRoute>
                <TradingPage />
              </ProtectedRoute>
            } />
            <Route path="portfolio" element={
              <ProtectedRoute>
                <PortfolioPage />
              </ProtectedRoute>
            } />
            <Route path="profile" element={
              <ProtectedRoute>
                <UserProfilePage />
              </ProtectedRoute>
            } />
            
            <Route path="*" element={<NotFoundPage />} />
          </Route>
        </Routes>
      </div>
    </AuthProvider>
  );
}

export default App; 