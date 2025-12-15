import React from 'react';
import { Link } from 'react-router-dom';
import styles from './AdminHomePage.module.css';
import AnimatedPage from '../../Components/AnimatedPage/AnimatedPage';
import HeartBackground from '../../Components/HeartBackground/HeartBackground';
import HeaderWithProfile from '../../Components/HeaderWithProfile/HeaderWithProfile'


const AdminHomePage = () => {
    return (
        <AnimatedPage> 
            <div className={styles.pageContainer}>
                <HeartBackground />
                
                <HeaderWithProfile />

                <div className={styles.titleBlock}>
                    <h1 className={styles.title}>
                        Welcome back, <span className={styles.highlight}>Admin!</span>
                    </h1>
                </div>

                <main className={styles.mainContent}>
                    <div className={styles.actionCard}>
                        <Link to="/admin/add-doctor" className={styles.addBtn}>
                            Add new doctor
                        </Link>

                        <Link to="/admin/remove-doctor" className={styles.removeBtn}>
                            Remove doctor
                        </Link>
                    </div>
                </main>
            </div>
        </AnimatedPage>
    );
};

export default AdminHomePage;