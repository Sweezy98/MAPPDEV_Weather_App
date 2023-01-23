import React from 'react'

import  { StyledCheckbox } from './checkbox.styled';

interface Props {
    className?: string;
    state: 0 | 1 | 2;
    onClick?: () => void;
}


export const checkbox: React.FC<Props> = (props: Props) => {
    return (
        <StyledCheckbox state={props.state} onClick={props.onClick} >
            <svg version="1.1" x="0px" y="0px" viewBox="0 0 20.84 19" >
                <path id="box" d="M5,0.5h8c2.49,0,4.5,2.01,4.5,4.5v9c0,2.49-2.01,4.5-4.5,4.5H5c-2.49,0-4.5-2.01-4.5-4.5V5
                    C0.5,2.51,2.51,0.5,5,0.5z"/>
                <path id="checkmark" d="M2.94,8.55l0.02-0.02c0.97-0.97,2.54-0.97,3.52,0l1.76,1.76l8.04-8.04c0.97-0.97,2.54-0.97,3.52,0l0.1,0.1
                    c0.97,0.97,0.97,2.54,0,3.52l-9.82,9.82c-0.97,0.97-2.54,0.97-3.52,0l-3.62-3.62C1.97,11.09,1.97,9.52,2.94,8.55z"/>
            </svg>
        </StyledCheckbox>
    )
}

export default checkbox
