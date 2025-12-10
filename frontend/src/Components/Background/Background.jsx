import background from '../../Assets/background.png';
import styles from './Background.module.css';

const Background = ({children}) => {
    return(
        <div>
            <img src={background} className={styles.background}></img>
            <div alt="background" className={styles.backgroundBlack}></div>
            <div className={styles.content}>{children}</div>
        </div>
    )
}

export default Background