import React from 'react';
import './AboutPage.css';

const AboutPage = () => {
  return (
    <div className="about-page">
      <div className="page-header">
        <h1>About Croesus</h1>
        <p>Learn more about our mission, features, and the team behind Croesus</p>
      </div>
      
      <section className="about-section">
        <h2>Our Mission</h2>
        <p>
          Croesus aims to democratize access to cryptocurrency investment tools and information. 
          We believe that everyone should have access to powerful, easy-to-use tools for managing 
          their digital assets, regardless of their level of experience or technical knowledge.
        </p>
        <p>
          Our platform provides real-time data, portfolio tracking, and analytical tools to help 
          you make informed decisions about your cryptocurrency investments.
        </p>
      </section>
      
      <section className="about-section">
        <h2>Platform Features</h2>
        <div className="features-grid">
          <div className="feature-card">
            <h3>Real-Time Market Data</h3>
            <p>
              Access up-to-date information on cryptocurrency prices, market capitalization, 
              volume, and more from trusted sources.
            </p>
          </div>
          
          <div className="feature-card">
            <h3>Portfolio Tracking</h3>
            <p>
              Keep track of your cryptocurrency investments, monitor performance, and analyze 
              your portfolio's diversification.
            </p>
          </div>
          
          <div className="feature-card">
            <h3>Trading Tools</h3>
            <p>
              Use advanced trading features to execute trades, set orders, and manage your 
              trading strategy effectively.
            </p>
          </div>
          
          <div className="feature-card">
            <h3>Educational Resources</h3>
            <p>
              Access guides, tutorials, and resources to learn about cryptocurrencies, blockchain 
              technology, and investment strategies.
            </p>
          </div>
        </div>
      </section>
      
      <section className="about-section">
        <h2>Our Team</h2>
        <p>
          Croesus is developed by a team of passionate cryptocurrency enthusiasts, developers, 
          and financial experts committed to creating the best platform for cryptocurrency 
          investment management.
        </p>
        <p>
          We are constantly working to improve our platform and add new features to better serve 
          our users. If you have any suggestions or feedback, we'd love to hear from you!
        </p>
      </section>
    </div>
  );
};

export default AboutPage; 