import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { getAllCryptoCurrencies, refreshCryptoCurrencies } from '../../services/cryptoService';
import './CryptoOverview.css';

const CryptoOverview = () => {
  const [cryptos, setCryptos] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [lastUpdated, setLastUpdated] = useState(null);

  const fetchCryptoData = async () => {
    try {
      setLoading(true);
      setError(null);
      
      const data = await getAllCryptoCurrencies();
      setCryptos(data.slice(0, 8)); // Just show top 8 on home page
      setLastUpdated(new Date());
      setLoading(false);
    } catch (err) {
      console.error('Error fetching crypto data:', err);
      setError('Failed to load cryptocurrency data. Please try again later.');
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchCryptoData();
  }, []);

  const handleRefresh = async () => {
    try {
      setLoading(true);
      
      const data = await refreshCryptoCurrencies();
      setCryptos(data.slice(0, 8));
      setLastUpdated(new Date());
      setLoading(false);
    } catch (err) {
      console.error('Error refreshing crypto data:', err);
      setError('Failed to refresh cryptocurrency data. Please try again later.');
      setLoading(false);
    }
  };

  if (loading && cryptos.length === 0) {
    return (
      <section className="crypto-overview">
        <div className="overview-header">
          <h2 className="section-title">Cryptocurrency Markets</h2>
          <div className="loading-state">Loading cryptocurrency data...</div>
        </div>
      </section>
    );
  }

  if (error && cryptos.length === 0) {
    return (
      <section className="crypto-overview">
        <div className="overview-header">
          <h2 className="section-title">Cryptocurrency Markets</h2>
          <div className="error-state">{error}</div>
          <button className="refresh-button" onClick={handleRefresh}>
            <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
              <path d="M23 4v6h-6"></path>
              <path d="M1 20v-6h6"></path>
              <path d="M3.51 9a9 9 0 0 1 14.85-3.36L23 10"></path>
              <path d="M1 14l4.64 4.36A9 9 0 0 0 20.49 15"></path>
            </svg>
            Try Again
          </button>
        </div>
      </section>
    );
  }

  return (
    <section className="crypto-overview">
      <div className="overview-header">
        <div>
          <h2 className="section-title">Cryptocurrency Markets</h2>
          {lastUpdated && (
            <div className="last-updated">
              Last updated: {lastUpdated.toLocaleString()}
              <button className="refresh-button" onClick={handleRefresh} disabled={loading}>
                <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
                  <path d="M23 4v6h-6"></path>
                  <path d="M1 20v-6h6"></path>
                  <path d="M3.51 9a9 9 0 0 1 14.85-3.36L23 10"></path>
                  <path d="M1 14l4.64 4.36A9 9 0 0 0 20.49 15"></path>
                </svg>
                {loading ? 'Refreshing...' : 'Refresh'}
              </button>
            </div>
          )}
        </div>
        <Link to="/crypto" className="view-all-link">View all cryptocurrencies →</Link>
      </div>
      
      <div className="crypto-grid">
        {cryptos.map(crypto => (
          <div className="crypto-card" key={crypto.symbol}>
            <div className="crypto-header">
              <img 
                src={crypto.imageUrl} 
                alt={crypto.name} 
                className="crypto-logo"
                onError={(e) => {
                  e.target.onerror = null;
                  e.target.src = '/assets/crypto-placeholder.png';
                }}
              />
              <div className="crypto-info">
                <h3 className="crypto-name">{crypto.name}</h3>
                <span className="crypto-symbol">{crypto.symbol}</span>
              </div>
            </div>
            <div className="crypto-price">
              ${crypto.currentPrice.toLocaleString('en-US', {minimumFractionDigits: 2, maximumFractionDigits: 6})}
            </div>
            <div className={`crypto-change ${crypto.priceChangePercent24h >= 0 ? 'up' : 'down'}`}>
              {crypto.priceChangePercent24h >= 0 ? '↑' : '↓'} {Math.abs(crypto.priceChangePercent24h).toFixed(2)}%
            </div>
          </div>
        ))}
      </div>
    </section>
  );
};

export default CryptoOverview; 