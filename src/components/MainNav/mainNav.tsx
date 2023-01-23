import React from 'react';

import { StyledNav } from './mainNav.styled';
import TopNav from './TopNav/topNav';


interface Props {

}

export const mainNav: React.FC<Props> = (props: Props) => {
    return (            
        <StyledNav>
            <TopNav/>
        </StyledNav>
    )
}

export default mainNav;