import React from 'react';
import ReactDOM from 'react-dom/client';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom'
import { ApolloProvider } from '@apollo/client';
import { ThemeProvider } from 'styled-components';

import client from './graphql/Client';

import Navigation from './components/MainNav/mainNav';

import NotFoundPage from './pages/NotFound/notFound';

import Dashboard from './Modules/Dashboard/Dashboard';
import Users from './Modules/Users/Users';
import Sessions from './Modules/Sessions/Sessions';
import Logs from './Modules/Logs/Logs';
import Settings from './Modules/Settings/Settings';

import defaultTheme from './themes/default.theme';
import GlobalStyle from './globalStyle';

import './index.css';

const root = ReactDOM.createRoot(
  document.getElementById('root') as HTMLElement
);

const App: React.FC = () => {
  return (
    <ThemeProvider theme={defaultTheme}>
      <GlobalStyle />
      <ApolloProvider client={client}>
        <Router basename='/admin'>
          <Navigation />
          <Routes>
            <Route path="/" element={<Navigate to="/dashboard" />} />
            <Route path="/dashboard" element={<Dashboard />} />
            <Route path="/users" element={<Users />} />
            <Route path="/sessions" element={<Sessions />} />
            <Route path="/logs" element={<Logs />} />
            <Route path="/settings" element={<Settings />} />
            <Route path="*" element={<NotFoundPage />} />
          </Routes>
        </Router>
      </ApolloProvider>
    </ThemeProvider>
  );
}

root.render(
  <React.StrictMode>
    <App />
  </React.StrictMode>
);