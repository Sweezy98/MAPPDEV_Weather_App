import styled from "styled-components";
import { darken } from 'polished';

interface Props {
    state: 0 | 1 | 2;
}

export const StyledCheckbox = styled.div<Props>`
    display: flex;
    svg {
        height: 20px;
        #box {
            fill: none;
            stroke-width: 1.5px;
            stroke: ${({ state, theme }) => (state == 1 ? theme.colors.accent : theme.colors.deactivated)};
        }
        #checkmark {
            fill: ${({ theme, state }) => (state == 2 ? theme.colors.deactivated : theme.colors.accent)};
            display: ${({ state }) => (state == 0 ? "none" : "block")};
        }
    }
    /* &:hover {
        svg {
            #box {
                stroke: ${({ state, theme }) => (state == 1 ? darken(0.10, theme.colors.accent) : darken(0.10, theme.colors.deactivated))};
            }
            #checkmark {
                fill: ${({ theme, state }) => (state == 2 ? darken(0.10, theme.colors.deactivated) : darken(0.10, theme.colors.accent))};
            }
        }
    } */
    /* &:active {
        svg {
            #box {
                stroke: ${({ state, theme }) => (state == 1 ? darken(0.20, theme.colors.accent) : darken(0.20, theme.colors.deactivated))};
            }
            #checkmark {
                fill: ${({ theme, state }) => (state == 2 ? darken(0.20, theme.colors.deactivated) : darken(0.20, theme.colors.accent))};
            }
        }
    } */
`