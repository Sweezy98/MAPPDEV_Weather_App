import styled from "styled-components";

interface Props {

}

export const StyledTopNav = styled.div<Props>`
    -webkit-app-region: drag;
    width: 100%;
    height: 54px;
    display: flex;
    flex-direction: row;
    align-items: center;
    justify-content: flex-start;
    background-color: ${({ theme }) => (theme.colors.secondary)};
    border-bottom: 1px solid ${({ theme }) => (theme.colors.border)};
    .app_logo {
        -webkit-app-region: no-drag;
        height: 100%;
        display: flex;
        align-items: center;
        justify-content: center;
        padding-left: 11px;
        padding-right: 11px;
        user-select: none;
        -webkit-user-drag: none;
        svg {
            height: 56%;
            user-select: none;
            pointer-events: none;
            color: ${({ theme }) => (theme.colors.accent)};
        }
    }
`