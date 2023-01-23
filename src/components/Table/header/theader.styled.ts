import styled from "styled-components";

interface Props {

}

export const StyledTHeader = styled.div<Props>`
    border-radius: 10px;
    background-color: white;
    border: 1px solid ${({ theme }) => (theme.colors.border)};
    box-shadow: 1px 1px 5px ${({ theme }) => (theme.colors.shadow)};
`

export const StyledTHeaderTop = styled.div`
    height: 48px;
    border-bottom: 1px solid ${({ theme }) => (theme.colors.border)};
    display: flex;
    justify-content: right;
    align-items: center;
    text-align: center;
    padding-left: 23px;
    .select {
        height: 100%;
        display: flex;
        align-items: center;
        text-align: center;
        .select_counter {
            margin: 0;
            user-select: none;
            font-size: 8pt;
            margin-left: 5px;
            margin-top: 1px;
            color: ${({ theme }) => (theme.colors.deactivated)};
        }
    }
    .count {
        height: 100%;
        display: flex;
        align-items: center;
        text-align: center;
        .entry_counter {
            margin: 0;
            user-select: none;
            font-size: 11pt;
            margin-right: 15px;
            color: ${({ theme }) => (theme.colors.accent)};
        }
    }
`

export const StyledTHeaderBottom = styled.div`
    display: flex;
    flex-direction: column;
    height: 100%;
    padding-left: 15px;
    padding-right: 38px;
`