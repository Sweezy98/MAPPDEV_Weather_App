import React from "react";

import { StyledTHeader, StyledTHeaderTop, StyledTHeaderBottom } from "./theader.styled";
import Checkbox from '../../Checkbox/checkbox';


interface Props {
    children: React.ReactNode,
    entries: number,
    entities: string,
    selectionState: 0 | 1 | 2,
    selected: number,
    toggleSelectAll: () => void,
}

const THeader: React.FC<Props> = (props: Props) => {
    return (
        <StyledTHeader>
            <StyledTHeaderTop>
                <div className="count" >
                    <p className="entry_counter" >{props.entries + " " + props.entities}</p>
                </div>
            </StyledTHeaderTop>
            <StyledTHeaderBottom>
                {props.children}
            </StyledTHeaderBottom>
        </StyledTHeader>
    );
}

export default THeader