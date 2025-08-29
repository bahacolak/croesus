import React from 'react';
import { Link } from 'react-router-dom';
import './NotFoundPage.css';

const NotFoundPage = () => {
  return (
    <div className="not-found-page">
      <div className="not-found-content">
        <h1>404</h1>
        <h2>Page Not Found</h2>
        <p>The page you are looking for could not be found or may have been moved.</p>
        <Link to="/" className="btn-home">
          Return to Home Page
        </Link>
      </div>
    </div>
  );
};

export default NotFoundPage; 