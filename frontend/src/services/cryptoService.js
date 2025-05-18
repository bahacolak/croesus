import api from './api';

/**
 * Tüm kripto paraları getiren servis fonksiyonu
 * @returns {Promise} - Kripto para listesi içeren promise
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
 * Kripto para verilerini güncellemek için API isteği
 * @returns {Promise} - Güncellenen kripto para listesi içeren promise
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
 * Belirli bir kripto parayı ID'ye göre getiren servis
 * @param {string} id - Kripto para ID'si
 * @returns {Promise} - Kripto para bilgisi içeren promise
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