import React from 'react';
import { Link } from 'react-router-dom';
import './Footer.css';

const Footer = () => {
  const currentYear = new Date().getFullYear();
  
  return (
    <footer className="footer">
      <div className="container">
        <div className="footer-content">
          <div className="footer-section">
            <h3 className="footer-heading">Croesus</h3>
            <p className="footer-description">
              Digital Asset Account and Investment Platform. Track, manage, and optimize 
              your crypto investments with our powerful tools.
            </p>
          </div>
          
          <div className="footer-section">
            <h3 className="footer-heading">Quick Links</h3>
            <ul className="footer-links">
              <li>
                <Link to="/">Home</Link>
              </li>
              <li>
                <Link to="/crypto">Cryptocurrency</Link>
              </li>
              <li>
                <Link to="/trading">Trading</Link>
              </li>
              <li>
                <Link to="/portfolio">Portfolio</Link>
              </li>
            </ul>
          </div>
          
          <div className="footer-section">
            <h3 className="footer-heading">Resources</h3>
            <ul className="footer-links">
              <li>
                <a href="https://www.investopedia.com/cryptocurrency-4427699" target="_blank" rel="noopener noreferrer">
                  Crypto Education
                </a>
              </li>
              <li>
                <a href="https://coinmarketcap.com/" target="_blank" rel="noopener noreferrer">
                  Market Cap Rankings
                </a>
              </li>
              <li>
                <a href="https://www.coingecko.com/" target="_blank" rel="noopener noreferrer">
                  CoinGecko
                </a>
              </li>
            </ul>
          </div>
          
          <div className="footer-section">
            <h3 className="footer-heading">Contact</h3>
            <ul className="footer-contact">
              <li>support@croesus.com</li>
              <li>+1 (123) 456-7890</li>
              <li className="social-links">
                <a href="https://twitter.com" target="_blank" rel="noopener noreferrer" aria-label="Twitter">
                  Twitter
                </a>
                <a href="https://linkedin.com" target="_blank" rel="noopener noreferrer" aria-label="LinkedIn">
                  LinkedIn
                </a>
              </li>
            </ul>
          </div>
        </div>
        
        <div className="footer-bottom">
          <p>&copy; {currentYear} Croesus. All rights reserved.</p>
        </div>
      </div>
    </footer>
  );
};

export default Footer; 