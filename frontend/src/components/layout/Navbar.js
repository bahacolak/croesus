import React, { useState } from 'react';
import { NavLink, Link } from 'react-router-dom';
import './Navbar.css';

const Navbar = () => {
  const [isMenuOpen, setIsMenuOpen] = useState(false);

  const toggleMenu = () => {
    setIsMenuOpen(!isMenuOpen);
  };

  return (
    <nav className="navbar">
      <div className="container">
        <div className="navbar-content">
          <Link to="/" className="navbar-logo">
            <svg className="logo-icon" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
              <path d="M12 2L2 7l10 5 10-5-10-5z"></path>
              <path d="M2 17l10 5 10-5"></path>
              <path d="M2 12l10 5 10-5"></path>
            </svg>
            <span>Croesus</span>
          </Link>

          <button
            className={`navbar-toggle ${isMenuOpen ? 'active' : ''}`}
            onClick={toggleMenu}
            aria-label="Toggle navigation menu"
          >
            <span></span>
            <span></span>
            <span></span>
          </button>

          <ul className={`navbar-menu ${isMenuOpen ? 'active' : ''}`}>
            <li className="navbar-item">
              <NavLink 
                to="/" 
                className={({ isActive }) => isActive ? 'navbar-link active' : 'navbar-link'}
                end
              >
                Home
              </NavLink>
            </li>
            <li className="navbar-item">
              <NavLink 
                to="/crypto" 
                className={({ isActive }) => isActive ? 'navbar-link active' : 'navbar-link'}
              >
                Crypto
              </NavLink>
            </li>
            <li className="navbar-item">
              <NavLink 
                to="/trading" 
                className={({ isActive }) => isActive ? 'navbar-link active' : 'navbar-link'}
              >
                Trading
              </NavLink>
            </li>
            <li className="navbar-item">
              <NavLink 
                to="/portfolio" 
                className={({ isActive }) => isActive ? 'navbar-link active' : 'navbar-link'}
              >
                Portfolio
              </NavLink>
            </li>
            <li className="navbar-item">
              <NavLink 
                to="/about" 
                className={({ isActive }) => isActive ? 'navbar-link active' : 'navbar-link'}
              >
                About
              </NavLink>
            </li>
          </ul>
        </div>
      </div>
    </nav>
  );
};

export default Navbar; 