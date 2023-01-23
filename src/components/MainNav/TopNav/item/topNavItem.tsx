import React from 'react';
import { useNavigate, useLocation } from 'react-router-dom';

import { StyledTopNavItem } from './topNavItem.styled';

interface Props {
    icon: JSX.Element;
    text: string;
    link: string;
}

export const TopNavItem: React.FC<Props> = (props: Props) => {
    const navigate = useNavigate();
    const location = useLocation();
    return (
        <StyledTopNavItem isActive={location.pathname === props.link} onClick={() => navigate(props.link)}>
            {props.icon}
            <p>{props.text}</p>
        </StyledTopNavItem>
    )
}

export default TopNavItem