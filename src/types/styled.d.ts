import 'styled-components';

interface Colors {
    border: string;
    accent: string;
    primary: string;
    secondary: string;
    deactivated: string;
    controls: string;
    shadow: any;
    windowControls: {
        default: string;
        close: string;
    }
    statusIndicator: {
        active: string;
        inactive: string;
    }
}

declare module 'styled-components' {
    export interface DefaultTheme {
      colors: Colors;
    }
}