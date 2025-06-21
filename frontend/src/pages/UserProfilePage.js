import React, { useState, useEffect } from 'react';
import { useAuth } from '../context/AuthContext';
import api from '../services/api';
import './UserProfilePage.css';

const UserProfilePage = () => {
  const { user, logout } = useAuth();
  const [userStats, setUserStats] = useState({
    totalBalance: 0,
    totalAssets: 0,
    totalTrades: 0,
    joinDate: null
  });
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
    fetchUserStats();
  }, []);

  const fetchUserStats = async () => {
    try {
      setLoading(true);
      
      const walletResponse = await api.get('/wallet-service/api/wallet');
      const portfolioResponse = await api.get('/portfolio-service/api/portfolio');
      
      setUserStats({
        totalBalance: walletResponse.data?.balance || 0,
        totalAssets: portfolioResponse.data?.assets?.length || 0,
        totalTrades: 0,
        joinDate: user?.createdAt
      });
      
    } catch (error) {
      console.error('Error fetching user stats:', error);
      setError('Error fetching user stats.');
    } finally {
      setLoading(false);
    }
  };

  const formatDate = (dateString) => {
    if (!dateString) return 'Unknown';
    return new Date(dateString).toLocaleDateString('en-US');
  };

  const formatCurrency = (amount) => {
    return new Intl.NumberFormat('en-US', {
      style: 'currency',
      currency: 'USD'
    }).format(amount);
  };

  if (loading) {
    return (
      <div className="loading-container">
        <div className="loading-spinner"></div>
        <p>Loading profile information...</p>
      </div>
    );
  }

  return (
    <div className="profile-page">
      <div className="profile-container">
        <div className="profile-header">
          <div className="profile-avatar">
            <div className="avatar-circle">
              {user?.firstName && user?.lastName 
                ? `${user.firstName.charAt(0)}${user.lastName.charAt(0)}`.toUpperCase()
                : user?.username?.charAt(0).toUpperCase() || 'U'}
            </div>
          </div>
          <div className="profile-info">
            <h1>{user?.firstName && user?.lastName ? `${user.firstName} ${user.lastName}` : user?.username || 'User'}</h1>
            <p className="profile-email">{user?.email}</p>
            <p className="profile-join-date">
              Join Date: {formatDate(userStats.joinDate)}
            </p>
          </div>
        </div>

        {error && <div className="error-message">{error}</div>}

        <div className="profile-stats">
          <div className="stats-grid">
            <div className="stat-card">
              <div className="stat-icon">💰</div>
              <div className="stat-info">
                <h3>Total Balance</h3>
                <p className="stat-value">{formatCurrency(userStats.totalBalance)}</p>
              </div>
            </div>
            
            <div className="stat-card">
              <div className="stat-icon">📊</div>
              <div className="stat-info">
                <h3>Total Assets</h3>
                <p className="stat-value">{userStats.totalAssets}</p>
              </div>
            </div>
            
            <div className="stat-card">
              <div className="stat-icon">🔄</div>
              <div className="stat-info">
                <h3>Total Trades</h3>
                <p className="stat-value">{userStats.totalTrades}</p>
              </div>
            </div>
          </div>
        </div>

        <div className="profile-actions">
          <div className="action-card">
            <h3>Account Actions</h3>
            <div className="action-buttons">
              <button className="btn-secondary" onClick={() => fetchUserStats()}>
                🔄 Refresh Information
              </button>
              <button className="btn-danger" onClick={logout}>
                🚪 Logout
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default UserProfilePage; 