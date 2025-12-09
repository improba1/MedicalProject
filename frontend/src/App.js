import React from 'react';
import { Routes, Route, Navigate, useLocation } from 'react-router-dom';
import { AnimatePresence } from 'framer-motion';

import LoginForm from './Pages/LoginForm/LoginForm';
import SignUpForm from './Pages/SignUpForm/SignUp';
import WelcomeScreen from './Pages/WelcomeScreen/WelcomeScreen';
import MyProfile from './Pages/MyProfile/MyProfile';
import DoctorHomePage from './Pages/DoctorHomePage/DoctorHomePage';
import NewRaport from './Pages/NewRaport/NewRaport';
import ManageSchedule from './Pages/ManageSchedule/ManageSchedule';
import MyPatients from './Pages/MyPatients/MyPatients';

function App() {
  const location = useLocation();

  return (
    <AnimatePresence mode="wait">
      <Routes location={location} key={location.pathname}>
        
        <Route path="/" element={<WelcomeScreen />} />
        
        <Route path="/login" element={<LoginForm />} />
        <Route path="/signUpForm" element={<SignUpForm />} />
        <Route path="/my-profile" element={<MyProfile />} />
        <Route path="/doc-home-page" element={<DoctorHomePage name="Dr.House" />} />
        <Route path="/new-raport" element={<NewRaport />} />
        <Route path="/manage-schedule" element={<ManageSchedule />} />
        <Route path="/my-patients" element={<MyPatients />} />
        
        
      </Routes>
    </AnimatePresence>
  );
}

export default App;