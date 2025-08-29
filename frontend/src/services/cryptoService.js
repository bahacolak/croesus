import api from './api';

/**
 * Service function to get all cryptocurrencies
 * @returns {Promise} - Promise containing the list of cryptocurrencies
 */
export const getAllCryptoCurrencies = async () => {
  try {
    const response = await api.get('/api/crypto');
    return response.data;
  } catch (error) {
    console.error('Error fetching cryptocurrencies:', error);
    throw error;
  }
};

/**
 * API request to update cryptocurrency data
 * @returns {Promise} - Promise containing the updated list of cryptocurrencies
 */
export const refreshCryptoCurrencies = async () => {
  try {
    const response = await api.post('/api/crypto/refresh');
    return response.data;
  } catch (error) {
    console.error('Error refreshing cryptocurrencies:', error);
    throw error;
  }
};

/**
 * Service to get a specific cryptocurrency by ID
 * @param {string} id - Cryptocurrency ID
 * @returns {Promise} - Promise containing the cryptocurrency information
 */
export const getCryptoCurrencyById = async (id) => {
  try {
    const response = await api.get(`/api/crypto/${id}`);
    return response.data;
  } catch (error) {
    console.error(`Error fetching cryptocurrency with id ${id}:`, error);
    throw error;
  }
}; 