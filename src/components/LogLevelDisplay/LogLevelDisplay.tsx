import React from 'react';

import { StyledLogLevelDisplay } from './LogLevelDisplay.styled';


interface Props {
    loglevel: string | null;
}

export const mainNav: React.FC<Props> = (props: Props) => {
    return (            
        <StyledLogLevelDisplay logLevel={props.loglevel}>
            <p>{props.loglevel}</p>
        </StyledLogLevelDisplay>
    )
}

export default mainNav;