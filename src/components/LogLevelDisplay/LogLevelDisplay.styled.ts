import styled from 'styled-components';

interface Props {
    logLevel: string | null;
}

export const StyledLogLevelDisplay = styled.div<Props>`
    width: 100%;
    height: 25px;
    border-radius: 30px;
    display: flex;
    padding-left: 12px;
    padding-right: 12px;
    align-items: center;
    text-align: center;
    justify-content: center;
    background-color: ${({ logLevel }) => (
        // INFO -> green
        logLevel === 'INFO' ? '#00cf00' :
        // ERROR -> red
        logLevel === 'ERROR' ? '#ff0000' :
        // DEBUG -> orange
        logLevel === 'DEBUG' ? '#ff8000' :
        // WARNING -> yellow
        logLevel === 'WARNING' ? '#edd000' :
        // ALERT -> purple
        logLevel === 'ALERT' ? '#ff00ff' :
        // CRITICAL -> red
        logLevel === 'CRITICAL' ? '#ff0000' :
        // STARTUP -> blue
        logLevel === 'STARTUP' ? '#2090E8' :
        // DEFAULT (else) -> grey
        '#808080'
    )};
    box-shadow: 1px 1px 5px ${({ theme }) => (theme.colors.shadow)};
    p {
        user-select: none;
        margin: 0;
        font-family: SemiBold;
        color: #ffffff;
    }
`