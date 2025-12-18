import React from 'react';
import ReactDOM from 'react-dom/client';
import { BrowserRouter, HashRouter } from 'react-router-dom';
import App from './App';
import './index.css';

// Use HashRouter in production (GitHub Pages) and BrowserRouter during development
const RouterWrapper = import.meta.env.PROD ? HashRouter : BrowserRouter;

ReactDOM.createRoot(document.getElementById('root') as HTMLElement).render(
  <React.StrictMode>
    <RouterWrapper>
      <App />
    </RouterWrapper>
  </React.StrictMode>
);