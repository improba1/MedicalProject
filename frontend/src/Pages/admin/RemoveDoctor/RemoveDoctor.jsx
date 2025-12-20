import React from 'react';
import { Link } from 'react-router-dom';
import styles from './RemoveDoctor.module.css';
import AnimatedPage from '../../../Components/AnimatedPage/AnimatedPage';
import HeaderWithoutProfile from '../../../Components/HeaderWithoutProfile/HeaderWithoutProfile'



const RemoveDoctor = () => {
    return (
        <AnimatedPage>
            <div className={styles.pageContainer}>

                <HeaderWithoutProfile />
                
                <main className={styles.mainContent}>
                    <div className={styles.titleBlock}>
                        <h1 className={styles.title}>
                            Remove doctor
                        </h1>
                    </div>
                    <div className={styles.actionCard}>
                        <div className={styles.inputBox}>
                            <input type="text" placeholder="Type doctor's name"></input>
                        </div>
                        <Link to="/admin/remove-doctor" className={styles.removeBtn}>
                            Remove
                        </Link>
                    </div>
                </main>
            </div>
        </AnimatedPage>
    );
};

export default RemoveDoctor;