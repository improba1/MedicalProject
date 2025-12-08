import React from 'react';
import { Routes, Route, Navigate, useLocation } from 'react-router-dom';
import { AnimatePresence } from 'framer-motion';

import LoginForm from './Pages/LoginForm/LoginForm';
import SignUpForm from './Pages/SignUpForm/SignUp';
import WelcomeScreen from './Pages/WelcomeScreen/WelcomeScreen';
import MyProfile from './Pages/MyProfile/MyProfile';

function App() {
  const location = useLocation();

  return (
    <AnimatePresence mode="wait">
      <Routes location={location} key={location.pathname}>
        
        <Route path="/" element={<WelcomeScreen />} />
        
        <Route path="/login" element={<LoginForm />} />
        <Route path="/signUpForm" element={<SignUpForm />} />
        <Route path="/my-profile" element={<MyProfile />} />
        
      </Routes>
    </AnimatePresence>
  );
}

export default App;