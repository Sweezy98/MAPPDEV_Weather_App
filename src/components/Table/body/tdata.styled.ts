import styled from "styled-components";

interface Props {
    
}

export const StyledTData = styled.div<Props>`
    height: 100%;
    margin-left: 8px;
    margin-right: 8px;
    display: flex;
    text-align: center;
    align-items: center;
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
    font-size: 11pt;
    color: ${({ theme }) => (theme.colors.deactivated)};
    p {
        margin: 0;
        user-select: none;
    }
`