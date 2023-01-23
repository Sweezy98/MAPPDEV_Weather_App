import styled from 'styled-components';


interface Props {
    columnGrid: string,
    isSelected?: boolean,
}

export const StyledTrow = styled.div<Props>`
    display: grid;
    width: calc(100% - 35px);
    height: 48px;
    border-radius: 10px;
    background-color: white;
    border: ${({ isSelected }) => (isSelected ? "2px" : "1px")} solid ${({ theme, isSelected }) => (isSelected ? theme.colors.accent : theme.colors.border)};
    box-shadow: 1px 1px 5px ${({ theme }) => (theme.colors.shadow)};
    margin-top: ${({ isSelected }) => (isSelected ? "19px" : "20px")};
    margin-bottom: ${({ isSelected }) => (isSelected ? "-1px" : "")};
    padding-left: ${({ isSelected }) => (isSelected ? "4px" : "5px")};
    padding-right: ${({ isSelected }) => (isSelected ? "4px" : "5px")};

    overflow: hidden;

    grid-template-columns: ${({ columnGrid }) => (columnGrid)};

    &:hover {
        border-color: ${({ theme }) => (theme.colors.accent)};
        box-shadow: 1px 1px 5px ${({ theme }) => (theme.colors.accent)};
    }
`