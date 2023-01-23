import styled from "styled-components";


interface Props {

}

export const StyledTBody = styled.div<Props>`
    height: calc(100% - 97px);
    padding-left: 10px;
    margin-right: 10px;
    overflow: scroll;
    overflow-x: hidden;
    overflow-y: overlay;
    > * {
        &:last-child {
            margin-bottom: 20px;
        }
    }
`