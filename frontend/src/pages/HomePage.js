import React from 'react';
import { Link } from 'react-router-dom';
import CryptoOverview from '../components/crypto/CryptoOverview';
import './HomePage.css';

const HomePage = () => {
  return (
    <div className="home-page">
      {/* Hero Section */}
      <section className="hero-section">
        <div className="hero-content">
          <h1>Track, Manage, and Grow Your Digital Assets</h1>
          <p>
            Croesus provides you with powerful tools to track cryptocurrencies,
            manage your portfolio, and optimize your investment strategy.
          </p>
          <div className="hero-buttons">
            <Link to="/crypto" className="btn btn-primary">Explore Cryptocurrencies</Link>
            <Link to="/about" className="btn btn-secondary">Learn More</Link>
          </div>
        </div>
      </section>
      
      {/* Crypto Price Overview */}
      <CryptoOverview />
      
      {/* Features Section */}
      <section className="features-section">
        <h2 className="section-title">Why Choose Croesus</h2>
        <p className="section-subtitle">
          Our platform offers advanced tools to help you succeed in the crypto market
        </p>
        
        <div className="features-grid">
          <div className="feature-card">
            <div className="feature-icon">
              <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
                <path d="M12 2L2 7l10 5 10-5-10-5z"></path>
                <path d="M2 17l10 5 10-5"></path>
                <path d="M2 12l10 5 10-5"></path>
              </svg>
            </div>
            <h3>Real-Time Tracking</h3>
            <p>
              Track cryptocurrency prices in real-time with data from trusted sources.
            </p>
          </div>
          
          <div className="feature-card">
            <div className="feature-icon">
              <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
                <path d="M21 15v4a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2v-4"></path>
                <polyline points="7 10 12 15 17 10"></polyline>
                <line x1="12" y1="15" x2="12" y2="3"></line>
              </svg>
            </div>
            <h3>Portfolio Management</h3>
            <p>
              Keep track of your investments, analyze performance, and manage your portfolio effectively.
            </p>
          </div>
          
          <div className="feature-card">
            <div className="feature-icon">
              <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
                <path d="M12 20v-6M6 20V10M18 20V4"></path>
              </svg>
            </div>
            <h3>Trading Features</h3>
            <p>
              Access to advanced trading features, charts, and analysis tools for better decision making.
            </p>
          </div>
        </div>
      </section>
      
      {/* CTA Section */}
      <section className="cta-section">
        <h2>Ready to Start Your Crypto Journey?</h2>
        <p>
          Join thousands of users who trust Croesus for their cryptocurrency
          tracking and portfolio management needs.
        </p>
        <Link to="/crypto" className="btn btn-primary">
          Get Started Now
        </Link>
      </section>
    </div>
  );
};

export default HomePage; 