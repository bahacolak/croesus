import React from 'react';
import './PortfolioPage.css';

const PortfolioPage = () => {
  return (
    <div className="portfolio-page">
      <div className="page-header">
        <h1>Your Cryptocurrency Portfolio</h1>
        <p>Track, analyze, and manage your digital asset investments in one place</p>
      </div>
      
      <div className="portfolio-section">
        <div className="coming-soon">
          <div className="icon">💼</div>
          <h2>Portfolio Features Coming Soon</h2>
          <p>We're building a powerful portfolio tracking system. Check back soon to manage your investments!</p>
          <ul className="features-list">
            <li>Track multiple cryptocurrencies</li>
            <li>Performance analytics</li>
            <li>Investment history</li>
            <li>Profit/loss calculations</li>
            <li>Portfolio diversification analysis</li>
          </ul>
        </div>
      </div>
    </div>
  );
};

export default PortfolioPage; 