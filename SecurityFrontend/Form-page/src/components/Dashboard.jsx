
import React from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';

function Dashboard() {
  const username = localStorage.getItem('username');
  const navigate = useNavigate();

  const handleLogout = async () => {
    await axios.post('/api/logout-success', null, {
      params: { username },
    });
    localStorage.removeItem('username');
    navigate('/');
  };

  return (
    <div>
      <h2>Dashboard</h2>
      <p>Welcome, {username}</p>
      <button onClick={handleLogout}>Logout</button>
    </div>
  );
}

export default Dashboard;
