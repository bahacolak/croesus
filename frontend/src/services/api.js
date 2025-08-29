import axios from 'axios';

// API base configuration
const api = axios.create({
  baseURL: process.env.REACT_APP_API_URL || '', // Empty string default uses the same origin (thanks to proxy)
  headers: {
    'Content-Type': 'application/json',
  },
});

// Request interceptor - for common operations like adding token to every request
api.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('token');
    if (token) {
      config.headers['Authorization'] = `Bearer ${token}`;
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

// Response interceptor - for handling 401 errors etc.
api.interceptors.response.use(
  (response) => response,
  (error) => {
    // Operations like logout when token expires
    if (error.response && error.response.status === 401) {
      localStorage.removeItem('token');
      window.location.href = '/login';
    }
    return Promise.reject(error);
  }
);

export default api; 