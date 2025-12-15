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
import Conclusion from './Pages/Conclusion/Conclusion';
import RaportAdded from './Pages/RaportAdded/RaportAdded';
import LabTest from './Pages/LabTest/LabTest';
import CreateReferral from './Pages/CreateReferral/CreateReferral';
import Ai from './Pages/Ai/Ai';
import PatientDetails from './Pages/PatientDetails/PatientDetails';
import AppointmentDetails from './Pages/AppointmentDetails/AppointmentDetails';
import EditRaport from './Pages/EditRaport/EditRaport';
import EditProfile from './Pages/EditProfile/EditProfile';


import AdminHomePage from './Pages/AdminHomePage/AdminHomePage';

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
        <Route path="/my-patients" element={<MyPatients name="patient's name" />} />
        <Route path="/my-patients/:id" element={<PatientDetails />} />
        <Route path="/conclusion" element={<Conclusion />} />
        <Route path="/raport-added" element={<RaportAdded />} />
        <Route path="/lab-test" element={<LabTest />} />
        <Route path="/create-referral" element={<CreateReferral />} />
        <Route path="/ai" element={<Ai />} />
        <Route path="/appointment-details" element={<AppointmentDetails />} />
        <Route path="/edit-raport" element={<EditRaport />} />
        <Route path="/edit-profile" element={<EditProfile />} />
        


        <Route path="/admin" element={<AdminHomePage />} />
        
        
      </Routes>
    </AnimatePresence>
  );
}

export default App;