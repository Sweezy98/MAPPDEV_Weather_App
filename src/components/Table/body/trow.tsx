import React from 'react';

import { StyledTrow } from './trow.styled';

interface Props {
    children: React.ReactNode,
    columnGrid: string,
    isSelected: boolean,
    onClick?: (e: any) => void,
    onDoubleClick?: () => void,
}

const TRow: React.FC<Props> = (props: Props) => {
    return (
        // <StyledTrow isSelected={props.isSelected} columnGrid={props.columnGrid} onClick={props.onClick} onDoubleClick={props.onDoubleClick}>
        <StyledTrow isSelected={props.isSelected} columnGrid={props.columnGrid}>
            {props.children}
        </StyledTrow>
    );
}

export default TRow