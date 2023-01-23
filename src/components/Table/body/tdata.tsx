import React from "react";

import { StyledTData } from "./tdata.styled";

interface Props {
    children: React.ReactNode,
}

const TData: React.FC<Props> = (props: Props) => {
    return (
        <StyledTData>
            {props.children}
        </StyledTData>
    );
}

export default TData