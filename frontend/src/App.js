import React from 'react';
import { Routes, Route, Navigate, useLocation } from 'react-router-dom';
import { AnimatePresence } from 'framer-motion';

import LoginForm from './Pages/all/LoginForm/LoginForm';
import SignUpForm from './Pages/all/SignUpForm/SignUp';
import WelcomeScreen from './Pages/all/WelcomeScreen/WelcomeScreen';
import MyProfile from './Pages/all/MyProfile/MyProfile';
import DoctorHomePage from './Pages/doctor/DoctorHomePage/DoctorHomePage';
import NewRaport from './Pages/doctor/NewRaport/NewRaport';
import ManageSchedule from './Pages/doctor/ManageSchedule/ManageSchedule';
import MyPatients from './Pages/doctor/MyPatients/MyPatients';
import Conclusion from './Pages/doctor/Conclusion/Conclusion';
import RaportAdded from './Pages/doctor/RaportAdded/RaportAdded';
import LabTest from './Pages/doctor/LabTest/LabTest';
import CreateReferral from './Pages/doctor/CreateReferral/CreateReferral';
import Ai from './Pages/doctor/Ai/Ai';
import PatientDetails from './Pages/doctor/PatientDetails/PatientDetails';
import AppointmentDetails from './Pages/doctor/AppointmentDetails/AppointmentDetails';
import EditRaport from './Pages/doctor/EditRaport/EditRaport';
import EditProfile from './Pages/all/EditProfile/EditProfile';


import AdminHomePage from './Pages/admin/AdminHomePage/AdminHomePage';
import RemoveDoctor from './Pages/admin/RemoveDoctor/RemoveDoctor';
import AddDoctor from './Pages/admin/AddDoctor/AddDoctor';
import PatientHomePage from './Pages/patient/PatientHomePage/PatientHomePage';
import BookAppointment from './Pages/patient/BookAppointment/BookAppointment';

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
        <Route path="/admin/remove-doctor" element={<RemoveDoctor />} />
        <Route path="/admin/add-doctor" element={<AddDoctor />} />



        <Route path="/patient" element={<PatientHomePage />} />
        <Route path="/book-appointment" element={<BookAppointment />} />




        
        
      </Routes>
    </AnimatePresence>
  );
}

export default App;