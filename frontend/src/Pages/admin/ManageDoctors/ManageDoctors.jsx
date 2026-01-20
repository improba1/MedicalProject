import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import styles from './ManageDoctors.module.css';
import AnimatedPage from '../../../Components/AnimatedPage/AnimatedPage';
import HeaderWithoutProfile from '../../../Components/HeaderWithoutProfile/HeaderWithoutProfile';
import { adminDoctorApi } from '../../../Api/admin/doctorApi';

const ManageDoctors = () => {
    const [doctors, setDoctors] = useState([]);
    const [searchName, setSearchName] = useState('');
    const [loading, setLoading] = useState(false);
    const navigate = useNavigate();

    const fetchDoctors = async () => {
        setLoading(true);
        try {
            const payload = {
                name: searchName || null,
                isActive: null, 
                specialization: null
            };

            const response = await adminDoctorApi.searchDoctors(payload);
            setDoctors(response.data || []);
        } catch (error) {
            console.error("Failed to fetch doctors", error);
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        fetchDoctors();
    }, []);

    const handleHardDelete = async (id, name) => {
        if (window.confirm(`Are you sure you want to PERMANENTLY delete Dr. ${name}? This cannot be undone.`)) {
            try {
                await adminDoctorApi.hardDelete(id);
                fetchDoctors(); 
            } catch (error) {
                alert("Failed to delete doctor.");
            }
        }
    };

    const handleEdit = (doctor) => {
        navigate(`/admin/edit-doctor/${doctor.id}`, { state: { doctor } });
    };

    const handleSearchSubmit = (e) => {
        e.preventDefault();
        fetchDoctors();
    };

    return (
        <AnimatedPage>
            <div className={styles.pageContainer}>
                <HeaderWithoutProfile />
                
                <main className={styles.contentWrapper}>
                    <h1 className={styles.title}>Manage Doctors</h1>

                    <form onSubmit={handleSearchSubmit} className={styles.filterSection}>
                        <div style={{position: 'relative', flex: 1}}>
                            <input 
                                type="text" 
                                placeholder="Search by name..." 
                                className={styles.searchInput}
                                value={searchName}
                                onChange={(e) => setSearchName(e.target.value)}
                            />
                        </div>
                        
                        <button type="submit" className={styles.searchBtn}>
                            <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2.5" strokeLinecap="round" strokeLinejoin="round">
                                <circle cx="11" cy="11" r="8"></circle>
                                <line x1="21" y1="21" x2="16.65" y2="16.65"></line>
                            </svg>
                            Search
                        </button>
                    </form>

                    <div className={styles.doctorsGrid}>
                        {loading ? (
                            <div style={{color:'white', width:'100%', textAlign:'center'}}>Loading...</div>
                        ) : doctors.length > 0 ? (
                            doctors.map((doc) => (
                                <div key={doc.id} className={styles.card}>
                                    
                                    <img 
                                        src={doc.image?.downloadUrl || 'https://via.placeholder.com/150'} 
                                        alt="doc" 
                                        className={styles.avatar}
                                        onError={(e) => {e.target.src='https://via.placeholder.com/150?text=No+Img'}}
                                    />

                                    <div style={{width:'100%'}}>
                                        <div className={styles.cardInfo}>
                                            <h3 className={styles.name}>{doc.firstname} {doc.lastname}</h3>
                                            <div className={styles.specialization}>{doc.specialization}</div>
                                            <div className={styles.details}>
                                                <span>{doc.email}</span>
                                                <span style={{color: '#666', fontSize: '0.8rem'}}>ID: ...{doc.id.slice(-6)}</span>
                                            </div>
                                        </div>

                                        <div className={styles.cardActions}>
                                            <button 
                                                type="button"
                                                onClick={() => handleEdit(doc)}
                                                className={`${styles.actionBtn} ${styles.btnEdit}`}
                                            >
                                                <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2"><path d="M11 4H4a2 2 0 0 0-2 2v14a2 2 0 0 0 2 2h14a2 2 0 0 0 2-2v-7"></path><path d="M18.5 2.5a2.121 2.121 0 0 1 3 3L12 15l-4 1 1-4 9.5-9.5z"></path></svg>
                                                Edit
                                            </button>

                                            <button 
                                                type="button"
                                                onClick={() => handleHardDelete(doc.id, doc.lastname)}
                                                className={`${styles.actionBtn} ${styles.btnDelete}`}
                                            >
                                                <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2"><polyline points="3 6 5 6 21 6"></polyline><path d="M19 6v14a2 2 0 0 1-2 2H7a2 2 0 0 1-2-2V6m3 0V4a2 2 0 0 1 2-2h4a2 2 0 0 1 2 2v2"></path><line x1="10" y1="11" x2="10" y2="17"></line><line x1="14" y1="11" x2="14" y2="17"></line></svg>
                                                Delete
                                            </button>
                                        </div>
                                    </div>
                                </div>
                            ))
                        ) : (
                            <div className={styles.noData}>No doctors found matching your criteria.</div>
                        )}
                    </div>
                </main>
            </div>
        </AnimatedPage>
    );
};

export default ManageDoctors;