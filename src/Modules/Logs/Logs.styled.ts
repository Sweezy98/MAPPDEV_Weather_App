import styled from 'styled-components';

interface Props {

}

export const StyledLogs = styled.div<Props>`
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    user-select: none;
    h1 {
        font-size: 3rem;
    }
`

export const StyledTableContainer = styled.div`
    width: 95%;
    height: calc(100% - 25px);
    padding-top: 25px;
`