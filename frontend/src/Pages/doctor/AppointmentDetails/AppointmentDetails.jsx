import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import Background from '../../../Components/Background/Background';
import LogOutBtn from '../../../Components/LogOutButton/LogOutButton';
import BackBtn from '../../../Components/BackButton/BackButton'; 
import HealthcareTxt from '../../../Components/HealthcareText/Healthcare';
import styles from './AppointmentDetails.module.css';
import MyProfileBtn from '../../../Components/MyProfileButton/MyProfileButton';
import { reportApi } from '../../../Api/doctor/raportApi';  

//  РАПОРТА НЕ СУЩЕСТВУЕТ ЕСЛИ НЕ БЫЛО ВИЗИТА

const AppointmentDetails = () => {
    const location = useLocation();
    const navigate = useNavigate();
    
    const { visit } = location.state || {}; 

    const [reportData, setReportData] = useState(null);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        const fetchDetails = async () => {
            if (!visit) return;

            try {
                const targetId = visit.id ; 
                console.log(targetId);
                
                const response = await reportApi.getReportDetails(targetId);
                setReportData(response.data);  

            } catch (error) {
                console.error("Error fetching report details:", error);
            } finally {
                setLoading(false);
            }
        };

        fetchDetails();
    }, [visit]);

    if (!visit) {
        return (
            <Background>
                <div style={{color:'white', textAlign:'center', marginTop: '20%'}}>
                    No visit selected. <br/>
                    <button onClick={() => navigate(-1)} style={{marginTop: 10, cursor:'pointer'}}>Go Back</button>
                </div>
            </Background>
        );
    }

    if (loading) {
        return <Background><div style={{color:'white', textAlign:'center', marginTop: '20%'}}>Loading details...</div></Background>;
    }

    if (!reportData) {
        return <Background><div style={{color:'white', textAlign:'center', marginTop: '20%'}}>Report not found or error loading.</div></Background>;
    }

    const formatDate = (dateString) => {
        if (!dateString) return "N/A";
        return new Date(dateString).toLocaleString('en-US', {
            year: 'numeric', month: 'long', day: 'numeric',
            hour: '2-digit', minute: '2-digit'
        });
    };

    return (
        <Background>
            <div className={styles.navHeader}>
                 <div className={styles.navLeft}>
                    <BackBtn />
                    <HealthcareTxt />
                    <MyProfileBtn />
                 </div>
                 <LogOutBtn />
            </div>

            <div className={styles.container}>
                <h1 className={styles.pageTitle}>Appointment Details</h1>
                
                <div className={styles.contentRow}>
                    
                    <div className={styles.infoCard}>
                        <InfoRow label="Appointment Time" value={formatDate(reportData.appointmentTime)} />
                        <InfoRow label="Visit Status" value={reportData.visitStatus} />
                        <InfoRow label="Patient ID" value={reportData.patientId} />
                        
                        <div className={styles.divider} style={{height: 1, background: '#eee', margin: '15px 0'}} />
                        
                        <InfoRow label="Disease" value={reportData.disease || "Not specified"} />
                        <InfoRow label="Symptoms" value={reportData.symptoms || "No symptoms recorded"} />
                        <InfoRow label="Price" value={reportData.price ? `$${reportData.price}` : "0.00"} />
                        
                        <div className={styles.infoRow} style={{flexDirection: 'column', alignItems: 'flex-start', gap: 5}}>
                            <span className={styles.label}>Notes:</span>
                            <p className={styles.value} style={{margin: 0, fontSize: 14, lineHeight: 1.4}}>
                                {reportData.notes || "No notes available."}
                            </p>
                        </div>
                    </div>

                    <Link 
                        to="/edit-raport" 
                        state={{ report: reportData }} 
                        style={{textDecoration: 'none'}}
                    >
                        <button className={styles.editBtn}>
                            Edit Report
                        </button>
                    </Link>
                </div>
            </div>
        </Background>
    );
};

const InfoRow = ({ label, value }) => (
    <div className={styles.infoRow}>
        <span className={styles.label}>{label}: </span>
        <span className={styles.value} style={{marginLeft: 10}}>{value}</span>
    </div>
);

export default AppointmentDetails;