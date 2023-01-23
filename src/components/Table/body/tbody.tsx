import React from "react";

import { StyledTBody } from "./tbody.styled";

interface Props {
    children: React.ReactNode;
}

const TBody: React.FC<Props> = (props: Props) => {
    return (
        <StyledTBody>
            {props.children}
        </StyledTBody>
    );
}

export default TBody