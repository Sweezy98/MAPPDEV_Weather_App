import styled from "styled-components";

interface Props {
    isSortable: boolean,
}

export const StyledTHead = styled.div<Props>`
    display: flex;
    flex-direction: row;
    align-items: center;
    font-family: "SemiBold";
    color: ${({ theme }) => (theme.colors.deactivated)};
    margin-left: 8px;
    margin-right: 8px;
    font-size: 11pt;
    svg {
        margin-left: 3px;
    }
    &:hover {
        color: ${({ isSortable, theme }) => (isSortable ? theme.colors.accent : theme.colors.deactivated)};
    }
`