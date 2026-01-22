import React, { useState, useEffect } from 'react';
import { useLocation, useNavigate } from 'react-router-dom';
import styles from './PaymentPage.module.css';
import AnimatedPage from '../../../Components/AnimatedPage/AnimatedPage';
import HeaderWithProfile from '../../../Components/HeaderWithoutProfile/HeaderWithoutProfile';
import { cartApi } from '../../../Api/patient/CartApi';
import { paymentApi } from '../../../Api/patient/paymentApi';

const PaymentPage = () => {
    const location = useLocation();
    const navigate = useNavigate();
    const { visitId } = location.state || {};

    const [cartData, setCartData] = useState(null);
    const [loading, setLoading] = useState(true);
    const [processing, setProcessing] = useState(false);

    useEffect(() => {
        if (!visitId) {
            navigate('/patient');
            return;
        }

        const fetchCart = async () => {
            try {
                const response = await cartApi.getCartItems(visitId);
                // response.data is expected to be a List<VisitServiceItemResponse>
                const items = response.data || [];
                const total = items.reduce((sum, item) => sum + (item.priceAtMomentOfPurchase * item.quantity), 0);

                setCartData({
                    services: items,
                    totalPrice: total
                });
            } catch (error) {
                console.error("Error fetching cart:", error);
            } finally {
                setLoading(false);
            }
        };

        fetchCart();
    }, [visitId, navigate]);

    const handleFinalPay = async () => {
        setProcessing(true);
        try {
            const response = await paymentApi.initiatePayment(visitId);
            const stripeUrl = response.data;

            if (stripeUrl) {
                window.location.href = stripeUrl; // Редирект на Stripe
            }

        } catch (error) {
            console.error(error);
            alert("Payment initialization failed.");
        } finally {
            // setProcessing(false); // Не сбрасываем, так как переходим на другую страницу
        }
    };

    if (loading) return <div className={styles.loader}>Loading summary...</div>;

    return (
        <AnimatedPage>
            <div className={styles.pageContainer}>
                <HeaderWithProfile />
                <main className={styles.content}>
                    <div className={styles.paymentCard}>
                        <h1 className={styles.title}>Order Summary</h1>

                        <div className={styles.servicesList}>
                            {cartData?.services?.map((item) => (
                                <div key={item.id} className={styles.serviceItem}>
                                    <div className={styles.info}>
                                        <span className={styles.name}>{item.medicalServiceName || item.serviceName}</span>
                                        <span className={styles.quantity}>Qty: {item.quantity}</span>
                                    </div>
                                    <span className={styles.price}>
                                        ${(item.priceAtMomentOfPurchase * item.quantity).toFixed(2)}
                                    </span>
                                </div>
                            ))}
                        </div>

                        <div className={styles.divider} />

                        <div className={styles.totalSection}>
                            <span>Total to Pay:</span>
                            <span className={styles.totalAmount}>
                                ${cartData?.totalPrice?.toFixed(2)}
                            </span>
                        </div>

                        <button
                            className={styles.payBtn}
                            onClick={handleFinalPay}
                            disabled={processing}
                        >
                            {processing ? "Redirecting..." : "Pay with Stripe"}
                        </button>

                        <button
                            className={styles.backBtn}
                            onClick={() => navigate(-1)}
                            disabled={processing}
                        >
                            Back
                        </button>
                    </div>
                </main>
            </div>
        </AnimatedPage>
    );
};

export default PaymentPage;