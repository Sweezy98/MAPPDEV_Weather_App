import styled from 'styled-components';

interface Props {

}

export const StyledDashboard = styled.div<Props>`
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    user-select: none;
    h1 {
        font-size: 3rem;
    }
`