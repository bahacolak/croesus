import React, { useState, useEffect } from 'react';
import { getAllCryptoCurrencies, refreshCryptoCurrencies } from '../services/cryptoService';
import './CryptoPage.css';

const CryptoPage = () => {
  const [cryptos, setCryptos] = useState([]);
  const [filteredCryptos, setFilteredCryptos] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [lastUpdated, setLastUpdated] = useState(null);
  const [searchTerm, setSearchTerm] = useState('');
  const [sortConfig, setSortConfig] = useState({
    key: 'marketCap',
    direction: 'descending'
  });

  const fetchCryptoData = async () => {
    try {
      setLoading(true);
      setError(null);
      
      const data = await getAllCryptoCurrencies();
      setCryptos(data);
      setFilteredCryptos(data);
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

  useEffect(() => {
    const filtered = cryptos.filter(
      crypto => 
        crypto.name.toLowerCase().includes(searchTerm.toLowerCase()) || 
        crypto.symbol.toLowerCase().includes(searchTerm.toLowerCase())
    );
    
    const sortedData = [...filtered].sort((a, b) => {
      if (a[sortConfig.key] < b[sortConfig.key]) {
        return sortConfig.direction === 'ascending' ? -1 : 1;
      }
      if (a[sortConfig.key] > b[sortConfig.key]) {
        return sortConfig.direction === 'ascending' ? 1 : -1;
      }
      return 0;
    });
    
    setFilteredCryptos(sortedData);
  }, [cryptos, searchTerm, sortConfig]);

  const handleRefresh = async () => {
    try {
      setLoading(true);
      
      const data = await refreshCryptoCurrencies();
      setCryptos(data);
      setLastUpdated(new Date());
      setLoading(false);
    } catch (err) {
      console.error('Error refreshing crypto data:', err);
      setError('Failed to refresh cryptocurrency data. Please try again later.');
      setLoading(false);
    }
  };

  const handleSearch = (e) => {
    setSearchTerm(e.target.value);
  };

  const requestSort = (key) => {
    let direction = 'ascending';
    if (sortConfig.key === key && sortConfig.direction === 'ascending') {
      direction = 'descending';
    }
    setSortConfig({ key, direction });
  };

  const getSortIcon = (columnName) => {
    if (sortConfig.key !== columnName) {
      return '⇵';
    }
    
    return sortConfig.direction === 'ascending' ? '↑' : '↓';
  };

  if (loading && cryptos.length === 0) {
    return (
      <div className="crypto-page">
        <div className="page-header">
          <h1>Cryptocurrency Market</h1>
          <div className="loading-state">Loading cryptocurrency data...</div>
        </div>
      </div>
    );
  }

  if (error && cryptos.length === 0) {
    return (
      <div className="crypto-page">
        <div className="page-header">
          <h1>Cryptocurrency Market</h1>
          <div className="error-state">{error}</div>
          <button className="btn btn-primary" onClick={handleRefresh}>
            Try Again
          </button>
        </div>
      </div>
    );
  }

  return (
    <div className="crypto-page">
      <div className="page-header">
        <h1>Cryptocurrency Market</h1>
        <p>
          Track prices, market cap, and more for the top cryptocurrencies
          {lastUpdated && (
            <span className="last-updated">
              Last updated: {lastUpdated.toLocaleString()}
            </span>
          )}
        </p>
      </div>
      
      <div className="controls">
        <div className="search-container">
          <input
            type="text"
            placeholder="Search cryptocurrencies..."
            value={searchTerm}
            onChange={handleSearch}
            className="search-input"
          />
          <span className="search-results">
            {filteredCryptos.length} of {cryptos.length} cryptocurrencies
          </span>
        </div>
        
        <button 
          className="btn btn-primary refresh-btn" 
          onClick={handleRefresh}
          disabled={loading}
        >
          {loading ? 'Refreshing...' : 'Refresh Prices'}
        </button>
      </div>
      
      <div className="crypto-table-container">
        <table className="crypto-table">
          <thead>
            <tr>
              <th onClick={() => requestSort('name')}>
                Name {getSortIcon('name')}
              </th>
              <th onClick={() => requestSort('currentPrice')}>
                Price {getSortIcon('currentPrice')}
              </th>
              <th onClick={() => requestSort('priceChangePercent24h')}>
                24h Change {getSortIcon('priceChangePercent24h')}
              </th>
              <th onClick={() => requestSort('marketCap')}>
                Market Cap {getSortIcon('marketCap')}
              </th>
            </tr>
          </thead>
          <tbody>
            {filteredCryptos.map(crypto => (
              <tr key={crypto.symbol}>
                <td className="name-cell">
                  <img 
                    src={crypto.imageUrl} 
                    alt={crypto.name} 
                    className="crypto-logo"
                    onError={(e) => {
                      e.target.onerror = null;
                      e.target.src = '/assets/crypto-placeholder.png';
                    }}
                  />
                  <div>
                    <div className="crypto-name">{crypto.name}</div>
                    <div className="crypto-symbol">{crypto.symbol}</div>
                  </div>
                </td>
                <td className="price-cell">
                  ${crypto.currentPrice.toLocaleString('en-US', {minimumFractionDigits: 2, maximumFractionDigits: 6})}
                </td>
                <td className={`change-cell ${crypto.priceChangePercent24h >= 0 ? 'up' : 'down'}`}>
                  {crypto.priceChangePercent24h >= 0 ? '+' : ''}
                  {crypto.priceChangePercent24h.toFixed(2)}%
                </td>
                <td className="market-cap-cell">
                  ${crypto.marketCap.toLocaleString('en-US')}
                </td>
              </tr>
            ))}
          </tbody>
        </table>
        
        {filteredCryptos.length === 0 && (
          <div className="no-results">
            No cryptocurrencies match your search. Try a different term.
          </div>
        )}
      </div>
    </div>
  );
};

export default CryptoPage; 