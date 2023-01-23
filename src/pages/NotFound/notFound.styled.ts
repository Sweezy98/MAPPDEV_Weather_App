import styled from 'styled-components';

interface Props {

}

export const StyledNotFoundPage = styled.div<Props>`
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    user-select: none;
    pointer-events: none;
    h1 {
        font-size: 2.5rem;
        font-weight: normal;
        margin: 0;
        margin-bottom: 1rem;
    }
`