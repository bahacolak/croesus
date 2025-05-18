import axios from 'axios';

// API Temel yapılandırması
const api = axios.create({
  baseURL: process.env.REACT_APP_API_URL || '', // Boş string varsayılan olarak aynı kaynağı kullanır (proxy sayesinde)
  headers: {
    'Content-Type': 'application/json',
  },
});

// İstek interceptor - her istekte token ekleme gibi yaygın işlemler için
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

// Yanıt interceptor - 401 hataları vb. için
api.interceptors.response.use(
  (response) => response,
  (error) => {
    // Token süresi dolmuşsa oturum kapama gibi işlemler
    if (error.response && error.response.status === 401) {
      localStorage.removeItem('token');
      window.location.href = '/login';
    }
    return Promise.reject(error);
  }
);

export default api; 