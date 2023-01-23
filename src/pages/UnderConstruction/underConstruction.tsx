import React from 'react';
import { useLocation } from 'react-router-dom';

import { StyledUnderConstructionPage } from './underConstruction.styled';

interface Props {

}

export const UnderConstructionPage: React.FC<Props> = () => {
    const {pathname} = useLocation();
    return (
        <StyledUnderConstructionPage className="content">
            <h1>Dieses Feature ist noch nicht verfügbar!</h1>
            <p>Das Feature "{pathname}" ist noch in Arbeit</p>
        </StyledUnderConstructionPage>
    )
}

export default UnderConstructionPage