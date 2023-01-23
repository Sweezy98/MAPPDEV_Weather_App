import { DefaultTheme } from 'styled-components';
import { rgba } from 'polished';

const theme: DefaultTheme = {
    colors: {
        border: '#d4dadf',
        accent: '#2090E8',
        primary: '#eff4f9',
        secondary: '#ffffff',
        deactivated: '#7c7c7c',
        controls: '#ffffff',
        shadow: rgba(0, 0, 0, 0.1),
        windowControls: {
            default: "#f5f5f5",
            close: '#c42b1c'
        },
        statusIndicator: {
            active: '#00cf00',
            inactive: '#ff0000'
        }
    }
}

export default theme