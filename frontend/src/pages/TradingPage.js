import React from 'react';
import './TradingPage.css';

const TradingPage = () => {
  return (
    <div className="trading-page">
      <div className="page-header">
        <h1>Cryptocurrency Trading</h1>
        <p>Access advanced trading tools and execute trades with real-time market data</p>
      </div>
      
      <div className="trading-section">
        <div className="coming-soon">
          <div className="icon">🚀</div>
          <h2>Trading Features Coming Soon</h2>
          <p>We're working hard to bring you advanced trading features. Check back soon!</p>
          <ul className="features-list">
            <li>Real-time order book</li>
            <li>Advanced charting tools</li>
            <li>Multiple order types</li>
            <li>Trading history and analytics</li>
            <li>API integration with exchanges</li>
          </ul>
        </div>
      </div>
    </div>
  );
};

export default TradingPage; 