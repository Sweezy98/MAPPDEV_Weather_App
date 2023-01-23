import { createGlobalStyle } from 'styled-components';
import { darken } from 'polished';

const GlobalStyle = createGlobalStyle`
    @font-face {
    font-family: 'Regular';
    src: local('Poppins-Regular'), url(./fonts/Poppins-Regular.ttf) format('woff');
    }

    @font-face {
    font-family: 'Bold';
    src: local('Poppins-Bold'), url(./fonts/Poppins-Bold.ttf) format('woff');
    }

    @font-face {
    font-family: 'SemiBold';
    src: local('Poppins-SemiBold'), url(./fonts/Poppins-SemiBold.ttf) format('woff');
    }

    @font-face {
    font-family: 'Segoe Fluent Icons';
    src: local('Segoe Fluent Icons'), url(./fonts/SegoeIcons.ttf) format('opentype');
    }

    html {
        font-family: Regular;
    }

    body {
        margin: 0;
        font-family: Regular;
        background-color: ${({ theme }) => (theme.colors.primary)};
        -webkit-font-smoothing: antialiased;
        -moz-osx-font-smoothing: grayscale;
        -webkit-tap-highlight-color: rgba(0,0,0,0);
        -webkit-tap-highlight-color: transparent;
    }

    p {
        user-select: none;
        pointer-events: none;
        margin: 0;
    }

    img {
        user-select: none;
        pointer-events: none;
    }

    //Scrollbar
    /* width */
    ::-webkit-scrollbar {
        width: 8px;
    }
    
    /* Track */
    ::-webkit-scrollbar-track {
        box-shadow: inset 0 0 3px ${({ theme }) => (theme.colors.border)};
        border: 0.5px solid ${({ theme }) => (theme.colors.border)};
        background-color: ${({ theme }) => (theme.colors.secondary)};
        border-radius: 10px;
        margin-top: 20px;
        margin-bottom: 20px;
    }
    
    /* Handle */
    ::-webkit-scrollbar-thumb {
        box-shadow: inset 0 0 2px ${({ theme }) => (theme.colors.border)};
        background: ${({ theme }) => (theme.colors.accent)};
        border-radius: 10px;
    }
    
    ::-webkit-scrollbar-button {
        display: none;
    }
    
    ::-webkit-scrollbar-thumb:hover {
        background: ${({ theme }) => (darken(0.10, theme.colors.accent))};
    }

    ::-webkit-scrollbar-thumb:active {
        background: ${({ theme }) => (darken(0.20, theme.colors.accent))};
    }
`

export default GlobalStyle