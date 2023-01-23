import styled from "styled-components";

interface Props {
    columnGrid: string,
}

export const StyledTHeaderGroup = styled.div<Props>`
    height: 48px;
    width: 100%;
    align-content: center;
    display: grid;
    grid-template-columns: ${({ columnGrid }) => (columnGrid)};
`