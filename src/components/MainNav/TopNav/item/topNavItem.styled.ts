import styled from "styled-components";
import { darken } from 'polished';

interface Props {
    isActive: boolean;
}

export const StyledTopNavItem = styled.div<Props>`
    -webkit-app-region: no-drag;
    -webkit-user-drag: none;
    display: flex;
    flex-direction: row;
    align-items: center;
    height: 100%;
    background-color: ${({ isActive, theme }) => (isActive ? theme.colors.primary : "")};
    padding-top: ${({ isActive }) => (isActive ? "1px" : "0px")};
    border-left: ${({ isActive, theme }) => (isActive ? `1px solid ${theme.colors.border}` : "none")};
    border-right: ${({ isActive, theme }) => (isActive ? `1px solid ${theme.colors.border}` : "none")};
    border-bottom: ${({ isActive, theme }) => (isActive ? `1px solid ${theme.colors.primary}` : "none")};
    svg, p {
        user-select: none;
        color: ${({ isActive, theme }) => (isActive ? theme.colors.accent : theme.colors.deactivated)};
    }
    svg {
        height: 18px;
        width: 18px;
        margin-left: ${({ isActive }) => (isActive ? "11px" : "12px")};
        margin-right: 9px;
    }
    p {
        margin-right: ${({ isActive }) => (isActive ? "13px" : "14px")};
        font-size: 1rem;
        overflow: hidden;
    }
    &:hover {
        svg, p {
            color: ${({ theme }) => (theme.colors.accent)};
        }
    }
    &:active {
        svg, p {
            color: ${({ theme }) => (darken(0.10, theme.colors.accent))};
        }
    }
`